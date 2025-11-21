package FuzzyLogic.Variable;

import FuzzyLogic.Membership.*;
import FuzzyLogic.Variable.Enums.RainForecastClass;

public class RainForecast extends FuzzyVariable<RainForecastClass> {

    public RainForecast() {
        super("Rain Forecast", RainForecastClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(RainForecastClass.NONE, new TrapezoidalMF(0, 0, 1, 2));
        sets.put(RainForecastClass.LIGHT, new TrapezoidalMF(1, 3, 5, 7));
        sets.put(RainForecastClass.HEAVY, new TrapezoidalMF(5, 8, 10, 10));
    }
}
