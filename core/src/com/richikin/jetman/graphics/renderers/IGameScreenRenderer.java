
package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;

@FunctionalInterface
public interface IGameScreenRenderer
{
    void render(SpriteBatch spriteBatch, OrthoGameCamera guiCamera);
}
