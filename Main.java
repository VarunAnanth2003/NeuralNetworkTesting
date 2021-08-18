
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import Exceptions.TooFewLayersException;
import NetworkClasses.Network;
import Other.Constants;
import Other.DataGenerator;
import Other.Util;
import Other.ActivationClasses.FunctionOptions;

public class Main {
    public static HashMap<OutputProfiles, double[][][]> trainingData = new HashMap<>();

    public static void main(String[] args) throws TooFewLayersException {
        prepareData();
        Network n = new Network(new int[] {81, 4}, new FunctionOptions[] {FunctionOptions.SIGMOID, FunctionOptions.SIGMOID});
        testNN(n);
        for (int i = 0; i < Constants.batchSize * 100; i++) {
            OutputProfiles op = OutputProfiles.getRandomProfile();
            n.pulseWithInput(Util.flattenArr(trainingData.get(op)[new Random().nextInt(100000)]));
            n.learnFrom(op.getProfile());
            if (i % Constants.batchSize == 0)
                n.updateLayers();
        }
        testNN(n);
    }

    public static void testNN(Network n) {
        System.out.println("\nTest begun");
        n.initializeNetwork(Util.flattenArr(DataGenerator.generateA()));
        final DecimalFormat df = new DecimalFormat("0.00");
        double[] result = n.pulseWithCost();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " "));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.A.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.A.getProfile()));

        n.initializeNetwork(Util.flattenArr(DataGenerator.generateB()));
        result = n.pulseWithCost();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " "));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.B.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.B.getProfile()));

        n.initializeNetwork(Util.flattenArr(DataGenerator.generateC()));
        result = n.pulseWithCost();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " "));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.C.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.C.getProfile()));

        n.initializeNetwork(Util.flattenArr(DataGenerator.generateD()));
        result = n.pulseWithCost();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " "));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.D.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.D.getProfile()));

        System.out.println("Test ended\n");
    }

    public static void prepareData() {
        int dataNum = 100000;
        double[][][] aData = new double[dataNum][][];
        for (int i = 0; i < aData.length; i++) {
            aData[i] = DataGenerator.generateA();
        }
        trainingData.put(OutputProfiles.A, aData);

        double[][][] bData = new double[dataNum][][];
        for (int i = 0; i < bData.length; i++) {
            bData[i] = DataGenerator.generateB();
        }
        trainingData.put(OutputProfiles.B, bData);

        double[][][] cData = new double[dataNum][][];
        for (int i = 0; i < cData.length; i++) {
            cData[i] = DataGenerator.generateC();
        }
        trainingData.put(OutputProfiles.C, cData);

        double[][][] dData = new double[dataNum][][];
        for (int i = 0; i < dData.length; i++) {
            dData[i] = DataGenerator.generateD();
        }
        trainingData.put(OutputProfiles.D, dData);

    }

    enum OutputProfiles {
        A(new double[] { 1, 0, 0, 0 }), B(new double[] { 0, 1, 0, 0 }), C(new double[] { 0, 0, 1, 0 }), D(new double[] {0, 0, 0, 1});

        private double[] profile;

        public double[] getProfile() {
            return profile;
        }

        private OutputProfiles(double[] profile) {
            this.profile = profile;
        }

        public static OutputProfiles getRandomProfile() {
            int randSel = new Random().nextInt(4);
            if (randSel == 0) {
                return OutputProfiles.A;
            } else if (randSel == 1) {
                return OutputProfiles.B;
            } else if (randSel == 2) {
                return OutputProfiles.C;
            } else {
                return OutputProfiles.D;
            }
        }

    }

}