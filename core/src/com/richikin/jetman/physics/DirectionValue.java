package com.richikin.jetman.physics;

public class DirectionValue
{
    public final int          dirX;
    public final int          dirY;
    public final Movement.Dir translated;

    public DirectionValue(int _x, int _y, Movement.Dir _trans)
    {
        dirX = _x;
        dirY = _y;
        translated = _trans;
    }
}