package FuzzyLogic.Operators;

public class AndMin implements LogicalOperator {
    @Override
    public double apply(double a, double b) {
        return Math.min(a, b);
    }

}

