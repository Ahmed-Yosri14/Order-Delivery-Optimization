package NeuralNetwork.Layers;

public interface Layer {
    double[] forward(double[] input);
    double[] backward(double[] gradOutput);
    void update(double learningRate);
}
