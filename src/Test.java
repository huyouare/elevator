import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
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
			startFloor = in.nextInt()-1; //USING INDEXES, RATHER THAN FLOOR NUMBER
			destFloor = in.nextInt()-1; //USING INDEXES, RATHER THAN FLOOR NUMBER
			
			System.out.println("Rider" + riderNumber + ": from Floor " + startFloor + " to Floor " + destFloor);
			
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
		Scanner in = new Scanner(new FileReader("./src/input.txt"));
		
//		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
//		System.setOut(out);
		Logger.log("Start");
		
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
			if(args[1].equals("p1")){
			// call the EventBarrier"
				
			}
			if(args[1].equals("p2part1")){
			// call the elevator part1
			//	Include a test program that demonstrates the operation of your elevator for multiple riders. 
			//	Your program should be "well-behaved" in the following sense: 
			//	any rider that calls the elevator for the up or down direction (using CallUp or CallDown) 
			//	should wait until the elevator arrives, then get on it (Enter), request a floor (RequestFloor), 
			//	then get off (Exit) when RequestFloor returns, signifying arrival at the correct floor.
				b = new Building(numFloors, 1);
				runTest(b, in);
			} 
			else if(args[1].equals("p2part2")){
			// call the elevator part2
				b = new Building(numFloors, 1, maxCapacity);
				runTest(b, in);
				
			} 
			else if(args[1].equals("p2part3")){
			// call the elevator part3
				b = new Building(numFloors, numElevators, maxCapacity);
				runTest(b, in);
			}
		}
	}

}
