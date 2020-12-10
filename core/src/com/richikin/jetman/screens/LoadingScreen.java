
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

        background = Scene2DUtils.createImage(new TextureRegion(texture));
        background.setVisible(true);
        background.setZIndex(99);
    }

    public void update()
    {
    }

    public void render()
    {
        if (background != null)
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
