package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.SharpHole;

public class DrillingValidator implements SourceValidator<SharpHole>{

    double maxDiam;

    public DrillingValidator(double maxDiam) {
        this.maxDiam = maxDiam;
    }

    @Override
    public boolean validate(SharpHole element, Piece piece) {
        return element.getDiameter() <= maxDiam;
    }
}
