import java.util.Arrays;
import java.util.Random;

public class OldMain {
    public static final int len = 13000;
    public static final int populationSize = 25;
    public static final int maxVal = 1000000;
    public static final int[] targetArr = initializeTarget();
    public static final int generation = 100;

    /*public static void main(String[] args) {
        //initialization
        int[][] population = initializeRandom(populationSize);
        int[] initError = errorCalculatorB(population);

        //logging
        System.out.println("Target: " + Arrays.toString(targetArr) + "\n");
        System.out.println(Util.stringify2DArr(population, initError));
        for (int i = 0; i < 100; i++) {
            //breeding process
            int[] error = errorCalculatorB(population);
            int[][] breeders = getBreeders(population, error);
            population = BreedingGround.breedA(breeders);
        }
        //error calculation
        int[] error = errorCalculatorB(population);
        Util.printBestChild(population, error);

    }*/
    public static int[] initializeTarget() {
        int[] ret_val = new int[len];
        for (int i = 0; i < ret_val.length; i++) {
            ret_val[i] = new Random().nextInt(maxVal+1);
        }
        return ret_val;
    }
    public static int[][] initializeRandom(int amount) {
        int[][] ret_val = new int[amount][];
        for (int i = 0; i < amount; i++) {
            int[] sub = new int[len];
            for (int j = 0; j < sub.length; j++) {
                sub[j] = new Random().nextInt(maxVal);
            }
            ret_val[i] = sub;
        }
        return ret_val;
    }
    public static int[] errorCalculator(int[][] arr) {
        int[] ret_val = new int[populationSize];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                int d = arr[i][j] - targetArr[j];
                ret_val[i] += (d < 0 ? d * -1 : d);
            }
        }
        return ret_val;
    }
    public static int[] errorCalculatorB(int[][] arr) {
        int[] ret_val = new int[populationSize];
        for (int i = 0; i < arr.length; i++) {
            int sumA = 0;
            int sumB = 0;
            for (int j = 0; j < arr[i].length; j++) {
                sumA += arr[i][j];
                sumB += targetArr[j];
            }
            int sum = sumA - sumB;
            ret_val[i] = (sum < 0 ? sum * -1 : sum);
        }
        return ret_val;
    }
    public static int[][] getBreeders(int[][] population, int[] errors) {
        int[][] ret_val = new int[2][];
        for (int i = 0; i < ret_val.length; i++) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < errors.length; j++) {
                if (errors[j] < min && errors[j] >= 0) {
                    min = errors[j];
                    minIndex = j;
                    ret_val[i] = population[j];
                }
            }
            errors[minIndex] = -1;
        }
        return ret_val;
    }
    
}