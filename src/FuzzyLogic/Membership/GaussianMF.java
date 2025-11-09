package FuzzyLogic.Membership;

public class GaussianMF implements MembershipFunction {
    private double center, sigma;

    public GaussianMF(double center, double sigma) {
        this.center = center;
        this.sigma = sigma;
    }

    @Override
    public double getMembership(double x) {
        return Math.exp(-Math.pow(x - center, 2) / (2 * sigma * sigma));
    }

}
