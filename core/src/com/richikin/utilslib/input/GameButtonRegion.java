package com.richikin.utilslib.input;

import com.richikin.utilslib.maths.Box;

public class GameButtonRegion extends Switch
{
    private final Box region;

    public GameButtonRegion(int _x, int _y, int _width, int _height)
    {
        super();

        this.region = new Box(_x, _y, _width, _height);
    }

    public boolean contains(int _x, int _y)
    {
        return (region.contains(_x, _y));
    }
}
