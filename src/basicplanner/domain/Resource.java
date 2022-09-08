package basicplanner.domain;

import elements.Element;
import basicplanner.engine.validator.SourceValidator;
import java.util.ArrayList;
import java.util.List;

public class Resource {
    List<Class<? extends Element>> elements;
    private Timeline timeline;

    public SourceValidator<? extends Element> validator;

    public String name;

    public Resource(String name) {
        elements = new ArrayList<>();
        timeline = new Timeline();
        this.name = name;
    }

    public void setValidator(SourceValidator<? extends Element> validator) {
        this.validator = validator;
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