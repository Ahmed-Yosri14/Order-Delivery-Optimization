package FuzzyLogic.Membership;

public class GaussianMF implements MembershipFunction {
    private double center, sigma;

    public GaussianMF(double center, double sigma) {
        this.center = center;
        this.sigma = sigma;
    }

    @Override
    public double getMembership(double x) {
        // implement
        return 0.0;
    }

}
