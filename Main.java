import java.util.Arrays;

import Exceptions.TooFewLayersException;

public class Main {
    public static void main(String[] args) throws TooFewLayersException {
        Network n = new Network(100, 16, 16, 5);
        n.initializeNetwork(/*pixeldata should go here*/);
        double[] result = n.pulse();
    }
}
