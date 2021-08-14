import java.util.Arrays;

import Exceptions.TooFewLayersException;

public class Main {
    public static void main(String[] args) throws TooFewLayersException {
        Network n = new Network(100, 16, 16, 5);
        n.initializeNetwork(/*pixeldata should go here*/);
        double[] result = n.pulse();
        //TODO: Get pixel data from 100px by 100px images
        //TODO: Find a way to average cost over multiple training data images
        //TODO: Use average cost to select best NNs
        //TODO: Breed NN for performance with variable noise and see results
    }
}
