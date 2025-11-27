package FuzzyLogic.Rules;

import FuzzyLogic.Apis.Condition;
import FuzzyLogic.Apis.RuleDocument;
import FuzzyLogic.Rules.*;
import FuzzyLogic.Variable.*;

import java.util.Map;

public class RuleConverter {

    public static FuzzyRule toFuzzyRule(RuleDocument doc, Map<String, FuzzyVariable> vars) {
        FuzzyConsequent consequent;

        if (doc.output.negated) {
            consequent = FuzzyConsequent.createNegated(
                    (FuzzyVariable<?>) vars.get(doc.output.variable),
                    doc.output._class
            );
        } else {
            consequent = new FuzzyConsequent(
                    vars.get(doc.output.variable),
                    doc.output._class
            );
        }

        FuzzyRule rule;

        // Create rule based on operator mode
        if (doc.usesMixedOperators()) {
            rule = new FuzzyRule(doc.operators, consequent);
        } else {
            String op = doc.operator != null ? doc.operator : "AND";
            rule = new FuzzyRule(op, consequent);
        }

        // Add conditions
        for (Condition c : doc.conditions) {
            FuzzyVariable var = vars.get(c.variable);
            if (var == null)
                throw new RuntimeException("Unknown variable: " + c.variable);

            rule.addAntecedent(new FuzzyCondition(var, c._class, c.negated));
        }

        // Apply weight from document to rule
        rule.setWeight(doc.weight);

        return rule;
    }

    public static FuzzyRule toSugenoRule(RuleDocument doc, Map<String, FuzzyVariable> vars,
                                         double outputMin, double outputMax) {
        FuzzyVariable outputVar = vars.get(doc.output.variable);
        if (outputVar == null) {
            throw new RuntimeException("Unknown output variable: " + doc.output.variable);
        }

        double centroid = outputVar.getCentroidOf(doc.output._class);
        double crispValue = centroid;
        if (doc.output.negated) {
            double reflected = outputMax - (centroid - outputMin);
            crispValue = Math.max(outputMin, Math.min(outputMax, reflected));
        }

        FuzzyConsequent consequent = new FuzzyConsequent(outputVar, crispValue);

        FuzzyRule rule;
        if (doc.usesMixedOperators()) {
            rule = new FuzzyRule(doc.operators, consequent);
        } else {
            String op = doc.operator != null ? doc.operator : "AND";
            rule = new FuzzyRule(op, consequent);
        }

        for (Condition c : doc.conditions) {
            FuzzyVariable var = vars.get(c.variable);
            if (var == null)
                throw new RuntimeException("Unknown variable: " + c.variable);

            rule.addAntecedent(new FuzzyCondition(var, c._class, c.negated));
        }

        rule.setWeight(doc.weight);
        return rule;
    }
}
