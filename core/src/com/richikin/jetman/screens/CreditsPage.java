
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.ui.IUIPage;

import java.util.concurrent.TimeUnit;

public class CreditsPage implements IUIPage, Disposable
{
    private Texture foreground;
    private StopWatch stopWatch;
    private App       app;

    CreditsPage(App _app)
    {
        this.app = _app;

        foreground = app.assets.loadSingleAsset("data/credits_foreground.png", Texture.class);

        this.stopWatch = StopWatch.start();
    }

    @Override
    public boolean update()
    {
        if (stopWatch == null)
        {
            return false;
        }

        return (stopWatch.time(TimeUnit.MILLISECONDS) >= 5000);
    }

    @Override
    public void reset()
    {
    }

    @Override
    public void show()
    {
        stopWatch.reset();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float originX, float originY)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, originX, originY);
        }
    }

    @Override
    public void dispose()
    {
        app.assets.unloadAsset("data/credits_foreground.png");

        stopWatch = null;
        foreground = null;
        app = null;
    }
}
