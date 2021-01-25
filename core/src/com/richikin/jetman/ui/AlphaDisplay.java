package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

/**
 * CLass intended to emulate the Alphanumeric
 * displays used on fruit machines etc...
 */
public class AlphaDisplay implements Disposable
{
    public boolean activeMessage;

    private String        message;
    private TextureRegion display;
    private SimpleVec2    position;

    public AlphaDisplay()
    {
    }

    public void initialise(int x, int y)
    {
        display       = App.assets.getObjectRegion("messagebox_background");
        activeMessage = false;
        position      = new SimpleVec2(x, y);
    }

    public void update()
    {
    }

    public void draw()
    {
        if (activeMessage)
        {
            App.spriteBatch.draw(display, position.x, position.y);
        }
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setActive(boolean state)
    {
        activeMessage = state;
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        display = null;
        message = null;
    }
}
