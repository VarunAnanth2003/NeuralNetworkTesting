package NetworkClasses;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import Other.Constants;
import Other.Util;
import Other.ActivationClasses.ActivationFunction;

public class Layer {
    private Queue<Neuron> neuronQueue = new LinkedList<>();
    private HashSet<double[][]> dWSet = new HashSet<>();
    private HashSet<double[]> dBSet = new HashSet<>();
    private int[] matrixDims = new int[2];
    private boolean hasWeights = false;
    private boolean hasBiases = false;
    private ActivationFunction af;

    public Layer(int nodesNum, int nextNodesNum, ActivationFunction af) {
        for (int i = 0; i < nodesNum; i++) {
            neuronQueue.add(new Neuron(true, nextNodesNum));
        }
        this.af = af;
    }

    public Layer(int nodesNum,  ActivationFunction af) {
        for (int i = 0; i < nodesNum; i++) {
            neuronQueue.add(new Neuron(true));
        }
        this.af = af;
    }

    public Layer(Queue<Neuron> neuronSet, ActivationFunction af) {
        this.neuronQueue = neuronSet;
        this.af = af;
    }

    public Queue<Neuron> getNeurons() {
        return neuronQueue;
    }

    public double[] getValuesAsVector() {
        double[] ret_val = new double[neuronQueue.size()];
        int counter = 0;
        for (Neuron n : neuronQueue) {
            ret_val[counter] = n.getVal();
            counter++;
        }
        return ret_val;
    }

    public double[][] getWeightsAsMatrix() {
        double[][] ret_val = new double[neuronQueue.size()][];
        int counter = 0;
        for (Neuron n : neuronQueue) {
            ret_val[counter] = n.getWeights();
            counter++;
        }
        return ret_val;
    }

    public ActivationFunction getActivationFunction() {
        return af;
    }

    public void activateLayer(Layer nextLayer) {
        int counter = 0;
        for (Neuron b : nextLayer.getNeurons()) {
            double sum = 0;
            for (Neuron a : this.neuronQueue) {
                sum += (a.getVal() * (a.getWeights()[counter]));
            }
            counter++;
            sum = Util.sigmafy(sum - b.getBias());
            b.setVal(sum);
        }
    }

    public void addWeightDeltas(double[][] dW) {
        if (dWSet.isEmpty()) {
            matrixDims[0] = dW.length;
            matrixDims[1] = dW[0].length;
        }
        dWSet.add(dW);
        hasWeights = true;
    }

    public void addBiasDeltas(double[] dB) {
        dBSet.add(dB);
        hasBiases = true;
    }

    public void adjustWB() {
        double[][] dW = new double[matrixDims[0]][matrixDims[1]];
        for (double[][] a : dWSet) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    dW[i][j] += a[i][j];
                }
            }
        }
        for (int i = 0; i < dW.length; i++) {
            for (int j = 0; j < dW[i].length; j++) {
                dW[i][j] /= dWSet.size();
            }
        }

        double[] dB = new double[neuronQueue.size()];
        for (double[] a : dBSet) {
            for (int i = 0; i < a.length; i++) {
                dB[i] += a[i];
            }
        }
        for (int i = 0; i < dB.length; i++) {
            dB[i] /= dBSet.size();
        }
        int counter = 0;
        if (hasBiases) {
            for (Neuron n : neuronQueue) {
                n.setBias(n.getBias() + (dB[counter] * Constants.learningRate));
                counter++;
            }
        }
        counter = 0;
        if (hasWeights) {
            dW = Util.l2RegularizeMatrix(dW, Constants.L2regConstant);
            for (Neuron n : neuronQueue) {
                for (int i = 0; i < dW[counter].length; i++) {
                    n.setWeight(i, n.getWeights()[i] - (dW[counter][i] * Constants.learningRate));
                }
                counter++;
            }
        }
        dWSet.clear();
        dBSet.clear();
        hasWeights = false;
        hasBiases = false;
    }

    @Override
    public String toString() {
        String ret_val = "";
        for (Neuron n : neuronQueue) {
            ret_val += n.toString();
        }
        return ret_val + "\n";
    }
}
