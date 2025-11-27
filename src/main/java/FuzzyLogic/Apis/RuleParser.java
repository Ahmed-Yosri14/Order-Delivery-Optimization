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
            throw new IllegalArgumentException("Rule must be in format: IF ... THEN ... [WEIGHT <value>]");

        // Extract parts
        String ifPart = text.substring(2, thenIndex).trim();    // remove 'IF'
        String thenPart = text.substring(thenIndex + 6).trim(); // remove 'THEN'

        // Check for optional WEIGHT clause
        double weight = 1.0; // default
        int weightIndex = thenPart.toUpperCase().indexOf(" WEIGHT ");
        if (weightIndex != -1) {
            String weightStr = thenPart.substring(weightIndex + 8).trim();
            thenPart = thenPart.substring(0, weightIndex).trim();

            try {
                weight = Double.parseDouble(weightStr);
                if (weight < 0.0 || weight > 1.0) {
                    throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid weight value: " + weightStr);
            }
        }

        // Detect operator(s) - enhanced for mixed operators
        List<String> operatorSequence = new ArrayList<>();
        boolean hasMixedOperators = false;
        String singleOperator = null;

        // Parse operators by finding their positions in the condition string
        String upperIfPart = ifPart.toUpperCase();
        List<String> conditionTexts = new ArrayList<>();

        // Find all AND and OR positions
        List<Integer> andPositions = new ArrayList<>();
        List<Integer> orPositions = new ArrayList<>();

        int pos = 0;
        while ((pos = upperIfPart.indexOf(" AND ", pos)) != -1) {
            andPositions.add(pos);
            pos += 5; // length of " AND "
        }

        pos = 0;
        while ((pos = upperIfPart.indexOf(" OR ", pos)) != -1) {
            orPositions.add(pos);
            pos += 4; // length of " OR "
        }

        // Combine and sort all operator positions
        List<OperatorInfo> allOperators = new ArrayList<>();
        for (int andPos : andPositions) {
            allOperators.add(new OperatorInfo(andPos, "AND"));
        }
        for (int orPos : orPositions) {
            allOperators.add(new OperatorInfo(orPos, "OR"));
        }

        allOperators.sort((a, b) -> Integer.compare(a.position, b.position));

        if (allOperators.isEmpty()) {
            // Single condition, no operators
            singleOperator = "AND"; // default
        } else {
            // Extract operator sequence
            for (OperatorInfo opInfo : allOperators) {
                operatorSequence.add(opInfo.operator);
            }

            // Check if mixed
            Set<String> uniqueOps = new HashSet<>(operatorSequence);
            hasMixedOperators = uniqueOps.size() > 1;

            if (!hasMixedOperators) {
                singleOperator = operatorSequence.get(0);
            }
        }

        // Split conditions based on operator positions
        if (allOperators.isEmpty()) {
            conditionTexts.add(ifPart.trim());
        } else {
            int lastEnd = 0;
            for (OperatorInfo opInfo : allOperators) {
                String conditionText = ifPart.substring(lastEnd, opInfo.position).trim();
                conditionTexts.add(conditionText);
                lastEnd = opInfo.position + (opInfo.operator.equals("AND") ? 5 : 4);
            }
            // Add the last condition
            conditionTexts.add(ifPart.substring(lastEnd).trim());
        }

        // Parse conditions
        List<Condition> conditionList = new ArrayList<>();
        for (String c : conditionTexts) {
            String trimmed = c.trim();
            boolean negated = false;

            if (trimmed.toUpperCase().startsWith("NOT ")) {
                negated = true;
                trimmed = trimmed.substring(4).trim();
            }

            String[] pair = trimmed.split("(?i)\\s+is\\s+");

            if (pair.length != 2)
                throw new IllegalArgumentException("Invalid condition format: " + c);

            String var = pair[0].trim();
            String clazz = pair[1].trim();

            if (clazz.toUpperCase().startsWith("NOT ")) {
                negated = true;
                clazz = clazz.substring(4).trim();
            }

            validateCondition(var, clazz);

            Condition cond = new Condition();
            cond.variable = var;
            cond._class = clazz;
            cond.negated = negated;
            conditionList.add(cond);
        }

        // Parse THEN part
        String[] thenSplit = thenPart.split("(?i)\\s+is\\s+");
        if (thenSplit.length != 2)
            throw new IllegalArgumentException("THEN clause must be in format: Water Duration is <Class>");

        String outVar = thenSplit[0].trim();
        String outClass = thenSplit[1].trim();
        boolean outputNegated = false;

        if (outClass.toUpperCase().startsWith("NOT ")) {
            outputNegated = true;
            outClass = outClass.substring(4).trim();
        }

        if (!outVar.equalsIgnoreCase("Water Duration"))
            throw new IllegalArgumentException("Output variable must be 'Water Duration'");

        validateClass("Water Duration", outClass);

        Output output = new Output();
        output.variable = "Water Duration";
        output._class = outClass;
        output.negated = outputNegated;

        // Build final object
        RuleDocument doc = new RuleDocument();

        if (hasMixedOperators) {
            doc.operators = operatorSequence;
            doc.operator = null; // Clear single operator for mixed mode
        } else {
            doc.operator = singleOperator;
            doc.operators = null; // Clear operators list for single operator mode
        }

        doc.conditions = conditionList;
        doc.output = output;
        doc.weight = weight;

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

    // Helper class for tracking operator positions
    private static class OperatorInfo {
        int position;
        String operator;

        OperatorInfo(int position, String operator) {
            this.position = position;
            this.operator = operator;
        }
    }
}
