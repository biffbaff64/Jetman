
package com.richikin.jetman.input.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.maths.Box;
import com.richikin.jetman.utils.logging.Trace;
import org.jetbrains.annotations.NotNull;

public class GameButton implements GDXButton, Disposable
{
    public TextureRegion bg;
    public TextureRegion bgPressed;
    public TextureRegion bgDisabled;
    public Box           buttonRect;
    public Actions       buttonAction;

    public int x;
    public int y;
    public int width;
    public int height;

    private boolean _isDrawable;
    private boolean _isPressed;
    private boolean _isDisabled;

    private final ShapeRenderer sr;
    private final int           mapIndex;
    private final App           app;

    /**
     * Define a GameButton
     *
     * @param textureRegion        - Image used for default state
     * @param textureRegionPressed - Image used for PRESSED state
     * @param x                    - X Display co-ordinate
     * @param y                    - Y Display co-ordinate
     * @param _app                 - Instance of the game
     */
    public GameButton(TextureRegion textureRegion, TextureRegion textureRegionPressed, int x, int y, App _app)
    {
        this(x, y, _app);

        this.bg          = textureRegion;
        this.bgPressed   = textureRegionPressed;
        this.width       = textureRegion.getRegionWidth();
        this.height      = textureRegion.getRegionHeight();
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
    public GameButton(int x, int y, App _app)
    {
        this(_app);

        this.bg          = null;
        this.bgPressed   = null;
        this.bgDisabled  = null;
        this.x           = x;
        this.y           = y;
        this.width       = 0;
        this.height      = 0;
        this._isDrawable = false;
    }

    public GameButton(App _app)
    {
        this.app          = _app;
        this._isPressed   = false;
        this._isDisabled  = false;
        this.buttonAction = Actions._NO_ACTION;
        this.buttonRect   = new Box();
        this.sr           = new ShapeRenderer();

        mapIndex = app.inputManager.gameButtons.size;

        app.inputManager.gameButtons.add(this);
    }

    @Override
    public void update()
    {
    }

    @Override
    public boolean checkPress(int touchX, int touchY)
    {
        boolean returnFlag = false;

        if (!_isDisabled)
        {
            if (contains(touchX, touchY))
            {
                press();
                returnFlag = true;
            }
        }

        return returnFlag;
    }

    @Override
    public boolean checkRelease(int touchX, int touchY)
    {
        boolean returnFlag = false;

        if (!_isDisabled)
        {
            if (contains(touchX, touchY))
            {
                release();
                returnFlag = true;
            }
        }

        return returnFlag;
    }

    public boolean contains(int x, int y)
    {
        return !_isDisabled && buttonRect.contains((float) x, (float) y);
    }

    public boolean contains(float x, float y)
    {
        return !_isDisabled && buttonRect.contains(x, y);
    }

    public void draw()
    {
        if (_isDrawable)
        {
            TextureRegion textureRegion = _isPressed ? bgPressed : bg;

            if (_isDisabled)
            {
                textureRegion = bgDisabled;
            }

            app.spriteBatch.draw
                (
                    textureRegion,
                    (app.baseRenderer.hudGameCamera.getPosition().x + (float) (x - (Gfx._HUD_WIDTH / 2))),
                    (app.baseRenderer.hudGameCamera.getPosition().y + (float) (y - (Gfx._HUD_HEIGHT / 2))),
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
    public void setVisible(boolean _drawable)
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
    public boolean isVisible()
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
            + ", h: " + height;
    }
}
