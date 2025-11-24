package FuzzyLogic.Rules;

import FuzzyLogic.Variable.FuzzyVariable;


public class FuzzyConsequent {
    public enum Type {
        FUZZY,  // Mamdani: "Water Duration is Long"
        CRISP   // Sugeno: "Water Duration = 20"
    }

    private Type type;
    private FuzzyVariable outputVariable;
    private String linguisticTerm;
    private double crispValue;

    // Constructor for Fuzzy (Mamdani) consequent
    public FuzzyConsequent(FuzzyVariable outputVariable, String linguisticTerm) {
        this.type = Type.FUZZY;
        this.outputVariable = outputVariable;
        this.linguisticTerm = linguisticTerm;
        this.crispValue = 0.0;
    }

    // Constructor for Crisp (Sugeno) consequent
    public FuzzyConsequent(FuzzyVariable outputVariable, double crispValue) {
        this.type = Type.CRISP;
        this.outputVariable = outputVariable;
        this.linguisticTerm = null;
        this.crispValue = crispValue;
    }

    public Type getType() {
        return type;
    }

    public FuzzyVariable getOutputVariable() {
        return outputVariable;
    }

    public String getLinguisticTerm() {
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

    @Override
    public String toString() {
        if (type == Type.FUZZY) {
            return outputVariable.getName() + " is " + linguisticTerm;
        } else {
            return outputVariable.getName() + " = " + crispValue;
        }
    }
}
