import java.util.Arrays;
import java.util.Random;


public class Runner {
	public static void main(String[] args){
		Building b = new Building(150, 3, 10);
		int[] rider = new int[10];
		Random r = new Random(1234);
		for(int i = 0; i<20; i++){
			for(int j = 0; j<10; j++){
				rider[j]=r.nextInt(150);
			}
			System.out.println("Rider" + i + " : " + Arrays.toString(rider));
			(new Thread(new Rider(rider, b))).start();
		}
	}
}
