package NeuralNetwork.Activations;

public class ReLU implements Activation {
    private boolean[] mask;

    @Override
    public double[] forward(double[] input) {
        mask = new boolean[input.length];
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            mask[i] = input[i] > 0;
            output[i] = mask[i] ? input[i] : 0;
        }
        return output;
    }

    @Override
    public double[] backward(double[] gradOutput) {
        double[] gradInput = new double[gradOutput.length];
        for (int i = 0; i < gradOutput.length; i++) {
            gradInput[i] = mask[i] ? gradOutput[i] : 0;
        }
        return gradInput;
    }
}