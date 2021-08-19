package Other.FunctionClasses.Cost;

public class CostFunction {
    private CostFunctionInterface selectedFunction;

    public CostFunction(CostOptions f) {
        switch (f) {
            case QUADRATIC:
                selectedFunction = new CostFunctionInterface() {

                    @Override
                    public double calculateOriginal(double[] result, double[] desiredValues) {
                        double ret_val = 0;
                        for (int i = 0; i < result.length; i++) {
                            try {
                                ret_val += Math.pow(result[i] - desiredValues[i], 2);
                            } catch (IndexOutOfBoundsException e) {
                                System.err.println("Desired values array does not match the network result array");
                                e.printStackTrace();
                            }
                        }
                        return ret_val;
                    }

                    @Override
                    public double[] calculateDerivative(double[] result, double[] desiredValues) {
                        double[] ret_val = new double[result.length];
                        for (int i = 0; i < result.length; i++) {
                            try {
                                ret_val[i] = 2 * (result[i] - desiredValues[i]);
                            } catch (IndexOutOfBoundsException e) {
                                System.err.println("Desired values array does not match the network result array");
                                e.printStackTrace();
                            }
                        }
                        return ret_val;
                    }

                };
                break;
            default:
        }
    }

    public CostFunctionInterface getFunction() {
        return selectedFunction;
    }
}
