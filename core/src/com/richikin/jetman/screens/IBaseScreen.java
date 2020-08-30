
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;

public interface IBaseScreen
{
    void initialise();

    void update();

    void triggerFadeIn();

    void triggerFadeOut();

    void show();

    void hide();

    void render(float delta);

    void resize(int _width, int _height);

    void pause();

    void resume();

    void dispose();

    void loadImages();
}
