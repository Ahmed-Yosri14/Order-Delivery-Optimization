package FuzzyLogic.Operators;


public class OrSum implements LogicalOperator {
    @Override
    public double apply(double a, double b) {
        return a + b - (a * b);
    }
}

