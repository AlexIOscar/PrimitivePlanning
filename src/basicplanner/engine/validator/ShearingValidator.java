package basicplanner.engine.validator;
import autorouter.core.Piece;
import elements.Contour;

public class ShearingValidator implements SourceValidator<Contour>{

    double maxThickness;

    public ShearingValidator(double maxThickness) {
        this.maxThickness = maxThickness;
    }

    @Override
    public boolean validate(Contour contour, Piece piece) {
        //Проверка на прямолинейность и выпуклость контура. Если и прямолинейный и выпуклый, то вырубка на гильотине
        // возможна
        return auxillary.ContourSolver.isConvexNGon(contour) && piece.getHeight() <= maxThickness;
    }
}
