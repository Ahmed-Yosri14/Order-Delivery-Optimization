package FuzzyLogic.Rules;

import FuzzyLogic.Variable.*;
import FuzzyLogic.Membership.MembershipFunction;
import FuzzyLogic.Inference.SugenoInferenceEngine;
import FuzzyLogic.Inference.MamdaniInferenceEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRuleSystem {
    public static void main(String[] args) {
        System.out.println("=== Testing Phase 2: Rule System Foundation ===\n");

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

        System.out.println("--- Building duration rules and computing centroid-average crisp value ---");

        FuzzyConsequent consShort = new FuzzyConsequent(water, "Short");
        FuzzyConsequent consMedium = new FuzzyConsequent(water, "Medium");
        FuzzyConsequent consLong = new FuzzyConsequent(water, "Long");

        FuzzyRule rShort = new FuzzyRule("OR", consShort);
        rShort.addAntecedent(new FuzzyCondition(soil, "Wet"));
        rShort.addAntecedent(new FuzzyCondition(rain, "Heavy"));

        FuzzyRule rMedium = new FuzzyRule("AND", consMedium);
        rMedium.addAntecedent(new FuzzyCondition(soil, "Normal"));

        FuzzyRule rLong = new FuzzyRule("AND", consLong);
        rLong.addAntecedent(new FuzzyCondition(soil, "Dry"));
        rLong.addAntecedent(new FuzzyCondition(temp, "Hot"));

        List<FuzzyRule> rules = new ArrayList<>();
        rules.add(rShort);
        rules.add(rMedium);
        rules.add(rLong);

        // Debug prints
        debugEvaluateRule(rShort, inputs, "rShort");
        debugEvaluateRule(rMedium, inputs, "rMedium");
        debugEvaluateRule(rLong, inputs, "rLong");

        // Run Sugeno engine
        SugenoInferenceEngine sugeno = new SugenoInferenceEngine(inputs, rules);
        double sugenoResult = sugeno.evaluate();

        // Run Mamdani engine
        MamdaniInferenceEngine mamdani = new MamdaniInferenceEngine(inputs, rules);
        double mamdaniResult = mamdani.evaluate();

        System.out.println("\nSummary:");
        System.out.println("  Sugeno crisp output: " + String.format("%.3f", sugenoResult));
        System.out.println("  Mamdani crisp output: " + String.format("%.3f", mamdaniResult));
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