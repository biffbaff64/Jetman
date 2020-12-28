package com.richikin.utilslib.maths;

public class XYSetF extends SimpleVec2F
{
    public XYSetF()
    {
        super(0, 0);
    }

    public XYSetF(float x, float y)
    {
        super(x, y);
    }

    public void addXWrapped(float value, float minimum, float maximum)
    {
        x += value;

        if (x >= maximum)
        {
            x -= (maximum - minimum);
        }
        else if (x <= minimum)
        {
            x += (maximum - minimum);
        }
    }

    public void subXWrapped(float value, float minimum, float maximum)
    {
        if ((x -= value) <= minimum)
        {
            x += (maximum - minimum);
        }
    }

    public void addYWrapped(float value, float minimum, float maximum)
    {
        if ((y += value) >= maximum)
        {
            y -= (maximum - minimum);
        }
    }

    public void subYWrapped(float value, float minimum, float maximum)
    {
        if ((y -= value) <= minimum)
        {
            y += (maximum - minimum);
        }
    }

    @Override
    public void subX(float value)
    {
        if ((x -= value) <= 0)
        {
            x = 0;
        }
    }

    @Override
    public void subY(float value)
    {
        if ((y -= value) <= 0)
        {
            y = 0;
        }
    }

    @Override
    public void addX(float value)
    {
        x += value;
    }

    @Override
    public void addY(float value)
    {
        y += value;
    }

    @Override
    public boolean isEmpty()
    {
        return ((x <= 0) && (y <= 0));
    }
}
