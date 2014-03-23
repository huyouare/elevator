
public class EventBarrier extends AbstractEventBarrier {
	
	private int numWaiters;
	private boolean signaled; 
	
	public EventBarrier(){
		numWaiters = 0;
		signaled = false;
	}

	
	public synchronized void arrive() {
		numWaiters++;
		while(!signaled){
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		return;
		
	}

	public synchronized void raise() {
		if (numWaiters == 0)
			return;
		signaled = true;
		notifyAll();
		while(signaled){
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		return;
	}


	public synchronized void complete() {
		numWaiters--;
		while(numWaiters>0){
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		signaled = false;
		notifyAll();
		return;
	}

	public synchronized int waiters() {
		return numWaiters;
	}
	
}
