package NeuralNetwork.init;

import java.util.Random;

public class Xavier implements Initializer {
    private Random random = new Random();

    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double limit = Math.sqrt(6.0 / (inputSize + outputSize));
        double[][] weights = new double[inputSize][outputSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = random.nextDouble() * 2 * limit - limit;
            }
        }
        return weights;
    }
}
