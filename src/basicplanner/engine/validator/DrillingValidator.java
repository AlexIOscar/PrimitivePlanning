package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Element;
import elements.SharpHole;

import java.util.Set;

public class DrillingValidator implements SourceValidator {

    double maxDiam;

    public DrillingValidator(double maxDiam) {
        this.maxDiam = maxDiam;
    }

    @Override
    public boolean validate(Set<? extends Element> element, Piece piece) {
        for (Element sh : element) {
            if(!(sh instanceof SharpHole)){
                return false;
            }
            if (((SharpHole)sh).getDiameter() > maxDiam) return false;
        }
        return true;
    }
}
