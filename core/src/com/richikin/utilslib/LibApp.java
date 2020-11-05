package com.richikin.utilslib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.utilslib.core.HighScoreUtils;
import com.richikin.utilslib.graphics.IAssets;
import com.richikin.utilslib.core.ISettings;
import com.richikin.utilslib.google.IAdsController;
import com.richikin.utilslib.google.IPlayServices;
import com.richikin.utilslib.logging.StateManager;

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
