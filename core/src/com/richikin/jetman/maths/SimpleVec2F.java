
package com.richikin.jetman.maths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.jetbrains.annotations.NotNull;

public class SimpleVec2F
{
    public float x;
    public float y;

    public SimpleVec2F()
    {
        this.x = 0;
        this.y = 0;
    }

    public SimpleVec2F(float _x, float _y)
    {
        this.x = _x;
        this.y = _y;
    }

    public void add(float x, float y)
    {
        this.set(this.x + x, this.y + y);
    }

    public void sub(float x, float y, float z)
    {
        this.set(this.x - x, this.y - y);
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public <T> void set(Object _obj, Class<T> clazz)
    {
        if (_obj instanceof SimpleVec2)
        {
            this.x = ((SimpleVec2) _obj).x;
            this.y = ((SimpleVec2) _obj).y;
        }
        else if (_obj instanceof SimpleVec2F)
        {
            this.x = ((SimpleVec2F) _obj).x;
            this.y = ((SimpleVec2F) _obj).y;
        }
        else if (_obj instanceof Vector2)
        {
            this.x = ((Vector2) _obj).x;
            this.y = ((Vector2) _obj).y;
        }
        else if (_obj instanceof Vector3)
        {
            this.x = ((Vector3) _obj).x;
            this.y = ((Vector3) _obj).y;
        }
    }

    public void set(@NotNull SimpleVec2F vec2)
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
