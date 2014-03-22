
public class Runner {
	public static void main(String[] args){
		Elevator e = new Elevator(25, 1 , 3);
		Building b = new Building(25, 1, e);
		e.addBuilding(b);
		int [] a = {5,4,8,9,10,11};
		int [] c = {2, 4, 22, 24, 10};
		Thread r1 = new Thread(new Rider(a, b));
		Thread r2 = new Thread(new Rider(c, b));
		Thread ev = new Thread(e);
		r1.start();
		r2.start();
		ev.start();
	}
}
