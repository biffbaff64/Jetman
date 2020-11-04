package com.richikin.jetman.core;

import com.richikin.jetman.config.AppConfig;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.google.DummyAdsController;
import com.richikin.utilslib.google.IPlayServices;
import com.richikin.utilslib.states.StateID;

public class MainGame extends com.badlogic.gdx.Game
{
    /**
     * Instantiates a new Main game.
     */
    public MainGame(IPlayServices _services)
    {
        // TODO: 04/11/2020 - Can these be moved to Startup?
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

    /**
     * Handles window resizing
     *
     * @param width  The new window width
     * @param height The new window height
     */
    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

    /**
     * Pause the app
     */
    @Override
    public void pause()
    {
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
        if (!Developer.isDevMode()
            && (App.appState != null)
            && (App.appState.equalTo(StateID._STATE_GAME)))
        {
            AppConfig.unPause();
        }
    }
}
