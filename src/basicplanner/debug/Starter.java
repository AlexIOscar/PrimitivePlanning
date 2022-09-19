package basicplanner.debug;

import autorouter.core.Piece;
import basicplanner.domain.Resource;
import basicplanner.domain.Timeline;
import elements.Bend;
import elements.Contour;
import elements.Roll;
import elements.SkewPunch;
import basicplanner.engine.PlannerDispatcher;
import basicplanner.engine.validator.PlasmaValidator;

import java.util.Calendar;
import java.util.Date;

public class Starter {

    public static void main(String[] args) {
        Resource rs = new Resource("ms-mg6001");
        System.out.println(rs.getTimeline());
        rs.setValidator(new PlasmaValidator(40, false));
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
        Piece pc = new Piece();
        pc.addElement(new Roll());
        pc.addElement(new Bend());
        pc.addElement(new Contour());
        pc.addElement(new SkewPunch());
        //ds.planPiece(pc);
    }
}
