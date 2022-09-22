package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Element;

import java.util.Set;

public interface SourceValidator<T extends Element> {
    boolean validate (Set<? extends T> element, Piece piece);
}
