package FuzzyLogic.Rules;

import FuzzyLogic.Variable.*;
import FuzzyLogic.Variable.Enums.RainForecastClass;
import FuzzyLogic.Variable.Enums.SoilMoistureClass;
import FuzzyLogic.Variable.Enums.TemperatureClass;
import FuzzyLogic.Variable.Enums.WaterDurationClass;

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
        FuzzyCondition<SoilMoistureClass> cond1 = new FuzzyCondition<>(soil, SoilMoistureClass.DRY);
        FuzzyCondition<TemperatureClass> cond2 = new FuzzyCondition<>(temp, TemperatureClass.HOT);
        FuzzyCondition<RainForecastClass> cond3 = new FuzzyCondition<>(rain, RainForecastClass.NONE);

        System.out.println("Evaluating conditions:");
        System.out.println("  " + cond1.getVariable().getName() + " is " + cond1.getLinguisticTerm() + ": " + cond1.evaluate());
        System.out.println("  " + cond2.getVariable().getName() + " is " + cond2.getLinguisticTerm() + ": " + cond2.evaluate());
        System.out.println("  " + cond3.getVariable().getName() + " is " + cond3.getLinguisticTerm() + ": " + cond3.evaluate());
        System.out.println();

        // Test FuzzyConsequent (Fuzzy/Mamdani)
        System.out.println("--- Testing FuzzyConsequent (Mamdani) ---");
        FuzzyConsequent<WaterDurationClass> consequentFuzzy = new FuzzyConsequent<>(water, WaterDurationClass.LONG);
        System.out.println("  Consequent: " + consequentFuzzy);
        System.out.println("  Type: " + consequentFuzzy.getType());
        System.out.println("  Is Fuzzy: " + consequentFuzzy.isFuzzy());
        System.out.println();

        // Test FuzzyConsequent (Crisp/Sugeno)
        System.out.println("--- Testing FuzzyConsequent (Sugeno) ---");
        FuzzyConsequent<WaterDurationClass> consequentCrisp = new FuzzyConsequent<>(water, 25.0);
        System.out.println("  Consequent: " + consequentCrisp);
        System.out.println("  Type: " + consequentCrisp.getType());
        System.out.println("  Is Crisp: " + consequentCrisp.isCrisp());
        System.out.println();

        // Test FuzzyRule with AND
        System.out.println("--- Testing FuzzyRule (AND logic) ---");
        FuzzyRule rule1 = new FuzzyRule("AND", consequentFuzzy);
        rule1.addAntecedent(new FuzzyCondition<>(soil, SoilMoistureClass.DRY));
        rule1.addAntecedent(new FuzzyCondition<>(temp, TemperatureClass.HOT));
        rule1.addAntecedent(new FuzzyCondition<>(rain, RainForecastClass.NONE));

        double firingStrength = rule1.evaluate();
        System.out.println("  Rule: " + rule1);
        System.out.println("  Firing Strength: " + firingStrength);
        System.out.println();

        // Test FuzzyRule with OR
        System.out.println("--- Testing FuzzyRule (OR logic) ---");
        FuzzyConsequent<WaterDurationClass> shortWater = new FuzzyConsequent<>(water, WaterDurationClass.SHORT);
        FuzzyRule rule2 = new FuzzyRule("OR", shortWater);
        rule2.addAntecedent(new FuzzyCondition<>(soil, SoilMoistureClass.DRY));
        rule2.addAntecedent(new FuzzyCondition<>(rain, RainForecastClass.HEAVY));

        double firingStrength2 = rule2.evaluate();
        System.out.println("  Rule: " + rule2);
        System.out.println("  Firing Strength: " + firingStrength2);
        System.out.println();


    }
}
