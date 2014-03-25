import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Logger {
	static PrintStream out;
	
	public static void open(){
		try {
			out = new PrintStream(new File("Elevator.log"));
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}
    public static void log(String message){ 
    	System.out.println("Writing: " + message);
		out.println(message);
	}
    public static void close(){
    	out.close();
    }
}