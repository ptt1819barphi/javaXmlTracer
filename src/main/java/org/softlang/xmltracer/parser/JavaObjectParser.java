package org.softlang.xmltracer.parser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.softlang.xmltracer.data.ArrayElement;
import org.softlang.xmltracer.data.Element;
import org.softlang.xmltracer.data.ListElement;
import org.softlang.xmltracer.data.ObjectElement;
import org.softlang.xmltracer.data.PrimitiveElement;
import org.softlang.xmltracer.data.SetElement;

public class JavaObjectParser {

    private final Map<Predicate<Class<? extends Object>>, Function<Object, Element>> map = new HashMap<>();

    /**
     * Default Constructor for the class JavaObjectParser.
     */
    public JavaObjectParser() {
        registerParser(clazz -> clazz.isPrimitive()
                || Number.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || String.class.isAssignableFrom(clazz),
                obj -> new PrimitiveElement(obj.toString()));

        registerParser(clazz -> Set.class.isAssignableFrom(clazz),
                obj -> getSetElement((Set) obj));

        registerParser(clazz -> List.class.isAssignableFrom(clazz),
                obj -> getListElement((List) obj));

        registerParser(clazz -> clazz.isArray(),
                obj -> getArrayElement(obj));
    }

    /**
     * Method for registering the mapping between predicates and functions.
     *
     * @param predicate The predicate for the given function. Checks if the
     * funtion can be used with the conditions specified in the predicate.
     * @param function The function for creating an Element type from a
     * specified Java Object.
     */
    public final void registerParser(Predicate<Class<? extends Object>> predicate, Function<Object, Element> function) {
        map.put(predicate, function);
    }

    /**
     * Helper method for calling registered parsers under checked conditions.
     *
     * @param obj The Object to be parsed.
     * @return Returns the Element resulting from the given Object.
     * @throws IllegalArgumentException If the specified object is not an
     * instance of the class or interface declaring the underlying field.
     * @throws IllegalAccessException If a Field of the object is enforcing Java
     * language access control, and the field is inaccessible.
     */
    private Element getElement(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Class<? extends Object> clazz = obj.getClass();

        for (Map.Entry<Predicate<Class<? extends Object>>, Function<Object, Element>> entry : map.entrySet()) {
            if (entry.getKey().test(clazz)) {
                return entry.getValue().apply(obj);
            }
        }

        return getObjectElement(obj);
    }

    /**
     * Method for parsing a Java Object to an ObjectElement.
     *
     * @param object The Java Object to be parsed.
     * @return The parsed ObjectElement.
     * @throws IllegalArgumentException If the specified object is not an
     * instance of the class or interface declaring the underlying field.
     * @throws IllegalAccessException If a Field of the object is enforcing Java
     * language access control, and the field is inaccessible.
     */
    public ObjectElement getObjectElement(Object object) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Element> map = new HashMap<>();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj = field.get(object);

            if (obj == null || Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) || field.isSynthetic()) {
                continue;
            }

            Element element = getElement(obj);

            if (element != null) {
                map.put(field.getName(), element);
            }
        }
        return new ObjectElement(map);
    }

    /**
     * Method for parsing a Java Array to an ArrayElement.
     *
     * @param object The Java Array to be parsed.
     * @return The parsed ArrayElement.
     * @throws IllegalArgumentException If the specified object is not an
     * instance of the class or interface declaring the underlying field.
     * @throws IllegalAccessException If a Field of the object is enforcing Java
     * language access control, and the field is inaccessible.
     */
    private ArrayElement getArrayElement(Object object) throws IllegalArgumentException, IllegalAccessException {
        if (Array.getLength(object) == 0) {
            return null;
        }

        Element[] elements = new Element[Array.getLength(object)];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = getElement(Array.get(object, i));
        }
        return new ArrayElement(elements);
    }

    /**
     * Method for parsing a Java List to a ListElement.
     *
     * @param object The Java List to be parsed.
     * @return The parsed ListElement.
     * @throws IllegalArgumentException If the specified object is not an
     * instance of the class or interface declaring the underlying field.
     * @throws IllegalAccessException If a Field of the object is enforcing Java
     * language access control, and the field is inaccessible.
     */
    private ListElement getListElement(List list) throws IllegalArgumentException, IllegalAccessException {
        if (list.isEmpty()) {
            return null;
        }
        List<Element> elements = new ArrayList<>();
        for (Object object : list) {
            elements.add(getElement(object));
        }
        return new ListElement(elements);
    }

    /**
     * Method for parsing a Java Set to a SetElement.
     *
     * @param object The Java Set to be parsed.
     * @return The parsed SetElement.
     * @throws IllegalArgumentException If the specified object is not an
     * instance of the class or interface declaring the underlying field.
     * @throws IllegalAccessException If a Field of the object is enforcing Java
     * language access control, and the field is inaccessible.
     */
    private SetElement getSetElement(Set set) throws IllegalArgumentException, IllegalAccessException {
        if (set.isEmpty()) {
            return null;
        }

        Set<Element> elements = new HashSet<>();
        for (Object object : set) {
            elements.add(getElement(object));
        }
        return new SetElement(elements);
    }

    @FunctionalInterface
    public static interface Function<T, R> {

        R apply(T t) throws IllegalArgumentException, IllegalAccessException;
    }
}
