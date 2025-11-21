package FuzzyLogic.Membership;

public class GaussianMF implements MembershipFunction {
    private double center, sigma;

    public GaussianMF(double center, double sigma) {
        if (sigma <= 0) {
            throw new IllegalArgumentException("Sigma must be greater than zero.");
        }
        this.center = center;
        this.sigma = sigma;
    }

    @Override
    public double getMembership(double x) {
        double diff = x - center;
        return Math.exp(-(diff * diff) / (2 * sigma * sigma));
    }
}
