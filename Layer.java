import java.util.Arrays;
import java.util.HashSet;

public class Layer {
    private double learningRate = 0.1;
    private HashSet<Neuron> neuronSet = new HashSet<>();
    public Layer(int nodesNum, int nextNodesNum) {
        for (int i = 0; i < nodesNum; i++) {
            neuronSet.add(new Neuron(true, nextNodesNum));
        }
    }
    public Layer(int nodesNum) {
        for (int i = 0; i < nodesNum; i++) {
            neuronSet.add(new Neuron(true));
        }
    }
    public Layer(HashSet<Neuron> neuronSet) {
        this.neuronSet = neuronSet;
    }
    public HashSet<Neuron> getNeurons() {
        return neuronSet;
    }
    public double[] getValuesAsVector() {
        double[] ret_val = new double[neuronSet.size()];
        int counter = 0;
        for(Neuron n : neuronSet) {
            ret_val[counter] = n.getVal();
            counter++;
        }
        return ret_val;
    }
    public double[][] getWeightsAsMatrix() {
        double[][] ret_val = new double[neuronSet.size()][];
        int counter = 0;
        for(Neuron n : neuronSet) {
            ret_val[counter] = n.getWeights();
            counter++;
        }
        return ret_val;
    }
    public void activateLayer(Layer nextLayer) {
        for(Neuron b : nextLayer.getNeurons()) {
            double sum = 0;
            int counter = 0;
            for(Neuron a : this.neuronSet) {
                sum+=a.getVal()*(a.getWeights()[counter]);
            }
            counter++;
            sum = Util.sigmafy(sum + b.getBias());
            b.setVal(sum);
        }
    }
    public void addWeightDeltas(double[][] dW) {
        //System.out.println(Util.stringify2DArr(dW));
        int counter = 0;
        for(Neuron n : neuronSet) {
            //System.out.println(Arrays.toString(n.getWeights()));
            for (int i = 0; i < dW[counter].length; i++) {
                n.setWeight(i, n.getWeights()[i]-(dW[counter][i]*learningRate));
            } 
            counter++;
        }
    }
    public void addBiasDeltas(double[] dB) {
        int counter = 0;
        for(Neuron n : neuronSet) {
            n.setBias(n.getBias()-(dB[counter]*learningRate));
            counter++;
        }
    }
    @Override
    public String toString() {
        String ret_val = "";
        for (Neuron n : neuronSet) {
            ret_val+=n.toString();
        }
        return ret_val + "\n";
    }
}
