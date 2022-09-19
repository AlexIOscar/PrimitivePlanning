package basicplanner.engine;

import autorouter.core.Piece;

import java.util.*;

import basicplanner.domain.Resource;
import basicplanner.domain.Timeline;
import basicplanner.engine.validator.DrillingValidator;
import elements.*;
import basicplanner.engine.validator.PlasmaValidator;
import basicplanner.engine.validator.ShearingValidator;

public class PlannerDispatcher {
    List<Piece> planningList;
    List<Resource> resourceList;

    //Карта соответствия между элементом и его приоритетом изготовления. Нужна по причине отсутствия готового блока
    // поиска путей на графе. В качестве упрощения здесь будет использован механизм простого ранжирования по приоритетам
    private static final Map<Class<? extends Element>, Integer> ratedEls;

    private static final Map<Class<? extends Element>, List<Resource>> opToSourceCache;

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

        opToSourceCache = new HashMap<>();

        Resource mg6001 = new Resource("MStep MG.6001");
        Resource voortman = new Resource("Voortman V304");
        Resource shearMachine = new Resource("Shear");

        mg6001.setValidator(new PlasmaValidator(40, false));
        voortman.setValidator(new PlasmaValidator(40, true));
        shearMachine.setValidator(new ShearingValidator(10));

        List<Resource> contList = new ArrayList<>();
        contList.add(mg6001);
        contList.add(voortman);
        contList.add(shearMachine);

        opToSourceCache.put(Contour.class, contList);

        Resource fgEvo2000 = new Resource("FGEvo2000");
        Resource rdm = new Resource("RDM");
        Resource manualDrill = new Resource("Manual Drill");

        fgEvo2000.setValidator(new DrillingValidator(40));
        rdm.setValidator(new DrillingValidator(40));
        manualDrill.setValidator(new DrillingValidator(10));
    }

    public PlannerDispatcher() {
        this.planningList = new ArrayList<>();
        this.resourceList = new ArrayList<>();
    }

    /**
     * Получить для детали карту <группа элементов, время выполнения>
     *
     * @param piece деталь
     * @return карта bundle - labour time
     */
    private Map<Set<? extends Element>, Map<Resource, Double>> getLabMap(Piece piece) {
        //создаем карту, в которую по объекту "группа"(bundle) будем класть/получать рассчитанное время, чтоб не
        //считать его больше одного раза
        Map<Set<? extends Element>, Map<Resource, Double>> bundlesTimeMap = new HashMap<>();

        for (Set<? extends Element> bundle : piece.getBundleSet()) {
            List<Resource> resources;
            try {
                resources = opToSourceCache.get(bundle.iterator().next().getClass());
            } catch (NoSuchElementException nsee) {
                System.out.println("bundle is empty");
                continue;
            }
            //карта, в которую будем класть вычисленные трудоемкости для разных ресурсов
            Map<Resource, Double> labMapByResource = new HashMap<>();
            for (Resource r : resources) {
                double duration = LabourDispatcher.getDuration(bundle, r, piece);
                labMapByResource.put(r, duration);
            }
            bundlesTimeMap.put(bundle, labMapByResource);
        }
        return bundlesTimeMap;
    }

    public void planPiece(Piece piece, Date notLater) {
        //получаем карту вариантов (by resource) трудоемкостей на группу элементов
        Map<Set<? extends Element>, Map<Resource, Double>> labMap = getLabMap(piece);
        //и список этих групп в порядке исполнения (своего рода маршрут)
        List<Set<? extends Element>> route = getRoute(piece.getBundleSet());
        for (Set<? extends Element> bundle : route) {
            //для данной "операции" получаем карту "станок - трудоемкость"
            Map<Resource, Double> labByResMap = labMap.get(bundle);
            //создаем карту "станок - дата исполнения"
            Map<Resource, Date> dateMap = new HashMap<>();
            for (Resource res : labByResMap.keySet()) {
                Date time = res.getTimeline().findTime(notLater, labByResMap.get(res), new Timeline.IndexWrapper());
                dateMap.put(res, time);
            }
            dateMap.entrySet();
        }
    }

    /**
     * Преобразует набор входящих операций в упорядоченный список операций. Упорядочивание производится в
     * соответствии с назначенными приоритетами (см. ratedEls)
     *
     * @param inputSet input set of elements
     * @return sorted list of elements
     */
    private List<Set<? extends Element>> getRoute(Set<Set<? extends Element>> inputSet) {
        List<Set<? extends Element>> out = new ArrayList<>(inputSet);
        try {
            out.sort(Comparator.comparingInt(o -> ratedEls.get(o.iterator().next().getClass())));
        } catch (NullPointerException npe) {
            System.out.println("не найден приоритет элемента");
        }
        return out;
    }
}
