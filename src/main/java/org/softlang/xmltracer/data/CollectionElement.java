package org.softlang.xmltracer.data;

import java.util.Collection;
import java.util.Collections;

public class CollectionElement implements Element {

    protected final Collection<Element> collection;

    public CollectionElement(Collection<Element> collection) {
        this.collection = collection;
    }

    public Collection<Element> getCollection() {
        return Collections.unmodifiableCollection(collection);
    }

    @Override
    public String toString() {
        return collection.toString();
    }
}
