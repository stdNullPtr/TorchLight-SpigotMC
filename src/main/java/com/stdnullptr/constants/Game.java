package com.stdnullptr.constants;

public final class Game {
    private Game() {
        throw new AssertionError("What are you trying to do?.");
    }

    public static final int MINIMUM_LIGHT_LEVEL = 1;

    public static final int MAXIMUM_LIGHT_LEVEL = 15;

    public static final int TICKS_ONE_SECOND = 20; // 1 second = 20 ticks
}
