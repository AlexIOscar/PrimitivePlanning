package basicplanner.engine;

import autorouter.core.Piece;
import basicplanner.domain.Resource;
import elements.Element;

public class LabourDispatcher {

    //Здесь каким-то способом производится получение времени трудоемкости для конкретной комбинации "операции"
    // (элемента и детали) и оборудования. Все это в уже рассчитанном виде хранится в БД, здесь ничего не считается.
    public static double getDuration(Element el, Resource r, Piece p){
        return 42;
    }
}
