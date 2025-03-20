import java.util.Random;

public class Main {
	public static void main(String[] args){
		Random random = new Random();
		
		System.out.println(getPow(2, 5));
		System.out.println(testFerma(101, 15));
		System.out.println(testFerma(101, 29));
		System.out.println(testFerma(101, 82));
		System.out.println(testFerma(101, 11));
	}
	
	public static int getPow(int osn, int step){
		int res = 1;
		while (osn > 0){
			if (step % 2 != 0){
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
	
	public static boolean testFerma(int p, int n){
		if((getPow(n, p-1) % p) == 1){
			return true;
		} return false;
	}
}