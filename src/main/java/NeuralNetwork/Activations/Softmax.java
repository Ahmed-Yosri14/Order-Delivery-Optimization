package NeuralNetwork.Activations;

public class Softmax implements Activation {
    private double[] output;

    @Override
    public double[] forward(double[] input) {
        output = new double[input.length];

        double max = input[0];
        for (double v : input) {
            if (v > max) max = v;
        }

        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.exp(input[i] - max);
            sum += output[i];
        }

        for (int i = 0; i < output.length; i++) {
            output[i] /= sum;
        }

        return output;
    }

    @Override
    public double[] backward(double[] gradOutput) {
        return gradOutput;
    }

    public double[] getOutput() {
        return output;
    }
}
