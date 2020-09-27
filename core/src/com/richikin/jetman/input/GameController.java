
package com.richikin.jetman.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.input.objects.ControllerMap;
import com.richikin.jetman.input.objects.ControllerPos;
import com.richikin.jetman.input.objects.ControllerType;
import com.richikin.jetman.input.controllers.DefaultControllerMap;
import com.richikin.jetman.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class GameController implements ControllerListener
{
    public Controller controller;

    protected App app;

    public GameController(App _app)
    {
        this.app = _app;
    }

    public boolean setup()
    {
        AppConfig.controllersFitted = false;
        AppConfig.gameButtonsReady  = false;

        addExternalController();

        if (!AppConfig.controllersFitted)
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

        return AppConfig.controllersFitted;
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

                    AppConfig.controllersFitted = true;

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

            if (AppConfig.isAndroidApp())
            {
                Trace.dbg("::Switched to _VIRTUAL");

                if (!AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
                {
                    AppConfig.availableInputs.add(ControllerType._VIRTUAL);
                }
                AppConfig.virtualControllerPos = ControllerPos._LEFT;
            }
            else
            {
                Trace.dbg("::Switched to _KEYBOARD");

                if (!AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
                {
                    AppConfig.availableInputs.add(ControllerType._KEYBOARD);
                }
                AppConfig.virtualControllerPos = ControllerPos._HIDDEN;
            }
        }
    }

    public void disableExternalControllers()
    {
        try
        {
            controller = null;

            AppConfig.controllersFitted = false;

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
        AppConfig.controllersFitted = true;
    }

    /**
     * A {@link Controller} got disconnected.
     *
     * @param controller
     */
    @Override
    public void disconnected(Controller controller)
    {
        AppConfig.controllersFitted = false;
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
        UIButtons.controllerButtonCode = buttonCode;

        if (buttonCode == ControllerMap._BUTTON_A)
        {
            UIButtons.controllerAPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonA != null)
                {
                    app.getHud().buttonA.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_B)
        {
            UIButtons.controllerBPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_X)
        {
            UIButtons.controllerXPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonX != null)
                {
                    app.getHud().buttonX.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_Y)
        {
            UIButtons.controllerYPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonY != null)
                {
                    app.getHud().buttonY.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_LB)
        {
            UIButtons.controllerLBPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_RB)
        {
            UIButtons.controllerRBPressed = true;
        }
        else if (buttonCode == ControllerMap._BUTTON_START)
        {
            UIButtons.controllerStartPressed = true;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonPause != null)
                {
                    app.getHud().buttonPause.press();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_BACK)
        {
            UIButtons.controllerBackPressed = true;
        }
        else if (buttonCode == ControllerMap._LEFT_TRIGGER)
        {
            UIButtons.controllerLeftFirePressed = true;
            UIButtons.controllerFirePressed     = true;
        }
        else if (buttonCode == ControllerMap._RIGHT_TRIGGER)
        {
            UIButtons.controllerRightFirePressed = true;
            UIButtons.controllerFirePressed      = true;
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
        UIButtons.controllerButtonCode = -1;

        if (buttonCode == ControllerMap._BUTTON_A)
        {
            UIButtons.controllerAPressed = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonA != null)
                {
                    app.getHud().buttonA.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_B)
        {
            UIButtons.controllerBPressed = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonB != null)
                {
                    app.getHud().buttonB.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_X)
        {
            UIButtons.controllerXPressed = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonX != null)
                {
                    app.getHud().buttonX.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_Y)
        {
            UIButtons.controllerYPressed = false;

            if (AppConfig.hudExists)
            {
                if (app.getHud().buttonY != null)
                {
                    app.getHud().buttonY.release();
                }
            }
        }
        else if (buttonCode == ControllerMap._BUTTON_LB)
        {
            UIButtons.controllerLBPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_RB)
        {
            UIButtons.controllerRBPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_START)
        {
            UIButtons.controllerStartPressed = false;
        }
        else if (buttonCode == ControllerMap._BUTTON_BACK)
        {
            UIButtons.controllerBackPressed = false;
        }
        else if (buttonCode == ControllerMap._LEFT_TRIGGER)
        {
            UIButtons.controllerLeftFirePressed = false;
            UIButtons.controllerFirePressed     = false;
        }
        else if (buttonCode == ControllerMap._RIGHT_TRIGGER)
        {
            UIButtons.controllerRightFirePressed = false;
            UIButtons.controllerFirePressed      = false;
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
        UIButtons.controllerAxisCode  = axisCode;
        UIButtons.controllerAxisValue = value;

        if (UIButtons.controllerAxisValue > 1.0f)
        {
            UIButtons.controllerAxisValue = 1.0f;
        }

        if (UIButtons.controllerAxisValue < -1.0f)
        {
            UIButtons.controllerAxisValue = -1.0f;
        }

        if ((axisCode == ControllerMap._AXIS_LEFT_X) || (axisCode == ControllerMap._AXIS_RIGHT_X))
        {
            app.inputManager._horizontalValue = value;

            if (ControllerMap.isInNegativeRange(value))
            {
                UIButtons.controllerLeftPressed = true;
                UIButtons.controllerRightPressed = false;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonLeft != null)
                    {
                        app.getHud().buttonRight.release();
                        app.getHud().buttonLeft.press();
                    }
                }
            }
            else if (ControllerMap.isInPositiveRange(value))
            {
                UIButtons.controllerRightPressed = true;
                UIButtons.controllerLeftPressed  = false;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonRight != null)
                    {
                        app.getHud().buttonLeft.release();
                        app.getHud().buttonRight.press();
                    }
                }
            }
            else
            {
                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonLeft != null)
                    {
                        app.getHud().buttonLeft.release();
                        app.getHud().buttonRight.release();
                    }
                }

                UIButtons.controllerLeftPressed  = false;
                UIButtons.controllerRightPressed = false;

                UIButtons.controllerAxisCode  = -1;
                UIButtons.controllerAxisValue = 0;
            }
        }
        else if ((axisCode == ControllerMap._AXIS_LEFT_Y) || (axisCode == ControllerMap._AXIS_RIGHT_Y))
        {
            app.inputManager._verticalValue = value;

            if (ControllerMap.isInNegativeRange(value))
            {
                UIButtons.controllerUpPressed = true;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonUp != null)
                    {
                        app.getHud().buttonUp.press();
                    }
                }
            }
            else if (ControllerMap.isInPositiveRange(value))
            {
                UIButtons.controllerDownPressed = true;

                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonDown != null)
                    {
                        app.getHud().buttonDown.press();
                    }
                }
            }
            else
            {
                if (AppConfig.hudExists)
                {
                    if (app.getHud().buttonUp != null)
                    {
                        app.getHud().buttonUp.release();
                        app.getHud().buttonDown.release();
                    }
                }

                UIButtons.controllerUpPressed   = false;
                UIButtons.controllerDownPressed = false;

                UIButtons.controllerAxisCode  = -1;
                UIButtons.controllerAxisValue = 0;
            }
        }
        else
        {
            UIButtons.controllerUpPressed    = false;
            UIButtons.controllerDownPressed  = false;
            UIButtons.controllerLeftPressed  = false;
            UIButtons.controllerRightPressed = false;

            if (AppConfig.hudExists)
            {
                app.getHud().releaseDirectionButtons();
            }

            UIButtons.controllerAxisCode  = -1;
            UIButtons.controllerAxisValue = 0;
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
        UIButtons.controllerPovDirection = value;
        UIButtons.controllerPovCode = povCode;

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
