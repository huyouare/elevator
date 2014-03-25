import java.util.Random;


public class Rider implements Runnable {
	private int myFloor;
	private int[] floorVisits;
	private Building myBuilding;
	private Random r;
	private int myNumber;
	private int behavior = 0; 	// VARIABLE FOR WELL/MAL-BEHAVED, 0 IS WELL-BEHAVED
								// 1 = call the elevator but don't wait for it
								// 2 = get on but don't call floor
								// 3 = doesn't exit on requested floor
	
	public Rider(int [] f, Building b){
		myBuilding = b;
		myFloor =1;
		floorVisits= new int[f.length];
		for(int i = 0; i<f.length; i++){
			floorVisits[i]=f[i];
		}
		r = new Random(1234);
	}
	
	public Rider(int [] f, Building b, int startFloor, int riderNumber){
		myBuilding = b;
		myFloor = startFloor;
		floorVisits= new int[f.length];
		for(int i = 0; i<f.length; i++){
			floorVisits[i]=f[i];
		}
		r = new Random(1234);
		myNumber = riderNumber;
//		Thread.currentThread().setName("" + riderNumber);
//		System.out.println("NAME: " + Thread.currentThread().getName());
	}
	
	public int[] getFloorVisits(){
		return this.floorVisits;
	}
	
	public int getNumber(){
		return this.myNumber;
	}
	
	public void setFloorVisits(int[] visits){
		this.floorVisits = visits;
	}
	
	public void setBehavior(int b){
		this.behavior = b;
	}
	
	public void run() {
		AbstractElevator e;
		int sleeper;
		Thread.currentThread().setName("" + this.myNumber);
		System.out.println("NAME: " + Thread.currentThread().getName());
		for (int i = 0 ; i<floorVisits.length; i++){
			if (floorVisits[i]>myFloor){
				//System.out.println("Do we get here0");
				System.out.println(myFloor);
				Logger.log("R" + myNumber + " pushes U" + (myFloor)); //Output floor number, not index
				e = myBuilding.CallUp(myFloor);
			}
			else{
				Logger.log("R" + myNumber + " pushes D" + (myFloor)); //Output floor number
				e = myBuilding.CallDown(myFloor);
			}
			//System.out.println("Do we get here1?");
			if(behavior==1){
				continue;
			}
			while (!e.Enter()){	
				//System.out.println("do we get here2?");
				if (floorVisits[i]>myFloor){
					Logger.log("R" + myNumber + " pushes U" + (myFloor)); //Output floor number
					e = myBuilding.CallUp(myFloor);
				}
				else{
					Logger.log("R" + myNumber + " pushes D" + (myFloor)); //Output floor number
					e = myBuilding.CallDown(myFloor);
				}
			}
			
			if(behavior==2)
				continue;
			
			Logger.log("R" + myNumber + " pushes E" + e.elevatorId + "F" + (floorVisits[i]));
			e.RequestFloor(floorVisits[i]);
			
			if(behavior==3)
			
			e.Exit();
			Logger.log("R" + myNumber + " exits E" + e.elevatorId + " on F" + (floorVisits[i]));
			myFloor = floorVisits[i];
			
//			sleeper = r.nextInt(1000);
//			try {
//				Thread.currentThread().sleep(sleeper);
//			} catch (InterruptedException e1) {
//				e1.printStackTrace();
//			}
			System.out.println("HI, I'm rider " + Thread.currentThread().getName() + " and I'm on floor " + myFloor);
		}
	}

}
