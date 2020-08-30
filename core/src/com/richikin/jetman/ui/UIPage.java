package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;

/**
 * UIPaqes are Main Menu/Title sequences pages,
 * such as Menu, Credits page, hiscore page etc.
 * Pages should implement this interface.
 */
public interface UIPage
{
    boolean update();

    void show();

    void hide();

    void draw(SpriteBatch spriteBatch, OrthoGameCamera camera, float originX, float originY);

    void reset();

    void dispose();
}
