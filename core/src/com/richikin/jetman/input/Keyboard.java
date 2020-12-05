
package com.richikin.jetman.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.input.DirectionMap;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.DirectionValue;
import com.richikin.jetman.physics.Movement;

@SuppressWarnings("WeakerAccess")
public class Keyboard extends InputAdapter
{
    // =================================================================
    // DEFAULT Keyboard options.
    //
    public static final int defaultValueUp       = Input.Keys.W;
    public static final int defaultValueDown     = Input.Keys.S;
    public static final int defaultValueLeft     = Input.Keys.A;
    public static final int defaultValueRight    = Input.Keys.D;
    public static final int defaultValueA        = Input.Keys.NUMPAD_2;
    public static final int defaultValueB        = Input.Keys.NUMPAD_6;
    public static final int defaultValueX        = Input.Keys.NUMPAD_1;
    public static final int defaultValueY        = Input.Keys.NUMPAD_5;
    public static final int defaultValueHudInfo  = Input.Keys.F9;
    public static final int defaultValuePause    = Input.Keys.ESCAPE;
    public static final int defaultValueSettings = Input.Keys.F10;

    public boolean ctrlButtonHeld;
    public boolean shiftButtonHeld;

    public Keyboard()
    {
        ctrlButtonHeld  = false;
        shiftButtonHeld = false;
    }

    public void update()
    {
        if (App.getHud().buttonUp.isPressed())
        {
            App.getPlayer().direction.setY(Movement._DIRECTION_UP);
        }
        else if (App.getHud().buttonDown.isPressed())
        {
            App.getPlayer().direction.setY(Movement._DIRECTION_DOWN);
        }
        else
        {
            App.getPlayer().direction.setY(Movement._DIRECTION_STILL);
        }

        if (App.getHud().buttonLeft.isPressed())
        {
            App.getPlayer().direction.setX(Movement._DIRECTION_LEFT);
        }
        else if (App.getHud().buttonRight.isPressed())
        {
            App.getPlayer().direction.setX(Movement._DIRECTION_RIGHT);
        }
        else
        {
            App.getPlayer().direction.setX(Movement._DIRECTION_STILL);
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        boolean returnFlag = false;

        if (AppConfig.isDesktopApp())
        {
            if (App.settings.isEnabled(Settings._SCROLL_DEMO))
            {
                App.getHud().buttonRight.press();
                returnFlag = true;
            }
            else
            {
                if (AppConfig.gameScreenActive())
                {
                    returnFlag = maingameKeyDown(keycode);
                }
            }
        }

        return returnFlag;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        boolean returnFlag = false;

        if (AppConfig.isDesktopApp())
        {
            if (AppConfig.gameScreenActive())
            {
                returnFlag = maingameKeyUp(keycode);
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyDown(int keycode)
    {
        boolean returnFlag;

        if (keycode == defaultValueLeft)
        {
            App.getHud().buttonLeft.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueRight)
        {
            App.getHud().buttonRight.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueUp)
        {
            App.getHud().buttonUp.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueDown)
        {
            App.getHud().buttonDown.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueB)
        {
            App.getHud().buttonAttack.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueA)
        {
            App.getHud().buttonAction.press();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.K:
                {
                    if (Developer.isDevMode())
                    {
                        App.getPlayer().strength = 0;
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.E:
                {
                    App.gameProgress.getLives().setTotal(1);
                    returnFlag = true;
                }
                break;

                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    App.getHud().buttonPause.press();

                    returnFlag = true;
                }
                break;

                case Input.Keys.STAR:
                {
                    if (Developer.isDevMode())
                    {
                        App.cameraUtils.resetCameraZoom();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                {
                    shiftButtonHeld = true;
                    returnFlag      = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = true;
                    returnFlag     = true;
                }
                break;

                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyUp(int keycode)
    {
        boolean returnFlag;

        if (keycode == defaultValueLeft)
        {
            App.getHud().buttonLeft.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueRight)
        {
            App.getHud().buttonRight.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueUp)
        {
            App.getHud().buttonUp.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueDown)
        {
            App.getHud().buttonDown.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueB)
        {
            App.getHud().buttonAttack.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueA)
        {
            App.getHud().buttonAction.release();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    App.getHud().buttonPause.release();

                    returnFlag = true;
                }
                break;

                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                {
                    shiftButtonHeld = false;
                    returnFlag      = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = false;
                    returnFlag     = true;
                }
                break;

                case Input.Keys.NUM_1:
                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = App.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - App.mapData.mapPosition.getX());
        int screenY = (int) (newPoints.y - App.mapData.mapPosition.getY());

        boolean returnFlag = false;

        if (AppConfig.gameScreenActive())
        {
            returnFlag = App.inputManager.touchScreen.gameScreenTouchDown(screenX, screenY, pointer);
        }
        else
        {
            returnFlag = App.inputManager.touchScreen.titleScreenTouchDown(screenX, screenY);
        }

        return returnFlag;
    }

    @Override
    public boolean touchUp(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = App.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - App.mapData.mapPosition.getX());
        int screenY = (int) (newPoints.y - App.mapData.mapPosition.getY());

        boolean returnFlag = false;

        if (AppConfig.gameScreenActive())
        {
            returnFlag = App.inputManager.touchScreen.gameScreenTouchUp(screenX, screenY);
        }
        else
        {
            returnFlag = App.inputManager.touchScreen.titleScreenTouchUp(screenX, screenY);
        }

        return returnFlag;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
//        Vector2 newPoints = new Vector2(screenX, screenY);
//        newPoints = App.baseRenderer.hudGameCamera.viewport.unproject(newPoints);
//
//        int touchX = (int) (newPoints.x - App.mapData.mapPosition.getX());
//        int touchY = (int) (newPoints.y - App.mapData.mapPosition.getY());
//
//        boolean returnFlag = false;
//
//        if (App.currentScreenID == ScreenID._GAME_SCREEN)
//        {
//            if ((App.getHud().buttonB.pointer == pointer)
//                && !App.getHud().buttonB.contains(touchX, touchY))
//            {
//                App.getHud().buttonB.release();
//                returnFlag = true;
//            }
//        }
//
//        return returnFlag;

        return false;
    }

    /**
     * Process a movement of the mouse pointer.
     * Not called if any mouse button pressed.
     * Not called on iOS builds.
     *
     * @param screenX - new X coordinate.
     * @param screenY - new Y coordinate.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        Vector2 newPoints = new Vector2(screenX, screenY);
        newPoints = App.baseRenderer.hudGameCamera.viewport.unproject(newPoints);

        App.inputManager.mouseWorldPosition.set(newPoints.x, newPoints.y);

        int touchX = (int) (newPoints.x - App.mapData.mapPosition.getX());
        int touchY = (int) (newPoints.y - App.mapData.mapPosition.getY());

        App.inputManager.mousePosition.set(touchX, touchY);

        return false;
    }

    /**
     * React to the mouse wheel scrolling
     *
     * @param amount - scroll amount.
     *               - amount < 0 == scroll down.
     *               - amount > 0 == scroll up.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean scrolled(int amount)
    {
        if (AppConfig.gameScreenActive())
        {
            if (Developer.isDevMode())
            {
                if (ctrlButtonHeld)
                {
                    if (amount < 0)
                    {
                        App.baseRenderer.gameZoom.out(0.10f);
                    }
                    else if (amount > 0)
                    {
                        App.baseRenderer.gameZoom.in(0.10f);
                    }
                }
                if (shiftButtonHeld)
                {
                    if (amount < 0)
                    {
                        App.baseRenderer.hudZoom.out(0.10f);
                    }
                    else if (amount > 0)
                    {
                        App.baseRenderer.hudZoom.in(0.10f);
                    }
                }
            }
        }

        return false;
    }

    public Movement.Dir evaluateKeyboardDirection()
    {
        Direction direction = new Direction
            (
                (int) App.inputManager._horizontalValue,
                (int) App.inputManager._verticalValue
            );

        Movement.Dir keyDir = DirectionMap.map[DirectionMap.map.length - 1].translated;

        for (DirectionValue dv : DirectionMap.map)
        {
            if ((dv.dirX == direction.getX()) && (dv.dirY == direction.getY()))
            {
                keyDir = dv.translated;
            }
        }

        App.inputManager.lastRegisteredDirection = keyDir;

        return keyDir;
    }

    public void translateXPercent()
    {
        App.inputManager._horizontalValue = App.getPlayer().lookingAt.getX();
    }

    public void translateYPercent()
    {
        App.inputManager._verticalValue = App.getPlayer().lookingAt.getY();
    }
}
