package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.enumslib.ScreenID;
import com.richikin.utilslib.states.StateID;
import com.richikin.utilslib.states.StateManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;

public class MainMenuScreen extends AbstractBaseScreen
{
    private Texture background;

    public MainMenuScreen(App _app)
    {
        super(_app);
    }

    @Override
    public void initialise()
    {
        app.mapData.mapPosition.set(0, 0);
    }

    @Override
    public void show()
    {
        super.show();

        AppConfig.currentScreenID = ScreenID._MAIN_MENU;

        app.appState.set(StateID._STATE_MAIN_MENU);
        app.cameraUtils.resetCameraZoom();
        app.cameraUtils.disableAllCameras();
        app.baseRenderer.hudGameCamera.isInUse = true;

        initialise();

        app.baseRenderer.isDrawingStage = true;
    }

    @Override
    public void hide()
    {
        app.assets.unloadAsset(GameAssets._TEMPLATE_BACKGROUND_ASSET);
    }

    @Override
    public void render(float delta)
    {
        super.update();

        //
        // Check that the appState is still valid before continuing.
        if (app.appState.peek() == StateID._STATE_MAIN_MENU)
        {
            StateID tempState;

            if ((tempState = update(app.appState).peek()) != StateID._STATE_MAIN_MENU)
            {
                app.appState.set(tempState);
            }

            super.render(delta);
        }
    }

    private StateManager update(StateManager _state)
    {
        return _state;
    }

    public void draw(SpriteBatch _spriteBatch, OrthoGameCamera _camera)
    {
        if (app.appState.peek() == StateID._STATE_MAIN_MENU)
        {
            float originX = (_camera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            float originY = (_camera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            app.spriteBatch.draw(background, originX, originY);
        }
    }

    @Override
    public void loadImages()
    {
        background = app.assets.loadSingleAsset(GameAssets._TEMPLATE_BACKGROUND_ASSET, Texture.class);
    }
}
