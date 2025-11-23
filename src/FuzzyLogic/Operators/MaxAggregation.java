package FuzzyLogic.Operators;

public class MaxAggregation implements LogicalOperator {
    @Override
    public double apply(double value1, double value2) {
        return Math.max(value1, value2);
    }
}