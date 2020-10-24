package com.richikin.utilslib;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.utilslib.assets.Assets;
import com.richikin.utilslib.google.AdsController;
import com.richikin.utilslib.google.PlayServices;

public abstract class App extends com.badlogic.gdx.Game
{
    public SpriteBatch spriteBatch;
    public Stage       stage;
    public Assets      assets;

    public AdsController adsController;
    public PlayServices  googleServices;
}
