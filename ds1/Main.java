import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int p = sc.nextInt();
		Random random = new Random();
		int count = 0;
		
		for (int i = 0; i < 5; i++) {
			int n = random.nextInt(1, p-1);
			if (testFerma(n, p-1) == 1) {
				count++;
			}
		}
		
		if(count == 5) {
			System.out.println("Число " + p + " простое ");
		} else {
			System.out.println("Число " + p + " составное");
		}
	}
	
	public static long getPow(int osn, int step){
		int res = 1;
		while (osn > 0){
			if (osn % 2 != 0){
				res *= osn;
				osn *= osn;
				step /= 2;
			} else {
				osn *= osn;
				step /= 2;
			}
		}
		return res;
	}
	
	
	public static int testFerma(int p, int n){
		if ((getPow(n, p-1) % p) == 1){
			return 1;
		} return 0;
	}
}