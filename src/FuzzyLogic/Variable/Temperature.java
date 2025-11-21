package FuzzyLogic.Variable;

import FuzzyLogic.Membership.TriangularMF;
import FuzzyLogic.Variable.Enums.TemperatureClass;

public class Temperature extends FuzzyVariable<TemperatureClass> {
    public Temperature() {
        super("Temperature", TemperatureClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(TemperatureClass.COLD, new TriangularMF(0, 8, 16));
        sets.put(TemperatureClass.WARM, new TriangularMF(12, 22, 32));
        sets.put(TemperatureClass.HOT, new TriangularMF(26, 38, 50));
    }
}
