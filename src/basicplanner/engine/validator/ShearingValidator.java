package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Contour;
import elements.Element;

import java.util.Set;

public class ShearingValidator implements SourceValidator {

    double maxThickness;

    public ShearingValidator(double maxThickness) {
        this.maxThickness = maxThickness;
    }

    @Override
    public boolean validate(Set<? extends Element> contour, Piece piece) {
        //Проверка на прямолинейность и выпуклость контура. Если и прямолинейный и выпуклый, то вырубка на гильотине
        // возможна
        for (Element c : contour) {
            if (!(c instanceof Contour)){
                return false;
            }
            if (!(auxillary.ContourSolver.isConvexNGon((Contour) c) && piece.getHeight() <= maxThickness)) return false;
        }
        return true;
    }
}