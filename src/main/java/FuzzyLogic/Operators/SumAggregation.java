package FuzzyLogic.Operators;

public class SumAggregation implements AggregationOperator {
    @Override
    public double apply(double value1, double value2) {
        return Math.min(1.0, value1 + value2); // Bounded sum
    }
}