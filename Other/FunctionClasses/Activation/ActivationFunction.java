package Other.FunctionClasses.Activation;

public class ActivationFunction {
    private ActivationFunctionInterface selectedFunction;
    private ActivationOptions ao;

    public ActivationFunction(ActivationOptions f) {
        ao = f;
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
                selectedFunction = new ActivationFunctionInterface() {

                    @Override
                    public double calculateOriginal(double input) {
                        return input > 0 ? input : 0;
                    }

                    @Override
                    public double calculateDerivative(double input) {
                        return input > 0 ? 1 : 0;
                    }

                };
                break;
            case LEAKY_RE_LU:
                selectedFunction = new ActivationFunctionInterface() {

                    @Override
                    public double calculateOriginal(double input) {
                        return input > 0 ? input : 0.1*input;
                    }

                    @Override
                    public double calculateDerivative(double input) {
                        return input > 0 ? 1 : 0.1;
                    }

                };
                break;
            default:
        }
    }

    public ActivationFunctionInterface getFunction() {
        return selectedFunction;
    }

    public String getAo() {
        return ao.toString();
    }

    public static ActivationFunction convertStringToObject(String s) {
        return new ActivationFunction(ActivationOptions.valueOf(s));
    }
}