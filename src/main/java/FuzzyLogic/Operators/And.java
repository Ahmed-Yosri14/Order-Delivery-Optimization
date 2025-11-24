package FuzzyLogic.Operators;

public class And implements LogicalOperator {
    @Override
    public double apply(double a, double b) {
        return Math.min(a, b);
    }

}

