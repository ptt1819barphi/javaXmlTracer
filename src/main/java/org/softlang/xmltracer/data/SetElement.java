package org.softlang.xmltracer.data;

import java.util.Collections;
import java.util.Set;

public class SetElement extends CollectionElement {

    public SetElement(Set<Element> set) {
        super(set);
    }

    public Set<Element> getElementSet() {
        return Collections.unmodifiableSet((Set<Element>) collection);
    }
}
