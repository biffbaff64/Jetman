
package com.richikin.jetman.input;

import com.richikin.jetman.core.App;

public class TouchScreen
{
    private final App app;

    public TouchScreen(App _app)
    {
        this.app = _app;
    }

    public boolean titleScreenTouchDown(int screenX, int screenY)
    {
        return false;
    }

    public boolean titleScreenTouchUp(int screenX, int screenY)
    {
        return false;
    }

    public boolean gameScreenTouchDown(int screenX, int screenY, int pointer)
    {
        return false;
    }

    public boolean gameScreenTouchUp(int screenX, int screenY)
    {
        return false;
    }
}
