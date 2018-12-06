package org.softlang.xmltracer.comparator.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Base {

    private final boolean a = true;
    private final Boolean b = false;

    private final byte c = Byte.MIN_VALUE;
    private final Byte d = Byte.MAX_VALUE;

    private final char e = 'e';
    private final Character f = 'f';

    private final short g = Short.MIN_VALUE;
    private final Short h = Short.MAX_VALUE;

    private final int i = Integer.MIN_VALUE;
    private final Integer j = Integer.MAX_VALUE;

    private final long k = Long.MIN_VALUE;
    private final Long l = Long.MAX_VALUE;

    private final float m = Float.MIN_VALUE;
    private final Float n = Float.MAX_VALUE;

    private final double o = Double.MIN_VALUE;
    private final Double p = Double.MAX_VALUE;

    private final String q = "Test";

    private final SubBase r = new SubBase();

    private final int[] s = new int[]{1, 2, 3, 4};
    private final SubBase[] t = new SubBase[]{new SubBase(0), new SubBase(1)};

    private final List<String> u = Arrays.asList("1", "2", "3", "4", "5");
    private final List<SubBase> v = Arrays.asList(new SubBase(0), new SubBase(1));

    private final Set<Boolean> w = new HashSet<>(Arrays.asList(true, true, false, true, true));
    private final Set<SubBase> x = new HashSet<>(Arrays.asList(new SubBase(0), new SubBase(1)));

    private static class SubBase {

        private final int i;

        public SubBase() {
            this.i = 0;
        }

        public SubBase(int i) {
            this.i = i;
        }

    }
}
