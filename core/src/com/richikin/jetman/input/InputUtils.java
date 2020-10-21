
package com.richikin.jetman.input;

import com.badlogic.gdx.math.Vector2;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.input.objects.ControllerType;

public class InputUtils
{
    public static float getJoystickAngle(App app)
    {
        return getJoystickVector(app).angle();
    }

    public static Vector2 getJoystickVector(App app)
    {
        float xPerc = app.inputManager.virtualJoystick.getTouchpad().getKnobPercentX();
        float yPerc = app.inputManager.virtualJoystick.getTouchpad().getKnobPercentY();

        Vector2 vector2 = new Vector2(xPerc, yPerc);

        return vector2.rotate90(-1);
    }

    public static boolean isInputAvailable(ControllerType _inputType)
    {
        return AppConfig.availableInputs.contains(_inputType, true);
    }
}
