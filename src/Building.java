import java.util.ArrayList;


public class Building extends AbstractBuilding{
	public Object lock; //used by elevators 
	public Elevator[] myFloors; //just an array of null pointers to elevators which we use to return the right 
	                             //elevator in callUP and callDown
	public Elevator[] myElevators; //actual array of elevators
	

	private EventBarrier [] thresholdsUp; //one for each floor
	private EventBarrier [] thresholdsDown;//one for each floor
	
	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		thresholdsUp = new EventBarrier[numFloors];
		for (int i = 0; i<numFloors; i++){
			thresholdsUp[i] = new EventBarrier();
		}
		thresholdsDown = new EventBarrier[numFloors];
		for (int i = 0; i<numFloors; i++){
			thresholdsDown[i] = new EventBarrier();
		}
	}
	public Building(int numFloors, int numElevators, int maxThreshold){
		super(numFloors, numElevators);
		lock = new Object();
		
		thresholdsUp = new EventBarrier[numFloors];
		for (int i = 0; i<numFloors; i++){
			thresholdsUp[i] = new EventBarrier();
		}
		thresholdsDown = new EventBarrier[numFloors];
		for (int i = 0; i<numFloors; i++){
			thresholdsDown[i] = new EventBarrier();
		}
		myElevators = new Elevator[numElevators];
		myFloors = new Elevator[numFloors];
		for(int i = 0; i<numElevators; i++){
			myElevators[i] = new Elevator(numFloors, i, maxThreshold, this);
		}
		for(int i = 0; i<numElevators; i++){
			(new Thread(myElevators[i])).start();
		}
	}

	//a waiter comes, notifies the building he's there, and blocks at the event barrier.
	public AbstractElevator CallUp(int fromFloor) {
		thresholdsUp[fromFloor-1].arrive();//arrive on the building's copy of the eventbarriers
		//once arrive returns, that means the elevator must have called allowRidersOn and 
		//called raise() on the event barrier associated with fromFloor. This also means that
		//myFloors[floor] is now a pointer to this specific elevator since when an elevator visits
		//a floor and calls allowRidersOn, we set myFloors[floor] = e.
		return myFloors[fromFloor-1];
	}
	
	
	
	//a waiter comes, notifies the building he's there, and blocks at the event barrier.
	public AbstractElevator CallDown(int fromFloor) {
		//same thing as CallDown, but on a different set of eventBarriers so we can differentiate
		thresholdsDown[fromFloor-1].arrive();
		return myFloors[fromFloor-1];
	}
	
	
	
	
	//called by an elevator to let folks on in the direction it's going.
	public void allowRidersOn(int floor, boolean Up, Elevator e){
		//for debugging purposes
		System.out.println("people waiting to go UP: " + thresholdsUp[floor-1].waiters());
		System.out.println("people waiting to go DOWN: " + thresholdsDown[floor-1].waiters());
		
		myFloors[floor-1] = e;//this line of code is critical to ensuring the CallUp and CallDown
		//actually return the right elevator. 
		
		if (Up){
			thresholdsUp[floor-1].raise();
		}
		else{
			thresholdsDown[floor-1].raise();
		}
	}
		
	//are there people waiting to go up on floor?
	public boolean hasUpWaiters(int floor){
		if (thresholdsUp[floor-1].waiters()>0){
			return true;
		}
		return false;
	}
	
	
	//are there people waiting to go down on floor?
	public boolean hasDownWaiters(int floor){
		if (thresholdsDown[floor-1].waiters()>0){
			return true;
		}
		return false;
	}
	
	
	
	//This method is called when the while loop of the elevator algorithm is over, i.e., 
	//when there are no passengers left on the elevator. In this case, we want to go to the 
	//closest floor with any waiters, and then set our direction accordingly. Also, we
	//only go to that floor if another elevator is not already going to that floor. To ensure
	//this, we use prohibitedFloors, which is a list of the floors that other idle elevators 
	//are about to visit.
	public int getClosestRequest(int currFloor, ArrayList<Integer> prohibitedFloors, Elevator e) {
		for (int i = 1; i<numFloors+1; i++){
			if (currFloor-i < 1 && currFloor+i >= numFloors+1)
				return -1;
			if (currFloor-i>=1 && thresholdsUp[currFloor-i-1].waiters()>0 && !prohibitedFloors.contains(currFloor-i)){
				e.setDirection(true);
				return currFloor-i;
			}
			if (currFloor-i>=1 && thresholdsDown[currFloor-i-1].waiters()>0 && !prohibitedFloors.contains(currFloor-i)){
				e.setDirection(false);
				return currFloor-i;
			}
			if (currFloor+i<numFloors+1 && thresholdsUp[currFloor+i-1].waiters()>0 && !prohibitedFloors.contains(currFloor+i)){
				e.setDirection(true);
				return currFloor+i;
			}
			if (currFloor+i<numFloors+1 && thresholdsDown[currFloor+i-1].waiters()>0 && !prohibitedFloors.contains(currFloor+i)){
				e.setDirection(false);
				return currFloor+i;
			}
		}
		return -1;
	}
	
	
	//called by rider in enter so that the rider completes on the building's copy 
	//of the event barriers, i.e., the rider is now safely inside the elevator.
	public void tellBuilding(int currFloor, boolean Up) {
		if (Up){
			thresholdsUp[currFloor-1].complete();
		}
		else{
			thresholdsDown[currFloor-1].complete();
		}
		
	}
	
	
	
}
