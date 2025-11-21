package FuzzyLogic.Variable;

import FuzzyLogic.Membership.GaussianMF;
import FuzzyLogic.Variable.Enums.TemperatureClass;

public class Temperature extends FuzzyVariable<TemperatureClass> {
    public Temperature() {
        super("Temperature", TemperatureClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(TemperatureClass.COLD, new GaussianMF(10, 5));
        sets.put(TemperatureClass.WARM, new GaussianMF(25, 5));
        sets.put(TemperatureClass.HOT, new GaussianMF(40, 5));
    }
}
