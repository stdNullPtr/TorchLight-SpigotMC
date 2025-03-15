package com.stdnullptr.constants;

public final class Config {
    private Config() {
        throw new AssertionError("What are you trying to do?.");
    }

    public static final String TORCHLIGHT_LIGHT_TIMER = "light-timer-seconds";

    public static final String TORCHLIGHT_ENABLED = "torchlight-enabled";

    public static final String TORCHLIGHT_LIGHT_LEVEL = "light-level";

    public static final int MINIMUM_LIGHT_TIME_SECONDS = 1;

    public static final int MAXIMUM_LIGHT_TIME_SECONDS = 30;

    public static final boolean DEFAULT_PLUGIN_STATE = true;

    public static final int DEFAULT_LIGHT_TIME_SECONDS = 10;

    public static final int DEFAULT_LIGHT_LEVEL = Game.MAXIMUM_LIGHT_LEVEL;
}
