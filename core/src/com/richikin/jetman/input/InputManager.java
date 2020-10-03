
package com.richikin.jetman.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.input.buttons.GDXButton;
import com.richikin.jetman.input.objects.ControllerType;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class InputManager
{
    public Array<GDXButton> gameButtons;
    public Vector2          mousePosition;
    public Vector2          mouseWorldPosition;
    public Keyboard         keyboard;
    public VirtualJoystick  virtualJoystick;
    public TouchScreen      touchScreen;
    public GameController   gameController;
    public InputMultiplexer inputMultiplexer;
    public Movement.Dir     currentRegisteredDirection;
    public Movement.Dir     lastRegisteredDirection;
    public float            _horizontalValue;
    public float            _verticalValue;

    protected App app;

    public InputManager(App _app)
    {
        this.app = _app;

        setup();
    }

    public boolean setup()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(app.stage);

        currentRegisteredDirection = Movement.Dir._STILL;
        lastRegisteredDirection    = Movement.Dir._STILL;

        //
        // Initialise virtual controllers if enabled.
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            Trace.dbg("Initialising _VIRTUAL Controller Type");

            virtualJoystick = new VirtualJoystick(app);
            virtualJoystick.create();
        }

        //
        // Initialise external controllers if enabled.
        if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
        {
            Trace.dbg("Initialising _EXTERNAL Controller Type");

            gameController = new GameController(app);

            if (!gameController.setup())
            {
                gameController = null;
            }
        }

        mousePosition      = new Vector2();
        mouseWorldPosition = new Vector2();
        keyboard           = new Keyboard(app);
        touchScreen        = new TouchScreen(app);
        gameButtons        = new Array<>();

        inputMultiplexer.addProcessor(keyboard);

        UIButtons.setup(app);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

        return true;
    }

    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (virtualJoystick != null)
                {
                    xPercent = virtualJoystick.getXPercent();
                }
            }
            else
            {
                if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
                {
                    xPercent = _horizontalValue;
                }
                else
                {
                    if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
                    {
                        keyboard.translateXPercent();

                        xPercent = app.getPlayer().direction.getX();
                    }
                }
            }
        }

        return xPercent;
    }

    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if (app.getHud() != null)
        {
            if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                if (virtualJoystick != null)
                {
                    yPercent = virtualJoystick.getYPercent();
                }
            }
            else
            {
                if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
                {
                    yPercent = _verticalValue;

                    switch (gameController.controller.getName())
                    {
                        case "PC/PS3/Android":
                        case "Controller (Inno GamePad..)":
                        {
                            yPercent *= -1;
                        }
                        break;

                        default:
                            break;

                    }
                }
                else
                {
                    if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
                    {
                        keyboard.translateYPercent();

                        yPercent = app.getPlayer().direction.getY();
                    }
                }
            }
        }

        return yPercent;
    }
}
