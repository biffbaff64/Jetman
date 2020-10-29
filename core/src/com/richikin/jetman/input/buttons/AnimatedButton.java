
package com.richikin.jetman.input.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.utilslib.input.GDXButton;
import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.maths.Box;
import com.richikin.utilslib.physics.Movement;
import org.jetbrains.annotations.NotNull;

public class AnimatedButton implements GDXButton, Disposable
{
    private static final int _INITIAL_ROTATION  = 270;
    private static final int _INITIAL_DISTANCE  = 45;
    private static final int _ROTATE_SPEED      = 1;
    private static final int _INITIAL_DIRECTION = Movement._DIRECTION_RIGHT;

    public boolean isRotating;
    public boolean isScaling;
    public boolean isToAndFro;
    public boolean isClockwise;
    public boolean hasSound;

    public float scaleMinimum;
    public float scaleMaximum;
    public float scale;
    public float buttonTimer;
    public float alpha;
    public float scaleSpeed;

    public int direction;
    public int rotation;
    public int rotateDir;
    public int rotateDist;
    public int rotateSpeed;

    public TextureRegion bg;
    public TextureRegion bgPressed;
    public TextureRegion bgDisabled;
    public Box           buttonRect;
    public Actions       buttonAction;

    public boolean _isDrawable;
    public boolean _isPressed;
    public boolean _isDisabled;

    public int x;
    public int y;
    public int width;
    public int height;

    private       int mapIndex;
    private final App app;

    /**
     * Define a GameButton
     *
     * @param textureRegion        - Image used for default state
     * @param textureRegionPressed - Image used for PRESSED state
     * @param x                    - X Display co-ordinate
     * @param y                    - Y Display co-ordinate
     * @param _app                 - Instance of the game
     */
    public AnimatedButton(TextureRegion textureRegion, TextureRegion textureRegionPressed, int x, int y, App _app)
    {
        this(x, y, _app);

        this.bg         = textureRegion;
        this.bgPressed  = textureRegionPressed;
        this.width      = textureRegion.getRegionWidth();
        this.height     = textureRegion.getRegionHeight();
        this.alpha       = 1.0f;
        this._isDrawable = true;
        this.buttonRect  = new Box(this.x, this.y, this.width, this.height);
    }

    /**
     * Define a GameButton
     *
     * @param x    - X Display co-ordinate
     * @param y    - Y Display co-ordinate
     * @param _app - Instance of the game
     */
    public AnimatedButton(int x, int y, App _app)
    {
        this(_app);

        this.bg           = null;
        this.bgPressed    = null;
        this.bgDisabled   = null;
        this.x            = x;
        this.y            = y;
        this.width        = 0;
        this.height       = 0;
        this._isDrawable  = false;
        this.isRotating   = false;
        this.rotation     = _INITIAL_ROTATION;
        this.rotateDir    = _INITIAL_DIRECTION;
        this.rotateDist   = _INITIAL_DISTANCE;
        this.rotateSpeed  = 1;
        this.isScaling    = false;
        this.scaleMaximum = 1.3f;
        this.scaleMinimum = 0.7f;
        this.scale        = 0.95f;
        this.direction    = Movement._DIRECTION_STILL;
        this.buttonTimer  = 0;
        this.isToAndFro   = false;

        mapIndex = app.inputManager.gameButtons.size;

        app.inputManager.gameButtons.add(this);
    }

    public AnimatedButton(App _app)
    {
        this.app            = _app;
        this._isPressed     = false;
        this._isDisabled    = false;
        this.hasSound       = true;
        this.buttonAction   = Actions._NO_ACTION;

        this.buttonRect = new Box();
    }

    /**
     * Updates any button animations.
     */
    public void update()
    {
        buttonTimer += Gdx.graphics.getDeltaTime();

        if (buttonTimer >= 0.04f)
        {
            if (isScaling)
            {
                if (direction == Movement._DIRECTION_UP)
                {
                    if ((scale += (scaleSpeed * direction)) > scaleMaximum)
                    {
                        scale = scaleMaximum;
                        direction *= -1;
                    }
                }
                else if (direction == Movement._DIRECTION_DOWN)
                {
                    if ((scale += (scaleSpeed * direction)) < scaleMinimum)
                    {
                        scale = scaleMinimum;
                        direction *= -1;
                    }
                }
                else
                {
                    direction = Movement._DIRECTION_DOWN;
                }
            }

            if (isRotating)
            {
                isClockwise = true;

                rotation = (rotation + rotateSpeed) % 360;
            }
            else if (isToAndFro)
            {
                if (rotateDist <= 0)
                {
                    rotateDist = (_INITIAL_DISTANCE * 2);
                    rotateDir *= -1;
                }
                else
                {
                    rotation = (rotation + (_ROTATE_SPEED * rotateDir)) % 360;
                    rotateDist -= _ROTATE_SPEED;
                }
            }

            buttonTimer = 0;
        }
    }

    @Override
    public boolean checkPress(int touchX, int touchY)
    {
        return false;
    }

    @Override
    public boolean checkRelease(int touchX, int touchY)
    {
        return false;
    }

    public boolean contains(int x, int y)
    {
        return !_isDisabled && buttonRect.contains((float) x, (float) y);
    }

    public boolean contains(float x, float y)
    {
        return !_isDisabled && buttonRect.contains(x, y);
    }

    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera)
    {
        if (_isDrawable)
        {
            TextureRegion textureRegion = _isPressed ? bgPressed : bg;

            if (_isDisabled)
            {
                textureRegion = bgDisabled;
            }

            spriteBatch.draw
                (
                    textureRegion,
                    (camera.position.x + (float) (x - (Gfx._VIEW_WIDTH / 2))),
                    (camera.position.y + (float) (y - (Gfx._VIEW_HEIGHT / 2))),
                    width,
                    height
                );
        }
    }

    public void setTextureRegion(TextureRegion textureRegion)
    {
        this.bg     = textureRegion;
        this.width  = this.bg.getRegionWidth();
        this.height = this.bg.getRegionHeight();

        refreshBounds();
    }

    public void setSize(int _width, int _height)
    {
        this.width  = _width;
        this.height = _height;

        refreshBounds();
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;

        refreshBounds();
    }

    public void refreshBounds()
    {
        buttonRect.set(x, y, width, height);
    }

    public Box getBounds()
    {
        return buttonRect;
    }

    @Override
    public void setDisabled(boolean _disabled)
    {
        _isDisabled = _disabled;
    }

    @Override
    public void setDrawable(boolean _drawable)
    {
        _isDrawable = _drawable;
    }

    @Override
    public void press()
    {
        _isPressed = true;
    }

    @Override
    public void release()
    {
        _isPressed = false;
    }

    @Override
    public boolean isPressed()
    {
        return _isPressed;
    }

    @Override
    public boolean isDisabled()
    {
        return _isDisabled;
    }

    @Override
    public boolean isDrawable()
    {
        return _isDrawable;
    }

    @Override
    public void toggleDisabled()
    {
        _isDisabled = !_isDisabled;
    }

    @Override
    public void togglePressed()
    {
        _isPressed = !_isPressed;
    }

    public void delete()
    {
        app.inputManager.gameButtons.removeIndex(mapIndex);
    }

    @Override
    public void dispose()
    {
        bg         = null;
        bgPressed  = null;
        bgDisabled = null;
    }

    @NotNull
    @Override
    public String toString()
    {
        return "x: " + x
                + ", y: " + y
                + ", w: " + width
                + ", h: " + height
                + ", rotating: " + isRotating
                + ", scaling: " + isScaling
                + ", to and fro: " + isToAndFro;
    }
}
