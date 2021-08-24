package Other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Stream;

import Exceptions.TooFewLayersException;
import NetworkClasses.Layer;
import NetworkClasses.Network;
import NetworkClasses.Neuron;
import Other.FunctionClasses.Activation.ActivationFunction;
import Other.FunctionClasses.Cost.CostFunction;

public class Util {

    public static String stringify2DArr(double[][] arr) {
        String ret_val = "";
        for (int i = 0; i < arr.length; i++) {
            ret_val += i + ": " + Arrays.toString(arr[i]) + "\n";
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

    public static double[] calculatedCdO(double[][] a, double[] b) {
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

    public static Network readFromFile(File f) {
        System.out.println("Reading...");
        try {
            Queue<Layer> layerQueue = new LinkedList<>();
            BufferedReader b = new BufferedReader(new FileReader(f));
            String costFunctionString = b.readLine();
            int layerAmount = Integer.parseInt(b.readLine());
            for (int i = 0; i < layerAmount; i++) {
                Scanner paramReader = new Scanner(b.readLine());
                Integer nodeNum = paramReader.nextInt();
                Integer nextNodeNum = paramReader.nextInt();
                String activationFunctionString = paramReader.next();
                Queue<Neuron> neuronQueue = new LinkedList<>();
                for (int j = 0; j < nodeNum; j++) {
                    Neuron n = new Neuron(false);
                    Scanner weightBiasReader = new Scanner(b.readLine());
                    double[] weights = new double[nextNodeNum];
                    for (int k = 0; k < nextNodeNum; k++) {
                        weights[k] = weightBiasReader.nextDouble();
                    }
                    double bias = Double.parseDouble(b.readLine());
                    n.setWeights(weights);
                    n.setBias(bias);
                    neuronQueue.add(n);
                    weightBiasReader.close();
                }
                layerQueue.add(new Layer(neuronQueue, ActivationFunction.convertStringToObject(activationFunctionString), nextNodeNum));
                paramReader.close();
            }
            b.close();
            return new Network(layerQueue, CostFunction.convertStringToObject(costFunctionString));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TooFewLayersException e) {
            e.printStackTrace();
        }
        return null;
    }
}