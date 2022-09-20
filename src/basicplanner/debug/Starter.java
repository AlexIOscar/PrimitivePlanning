package basicplanner.debug;

import autorouter.core.Piece;
import basicplanner.domain.Timeline;
import elements.Contour;
import basicplanner.engine.PlannerDispatcher;
import elements.SharpHole;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Starter {

    public static void main(String[] args) {
        test2();
    }

    private static void test1(){
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
    }

    private static void test2(){
        PlannerDispatcher ds = new PlannerDispatcher();
        Piece pc = new Piece(10, 10, 10);
        Set<Contour> contBundle = new HashSet<>();
        Contour c = new Contour();
        contBundle.add(c);

        Set<SharpHole> holes = new HashSet<>();
        SharpHole sh1 = new SharpHole(12);
        holes.add(sh1);

        pc.getBundleSet().add(contBundle);
        pc.getBundleSet().add(holes);

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.AUGUST, 25, 12, 0, 0);
        Date d = cal.getTime();

        for (int i = 0; i <= 10; i++){
            ds.planPiece(pc, d);
        }
        System.out.println(ds.resourceList);
    }
}
