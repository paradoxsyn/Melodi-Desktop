package com.game.melodi.Maps;

/**
 * Created by Paradox on 5/2/2017.
 */

public class FlyingObjects {
    /** Hit object types (bits). */
    public static final int
            TYPE_CIRCLE   = 1,
            TYPE_TRIANGLE   = 2,
            TYPE_SQUARE = 4,
            TYPE_Poly  = 8;

    /** The x and y offsets for hit object coordinates. */
    private static int
            xOffset,   // offset right of border
            yOffset;   // offset below health bar

    /** The container height. */
    private static int containerHeight;

    /** The offset per stack. */
    private static float stackOffset;

    /**
     * Returns the stack position modifier, in pixels.
     * @return stack position modifier
     */
    public static float getStackOffset() { return stackOffset; }

    /**
     * Sets the stack position modifier.
     * @param offset stack position modifier, in pixels
     */
    public static void setStackOffset(float offset) { stackOffset = offset; }

    /** Starting coordinates. */
    private float x, y;

    /** Start time (in ms). */
    private int time;

    /** Hit object type (TYPE_* bitmask). */
    private int type;

    /** Object coordinate lists. */
    private float[] sliderX, sliderY;

    /** Hit object index in the current stack. */
    private int stack;


}
