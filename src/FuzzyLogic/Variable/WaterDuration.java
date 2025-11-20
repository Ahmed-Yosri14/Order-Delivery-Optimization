package FuzzyLogic.Variable;

import FuzzyLogic.Membership.TriangularMF;

public class WaterDuration extends FuzzyVariable {
    public WaterDuration() {
        super("Water Duration");
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put("Short", new TriangularMF(0, 0, 10));
        sets.put("Medium", new TriangularMF(5, 15, 25));
        sets.put("Long", new TriangularMF(20, 30, 30));
    }
}
