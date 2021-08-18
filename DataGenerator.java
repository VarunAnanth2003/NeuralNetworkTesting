import java.util.Random;

public class DataGenerator {
    private static Random r = new Random();
    public static double[][] generateA() {
        double a = (r.nextDouble() * (0.25)) + (0.75);
        double[][] ret_val = {
            {a, a, a, a, a, a, a, a, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {a, a, a, a, a, a, a, a, a}
        };
        return ret_val;
    }
    public static double[][] generateB() {
        double a = (r.nextDouble() * (0.25)) + (0.75);
        double[][] ret_val = {
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, a, 0, a, 0, 0, 0},
            {0, 0, a, 0, 0, 0, a, 0, 0},
            {0, a, 0, 0, 0, 0, 0, a, 0},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {0, a, 0, 0, 0, 0, 0, a, 0},
            {0, 0, a, 0, 0, 0, a, 0, 0},
            {0, 0, 0, a, 0, a, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0}
        };
        return ret_val;
    }
    public static double[][] generateC() {
        double a = (r.nextDouble() * (0.25)) + (0.75);
        double[][] ret_val = {
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {a, a, a, a, a, a, a, a, a},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0}
        };
        return ret_val;
    }
    public static double[][] generateD() {
        double a = (r.nextDouble() * (0.25)) + (0.75);
        double[][] ret_val = {
            {a, 0, 0, 0, 0, 0, 0, 0, a},
            {0, a, 0, 0, 0, 0, 0, a, 0},
            {0, 0, a, 0, 0, 0, a, 0, 0},
            {0, 0, 0, a, 0, a, 0, 0, 0},
            {0, 0, 0, 0, a, 0, 0, 0, 0},
            {0, 0, 0, a, 0, a, 0, 0, 0},
            {0, 0, a, 0, 0, 0, a, 0, 0},
            {0, a, 0, 0, 0, 0, 0, a, 0},
            {a, 0, 0, 0, 0, 0, 0, 0, a},
        };
        return ret_val;
    }
}
//(r.nextDouble() * (.25)) + (0.75)
