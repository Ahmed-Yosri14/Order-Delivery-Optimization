package FuzzyLogic.Operators;

public class ProductImplication implements ImplicationOperator {
    @Override
    public double apply(double membershipValue, double firingStrength) {
        return membershipValue * firingStrength;
    }
}