package basicplanner.engine;

import autorouter.core.Piece;
import basicplanner.domain.Resource;
import elements.Element;

import java.util.Set;

public class LabourDispatcher {

    //Здесь каким-то способом производится получение времени трудоемкости для конкретной комбинации "операции"
    // (элемента и детали) и оборудования. Все это в уже рассчитанном виде хранится в БД, здесь ничего не считается.
    public static double getDuration(Element el, Resource r, Piece p) {
        return 42;
    }

    // В реальных механизмах расчета норма считается сразу на группу элементов, например, сверловка группы отверстий.
    // Группа элементов представлена в детали в виде набора (bundle)
    public static double getDuration(Set<? extends Element> elBundle, Resource r, Piece p) {
        switch (r.name){
            case "MStep MG.6001": return 35;
            case "Voortman V304": return 30;
            case "Shear": return 50;
            default: return 42;
        }
    }
}
