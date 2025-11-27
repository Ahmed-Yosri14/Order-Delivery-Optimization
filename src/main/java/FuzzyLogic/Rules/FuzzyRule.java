package FuzzyLogic.Rules;

import FuzzyLogic.Variable.FuzzyVariable;
import FuzzyLogic.Operators.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuzzyRule {
    private List<FuzzyCondition> antecedents;
    private String logicOperator; // "AND" or "OR" - for backward compatibility
    private List<String> operatorSequence; // For mixed operators between conditions
    private FuzzyConsequent consequent;
    private double weight;
    private double firingStrength;

    private LogicalOperator andOperator;
    private LogicalOperator orOperator;

    // Constructor for single operator (backward compatibility)
    public FuzzyRule(String logicOperator, FuzzyConsequent consequent) {
        this.antecedents = new ArrayList<>();
        this.logicOperator = logicOperator.toUpperCase();
        this.operatorSequence = null;
        this.consequent = consequent;
        this.weight = 1.0;
        this.firingStrength = 0.0;
        this.andOperator = new And();
        this.orOperator = new Or();
    }

    // Constructor for mixed operators
    public FuzzyRule(List<String> operatorSequence, FuzzyConsequent consequent) {
        this.antecedents = new ArrayList<>();
        this.logicOperator = null;
        this.operatorSequence = new ArrayList<>(operatorSequence);
        this.consequent = consequent;
        this.weight = 1.0;
        this.firingStrength = 0.0;
        this.andOperator = new And();
        this.orOperator = new Or();
    }

    public FuzzyRule(String logicOperator, FuzzyConsequent consequent,
                     LogicalOperator andOperator, LogicalOperator orOperator) {
        this(logicOperator, consequent);
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }

    public FuzzyRule(List<String> operatorSequence, FuzzyConsequent consequent,
                     LogicalOperator andOperator, LogicalOperator orOperator) {
        this(operatorSequence, consequent);
        this.andOperator = andOperator;
        this.orOperator = orOperator;
    }


    // Add a condition to the antecedents
    public void addAntecedent(FuzzyCondition condition) {
        antecedents.add(condition);
    }


    // Evaluate the rule by computing the firing strength
    public double evaluate(Map<String, FuzzyVariable> inputs) {

        if (antecedents.isEmpty()) {
            firingStrength = 0.0;
            return firingStrength;
        }

        // Evaluate each condition
        List<Double> memberships = new ArrayList<>();
        for (FuzzyCondition condition : antecedents) {
            FuzzyVariable override = null;
            if (inputs != null) {
                String varName = condition.getVariable().getName();
                if (inputs.containsKey(varName)) {
                    override = inputs.get(varName);
                }
            }
            double membership = condition.evaluate(override);
            memberships.add(membership);
        }

        // Combine memberships based on operator type
        if (usesMixedOperators()) {
            firingStrength = combineWithMixedOperators(memberships);
        } else {
            // Backward compatibility - single operator
            if (logicOperator.equals("AND")) {
                firingStrength = combineWithAnd(memberships);
            } else if (logicOperator.equals("OR")) {
                firingStrength = combineWithOr(memberships);
            } else {
                throw new IllegalArgumentException("Invalid logic operator: " + logicOperator);
            }
        }

        // Apply rule weight
        firingStrength *= weight;

        return firingStrength;
    }

    // Check if this rule uses mixed operators
    public boolean usesMixedOperators() {
        return operatorSequence != null && !operatorSequence.isEmpty();
    }

    // Combine memberships using mixed operators in sequence
    private double combineWithMixedOperators(List<Double> memberships) {
        if (memberships.size() == 1) {
            return memberships.get(0);
        }

        if (operatorSequence.size() != memberships.size() - 1) {
            throw new IllegalStateException("Operator sequence length must be conditions length - 1");
        }

        double result = memberships.get(0);
        for (int i = 0; i < operatorSequence.size(); i++) {
            String operator = operatorSequence.get(i);
            double nextMembership = memberships.get(i + 1);

            if ("AND".equalsIgnoreCase(operator)) {
                result = andOperator.apply(result, nextMembership);
            } else if ("OR".equalsIgnoreCase(operator)) {
                result = orOperator.apply(result, nextMembership);
            } else {
                throw new IllegalArgumentException("Invalid operator in sequence: " + operator);
            }
        }

        return result;
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


    public List<FuzzyCondition> getAntecedents() {
        return antecedents;
    }

    public String getLogicOperator() {
        return logicOperator;
    }

    public List<String> getOperatorSequence() {
        return operatorSequence != null ? new ArrayList<>(operatorSequence) : null;
    }

    public FuzzyConsequent getConsequent() {
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
            FuzzyCondition condition = antecedents.get(i);
            String prefix = condition.isNegated() ? "NOT " : "";
            sb.append(condition.getVariable().getName())
              .append(" is ")
              .append(prefix)
              .append(condition.getLinguisticTerm());

            if (i < antecedents.size() - 1) {
                if (usesMixedOperators() && i < operatorSequence.size()) {
                    sb.append(" ").append(operatorSequence.get(i)).append(" ");
                } else {
                    sb.append(" ").append(logicOperator).append(" ");
                }
            }
        }

        sb.append(" THEN ").append(consequent.toString());
        sb.append(" [Weight: ").append(String.format("%.2f", weight))
                .append(", Firing: ").append(String.format("%.3f", firingStrength))
                .append("]");

        return sb.toString();
    }
}
