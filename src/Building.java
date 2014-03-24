import java.util.ArrayList;


public class Building extends AbstractBuilding{
	public Object lock; 
	public Elevator[] myFloors;
	public Elevator[] myElevators;
	

	private EventBarrier [] thresholdsUp; //one for each floor
	private EventBarrier [] thresholdsDown;
	
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
		thresholdsUp[fromFloor].arrive();
		return myFloors[fromFloor];
	}
	
	
	
	//a waiter comes, notifies the building he's there, and blocks at the event barrier.
	public AbstractElevator CallDown(int fromFloor) {
		thresholdsDown[fromFloor].arrive();
		return myFloors[fromFloor];
	}
	
	
	
	
	//called by an elevator to let folks on in the direction it's going.
	public void allowRidersOn(int floor, boolean Up, Elevator e){
		System.out.println("people waiting to go UP: " + thresholdsUp[floor].waiters());
		System.out.println("people waiting to go DOWN: " + thresholdsDown[floor].waiters());
		myFloors[floor] = e;
		if (Up){
			thresholdsUp[floor].raise();
		}
		else{
			thresholdsDown[floor].raise();
		}
	}
		
	
	public boolean hasUpWaiters(int floor){
		if (thresholdsUp[floor].waiters()>0){
			return true;
		}
		return false;
	}
	
	
	
	public boolean hasDownWaiters(int floor){
		if (thresholdsDown[floor].waiters()>0){
			return true;
		}
		return false;
	}
	
	
	
	//called by an elevator to get a pickup request in ANY direction.
	public int getClosestRequest(int currFloor, ArrayList<Integer> prohibitedFloors, Elevator e) {
		for (int i = 0; i<numFloors; i++){
			if (currFloor-i < 0 && currFloor+i >= numFloors)
				return -1;
			if (currFloor-i>=0 && thresholdsUp[currFloor-i].waiters()>0 && !prohibitedFloors.contains(currFloor-i)){
				e.setDirection(true);
				return currFloor-i;
			}
			if (currFloor-i>=0 && thresholdsDown[currFloor-i].waiters()>0 && !prohibitedFloors.contains(currFloor-i)){
				e.setDirection(false);
				return currFloor-i;
			}
			if (currFloor+i<numFloors && thresholdsUp[currFloor+i].waiters()>0 && !prohibitedFloors.contains(currFloor+i)){
				e.setDirection(true);
				return currFloor+i;
			}
			if (currFloor+i<numFloors && thresholdsDown[currFloor+i].waiters()>0 && !prohibitedFloors.contains(currFloor+i)){
				e.setDirection(false);
				return currFloor+i;
			}
		}
		return -1;
	}
	
	
	//called by rider to complete
	public void tellBuilding(int currFloor, boolean Up) {
		if (Up){
			thresholdsUp[currFloor].complete();
		}
		else{
			thresholdsDown[currFloor].complete();
		}
		
	}
	
	
	
}
