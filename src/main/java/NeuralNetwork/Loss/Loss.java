package NeuralNetwork.Loss;

public interface Loss {
    double forward(double[] yTrue, double[] yPred);
    double[] backward(double[] yTrue, double[] yPred);
}
