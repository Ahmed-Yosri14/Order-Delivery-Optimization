package FuzzyLogic.Rules;

import FuzzyLogic.Variable.FuzzyVariable;

public class FuzzyCondition {
    private final FuzzyVariable<?> variable;
    private final String linguisticTerm;
    private double membership;

    public FuzzyCondition(FuzzyVariable<?> variable, String linguisticTerm) {
        this.variable = variable;
        this.linguisticTerm = linguisticTerm;
        this.membership = 0.0;
    }

    // Evaluates the condition by getting the membership degree of the
    // variable's current value in the specified linguistic term.
    public double evaluate() {
        membership = variable.getMembershipByName(linguisticTerm);
        return membership;
    }

    // Evaluate using an overriding variable instance (from inputs map)
    public double evaluate(FuzzyVariable<?> overrideVar) {
        if (overrideVar == null) {
            return evaluate();
        }
        membership = overrideVar.getMembershipByName(linguisticTerm);
        return membership;
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

    @Override
    public String toString() {
        return variable.getName() + " is " + linguisticTerm + " (" + String.format("%.3f", membership) + ")";
    }
}