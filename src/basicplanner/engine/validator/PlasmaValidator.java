package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Contour;
import elements.Element;

import java.util.Set;

public class PlasmaValidator implements SourceValidator {

    double maxThickness;
    boolean skewCutAbility;

    public PlasmaValidator(double maxThickness, boolean skewCutAbility) {
        this.maxThickness = maxThickness;
        this.skewCutAbility = skewCutAbility;
    }

    @Override
    public boolean validate(Set<? extends Element> contours, Piece piece) {
        boolean cond1 = piece.getHeight() <= maxThickness;
        boolean isSkewed = false;
        for (Element c : contours) {
            if (!(c instanceof Contour)){
                return false;
            }
            if (((Contour)c).getPointList().stream().anyMatch(p -> p.ang1 != 0 || p.ang2 != 0)) {
                isSkewed = true;
            }
        }
        boolean cond2 = skewCutAbility || !isSkewed;
        return cond1 && cond2;
    }
}
