import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	static Logger logger;
	static FileHandler fh;  
	
	public static void open(){
		logger = Logger.getLogger("MyLog");  

	    try {  
	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("output.log", false);  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	        logger.info("My first log");  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	    logger.info("Hi How r u?");  
	}
	
    public static void log(String message){ 
    	System.out.println("Writing: " + message);
		logger.info(message);
	}
    
    public static void close(){
    	fh.close();
    }
}

