package FuzzyLogic.Variable;

import FuzzyLogic.Membership.TriangularMF;
import FuzzyLogic.Variable.Enums.WaterDurationClass;

public class WaterDuration extends FuzzyVariable<WaterDurationClass> {
    public WaterDuration() {
        super("Water Duration", WaterDurationClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(WaterDurationClass.SHORT, new TriangularMF(0, 0, 10));
        sets.put(WaterDurationClass.MEDIUM, new TriangularMF(5, 15, 25));
        sets.put(WaterDurationClass.LONG, new TriangularMF(20, 30, 30));
    }
}
