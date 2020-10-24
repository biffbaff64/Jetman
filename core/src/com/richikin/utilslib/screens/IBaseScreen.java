
package com.richikin.utilslib.screens;

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
