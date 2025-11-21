package FuzzyLogic.Variable;

import FuzzyLogic.Membership.TriangularMF;
import FuzzyLogic.Variable.Enums.WaterDurationClass;

public class WaterDuration extends FuzzyVariable<WaterDurationClass> {
    public WaterDuration() {
        super("Water Duration", WaterDurationClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(WaterDurationClass.SHORT, new TriangularMF(0, 4, 8));
        sets.put(WaterDurationClass.MEDIUM, new TriangularMF(6, 15, 24));
        sets.put(WaterDurationClass.LONG, new TriangularMF(20, 27, 30));
    }
}
