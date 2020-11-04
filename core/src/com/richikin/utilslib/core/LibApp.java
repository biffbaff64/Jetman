package com.richikin.utilslib.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.utilslib.assets.IAssets;
import com.richikin.utilslib.config.ISettings;
import com.richikin.utilslib.google.IAdsController;
import com.richikin.utilslib.google.IPlayServices;
import com.richikin.utilslib.misc.HighScoreUtils;
import com.richikin.utilslib.states.StateManager;

public abstract class LibApp
{
    public static SpriteBatch    spriteBatch;
    public static Stage          stage;
    public static IAssets        assets;
    public static ISettings      settings;
    public static StateManager   appState;
    public static IAdsController adsController;
    public static IPlayServices  googleServices;
    public static CollisionUtils collisionUtils;
    public static HighScoreUtils highScoreUtils;
}
