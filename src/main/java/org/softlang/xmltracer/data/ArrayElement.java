package org.softlang.xmltracer.data;

import java.util.Arrays;
import java.util.List;

public class ArrayElement extends ListElement {

    public ArrayElement(Element[] elements) {
        this(Arrays.asList(elements));
    }

    public ArrayElement(List<Element> list) {
        super(list);
    }

}
