package NeuralNetwork.Core;

import NeuralNetwork.Layers.Layer;
import NeuralNetwork.Loss.Loss;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    private List<Layer> layers = new ArrayList<>();

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public double[] forward(double[] input) {
        for (Layer layer : layers) {
            input = layer.forward(input);
        }
        return input;
    }

    public void backward(double[] lossGrad) {
        for (int i = layers.size() - 1; i >= 0; i--) {
            lossGrad = layers.get(i).backward(lossGrad);
        }
    }

    public void update(double learningRate) {
        for (Layer layer : layers) {
            layer.update(learningRate);
        }
    }

    public void train(double[][] X, double[][] y,
                      Loss lossFn, int epochs, double lr) {

        for (int epoch = 0; epoch < epochs; epoch++) {
            double totalLoss = 0;

            for (int i = 0; i < X.length; i++) {
                double[] yPred = forward(X[i]);
                totalLoss += lossFn.forward(y[i], yPred);

                double[] grad = lossFn.backward(y[i], yPred);
                backward(grad);
                update(lr);
            }

            System.out.println("Epoch " + epoch +
                    " Loss: " + totalLoss / X.length);
        }
    }
}
