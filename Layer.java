import java.util.HashSet;

public class Layer {
    private HashSet<Neuron> neuronSet = new HashSet<>();
    public Layer(int nodesNum) {
        for (int i = 0; i < nodesNum; i++) {
            neuronSet.add(new Neuron(true));
        }
    }
    public HashSet<Neuron> getNeurons() {
        return neuronSet;
    }
    public void activateLayer(Layer nextLayer) {
        for(Neuron b : nextLayer.getNeurons()) {
            double sum = 0;
            for(Neuron a : this.neuronSet) {
                sum+=a.getVal()*a.getWeight();
            }
            sum = Util.sigmafy(sum + b.getBias());
            b.setVal(sum);
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
