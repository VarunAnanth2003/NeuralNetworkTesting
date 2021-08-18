package NetworkClasses;


import java.util.Arrays;
import java.util.Random;

import Other.Constants;

public class Neuron {
    private double[] w;
    private double b;
    private double val = 0;

    public Neuron(boolean randomize, int nextLayerNeuronCount) {
        w = new double[nextLayerNeuronCount];
        if (randomize) {
            for (int i = 0; i < nextLayerNeuronCount; i++) {
                w[i] = Constants.minNeuronRange
                        + ((Constants.maxNeuronRange - Constants.minNeuronRange) * (new Random().nextDouble()));
            }
            b = Constants.minNeuronRange + ((Constants.maxNeuronRange - Constants.minNeuronRange) * (new Random().nextDouble()));
        } else {
            for (int i = 0; i < nextLayerNeuronCount; i++) {
                w[i] = 0;
            }
            b = 0;
        }
    }

    public Neuron(boolean randomize) {
        if (randomize) {
            w = new double[0];
            b = Constants.minNeuronRange + (Constants.maxNeuronRange - Constants.minNeuronRange) * (new Random().nextDouble());
        } else {
            w = new double[0];
            b = 0;
        }
    }

    public double[] getWeights() {
        return w;
    }

    public double getBias() {
        return b;
    }

    public double getVal() {
        return val;
    }

    public void setWeights(double[] w) {
        this.w = w;
    }

    public void setWeight(int index, double w) {
        this.w[index] = w;
    }

    public void setBias(double b) {
        this.b = b;
    }

    public void setVal(double val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "w: " + Arrays.toString(w) + " b: " + b + " val: " + val + " ";
    }
}
