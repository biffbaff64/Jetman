package com.richikin.utilslib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.utilslib.assets.IAssets;
import com.richikin.utilslib.google.IAdsController;
import com.richikin.utilslib.google.IPlayServices;
import com.richikin.utilslib.misc.HighScoreUtils;
import com.richikin.utilslib.states.StateManager;

public abstract class LibApp extends com.badlogic.gdx.Game
{
    public SpriteBatch spriteBatch;
    public Stage   stage;
    public IAssets assets;

    public StateManager appState;

    public IAdsController adsController;
    public IPlayServices  googleServices;

    public CollisionUtils collisionUtils;

    public HighScoreUtils highScoreUtils;
}
