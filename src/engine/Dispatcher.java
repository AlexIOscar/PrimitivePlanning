package engine;

import autorouter.core.Piece;

import java.util.*;

import domain.Resource;
import elements.*;

public class Dispatcher {
    List<Piece> planningList;
    List<Resource> resourceList;

    //Карта соответствия между элементом и его приоритетом изготовления. Нужна по причине отсутствия готового блока
    // поиска путей на графе. В качестве упрощения здесь будет использован механизм простого ранжирования по приоритетам
    private static final Map<Class<? extends Element>, Integer> ratedEls;

    static {
        ratedEls = new HashMap<>();
        ratedEls.put(Roll.class, 0);
        ratedEls.put(Contour.class, 1);
        ratedEls.put(TechHole.class, 1);
        ratedEls.put(SharpContour.class, 2);
        ratedEls.put(SharpHole.class, 3);
        ratedEls.put(Butt.class, 1);
        ratedEls.put(Bend.class, 5);
        ratedEls.put(SkewPunch.class, 4);
    }

    public Dispatcher() {
        this.planningList = new ArrayList<>();
        this.resourceList = new ArrayList<>();
    }

    //запланировать деталь
    public void planPiece(Piece piece) {
        Set<Element> elSet = piece.getElementList();
        System.out.println(getRoute(elSet));
    }

    private List<Element> getRoute(Set<Element> inputSet) {
        List<Element> out = new ArrayList<>(inputSet);
        try{
            out.sort(Comparator.comparingInt(o -> ratedEls.get(o.getClass())));
        } catch (NullPointerException npe){
            System.out.println("не найден приоритет элемента");
        }
        return out;
    }
}
