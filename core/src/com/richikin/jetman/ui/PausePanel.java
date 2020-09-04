package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.core.App;

public class PausePanel extends BasicPanel
{
    private App app;

    public PausePanel(App _app)
    {
        this.app = _app;
    }

    @Override
    public void initialise(TextureRegion _region, String _nameID, Object... args)
    {
    }

    @Override
    public boolean update()
    {
        return false;
    }

    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera, float originX, float originY)
    {
    }
}
