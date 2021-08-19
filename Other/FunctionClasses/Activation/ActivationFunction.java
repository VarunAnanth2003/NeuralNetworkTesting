package Other.FunctionClasses.Activation;

public class ActivationFunction {
    private ActivationFunctionInterface selectedFunction;

    public ActivationFunction(ActivationOptions f) {
        switch (f) {
            case SIGMOID:
                selectedFunction = new ActivationFunctionInterface() {

                    @Override
                    public double calculateOriginal(double input) {
                        return 1 / (1 + Math.exp(-input));
                    }

                    @Override
                    public double calculateDerivative(double input) {
                        return (calculateOriginal(input) * (1 - calculateOriginal(input)));
                    }

                };
                break;
            case RE_LU:
                // TODO:
                break;
            case LEAKY_RE_LU:
                // TODO:
                break;
            default:
        }
    }

    public ActivationFunctionInterface getFunction() {
        return selectedFunction;
    }
}