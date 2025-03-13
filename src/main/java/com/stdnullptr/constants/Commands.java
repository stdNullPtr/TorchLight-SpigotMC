package com.stdnullptr.constants;

public final class Commands {
    private Commands() {
        throw new AssertionError("What are you trying to do?.");
    }

    public static final String TORCHLIGHT = "torchlight";

    public static final String TORCHLIGHT_USAGE_HINT = "Usage: /torchlight <on | off | time>";

    public static final String TORCHLIGHT_TIMER_USAGE_HINT = "Usage: /torchlight time <seconds>";
}
