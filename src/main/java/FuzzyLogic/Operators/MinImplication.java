package FuzzyLogic.Operators;

public class MinImplication implements LogicalOperator {
    @Override
    public double apply(double membershipValue, double firingStrength) {
        return Math.min(membershipValue, firingStrength);
    }
}