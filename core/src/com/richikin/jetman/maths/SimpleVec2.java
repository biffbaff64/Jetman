
package com.richikin.jetman.maths;

import org.jetbrains.annotations.NotNull;

public class SimpleVec2
{
    public int x;
    public int y;

    public SimpleVec2()
    {
        this.x = 0;
        this.y = 0;
    }

    public SimpleVec2(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void add(int _x, int _y)
    {
        this.set(this.x + _x, this.y + _y);
    }

    public void sub(int _x, int _y)
    {
        this.set(this.x - _x, this.y - _y);
    }

    public void set(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void set(SimpleVec2 vec2)
    {
        this.x = vec2.x;
        this.y = vec2.y;
    }

    public boolean isEmpty()
    {
        return ((x == 0) && (y == 0));
    }

    public void setEmpty()
    {
        this.x = 0;
        this.y = 0;
    }

    public int getX()
    {
        return x;
    }

    public void setX(final int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(final int y)
    {
        this.y = y;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y;
    }
}
