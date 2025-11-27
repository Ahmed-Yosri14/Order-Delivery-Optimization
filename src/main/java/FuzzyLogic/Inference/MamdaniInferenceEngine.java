package FuzzyLogic.Inference;

import FuzzyLogic.Membership.AggregatedFuzzySet;
import FuzzyLogic.Membership.MembershipFunction;
import FuzzyLogic.Membership.NegatedMembershipFunction;
import FuzzyLogic.Operators.*;
import FuzzyLogic.Rules.FuzzyConsequent;
import FuzzyLogic.Rules.FuzzyRule;
import FuzzyLogic.Variable.FuzzyVariable;

import java.util.List;
import java.util.Map;


public class MamdaniInferenceEngine {
    private final Map<String, FuzzyVariable> variables;
    private final List<FuzzyRule> rules;

    private LogicalOperator implicationOperator;
    private LogicalOperator aggregationOperator;
    private DefuzzificationMethod defuzzificationMethod;

    private double outputMin;
    private double outputMax;
    private int resolution;


    public MamdaniInferenceEngine(Map<String, FuzzyVariable> variables, List<FuzzyRule> rules,
                                  double outputMin, double outputMax) {
        this.variables = variables;
        this.rules = rules;
        this.outputMin = outputMin;
        this.outputMax = outputMax;
        this.resolution = 1000;

        this.implicationOperator = new MinImplication();
        this.aggregationOperator = new MaxAggregation();
        this.defuzzificationMethod = DefuzzificationMethod.CENTROID;
    }

    public MamdaniInferenceEngine(Map<String, FuzzyVariable> variables, List<FuzzyRule> rules,
                                  double outputMin, double outputMax, int resolution,
                                  LogicalOperator implicationOperator,
                                  LogicalOperator aggregationOperator,
                                  DefuzzificationMethod defuzzificationMethod) {
        this.variables = variables;
        this.rules = rules;
        this.outputMin = outputMin;
        this.outputMax = outputMax;
        this.resolution = resolution;
        this.implicationOperator = implicationOperator;
        this.aggregationOperator = aggregationOperator;
        this.defuzzificationMethod = defuzzificationMethod;
    }

    public double evaluate() {
        System.out.println("\n=== TRUE Mamdani Inference Engine ===");
        System.out.println("Implication: " + implicationOperator.getClass().getSimpleName());
        System.out.println("Aggregation: " + aggregationOperator.getClass().getSimpleName());
        System.out.println("Defuzzification: " + defuzzificationMethod);

        // Create aggregated fuzzy set
        AggregatedFuzzySet aggregatedSet = new AggregatedFuzzySet(
                outputMin, outputMax, resolution, aggregationOperator
        );

        // Step 1 & 2: Evaluate rules and apply implication
        for (int i = 0; i < rules.size(); i++) {
            FuzzyRule rule = rules.get(i);
            double firingStrength = rule.evaluate(variables);
            FuzzyConsequent consequent = rule.getConsequent();

            if (!consequent.isFuzzy()) {
                throw new IllegalStateException(
                        "Mamdani inference requires fuzzy consequents, not crisp values"
                );
            }

            // Get the output membership function
            String linguisticTerm = consequent.getLinguisticTerm();
            MembershipFunction outputMF = consequent.getOutputVariable()
                    .getMembershipFunction(linguisticTerm);

            if (consequent.isNegated()) {
                outputMF = new NegatedMembershipFunction(outputMF);
            }

            System.out.println("[Mamdani] Rule " + (i + 1) + ": " + rule +
                    " -> firing=" + String.format("%.3f", firingStrength));

            // Add implied set to aggregation
            aggregatedSet.addImpliedSet(outputMF, firingStrength, implicationOperator);
        }

        // Step 3: Aggregation is done within AggregatedFuzzySet

        // Step 4: Defuzzification
        double crispOutput;
        if (defuzzificationMethod == DefuzzificationMethod.CENTROID) {
            crispOutput = aggregatedSet.calculateCentroid();
        } else if (defuzzificationMethod == DefuzzificationMethod.MEAN_OF_MAXIMUM) {
            crispOutput = aggregatedSet.calculateMeanOfMaximum();
        } else {
            throw new IllegalStateException("Unsupported defuzzification method: " +
                    defuzzificationMethod);
        }

        System.out.println("[Mamdani] Final crisp output = " + String.format("%.3f", crispOutput));
        return crispOutput;
    }

    public AggregatedFuzzySet getAggregatedSet() {
        AggregatedFuzzySet aggregatedSet = new AggregatedFuzzySet(
                outputMin, outputMax, resolution, aggregationOperator
        );

        for (FuzzyRule rule : rules) {
            double firingStrength = rule.evaluate(variables);
            FuzzyConsequent consequent = rule.getConsequent();

            if (consequent.isFuzzy()) {
                String linguisticTerm = consequent.getLinguisticTerm();
                MembershipFunction outputMF = consequent.getOutputVariable()
                        .getMembershipFunction(linguisticTerm);
                if (consequent.isNegated()) {
                    outputMF = new NegatedMembershipFunction(outputMF);
                }
                aggregatedSet.addImpliedSet(outputMF, firingStrength, implicationOperator);
            }
        }

        return aggregatedSet;
    }

    public void setImplicationOperator(LogicalOperator implicationOperator) {
        this.implicationOperator = implicationOperator;
    }

    public void setAggregationOperator(LogicalOperator aggregationOperator) {
        this.aggregationOperator = aggregationOperator;
    }

    public void setDefuzzificationMethod(DefuzzificationMethod defuzzificationMethod) {
        this.defuzzificationMethod = defuzzificationMethod;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}