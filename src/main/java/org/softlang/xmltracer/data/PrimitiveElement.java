package org.softlang.xmltracer.data;

import java.util.Objects;

public class PrimitiveElement implements Element {

    private final String value;

    public PrimitiveElement(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value is not allowed.");
        }

        this.value = value;
    }

    public PrimitiveElement(Boolean value) {
        this(value.toString());
    }

    public PrimitiveElement(Character value) {
        this(value.toString());
    }

    public PrimitiveElement(Number value) {
        this(value.toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final PrimitiveElement other = (PrimitiveElement) obj;

        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

}
