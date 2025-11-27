package FuzzyLogic.Membership;

import FuzzyLogic.Operators.LogicalOperator;
import FuzzyLogic.Operators.MaxAggregation;

import java.util.ArrayList;
import java.util.List;

public class AggregatedFuzzySet {

    private static class ImpliedSet {
        MembershipFunction membershipFunction;
        double firingStrength;
        LogicalOperator implicationOperator;

        ImpliedSet(MembershipFunction mf, double firing, LogicalOperator impl) {
            this.membershipFunction = mf;
            this.firingStrength = firing;
            this.implicationOperator = impl;
        }
    }

    private List<ImpliedSet> impliedSets;
    private LogicalOperator aggregationOperator;
    private double minDomain;
    private double maxDomain;
    private int resolution;


    //Constructor with default MAX aggregation
    public AggregatedFuzzySet(double minDomain, double maxDomain, int resolution) {
        this.impliedSets = new ArrayList<>();
        this.aggregationOperator = new MaxAggregation();
        this.minDomain = minDomain;
        this.maxDomain = maxDomain;
        this.resolution = resolution;
    }


    //Constructor with custom aggregation operator
    public AggregatedFuzzySet(double minDomain, double maxDomain, int resolution,
                              LogicalOperator aggregationOperator) {
        this(minDomain, maxDomain, resolution);
        this.aggregationOperator = aggregationOperator;
    }


    //Add a rule's output (implied fuzzy set) to the aggregation
    public void addImpliedSet(MembershipFunction mf, double firingStrength,
                              LogicalOperator implicationOperator) {
        if (firingStrength > 0.0) {
            impliedSets.add(new ImpliedSet(mf, firingStrength, implicationOperator));
        }
    }


    // Get the aggregated membership value at a specific point

    public double getMembershipAt(double x) {
        if (impliedSets.isEmpty()) {
            return 0.0;
        }

        // Start with first implied set
        double result = impliedSets.get(0).implicationOperator.apply(
                impliedSets.get(0).membershipFunction.getMembership(x),
                impliedSets.get(0).firingStrength
        );

        // Aggregate with remaining implied sets
        for (int i = 1; i < impliedSets.size(); i++) {
            ImpliedSet impliedSet = impliedSets.get(i);
            double impliedMembership = impliedSet.implicationOperator.apply(
                    impliedSet.membershipFunction.getMembership(x),
                    impliedSet.firingStrength
            );
            result = aggregationOperator.apply(result, impliedMembership);
        }

        return result;
    }


    // Calculate centroid of the aggregated fuzzy set
    public double calculateCentroid() {
        if (impliedSets.isEmpty()) {
            return 0.0;
        }

        double step = (maxDomain - minDomain) / resolution;
        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i <= resolution; i++) {
            double x = minDomain + i * step;
            double membership = getMembershipAt(x);
            numerator += x * membership;
            denominator += membership;
        }

        return (denominator == 0.0) ? (minDomain + maxDomain) / 2.0 : numerator / denominator;
    }


    // Calculate Mean of Maximum of the aggregated fuzzy set
    public double calculateMeanOfMaximum() {
        if (impliedSets.isEmpty()) {
            return 0.0;
        }

        double step = (maxDomain - minDomain) / resolution;
        double maxMembership = 0.0;
        double sumOfMaxX = 0.0;
        int countOfMax = 0;

        // Find maximum membership value
        for (int i = 0; i <= resolution; i++) {
            double x = minDomain + i * step;
            double membership = getMembershipAt(x);
            if (membership > maxMembership) {
                maxMembership = membership;
            }
        }

        // Find all x values with maximum membership
        double tolerance = 0.001; // Small tolerance for floating point comparison
        for (int i = 0; i <= resolution; i++) {
            double x = minDomain + i * step;
            double membership = getMembershipAt(x);
            if (Math.abs(membership - maxMembership) < tolerance) {
                sumOfMaxX += x;
                countOfMax++;
            }
        }

        return (countOfMax > 0) ? sumOfMaxX / countOfMax : (minDomain + maxDomain) / 2.0;
    }

    public boolean isEmpty() {
        return impliedSets.isEmpty();
    }

    public void setAggregationOperator(LogicalOperator aggregationOperator) {
        this.aggregationOperator = aggregationOperator;
    }
}