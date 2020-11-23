
package com.richikin.utilslib.maths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    public SimpleVec2(SimpleVec2 _vec2)
    {
        this.x = _vec2.x;
        this.y = _vec2.y;
    }

    public void add(int _x, int _y)
    {
        this.set(this.x + _x, this.y + _y);
    }

    public void addX(int value)
    {
        this.x += value;
    }

    public void addY(int value)
    {
        this.y += value;
    }

    public void sub(int _x, int _y)
    {
        this.set(this.x - _x, this.y - _y);
    }

    public void subX(int value)
    {
        this.x -= value;
    }

    public void subY(int value)
    {
        this.y -= value;
    }

    //
    // Experimental
    public <T> void set(Object _obj, Class<T> clazz)
    {
        if (_obj instanceof SimpleVec2)
        {
            this.x = ((SimpleVec2) _obj).x;
            this.y = ((SimpleVec2) _obj).y;
        }
        else if (_obj instanceof SimpleVec2F)
        {
            this.x = (int) ((SimpleVec2F) _obj).x;
            this.y = (int) ((SimpleVec2F) _obj).y;
        }
        else if (_obj instanceof Vector2)
        {
            this.x = (int) ((Vector2) _obj).x;
            this.y = (int) ((Vector2) _obj).y;
        }
        else if (_obj instanceof Vector3)
        {
            this.x = (int) ((Vector3) _obj).x;
            this.y = (int) ((Vector3) _obj).y;
        }
    }

    public void set(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void set(float _x, float _y)
    {
        this.x = (int) _x;
        this.y = (int) _y;
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
