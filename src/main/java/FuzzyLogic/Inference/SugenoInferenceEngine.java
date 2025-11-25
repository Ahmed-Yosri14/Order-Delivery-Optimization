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
        System.out.println("\n=== Sugeno Inference Engine (Zero-Order) ===");

        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < rules.size(); i++) {
            FuzzyRule rule = rules.get(i);
            double firingStrength = rule.evaluate(variables);
            FuzzyConsequent consequent = rule.getConsequent();

            double consequentValue;

            if (consequent.isCrisp()) {
                // Zero-order Sugeno: crisp constant
                consequentValue = consequent.getCrispValue();
            } else {
                // Fallback for fuzzy consequents: use centroid
                // (Not typical Sugeno, but allows flexibility)
                String linguisticTerm = consequent.getLinguisticTerm();
                consequentValue = consequent.getOutputVariable()
                        .getMembershipFunction(linguisticTerm)
                        .getCentroid();
                System.out.println("[Warning] Fuzzy consequent in Sugeno - using centroid");
            }

            System.out.println("[Sugeno] Rule " + (i + 1) + ": " + rule +
                    " -> firing=" + String.format("%.3f", firingStrength) +
                    ", output=" + String.format("%.3f", consequentValue));

            numerator += firingStrength * consequentValue;
            denominator += firingStrength;
        }

        double crispOutput = (denominator == 0.0) ? 0.0 : (numerator / denominator);
        System.out.println("[Sugeno] Final crisp output = " + String.format("%.3f", crispOutput));

        return crispOutput;
    }

    public Map<FuzzyRule, Double> getFiringStrengths() {
        Map<FuzzyRule, Double> firingMap = new java.util.HashMap<>();
        for (FuzzyRule rule : rules) {
            firingMap.put(rule, rule.evaluate(variables));
        }
        return firingMap;
    }
}