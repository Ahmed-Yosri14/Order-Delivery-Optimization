package FuzzyLogic.Variable;

import FuzzyLogic.Membership.GaussianMF;

public class Temperature extends FuzzyVariable {
    public Temperature() {
        super("Temperature");
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put("Cold", new GaussianMF(10, 5));
        sets.put("Warm", new GaussianMF(25, 5));
        sets.put("Hot", new GaussianMF(40, 5));
    }
}
