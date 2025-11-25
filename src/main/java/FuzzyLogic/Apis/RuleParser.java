package FuzzyLogic.Apis;

import java.util.*;

public class RuleParser {

    private static final Map<String, Set<String>> VALID_CLASSES = new HashMap<>();

    public RuleParser() {
        VALID_CLASSES.put("SOIL MOISTURE", Set.of("DRY", "NORMAL", "WET"));
        VALID_CLASSES.put("TEMPERATURE", Set.of("COLD", "WARM", "HOT"));
        VALID_CLASSES.put("RAIN FORECAST", Set.of("NONE", "LIGHT", "HEAVY"));
        VALID_CLASSES.put("WATER DURATION", Set.of("SHORT", "MEDIUM", "LONG"));
    }

    public RuleDocument parse(String ruleText) {

        if (ruleText == null || ruleText.isBlank())
            throw new IllegalArgumentException("Rule cannot be empty");

        String text = ruleText.trim();

        // Case-insensitive split
        int thenIndex = text.toUpperCase().indexOf(" THEN ");
        if (thenIndex == -1 || !text.toUpperCase().startsWith("IF "))
            throw new IllegalArgumentException("Rule must be in format: IF ... THEN ...");

        // Extract parts
        String ifPart = text.substring(2, thenIndex).trim();    // remove 'IF'
        String thenPart = text.substring(thenIndex + 6).trim(); // remove 'THEN'

        // Detect operator
        boolean hasAnd = ifPart.toUpperCase().contains(" AND ");
        boolean hasOr = ifPart.toUpperCase().contains(" OR ");
        if (hasAnd && hasOr)
            throw new IllegalArgumentException("Rule cannot mix AND and OR");

        String operator = hasAnd ? "AND" : "OR";
        String[] conditions = ifPart.split("(?i)\\s" + operator + "\\s");

        // Parse conditions
        List<Condition> conditionList = new ArrayList<>();
        for (String c : conditions) {
            // (?i) for case-insensitive \\s+ for spaces around 'is'
            String[] pair = c.split("(?i)\\s+is\\s+");

            if (pair.length != 2)
                throw new IllegalArgumentException("Invalid condition format: " + c);

            String var = pair[0].trim();
            String clazz = pair[1].trim();

            validateCondition(var, clazz);

            Condition cond = new Condition();
            cond.variable = var;
            cond._class = clazz;
            conditionList.add(cond);
        }

        // Parse THEN part
        String[] thenSplit = thenPart.split("(?i)\\s+is\\s+");
        if (thenSplit.length != 2)
            throw new IllegalArgumentException("THEN clause must be in format: Water Duration is <Class>");

        String outVar = thenSplit[0].trim();
        String outClass = thenSplit[1].trim();

        if (!outVar.equalsIgnoreCase("Water Duration"))
            throw new IllegalArgumentException("Output variable must be 'Water Duration'");

        validateClass("Water Duration", outClass);

        Output output = new Output();
        output.variable = "Water Duration";
        output._class = outClass;

        // Build final object
        RuleDocument doc = new RuleDocument();
        doc.operator = operator;
        doc.conditions = conditionList;
        doc.output = output;

        return doc;
    }

    private void validateCondition(String variable, String clazz) {
        String v = variable.toUpperCase();
        if (!VALID_CLASSES.containsKey(v))
            throw new IllegalArgumentException("Unknown variable: " + variable);

        validateClass(variable, clazz);
    }

    private void validateClass(String variable, String clazz) {
        String v = variable.toUpperCase();
        Set<String> allowed = VALID_CLASSES.get(v);

        if (allowed == null)
            throw new IllegalArgumentException("Unknown variable: " + variable);

        if (!allowed.contains(clazz.toUpperCase()))
            throw new IllegalArgumentException(
                    "Invalid class '" + clazz + "' for variable '" + variable +
                            "'. Allowed: " + allowed
            );
    }
}
