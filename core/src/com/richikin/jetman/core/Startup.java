package com.richikin.jetman.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.assets.AssetLoader;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.entities.EntityData;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.CameraUtils;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.graphics.camera.Shake;
import com.richikin.jetman.graphics.renderers.BaseRenderer;
import com.richikin.jetman.input.InputManager;
import com.richikin.jetman.maps.MapCreator;
import com.richikin.jetman.maps.MapData;
import com.richikin.jetman.maps.MapUtils;
import com.richikin.jetman.screens.MainGameScreen;
import com.richikin.jetman.screens.MainMenuScreen;
import com.richikin.jetman.ui.PanelManager;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.core.HighScoreUtils;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.logging.StateManager;

public class Startup
{
    public Startup()
    {
    }

    public void startApp()
    {
        //
        // Initialise DEBUG classes
        //noinspection LibGDXLogLevel
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Trace.__FILE_FUNC_WithDivider();

        App.appState = new StateManager(StateID._STATE_POWER_UP);

        App.settings = new Settings();
        App.settings.initialise();

        App.assets = new AssetLoader();
        App.spriteBatch = new SpriteBatch();

        AppConfig.setup();
        AppConfig.freshInstallCheck();

        Gfx.setPPM(32.0f);

        if (AppSystem.isAndroidApp())
        {
            App.googleServices.setup();
            App.googleServices.createApiClient();
        }

        App.cameraUtils    = new CameraUtils();
        App.worldModel     = new WorldModel();
        App.baseRenderer   = new BaseRenderer();

        //
        // This needs setting here as InputManager needs access to it.
        App.stage = new Stage(App.baseRenderer.hudGameCamera.viewport, App.spriteBatch);

        App.inputManager   = new InputManager();
        App.panelManager   = new PanelManager();

        App.highScoreUtils = new HighScoreUtils();

        //
        // TODO: 19/08/2020
        // These objects should really be initialised when moving
        // from MainMenuScreen to MainGameScreen.
        App.mapCreator     = new MapCreator();
        App.entityData     = new EntityData();
        App.mapData        = new MapData();
        App.mapUtils       = new MapUtils();
        App.gameProgress   = new GameProgress();
        App.mainMenuScreen = new MainMenuScreen();
        App.mainGameScreen = new MainGameScreen();

        GameAudio.inst().setup();
        Shake.setAllowed(false);

        AppSystem.addBackButton();

        Trace.divider();
    }

    public void closeStartup()
    {
        if (Developer.isDevMode() && App.settings.isEnabled(Settings._DISABLE_MENU_SCREEN))
        {
            App.mainGame.setScreen(App.mainGameScreen);
        }
        else
        {
            App.mainGame.setScreen(App.mainMenuScreen);
        }
    }
}
