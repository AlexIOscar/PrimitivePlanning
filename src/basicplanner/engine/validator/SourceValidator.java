package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Element;

import java.util.Set;

public interface SourceValidator {
    boolean validate (Set<? extends Element> element, Piece piece);
}
