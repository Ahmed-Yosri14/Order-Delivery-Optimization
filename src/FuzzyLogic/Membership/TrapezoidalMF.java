package FuzzyLogic.Membership;

public class TrapezoidalMF implements MembershipFunction {
    private double leftBase, leftPeak, rightPeak, rightBase;
    
    public TrapezoidalMF(double leftBase, double leftPeak, double rightPeak, double rightBase) {
        this.leftBase = leftBase;
        this.leftPeak = leftPeak;
        this.rightPeak = rightPeak;
        this.rightBase = rightBase;
    }
    
    @Override
    public double getMembership(double x) {
        if (x <= leftBase || x >= rightBase) return 0.0;
        else if (x >= leftPeak && x <= rightPeak) return 1.0;
        else if (x > leftBase && x < leftPeak) return (x - leftBase) / (leftPeak - leftBase);
        else return (rightBase - x) / (rightBase - rightPeak);
    }

}
