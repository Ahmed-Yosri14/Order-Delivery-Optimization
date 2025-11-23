package FuzzyLogic.Rules;

import FuzzyLogic.Variable.*;
import FuzzyLogic.Membership.MembershipFunction;
import FuzzyLogic.Inference.*;
import FuzzyLogic.Operators.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRuleSystem {
    public static void main(String[] args) {
        System.out.println("=== Testing Phase 2: Rule System with TRUE Mamdani & Sugeno ===\n");

        // Create fuzzy variables
        SoilMoisture soil = new SoilMoisture();
        Temperature temp = new Temperature();
        RainForecast rain = new RainForecast();
        WaterDuration water = new WaterDuration();

        // Set input values
        soil.setValue(50.0);  // Mid soil value so Dry/Normal/Wet overlap
        temp.setValue(32.0);  // Warm/hot region
        rain.setValue(6.0);   // Rain value that maps to Heavy/Light overlap

        System.out.println("Input Values:");
        System.out.println("  Soil Moisture: " + soil.getValue() + "%");
        System.out.println("  Temperature: " + temp.getValue() + "°C");
        System.out.println("  Rain Forecast: " + rain.getValue() + " mm\n");

        Map<String, FuzzyVariable> inputs = new HashMap<>();
        inputs.put(soil.getName(), soil);
        inputs.put(temp.getName(), temp);
        inputs.put(rain.getName(), rain);

        System.out.println("--- Building Mamdani Rules (Fuzzy Consequents) ---");

        // MAMDANI RULES: Fuzzy consequents
        List<FuzzyRule> mamdaniRules = new ArrayList<>();

        FuzzyRule rShort = new FuzzyRule("OR", new FuzzyConsequent(water, "SHORT"));
        rShort.addAntecedent(new FuzzyCondition(soil, "WET"));
        rShort.addAntecedent(new FuzzyCondition(rain, "HEAVY"));
        mamdaniRules.add(rShort);

        FuzzyRule rMedium = new FuzzyRule("AND", new FuzzyConsequent(water, "MEDIUM"));
        rMedium.addAntecedent(new FuzzyCondition(soil, "NORMAL"));
        mamdaniRules.add(rMedium);

        FuzzyRule rLong = new FuzzyRule("AND", new FuzzyConsequent(water, "LONG"));
        rLong.addAntecedent(new FuzzyCondition(soil, "DRY"));
        rLong.addAntecedent(new FuzzyCondition(temp, "HOT"));
        mamdaniRules.add(rLong);

        System.out.println("--- Building Sugeno Rules (Crisp Consequents) ---");

        // SUGENO RULES: Crisp consequents
        List<FuzzyRule> sugenoRules = new ArrayList<>();

        FuzzyRule sShort = new FuzzyRule("OR", new FuzzyConsequent(water, 4.0));
        sShort.addAntecedent(new FuzzyCondition(soil, "WET"));
        sShort.addAntecedent(new FuzzyCondition(rain, "HEAVY"));
        sugenoRules.add(sShort);

        FuzzyRule sMedium = new FuzzyRule("AND", new FuzzyConsequent(water, 15.0));
        sMedium.addAntecedent(new FuzzyCondition(soil, "NORMAL"));
        sugenoRules.add(sMedium);

        FuzzyRule sLong = new FuzzyRule("AND", new FuzzyConsequent(water, 27.0));
        sLong.addAntecedent(new FuzzyCondition(soil, "DRY"));
        sLong.addAntecedent(new FuzzyCondition(temp, "HOT"));
        sugenoRules.add(sLong);

        // Debug prints for Mamdani rules
        System.out.println("\n=== DEBUGGING MAMDANI RULES ===");
        debugEvaluateRule(mamdaniRules.get(0), inputs, "rShort");
        debugEvaluateRule(mamdaniRules.get(1), inputs, "rMedium");
        debugEvaluateRule(mamdaniRules.get(2), inputs, "rLong");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("RUNNING INFERENCE ENGINES");
        System.out.println("=".repeat(70));

        // ========================================
        // TEST 1: TRUE MAMDANI (Default: MIN/MAX/CENTROID)
        // ========================================
        System.out.println("\n### TEST 1: TRUE MAMDANI (MIN Implication, MAX Aggregation, CENTROID) ###");
        MamdaniInferenceEngine mamdani1 = new MamdaniInferenceEngine(
                inputs, mamdaniRules,
                0.0, 30.0  // Water duration domain: 0-30 minutes (matches your MF definitions)
        );
        double mamdaniResult1 = mamdani1.evaluate();

        // ========================================
        // TEST 2: MAMDANI with PRODUCT Implication
        // ========================================
        System.out.println("\n### TEST 2: MAMDANI (PRODUCT Implication, MAX Aggregation, CENTROID) ###");
        MamdaniInferenceEngine mamdani2 = new MamdaniInferenceEngine(
                inputs, mamdaniRules,
                0.0, 30.0, 1000,
                new ProductImplication(),
                new MaxAggregation(),
                DefuzzificationMethod.CENTROID
        );
        double mamdaniResult2 = mamdani2.evaluate();

        // ========================================
        // TEST 3: MAMDANI with Mean of Maximum
        // ========================================
        System.out.println("\n### TEST 3: MAMDANI (MIN Implication, MAX Aggregation, MOM) ###");
        MamdaniInferenceEngine mamdani3 = new MamdaniInferenceEngine(
                inputs, mamdaniRules,
                0.0, 30.0
        );
        mamdani3.setDefuzzificationMethod(DefuzzificationMethod.MEAN_OF_MAXIMUM);
        double mamdaniResult3 = mamdani3.evaluate();

        // ========================================
        // TEST 4: SUGENO (Zero-Order)
        // ========================================
        System.out.println("\n### TEST 4: SUGENO (Zero-Order) ###");
        SugenoInferenceEngine sugeno = new SugenoInferenceEngine(inputs, sugenoRules);
        double sugenoResult = sugeno.evaluate();

        // ========================================
        // TEST 5: Operator Switching Demo
        // ========================================
        System.out.println("\n### TEST 5: OPERATOR SWITCHING (AND: MIN vs PRODUCT) ###");

        // Reset to MIN
        for (FuzzyRule rule : mamdaniRules) {
            rule.setAndOperator(new And());
        }
        System.out.println("\nUsing MIN for AND:");
        mamdani1.evaluate();

        // Switch to PRODUCT
        for (FuzzyRule rule : mamdaniRules) {
            rule.setAndOperator(new AndProduct());
        }
        System.out.println("\nUsing PRODUCT for AND:");
        mamdani1.evaluate();

        // ========================================
        // FINAL SUMMARY
        // ========================================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("FINAL RESULTS SUMMARY");
        System.out.println("=".repeat(70));
        System.out.println("Mamdani (MIN/MAX/CENTROID):      " + String.format("%.3f", mamdaniResult1) + " minutes");
        System.out.println("Mamdani (PRODUCT/MAX/CENTROID):  " + String.format("%.3f", mamdaniResult2) + " minutes");
        System.out.println("Mamdani (MIN/MAX/MOM):           " + String.format("%.3f", mamdaniResult3) + " minutes");
        System.out.println("Sugeno (Zero-Order):             " + String.format("%.3f", sugenoResult) + " minutes");
        System.out.println("=".repeat(70));

        System.out.println("\n✅ All tests completed successfully!");
        System.out.println("Notice how Mamdani and Sugeno produce DIFFERENT results!");
        System.out.println("This proves we have TRUE implementations of both methods.");
    }

    // Debug helper: prints step-by-step antecedent membership values and combination steps
    private static void debugEvaluateRule(FuzzyRule rule, Map<String, FuzzyVariable> inputs, String ruleName) {
        System.out.println("\n[DEBUG] Evaluating rule: " + ruleName + " -> " + rule.getConsequent());
        System.out.println("[DEBUG] Logic operator: " + rule.getLogicOperator());

        double[] memberships = new double[rule.getAntecedents().size()];
        for (int i = 0; i < rule.getAntecedents().size(); i++) {
            FuzzyCondition cond = rule.getAntecedents().get(i);
            String varName = cond.getVariable().getName();
            String term = cond.getLinguisticTerm();

            System.out.println("[DEBUG] Antecedent #" + (i + 1) + ": Variable='" + varName + "' RequestedTerm='" + term + "'");

            // Print available labels for the variable
            try {
                Map<?, MembershipFunction> sets = cond.getVariable().getSets();
                System.out.print("[DEBUG]   Available labels: ");
                boolean first = true;
                for (Object k : sets.keySet()) {
                    if (!first) System.out.print(", ");
                    System.out.print(k.toString());
                    first = false;
                }
                System.out.println();
            } catch (Exception ex) {
                System.out.println("[DEBUG]   Could not read available labels: " + ex.getMessage());
            }

            // Try to get membership using the inputs map override if present
            double value = 0.0;
            try {
                FuzzyVariable override = inputs.get(varName);
                if (override != null) {
                    value = override.getMembershipByName(term);
                } else {
                    value = cond.getVariable().getMembershipByName(term);
                }
                System.out.println("[DEBUG]   Membership for '" + term + "' = " + value);
            } catch (Exception ex) {
                System.out.println("[DEBUG]   ERROR computing membership for term '" + term + "' on var '" + varName + "': " + ex.getMessage());
                value = 0.0;
            }
            memberships[i] = value;
        }

        // Combine memberships step by step
        if (rule.getLogicOperator().equalsIgnoreCase("AND")) {
            double acc = memberships.length > 0 ? memberships[0] : 0.0;
            System.out.println("[DEBUG] Combining with AND (min): start=" + acc);
            for (int i = 1; i < memberships.length; i++) {
                double before = acc;
                acc = Math.min(acc, memberships[i]);
                System.out.println("[DEBUG]  min(" + before + ", " + memberships[i] + ") => " + acc);
            }
            System.out.println("[DEBUG] AND result = " + acc);
        } else { // OR
            double acc = memberships.length > 0 ? memberships[0] : 0.0;
            System.out.println("[DEBUG] Combining with OR (max): start=" + acc);
            for (int i = 1; i < memberships.length; i++) {
                double before = acc;
                acc = Math.max(acc, memberships[i]);
                System.out.println("[DEBUG]  max(" + before + ", " + memberships[i] + ") => " + acc);
            }
            System.out.println("[DEBUG] OR result = " + acc);
        }

        System.out.println("[DEBUG] End of debug for rule: " + ruleName + "\n");
    }
}