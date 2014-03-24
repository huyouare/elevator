import java.util.ArrayList;
import java.util.Arrays;


public class Elevator extends AbstractElevator implements Runnable{
	private EventBarrier[] floorThresholds; //myelevator doors to let people out.
	private Building myBuilding;		
	private boolean UP;				//denotes current moving direction.
	private int numPeople;			
	private int currFloor;
	private int myDest; 
	
	
	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		UP = true;
		numPeople = 0;
		currFloor = 0;
		floorThresholds = new EventBarrier[numFloors];
		for (int i = 0; i<numFloors; i++){
			floorThresholds[i]=new EventBarrier();
		}
	}
	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold, Building b){
		super(numFloors, elevatorId, maxOccupancyThreshold);
		myBuilding = b;
		UP = true;
		numPeople = 0;
		currFloor = 0;
		floorThresholds = new EventBarrier[numFloors];
		for (int i = 0; i<numFloors; i++){
			floorThresholds[i]=new EventBarrier();
		}
	}
	public void addBuilding(Building b){
		myBuilding = b;
	}
	
	
	public void run(){
		int nextFloor;
		while(true){
			if ((nextFloor=getNextFloor()) != -1){
				VisitFloor(nextFloor);
				myDest = 0;
			}
		}
	}
	
	public int getNextFloor(){
		//if there are still requests;
		if (UP){
			for (int i = currFloor; i<numFloors; i++){
				if (floorThresholds[i].waiters()>0){
					return i;
				}
				if (myBuilding.hasUpWaiters(i)){
					return i;
				}
			}
		}
		else{
			for (int i = currFloor; i>=0; i--){
				if (floorThresholds[i].waiters()>0){
					return i;
				}
				if (myBuilding.hasDownWaiters(i)){
					return i;
				}
			}
		}
		
		ArrayList<Integer> prohibitedFloors = new ArrayList<Integer>();
		for(int i = 0; i<myBuilding.numElevators; i++){
			prohibitedFloors.add(myBuilding.myElevators[i].myDest);
		}
		synchronized(myBuilding.lock){
		myDest = myBuilding.getClosestRequest(currFloor, prohibitedFloors, this);
		}
		//get available request on Building, this will also set my direction to be correct for the riders there.
		return myDest;
		
		
	}
	
	
	//these methods are invoked by elevator threads.
	
	//allow current riders OFF. They will complete in Exit.
	public void OpenDoors() {
		floorThresholds[currFloor].raise();
	}
	public void CloseDoors() {	
		myBuilding.allowRidersOn(currFloor, UP, this);
	}
	public void VisitFloor(int floor) {
		System.out.println("Visiting Floor :" + floor);
		currFloor = floor;	
		OpenDoors(); //let riders off;
		CloseDoors(); //let new riders on;
		System.out.println("All aboard, heading onward!");
	}

	//these are invoked by rider threads. note that rider threads block here.
	public boolean Enter() {
		if (numPeople<maxOccupancyThreshold){
			synchronized(this){
				numPeople++;
			}
			System.out.println("Inside elevator -" + Thread.currentThread().getName());
			myBuilding.tellBuilding(currFloor, UP);  //notify Building that you're on the Elevator
			return true;
		}
		myBuilding.tellBuilding(currFloor, UP);   //same as above.
		return false;
	}

	@Override
	public void Exit() {
		synchronized(this){
			numPeople--;
		}
		System.out.println("Arrived at floor " + currFloor + " " + Thread.currentThread().getName());
		floorThresholds[currFloor].complete(); //rider is off.
	}

	//requesting a floor is equivalent to arriving at the eventbarrier for that floor
	//the rider thread will block here until the elevator lets him off at his correct floor.
	public void RequestFloor(int floor) {
		floorThresholds[floor].arrive(); //rider waits here until we get to his floor;
	}
	
	public void setDirection(boolean Up){
		UP = Up;
	}

	

}
