
package com.richikin.jetman.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.input.IGDXButton;
import com.richikin.utilslib.input.controllers.ControllerData;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.logging.Trace;
import com.richikin.jetman.physics.Movement;

@SuppressWarnings("WeakerAccess")
public class InputManager
{
    public Array<IGDXButton> gameButtons;
    public Vector2           mousePosition;
    public Vector2           mouseWorldPosition;
    public Keyboard          keyboard;
    public VirtualJoystick   virtualJoystick;
    public TouchScreen       touchScreen;
    public GameController    gameController;
    public InputMultiplexer  inputMultiplexer;
    public Movement.Dir      currentRegisteredDirection;
    public Movement.Dir      lastRegisteredDirection;
    public float             _horizontalValue;
    public float             _verticalValue;

    public InputManager()
    {
    }

    public boolean setup()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(App.stage);

        currentRegisteredDirection = Movement.Dir._STILL;
        lastRegisteredDirection    = Movement.Dir._STILL;

        //
        // Initialise virtual controllers if enabled.
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            Trace.dbg("Initialising _VIRTUAL Controller Type");

            virtualJoystick = new VirtualJoystick();
            virtualJoystick.create();
        }

        //
        // Initialise external controllers if enabled.
        if (AppConfig.availableInputs.contains(ControllerType._EXTERNAL, true))
        {
            Trace.dbg("Initialising _EXTERNAL Controller Type");

            gameController = new GameController();

            if (!gameController.setup())
            {
                gameController = null;
            }
        }

        mousePosition      = new Vector2();
        mouseWorldPosition = new Vector2();
        keyboard           = new Keyboard();
        touchScreen        = new TouchScreen();
        gameButtons        = new Array<>();

        inputMultiplexer.addProcessor(keyboard);

        ControllerData.setup();

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

        return true;
    }

    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if (App.getHud() != null)
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

                        xPercent = App.getPlayer().direction.getX();
                    }
                }
            }
        }

        return xPercent;
    }

    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if (App.getHud() != null)
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

                        yPercent = App.getPlayer().direction.getY();
                    }
                }
            }
        }

        return yPercent;
    }
}
