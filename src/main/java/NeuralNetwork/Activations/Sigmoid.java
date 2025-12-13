package NeuralNetwork.Activations;

public class Sigmoid implements Activation {
    private double[] output;

    @Override
    public double[] forward(double[] input) {
        output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = 1.0 / (1.0 + Math.exp(-input[i]));
        }
        return output;
    }

    @Override
    public double[] backward(double[] gradOutput) {
        double[] gradInput = new double[gradOutput.length];
        for (int i = 0; i < gradOutput.length; i++) {
            gradInput[i] = gradOutput[i] * output[i] * (1 - output[i]);
        }
        return gradInput;
    }
}
