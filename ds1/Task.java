public class Task{
	
	private int p;
	
	public Task(int p){
		this.p = p;
		
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
	
	public int getP(){
		return p;
	}
}