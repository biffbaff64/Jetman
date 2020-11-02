package com.richikin.utilslib.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * UIPaqes are Main Menu/Title sequences pages,
 * such as Menu, Credits page, hiscore page etc.
 * Pages should implement this interface.
 */
public interface IUIPage
{
    boolean update();

    void show();

    void hide();

    void draw(SpriteBatch spriteBatch, OrthographicCamera camera, float originX, float originY);

    void reset();

    void dispose();
}
