package FuzzyLogic.Rules;

import FuzzyLogic.Apis.RuleDocument;
import FuzzyLogic.Apis.RuleRepository;
import FuzzyLogic.Inference.*;
import FuzzyLogic.Membership.MembershipFunction;
import FuzzyLogic.Operators.*;
import FuzzyLogic.Variable.*;

import java.util.*;

public class TestRuleSystem {

    public static void main(String[] args) {

        System.out.println("=== TESTING FUZZY IRRIGATION SYSTEM (DB-DRIVEN RULES) ===\n");

        // ---------------------------------------------------------
        // 1. Create fuzzy variables and set test input values
        // ---------------------------------------------------------

        SoilMoisture soil = new SoilMoisture();
        Temperature temp = new Temperature();
        RainForecast rain = new RainForecast();
        WaterDuration water = new WaterDuration();

        soil.setValue(50);   // Example: mid-level moisture
        temp.setValue(32);   // Example: warm/hot
        rain.setValue(6);    // Example: medium–heavy rain

        System.out.println("Input Values:");
        System.out.println("   Soil Moisture : " + soil.getValue());
        System.out.println("   Temperature   : " + temp.getValue());
        System.out.println("   Rain Forecast : " + rain.getValue() + "\n");

        // Map of variables for rule evaluation
        Map<String, FuzzyVariable> inputs = new HashMap<>();
        inputs.put(soil.getName(), soil);
        inputs.put(temp.getName(), temp);
        inputs.put(rain.getName(), rain);
        inputs.put(water.getName(), water);

        // ---------------------------------------------------------
        // 2. Load rule documents from MongoDB
        // ---------------------------------------------------------

        System.out.println("--- LOADING RULES FROM MONGODB ---");

        RuleRepository repo = new RuleRepository();
        List<RuleDocument> documents = repo.findAll();

        if (documents.isEmpty()) {
            System.out.println("❌ No rules found in MongoDB. Please insert rules first.");
            return;
        }

        System.out.println("Loaded " + documents.size() + " rule(s) from database.\n");

        // ---------------------------------------------------------
        // 3. Convert rule documents to FuzzyRule objects
        // ---------------------------------------------------------

        List<FuzzyRule> mamdaniRules = new ArrayList<>();
        List<FuzzyRule> sugenoRules = new ArrayList<>();

        for (RuleDocument doc : documents) {
            FuzzyRule fuzzyRule = RuleConverter.toFuzzyRule(doc, inputs);

            // Mamdani: fuzzy consequents only
            if (isFuzzyClass(doc.output._class)) {
                mamdaniRules.add(fuzzyRule);
            }

            // Sugeno: crisp consequents only
            if (isNumeric(doc.output._class)) {
                sugenoRules.add(fuzzyRule);
            }
        }

        if (mamdaniRules.isEmpty()) {
            System.out.println("⚠ No Mamdani rules found. Ensure output classes are SHORT/MEDIUM/LONG.");
        }

        if (sugenoRules.isEmpty()) {
            System.out.println("⚠ No Sugeno rules found. Ensure output values are numeric.");
        }

        // ---------------------------------------------------------
        // 4. Execute Mamdani Inference
        // ---------------------------------------------------------

        System.out.println("\n=== MAMDANI INFERENCE (CENTROID) ===");

        MamdaniInferenceEngine mamEngine = new MamdaniInferenceEngine(
                inputs,
                mamdaniRules,
                0.0,
                30.0
        );

        double mamdaniOutput = mamEngine.evaluate();
        System.out.println("Mamdani Final Output = " + mamdaniOutput + " minutes\n");

        // ---------------------------------------------------------
        // 5. Execute Sugeno Inference (if numeric rules exist)
        // ---------------------------------------------------------

        if (!sugenoRules.isEmpty()) {
            System.out.println("\n=== SUGENO INFERENCE (ZERO-ORDER) ===");

            SugenoInferenceEngine sugEngine = new SugenoInferenceEngine(inputs, sugenoRules);
            double sugenoOutput = sugEngine.evaluate();

            System.out.println("Sugeno Final Output = " + sugenoOutput + " minutes\n");
        }

        System.out.println("\n=== END OF TEST ===");
    }

    // ---------------------------------------------------------
    // Helper: check if output class is fuzzy (SHORT/MEDIUM/LONG)
    // ---------------------------------------------------------
    private static boolean isFuzzyClass(String clazz) {
        String c = clazz.toUpperCase();
        return c.equals("SHORT") || c.equals("MEDIUM") || c.equals("LONG");
    }

    // ---------------------------------------------------------
    // Helper: detect numeric Sugeno consequents
    // ---------------------------------------------------------
    private static boolean isNumeric(String clazz) {
        try {
            Double.parseDouble(clazz);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
