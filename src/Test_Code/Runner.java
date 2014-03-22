package Test_Code;
import EventBarrier;


public class Runner {

	
	public static void main(String[] args) {
		EventBarrier e = new EventBarrier();
		Thread m1 = new Thread(new Minstrel(e));
		Thread m2 = new Thread(new Minstrel(e));
		Thread m3 = new Thread(new Minstrel(e));
		Thread g1 = new Thread(new Gaurd(e));
		m1.start();
		m2.start();
		g1.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		m3.start();
		return;
	}

}
