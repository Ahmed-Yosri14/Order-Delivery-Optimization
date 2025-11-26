package FuzzyLogic.Rules;

import FuzzyLogic.Apis.Condition;
import FuzzyLogic.Apis.RuleDocument;
import FuzzyLogic.Rules.*;
import FuzzyLogic.Variable.*;

import java.util.Map;

public class RuleConverter {

    public static FuzzyRule toFuzzyRule(RuleDocument doc, Map<String, FuzzyVariable> vars) {
        FuzzyConsequent consequent = new FuzzyConsequent(
                vars.get(doc.output.variable),
                doc.output._class
        );

        FuzzyRule rule = new FuzzyRule(doc.operator, consequent);

        for (Condition c : doc.conditions) {
            FuzzyVariable var = vars.get(c.variable);
            if (var == null)
                throw new RuntimeException("Unknown variable: " + c.variable);

            rule.addAntecedent(new FuzzyCondition(var, c._class));
        }

        // Apply weight from document to rule
        rule.setWeight(doc.weight);

        return rule;
    }
}
