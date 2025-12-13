package NeuralNetwork.Activations;

public interface Activation {
    double[] forward(double[] input);
    double[] backward(double[] gradOutput);
}
