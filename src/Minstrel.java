public class Minstrel implements Runnable {
	EventBarrier e; 
	public Minstrel(EventBarrier event){
		e = event;
	}
	public void run(){
		speak1();
		//System.out.println("There are currently " + e.waiters() + " at the bridge.\n");
		e.arrive();
		speak2();
		e.complete();
	}
	public void speak2(){
		System.out.format("Hello from me a Minstrel. Made it accross the bridge: -%s\n", Thread.currentThread().getName());
	}
	public void speak1(){
		System.out.format("At the bridge: -%s\n", Thread.currentThread().getName());
	}
	
}
