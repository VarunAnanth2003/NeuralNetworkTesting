import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Exceptions.TooFewLayersException;

public class Network {
    private Queue<Layer> layerQueue = new LinkedList<>();
    private Layer inputLayer;
    private int numLayers;
    private double cost = 0;
    /**
     * Network constructor
     * @param layerValues should be the number of neurons per layer, in order from left to right of the NN
     * @throws TooFewLayersException
     */
    public Network(int... layerNodes) throws TooFewLayersException {
        numLayers = layerNodes.length;
        if(numLayers < 2) {
            throw new TooFewLayersException();
        }
        for (int i = 0; i < layerNodes.length; i++) {
            layerQueue.add(new Layer(layerNodes[i]));
        }
        inputLayer = layerQueue.peek();
    }
    public void initializeNetwork(double[] initialValues) {
        int counter = 0;
        for (Neuron n : inputLayer.getNeurons()) {
            try{
                n.setVal(initialValues[counter]); 
                counter++;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("Too many initial values for base network layer.");
            }
        }
    }
    public void initializeNetwork() {
        for (Neuron n : inputLayer.getNeurons()) {
            try{
                n.setVal(new Random().nextDouble()); 
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("Too many initial values for base network layer.");
            }
        }
    }
    public double[] pulse() {
        Layer currentLayer = null;
        Layer nextLayer = null;
        for (int i = 0; i < numLayers-1; i++) {
            currentLayer = layerQueue.poll();
            nextLayer = layerQueue.peek();
            currentLayer.activateLayer(nextLayer);
            layerQueue.add(currentLayer);
        }
        double[] ret_val = new double[nextLayer.getNeurons().size()];
        int counter = 0;
        for(Neuron n : nextLayer.getNeurons()) {
            ret_val[counter] = n.getVal();
            counter++;
        }
        return ret_val;
    }
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    @Override
    public String toString() {
        String ret_val = "";
        for(Layer l : layerQueue) {
            ret_val+=l.toString();
        }
        return ret_val;
    }
}
