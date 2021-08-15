import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Stream;

import Exceptions.TooFewLayersException;

public class Util {
    public static double minNeuronRange = -1;
    public static double maxNeuronRange = 1;
    public static double noiseStepWeight = 0.3;
    public static double noiseStepBias = 0.05;

    public static String stringify2DArr(int[][] arr) {
        String ret_val = "";
        for (int i = 0; i < arr.length; i++) {
            ret_val += i + ": " + Arrays.toString(arr[i]) + "\n";
        }
        return ret_val;
    }

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

    public static int getAvgError(int[] error) {
        int sum = 0;
        for (int i = 0; i < error.length; i++) {
            sum += error[i];
        }
        return (sum / error.length);
    }

    public static double getAvgError(double[] error) {
        double sum = 0;
        for (int i = 0; i < error.length; i++) {
            sum += error[i];
        }
        return (sum / error.length);
    }


    public static void printBestChild(int[][] population, int[] error) {
        int minError = Integer.MAX_VALUE;
        int minErrorIndex = -1;
        for (int i = 0; i < error.length; i++) {
            if (error[i] < minError) {
                minError = error[i];
                minErrorIndex = i;
            }
        }
        System.out.println("Best Child: " + Arrays.toString(population[minErrorIndex]) + "\nError: " + minError);
    }

    public static double sigmafy(double input) {
        return 1 / (1 + Math.exp(-input));
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
    public static double[] calculateCostDerivativeVector(double[] result, double[] desiredValues) {
        double[] ret_val = new double[result.length];
        for (int i = 0; i < result.length; i++) {
            try {
                ret_val[i] =  2*(result[i] - desiredValues[i]);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Desired values array does not match the network result array");
                e.printStackTrace();
            }
        }
        return ret_val;
    }
    public static double[] hadamardProduct(double[] a, double[] b) {
        double[] ret_val = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            ret_val[i] = a[i] * b[i];
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
    public static double dotProduct(double[]a, double[] b) {
        double ret_val = 0;
        for (int i = 0; i < a.length; i++) {
            ret_val+=a[i]*b[i];
        }
        return ret_val;
    }

    public static double[] flattenArr(double[][] arr) {
        double[] ret_val = Stream.of(arr).flatMapToDouble(Arrays::stream).toArray();
        return ret_val;
    }

    //obsolete, generational training doesn't work
    public static ArrayList<Network> breed(Network[] parents, int generationSize) {
        ArrayList<Network> ret_val = new ArrayList<>();
        Network base = parents[0];
        Network adder = parents[1];
        for (int i = 0; i < generationSize; i++) {
            Iterator<Layer> bItr = base.getLayers().iterator();
            Iterator<Layer> aItr = adder.getLayers().iterator();
            Queue<Layer> layerQueue = new LinkedList<>();
            while (bItr.hasNext() && aItr.hasNext()) {
                Layer baseLayer = bItr.next();
                Layer adderLayer = aItr.next();
                Iterator<Neuron> baseLayerItr = baseLayer.getNeurons().iterator();
                Iterator<Neuron> adderLayerItr = adderLayer.getNeurons().iterator();
                HashSet<Neuron> neuronSet = new HashSet<>();
                while (baseLayerItr.hasNext() && adderLayerItr.hasNext()) {
                    Neuron a = baseLayerItr.next();
                    Neuron b = adderLayerItr.next();
                    Neuron c = new Neuron(false, a.getWeights().length);
                    boolean randSel = new Random().nextInt(2) == 0 ? false : true;
                    for(int j = 0; j < a.getWeights().length; j++) {
                        randSel = new Random().nextInt(2) == 0 ? false : true;
                        if (randSel) {
                            c.setWeight(j, a.getWeights()[j]);
                        } else {
                            c.setWeight(j, b.getWeights()[j]);
                        }
                    }
                    randSel = new Random().nextInt(2) == 0 ? false : true;
                    if (randSel) {
                        c.setBias(a.getBias());
                    } else {
                        c.setBias(b.getBias());
                    }
                    for(int j = 0; j < a.getWeights().length; j++) {
                        randSel = new Random().nextInt(2) == 0 ? false : true;
                        if (randSel) {
                            c.setWeight(j, c.getWeights()[j] + noiseStepWeight);
                        } else {
                            c.setWeight(j, c.getWeights()[j] - noiseStepWeight);
                        }
                    }
                    randSel = new Random().nextInt(2) == 0 ? false : true;
                    if (randSel) {
                        c.setBias(c.getBias() + noiseStepBias);
                    } else {
                        c.setBias(c.getBias() - noiseStepBias);
                    }
                    neuronSet.add(c);
                }
                layerQueue.add(new Layer(neuronSet));
            }
            try {
                Network childNetwork = new Network(layerQueue);
                ret_val.add(childNetwork);
            } catch (TooFewLayersException e) {
                e.printStackTrace();
            }
        }
        return ret_val;
    }
}
