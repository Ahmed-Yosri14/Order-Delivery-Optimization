package NeuralNetwork.init;

import java.util.Random;

public class RandomUniform implements Initializer {
    private Random random = new Random();

    @Override
    public double[][] initialize(int inputSize, int outputSize) {
        double[][] weights = new double[inputSize][outputSize];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weights[i][j] = random.nextDouble() - 0.5;
            }
        }
        return weights;
    }
}