
package com.richikin.utilslib.input.controllers;

import com.richikin.utilslib.physics.Movement;

public enum ControllerPos
{
    _LEFT("LEFT", Movement._DIRECTION_LEFT),
    _RIGHT("RIGHT", Movement._DIRECTION_RIGHT),
    _HIDDEN("HIDDEN", Movement._DIRECTION_STILL);

    final int    value;
    final String text;

    ControllerPos(String _text, int _value)
    {
        text = _text;
        value = _value;
    }

    public String getText()
    {
        return text;
    }

    public int getValue()
    {
        return value;
    }
}
