package FuzzyLogic.Operators;

public class AndProduct implements LogicalOperator {
    @Override
    public double apply(double a, double b) {
        return a * b;
    }

}

