package FuzzyLogic.Variable;

import FuzzyLogic.Membership.*;

public class RainForecast extends FuzzyVariable {

    public RainForecast() {
        super("Rain Forecast");
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put("None", new TrapezoidalMF(0, 0, 1, 2));
        sets.put("Light", new TrapezoidalMF(1, 3, 5, 7));
        sets.put("Heavy", new TrapezoidalMF(5, 8, 10, 10));
    }
}
