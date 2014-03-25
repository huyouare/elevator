import java.util.Random;


public class Rider implements Runnable {
	private int myFloor;
	private int[] floorVisits;
	private Building myBuilding;
	private Random r;
	private int myNumber;
	
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
	
	public void run() {
		AbstractElevator e;
		int sleeper;
		for (int i = 0 ; i<floorVisits.length; i++){
			if (floorVisits[i]>myFloor){
				System.out.println("Do we get here0");
				e = myBuilding.CallUp(myFloor);
				Logger.log("R" + myNumber + " pushes U" + (myFloor+1)); //Output floor number, not index
			}
			else{
				e = myBuilding.CallDown(myFloor);
				Logger.log("R" + myNumber + " pushes D" + (myFloor+1)); //Output floor number, not index
			}
			System.out.println("Do we get here1?");
			while (!e.Enter()){	
				System.out.println("do we get here2?");
				if (floorVisits[i]>myFloor){
					e = myBuilding.CallUp(myFloor);
				}
				else{
					e = myBuilding.CallDown(myFloor);
				}
			}
			e.RequestFloor(floorVisits[i]);
			e.Exit();
			myFloor = floorVisits[i];
			sleeper = r.nextInt(1000);
			try {
				Thread.currentThread().sleep(sleeper);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("HI, I'm rider " + Thread.currentThread().getName() + " and I'm on floor " + myFloor);
		}
	}

}
