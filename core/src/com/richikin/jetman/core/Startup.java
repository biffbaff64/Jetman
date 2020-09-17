package com.richikin.jetman.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.assets.AssetLoader;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.EntityData;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.camera.CameraUtils;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.jetman.graphics.renderers.BaseRenderer;
import com.richikin.jetman.input.InputManager;
import com.richikin.jetman.maps.MapCreator;
import com.richikin.jetman.maps.MapData;
import com.richikin.jetman.maps.MapUtils;
import com.richikin.jetman.screens.MainGameScreen;
import com.richikin.jetman.screens.MainMenuScreen;
import com.richikin.jetman.ui.PanelManager;
import com.richikin.jetman.utils.Developer;
import com.richikin.jetman.utils.logging.Trace;

public class Startup
{
    private final App app;

    public Startup(App _app)
    {
        this.app = _app;
    }

    public void startApp()
    {
        //
        // Initialise DEBUG classes
        //noinspection LibGDXLogLevel
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Trace.__FILE_FUNC_WithDivider();

        app.appState = new StateManager(StateID._STATE_POWER_UP);

        app.settings = new Settings();
        app.settings.initialise();

        app.assets = new AssetLoader();
        app.spriteBatch = new SpriteBatch();

        AppConfig.setup(app);
        AppConfig.freshInstallCheck();

        Gfx.setPPM(32.0f);

        if (AppConfig.isAndroidApp())
        {
            app.googleServices.setup(app);
            app.googleServices.createApiClient();
        }

        app.cameraUtils    = new CameraUtils(app);
        app.worldModel     = new WorldModel(app);
        app.baseRenderer   = new BaseRenderer(app);

        //
        // This needs setting here as InputManager needs access to it.
        app.stage = new Stage(app.baseRenderer.hudGameCamera.viewport, app.spriteBatch);

        app.inputManager   = new InputManager(app);
        app.panelManager   = new PanelManager(app);

        //
        // TODO: 19/08/2020
        // These objects should really be initialised when moving
        // from MainMenuScreen to MainGameScreen.
        app.mapCreator     = new MapCreator(app);
        app.entityData     = new EntityData();
        app.mapData        = new MapData(app);
        app.mapUtils       = new MapUtils(app);
        app.gameProgress   = new GameProgress(app);
        app.mainMenuScreen = new MainMenuScreen(app);
        app.mainGameScreen = new MainGameScreen(app);

        GameAudio.setup(app);
        Shake.setAllowed(false);

        Trace.divider();
    }

    public void closeStartup()
    {
        if (Developer.isDevMode() && app.settings.isEnabled(Settings._DISABLE_MENU_SCREEN))
        {
            app.setScreen(app.mainGameScreen);
        }
        else
        {
            app.setScreen(app.mainMenuScreen);
        }
    }
}
