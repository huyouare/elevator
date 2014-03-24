import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Test {
	
	public static void main(String[] args) throws IOException{
		//Scanner in = new Scanner(new FileReader("input.txt"));
		
//		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
//		System.setOut(out);
		Logger.log("Start");
		
		if(args.length == 0){
			// No options specified; make the default as the part 3 elevator submission
			System.out.println("Using default: Part 3");
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
				
			} else if(args[1].equals("p2part2")){
			// call the elevator part2
				
			} else if(args[1].equals("p2part3")){
			// call the elevator part3
				
			}
		}
	}

}
