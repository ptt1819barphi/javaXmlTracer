package org.softlang.xmltracer.data;

import java.util.Collections;
import java.util.List;

public class ListElement extends CollectionElement {

    public ListElement(List<Element> list) {
        super(list);
    }

    public List<Element> getElementList() {
        return Collections.unmodifiableList((List<Element>) collection);
    }
}
