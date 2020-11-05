package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;
import com.richikin.utilslib.input.controllers.ControllerType;

public class HUDRenderer implements IGameScreenRenderer
{
    public HUDRenderer()
    {
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthoGameCamera hudCamera)
    {
        if (!AppSystem.shutDownActive)
        {
            switch (App.appState.peek())
            {
                case _STATE_MAIN_MENU:
                {
                    if (App.mainMenuScreen != null)
                    {
                        App.mainMenuScreen.draw(spriteBatch, hudCamera);
                    }
                }
                break;

                case _STATE_GET_READY:
                case _STATE_PAUSED:
                case _STATE_LEVEL_FINISHED:
                case _STATE_LEVEL_RETRY:
                case _STATE_GAME:
                case _STATE_MESSAGE_PANEL:
                case _STATE_DEBUG_HANG:
                case _STATE_ANNOUNCE_MISSILE:
                case _STATE_TELEPORTING:
                case _STATE_GAME_OVER:
                {
                    if (App.getHud() != null)
                    {
                        App.getHud().render(hudCamera.camera, (AppSystem.availableInputs.contains(ControllerType._VIRTUAL, true)));
                    }
                }
                break;

                case _STATE_GAME_FINISHED:
                {
                    if (App.getHud() != null)
                    {
                        App.getHud().render(hudCamera.camera, false);
                    }
                }
                break;

                case _STATE_CLOSING:
                default:
                {
                }
                break;
            }
        }
    }
}
