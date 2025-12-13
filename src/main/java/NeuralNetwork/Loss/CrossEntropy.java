package NeuralNetwork.Loss;

public class CrossEntropy implements Loss {

    @Override
    public double forward(double[] yTrue, double[] yPred) {
        double loss = 0.0;
        double epsilon = 1e-12;

        for (int i = 0; i < yTrue.length; i++) {
            loss += -yTrue[i] * Math.log(yPred[i] + epsilon);
        }
        return loss;
    }

    @Override
    public double[] backward(double[] yTrue, double[] yPred) {
        double[] grad = new double[yTrue.length];
        for (int i = 0; i < yTrue.length; i++) {
            grad[i] = yPred[i] - yTrue[i];
        }
        return grad;
    }
}
