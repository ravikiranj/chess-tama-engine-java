package com.chesstama.bitmath;

public final class BitMath {

    private static final int INT_MAX_LOG_2 = 31;

    private BitMath() {

    }

    public static int log2(int num) {
        if (num == 0) {
            throw new IllegalArgumentException("log 0 is undefined");
        }
        return INT_MAX_LOG_2 - Integer.numberOfLeadingZeros(num);
    }

}
