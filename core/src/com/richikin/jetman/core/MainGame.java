package com.richikin.jetman.core;

import com.richikin.jetman.config.AppConfig;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.google.DummyAdsController;
import com.richikin.utilslib.google.PlayServices;
import com.richikin.utilslib.states.StateID;

public class MainGame extends App
{
    /**
     * Instantiates a new Main game.
     */
    public MainGame(PlayServices _services)
    {
        super();

        this.googleServices = _services;
        this.adsController  = new DummyAdsController();
    }

    @Override
    public void create()
    {
        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        //
        Startup startup = new Startup(this);
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
            && (appState != null)
            && (appState.equalTo(com.richikin.utilslib.states.StateID._STATE_GAME)))
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
            && (appState != null)
            && (appState.equalTo(StateID._STATE_GAME)))
        {
            AppConfig.unPause();
        }
    }
}
