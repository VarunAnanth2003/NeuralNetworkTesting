import java.util.Arrays;
import java.util.stream.Stream;

public class Util {

    public static String stringify2DArr(double[][] arr) {
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

    public static double getAvgError(double[] error) {
        double sum = 0;
        for (int i = 0; i < error.length; i++) {
            sum += error[i];
        }
        return (sum / error.length);
    }

    public static double sigmafy(double input) {
        return 1 / (1 + Math.exp(-input));
    }

    public static double sigmafyDerivative(double input) {
        return (sigmafy(input) * (1 - sigmafy(input)));
    }

    public static double calculateCost(double[] result, double[] desiredValues) {
        double ret_val = 0;
        for (int i = 0; i < result.length; i++) {
            try {
                ret_val += Math.pow(result[i] - desiredValues[i], 2);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Desired values array does not match the network result array");
                e.printStackTrace();
            }
        }
        return ret_val;
    }

    public static double[] calculateCostDerivative(double[] result, double[] desiredValues) {
        double[] ret_val = new double[result.length];
        for (int i = 0; i < result.length; i++) {
            try {
                ret_val[i] = 2 * (result[i] - desiredValues[i]);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Desired values array does not match the network result array");
                e.printStackTrace();
            }
        }
        return ret_val;
    }

    public static double[] calculatedCdI(double[] a, double[] b, ActivationFunction af) {
        double[] ret_val = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret_val[i] = af.getFunction().calculateDerivative(a[i]) * b[i];    
        }
        return ret_val;
    }

    public static double[][] outerProduct(double[] a, double[] b) {
        double[][] ret_val = new double[b.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                ret_val[j][i] = a[i] * b[j];
            }
        }
        return ret_val;
    }

    public static double[] multiply(double[][] a, double[] b) {
        double[] ret_val = new double[a.length];
        for (int i = 0; i < ret_val.length; i++) {
            ret_val[i] = dotProduct(a[i], b);
        }
        return ret_val;
    }

    public static double dotProduct(double[] a, double[] b) {
        double ret_val = 0;
        for (int i = 0; i < a.length; i++) {
            ret_val += a[i] * b[i];
        }
        return ret_val;
    }

    public static double[] flattenArr(double[][] arr) {
        double[] ret_val = Stream.of(arr).flatMapToDouble(Arrays::stream).toArray();
        return ret_val;
    }

    public static double[][] multiplyOnMatrix(double[][] arr, double constant) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] -= constant;
            }
        }
        return arr;
    }

    public static double[][] l2RegularizeMatrix(double[][] arr, double l2constant) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                arr[i][j] = arr[i][j] - (arr[i][j] * l2constant);
            }
        }
        return arr;
    }
}
