package FuzzyLogic.Rules;

import FuzzyLogic.Variable.FuzzyVariable;

public class FuzzyCondition<T extends Enum<T>> {
    private final FuzzyVariable<T> variable;
    private final T linguisticTerm;
    private double membership;

    public FuzzyCondition(FuzzyVariable<T> variable, T linguisticTerm) {
        this.variable = variable;
        this.linguisticTerm = linguisticTerm;
        this.membership = 0.0;
    }

    // Evaluates the condition by getting the membership degree of the
    // variable's current value in the specified linguistic term.
    public double evaluate() {
        membership = variable.getMembership(linguisticTerm);
        return membership;
    }

    public FuzzyVariable<T> getVariable() {
        return variable;
    }

    public T getLinguisticTerm() {
        return linguisticTerm;
    }

    public double getMembership() {
        return membership;
    }

    @Override
    public String toString() {
        return variable.getName() + " is " + linguisticTerm.name() + " (" + String.format("%.3f", membership) + ")";
    }
}
