package basicplanner.engine.validator;

import autorouter.core.Piece;
import elements.Element;

public interface SourceValidator<T extends Element> {
    boolean validate(T element, Piece piece);
}
