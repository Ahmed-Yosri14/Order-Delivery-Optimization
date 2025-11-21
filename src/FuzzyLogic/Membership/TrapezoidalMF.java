package FuzzyLogic.Membership;

public class TrapezoidalMF implements MembershipFunction {
    private final double leftBase;
    private final double leftPeak;
    private final double rightPeak;
    private final double rightBase;

    public TrapezoidalMF(double leftBase, double leftPeak, double rightPeak, double rightBase) {
        this.leftBase = leftBase;
        this.leftPeak = leftPeak;
        this.rightPeak = rightPeak;
        this.rightBase = rightBase;
    }

    @Override
    public double getMembership(double x) {
        if (x >= leftPeak && x <= rightPeak) return 1.0;
        if (x <= leftBase || x >= rightBase) return 0.0;
        else if (x > leftBase && x < leftPeak) return (x - leftBase) / (leftPeak - leftBase); // Rising slope
        else return (rightBase - x) / (rightBase - rightPeak); // Falling slope
    }

    @Override
    public double getCentroid() {
        return (leftBase + leftPeak + rightPeak + rightBase) / 4.0;
    }

}
