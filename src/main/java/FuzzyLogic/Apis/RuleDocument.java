package FuzzyLogic.Apis;

import java.util.List;

public class RuleDocument {
    public String id;
    public String operator;
    public List<Condition> conditions;
    public Output output;
    public double weight = 1.0;
    public boolean enabled = true;
}
