package basicplanner.engine;

import basicplanner.domain.Resource;
import basicplanner.engine.validator.DrillingValidator;
import basicplanner.engine.validator.PlasmaValidator;
import basicplanner.engine.validator.ShearingValidator;
import elements.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс содержит "контекст сеанса" - разово конструируемые списки приоритетов, оборудования и т.п. В режиме
// разработке здесь все статическое, когда все будет подтягиваться из БД - можно отрефакторить чтоб юзать через инстанс
public class PlannerContext {
    static final Map<Class<? extends Element>, Integer> ratedEls;

    static final Map<Class<? extends Element>, List<Resource>> opToSourceCache;

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

        List<Resource> sHoleList = new ArrayList<>();
        sHoleList.add(fgEvo2000);
        sHoleList.add(rdm);
        sHoleList.add(manualDrill);

        opToSourceCache.put(SharpHole.class, sHoleList);
    }
}
