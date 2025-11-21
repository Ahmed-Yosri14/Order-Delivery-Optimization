package FuzzyLogic.Rules;

import FuzzyLogic.Variable.FuzzyVariable;

/**
 * Represents the consequent (THEN part) of a fuzzy rule.
 * Supports two types:
 * - FUZZY: For Mamdani inference (output variable + linguistic term)
 * - CRISP: For Sugeno inference (direct numeric value)
 */
public class FuzzyConsequent<T extends Enum<T>> {
    public enum Type {
        FUZZY,  // Mamdani: "Water Duration is Long"
        CRISP   // Sugeno: "Water Duration = 20"
    }

    private final Type type;
    private final FuzzyVariable<T> outputVariable;
    private final T linguisticTerm;
    private final double crispValue;

    // Constructor for Fuzzy (Mamdani) consequent
    public FuzzyConsequent(FuzzyVariable<T> outputVariable, T linguisticTerm) {
        this.type = Type.FUZZY;
        this.outputVariable = outputVariable;
        this.linguisticTerm = linguisticTerm;
        this.crispValue = 0.0;
    }

    // Constructor for Crisp (Sugeno) consequent
    public FuzzyConsequent(FuzzyVariable<T> outputVariable, double crispValue) {
        this.type = Type.CRISP;
        this.outputVariable = outputVariable;
        this.linguisticTerm = null;
        this.crispValue = crispValue;
    }

    @Override
    public String toString() {
        if (type == Type.FUZZY) {
            assert linguisticTerm != null;
            return outputVariable.getName() + " is " + linguisticTerm.name();
        } else {
            return outputVariable.getName() + " = " + crispValue;
        }
    }

    public Type getType() {
        return type;
    }

    public FuzzyVariable<T> getOutputVariable() {
        return outputVariable;
    }

    public T getLinguisticTerm() {
        return linguisticTerm;
    }

    public double getCrispValue() {
        return crispValue;
    }

    public boolean isFuzzy() {
        return type == Type.FUZZY;
    }

    public boolean isCrisp() {
        return type == Type.CRISP;
    }
}
