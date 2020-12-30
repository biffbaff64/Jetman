package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * UIPaqes are Main Menu/Title sequences pages,
 * such as Menu, Credits page, hiscore page etc.
 * Pages should implement this interface.
 */
public interface IUIPage
{
    void initialise();

    boolean update();

    void show();

    void hide();

    void draw(SpriteBatch spriteBatch);

    void dispose();
}
