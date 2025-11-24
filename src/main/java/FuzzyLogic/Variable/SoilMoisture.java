package FuzzyLogic.Variable;

import FuzzyLogic.Membership.*;
import FuzzyLogic.Variable.Enums.SoilMoistureClass;

public class SoilMoisture extends FuzzyVariable<SoilMoistureClass> {

    public SoilMoisture() {
        super("Soil Moisture", SoilMoistureClass.class);
    }

    @Override
    protected void defineMembershipFunctions() {
        // Wider, overlapping triangles so a single soil value can belong partly to Dry, Normal and Wet
        sets.put(SoilMoistureClass.DRY, new TriangularMF(0, 30, 60));
        sets.put(SoilMoistureClass.NORMAL, new TriangularMF(30, 50, 70));
        sets.put(SoilMoistureClass.WET, new TriangularMF(45, 75, 100));
    }
}
