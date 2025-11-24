package FuzzyLogic.Operators;

public class ProductImplication implements LogicalOperator {
    @Override
    public double apply(double membershipValue, double firingStrength) {
        return membershipValue * firingStrength;
    }
}