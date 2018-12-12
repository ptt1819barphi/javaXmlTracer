package org.softlang.xmltracer.comparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import org.softlang.xmltracer.data.ArrayElement;
import org.softlang.xmltracer.data.CollectionElement;
import org.softlang.xmltracer.data.Element;
import org.softlang.xmltracer.data.ListElement;
import org.softlang.xmltracer.data.ObjectElement;
import org.softlang.xmltracer.data.PrimitiveElement;
import org.softlang.xmltracer.data.SetElement;

public class Comparator {

    private final Set<ComparatorRule> rules = new HashSet<>();

    public Comparator() {
        registerComparatorRule((e1, e2) -> e1.getClass() == ObjectElement.class && e1.getClass() == e2.getClass(),
                (e1, e2) -> compareObject((ObjectElement) e1, (ObjectElement) e2));

        registerComparatorRule((e1, e2) -> e1.getClass() == PrimitiveElement.class && e1.getClass() == e2.getClass(),
                (e1, e2) -> comparePrimitive((PrimitiveElement) e1, (PrimitiveElement) e2));

        registerComparatorRule((e1, e2) -> e1.getClass() == CollectionElement.class && e1.getClass() == e2.getClass(),
                (e1, e2) -> compareCollection((CollectionElement) e1, (CollectionElement) e2));

        registerComparatorRule((e1, e2) -> e1.getClass() == SetElement.class && e1.getClass() == e2.getClass(),
                (e1, e2) -> compareSet((SetElement) e1, (SetElement) e2));

        registerComparatorRule((e1, e2) -> e1.getClass() == ListElement.class && e1.getClass() == e2.getClass(),
                (e1, e2) -> compareList((ListElement) e1, (ListElement) e2));

        registerComparatorRule((e1, e2) -> e1.getClass() == ArrayElement.class && e1.getClass() == e2.getClass(),
                (e1, e2) -> compareArray((ArrayElement) e1, (ArrayElement) e2));

    }

    /**
     * Method for registering Comparator rules.
     *
     * @param check Checks if check can compare the element pair.
     * @param rule Compares the element pair.
     */
    public final void registerComparatorRule(BiPredicate<Element, Element> check, BiPredicate<Element, Element> rule) {
        rules.add(new ComparatorRule(check, rule));
    }

    /**
     * Compare method for base type Element.
     *
     * @param ele1 First element.
     * @param ele2 Second element.
     * @return True if the elements could be matched, else false.
     */
    public boolean compare(Element ele1, Element ele2) {
        for (ComparatorRule rule : rules) {
            if (rule.getCheck().test(ele1, ele2)) {
                return rule.getRule().test(ele1, ele2);
            }
        }

        return false;
    }

    /**
     * Compare method for type PrimitiveElement.
     *
     * @param ele1 First element.
     * @param ele2 Second element.
     * @return True if the elements could be matched, else false.
     */
    public boolean comparePrimitive(PrimitiveElement ele1, PrimitiveElement ele2) {
        return ele1.equals(ele2);
    }

    /**
     * Compare method for type ObjectElement.
     *
     * @param ele1 First element.
     * @param ele2 Second element.
     * @return True if the elements could be matched, else false.
     */
    public boolean compareObject(ObjectElement ele1, ObjectElement ele2) {
        if (ele1.getElementMap().size() != ele2.getElementMap().size()) {
            return false;
        }

        for (Map.Entry<String, Element> entry : ele1.getElementMap().entrySet()) {
            Element otherEle = ele2.getElementMap().get(entry.getKey());

            if (otherEle == null) {
                return false;
            }

            if (!compare(entry.getValue(), otherEle)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Method for comparing a Collection pair.
     *
     * @param ele1 Fist collection.
     * @param ele2 Second collection.
     * @return True if the collections could be matched, else false.
     */
    public boolean compareCollection(CollectionElement ele1, CollectionElement ele2) {
        if (ele1.getCollection().size() != ele2.getCollection().size()) {
            return false;
        }

        List<Element> tempList = new ArrayList<>(ele2.getCollection());

        NEXT:
        for (Element ele : ele1.getCollection()) {
            for (Iterator<Element> iterator = tempList.iterator(); iterator.hasNext();) {
                Element otherEle = iterator.next();
                if (compare(ele, otherEle)) {
                    iterator.remove();
                    continue NEXT;
                }
            }
            return false;
        }

        return true;
    }

    /**
     * Method for comparing a SetElement pair.
     *
     * @param ele1 Fist SetElement.
     * @param ele2 Second SetElement.
     * @return True if the SetElements could be matched, else false.
     */
    public boolean compareSet(SetElement ele1, SetElement ele2) {
        return compareCollection(ele1, ele2);
    }

    public boolean compareList(ListElement ele1, ListElement ele2) {
        if (ele1.getCollection().size() != ele2.getCollection().size()) {
            return false;
        }

        for (int i = 0; i < ele1.getElementList().size(); i++) {
            if (!compare(ele1.getElementList().get(i), ele2.getElementList().get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Method for comparing a ArrayElement pair.
     *
     * @param ele1 Fist ArrayElement.
     * @param ele2 Second ArrayElement.
     * @return True if the ArrayElements could be matched, else false.
     */
    public boolean compareArray(ArrayElement ele1, ArrayElement ele2) {
        return compareList(ele1, ele2);
    }

    /**
     * Intern helper class for defining a datatype for data comparisons.
     */
    private class ComparatorRule {

        private final BiPredicate<Element, Element> check;
        private final BiPredicate<Element, Element> rule;

        public ComparatorRule(BiPredicate<Element, Element> check, BiPredicate<Element, Element> rule) {
            this.check = check;
            this.rule = rule;
        }

        public BiPredicate<Element, Element> getCheck() {
            return check;
        }

        public BiPredicate<Element, Element> getRule() {
            return rule;
        }
    }
}
