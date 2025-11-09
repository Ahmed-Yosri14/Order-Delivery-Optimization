package FuzzyLogic.Membership;

public class TriangularMF implements MembershipFunction {
    private double left, peak, right;
    
    public TriangularMF(double left, double peak, double right) {
        this.left = left;
        this.peak = peak;
        this.right = right;
    }



    @Override
    public double getMembership(double value) {
        if (value <= left|| value >= peak) return 0.0;
        else if (value == right) return 1.0;
        else if (value > left && value < right) return (value - left) / (right - left);
        else return (peak - value) / (peak - right);
    }

}
