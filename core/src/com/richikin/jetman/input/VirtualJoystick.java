
package com.richikin.jetman.input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.physics.Movement;

public class VirtualJoystick
{
    private static final float PAD_X      = 25;
    private static final float PAD_Y      = 25;
    private static final float PAD_WIDTH  = 240;
    private static final float PAD_HEIGHT = 240;

    private       Touchpad touchpad;
    private final App      app;

    public VirtualJoystick(App _app)
    {
        this.app = _app;
    }

    public void create()
    {
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("background", new Texture("data/packedimages/input/touch_background.png"));
        touchpadSkin.add("ball", new Texture("data/packedimages/input/joystick_ball.png"));

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

        Drawable touchBackground = touchpadSkin.getDrawable("background");
        Drawable touchKnob       = touchpadSkin.getDrawable("ball");

        touchpadStyle.background = touchBackground;
        touchpadStyle.knob       = touchKnob;

        touchpad = new Touchpad(1, touchpadStyle);
        touchpad.setBounds(PAD_X, PAD_Y, PAD_WIDTH, PAD_HEIGHT);
        touchpad.setResetOnTouchUp(true);
        app.stage.addActor(touchpad);
    }

    public void update()
    {
        app.getHud().releaseDirectionButtons();

        switch (evaluateJoypadDirection())
        {
            case _UP:
            {
                app.getHud().buttonUp.press();
            }
            break;

            case _DOWN:
            {
                app.getHud().buttonDown.press();
            }
            break;

            case _LEFT:
            {
                app.getHud().buttonLeft.press();
            }
            break;

            case _RIGHT:
            {
                app.getHud().buttonRight.press();
            }
            break;

            case _UP_LEFT:
            {
                app.getHud().buttonUp.press();
                app.getHud().buttonLeft.press();
            }
            break;

            case _UP_RIGHT:
            {
                app.getHud().buttonUp.press();
                app.getHud().buttonRight.press();
            }
            break;

            case _DOWN_LEFT:
            {
                app.getHud().buttonDown.press();
                app.getHud().buttonLeft.press();
            }
            break;

            case _DOWN_RIGHT:
            {
                app.getHud().buttonDown.press();
                app.getHud().buttonRight.press();
            }
            break;

            case _STILL:
            default:
            {
            }
            break;
        }
    }

    public void show()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.show());
        }
    }

    public void hide()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.hide());
        }
    }

    public float getXPercent()
    {
        return touchpad.getKnobPercentX();
    }

    public float getYPercent()
    {
        return touchpad.getKnobPercentY();
    }

    public Touchpad getTouchpad()
    {
        return touchpad;
    }

    private Movement.Dir evaluateJoypadDirection()
    {
        Movement.Dir joyDir;

        //
        // The default angle for joystick goes round anti-clockwise,
        // so modify so that the result is now clockwise.
        int angle = Math.abs((int) (InputUtils.getJoystickAngle(app) - 360));

        joyDir = DirectionMap.map[angle / 10].translated;

        app.inputManager.lastRegisteredDirection = joyDir;

        return joyDir;
    }

    public void removeTouchpad()
    {
        if (touchpad != null)
        {
            touchpad.addAction(Actions.removeActor());
            touchpad = null;
        }
    }
}
