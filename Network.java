import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

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
            try {
                int currentNodeAmount = layerNodes[i];
                int nextNodeAmount = layerNodes[i+1];
                layerQueue.add(new Layer(currentNodeAmount, nextNodeAmount));
            } catch (IndexOutOfBoundsException e) {
                //e.printStackTrace();
                layerQueue.add(new Layer(layerNodes[i]));
            }
        }
        inputLayer = layerQueue.peek();
    }
    public Network(Queue<Layer> layerQueue) throws TooFewLayersException {
        if(layerQueue.size() < 2) {
            throw new TooFewLayersException();
        }
        numLayers = layerQueue.size();
        this.layerQueue = layerQueue;
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
        layerQueue.add(layerQueue.poll());
        double[] ret_val = new double[nextLayer.getNeurons().size()];
        int counter = 0;
        for(Neuron n : nextLayer.getNeurons()) {
            ret_val[counter] = n.getVal();
            counter++;
        }
        return ret_val;
    }
    public void learnFrom(double[] expected) {
        Stack<Layer> stackLayers = new Stack<>();
        for(Layer l : layerQueue) {
            stackLayers.push(l);
        }
        
        Layer curLayer = stackLayers.pop();
        double[] dCdO = Util.calculateCostDerivativeVector(curLayer.getValuesAsVector(), expected);
        do {
            double[] dCdi = Util.hadamardProduct(curLayer.getValuesAsVector(), dCdO);
            double[][] dCdW = Util.outerProduct(dCdi, stackLayers.peek().getValuesAsVector());
            curLayer.addBiasDeltas(dCdi);
            stackLayers.peek().addWeightDeltas(dCdW);
            dCdO = Util.multiply(curLayer.getWeightsAsMatrix(), dCdi);
            curLayer = stackLayers.pop();
        } while (!stackLayers.empty());
        
    }
    public double getCost() {
        return this.cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public Queue<Layer> getLayers() {
        return layerQueue;
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
