
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.ui.Scene2DUtils;

public class LoadingScreen implements Disposable
{
    public boolean isAvailable;

    private Image background;

    public LoadingScreen()
    {
        isAvailable = true;

        Texture texture = App.assets.loadSingleAsset(GameAssets._SPLASH_SCREEN_ASSET, Texture.class);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        background = new Image(drawable);
        background.setVisible(true);
        background.setZIndex(99);
    }

    public void update()
    {
        if ((background != null) && isAvailable)
        {
            background.setPosition(AppConfig.hudOriginX, AppConfig.hudOriginY);
        }
    }

    @Override
    public void dispose()
    {
        background.setVisible(false);
        background.addAction(Actions.removeActor());

        App.assets.unloadAsset("data/splash_screen.png");

        background = null;
    }
}
