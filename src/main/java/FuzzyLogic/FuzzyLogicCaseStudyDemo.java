package FuzzyLogic;

import FuzzyLogic.Apis.*;
import FuzzyLogic.Inference.*;
import FuzzyLogic.Rules.*;
import FuzzyLogic.Variable.*;

import java.util.*;

/**
 * Simple Fuzzy Logic Case Study Demo
 *
 * This demo shows:
 * 1. Loading rules from rules.json
 * 2. Setting up fuzzy variables
 * 3. Running both Mamdani and Sugeno inference algorithms
 */
public class FuzzyLogicCaseStudyDemo {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   FUZZY LOGIC IRRIGATION SYSTEM");
        System.out.println("========================================\n");

        SoilMoisture soil = new SoilMoisture();
        Temperature temp = new Temperature();
        RainForecast rain = new RainForecast();
        WaterDuration water = new WaterDuration();

        Map<String, FuzzyVariable> inputs = new HashMap<>();
        inputs.put(soil.getName(), soil);
        inputs.put(temp.getName(), temp);
        inputs.put(rain.getName(), rain);
        inputs.put(water.getName(), water);

        System.out.println("=== LOADING RULES FROM JSON FILE ===");
        SimpleRuleManager ruleManager = new SimpleRuleManager();
        List<RuleDocument> enabledRules = ruleManager.loadEnabledRules();

        System.out.println("Loaded " + enabledRules.size() + " enabled rules:");
        for (int i = 0; i < enabledRules.size(); i++) {
            RuleDocument doc = enabledRules.get(i);
            String opType = doc.usesMixedOperators() ? "Mixed" : "Single";
            System.out.println("  " + (i + 1) + ". " + doc.id + " (" + opType + " operators, weight=" + doc.weight + ")");

            StringBuilder ruleText = new StringBuilder("     IF ");
            for (int j = 0; j < doc.conditions.size(); j++) {
                var condition = doc.conditions.get(j);
                ruleText.append(condition.variable)
                        .append(" is ");
                if (condition.negated) {
                    ruleText.append("NOT ");
                }
                ruleText.append(condition._class);

                if (j < doc.conditions.size() - 1) {
                    if (doc.usesMixedOperators()) {
                        ruleText.append(" ").append(doc.getOperatorAt(j)).append(" ");
                    } else {
                        ruleText.append(" ").append(doc.operator != null ? doc.operator : "AND").append(" ");
                    }
                }
            }
            ruleText.append(" THEN ")
                    .append(doc.output.variable)
                    .append(" is ");
            if (doc.output.negated) {
                ruleText.append("NOT ");
            }
            ruleText.append(doc.output._class);

            System.out.println(ruleText.toString());
        }

        System.out.println("\n=== CONVERTING TO FUZZY RULES ===");
        List<FuzzyRule> fuzzyRules = new ArrayList<>();
        List<FuzzyRule> sugenoRules = new ArrayList<>();
        double outputMin = 0.0;
        double outputMax = 30.0;
        for (RuleDocument doc : enabledRules) {
            FuzzyRule fuzzyRule = RuleConverter.toFuzzyRule(doc, inputs);
            fuzzyRules.add(fuzzyRule);
            sugenoRules.add(RuleConverter.toSugenoRule(doc, inputs, outputMin, outputMax));
            System.out.println("✓ Converted: " + doc.id);
        }

        // Define three illustrative scenarios
        class Scenario {
            final String name;
            final double soilValue;
            final double tempValue;
            final double rainValue;
            Scenario(String name, double soilValue, double tempValue, double rainValue) {
                this.name = name;
                this.soilValue = soilValue;
                this.tempValue = tempValue;
                this.rainValue = rainValue;
            }
        }

        List<Scenario> scenarios = Arrays.asList(
                new Scenario("High Soil Moisture & Heavy Rain", 80.0, 24.0, 18.0),
                new Scenario("Low Soil Moisture & No Rain", 10.0, 34.0, 0.0),
                new Scenario("Moderate Soil Moisture & Light Rain", 35.0, 30.0, 3.0)
        );

        for (Scenario scenario : scenarios) {
            System.out.println("\n----------------------------------------");
            System.out.println("SCENARIO: " + scenario.name);
            System.out.println("----------------------------------------");

            soil.setValue(scenario.soilValue);
            temp.setValue(scenario.tempValue);
            rain.setValue(scenario.rainValue);

            System.out.println("Input Values:");
            System.out.println("  Soil Moisture: " + soil.getValue() + "%");
            System.out.println("  Temperature: " + temp.getValue() + "°C");
            System.out.println("  Rain Forecast: " + rain.getValue() + "mm\n");

            System.out.println("=== MEMBERSHIP DEGREES ===");
            System.out.println("Soil Moisture: DRY=" + String.format("%.3f", soil.getMembership("DRY")) +
                    ", NORMAL=" + String.format("%.3f", soil.getMembership("NORMAL")) +
                    ", WET=" + String.format("%.3f", soil.getMembership("WET")));
            System.out.println("Temperature : COLD=" + String.format("%.3f", temp.getMembership("COLD")) +
                    ", WARM=" + String.format("%.3f", temp.getMembership("WARM")) +
                    ", HOT=" + String.format("%.3f", temp.getMembership("HOT")));
            System.out.println("Rain Forecast: NONE=" + String.format("%.3f", rain.getMembership("NONE")) +
                    ", LIGHT=" + String.format("%.3f", rain.getMembership("LIGHT")) +
                    ", HEAVY=" + String.format("%.3f", rain.getMembership("HEAVY")));

            System.out.println("\n=== MAMDANI INFERENCE ===");
            MamdaniInferenceEngine mamdaniEngine = new MamdaniInferenceEngine(inputs, fuzzyRules, outputMin, outputMax);
            double mamdaniResult = mamdaniEngine.evaluate();
            System.out.println("Mamdani Result: " + String.format("%.1f", mamdaniResult) + " minutes");

            System.out.println("\n=== SUGENO INFERENCE ===");
            SugenoInferenceEngine sugenoEngine = new SugenoInferenceEngine(inputs, sugenoRules);
            double sugenoResult = sugenoEngine.evaluate();
            System.out.println("Sugeno Result:  " + String.format("%.1f", sugenoResult) + " minutes");

            System.out.println("\n=== COMPARISON ===");
            System.out.println("Mamdani (fuzzy output): " + String.format("%.1f", mamdaniResult) + " minutes");
            System.out.println("Sugeno (crisp output):  " + String.format("%.1f", sugenoResult) + " minutes");
            System.out.println("Difference: " + String.format("%.1f", Math.abs(mamdaniResult - sugenoResult)) + " minutes");

            double finalResult = mamdaniResult;
            System.out.println("\nRecommendation: " + String.format("%.1f", finalResult) + " minutes");
            if (finalResult < 8) {
                System.out.println("Interpretation: SHORT watering - Soil has adequate moisture");
            } else if (finalResult < 18) {
                System.out.println("Interpretation: MEDIUM watering - Balanced conditions");
            } else {
                System.out.println("Interpretation: LONG watering - Soil needs more water");
            }
        }

        System.out.println("\n========================================");
        System.out.println("        MULTI-SCENARIO DEMO COMPLETE");
        System.out.println("========================================");
    }
}
