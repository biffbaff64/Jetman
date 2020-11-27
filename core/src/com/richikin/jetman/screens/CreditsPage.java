
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIPage;

public class CreditsPage implements IUIPage, Disposable
{
    private Texture foreground;
    private StopWatch stopWatch;

    public CreditsPage()
    {
    }

    @Override
    public void initialise()
    {
        foreground = App.assets.loadSingleAsset(GameAssets._CREDITS_PANEL_ASSET, Texture.class);

        this.stopWatch = StopWatch.start();
    }

    @Override
    public boolean update()
    {
        return false;
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
        Trace.__FILE_FUNC();

        App.assets.unloadAsset(GameAssets._CREDITS_PANEL_ASSET);

        stopWatch = null;
        foreground = null;
    }
}
