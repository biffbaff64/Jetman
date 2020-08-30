
package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;

public interface GameScreenRenderer
{
    void render(SpriteBatch spriteBatch, OrthoGameCamera guiCamera);
}
