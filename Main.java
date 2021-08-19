import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import Exceptions.TooFewLayersException;
import NetworkClasses.Network;
import Other.Constants;
import Other.DataGenerator;
import Other.Util;
import Other.FunctionClasses.Activation.ActivationOptions;
import Other.FunctionClasses.Cost.CostOptions;

public class Main {
    public static HashMap<OutputProfiles, double[][][]> trainingData = new HashMap<>();

    //TODO: implement momentum cleanly
    //TODO: Use MNIST handwriting to test NN
    //TODO: Comment code and make useable
    public static void main(String[] args) throws TooFewLayersException {
        prepareData();
        Network n = new Network(
            new int[] { 81, 4 },
            new ActivationOptions[] { ActivationOptions.SIGMOID, ActivationOptions.SIGMOID }, 
            CostOptions.QUADRATIC
            );
        //testNN(n);
        for (int i = 0; i < Constants.batchSize * 1000; i++) {
            OutputProfiles op = OutputProfiles.getRandomProfile();
            n.pulseWithInput(Util.flattenArr(trainingData.get(op)[new Random().nextInt(100000)]));
            n.learnFrom(op.getProfile());
            if (i % Constants.batchSize == 0)
                n.updateLayers();
        }
        testNN(n);
    }

    public static void testNN(Network n) {
        try {
            BufferedImage image = ImageIO.read(new File("C.png"));
            double[][] data = new double[image.getHeight()][image.getWidth()];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    int rgb = image.getRGB(j, i);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);
                    data[j][i] = Math.abs((((r+g+b)/3)/255)-1);
                }
            }
            n.initializeNetwork(Util.flattenArr(data));
            double[] result = n.pulseWithResult();
            System.out.println("This is a " + OutputProfiles.getBestProfile(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prepareData() {
        int dataNum = 100000;
        double[][][] aData = new double[dataNum][][];
        for (int i = 0; i < aData.length; i++) {
            aData[i] = DataGenerator.generateA();
        }
        trainingData.put(OutputProfiles.SQUARE, aData);

        double[][][] bData = new double[dataNum][][];
        for (int i = 0; i < bData.length; i++) {
            bData[i] = DataGenerator.generateB();
        }
        trainingData.put(OutputProfiles.DIAMOND, bData);

        double[][][] cData = new double[dataNum][][];
        for (int i = 0; i < cData.length; i++) {
            cData[i] = DataGenerator.generateC();
        }
        trainingData.put(OutputProfiles.PLUS, cData);

        double[][][] dData = new double[dataNum][][];
        for (int i = 0; i < dData.length; i++) {
            dData[i] = DataGenerator.generateD();
        }
        trainingData.put(OutputProfiles.CROSS, dData);

    }

    enum OutputProfiles {
        SQUARE(new double[] { 1, 0, 0, 0 }), DIAMOND(new double[] { 0, 1, 0, 0 }), PLUS(new double[] { 0, 0, 1, 0 }),
        CROSS(new double[] { 0, 0, 0, 1 });

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
                return OutputProfiles.SQUARE;
            } else if (randSel == 1) {
                return OutputProfiles.DIAMOND;
            } else if (randSel == 2) {
                return OutputProfiles.PLUS;
            } else {
                return OutputProfiles.CROSS;
            }
        }
        public static OutputProfiles getBestProfile(double[] result) {
            OutputProfiles[] desiredValues = OutputProfiles.values();
            double costArr[] = new double[desiredValues.length];
            for (int i = 0; i < costArr.length; i++) {
                costArr[i] = Util.calculateCost(result, desiredValues[i].getProfile());
            }
            double min = Double.POSITIVE_INFINITY;
            int minIndex = -1;
            for (int i = 0; i < costArr.length; i++) {
                if(costArr[i] < min) {
                    min = costArr[i];
                    minIndex = i;
                }
            }
            return desiredValues[minIndex];
        }
    }

}
    /*public static void testNN(Network n) {
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

        double a = (new Random().nextDouble() * (0.25)) + (0.75);
        double b = (new Random().nextDouble() * (0.25));
        double[][] unseenData = {
            {a, a, b, a, a, a, a, a, a},
            {a, 0, 0, 0, 0, 0, 0, b, a},
            {a, 0, b, 0, 0, 0, b, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, b, b, 0, b, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, b},
            {a, 0, b, 0, 0, 0, 0, 0, a},
            {a, 0, 0, b, 0, b, 0, 0, a},
            {a, b, a, a, a, a, a, a, a}
        };
        n.initializeNetwork(Util.flattenArr(unseenData));
        result = n.pulseWithCost();
        Arrays.stream(result).forEach(e -> System.out.print(df.format(e) + " "));
        System.out.println();
        System.out.println(Arrays.toString(OutputProfiles.A.getProfile()));
        System.out.println(Util.calculateCost(result, OutputProfiles.A.getProfile()));
        System.out.println("Test ended\n");
    }*/