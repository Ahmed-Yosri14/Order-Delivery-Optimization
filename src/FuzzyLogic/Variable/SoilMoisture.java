package FuzzyLogic.Variable;

import FuzzyLogic.Membership.*;

public class SoilMoisture extends FuzzyVariable {

    public SoilMoisture() {
        super("Soil Moisture");
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put("Dry", new TriangularMF(0, 0, 40));
        sets.put("Normal", new TriangularMF(30, 50, 70));
        sets.put("Wet", new TriangularMF(60, 100, 100));
    }
}
