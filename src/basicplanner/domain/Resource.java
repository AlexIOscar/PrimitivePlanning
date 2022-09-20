package basicplanner.domain;

import elements.Element;
import basicplanner.engine.validator.SourceValidator;
import java.util.ArrayList;
import java.util.List;

public class Resource {
    //список элементов, с которыми умеет работать ресурс
    List<Class<? extends Element>> elements;
    //таймлайн ресурса
    private Timeline timeline;

    //валидатор - с помощью него можно уточнить, может ли данный элемент быть выполнен на данном ресурсе
    // (удовлетворяет ли ограничениям)
    public SourceValidator<? extends Element> validator;

    //наименование
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