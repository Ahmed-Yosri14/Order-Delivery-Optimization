package FuzzyLogic.Rules;

import FuzzyLogic.Variable.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class to verify Phase 2 implementation
 */
public class TestRuleSystem {
    public static void main(String[] args) {
        System.out.println("=== Testing Phase 2: Rule System Foundation ===\n");

        // Create fuzzy variables
        SoilMoisture soil = new SoilMoisture();
        Temperature temp = new Temperature();
        RainForecast rain = new RainForecast();
        WaterDuration water = new WaterDuration();

        // Set input values
        soil.setValue(25.0);  // Dry soil
        temp.setValue(38.0);  // Hot temperature
        rain.setValue(0.0);   // No rain

        System.out.println("Input Values:");
        System.out.println("  Soil Moisture: " + soil.getValue() + "%");
        System.out.println("  Temperature: " + temp.getValue() + "°C");
        System.out.println("  Rain Forecast: " + rain.getValue() + " mm\n");

        // Test FuzzyCondition
        System.out.println("--- Testing FuzzyCondition ---");
        FuzzyCondition cond1 = new FuzzyCondition(soil, "Dry");
        FuzzyCondition cond2 = new FuzzyCondition(temp, "Hot");
        FuzzyCondition cond3 = new FuzzyCondition(rain, "None");

        System.out.println("Evaluating conditions:");
        System.out.println("  " + cond1.getVariable().getName() + " is " + cond1.getLinguisticTerm() + ": " + cond1.evaluate());
        System.out.println("  " + cond2.getVariable().getName() + " is " + cond2.getLinguisticTerm() + ": " + cond2.evaluate());
        System.out.println("  " + cond3.getVariable().getName() + " is " + cond3.getLinguisticTerm() + ": " + cond3.evaluate());
        System.out.println();

        // Test FuzzyConsequent (Fuzzy/Mamdani)
        System.out.println("--- Testing FuzzyConsequent (Mamdani) ---");
        FuzzyConsequent consequentFuzzy = new FuzzyConsequent(water, "Long");
        System.out.println("  Consequent: " + consequentFuzzy);
        System.out.println("  Type: " + consequentFuzzy.getType());
        System.out.println("  Is Fuzzy: " + consequentFuzzy.isFuzzy());
        System.out.println();

        // Test FuzzyConsequent (Crisp/Sugeno)
        System.out.println("--- Testing FuzzyConsequent (Sugeno) ---");
        FuzzyConsequent consequentCrisp = new FuzzyConsequent(water, 25.0);
        System.out.println("  Consequent: " + consequentCrisp);
        System.out.println("  Type: " + consequentCrisp.getType());
        System.out.println("  Is Crisp: " + consequentCrisp.isCrisp());
        System.out.println();

        // Test FuzzyRule with AND
        System.out.println("--- Testing FuzzyRule (AND logic) ---");
        FuzzyRule rule1 = new FuzzyRule("AND", consequentFuzzy);
        rule1.addAntecedent(new FuzzyCondition(soil, "Dry"));
        rule1.addAntecedent(new FuzzyCondition(temp, "Hot"));
        rule1.addAntecedent(new FuzzyCondition(rain, "None"));

        Map<String, FuzzyVariable> inputs = new HashMap<>();
        inputs.put(soil.getName(), soil);
        inputs.put(temp.getName(), temp);
        inputs.put(rain.getName(), rain);

        double firingStrength = rule1.evaluate(inputs);
        System.out.println("  Rule: " + rule1);
        System.out.println("  Firing Strength: " + firingStrength);
        System.out.println();

        // Test FuzzyRule with OR
        System.out.println("--- Testing FuzzyRule (OR logic) ---");
        FuzzyConsequent shortWater = new FuzzyConsequent(water, "Short");
        FuzzyRule rule2 = new FuzzyRule("OR", shortWater);
        rule2.addAntecedent(new FuzzyCondition(soil, "Dry"));
        rule2.addAntecedent(new FuzzyCondition(rain, "Heavy"));

        double firingStrength2 = rule2.evaluate(inputs);
        System.out.println("  Rule: " + rule2);
        System.out.println("  Firing Strength: " + firingStrength2);
        System.out.println();


    }
}
