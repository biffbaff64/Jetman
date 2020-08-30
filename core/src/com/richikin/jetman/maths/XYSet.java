package com.richikin.jetman.maths;

import com.richikin.jetman.maths.SimpleVec2;

public class XYSet extends SimpleVec2
{
    public XYSet()
    {
        super(0, 0);
    }

    public XYSet(int _x, int _y)
    {
        super(_x, _y);
    }

    public void addXWrapped(int value, int minimum, int maximum)
    {
        if ((x += value) >= maximum)
        {
            x -= (maximum - minimum);
        }
    }

    public void subXWrapped(int value, int minimum, int maximum)
    {
        if ((x -= value) <= minimum)
        {
            x += (maximum - minimum);
        }
    }

    public void addYWrapped(int value, int minimum, int maximum)
    {
        if ((y += value) >= maximum)
        {
            y -= (maximum - minimum);
        }
    }

    public void subYWrapped(int value, int minimum, int maximum)
    {
        if ((y -= value) <= minimum)
        {
            y += (maximum - minimum);
        }
    }

    public void subXMinZero(int value)
    {
        if (this.x < value)
        {
            this.x = 0;
        }
        else
        {
            this.x -= value;
        }
    }

    public void subYMinZero(int value)
    {
        if (this.y < value)
        {
            this.y = 0;
        }
        else
        {
            this.y -= value;
        }
    }

    public void subX(int value)
    {
        if ((x -= value) <= 0)
        {
            x = 0;
        }
    }

    public void subY(int value)
    {
        if ((y -= value) <= 0)
        {
            y = 0;
        }
    }

    public void addX(int value)
    {
        x += value;
    }

    public void addY(int value)
    {
        y += value;
    }

    public boolean isEmpty()
    {
        return false;
    }
}
