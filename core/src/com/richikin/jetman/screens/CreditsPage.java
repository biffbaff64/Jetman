
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.ui.IUIPage;

import java.util.concurrent.TimeUnit;

public class CreditsPage implements IUIPage, Disposable
{
    private Texture foreground;
    private StopWatch stopWatch;

    public CreditsPage()
    {
        foreground = App.assets.loadSingleAsset("data/credits_foreground.png", Texture.class);

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
        AppSystem.backButton.setVisible(true);
        AppSystem.backButton.setDisabled(false);
        AppSystem.backButton.setChecked(false);

        stopWatch.reset();
    }

    @Override
    public void hide()
    {
        AppSystem.backButton.setVisible(false);
        AppSystem.backButton.setDisabled(true);
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, AppSystem.hudOriginX, AppSystem.hudOriginY);
        }
    }

    @Override
    public void dispose()
    {
        App.assets.unloadAsset("data/credits_foreground.png");

        stopWatch = null;
        foreground = null;
    }
}
