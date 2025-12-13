package NeuralNetwork.Activations;

public class Tanh implements Activation {
    private double[] output;

    @Override
    public double[] forward(double[] input) {
        output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.tanh(input[i]);
        }
        return output;
    }

    @Override
    public double[] backward(double[] gradOutput) {
        double[] gradInput = new double[gradOutput.length];
        for (int i = 0; i < gradOutput.length; i++) {
            gradInput[i] = gradOutput[i] * (1 - output[i] * output[i]);
        }
        return gradInput;
    }
}