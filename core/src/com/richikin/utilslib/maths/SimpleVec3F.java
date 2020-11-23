
package com.richikin.utilslib.maths;

import org.jetbrains.annotations.NotNull;

public class SimpleVec3F
{
    public float x;
    public float y;
    public float z;

    public SimpleVec3F()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public SimpleVec3F(float _x, float _y, float _z)
    {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }
    public SimpleVec3F(SimpleVec3F _vec2)
    {
        this.x = _vec2.x;
        this.y = _vec2.y;
        this.z = _vec2.z;
    }

    public void add(float x, float y, float z)
    {
        this.set(this.x + x, this.y + y, this.z + z);
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
        this.set(this.x - x, this.y - y, this.z - z);
    }

    public void subX(float value)
    {
        this.x -= value;
    }

    public void subY(float value)
    {
        this.y -= value;
    }

    public void set(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(SimpleVec3F vec2)
    {
        this.x = vec2.x;
        this.y = vec2.y;
        this.z = vec2.z;
    }

    public boolean isEmpty()
    {
        return ((x == 0) && (y == 0) && (z == 0));
    }

    public void setEmpty()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
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

    public float getZ()
    {
        return z;
    }

    public void setZ(final float z)
    {
        this.z = z;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y + ", z: " + z;
    }
}
