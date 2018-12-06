package org.softlang.xmltracer.data;

import java.util.Collections;
import java.util.Map;

public class ObjectElement implements Element {

    private final Map<String, Element> map;

    public ObjectElement(Map<String, Element> map) {
        this.map = map;
    }

    public Map<String, Element> getElementMap() {
        return Collections.unmodifiableMap(map);
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
