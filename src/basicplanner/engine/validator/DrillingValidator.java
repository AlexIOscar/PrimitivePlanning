package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Element;
import elements.SharpHole;

import java.util.Set;

public class DrillingValidator implements SourceValidator<SharpHole> {

    double maxDiam;

    public DrillingValidator(double maxDiam) {
        this.maxDiam = maxDiam;
    }

    @Override
    public boolean validate(Set<SharpHole> element, Piece piece) {
        for (SharpHole sh : element) {
            if (sh.getDiameter() > maxDiam) return false;
        }
        return true;
    }
}
