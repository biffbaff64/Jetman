
package com.richikin.utilslib.maths;

import org.jetbrains.annotations.NotNull;

public class Vec2Boolean
{
    public boolean x;
    public boolean y;

    public Vec2Boolean()
    {
        this.x = false;
        this.y = false;
    }

    public Vec2Boolean(boolean _x, boolean _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void set(boolean _x, boolean _y)
    {
        this.x = _x;
        this.y = _y;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y;
    }
}
