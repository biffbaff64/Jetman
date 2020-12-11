package com.richikin.jetman.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.google.DummyAdsController;
import com.richikin.utilslib.google.IPlayServices;
import com.richikin.enumslib.StateID;

public class MainGame extends com.badlogic.gdx.Game
{
    /**
     * Instantiates a new Main game.
     */
    public MainGame(IPlayServices _services)
    {
        App.googleServices = _services;
        App.adsController  = new DummyAdsController();
    }

    @Override
    public void create()
    {
        App.mainGame = this;

        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        //
        Startup startup = new Startup();
        startup.startApp();
        startup.closeStartup();
    }

    @Override
    public void render()
    {
        super.render();

        AppConfig.configListener.update();

        App.baseRenderer.getSplashScreen().update();
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
