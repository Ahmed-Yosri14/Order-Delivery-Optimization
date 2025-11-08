package FuzzyLogic.Operators;

public class OrMax implements LogicalOperator {
    @Override
    public double apply(double a, double b) {
        return Math.max(a, b);
    }

}

