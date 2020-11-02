
package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;

public interface IGameScreenRenderer
{
    void render(SpriteBatch spriteBatch, OrthoGameCamera guiCamera);
}
