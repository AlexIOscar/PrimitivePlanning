package basicplanner.domain;

import java.util.*;

//класс, предназначенный для хранения данных доступности ресурса
public class Timeline {

    //через простой список
    private final LinkedList<TimeGap> busyTimes;

    public Timeline() {
        this.busyTimes = new LinkedList<>();
    }

    //Занять время на таймлайне (хардово, без любых проверок). Последующая сортировка плоха для перформанса, зато
    // гарантирует получение упорядоченного листа после вызова
    public void engageTime(Date opEnding, double duration) {
        Date opStart = new Date((long) (opEnding.getTime() - duration * 1000));
        busyTimes.add(new TimeGap(opStart, opEnding));
        Collections.sort(busyTimes);
    }

    //сохранение верной сортировки - на стороне кода-клиента
    public TimeGap engageByIndex(Date opEnding, double duration, int index) {
        Date opStart = new Date((long) (opEnding.getTime() - duration * 1000));
        TimeGap tg = new TimeGap(opStart, opEnding);
        busyTimes.add(index, tg);
        return tg;
    }

    //сохранение верной сортировки - на стороне кода-клиента
    public TimeGap engageLeft(Date opEnding, double duration) {
        Date opStart = new Date((long) (opEnding.getTime() - duration * 1000));
        TimeGap tg = new TimeGap(opStart, opEnding);
        busyTimes.addFirst(tg);
        return tg;
    }

    //найти свободное время на таймлайне и занять его
    public TimeGap findTimeAndEngage(Date notLater, double duration) {
        IndexWrapper iw = new IndexWrapper();
        Date d = findTime(notLater, duration, iw);

        if (iw.index == 0) {
            return engageLeft(d, duration);
        } else {
            return engageByIndex(d, duration, iw.index);
        }
    }

    /**
     * Найти время без его занятия, вернуть точку окончания найденного отрезка времени
     * @param notLater
     * @param duration
     * @param iw
     * @return
     */
    public Date findTime(Date notLater, double duration, IndexWrapper iw){
        int closestLeft = closestIndexLeft(notLater);

        //если точка левее всех отрезков, то просто возвращаем notLater - в нее можно вставить отрезок любой длины
        //iw.index остается нулевым, поскольку занять надо самое левое место
        if (closestLeft == -1) {
            return notLater;
        }

        //получаем отрезок, начало которого ближайшее слева
        TimeGap closest = busyTimes.get(closestLeft);
        //Возможны две ситуации - тестируемая точка лежит внутри отрезка (true), или правее (false). Проверяем:
        //если правее, то чекаем что "места" хватит на новый отрезок, если хватает, то записываем отрезок по индексу
        // closestLeft + 1 и выходим
        if (!checkCollide(notLater, closest)) {
            if (notLater.getTime() - closest.gapEn.getTime() >= duration * 1000) {
                iw.index = closestLeft + 1;
                return notLater;
            }
        }
        //если тестируемая точка лежит внутри closest, то необходимо проверить все пустые места поочередно влево, и
        // по возможности занять ближайшее
        for (int i = closestLeft; i > 0; i--) {
            closest = busyTimes.get(i);
            if (isFitBetween(busyTimes.get(i - 1), closest, duration)) {
                iw.index = i;
                return closest.gapSt;
            }
        }

        //если места так и не нашлось, занимаем место слева вплотную к нулевому отрезку (который станет после этого
        // первым)
        // iw.index остается нулевым, поскольку занять надо самое левое место
        return busyTimes.get(0).gapSt;
    }

    /**
     * класс-обертка для индекса
     */
    public static class IndexWrapper {

        public int getIndex() {
            return index;
        }
        private int index;
        public IndexWrapper() {
            this.index = 0;
        }
    }

    //проверяем уместится ли операция между отрезками
    private boolean isFitBetween(TimeGap tg1, TimeGap tg2, double duration) {
        return tg2.gapSt.getTime() - tg1.gapEn.getTime() >= duration * 1000;
    }

    //Проверяем момент времени на "столкновение" с любым из занятых промежутков. Если занят, то возвращается
    // промежуток, с которым произошла коллизия, если свободен - null.
    private TimeGap checkAnyCollide(Date moment) {
        for (int i = busyTimes.size() - 1; i >= 0; i--) {
            TimeGap gap = busyTimes.get(i);
            if (moment.before(gap.gapEn) && moment.after(gap.gapSt)) {
                return gap;
            }
        }
        return null;
    }

    //true if collide
    private boolean checkCollide(Date moment, TimeGap tg) {
        return moment.after(tg.gapSt) && moment.before(tg.gapEn);
    }

    //выяснить индекс отрезка, стартовая точка которого является ближайшей слева к moment
    private int closestIndexLeft(Date moment) {
        for (int i = busyTimes.size() - 1; i >= 0; i--) {
            if (busyTimes.get(i).gapSt.before(moment)) {
                return i;
            }
        }
        //слева нет отрезков
        return -1;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "busyTimes=" + busyTimes +
                '}';
    }

    //промежуток времени на таймлайне
    static class TimeGap implements Comparable<TimeGap> {
        Date gapSt;
        Date gapEn;

        public TimeGap(Date gapSt, Date gapEn) {
            this.gapSt = gapSt;
            this.gapEn = gapEn;
        }

        @Override
        public String toString() {
            return "TimeGap {" +
                    "gapSt=" + gapSt +
                    ", gapEn=" + gapEn +
                    '}';
        }

        @Override
        //сравнение двух временных отрезков
        public int compareTo(TimeGap o) {
            if (this.gapSt.before(o.gapSt)) {
                return -1;
            } else return 1;
        }
    }
}
