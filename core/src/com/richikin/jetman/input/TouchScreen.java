
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
                for (GDXButton button : app.inputManager.gameButtons)
                {
                    if (button.checkPress(screenX, screenY))
                    {
                        returnFlag = true;
                    }
                }
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
                for (GDXButton button : app.inputManager.gameButtons)
                {
                    if (button.checkRelease(screenX, screenY))
                    {
                        returnFlag = true;
                    }
                }
            }
        }

        return returnFlag;
    }
}
