package FuzzyLogic.Variable;

import FuzzyLogic.Membership.*;
import FuzzyLogic.Variable.Enums.SoilMoistureClass;

public class SoilMoisture extends FuzzyVariable<SoilMoistureClass> {

    public SoilMoisture() {
        super("Soil Moisture", SoilMoistureClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        sets.put(SoilMoistureClass.DRY, new TriangularMF(0, 0, 40));
        sets.put(SoilMoistureClass.NORMAL, new TriangularMF(30, 50, 70));
        sets.put(SoilMoistureClass.WET, new TriangularMF(60, 100, 100));
    }
}
