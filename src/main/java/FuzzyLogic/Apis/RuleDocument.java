package FuzzyLogic.Apis;

import java.util.List;

public class RuleDocument {
    public String id;
    public String operator;
    public List<String> operators;
    public List<Condition> conditions;
    public Output output;
    public double weight = 1.0;
    public boolean enabled = true;

    public boolean usesMixedOperators() {
        return operators != null && !operators.isEmpty();
    }

    public String getOperatorAt(int index) {
        if (usesMixedOperators()) {
            return index < operators.size() ? operators.get(index) : "AND";
        }
        return operator != null ? operator : "AND";
    }
}
