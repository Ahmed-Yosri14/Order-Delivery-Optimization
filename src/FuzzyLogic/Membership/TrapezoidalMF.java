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
        // implement
        return 0.0;
    }

}
