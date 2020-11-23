
package com.richikin.utilslib.maths;

import org.jetbrains.annotations.NotNull;

public class SimpleVec3
{
    public int x;
    public int y;
    public int z;

    public SimpleVec3()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public SimpleVec3(int _x, int _y, int _z)
    {
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    public SimpleVec3(SimpleVec3 _vec2)
    {
        this.x = _vec2.x;
        this.y = _vec2.y;
        this.z = _vec2.z;
    }

    public void add(int x, int y, int z)
    {
        this.set(this.x + x, this.y + y, this.z + z);
    }

    public void addX(int value)
    {
        this.x += value;
    }

    public void addY(int value)
    {
        this.y += value;
    }

    public void sub(int x, int y, int z)
    {
        this.set(this.x - x, this.y - y, this.z - z);
    }

    public void subX(int value)
    {
        this.x -= value;
    }

    public void subY(int value)
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
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public void set(SimpleVec3 vec2)
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

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getZ()
    {
        return z;
    }

    public void setZ(int z)
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
