package FuzzyLogic.Variable;

import FuzzyLogic.Membership.*;
import FuzzyLogic.Variable.Enums.RainForecastClass;

public class RainForecast extends FuzzyVariable<RainForecastClass> {

    public RainForecast() {
        super("Rain Forecast", RainForecastClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(RainForecastClass.NONE, new TrapezoidalMF(0, 0, 0.5, 1.5));
        sets.put(RainForecastClass.LIGHT, new TrapezoidalMF(0.8, 1.5, 3.5, 4.5));
        sets.put(RainForecastClass.HEAVY, new TrapezoidalMF(3.5, 5, 10, 10));
    }

    protected void getCentroid() {

    }
}
