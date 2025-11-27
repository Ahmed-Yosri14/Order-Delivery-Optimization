package FuzzyLogic.Rules;

import FuzzyLogic.Variable.FuzzyVariable;

public class FuzzyCondition {
    private final FuzzyVariable<?> variable;
    private final String linguisticTerm;
    private final boolean negated;
    private double membership;

    public FuzzyCondition(FuzzyVariable<?> variable, String linguisticTerm) {
        this(variable, linguisticTerm, false);
    }

    public FuzzyCondition(FuzzyVariable<?> variable, String linguisticTerm, boolean negated) {
        this.variable = variable;
        this.linguisticTerm = linguisticTerm;
        this.negated = negated;
        this.membership = 0.0;
    }

    public double evaluate() {
        membership = variable.getMembershipByName(linguisticTerm);
        return negated ? 1.0 - membership : membership;
    }

    public double evaluate(FuzzyVariable<?> overrideVar) {
        if (overrideVar == null) {
            return evaluate();
        }
        membership = overrideVar.getMembershipByName(linguisticTerm);
        return negated ? 1.0 - membership : membership;
    }

    public FuzzyVariable<?> getVariable() {
        return variable;
    }

    public String getLinguisticTerm() {
        return linguisticTerm;
    }

    public double getMembership() {
        return membership;
    }

    public boolean isNegated() {
        return negated;
    }

    @Override
    public String toString() {
        String prefix = negated ? "NOT " : "";
        double shownMembership = negated ? 1.0 - membership : membership;
        return prefix + variable.getName() + " is " + linguisticTerm + " (" + String.format("%.3f", shownMembership) + ")";
    }
}