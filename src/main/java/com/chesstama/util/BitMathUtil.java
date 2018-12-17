package com.chesstama.util;

public final class BitMathUtil {

    private static final int INT_MAX_LOG_2 = 31;

    private BitMathUtil() {
    }

    public static int log2(final int num) {
        if (num == 0) {
            throw new IllegalArgumentException("log 0 is undefined");
        }
        return INT_MAX_LOG_2 - Integer.numberOfLeadingZeros(num);
    }

}
