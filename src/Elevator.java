import java.util.ArrayList;
import java.util.Arrays;


public class Elevator extends AbstractElevator implements Runnable{
	private EventBarrier[] floorThresholds; //myelevator doors to let people out.
	private Building myBuilding;		
	private boolean UP;				//denotes current moving direction.
	private int numPeople;		//number of passengers in elevator	
	private int currFloor;
	private int myDest;         //used to denote the destination of an elevator which 
								//was idle but is now about to visit a floor. This is 
								//used to create the prohibitedFloors list so that 
								//an idle elevator will not go to a floor if that 
								//floor is already the myDest of another elevator
	
	private double arriveTime;  //used to make sure that an elevator only leaves once everyone has 
								//requested a floor


	
	
	
	public Elevator(int numFloors, int elevatorId, int maxOccupancyThreshold) {
		super(numFloors, elevatorId, maxOccupancyThreshold);
		UP = true;
		numPeople = 0;
		currFloor = 1;
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
		currFloor = 1;
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
			arriveTime = System.currentTimeMillis();
			while ((nextFloor=getNextFloor()) != -1){
				VisitFloor(nextFloor);
				myDest = 0;
				arriveTime = System.currentTimeMillis();
			}
		}
	}
	
	public int getNextFloor(){
		int counter = 0; //total number of floorRequests
		
		while(counter<numPeople && (System.currentTimeMillis()-arriveTime<1000)){//if the total number of
			//floor requests is less than the number of people on the elevator, then we have to keep looping
			//until everyone enters a request. However, if we have looped for over a second, then we assume
			//that we're dealing with someone who got in but didn't request a floor, so we break out of the loop
			synchronized(this){
				//recompute the total number of floor requests atomically
				counter = 0;
				for(int i = 0; i<floorThresholds.length; i++){
					counter += floorThresholds[i].waiters();
				}
			}
		}
		
		
		if (UP){
			//standard elevator algorithm...look to say if we need to let anyone off or on in our 
			//same direction.
			for (int i = currFloor; i<numFloors+1; i++){
				if (floorThresholds[i-1].waiters()>0){
					return i;
				}
				if (myBuilding.hasUpWaiters(i)){
					return i;
				}
			}
		}
		else{
			for (int i = currFloor; i>=1; i--){
				if (floorThresholds[i-1].waiters()>0){
					return i;
				}
				if (myBuilding.hasDownWaiters(i)){
					return i;
				}
			}
		}
		//if we haven't returned yet, that means there was no one to let off and no one that wanted to
		//get on in our direction, so now the elevator is idle and we must find the next floor it should go to.
		//The next floor should be the closest floor that is not in prohibitedFloors, i.e., the closest 
		//floor that another idle elevator is not about to go to.
		ArrayList<Integer> prohibitedFloors = new ArrayList<Integer>();
		synchronized(myBuilding.lock){
		for(int i = 0; i<myBuilding.numElevators; i++){
			prohibitedFloors.add(myBuilding.myElevators[i].myDest);
		}
		//get available request on Building, this will also set my direction to be correct.
		myDest = myBuilding.getClosestRequest(currFloor, prohibitedFloors, this);
		}
		
		return myDest;
		
		
	}
	
	
	//these methods are invoked by elevator threads.
	
	//allow current riders OFF. They will complete in Exit.
	public void OpenDoors() {
		floorThresholds[currFloor-1].raise();
	}
	//allow new riders on. This is just a small wrapper around raise(), and riders complete in enter()
	public void CloseDoors() {	
		myBuilding.allowRidersOn(currFloor, UP, this);
	}
	
	public void VisitFloor(int floor) {
		System.out.println("Visiting Floor :" + floor);
		//System.out.println(this.hashCode());
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
			myBuilding.tellBuilding(currFloor, UP);  //notify Building that you're on the Elevator, i.e.,
			//complete on the event which was raised in closedoors
			return true;
		}
		myBuilding.tellBuilding(currFloor, UP);   //If you weren't able to get in, we still want you to complete
		//on the event so that the elevator isn't blocking forever. This is fine because you callUp or callDown again
		//right after. 
		return false;
	}

	@Override
	public void Exit() {
		synchronized(this){
			numPeople--;
		}
		System.out.println("Arrived at floor " + currFloor + " " + Thread.currentThread().getName());
		floorThresholds[currFloor].complete(); //complete on the event which is raised in openDoors.
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
