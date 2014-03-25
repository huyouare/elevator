import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Test {
	
	public static void runTest(Building b, Scanner in){
		HashMap<Integer, Rider> riders = new HashMap<Integer, Rider>();
		int[] visits = new int[1];
		int riderNumber;
		int startFloor;
		int destFloor;
		while(in.hasNextLine()){
			riderNumber = in.nextInt();
			startFloor = in.nextInt(); //FLOOR NUMBER STARTS AT 1
			destFloor = in.nextInt();
			
			System.out.println("Rider" + riderNumber + ": from Floor " + (startFloor) + " to Floor " + (destFloor));
			
			if(riders.containsKey(riderNumber)){
				Rider r = riders.get(riderNumber);
				int[] oldVisits = r.getFloorVisits();
				visits = new int[oldVisits.length + 1];
				System.arraycopy(oldVisits, 0, visits, 0, oldVisits.length );
				r.setFloorVisits(visits);
			}
			
			else{
				visits[0] = destFloor;
				Rider r = new Rider(visits, b, startFloor, riderNumber);
				Thread t = new Thread(r);
				t.start();
			}
		}
	}
	
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(new FileReader(args[1]));
		//input.txt or test1.txt or test2.txt or test3.txt or test-extra.txt
		
//		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
//		System.setOut(out);
		Logger.open();
		
		int numFloors = in.nextInt();
		int numElevators = in.nextInt();
		int numRiders = in.nextInt();
		int maxCapacity = in.nextInt();
		Building b;
		
		if(args.length == 0){
			// No options specified; make the default as the part 3 elevator submission
			System.out.println("Using default: Part 3");
			// call the elevator part3
			b = new Building(numFloors, numElevators, maxCapacity);
			runTest(b, in);
		}
		else if(args.length > 2){
			// Throw an error--too many args
			System.out.println("Error: too many arguments");
		}
		else{	// known just one arg
			if(args[0].equals("p1")){
			// call the EventBarrier"
				EventBarrier e = new EventBarrier();
				Thread m1 = new Thread(new Minstrel(e));
				Thread m2 = new Thread(new Minstrel(e));
				Thread m3 = new Thread(new Minstrel(e));
				Thread g1 = new Thread(new Guard(e));
				m1.start();
				m2.start();
				g1.start();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				m3.start();
			}
			else if(args[0].equals("p2part1")){
			// call the elevator part1
			//	Include a test program that demonstrates the operation of your elevator for multiple riders. 
			//	Your program should be "well-behaved" in the following sense: 
			//	any rider that calls the elevator for the up or down direction (using CallUp or CallDown) 
			//	should wait until the elevator arrives, then get on it (Enter), request a floor (RequestFloor), 
			//	then get off (Exit) when RequestFloor returns, signifying arrival at the correct floor.
				b = new Building(numFloors, 1);
				runTest(b, in);
			} 
			else if(args[0].equals("p2part2")){
			// call the elevator part2
				b = new Building(numFloors, 1, maxCapacity);
				runTest(b, in);
			} 
			else if(args[0].equals("p2part3")){
			// call the elevator part3
				b = new Building(numFloors, numElevators, maxCapacity);
				runTest(b, in);
			}
			else if(args[0].equals("extracredit")){
				b = new Building(numFloors, numElevators, maxCapacity);
				HashMap<Integer, Rider> riders = new HashMap<Integer, Rider>();
				int[] visits = new int[1];
				int riderNumber;
				int startFloor;
				int destFloor;
				int behavior;
				while(in.hasNextLine()){
					riderNumber = in.nextInt();
					startFloor = in.nextInt(); //FLOOR NUMBER STARTS AT 1
					destFloor = in.nextInt();
					behavior = in.nextInt();
					
					System.out.println("Rider" + riderNumber + ": from Floor " + (startFloor) + " to Floor " + (destFloor));
					
					if(riders.containsKey(riderNumber)){
						Rider r = riders.get(riderNumber);
						int[] oldVisits = r.getFloorVisits();
						visits = new int[oldVisits.length + 1];
						System.arraycopy(oldVisits, 0, visits, 0, oldVisits.length );
						r.setFloorVisits(visits);
					}
					
					else{
						visits[0] = destFloor;
						Rider r = new Rider(visits, b, startFloor, riderNumber);
						r.setBehavior(behavior);
						Thread t = new Thread(r);
						t.start();
					}
				}
			}
			else{
				System.out.println("Input not understood.");
			}
		}
	}

}
