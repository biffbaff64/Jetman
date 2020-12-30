
package com.richikin.jetman.input;

import com.richikin.jetman.config.AppConfig;

public class TouchScreen
{
    public TouchScreen()
    {
    }

    public boolean titleScreenTouchDown(int screenX, int screenY)
    {
        boolean returnFlag = false;

        if (AppConfig.fullScreenButton.contains(screenX, screenY))
        {
            AppConfig.fullScreenButton.press();
            returnFlag = true;
        }

        return returnFlag;
    }

    public boolean titleScreenTouchUp(int screenX, int screenY)
    {
        boolean returnFlag = false;

        if (AppConfig.fullScreenButton.contains(screenX, screenY))
        {
            AppConfig.fullScreenButton.release();
            returnFlag = true;
        }

        return returnFlag;
    }

    @SuppressWarnings("SameReturnValue")
    public boolean gameScreenTouchDown(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @SuppressWarnings("SameReturnValue")
    public boolean gameScreenTouchUp(int screenX, int screenY)
    {
        return false;
    }
}
