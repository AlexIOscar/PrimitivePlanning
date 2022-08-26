package engine;

import autorouter.core.Piece;
import java.util.ArrayList;
import java.util.List;
import domain.Resource;

public class Dispatcher {
    List<Piece> planningList;
    List<Resource> resourceList;

    public Dispatcher() {
        this.planningList = new ArrayList<>();
        this.resourceList = new ArrayList<>();
    }

    //запланировать деталь
    public void planPiece(Piece piece){

    }
}
