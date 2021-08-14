import java.util.Arrays;

public class Util {
    public static double minNeuronRange = -10;
    public static double maxNeuronRange = 10;
    public static String stringify2DArr(int[][] arr) {
        String ret_val = "";
        for (int i = 0; i < arr.length; i++) {
            ret_val += i + ": " + Arrays.toString(arr[i]) + "\n";
        }
        return ret_val;
    }
    public static String stringify2DArr(int[][] arr, int[] error) {
        String ret_val = "";
        for (int i = 0; i < arr.length; i++) {
            ret_val += i + ": " + Arrays.toString(arr[i]) + "; " + error[i] + "\n";
        }
        return ret_val;
    }
    public static int getAvgError(int[] error) {
        int sum = 0;
        for (int i = 0; i < error.length; i++) {
            sum += error[i];
        }
        return(sum/error.length);
    } 
    public static void printBestChild(int[][] population, int[] error) {
        int minError = Integer.MAX_VALUE;
        int minErrorIndex = -1;
        for (int i = 0; i < error.length; i++) {
            if(error[i] < minError) {
                minError = error[i];
                minErrorIndex = i;
            }
        }
        System.out.println("Best Child: " + Arrays.toString(population[minErrorIndex]) + "\nError: " + minError);
    }
    public static double sigmafy(double input) {
        return 1/(1+Math.exp(-input));
    }
    public static double calculateCost(double[] result, double[] desiredValues) {
        double ret_val = 0;
        for (int i = 0; i < result.length; i++) {
            try {
                ret_val+=Math.pow(result[i]-desiredValues[i], 2);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Desired values array does not match the network result array");
                e.printStackTrace();
            }
        }
        return ret_val;
    }
}
