package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;

public class WorldRenderer implements IGameScreenRenderer
{
    public WorldRenderer()
    {
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthoGameCamera gameCamera)
    {
        switch (App.appState.peek())
        {
            case _STATE_MAIN_MENU:
            {
                App.mainMenuScreen.draw(spriteBatch, gameCamera);
            }
            break;

            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_PAUSED:
            case _STATE_LEVEL_RETRY:
            case _STATE_LEVEL_FINISHED:
            case _STATE_GAME:
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_SETTINGS_PANEL:
            case _STATE_TELEPORTING:
            case _STATE_DEBUG_HANG:
            {
                if (!App.settings.isEnabled(Settings._USING_ASHLEY_ECS))
                {
                    App.entityManager.drawSprites();
                }
            }
            break;

            case _STATE_CLOSING:
            case _STATE_GAME_OVER:
            case _STATE_END_GAME:
            {
            }
            break;

            default:
                break;
        }
    }
}
