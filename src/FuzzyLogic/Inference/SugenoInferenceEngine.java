package FuzzyLogic.Inference;

import FuzzyLogic.Rules.FuzzyConsequent;
import FuzzyLogic.Rules.FuzzyRule;
import FuzzyLogic.Variable.FuzzyVariable;

import java.util.List;
import java.util.Map;

public class SugenoInferenceEngine {
    private final Map<String, FuzzyVariable> variables;
    private final List<FuzzyRule> rules;

    public SugenoInferenceEngine(Map<String, FuzzyVariable> variables, List<FuzzyRule> rules) {
        this.variables = variables;
        this.rules = rules;
    }

    public double evaluate() {
        System.out.println("\n=== Sugeno Inference Engine ===");
        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < rules.size(); i++) {
            FuzzyRule r = rules.get(i);
            double firing = r.evaluate(variables);
            FuzzyConsequent cons = r.getConsequent();

            double y;
            if (cons.isCrisp()) {
                y = cons.getCrispValue();
            } else {
                y = cons.getOutputVariable().getCentroidOf(cons.getLinguisticTerm());
            }

            System.out.println("[Sugeno] Rule " + (i + 1) + ": " + r + " -> firing=" + firing + ", outputValue=" + y);
            numerator += firing * y;
            denominator += firing;
        }

        double result = (denominator == 0.0) ? 0.0 : (numerator / denominator);
        System.out.println("[Sugeno] Final crisp output = " + result);
        return result;
    }
}

