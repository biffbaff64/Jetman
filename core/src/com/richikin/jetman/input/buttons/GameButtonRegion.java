package com.richikin.jetman.input.buttons;

import com.richikin.jetman.core.App;
import com.richikin.utilslib.maths.Box;

public class GameButtonRegion extends Switch
{
    private final Box region;
    private final App app;

    public GameButtonRegion(int _x, int _y, int _width, int _height, App _app)
    {
        this.app = _app;

        this.region = new Box(_x, _y, _width, _height);
    }

    public boolean contains(int _x, int _y)
    {
        return (region.contains(_x, _y));
    }
}
