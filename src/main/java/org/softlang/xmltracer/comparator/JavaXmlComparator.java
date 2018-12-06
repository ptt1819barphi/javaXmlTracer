package org.softlang.xmltracer.comparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.softlang.xmltracer.data.ArrayElement;
import org.softlang.xmltracer.data.CollectionElement;
import org.softlang.xmltracer.data.Element;
import org.softlang.xmltracer.data.ListElement;
import org.softlang.xmltracer.data.ObjectElement;
import org.softlang.xmltracer.data.PrimitiveElement;
import org.softlang.xmltracer.data.SetElement;

/**
 * An extension of the Comparator class, specified for comparing an Element from
 * Java with an element from XML. Note: The compare Method always takes the
 * element from Java as its first parameter.
 */
public class JavaXmlComparator extends Comparator {

    public JavaXmlComparator() {
        registerComparator((e1, e2) -> e1.getClass() == SetElement.class && e2.getClass() == CollectionElement.class,
                (e1, e2) -> compareSet((SetElement) e1, new SetElement(new HashSet<>(((CollectionElement) e2).getCollection()))));

        registerComparator((e1, e2) -> e1.getClass() == ListElement.class && e2.getClass() == CollectionElement.class,
                (e1, e2) -> compareList((ListElement) e1, new ListElement(new ArrayList<>(((CollectionElement) e2).getCollection()))));

        registerComparator((e1, e2) -> e1.getClass() == ArrayElement.class && e2.getClass() == CollectionElement.class,
                (e1, e2) -> compareArray((ArrayElement) e1, new ArrayElement(new ArrayList<>(((CollectionElement) e2).getCollection()))));

        registerComparator((e1, e2) -> e1.getClass() == SetElement.class && (e2.getClass() == ObjectElement.class || e2.getClass() == PrimitiveElement.class),
                (e1, e2) -> compareSet((SetElement) e1, new SetElement(Set.of(e2))));

        registerComparator((e1, e2) -> e1.getClass() == ListElement.class && (e2.getClass() == ObjectElement.class || e2.getClass() == PrimitiveElement.class),
                (e1, e2) -> compareList((ListElement) e1, new ListElement(List.of(e2))));

        registerComparator((e1, e2) -> e1.getClass() == ArrayElement.class && (e2.getClass() == ObjectElement.class || e2.getClass() == PrimitiveElement.class),
                (e1, e2) -> compareArray((ArrayElement) e1, new ArrayElement(new Element[]{e2})));
    }

}
