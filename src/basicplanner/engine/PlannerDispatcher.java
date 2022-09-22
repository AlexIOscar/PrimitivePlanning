package basicplanner.engine;

import autorouter.core.Piece;

import java.util.*;

import basicplanner.domain.Resource;
import basicplanner.domain.Timeline;
import elements.*;

import static basicplanner.engine.PlannerContext.opToSourceCache;
import static basicplanner.engine.PlannerContext.ratedEls;

public class PlannerDispatcher {
    List<Piece> planningList;
    public List<Resource> resourceList;

    //Карта соответствия между элементом и его приоритетом изготовления. Нужна по причине отсутствия готового блока
    // поиска путей на графе. В качестве упрощения здесь будет использован механизм простого ранжирования по приоритетам
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
                System.out.println("bundles is empty");
                continue;
            }
            //карта, в которую будем класть вычисленные трудоемкости для разных ресурсов
            Map<Resource, Double> labMapByResource = new HashMap<>();
            for (Resource r : resources) {
                //надо проверить исполним ли элемент на этой единице

                if (!r.validator.validate(bundle, piece)) {
                    continue;
                }

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
            //создаем карту "станок - дата исполнения с индексом"
            Map<Resource, DateIndexPare> dateMap2 = new HashMap<>();
            for (Resource res : labByResMap.keySet()) {
                Timeline.IndexWrapper iw = new Timeline.IndexWrapper();
                Date time = res.getTimeline().findTime(notLater, labByResMap.get(res), iw);
                dateMap2.put(res, new DateIndexPare(time, iw.getIndex()));
            }
            //получаем список энтри-сетов, и упорядочиваем его, чтобы найти наиболее !позднее время
            List<Map.Entry<Resource, DateIndexPare>> entryList = new ArrayList<>(dateMap2.entrySet());
            entryList.sort((o1, o2) -> (int) (o2.getValue().d.getTime() - o1.getValue().d.getTime()));

            //целевая запись: в ней та единица оборудования, на которой произвести операцию можно к наиболее близкому
            // к notLater моменту (по направлению в прошлое)
            Map.Entry<Resource, DateIndexPare> latestEntry = entryList.get(0);
            Date d = latestEntry.getValue().d;
            double dur = labMap.get(bundle).get(latestEntry.getKey());
            //занимаем время на таймлайне ресурса
            Timeline.TimeGap tg =
                    latestEntry.getKey().getTimeline().engageByIndex(d, dur, latestEntry.getValue().index);
            //перед следующим проходом передвигаем notLater на начало промежутка, занятого в данном цикле
            notLater = tg.getGapSt();
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

    private static class DateIndexPare {
        Date d;
        int index;

        public DateIndexPare(Date d, int index) {
            this.d = d;
            this.index = index;
        }
    }
}
