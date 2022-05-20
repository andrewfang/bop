package com.andrewbfang.bop;

import java.util.Random;

/**
 * Created by Andrew
 */
public enum Gestures {

    SWIPE_UP("Swipe Up", false),
    SWIPE_DOWN("Swipe Down", false),
    SWIPE_LEFT("Swipe Left", false),
    SWIPE_RIGHT("Swipe Right", false),
    TAP("Tap", false),
    DTAP("Double Tap", false),
    LONGPRESS("Long Press", false),
    UP_DOWN("Shake Up and Down", true),
    SIDEWAYS("Shake Side to Side", true);

    private final String name;
    private final boolean hasWatch;

    Gestures(String gestureName, boolean hasWatch) {
        this.name = gestureName;
        this.hasWatch = hasWatch;
    }

    private static final Random RANDOM = new Random();
    private static final Gestures[] VALUES = Gestures.values();
    private static final int SIZE = VALUES.length;

    public static Gestures getRandom() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }

    public boolean needsWatch() {
        return this.hasWatch;
    }

    @Override
    public String toString() {
        return this.name + "!";
    }
}
