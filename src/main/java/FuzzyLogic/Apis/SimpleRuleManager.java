package FuzzyLogic.Apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Simple class to read and write rules from rules.json file
 */
public class SimpleRuleManager {

    private final String filePath;
    private final Gson gson;

    public SimpleRuleManager() {
        this("rules.json");
    }

    public SimpleRuleManager(String fileName) {
        this.filePath = fileName;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Read all rules from JSON file and parse them into RuleDocument objects
     */
    public List<RuleDocument> loadRules() {
        List<SimpleRule> simpleRules = readSimpleRulesFromFile();
        List<RuleDocument> ruleDocuments = new ArrayList<>();
        RuleParser parser = new RuleParser();

        for (SimpleRule simpleRule : simpleRules) {
            try {
                // Parse the rule text
                RuleDocument doc = parser.parse(simpleRule.ruleText);

                // Set metadata from JSON
                doc.id = simpleRule.id;
                doc.weight = simpleRule.weight;
                doc.enabled = simpleRule.enabled;

                ruleDocuments.add(doc);

            } catch (Exception e) {
                System.err.println("Error parsing rule '" + simpleRule.id + "': " + e.getMessage());
                System.err.println("Rule text: " + simpleRule.ruleText);
            }
        }

        return ruleDocuments;
    }

    /**
     * Get only enabled rules
     */
    public List<RuleDocument> loadEnabledRules() {
        List<RuleDocument> allRules = loadRules();
        List<RuleDocument> enabledRules = new ArrayList<>();

        for (RuleDocument rule : allRules) {
            if (rule.enabled) {
                enabledRules.add(rule);
            }
        }

        return enabledRules;
    }

    /**
     * Add a new rule to the JSON file
     */
    public void addRule(String id, String ruleText, double weight, boolean enabled) {
        List<SimpleRule> rules = readSimpleRulesFromFile();

        SimpleRule newRule = new SimpleRule();
        newRule.id = id;
        newRule.ruleText = ruleText;
        newRule.weight = weight;
        newRule.enabled = enabled;

        rules.add(newRule);
        writeSimpleRulesToFile(rules);
    }

    /**
     * Update an existing rule
     */
    public boolean updateRule(String id, String newRuleText, Double newWeight, Boolean newEnabled) {
        List<SimpleRule> rules = readSimpleRulesFromFile();

        for (SimpleRule rule : rules) {
            if (rule.id.equals(id)) {
                if (newRuleText != null) rule.ruleText = newRuleText;
                if (newWeight != null) rule.weight = newWeight;
                if (newEnabled != null) rule.enabled = newEnabled;

                writeSimpleRulesToFile(rules);
                return true;
            }
        }
        return false; // Rule not found
    }

    /**
     * Enable a rule (soft delete alternative)
     */
    public boolean enableRule(String id) {
        return updateRule(id, null, null, true);
    }

    /**
     * Disable a rule (soft delete)
     */
    public boolean disableRule(String id) {
        return updateRule(id, null, null, false);
    }

    /**
     * Delete a rule completely
     */
    public boolean deleteRule(String id) {
        List<SimpleRule> rules = readSimpleRulesFromFile();
        boolean removed = rules.removeIf(rule -> rule.id.equals(id));

        if (removed) {
            writeSimpleRulesToFile(rules);
        }
        return removed;
    }

    /**
     * Get statistics about the rules
     */
    public RuleStats getStats() {
        List<SimpleRule> rules = readSimpleRulesFromFile();
        RuleStats stats = new RuleStats();

        stats.totalRules = rules.size();
        stats.enabledRules = (int) rules.stream().filter(r -> r.enabled).count();
        stats.disabledRules = stats.totalRules - stats.enabledRules;

        return stats;
    }

    private List<SimpleRule> readSimpleRulesFromFile() {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                return new ArrayList<>();
            }

            String content = Files.readString(Paths.get(filePath));
            if (content.trim().isEmpty()) {
                return new ArrayList<>();
            }

            Type listType = new TypeToken<List<SimpleRule>>(){}.getType();
            List<SimpleRule> rules = gson.fromJson(content, listType);
            return rules != null ? rules : new ArrayList<>();

        } catch (IOException e) {
            System.err.println("Error reading rules file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void writeSimpleRulesToFile(List<SimpleRule> rules) {
        try {
            String json = gson.toJson(rules);
            Files.writeString(Paths.get(filePath), json);
        } catch (IOException e) {
            System.err.println("Error writing rules file: " + e.getMessage());
        }
    }

    // Simple rule structure for JSON storage
    public static class SimpleRule {
        public String id;
        public String ruleText;
        public double weight = 1.0;
        public boolean enabled = true;
    }

    // Statistics class
    public static class RuleStats {
        public int totalRules;
        public int enabledRules;
        public int disabledRules;
    }
}
