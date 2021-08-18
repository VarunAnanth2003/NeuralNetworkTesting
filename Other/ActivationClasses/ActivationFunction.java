package Other.ActivationClasses;
public class ActivationFunction {
    private Function selectedFunction;
    public ActivationFunction(FunctionOptions f) {
        switch(f) {
            case SIGMOID:
            selectedFunction = new Function(){

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
            //TODO:
                break;
            case LEAKY_RE_LU:
            //TODO:
                break;
            default:
        }
    }
    public Function getFunction() {
        return selectedFunction;
    }
}
