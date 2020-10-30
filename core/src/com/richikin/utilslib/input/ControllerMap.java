
package com.richikin.utilslib.input;

import com.badlogic.gdx.controllers.PovDirection;
import com.richikin.utilslib.input.controllers.ControlMapData;

public abstract class ControllerMap
{
    public static float _MIN_RANGE;
    public static float _MAX_RANGE;
    public static float _DEAD_ZONE;

    public static int _BUTTON_X;
    public static int _BUTTON_Y;
    public static int _BUTTON_A;
    public static int _BUTTON_B;

    public static int _BUTTON_BACK;
    public static int _BUTTON_START;

    public static PovDirection _BUTTON_DPAD_UP;
    public static PovDirection _BUTTON_DPAD_DOWN;
    public static PovDirection _BUTTON_DPAD_RIGHT;
    public static PovDirection _BUTTON_DPAD_LEFT;
    public static PovDirection _BUTTON_DPAD_CENTRE;

    public static int _BUTTON_LB;
    public static int _BUTTON_L3;
    public static int _BUTTON_RB;
    public static int _BUTTON_R3;

    public static int _AXIS_LEFT_X;
    public static int _AXIS_LEFT_Y;
    public static int _AXIS_RIGHT_X;
    public static int _AXIS_RIGHT_Y;

    public static int _AXIS_LEFT_TRIGGER;
    public static int _AXIS_RIGHT_TRIGGER;
    public static int _LEFT_TRIGGER;
    public static int _RIGHT_TRIGGER;

    public static void setDataFrom(ControlMapData _mapData)
    {
    }

    public static boolean isInRange(final float value)
    {
        return ((value >= _MIN_RANGE) && (value <= _MAX_RANGE));
    }

    public static boolean isInNegativeRange(final float value)
    {
        return ((value <= -_DEAD_ZONE) && (value >= _MIN_RANGE));
    }

    public static boolean isInPositiveRange(final float value)
    {
        return ((value >= _DEAD_ZONE) && (value <= _MAX_RANGE));
    }
}
