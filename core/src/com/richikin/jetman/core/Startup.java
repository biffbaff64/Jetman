package com.richikin.jetman.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.screens.MainGameScreen;
import com.richikin.jetman.screens.MainMenuScreen;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.logging.Trace;

public class Startup
{
    public Startup()
    {
    }

    /**
     * App Startup process
     */
    public void startApp()
    {
        //
        // Initialise DEBUG classes
        //noinspection LibGDXLogLevel
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Trace.__FILE_FUNC_WithDivider();

        App.initialiseObjects();

        App.settings.createPreferencesObject();
        AppConfig.setup();
        App.settings.freshInstallCheck();
        App.settings.debugReport();
        App.inputManager.setup();

        Gfx.setPPM(Gfx._PPM_SETTING);

        if (AppConfig.isAndroidApp())
        {
            Trace.__FILE_FUNC("Initialising Google Play Services.");

            App.googleServices.setup();
            App.googleServices.createApiClient();
        }

        GameAudio.inst().setup();
        Shake.setAllowed(false);

        AppConfig.addBackButton("back_arrow", "back_arrow_pressed");

        Trace.divider();
    }

    /**
     * Ends the startup process by handing control
     * to the {@link MainMenuScreen} or, if MainMenuScreen
     * is disabled, control is passed to {@link MainGameScreen}
     */
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
