package debug;

import domain.Timeline;
import engine.Dispatcher;

import java.util.Calendar;
import java.util.Date;

public class Starter {
    public static void main(String[] args) {
        Timeline tl = new Timeline();

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.AUGUST, 25, 12, 0, 0);
        Date d = cal.getTime();

        tl.engageTime(d, 3600);
        System.out.println("before");
        System.out.println(tl);

        System.out.println("after 1");
        tl.findTimeAndEngage(new Date(d.getTime() - 1500), 600);
        System.out.println(tl);

        System.out.println("after 2");
        tl.findTimeAndEngage(new Date(d.getTime() - 1500), 600);
        System.out.println(tl);

        System.out.println("размещаем промежуток 5 минут");
        cal.set(2022, Calendar.AUGUST, 25, 10, 35, 0);
        Date d2 = cal.getTime();
        tl.engageTime(d2, 300);
        System.out.println(tl);

        System.out.println("after 3");
        tl.findTimeAndEngage(new Date(d.getTime() - 1500), 660);
        System.out.println(tl);

        System.out.println("after 4");
        tl.findTimeAndEngage(new Date(d.getTime() - 1500), 240);
        System.out.println(tl);

        Dispatcher ds = new Dispatcher();
    }
}
