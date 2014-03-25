public class Guard implements Runnable {
	EventBarrier e; 
	public Guard(EventBarrier event){
		e = event;
	}
	public void run(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.format("Oh hello, all %d of you. I'm here to open the bridge!\n", e.waiters());
		e.raise();
		speak();
	}
	public void speak(){
		System.out.format("they all made it through: -from %s \n", Thread.currentThread().getName());
	}
}
