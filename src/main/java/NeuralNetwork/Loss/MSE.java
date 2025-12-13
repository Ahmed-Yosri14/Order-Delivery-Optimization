package NeuralNetwork.Loss;

public class MSE implements Loss {

    @Override
    public double forward(double[] yTrue, double[] yPred) {
        double sum = 0;
        for (int i = 0; i < yTrue.length; i++) {
            sum += Math.pow(yTrue[i] - yPred[i], 2);
        }
        return sum / yTrue.length;
    }

    @Override
    public double[] backward(double[] yTrue, double[] yPred) {
        double[] grad = new double[yTrue.length];
        for (int i = 0; i < yTrue.length; i++) {
            grad[i] = 2 * (yPred[i] - yTrue[i]) / yTrue.length;
        }
        return grad;
    }
}
