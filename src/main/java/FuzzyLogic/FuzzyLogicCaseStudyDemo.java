package FuzzyLogic;

import FuzzyLogic.Apis.*;
import FuzzyLogic.Inference.*;
import FuzzyLogic.Membership.*;
import FuzzyLogic.Operators.*;
import FuzzyLogic.Rules.*;
import FuzzyLogic.Variable.*;

import java.util.*;

/**
 * COMPREHENSIVE CASE STUDY: Smart Irrigation System using Fuzzy Logic
 *
 * This demo covers ALL features of the Fuzzy Logic module:
 * 1. Membership Functions (Triangular, Trapezoidal, Gaussian)
 * 2. Fuzzy Variables (Input/Output)
 * 3. Fuzzy Rules (with weights and enable/disable)
 * 4. Mamdani Inference Engine
 * 5. Sugeno Inference Engine
 * 6. Rule Management (CRUD operations)
 * 7. Natural Language Rule Parsing
 * 8. MongoDB Integration
 * 9. Different Operators (AND, OR, Implication, Aggregation)
 * 10. Defuzzification Methods
 *
 * Case Study Problem:
 * An agricultural farm needs an intelligent irrigation system that decides
 * how long to water crops based on:
 * - Soil Moisture Level (0-100%)
 * - Temperature (0-50°C)
 * - Rain Forecast (0-10mm expected)
 *
 * Output: Water Duration (0-30 minutes)
 */
public class FuzzyLogicCaseStudyDemo {

    public static void main(String[] args) {

        System.out.println("================================================================");
        System.out.println("    FUZZY LOGIC CASE STUDY: SMART IRRIGATION SYSTEM");
        System.out.println("================================================================\n");

        // ===================================================================
        // PHASE 1: PROBLEM DEFINITION & SETUP
        // ===================================================================

        System.out.println("=== PHASE 1: PROBLEM DEFINITION ===\n");

        System.out.println("Problem: Determine optimal irrigation duration based on:");
        System.out.println("  Input 1: Soil Moisture (0-100%)");
        System.out.println("  Input 2: Temperature (0-50°C)");
        System.out.println("  Input 3: Rain Forecast (0-10mm)");
        System.out.println("  Output: Water Duration (0-30 minutes)\n");

        System.out.println("Challenge: Traditional systems use fixed rules,");
        System.out.println("but fuzzy logic handles uncertainty and gradual transitions.\n");

        // ===================================================================
        // PHASE 2: FUZZY VARIABLES & MEMBERSHIP FUNCTIONS
        // ===================================================================

        System.out.println("\n=== PHASE 2: FUZZY VARIABLES & MEMBERSHIP FUNCTIONS ===\n");

        // Create fuzzy variables
        SoilMoisture soil = new SoilMoisture();
        Temperature temp = new Temperature();
        RainForecast rain = new RainForecast();
        WaterDuration water = new WaterDuration();

        System.out.println("Input Variables:");
        System.out.println("1. Soil Moisture:");
        System.out.println("   - DRY: Triangular MF (0, 30, 60)");
        System.out.println("   - NORMAL: Triangular MF (30, 50, 70)");
        System.out.println("   - WET: Triangular MF (45, 75, 100)");

        System.out.println("\n2. Temperature:");
        System.out.println("   - COLD: Gaussian MF (center=10, sigma=5)");
        System.out.println("   - WARM: Gaussian MF (center=25, sigma=5)");
        System.out.println("   - HOT: Gaussian MF (center=40, sigma=5)");

        System.out.println("\n3. Rain Forecast:");
        System.out.println("   - NONE: Trapezoidal MF (0, 0, 0.5, 1.5)");
        System.out.println("   - LIGHT: Trapezoidal MF (0.8, 1.5, 3.5, 4.5)");
        System.out.println("   - HEAVY: Trapezoidal MF (3.5, 5, 10, 10)");

        System.out.println("\nOutput Variable:");
        System.out.println("4. Water Duration:");
        System.out.println("   - SHORT: Triangular MF (0, 4, 8)");
        System.out.println("   - MEDIUM: Triangular MF (6, 15, 24)");
        System.out.println("   - LONG: Triangular MF (20, 27, 30)");

        // ===================================================================
        // PHASE 3: FUZZY RULES WITH WEIGHTS
        // ===================================================================

        System.out.println("\n\n=== PHASE 3: FUZZY RULES WITH WEIGHTS ===\n");

        Map<String, FuzzyVariable> inputs = new HashMap<>();
        inputs.put(soil.getName(), soil);
        inputs.put(temp.getName(), temp);
        inputs.put(rain.getName(), rain);
        inputs.put(water.getName(), water);

        List<FuzzyRule> rules = new ArrayList<>();

        // Rule 1: High priority - Dry soil and hot temperature
        FuzzyRule rule1 = new FuzzyRule("AND", new FuzzyConsequent(water, "LONG"));
        rule1.addAntecedent(new FuzzyCondition(soil, "DRY"));
        rule1.addAntecedent(new FuzzyCondition(temp, "HOT"));
        rule1.setWeight(1.0); // Expert knowledge
        rules.add(rule1);
        System.out.println("Rule 1 (Weight=1.0): IF Soil is DRY AND Temp is HOT THEN Water is LONG");

        // Rule 2: Medium priority - Normal soil or warm temperature
        FuzzyRule rule2 = new FuzzyRule("OR", new FuzzyConsequent(water, "MEDIUM"));
        rule2.addAntecedent(new FuzzyCondition(soil, "NORMAL"));
        rule2.addAntecedent(new FuzzyCondition(temp, "WARM"));
        rule2.setWeight(0.8);
        rules.add(rule2);
        System.out.println("Rule 2 (Weight=0.8): IF Soil is NORMAL OR Temp is WARM THEN Water is MEDIUM");

        // Rule 3: High priority - Wet soil means short watering
        FuzzyRule rule3 = new FuzzyRule("AND", new FuzzyConsequent(water, "SHORT"));
        rule3.addAntecedent(new FuzzyCondition(soil, "WET"));
        rule3.setWeight(1.0);
        rules.add(rule3);
        System.out.println("Rule 3 (Weight=1.0): IF Soil is WET THEN Water is SHORT");

        // Rule 4: Rain forecast affects watering
        FuzzyRule rule4 = new FuzzyRule("AND", new FuzzyConsequent(water, "SHORT"));
        rule4.addAntecedent(new FuzzyCondition(rain, "HEAVY"));
        rule4.setWeight(0.9);
        rules.add(rule4);
        System.out.println("Rule 4 (Weight=0.9): IF Rain is HEAVY THEN Water is SHORT");

        // Rule 5: Light rain with dry soil
        FuzzyRule rule5 = new FuzzyRule("AND", new FuzzyConsequent(water, "MEDIUM"));
        rule5.addAntecedent(new FuzzyCondition(rain, "LIGHT"));
        rule5.addAntecedent(new FuzzyCondition(soil, "DRY"));
        rule5.setWeight(0.7);
        rules.add(rule5);
        System.out.println("Rule 5 (Weight=0.7): IF Rain is LIGHT AND Soil is DRY THEN Water is MEDIUM");

        // ===================================================================
        // PHASE 4: TEST SCENARIOS
        // ===================================================================

        System.out.println("\n\n=== PHASE 4: TEST SCENARIOS ===\n");

        // Scenario 1: Hot dry day, no rain
        System.out.println("--- SCENARIO 1: Hot Dry Day (No Rain) ---");
        soil.setValue(20);   // Very dry
        temp.setValue(38);   // Hot
        rain.setValue(0);    // No rain

        System.out.println("Inputs: Soil=20% (DRY), Temp=38°C (HOT), Rain=0mm (NONE)");
        demonstrateMembershipDegrees(soil, temp, rain);

        double scenario1Output = runMamdaniInference(inputs, rules);
        System.out.println(">>> RECOMMENDED WATERING: " + String.format("%.2f", scenario1Output) + " minutes <<<");
        System.out.println("Interpretation: Long watering needed for dry soil in hot weather\n");

        // Scenario 2: Moderate conditions
        System.out.println("\n--- SCENARIO 2: Moderate Conditions ---");
        soil.setValue(50);   // Normal
        temp.setValue(25);   // Warm
        rain.setValue(2);    // Light rain

        System.out.println("Inputs: Soil=50% (NORMAL), Temp=25°C (WARM), Rain=2mm (LIGHT)");
        demonstrateMembershipDegrees(soil, temp, rain);

        double scenario2Output = runMamdaniInference(inputs, rules);
        System.out.println(">>> RECOMMENDED WATERING: " + String.format("%.2f", scenario2Output) + " minutes <<<");
        System.out.println("Interpretation: Medium watering for balanced conditions\n");

        // Scenario 3: Wet soil with heavy rain
        System.out.println("\n--- SCENARIO 3: Wet Soil with Heavy Rain ---");
        soil.setValue(75);   // Wet
        temp.setValue(20);   // Cool
        rain.setValue(7);    // Heavy rain

        System.out.println("Inputs: Soil=75% (WET), Temp=20°C (COOL), Rain=7mm (HEAVY)");
        demonstrateMembershipDegrees(soil, temp, rain);

        double scenario3Output = runMamdaniInference(inputs, rules);
        System.out.println(">>> RECOMMENDED WATERING: " + String.format("%.2f", scenario3Output) + " minutes <<<");
        System.out.println("Interpretation: Minimal watering needed, nature handles it\n");

        // ===================================================================
        // PHASE 5: DIFFERENT INFERENCE METHODS
        // ===================================================================

        System.out.println("\n=== PHASE 5: COMPARING INFERENCE METHODS ===\n");

        // Reset to scenario 1
        soil.setValue(20);
        temp.setValue(38);
        rain.setValue(0);

        System.out.println("Using same inputs (Scenario 1):");
        System.out.println("Soil=20%, Temp=38°C, Rain=0mm\n");

        // Mamdani with different defuzzification
        System.out.println("--- Mamdani Inference ---");
        MamdaniInferenceEngine mamdaniCentroid = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0, 1000,
            new MinImplication(),
            new MaxAggregation(),
            DefuzzificationMethod.CENTROID
        );
        double outputCentroid = mamdaniCentroid.evaluate();
        System.out.println("Centroid defuzzification: " + String.format("%.2f", outputCentroid) + " minutes");

        MamdaniInferenceEngine mamdaniMOM = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0, 1000,
            new MinImplication(),
            new MaxAggregation(),
            DefuzzificationMethod.MEAN_OF_MAXIMUM
        );
        double outputMOM = mamdaniMOM.evaluate();
        System.out.println("Mean of Maximum defuzzification: " + String.format("%.2f", outputMOM) + " minutes");

        // Sugeno with crisp outputs
        System.out.println("\n--- Sugeno Inference (Zero-Order) ---");
        List<FuzzyRule> sugenoRules = createSugenoRules(soil, temp, rain, water);
        SugenoInferenceEngine sugenoEngine = new SugenoInferenceEngine(inputs, sugenoRules);
        double sugenoOutput = sugenoEngine.evaluate();
        System.out.println("Sugeno output: " + String.format("%.2f", sugenoOutput) + " minutes");

        // ===================================================================
        // PHASE 6: DIFFERENT OPERATORS
        // ===================================================================

        System.out.println("\n\n=== PHASE 6: TESTING DIFFERENT OPERATORS ===\n");

        System.out.println("Same rules, different operators:\n");

        // Min implication, Max aggregation (default)
        System.out.println("1. Min Implication + Max Aggregation:");
        MamdaniInferenceEngine engine1 = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0, 1000,
            new MinImplication(),
            new MaxAggregation(),
            DefuzzificationMethod.CENTROID
        );
        System.out.println("   Output: " + String.format("%.2f", engine1.evaluate()) + " minutes");

        // Product implication, Max aggregation
        System.out.println("\n2. Product Implication + Max Aggregation:");
        MamdaniInferenceEngine engine2 = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0, 1000,
            new ProductImplication(),
            new MaxAggregation(),
            DefuzzificationMethod.CENTROID
        );
        System.out.println("   Output: " + String.format("%.2f", engine2.evaluate()) + " minutes");

        // Min implication, Sum aggregation
        System.out.println("\n3. Min Implication + Sum Aggregation:");
        MamdaniInferenceEngine engine3 = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0, 1000,
            new MinImplication(),
            new SumAggregation(),
            DefuzzificationMethod.CENTROID
        );
        System.out.println("   Output: " + String.format("%.2f", engine3.evaluate()) + " minutes");

        System.out.println("\nNote: Different operators produce different outputs,");
        System.out.println("allowing flexibility based on application requirements.");

        // ===================================================================
        // PHASE 7: RULE WEIGHTS IMPACT
        // ===================================================================

        System.out.println("\n\n=== PHASE 7: IMPACT OF RULE WEIGHTS ===\n");

        System.out.println("Demonstrating how weights affect output:\n");

        // All weights = 1.0
        for (FuzzyRule rule : rules) {
            rule.setWeight(1.0);
        }
        MamdaniInferenceEngine engineEqual = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0
        );
        double outputEqual = engineEqual.evaluate();
        System.out.println("All weights = 1.0: " + String.format("%.2f", outputEqual) + " minutes");

        // Prioritize expert rules (rule 1 and 3)
        rule1.setWeight(1.0);
        rule2.setWeight(0.5);
        rule3.setWeight(1.0);
        rule4.setWeight(0.6);
        rule5.setWeight(0.4);
        MamdaniInferenceEngine enginePrioritized = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0
        );
        double outputPrioritized = enginePrioritized.evaluate();
        System.out.println("Expert rules prioritized: " + String.format("%.2f", outputPrioritized) + " minutes");
        System.out.println("Difference: " + String.format("%.2f", Math.abs(outputEqual - outputPrioritized)) + " minutes");

        // ===================================================================
        // PHASE 8: ENABLE/DISABLE RULES
        // ===================================================================

        System.out.println("\n\n=== PHASE 8: ENABLE/DISABLE RULES (SOFT DELETE) ===\n");

        // Reset weights
        for (FuzzyRule rule : rules) {
            rule.setWeight(1.0);
        }

        System.out.println("Testing with all rules enabled:");
        MamdaniInferenceEngine engineAllRules = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0
        );
        double outputAllRules = engineAllRules.evaluate();
        System.out.println("All rules enabled: " + String.format("%.2f", outputAllRules) + " minutes");

        // Simulate disabling rule 4 (heavy rain rule) by setting weight to 0
        System.out.println("\nSimulating disabled rain rule (weight=0):");
        rule4.setWeight(0.0);
        MamdaniInferenceEngine engineNoRain = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0
        );
        double outputNoRain = engineNoRain.evaluate();
        System.out.println("Rain rule disabled: " + String.format("%.2f", outputNoRain) + " minutes");
        System.out.println("Impact: " + String.format("%.2f", Math.abs(outputAllRules - outputNoRain)) + " minutes difference");

        System.out.println("\nNote: In the API, rules have an 'enabled' flag.");
        System.out.println("Disabled rules are automatically filtered out during inference.");

        // ===================================================================
        // PHASE 9: NATURAL LANGUAGE RULE PARSING
        // ===================================================================

        System.out.println("\n\n=== PHASE 9: NATURAL LANGUAGE RULE PARSING ===\n");

        System.out.println("The system can parse rules from natural language:");
        System.out.println();

        RuleParser parser = new RuleParser();

        String[] exampleRules = {
            "IF Soil Moisture is DRY AND Temperature is HOT THEN Water Duration is LONG",
            "IF Soil Moisture is WET THEN Water Duration is SHORT WEIGHT 0.9",
            "IF Rain Forecast is HEAVY THEN Water Duration is SHORT",
            "IF Temperature is WARM OR Soil Moisture is NORMAL THEN Water Duration is MEDIUM WEIGHT 0.7"
        };

        for (int i = 0; i < exampleRules.length; i++) {
            try {
                System.out.println("Rule " + (i + 1) + ":");
                System.out.println("  Input: \"" + exampleRules[i] + "\"");
                RuleDocument doc = parser.parse(exampleRules[i]);
                System.out.println("  Parsed successfully!");
                System.out.println("  Operator: " + doc.operator);
                System.out.println("  Conditions: " + doc.conditions.size());
                System.out.println("  Weight: " + doc.weight);
                System.out.println("  Enabled: " + doc.enabled);
                System.out.println();
            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
                System.out.println();
            }
        }

        // ===================================================================
        // PHASE 10: REST API INTEGRATION
        // ===================================================================


        System.out.println("\n================================================================");
        System.out.println("    CASE STUDY COMPLETE - FUZZY LOGIC MODULE");
        System.out.println("================================================================");
    }

    // Helper method to demonstrate membership degrees
    private static void demonstrateMembershipDegrees(SoilMoisture soil, Temperature temp, RainForecast rain) {
        System.out.println("Membership Degrees:");
        System.out.println("  Soil - DRY: " + String.format("%.3f", soil.getMembership("DRY")) +
                         ", NORMAL: " + String.format("%.3f", soil.getMembership("NORMAL")) +
                         ", WET: " + String.format("%.3f", soil.getMembership("WET")));
        System.out.println("  Temp - COLD: " + String.format("%.3f", temp.getMembership("COLD")) +
                         ", WARM: " + String.format("%.3f", temp.getMembership("WARM")) +
                         ", HOT: " + String.format("%.3f", temp.getMembership("HOT")));
        System.out.println("  Rain - NONE: " + String.format("%.3f", rain.getMembership("NONE")) +
                         ", LIGHT: " + String.format("%.3f", rain.getMembership("LIGHT")) +
                         ", HEAVY: " + String.format("%.3f", rain.getMembership("HEAVY")));
    }

    // Helper method to run Mamdani inference
    private static double runMamdaniInference(Map<String, FuzzyVariable> inputs, List<FuzzyRule> rules) {
        MamdaniInferenceEngine engine = new MamdaniInferenceEngine(
            inputs, rules, 0.0, 30.0
        );
        return engine.evaluate();
    }

    // Helper method to create Sugeno rules
    private static List<FuzzyRule> createSugenoRules(SoilMoisture soil, Temperature temp, RainForecast rain, WaterDuration water) {
        List<FuzzyRule> sugenoRules = new ArrayList<>();

        FuzzyRule s1 = new FuzzyRule("AND", new FuzzyConsequent(water, 25.0));
        s1.addAntecedent(new FuzzyCondition(soil, "DRY"));
        s1.addAntecedent(new FuzzyCondition(temp, "HOT"));
        s1.setWeight(1.0);
        sugenoRules.add(s1);

        FuzzyRule s2 = new FuzzyRule("OR", new FuzzyConsequent(water, 15.0));
        s2.addAntecedent(new FuzzyCondition(soil, "NORMAL"));
        s2.addAntecedent(new FuzzyCondition(temp, "WARM"));
        s2.setWeight(0.8);
        sugenoRules.add(s2);

        FuzzyRule s3 = new FuzzyRule("AND", new FuzzyConsequent(water, 5.0));
        s3.addAntecedent(new FuzzyCondition(soil, "WET"));
        s3.setWeight(1.0);
        sugenoRules.add(s3);

        FuzzyRule s4 = new FuzzyRule("AND", new FuzzyConsequent(water, 3.0));
        s4.addAntecedent(new FuzzyCondition(rain, "HEAVY"));
        s4.setWeight(0.9);
        sugenoRules.add(s4);

        return sugenoRules;
    }
}

