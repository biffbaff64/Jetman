
package com.richikin.jetman.input;

import com.badlogic.gdx.math.Vector2;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.config.AppSystem;
import com.richikin.utilslib.input.controllers.ControllerType;

public class InputUtils
{
    public static float getJoystickAngle()
    {
        return getJoystickVector().angle();
    }

    public static Vector2 getJoystickVector()
    {
        float xPerc = App.inputManager.virtualJoystick.getTouchpad().getKnobPercentX();
        float yPerc = App.inputManager.virtualJoystick.getTouchpad().getKnobPercentY();

        Vector2 vector2 = new Vector2(xPerc, yPerc);

        return vector2.rotate90(-1);
    }

    public static boolean isInputAvailable(ControllerType _inputType)
    {
        return AppSystem.availableInputs.contains(_inputType, true);
    }
}
