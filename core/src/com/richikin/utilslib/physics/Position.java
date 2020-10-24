
package com.richikin.utilslib.physics;

public class Position
{
    public final float x;
    public final float y;
    public final int direction;

    public Position(float _x, float _y)
    {
        this(_x, _y, Movement._DIRECTION_STILL);
    }

    public Position(float _x, float _y, int _dir)
    {
        this.x = _x;
        this.y = _y;
        this.direction = _dir;
    }
}
