package NetworkClasses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import Exceptions.TooFewLayersException;
import Other.Util;
import Other.FunctionClasses.Activation.ActivationFunction;
import Other.FunctionClasses.Activation.ActivationOptions;
import Other.FunctionClasses.Cost.CostFunction;
import Other.FunctionClasses.Cost.CostOptions;

public class Network {

    private Queue<Layer> layerQueue = new LinkedList<>();
    private Layer inputLayer;
    private int numLayers;
    private CostFunction cf;

    public Network(int[] layerNodeCounts, ActivationOptions[] afArr, CostOptions costOp) throws TooFewLayersException {
        numLayers = layerNodeCounts.length;
        if (numLayers < 2) {
            throw new TooFewLayersException();
        }
        for (int i = 0; i < layerNodeCounts.length - 1; i++) {
            int currentNodeAmount = layerNodeCounts[i];
            int nextNodeAmount = layerNodeCounts[i + 1];
            layerQueue.add(new Layer(currentNodeAmount, nextNodeAmount, new ActivationFunction(afArr[i])));
        }
        layerQueue.add(new Layer(layerNodeCounts[layerNodeCounts.length - 1],
                new ActivationFunction(afArr[layerNodeCounts.length - 1])));
        inputLayer = layerQueue.peek();
        cf = new CostFunction(costOp);
    }

    public Network(Queue<Layer> layerQueue, CostFunction cf) throws TooFewLayersException {
        if (layerQueue.size() < 2) {
            throw new TooFewLayersException();
        }
        numLayers = layerQueue.size();
        this.layerQueue = layerQueue;
        inputLayer = layerQueue.peek();
        this.cf = cf;
    }

    public void initializeNetwork(double[] initialValues) {
        int counter = 0;
        for (Neuron n : inputLayer.getNeurons()) {
            try {
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
            try {
                n.setVal(new Random().nextDouble());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("Too many initial values for base network layer.");
            }
        }
    }

    public void pulse() {
        Layer currentLayer = null;
        Layer nextLayer = null;
        for (int i = 0; i < numLayers - 1; i++) {
            currentLayer = layerQueue.poll();
            nextLayer = layerQueue.peek();
            currentLayer.activateLayer(nextLayer);
            layerQueue.add(currentLayer);
        }
        layerQueue.add(layerQueue.poll());
    }

    public double[] pulseWithResult() {
        Layer currentLayer = null;
        Layer nextLayer = null;
        for (int i = 0; i < numLayers - 1; i++) {
            currentLayer = layerQueue.poll();
            nextLayer = layerQueue.peek();
            currentLayer.activateLayer(nextLayer);
            layerQueue.add(currentLayer);
        }
        layerQueue.add(layerQueue.poll());
        double[] ret_val = new double[nextLayer.getNeurons().size()];
        int counter = 0;
        for (Neuron n : nextLayer.getNeurons()) {
            ret_val[counter] = n.getVal();
            counter++;
        }
        return ret_val;
    }

    public void pulseWithInput(double[] initialValues) {
        int counter = 0;
        for (Neuron n : inputLayer.getNeurons()) {
            try {
                n.setVal(initialValues[counter]);
                counter++;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.err.println("Too many initial values for base network layer.");
            }
        }
        Layer currentLayer = null;
        Layer nextLayer = null;
        for (int i = 0; i < numLayers - 1; i++) {
            currentLayer = layerQueue.poll();
            nextLayer = layerQueue.peek();
            currentLayer.activateLayer(nextLayer);
            layerQueue.add(currentLayer);
        }
        layerQueue.add(layerQueue.poll());
    }

    public void learnFrom(double[] expected) {
        Stack<Layer> stackLayers = new Stack<>();
        for (Layer l : layerQueue) {
            stackLayers.push(l);
        }
        Layer curLayer = stackLayers.pop();

        double[] dCdO = cf.getFunction().calculateDerivative(curLayer.getValuesAsVector(), expected);
        do {
            double[] dCdi = Util.calculatedCdI(curLayer.getValuesAsVector(), dCdO, curLayer.getActivationFunction());
            double[][] dCdW = Util.outerProduct(dCdi, stackLayers.peek().getValuesAsVector());
            curLayer.addBiasDeltas(dCdi);
            stackLayers.peek().addWeightDeltas(dCdW);
            dCdO = Util.calculatedCdO(stackLayers.peek().getWeightsAsMatrix(), dCdi);
            curLayer = stackLayers.pop();
        } while (!stackLayers.empty());

    }

    public void updateLayers() {
        for (Layer l : layerQueue) {
            l.adjustWB();
        }
    }

    public Queue<Layer> getLayers() {
        return layerQueue;
    }

    public void saveToFile(File f) {
        System.out.println("Saving...");
        try {
            f.createNewFile();
            FileWriter w = new FileWriter(f);
            w.write(this.cf.getCo() + "\n");
            w.write(layerQueue.size() + "\n");
            for (Layer l : layerQueue) {
                w.write(l.getNeurons().size() + " " + l.getNextNodesNum() + " ");
                w.write(l.getActivationFunction().getAo() + "\n");
                for (Neuron n : l.getNeurons()) {
                    Arrays.stream(n.getWeights()).forEach(value -> {
                        try {
                            w.write(value + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    w.write("\n" + n.getBias() + "\n");
                }
            }
            w.flush();
            w.close();
            System.out.println("Saved to " + f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        System.out.println("Saving...");
        try {
            File f = new File("Saved Networks\\" + System.currentTimeMillis() + ".txt");
            f.createNewFile();
            FileWriter w = new FileWriter(f);
            w.write(this.cf.getCo() + "\n");
            w.write(layerQueue.size() + "\n");
            for (Layer l : layerQueue) {
                w.write(l.getNeurons().size() + " " + l.getNextNodesNum() + " ");
                w.write(l.getActivationFunction().getAo() + "\n");
                for (Neuron n : l.getNeurons()) {
                    Arrays.stream(n.getWeights()).forEach(value -> {
                        try {
                            w.write(value + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    w.write("\n" + n.getBias() + "\n");
                }
            }
            w.flush();
            w.close();
            System.out.println("Saved to " + f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String ret_val = "";
        for (Layer l : layerQueue) {
            ret_val += l.toString();
            ret_val += ("\n---\n");
        }
        return ret_val;
    }
}