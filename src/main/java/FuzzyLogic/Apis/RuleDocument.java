package FuzzyLogic.Apis;

import java.util.List;

public class RuleDocument {
    public String id;
    public String operator;
    public List<Condition> conditions;
    public Output output;
}
