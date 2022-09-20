package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Contour;
import elements.Element;

import java.util.Set;

public class ShearingValidator implements SourceValidator<Contour> {

    double maxThickness;

    public ShearingValidator(double maxThickness) {
        this.maxThickness = maxThickness;
    }

    @Override
    public boolean validate(Set<Contour> contour, Piece piece) {
        //Проверка на прямолинейность и выпуклость контура. Если и прямолинейный и выпуклый, то вырубка на гильотине
        // возможна
        for (Contour c : contour) {
            if (!(auxillary.ContourSolver.isConvexNGon(c) && piece.getHeight() <= maxThickness)) return false;
        }
        return true;
    }
}