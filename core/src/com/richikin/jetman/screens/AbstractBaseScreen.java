package com.richikin.jetman.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.graphics.effects.FadeEffect;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.input.IGDXButton;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.screens.IBaseScreen;

public abstract class AbstractBaseScreen extends ScreenAdapter implements IBaseScreen, Disposable
{
    public AbstractBaseScreen()
    {
        super();
    }

    @Override
    public void update()
    {
        if (FadeEffect.isActive)
        {
            if (FadeEffect.update())
            {
                FadeEffect.end();
            }
        }
        else
        {
            if (AppConfig.gameScreenActive())
            {
                App.mapData.update();
                PointsManager.updatePointStacks();
            }

            //
            // Update any buttons that are animating/Scaling etc
            for (IGDXButton button : App.inputManager.gameButtons)
            {
                button.update();
            }
        }
    }

    @Override
    public void triggerFadeIn()
    {
        FadeEffect.triggerFadeIn();
    }

    @Override
    public void triggerFadeOut()
    {
        FadeEffect.triggerFadeOut();
    }

    @Override
    public void resize(int _width, int _height)
    {
        App.baseRenderer.resizeCameras(_width, _height);
    }

    @Override
    public void pause()
    {
        App.settings.getPrefs().flush();
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void show()
    {
        loadImages();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void render(float delta)
    {
        App.baseRenderer.render();
    }

    @Override
    public void loadImages()
    {
    }

    @Override
    public void dispose()
    {
    }
}
