package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Contour;

public class PlasmaValidator implements SourceValidator<Contour>{

    double maxThickness;
    boolean skewCutAbility;
    public PlasmaValidator(double maxThickness, boolean skewCutAbility) {
        this.maxThickness = maxThickness;
        this.skewCutAbility = skewCutAbility;
    }

    @Override
    public boolean validate(Contour contour, Piece piece){
        boolean cond1 =  piece.getHeight() <= maxThickness;
        boolean isSkewed = contour.getPointList().stream().anyMatch(p -> p.ang1 != 0 || p.ang2 != 0);
        boolean cond2 = skewCutAbility || !isSkewed;
        return  cond1 && cond2;
    }
}
