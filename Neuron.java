import java.util.Random;

public class Neuron {
    private double w;
    private double b;
    private double val = 0;
    public Neuron(boolean randomize) {
        if(randomize) {
            w = Util.minNeuronRange + (Util.maxNeuronRange - Util.minNeuronRange) * (new Random().nextDouble());
            b = Util.minNeuronRange + (Util.maxNeuronRange - Util.minNeuronRange) * (new Random().nextDouble());
        } else {
            w = 0;
            b = 0;
        }
    }
    public double getWeight() {
        return w;
    }
    public double getBias() {
        return b;
    }
    public double getVal() {
        return val;
    }
    public void setWeight(double w) {
        this.w = w;
    }
    public void setBias(double b) {
        this.b = b;
    }
    public void setVal(double val) {
        this.val = val;
    }
    @Override
    public String toString() {
        return "w: " + w + " b: " + b + " val: " + val + " ";
    }
}
