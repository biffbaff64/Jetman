package com.richikin.utilslib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.utilslib.assets.IAssets;
import com.richikin.utilslib.google.IAdsController;
import com.richikin.utilslib.google.IPlayServices;

public abstract class LibApp extends com.badlogic.gdx.Game
{
    public SpriteBatch spriteBatch;
    public Stage   stage;
    public IAssets assets;

    public IAdsController adsController;
    public IPlayServices  googleServices;

    public CollisionUtils collisionUtils;
}
