import java.util.Scanner;
public class Fib {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] result = pow(n);
        System.out.println(toString(result)); //вывод самой матрицы
        System.out.println("Число Фибоначчи для n = " + result[0][0]); //число Фибоначчи


    }

    public static int[][] mult(int[][] a, int[][] b){
        int[][] res = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                for(int k = 0; k < 2; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return res;
    }

    public static int[][] pow(int n){
        int[][] res = new int[2][2];
        res[0][0] = 1;
        res[0][1] = 0;
        res[1][0] = 0;
        res[1][1] = 1;
        int[][] matr = new int[2][2];
        matr[0][0] = 1;
        matr[0][1] = 1;
        matr[1][0] = 1;
        matr[1][1] = 0;
        while(n > 0){
            if(n % 2 != 0){
                res = mult(res, matr);
            }
            matr = mult(matr, matr);
            n /= 2;
        }
        return res;
    }

    public static String toString(int[][] matr) {
        StringBuilder matrStr = new StringBuilder();
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 2; j++){
                matrStr.append( matr[i][j] + " ");
            }
            matrStr.append("\n");
        }
        return matrStr.toString();
    }

}
