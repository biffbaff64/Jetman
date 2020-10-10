
package com.richikin.jetman.input;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.input.buttons.GDXButton;
import com.richikin.jetman.input.buttons.GameButton;
import com.richikin.jetman.input.objects.ControllerType;

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
        boolean returnFlag = false;

        if (AppConfig.gamePaused)
        {
        }
        else
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                returnFlag = app.getHud().buttonA.checkPress(screenX, screenY)
                            || app.getHud().buttonB.checkPress(screenX, screenY);
            }
        }

        return returnFlag;
    }

    public boolean gameScreenTouchUp(int screenX, int screenY)
    {
        boolean returnFlag = false;

        if (AppConfig.gamePaused)
        {
        }
        else
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                returnFlag = app.getHud().buttonA.checkRelease(screenX, screenY)
                    || app.getHud().buttonB.checkRelease(screenX, screenY);
            }
        }

        return returnFlag;
    }
}
