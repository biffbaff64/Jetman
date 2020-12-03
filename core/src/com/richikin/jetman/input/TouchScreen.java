
package com.richikin.jetman.input;

import com.richikin.jetman.config.AppConfig;
import com.richikin.utilslib.input.controllers.ControllerType;

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

    public boolean gameScreenTouchDown(int screenX, int screenY, int pointer)
    {
        boolean returnFlag = false;

        if (!AppConfig.gamePaused)
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
//                if (((GameButton) App.getHud().buttonA).contains(screenX, screenY))
//                {
//                    App.getHud().buttonA.press();
//                    returnFlag = true;
//                }

//                if (((GameButton) App.getHud().buttonB).contains(screenX, screenY))
//                {
//                    App.getHud().buttonB.press();
//                    returnFlag = true;
//                }
            }
        }

        return returnFlag;
    }

    public boolean gameScreenTouchUp(int screenX, int screenY)
    {
        boolean returnFlag = false;

        if (!AppConfig.gamePaused)
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
//                if (((GameButton) App.getHud().buttonA).contains(screenX, screenY))
//                {
//                    App.getHud().buttonA.release();
//                    returnFlag = true;
//                }

//                if (((GameButton) App.getHud().buttonB).contains(screenX, screenY))
//                {
//                    App.getHud().buttonB.release();
//                    returnFlag = true;
//                }
            }
        }

        return returnFlag;
    }
}
