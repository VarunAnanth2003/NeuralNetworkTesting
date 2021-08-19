package Other.FunctionClasses.Cost;

public interface CostFunctionInterface {
    public double calculateOriginal(double[] result, double[] desiredValues);

    public double[] calculateDerivative(double[] result, double[] desiredValues);
}