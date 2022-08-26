package domain;

import elements.Element;
import java.util.ArrayList;
import java.util.List;

public class Resource {
    List<Class<? extends Element>> elements;
    private Timeline timeline;

    public String name;

    public Resource(String name) {
        elements = new ArrayList<>();
        this.name = name;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public void addElement(Class<Element> elem){
        elements.add(elem);
    }
}