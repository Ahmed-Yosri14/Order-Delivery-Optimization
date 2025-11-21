package FuzzyLogic.Membership;

public class TriangularMF implements MembershipFunction {
    private final double left;
    private final double peak;
    private final double right;

    public TriangularMF(double left, double peak, double right) {
        this.left = left;
        this.peak = peak;
        this.right = right;
    }


    @Override
    public double getMembership(double value) {
        // Peak must be checked first (handles left/right-angled triangles)
        if (value == peak)
            return 1.0;

        if (value <= left || value >= right)
            return 0.0;

        // Rising slope
        if (value > left && value < peak)
            return (value - left) / (peak - left);

        // Falling slope
        if (value > peak && value < right)
            return (right - value) / (right - peak);

        return 0.0;
    }
}
