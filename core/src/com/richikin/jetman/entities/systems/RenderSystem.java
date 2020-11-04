package com.richikin.jetman.entities.systems;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.utilslib.graphics.LibGfx;

// TODO: 27/12/2018 - This class is becoming untidy, with multiple draw methods.
//                  - Investigate simplifying the draw methods.

public class RenderSystem
{
    private final ShapeRenderer sr;

    public RenderSystem()
    {
        this.sr = new ShapeRenderer();
    }

    /**
     * Draw all sprites to the scene.
     * Uses a Z-coord system: 0 at front, MAX at rear.
     * Any sprites with a Z value > MAX will not be drawn.
     */
    public void drawSprites()
    {
        if (App.entityManager.isEntityUpdateAllowed())
        {
            GdxSprite entity;

            for (int z = LibGfx._MAXIMUM_Z_DEPTH - 1; z >= 0; z--)
            {
                for (int i = 0; i < App.entityData.entityMap.size; i++)
                {
                    entity = (GdxSprite) App.entityData.entityMap.get(i);

                    if (entity != null)
                    {
                        switch (entity.gid)
                        {
                            case G_BACKGROUND_UFO:
                            case G_TWINKLE_STAR:
                            {
                            }
                            break;

                            default:
                            {
                                if (entity.zPosition == z)
                                {
                                    entity.setPositionfromBody();

                                    if (isInViewWindow(entity) && entity.isDrawable)
                                    {
                                        entity.draw(App.spriteBatch);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw non-game sprites which exist in the background layers.
     * These can be the twinkling stars, ufos, or anything else
     * which is animating.
     */
    public void drawBackgroundSprites()
    {
        GdxSprite entity;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            entity = (GdxSprite) App.entityData.entityMap.get(i);

            if (entity != null)
            {
                switch (entity.gid)
                {
                    case G_BACKGROUND_UFO:
                    case G_TWINKLE_STAR:
                    {
                        entity.draw(App.spriteBatch);
                    }
                    break;

                    default:
                    {
                    }
                    break;
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
        return App.mapData.viewportBox.overlaps(sprObj.sprite.getBoundingRectangle());
    }

    /**
     * Draw the teleporter beams, which are activated when the
     * teleporter is in use. These are not sprites.
     *
     * @param teleportBeam  The {@link TeleportBeam} object.
     */
    public void drawTeleportBeams(TeleportBeam teleportBeam)
    {
        if (teleportBeam != null)
        {
            teleportBeam.draw(App.spriteBatch);
        }
    }
}
