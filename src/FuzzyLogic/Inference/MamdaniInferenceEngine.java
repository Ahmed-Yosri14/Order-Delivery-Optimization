package FuzzyLogic.Inference;

import FuzzyLogic.Rules.FuzzyConsequent;
import FuzzyLogic.Rules.FuzzyRule;
import FuzzyLogic.Variable.FuzzyVariable;
import FuzzyLogic.Membership.MembershipFunction;

import java.util.List;
import java.util.Map;

public class MamdaniInferenceEngine {
    private final Map<String, FuzzyVariable> variables;
    private final List<FuzzyRule> rules;

    public MamdaniInferenceEngine(Map<String, FuzzyVariable> variables, List<FuzzyRule> rules) {
        this.variables = variables;
        this.rules = rules;
    }

    public double evaluate() {
        System.out.println("\n=== Mamdani Inference Engine (centroid-average over consequents) ===");

        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < rules.size(); i++) {
            FuzzyRule r = rules.get(i);
            double firing = r.evaluate(variables);
            FuzzyConsequent cons = r.getConsequent();

            double centroid = 0.0;
            if (cons.isFuzzy()) {
                centroid = cons.getOutputVariable().getCentroidOf(cons.getLinguisticTerm());
            } else {
                centroid = cons.getCrispValue();
            }

            System.out.println("[Mamdani] Rule " + (i + 1) + ": " + r + " -> firing=" + firing + ", centroid=" + centroid);

            numerator += firing * centroid;
            denominator += firing;
        }

        double result = (denominator == 0.0) ? 0.0 : (numerator / denominator);
        System.out.println("[Mamdani] Final crisp output = " + result);
        return result;
    }
}

