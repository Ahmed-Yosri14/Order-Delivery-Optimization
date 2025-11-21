package FuzzyLogic.Rules;

import FuzzyLogic.Operators.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete fuzzy rule with antecedents (IF part) and consequent (THEN part).
 * Example: IF Soil is Dry AND Temperature is Hot THEN Water Duration is Long
 */
public class FuzzyRule {
    private final List<FuzzyCondition<?>> antecedents;
    private final String logicOperator; // "AND" or "OR"
    private final FuzzyConsequent<?> consequent;
    private double weight;
    private double firingStrength;

    private LogicalOperator andOperator;
    private LogicalOperator orOperator;

    public FuzzyRule(String logicOperator, FuzzyConsequent<?> consequent) {
        this.antecedents = new ArrayList<>();
        this.logicOperator = logicOperator.toUpperCase();
        this.consequent = consequent;
        this.weight = 1.0;
        this.firingStrength = 0.0;
        // Default operators
        this.andOperator = new AndMin();
        this.orOperator = new OrMax();
    }

    public FuzzyRule(String logicOperator, FuzzyConsequent<?> consequent,
                     LogicalOperator andOperator, LogicalOperator orOperator) {
        this(logicOperator, consequent);
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }


    // Add a condition to the antecedents
    public void addAntecedent(FuzzyCondition<?> condition) {
        antecedents.add(condition);
    }


    // Evaluate the rule by computing the firing strength
    public double evaluate() {

        if (antecedents.isEmpty()) {
            firingStrength = 0.0;
            return firingStrength;
        }

        // Evaluate each condition
        List<Double> memberships = new ArrayList<>();
        for (FuzzyCondition<?> condition : antecedents) {
            double membership = condition.evaluate();
            memberships.add(membership);
        }

        // Combine memberships based on logic operator
        if (logicOperator.equals("AND")) {
            firingStrength = combineWithAnd(memberships);
        } else if (logicOperator.equals("OR")) {
            firingStrength = combineWithOr(memberships);
        } else {
            throw new IllegalArgumentException("Invalid logic operator: " + logicOperator);
        }

        // Apply rule weight
        firingStrength *= weight;

        return firingStrength;
    }

    // Combine memberships using AND operator (min)
    private double combineWithAnd(List<Double> memberships) {
        double result = memberships.get(0);
        for (int i = 1; i < memberships.size(); i++) {
            result = andOperator.apply(result, memberships.get(i));
        }
        return result;
    }


    // Combine memberships using OR operator (max)
    private double combineWithOr(List<Double> memberships) {
        double result = memberships.get(0);
        for (int i = 1; i < memberships.size(); i++) {
            result = orOperator.apply(result, memberships.get(i));
        }
        return result;
    }

    // Getters and Setters

    public List<FuzzyCondition<?>> getAntecedents() {
        return antecedents;
    }

    public String getLogicOperator() {
        return logicOperator;
    }

    public FuzzyConsequent<?> getConsequent() {
        return consequent;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }
        this.weight = weight;
    }


    public double getFiringStrength() {
        return firingStrength;
    }

    public void setAndOperator(LogicalOperator andOperator) {
        this.andOperator = andOperator;
    }

    public void setOrOperator(LogicalOperator orOperator) {
        this.orOperator = orOperator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");

        for (int i = 0; i < antecedents.size(); i++) {
            sb.append(antecedents.get(i).getVariable().getName())
                    .append(" is ")
                    .append(antecedents.get(i).getLinguisticTerm().name());

            if (i < antecedents.size() - 1) {
                sb.append(" ").append(logicOperator).append(" ");
            }
        }

        sb.append(" THEN ").append(consequent.toString());
        sb.append(" [Weight: ").append(String.format("%.2f", weight))
                .append(", Firing: ").append(String.format("%.3f", firingStrength))
                .append("]");

        return sb.toString();
    }
}

