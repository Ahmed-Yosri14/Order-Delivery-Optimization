package FuzzyLogic.Membership;

public class TriangularMF implements MembershipFunction {
    private double left, peak, right;
    
    public TriangularMF(double left, double peak, double right) {
        this.left = left;
        this.peak = peak;
        this.right = right;
    }

    @Override
    public double getMembership(double x) {
        // implement
        return 0.0;
    }

}
