package FuzzyLogic.Rules;

import FuzzyLogic.Apis.SimpleRuleManager;
import FuzzyLogic.Apis.RuleParser;
import FuzzyLogic.Apis.RuleDocument;

import java.util.List;
import java.util.Scanner;

public class RuleManagerConsole {

    private static SimpleRuleManager ruleManager;
    private static RuleParser ruleParser;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    FUZZY RULE MANAGER CONSOLE");
        System.out.println("========================================\n");

        ruleManager = new SimpleRuleManager();
        ruleParser = new RuleParser();
        scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    listRules();
                    break;
                case 2:
                    addNewRule();
                    break;
                case 3:
                    editRule();
                    break;
                case 4:
                    toggleRuleStatus();
                    break;
                case 5:
                    deleteRule();
                    break;
                case 6:
                    showStatistics();
                    break;
                case 7:
                    running = false;
                    System.out.println(" Goodbye! Your rules are saved in rules.json");
                    break;
                default:
                    System.out.println(" Invalid choice. Please try again.\n");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("RULE MANAGEMENT MENU");
        System.out.println("1. List all rules");
        System.out.println("2. Add new rule");
        System.out.println("3. Edit existing rule");
        System.out.println("4. Enable/Disable rule");
        System.out.println("5. Delete rule");
        System.out.println("6. Show statistics");
        System.out.println("7. Exit");
        System.out.println();
    }

    private static void listRules() {
        System.out.println("\n=== ALL RULES ===");
        List<RuleDocument> rules = ruleManager.loadRules();

        if (rules.isEmpty()) {
            System.out.println(" No rules found in rules.json");
        } else {
            for (int i = 0; i < rules.size(); i++) {
                RuleDocument rule = rules.get(i);
                String status = rule.enabled ? " ENABLED " : " DISABLED";
                String operators = rule.usesMixedOperators() ?
                    "Mixed: " + rule.operators :
                    "Single: " + (rule.operator != null ? rule.operator : "AND");

                System.out.println((i + 1) + ". " + status + " | " + rule.id + " (weight=" + rule.weight + ")");
                System.out.println("   Operators: " + operators);
                System.out.println("   Text: " + getRuleTextFromDocument(rule));
                System.out.println();
            }
        }
        waitForEnter();
    }

    private static void addNewRule() {
        System.out.println("\n=== ADD NEW RULE ===");
        System.out.println(" Rule Format Examples:");
        System.out.println("   IF Soil Moisture is DRY AND Temperature is HOT THEN Water Duration is LONG");
        System.out.println("   IF Soil Moisture is WET OR Rain Forecast is HEAVY THEN Water Duration is SHORT WEIGHT 0.9");
        System.out.println("   IF Temperature is WARM AND Soil Moisture is NORMAL OR Rain Forecast is LIGHT THEN Water Duration is MEDIUM");
        System.out.println();
        System.out.println(" Valid Variables: Soil Moisture, Temperature, Rain Forecast, Water Duration");
        System.out.println(" Valid Classes: DRY/NORMAL/WET, COLD/WARM/HOT, NONE/LIGHT/HEAVY, SHORT/MEDIUM/LONG");
        System.out.println();

        String ruleId = getStringInput("Enter rule ID (e.g., rule_006): ");

        // Check if ID already exists
        if (ruleExists(ruleId)) {
            System.out.println("  Rule ID '" + ruleId + "' already exists. Use option 3 to edit it.");
            waitForEnter();
            return;
        }

        String ruleText = getStringInput("Enter rule text: ");

        try {
            // Validate syntax
            RuleDocument testRule = ruleParser.parse(ruleText);

            double weight = testRule.weight; // Get weight from parsed rule, or ask user
            if (weight == 1.0) {
                String weightInput = getStringInput("Enter weight (0.0-1.0) [default: 1.0]: ");
                if (!weightInput.trim().isEmpty()) {
                    weight = Double.parseDouble(weightInput);
                    if (weight < 0.0 || weight > 1.0) {
                        System.out.println("  Weight must be between 0.0 and 1.0");
                        waitForEnter();
                        return;
                    }
                }
            }

            boolean enabled = getBooleanInput("Enable rule immediately? (y/n) [default: y]: ", true);

            // Add rule
            ruleManager.addRule(ruleId, ruleText, weight, enabled);

            System.out.println("   Rule '" + ruleId + "' added successfully!");
            System.out.println("   Text: " + ruleText);
            System.out.println("   Weight: " + weight);
            System.out.println("   Enabled: " + enabled);

            if (testRule.usesMixedOperators()) {
                System.out.println("   Mixed operators detected: " + testRule.operators);
            }

        } catch (Exception e) {
            System.out.println("Error adding rule: " + e.getMessage());
        }

        waitForEnter();
    }

    private static void editRule() {
        System.out.println("\n=== EDIT RULE ===");
        List<RuleDocument> rules = ruleManager.loadRules();

        if (rules.isEmpty()) {
            System.out.println("No rules to edit.");
            waitForEnter();
            return;
        }

        // Show numbered list
        for (int i = 0; i < rules.size(); i++) {
            RuleDocument rule = rules.get(i);
            String status = rule.enabled ? "Enabled" : "Disabled";
            System.out.println((i + 1) + ". " + status + " " + rule.id + " - " + getRuleTextFromDocument(rule));
        }

        int selection = getIntInput("\nSelect rule number to edit (1-" + rules.size() + "): ");

        if (selection < 1 || selection > rules.size()) {
            System.out.println("Invalid selection.");
            waitForEnter();
            return;
        }

        RuleDocument selectedRule = rules.get(selection - 1);
        System.out.println("\nEditing: " + selectedRule.id);
        System.out.println("Current: " + getRuleTextFromDocument(selectedRule));
        System.out.println();

        String newRuleText = getStringInput("Enter new rule text [Enter to keep current]: ");
        String newWeightStr = getStringInput("Enter new weight [Enter to keep " + selectedRule.weight + "]: ");

        try {
            Double newWeight = null;
            if (!newWeightStr.trim().isEmpty()) {
                newWeight = Double.parseDouble(newWeightStr);
                if (newWeight < 0.0 || newWeight > 1.0) {
                    System.out.println("Weight must be between 0.0 and 1.0");
                    waitForEnter();
                    return;
                }
            }

            // Validate new rule text if provided
            if (!newRuleText.trim().isEmpty()) {
                ruleParser.parse(newRuleText); // Just for validation
            }

            boolean updated = ruleManager.updateRule(
                selectedRule.id,
                newRuleText.trim().isEmpty() ? null : newRuleText,
                newWeight,
                null // Don't change enabled status here
            );

            if (updated) {
                System.out.println("Rule '" + selectedRule.id + "' updated successfully!");
            } else {
                System.out.println("Failed to update rule.");
            }

        } catch (Exception e) {
            System.out.println("Error updating rule: " + e.getMessage());
        }

        waitForEnter();
    }

    private static void toggleRuleStatus() {
        System.out.println("\n=== ENABLE/DISABLE RULE ===");
        List<RuleDocument> rules = ruleManager.loadRules();

        if (rules.isEmpty()) {
            System.out.println("No rules found.");
            waitForEnter();
            return;
        }

        // Show numbered list with current status
        for (int i = 0; i < rules.size(); i++) {
            RuleDocument rule = rules.get(i);
            String status = rule.enabled ? "ENABLED " : "DISABLED";
            System.out.println((i + 1) + ". " + status + " - " + rule.id);
        }

        int selection = getIntInput("\nSelect rule number to toggle (1-" + rules.size() + "): ");

        if (selection < 1 || selection > rules.size()) {
            System.out.println("Invalid selection.");
            waitForEnter();
            return;
        }

        RuleDocument selectedRule = rules.get(selection - 1);
        boolean newStatus = !selectedRule.enabled;

        boolean success;
        if (newStatus) {
            success = ruleManager.enableRule(selectedRule.id);
            System.out.println(success ? "Rule '" + selectedRule.id + "' ENABLED" : "Failed to enable rule");
        } else {
            success = ruleManager.disableRule(selectedRule.id);
            System.out.println(success ? "Rule '" + selectedRule.id + "' DISABLED" : "Failed to disable rule");
        }

        waitForEnter();
    }

    private static void deleteRule() {
        System.out.println("\n=== DELETE RULE ===");
        List<RuleDocument> rules = ruleManager.loadRules();

        if (rules.isEmpty()) {
            System.out.println("No rules to delete.");
            waitForEnter();
            return;
        }

        // Show numbered list
        for (int i = 0; i < rules.size(); i++) {
            RuleDocument rule = rules.get(i);
            System.out.println((i + 1) + ". " + rule.id + " - " + getRuleTextFromDocument(rule));
        }

        int selection = getIntInput("\nSelect rule number to DELETE (1-" + rules.size() + "): ");

        if (selection < 1 || selection > rules.size()) {
            System.out.println("Invalid selection.");
            waitForEnter();
            return;
        }

        RuleDocument selectedRule = rules.get(selection - 1);

        System.out.println("WARNING: This will permanently delete rule '" + selectedRule.id + "'");
        System.out.println("Rule: " + getRuleTextFromDocument(selectedRule));

        boolean confirm = getBooleanInput("Are you sure? (y/n): ", false);

        if (confirm) {
            boolean deleted = ruleManager.deleteRule(selectedRule.id);
            System.out.println(deleted ? "Rule '" + selectedRule.id + "' deleted successfully!" : "Failed to delete rule");
        } else {
            System.out.println("Delete cancelled.");
        }

        waitForEnter();
    }

    private static void showStatistics() {
        System.out.println("\n=== STATISTICS ===");
        SimpleRuleManager.RuleStats stats = ruleManager.getStats();

        System.out.println("Total rules: " + stats.totalRules);
        System.out.println("Enabled rules: " + stats.enabledRules);
        System.out.println("Disabled rules: " + stats.disabledRules);

        if (stats.totalRules > 0) {
            double enabledPercentage = (stats.enabledRules * 100.0) / stats.totalRules;
            System.out.println(" Enabled percentage: " + String.format("%.1f", enabledPercentage) + "%");
        }

        // Show weight distribution
        List<RuleDocument> rules = ruleManager.loadRules();
        if (!rules.isEmpty()) {
            System.out.println("\nWeight Distribution:");
            for (RuleDocument rule : rules) {
                String status = rule.enabled ? "Enabled" : "Disabled";
                System.out.println("   " + status + " " + rule.id + ": " + rule.weight);
            }
        }

        waitForEnter();
    }

    // Helper methods
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static boolean getBooleanInput(String prompt, boolean defaultValue) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.isEmpty()) {
            return defaultValue;
        }

        return input.equals("y") || input.equals("yes") || input.equals("true");
    }

    private static void waitForEnter() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }

    private static boolean ruleExists(String ruleId) {
        List<RuleDocument> rules = ruleManager.loadRules();
        return rules.stream().anyMatch(rule -> rule.id.equals(ruleId));
    }

    private static String getRuleTextFromDocument(RuleDocument doc) {
        StringBuilder sb = new StringBuilder();
        sb.append("IF ");

        for (int i = 0; i < doc.conditions.size(); i++) {
            var condition = doc.conditions.get(i);
            sb.append(condition.variable)
              .append(" is ");
            if (condition.negated) {
                sb.append("NOT ");
            }
            sb.append(condition._class);

            if (i < doc.conditions.size() - 1) {
                if (doc.usesMixedOperators()) {
                    sb.append(" ").append(doc.getOperatorAt(i)).append(" ");
                } else {
                    sb.append(" ").append(doc.operator != null ? doc.operator : "AND").append(" ");
                }
            }
        }

        sb.append(" THEN ")
          .append(doc.output.variable)
          .append(" is ");
        if (doc.output.negated) {
            sb.append("NOT ");
        }
        sb.append(doc.output._class);

        if (doc.weight != 1.0) {
            sb.append(" WEIGHT ").append(doc.weight);
        }

        return sb.toString();
    }
}
