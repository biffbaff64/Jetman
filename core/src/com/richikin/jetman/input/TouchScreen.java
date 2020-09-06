
package com.richikin.jetman.input;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
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

        if (((GameButton) app.getHud().buttonPause).contains(screenX, screenY))
        {
            app.getHud().buttonPause.press();
            returnFlag = true;
        }

        if (AppConfig.gamePaused)
        {
        }
        else
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (((GameButton) app.getHud().buttonA).contains(screenX, screenY))
                {
                    app.getHud().buttonA.press();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonB).contains(screenX, screenY))
                {
                    app.getHud().buttonB.press();
                    returnFlag = true;
                }
            }
        }

        return returnFlag;
    }

    public boolean gameScreenTouchUp(int screenX, int screenY)
    {
        boolean returnFlag = false;

        if (((GameButton) app.getHud().buttonPause).contains(screenX, screenY))
        {
            app.getHud().buttonPause.release();
            returnFlag = true;
        }

        if (AppConfig.gamePaused)
        {
        }
        else
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (((GameButton) app.getHud().buttonB).contains(screenX, screenY))
                {
                    app.getHud().buttonB.release();
                    returnFlag = true;
                }

                if (((GameButton) app.getHud().buttonA).contains(screenX, screenY))
                {
                    app.getHud().buttonA.release();
                    returnFlag = true;
                }
            }
        }

        return returnFlag;
    }
}
