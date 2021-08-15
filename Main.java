import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import Exceptions.TooFewLayersException;


public class Main {
    public static final int generationSize = 100;
    public static ArrayList<Network> generation = new ArrayList<>();
    public static double[][] leftWhiteA = { { 1, 0, 0 }, { 1, 0, 0 }, { 1, 0, 0 } };
    public static double[][] rightWhiteA = { { 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 } };
    public static double[][] leftWhiteB = { { 0.79, 0, 0 }, { 0.84, 0, 0 }, { 0.9, 0, 0 } };
    public static double[][] rightWhiteB = { { 0, 0, 0.93 }, { 0, 0, 0.89 }, { 0, 0, 0.95 } };
    public static double[][] leftWhiteC = { { 0.92, 0, 0 }, { 0.75, 0, 0 }, { 0.87, 0, 0 } };
    public static double[][] rightWhiteC = { { 0, 0, 0.97 }, { 0, 0, 1 }, { 0, 0, 0.79 } };

    public static double[][] leftWhiteTester = { { 0.9, 0, 0 }, { 0.69, 0, 0 }, { 0.91, 0, 0 } };
    public static double[][] junkTester = { { 0, 0.8, 0.66 }, { 0, 0.1, 0 }, { 0, 0, 0.43 } };
    public static double[][] rightWhiteTester = { { 0, 0, 0.9 }, { 0, 0, 0.9 }, { 0, 0, 0.97 } };
    public static HashMap<OutputProfiles, double[][][]> trainingData = new HashMap<>();
    //TODO: Implement batch gradient descent
    //TODO: Prevent bias training with variance of occurence between profiles
    public static void main(String[] args) throws TooFewLayersException {
        insertData();
        Network n = new Network(9, 2);
        testNN(n);
        for (OutputProfiles op : trainingData.keySet()) {
            double[] desiredValues = op.getProfile();
            System.out.println(op);
            for (int j = 0; j < trainingData.get(op).length; j++) {
                n.initializeNetwork(Util.flattenArr(trainingData.get(op)[j]));
                double[] result = n.pulse();
                double cost = Util.calculateCost(result, desiredValues);
                System.out.println("Cost: " + cost);
                n.learnFrom(op.profile);
            }
        }
        testNN(n);
    }

    public static void testNN(Network n) {
        System.out.println("\nTest begun");
        n.initializeNetwork(Util.flattenArr(leftWhiteTester));
        final DecimalFormat df = new DecimalFormat("0.00");
        double[] result = n.pulse();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " " ));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.LEFT_PROFILE.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.LEFT_PROFILE.getProfile()));

        n.initializeNetwork(Util.flattenArr(rightWhiteTester));
        result = n.pulse();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " " ));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.RIGHT_PROFILE.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.RIGHT_PROFILE.getProfile()));

        System.out.println("Test ended\n");
    }

    public static void insertData() {
        trainingData.put(OutputProfiles.LEFT_PROFILE, new double[][][] { leftWhiteA, leftWhiteB, leftWhiteC});
        trainingData.put(OutputProfiles.RIGHT_PROFILE, new double[][][] { rightWhiteA, rightWhiteC, rightWhiteC}); 
    }

    public static Network[] getBreeders(double[] costArr) {
        Network[] ret_val = new Network[2];
        double min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < costArr.length; i++) {
            if (costArr[i] < min) {
                min = costArr[i];
                minIndex = i;
            }
        }
        ret_val[0] = generation.get(minIndex);
        costArr[minIndex] = Integer.MAX_VALUE;
        min = Integer.MAX_VALUE;
        for (int i = 0; i < costArr.length; i++) {
            if (costArr[i] < min) {
                min = costArr[i];
                minIndex = i;
            }
        }
        ret_val[1] = generation.get(minIndex);
        return ret_val;
    }

    enum OutputProfiles {
        LEFT_PROFILE(new double[] { 1, 0 }), RIGHT_PROFILE(new double[] { 0, 1 });

        private double[] profile;

        public double[] getProfile() {
            return profile;
        }

        private OutputProfiles(double[] profile) {
            this.profile = profile;
        }
    }
}
// TODO: Get pixel data from 100px by 100px images
/*abstract       /*insertData();
        // train!
        for (int i = 0; i < generationSize; i++) {
            generation.add(new Network(9, 16, 2));
        }
        for (int k = 0; k < 1000; k++) {
            double[] costArr = new double[generationSize];
            for (int i = 0; i < generation.size(); i++) {
                double totalCost = 0;
                int dataCounter = 0;
                for (OutputProfiles op : trainingData.keySet()) {
                    double[] desiredValues = op.getProfile();
                    for (int j = 0; j < trainingData.get(op).length; j++) {
                        generation.get(i).initializeNetwork(Util.flattenArr(trainingData.get(op)[j]));
                        double[] result = generation.get(i).pulse();
                        double cost = Util.calculateCost(result, desiredValues);
                        totalCost += cost;
                        dataCounter++;
                    }
                }
                costArr[i] = totalCost/dataCounter;
                generation.get(i).setCost(costArr[i]);
            }
            //System.out.println(Arrays.toString(costArr));
            Network[] breeders = getBreeders(costArr);
            generation = Util.breed(breeders, generationSize);
        }

        generation.get(0).initializeNetwork(Util.flattenArr(leftWhiteTester));
        final DecimalFormat df = new DecimalFormat("0.00");
        double[] result = generation.get(0).pulse();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " " ));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.LEFT_PROFILE.getProfile()));

        generation.get(0).initializeNetwork(Util.flattenArr(rightWhiteTester));
        result = generation.get(0).pulse();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " " ));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.RIGHT_PROFILE.getProfile()));*/