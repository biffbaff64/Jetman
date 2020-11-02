package com.richikin.jetman.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.utilslib.states.StateID;
import com.richikin.utilslib.states.StateManager;
import com.richikin.jetman.graphics.effects.FadeEffect;
import com.richikin.utilslib.input.IGDXButton;
import com.richikin.utilslib.screens.IBaseScreen;

public abstract class AbstractBaseScreen extends ScreenAdapter implements IBaseScreen, Disposable
{
    protected final App          app;
    protected final StateManager flowState;

    public AbstractBaseScreen(App _app)
    {
        super();

        this.flowState  = new StateManager();
        this.app        = _app;
    }

    @Override
    public void update()
    {
        if (FadeEffect.isActive)
        {
            if (FadeEffect.update())
            {
                FadeEffect.end();
                flowState.set(StateID._STATE_GAME);
            }
        }
        else
        {
            if (AppConfig.gameScreenActive())
            {
                app.mapData.update();
                PointsManager.updatePointStacks();
            }

            //
            // Update any buttons that are animating/Scaling etc
            for (IGDXButton button : app.inputManager.gameButtons)
            {
                button.update();
            }
        }
    }

    @Override
    public void triggerFadeIn()
    {
        FadeEffect.triggerFadeIn();
        flowState.set(StateID._STATE_FADE_IN);
    }

    @Override
    public void triggerFadeOut()
    {
        FadeEffect.triggerFadeOut();
        flowState.set(StateID._STATE_FADE_OUT);
    }

    @Override
    public void resize(int _width, int _height)
    {
        app.baseRenderer.resizeCameras(_width, _height);
    }

    @Override
    public void pause()
    {
        app.settings.prefs.flush();
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void show()
    {
        flowState.set(StateID._STATE_GAME);

        loadImages();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void render(float delta)
    {
        app.baseRenderer.render();
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
