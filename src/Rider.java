import java.util.Random;


public class Rider implements Runnable {
	private int myFloor;
	private int[] floorVisits;
	private Building myBuilding;
	private Random r;
	private int myNumber;
	
	public Rider(int [] f, Building b){
		myBuilding = b;
		myFloor =0;
		floorVisits=f;
		r = new Random(1234);
	}
	
	public Rider(int [] f, Building b, int startFloor, int riderNumber){
		myBuilding = b;
		myFloor = startFloor;
		floorVisits=f;
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
				e = myBuilding.CallUp(myFloor);
			}
			else{
				e = myBuilding.CallDown(myFloor);
			}
			while (!e.Enter()){	
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
