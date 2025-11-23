package FuzzyLogic.Operators;

public class MinImplication implements ImplicationOperator {
    @Override
    public double apply(double membershipValue, double firingStrength) {
        return Math.min(membershipValue, firingStrength);
    }
}