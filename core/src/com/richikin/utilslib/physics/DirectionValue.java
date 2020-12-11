package com.richikin.utilslib.physics;

import com.richikin.utilslib.physics.Dir;

public class DirectionValue
{
    public final int dirX;
    public final int dirY;
    public final Dir translated;

    public DirectionValue(int x, int y, Dir trans)
    {
        dirX = x;
        dirY = y;
        translated = trans;
    }
}
