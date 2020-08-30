package com.richikin.jetman.entities.systems;

// TODO: 27/12/2018 - This class is becoming untidy, with multiple draw methods.
//                  - Investigate simplifying the draw methods.

import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.rootobjects.GameEntity;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.Trace;

public class RenderSystem
{
    private final App app;

    public RenderSystem(App _app)
    {
        this.app = _app;
    }

    /**
     * Draw all sprites to the scene.
     * Uses a Z-coord system: 0 at front, MAX at rear.
     */
    public void drawSprites()
    {
        if (app.entityManager.isEntityUpdateAllowed())
        {
            GdxSprite entity;

            for (int z = Gfx._MAXIMUM_Z_DEPTH; z >= 0; z--)
            {
                for (int i = 0; i < app.entityData.entityMap.size; i++)
                {
                    entity = (GdxSprite) app.entityData.entityMap.get(i);

                    if (entity != null)
                    {
                        if (entity.zPosition == z)
                        {
                            if (isInViewWindow(entity) && entity.isDrawable)
                            {
                                entity.draw(app.spriteBatch);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks for the supplied sprite being inside
     * the scene window.
     *
     * @param sprObj    The sprite to check.
     *
     * @return          TRUE if inside the window.
     */
    private boolean isInViewWindow(GdxSprite sprObj)
    {
        if (app.settings.isEnabled(Settings._CULL_SPRITES))
        {
            return app.mapData.viewportBox.overlaps(sprObj.sprite.getBoundingRectangle());
        }

        return true;
    }
}
