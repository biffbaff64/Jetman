
package com.richikin.jetman.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.input.controllers.ControllerData;
import com.richikin.utilslib.input.controllers.ControllerMap;
import com.richikin.utilslib.input.controllers.ControllerPos;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.input.controllers.DefaultControllerMap;
import com.richikin.utilslib.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class GameController implements ControllerListener
{
    public Controller controller;

    public GameController()
    {
    }

    public boolean setup()
    {
        AppSystem.controllersFitted = false;
        AppSystem.gameButtonsReady  = false;

        addExternalController();

        if (!AppSystem.controllersFitted)
        {
            if (Controllers.getControllers().size > 0)
            {
                Trace.__FILE_FUNC("Controllers.getControllers().size: " + Controllers.getControllers().size);

                for (Controller _controller : new Array.ArrayIterator<>(Controllers.getControllers()))
                {
                    Trace.dbg(_controller.getName());
                }

                Controllers.getControllers().clear();
            }
            else
            {
                Trace.__FILE_FUNC("No External Controller found");
            }

            Controllers.clearListeners();
        }

        return AppSystem.controllersFitted;
    }

    public void addExternalController()
    {
        try
        {
            if (Controllers.getControllers().size > 0)
            {
                controller = Controllers.getControllers().first();

                if (controller != null)
                {
                    Trace.__FILE_FUNC("Controller [" + controller.getName() + "] found");

                    AppSystem.controllersFitted = true;

                    createControllerMap();

                    Controllers.addListener(this);

                    Trace.dbg("Controller added");
                }
            }
            else
            {
                Trace.dbg("Controllers.getControllers().size: ", Controllers.getControllers().size);
            }
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC("::Failure");

            disableExternalControllers();

            if (AppSystem.isAndroidApp())
            {
                Trace.dbg("::Switched to _VIRTUAL");

                if (!AppSystem.availableInputs.contains(ControllerType._VIRTUAL, true))
                {
                    AppSystem.availableInputs.add(ControllerType._VIRTUAL);
                }
                AppSystem.virtualControllerPos = ControllerPos._LEFT;
            }
            else
            {
                Trace.dbg("::Switched to _KEYBOARD");

                if (!AppSystem.availableInputs.contains(ControllerType._KEYBOARD, true))
                {
                    AppSystem.availableInputs.add(ControllerType._KEYBOARD);
                }
                AppSystem.virtualControllerPos = ControllerPos._HIDDEN;
            }
        }
    }

    public void disableExternalControllers()
    {
        try
        {
            controller = null;

            AppSystem.controllersFitted = false;

            Controllers.removeListener(this);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC("WARNING!: " + npe.getMessage());
        }
    }

    /**
     * A {@link Controller} got connected.
     *
     * @param controller
     */
    @Override
    public void connected(Controller controller)
    {
        AppSystem.controllersFitted = true;
    }

    /**
     * A {@link Controller} got disconnected.
     *
     * @param controller
     */
    @Override
    public void disconnected(Controller controller)
    {
        AppSystem.controllersFitted = false;
    }

    /**
     * A button on the {@link Controller} was pressed.
     * The buttonCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts button constants for known controllers.
     *
     * @param controller
     * @param buttonCode - The {@link ControllerMap} button code.
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        ControllerData.controllerButtonCode = buttonCode;

        if (buttonCode == ControllerMap._BUTTON_A)
        {
            ControllerData.controllerAPressed = true;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonAction != null)
                {
                    App.getHud().buttonAction.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_B)
        {
            ControllerData.controllerBPressed = true;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonAttack != null)
                {
                    App.getHud().buttonAttack.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_X)
        {
            ControllerData.controllerXPressed = true;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonX != null)
                {
                    App.getHud().buttonX.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_Y)
        {
            ControllerData.controllerYPressed = true;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonY != null)
                {
                    App.getHud().buttonY.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_LB)
        {
            ControllerData.controllerLBPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_RB)
        {
            ControllerData.controllerRBPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_START)
        {
            ControllerData.controllerStartPressed = true;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonPause != null)
                {
                    App.getHud().buttonPause.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_BACK)
        {
            ControllerData.controllerBackPressed = true;
        }
        else if (buttonCode == ControllerMap._LEFT_TRIGGER)
        {
            ControllerData.controllerLeftFirePressed = true;
            ControllerData.controllerFirePressed     = true;
        }
        else if (buttonCode == ControllerMap._RIGHT_TRIGGER)
        {
            ControllerData.controllerRightFirePressed = true;
            ControllerData.controllerFirePressed      = true;
        }

        return false;
    }

    /**
     * A button on the {@link Controller} was released.
     * The buttonCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts button constants for known controllers.
     *
     * @param controller
     * @param buttonCode - The {@link ControllerMap} button code.
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        ControllerData.controllerButtonCode = -1;

        if (buttonCode == ControllerMap._BUTTON_A)
        {
            ControllerData.controllerAPressed = false;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonAction != null)
                {
                    App.getHud().buttonAction.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_B)
        {
            ControllerData.controllerBPressed = false;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonAttack != null)
                {
                    App.getHud().buttonAttack.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_X)
        {
            ControllerData.controllerXPressed = false;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonX != null)
                {
                    App.getHud().buttonX.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_Y)
        {
            ControllerData.controllerYPressed = false;

            if (AppSystem.hudExists)
            {
                if (App.getHud().buttonY != null)
                {
                    App.getHud().buttonY.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_LB)
        {
            ControllerData.controllerLBPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_RB)
        {
            ControllerData.controllerRBPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_START)
        {
            ControllerData.controllerStartPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_BACK)
        {
            ControllerData.controllerBackPressed = false;
        }
        else if (buttonCode == ControllerMap._LEFT_TRIGGER)
        {
            ControllerData.controllerLeftFirePressed = false;
            ControllerData.controllerFirePressed     = false;
        }
        else if (buttonCode == ControllerMap._RIGHT_TRIGGER)
        {
            ControllerData.controllerRightFirePressed = false;
            ControllerData.controllerFirePressed      = false;
        }

        return false;
    }

    /**
     * An axis on the {@link Controller} moved.
     * The axisCode is controller specific.
     * The axis value is in the range [-1, 1].
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts axes constants for known controllers.
     *
     * @param controller
     * @param axisCode - The {@link ControllerMap} axis code.
     * @param value the axis value, -1 to 1
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        ControllerData.controllerAxisCode  = axisCode;
        ControllerData.controllerAxisValue = value;

        if (ControllerData.controllerAxisValue > 1.0f)
        {
            ControllerData.controllerAxisValue = 1.0f;
        }

        if (ControllerData.controllerAxisValue < -1.0f)
        {
            ControllerData.controllerAxisValue = -1.0f;
        }

        if ((axisCode == ControllerMap._AXIS_LEFT_X) || (axisCode == ControllerMap._AXIS_RIGHT_X))
        {
            App.inputManager._horizontalValue = value;

            if (ControllerMap.isInNegativeRange(value))
            {
                ControllerData.controllerLeftPressed  = true;
                ControllerData.controllerRightPressed = false;

                if (AppSystem.hudExists)
                {
                    if (App.getHud().buttonLeft != null)
                    {
                        App.getHud().buttonRight.release();
                        App.getHud().buttonLeft.press();
                    }
                }
            }
            else if (ControllerMap.isInPositiveRange(value))
            {
                ControllerData.controllerRightPressed = true;
                ControllerData.controllerLeftPressed  = false;

                if (AppSystem.hudExists)
                {
                    if (App.getHud().buttonRight != null)
                    {
                        App.getHud().buttonLeft.release();
                        App.getHud().buttonRight.press();
                    }
                }
            }
            else
            {
                if (AppSystem.hudExists)
                {
                    if (App.getHud().buttonLeft != null)
                    {
                        App.getHud().buttonLeft.release();
                        App.getHud().buttonRight.release();
                    }
                }

                ControllerData.controllerLeftPressed  = false;
                ControllerData.controllerRightPressed = false;

                ControllerData.controllerAxisCode  = -1;
                ControllerData.controllerAxisValue = 0;
            }
        }
        else if ((axisCode == ControllerMap._AXIS_LEFT_Y) || (axisCode == ControllerMap._AXIS_RIGHT_Y))
        {
            App.inputManager._verticalValue = value;

            if (ControllerMap.isInNegativeRange(value))
            {
                ControllerData.controllerUpPressed = true;

                if (AppSystem.hudExists)
                {
                    if (App.getHud().buttonUp != null)
                    {
                        App.getHud().buttonUp.press();
                    }
                }
            }
            else if (ControllerMap.isInPositiveRange(value))
            {
                ControllerData.controllerDownPressed = true;

                if (AppSystem.hudExists)
                {
                    if (App.getHud().buttonDown != null)
                    {
                        App.getHud().buttonDown.press();
                    }
                }
            }
            else
            {
                if (AppSystem.hudExists)
                {
                    if (App.getHud().buttonUp != null)
                    {
                        App.getHud().buttonUp.release();
                        App.getHud().buttonDown.release();
                    }
                }

                ControllerData.controllerUpPressed   = false;
                ControllerData.controllerDownPressed = false;

                ControllerData.controllerAxisCode  = -1;
                ControllerData.controllerAxisValue = 0;
            }
        }
        else
        {
            ControllerData.controllerUpPressed    = false;
            ControllerData.controllerDownPressed  = false;
            ControllerData.controllerLeftPressed  = false;
            ControllerData.controllerRightPressed = false;

            if (AppSystem.hudExists)
            {
                App.getHud().releaseDirectionButtons();
            }

            ControllerData.controllerAxisCode  = -1;
            ControllerData.controllerAxisValue = 0;
        }

        return false;
    }

    /**
     * A POV on the {@link Controller} moved.
     * The povCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts POV constants for known controllers.
     *
     * @param controller
     * @param povCode
     * @param value
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value)
    {
        ControllerData.controllerPovDirection = value;
        ControllerData.controllerPovCode      = povCode;

        return false;
    }

    /**
     * An x-slider on the {@link Controller} moved.
     * The sliderCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts slider constants for known controllers.
     *
     * @param controller
     * @param sliderCode
     * @param value
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    /**
     * An y-slider on the {@link Controller} moved.
     * The sliderCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts slider constants for known controllers.
     *
     * @param controller
     * @param sliderCode
     * @param value
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value)
    {
        return false;
    }

    /**
     * An accelerometer value on the {@link Controller} changed.
     * The accelerometerCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts slider constants for known controllers. The value is a
     * {@link Vector3} representing the acceleration on a 3-axis
     * accelerometer in m/s^2.
     *
     * @param controller
     * @param accelerometerCode
     * @param value
     *
     * @return whether to hand the event to other listeners.
     */
    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value)
    {
        return false;
    }

    public Controller getController()
    {
        return controller;
    }

    private void createControllerMap()
    {
        Trace.__FILE_FUNC();

        ControllerMap._MIN_RANGE          = DefaultControllerMap._MIN_RANGE;
        ControllerMap._MAX_RANGE          = DefaultControllerMap._MAX_RANGE;
        ControllerMap._DEAD_ZONE          = DefaultControllerMap._DEAD_ZONE;
        ControllerMap._AXIS_LEFT_TRIGGER  = DefaultControllerMap._AXIS_LEFT_TRIGGER;
        ControllerMap._AXIS_RIGHT_TRIGGER = DefaultControllerMap._AXIS_RIGHT_TRIGGER;
        ControllerMap._AXIS_LEFT_X        = DefaultControllerMap._AXIS_LEFT_X;
        ControllerMap._AXIS_LEFT_Y        = DefaultControllerMap._AXIS_LEFT_Y;
        ControllerMap._AXIS_RIGHT_X       = DefaultControllerMap._AXIS_RIGHT_X;
        ControllerMap._AXIS_RIGHT_Y       = DefaultControllerMap._AXIS_RIGHT_Y;
        ControllerMap._BUTTON_A           = DefaultControllerMap._BUTTON_A;
        ControllerMap._BUTTON_B           = DefaultControllerMap._BUTTON_B;
        ControllerMap._BUTTON_X           = DefaultControllerMap._BUTTON_X;
        ControllerMap._BUTTON_Y           = DefaultControllerMap._BUTTON_Y;
        ControllerMap._BUTTON_START       = DefaultControllerMap._BUTTON_START;
        ControllerMap._BUTTON_BACK        = DefaultControllerMap._BUTTON_BACK;
        ControllerMap._BUTTON_L3          = DefaultControllerMap._BUTTON_L3;
        ControllerMap._BUTTON_R3          = DefaultControllerMap._BUTTON_R3;
        ControllerMap._BUTTON_LB          = DefaultControllerMap._BUTTON_LB;
        ControllerMap._BUTTON_RB          = DefaultControllerMap._BUTTON_RB;
        ControllerMap._BUTTON_DPAD_LEFT   = DefaultControllerMap._BUTTON_DPAD_LEFT;
        ControllerMap._BUTTON_DPAD_RIGHT  = DefaultControllerMap._BUTTON_DPAD_RIGHT;
        ControllerMap._BUTTON_DPAD_UP     = DefaultControllerMap._BUTTON_DPAD_UP;
        ControllerMap._BUTTON_DPAD_DOWN   = DefaultControllerMap._BUTTON_DPAD_DOWN;
        ControllerMap._BUTTON_DPAD_CENTRE = DefaultControllerMap._BUTTON_DPAD_CENTRE;
        ControllerMap._LEFT_TRIGGER       = DefaultControllerMap._LEFT_TRIGGER;
        ControllerMap._RIGHT_TRIGGER      = DefaultControllerMap._RIGHT_TRIGGER;

        Trace.finishedMessage();
    }
}
