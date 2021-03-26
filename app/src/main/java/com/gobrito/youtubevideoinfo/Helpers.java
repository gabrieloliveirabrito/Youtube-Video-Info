package com.gobrito.youtubevideoinfo;

import java.math.BigInteger;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Helpers {
    private static final String NAMES[] = new String[]{
            "mil",
            "mi",
            "bi",
            "tri"
    };
    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final NavigableMap<BigInteger, String> MAP;

    static {
        MAP = new TreeMap<>();
        for (int i = 0; i < NAMES.length; i++) {
            MAP.put(THOUSAND.pow(i + 1), NAMES[i]);
        }
    }

    public static String createString(BigInteger number) {
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null) {
            return number.toString();
        }
        BigInteger key = entry.getKey();
        BigInteger d = key.divide(THOUSAND);
        BigInteger m = number.divide(d);
        float f = m.floatValue() / 1000.0f;
        float rounded = ((int) (f * 100.0)) / 100.0f;
        if (rounded % 1 == 0) {
            return ((int) rounded) + " " + entry.getValue();
        }
        return rounded + " " + entry.getValue();
    }
}
