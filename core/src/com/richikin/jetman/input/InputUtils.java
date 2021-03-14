
package com.richikin.jetman.input;

import com.badlogic.gdx.math.Vector2;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.input.controllers.ControllerType;

public class InputUtils
{
    private static final InputUtils instance;

    static
    {
        instance = new InputUtils();
    }

    public static InputUtils inst()
    {
        return instance;
    }

    public float getJoystickAngle()
    {
        return getJoystickVector().angleDeg();
    }

    public Vector2 getJoystickVector()
    {
        float xPerc = App.inputManager.virtualJoystick.getTouchpad().getKnobPercentX();
        float yPerc = App.inputManager.virtualJoystick.getTouchpad().getKnobPercentY();

        Vector2 vector2 = new Vector2(xPerc, yPerc);

        return vector2.rotate90(-1);
    }

    public boolean isInputAvailable(ControllerType inputType)
    {
        return AppConfig.availableInputs.contains(inputType, true);
    }
}
