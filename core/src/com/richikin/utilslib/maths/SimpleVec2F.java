
package com.richikin.utilslib.maths;

import org.jetbrains.annotations.NotNull;

public class SimpleVec2F
{
    protected float x;
    protected float y;

    public SimpleVec2F()
    {
        this.x = 0;
        this.y = 0;
    }

    public SimpleVec2F(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public SimpleVec2F(SimpleVec2F vec2)
    {
        this.x = vec2.getX();
        this.y = vec2.getY();
    }

    public void add(float x, float y)
    {
        this.set(this.x + x, this.y + y);
    }

    public void addX(float value)
    {
        this.x += value;
    }

    public void addY(float value)
    {
        this.y += value;
    }

    public void sub(float x, float y, float z)
    {
        this.set(this.x - x, this.y - y);
    }

    public void subX(float value)
    {
        this.x -= value;
    }

    public void subY(float value)
    {
        this.y -= value;
    }

    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(@NotNull SimpleVec2F vec2)
    {
        this.x = vec2.getX();
        this.y = vec2.getY();
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

    public float getX()
    {
        return x;
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(final float y)
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
