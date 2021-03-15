package com.richikin.jetman.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.developer.Developer;
import com.richikin.jetman.graphics.effects.StarField;
import com.richikin.jetman.screens.SplashScreen;
import com.richikin.utilslib.google.DummyAdsController;
import com.richikin.utilslib.google.IPlayServices;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.google.PlayServicesData;

public class MainGame extends com.badlogic.gdx.Game
{
    private Startup startup;

    /**
     * Instantiates a new Main game.
     */
    public MainGame(IPlayServices _services)
    {
        App.googleServices   = _services;
        App.adsController    = new DummyAdsController();
        App.playServicesData = new PlayServicesData();
    }

    @Override
    public void create()
    {
        App.mainGame = this;

        SplashScreen.inst().setup();

        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        //
        startup = new Startup();
    }

    @Override
    public void render()
    {
        if (SplashScreen.inst().isAvailable)
        {
            if (!startup.startupDone)
            {
                startup.startApp();
            }

            SplashScreen.inst().update();
            SplashScreen.inst().render();

            if (!SplashScreen.inst().isAvailable)
            {
                startup.closeStartup();
            }
        }
        else
        {
            ScreenUtils.clear(1, 1, 1, 1);

            super.render();

            AppConfig.configListener.update();
        }
    }

    @Override
    public void setScreen(Screen screen)
    {
        Gdx.app.debug("MG: ", ("" + screen.getClass()));

        super.setScreen(screen);
    }

    /**
     * Pause the app
     */
    @Override
    public void pause()
    {
        super.pause();

        if (!Developer.isDevMode()
            && (App.appState != null)
            && (App.appState.equalTo(StateID._STATE_GAME)))
        {
            AppConfig.pause();
        }
    }

    /**
     * Actions to perform on leaving Pause
     */
    @Override
    public void resume()
    {
        super.resume();

        if (!Developer.isDevMode()
            && (App.appState != null)
            && (App.appState.equalTo(StateID._STATE_GAME)))
        {
            AppConfig.unPause();
        }
    }
}
