
package com.richikin.jetman.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

import org.jetbrains.annotations.NotNull;

public class EntityUtils
{
    public EntityUtils()
    {
    }

    public Animation<TextureRegion> createAnimation(String filename, TextureRegion[] destinationFrames, int frameCount, Animation.PlayMode playmode)
    {
        Animation<TextureRegion> animation;

        try
        {
            TextureRegion textureRegion = App.assets.getAnimationRegion(filename);

            TextureRegion[] tmpFrames = textureRegion.split
                (
                    (textureRegion.getRegionWidth() / frameCount),
                    textureRegion.getRegionHeight()
                )[0];

            System.arraycopy(tmpFrames, 0, destinationFrames, 0, frameCount);

            //noinspection MagicNumber
            animation = new Animation<>(0.75f / 6f, tmpFrames);
            animation.setPlayMode(playmode);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC("Craeting animation from " + filename + " failed!");

            animation = null;
        }

        return animation;
    }

    public TextureRegion getKeyFrame(final Animation<? extends TextureRegion> animation, final float elapsedTime, final boolean looping)
    {
        return animation.getKeyFrame(elapsedTime, looping);
    }

    /**
     * Gets a random sprite from {@link EntityData#entityMap}, making
     * sure to not return the specified sprite.
     */
    public GdxSprite getRandomSprite(GdxSprite oneToAvoid)
    {
        GdxSprite randomSprite;

        do
        {
            randomSprite = (GdxSprite) App.entityData.entityMap.get(MathUtils.random(App.entityData.entityMap.size - 1));
        }
        while ((randomSprite.gid == oneToAvoid.gid)
                || (randomSprite.sprite == null)
                || (randomSprite.spriteNumber == oneToAvoid.spriteNumber));

        return randomSprite;
    }

    /**
     * Finds the nearest sprite of type gid to the player.
     */
    public GdxSprite findNearest(GraphicID gid)
    {
        GdxSprite distantSprite = findFirstOf(gid);

        if (distantSprite != null)
        {
            float distance = App.getPlayer().getPosition().dst(distantSprite.getPosition());

            for (GameEntity entity : App.entityData.entityMap)
            {
                if (entity.gid == gid)
                {
                    GdxSprite gdxSprite = (GdxSprite) entity;

                    float tempDistance = App.getPlayer().getPosition().dst(gdxSprite.getPosition());

                    if (Math.abs(tempDistance) < Math.abs(distance))
                    {
                        distance      = tempDistance;
                        distantSprite = gdxSprite;
                    }
                }
            }
        }

        return distantSprite;
    }

    /**
     * Finds the furthest sprite of type gid to the player.
     */
    public GdxSprite getDistantSprite(@NotNull GdxSprite checkSprite)
    {
        GdxSprite distantSprite = App.getPlayer();

        float distance = checkSprite.getPosition().dst(distantSprite.getPosition());

        for (GameEntity entity : App.entityData.entityMap)
        {
            GdxSprite gdxSprite = (GdxSprite) entity;

            float tempDistance = checkSprite.getPosition().dst(gdxSprite.getPosition());

            if (Math.abs(tempDistance) > Math.abs(distance))
            {
                distance = tempDistance;
                distantSprite = gdxSprite;
            }
        }

        return distantSprite;
    }

    /**
     * Resets positions for all entities back to
     * their initialisation positions.
     */
    public void resetAllPositions()
    {
        if (App.entityData.entityMap != null)
        {
            GdxSprite entity;

            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) App.entityData.entityMap.get(i);

                entity.sprite.setPosition(entity.initXYZ.getX(), entity.initXYZ.getY());
            }
        }
    }

    /**
     * Fetch an initial Z position for the specified ID.
     *
     * @param graphicID The GraphicID.
     * @return Z position range is between 0 and Gfx._MAXIMUM_Z_DEPTH.
     */
    // TODO: 07/12/2020 - This method is too long, shorten it.
    public int getInitialZPosition(@NotNull GraphicID graphicID)
    {
        int zed;

        switch (graphicID)
        {
            case G_TWINKLE_STAR:
            case G_BACKGROUND_UFO:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH-1;
            }
            break;

            case G_PRIZE_BALLOON:
            case G_MESSAGE_BUBBLE:
            {
                zed = 10;
            }
            break;

            case G_DEFENDER:
            case G_DEFENDER_ZAP:
            {
                zed = 9;
            }
            break;

            case G_DEFENDER_BULLET:
            case G_ROVER_BULLET:
            case G_POWER_BEAM:
            {
                zed = 8;
            }
            break;

            case G_MISSILE:
            case G_MISSILE_BASE:
            case G_MISSILE_LAUNCHER:
            case G_TRANSPORTER:
            case G_ROVER_GUN:
            case G_ROVER_GUN_BARREL:
            {
                zed = 7;
            }
            break;

            case G_ROVER:
            case G_ROVER_BOOT:
            case G_ROVER_WHEEL:
            {
                zed = 6;
            }
            break;

            case G_LASER:
            case G_BOMB:
            case G_PLAYER:
            {
                zed = 5;
            }
            break;

            case G_STAIR_CLIMBER:
            case G_UFO_BULLET:
            case G_3BALLS_UFO:
            case G_3LEGS_ALIEN:
            case G_ALIEN_WHEEL:
            case G_ASTEROID:
            case G_BLOB:
            case G_DOG:
            case G_GREEN_BLOCK:
            case G_SPINNING_BALL:
            case G_STAR_SPINNER:
            case G_TOPSPIN:
            case G_TWINKLES:
            {
                zed = 4;
            }
            break;

            case G_EXPLOSION12:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:
            {
                zed = 0;
            }
            break;

            default:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH + 1;
            }
            break;
        }

        return zed;
    }

    public boolean isOnScreen(@NotNull GdxSprite spriteObject)
    {
        return App.mapData.viewportBox.overlaps(spriteObject.sprite.getBoundingRectangle());
    }

    public void tidy()
    {
        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            if (App.entityData.entityMap.get(i).entityAction == ActionStates._DEAD)
            {
                App.entityData.entityMap.removeIndex(i);
            }
        }
    }

    public GdxSprite findFirstOf(final GraphicID gid)
    {
        GdxSprite gdxSprite = null;

        for (GameEntity entity : App.entityData.entityMap)
        {
            if (entity.gid == gid)
            {
                gdxSprite = (GdxSprite) entity;
                break;
            }
        }

        return gdxSprite;
    }

    public GdxSprite findLastOf(final GraphicID gid)
    {
        GdxSprite gdxSprite = null;

        for (GameEntity entity : App.entityData.entityMap)
        {
            if (entity.gid == gid)
            {
                gdxSprite = (GdxSprite) entity;
                break;
            }
        }

        return gdxSprite;
    }

    public boolean canUpdate(@NotNull GraphicID graphicID)
    {
        boolean isUpdateable;

        switch (graphicID)
        {
            case G_TWINKLE_STAR:
            case G_ARROW:
            case G_BACKGROUND_UFO:
            {
                isUpdateable = true;
            }
            break;

            default:
            {
                isUpdateable = isEntityEnabled(graphicID);
            }
            break;
        }

        return isUpdateable;
    }

    // TODO: 07/12/2020 - This method is too long, shorten it.
    private boolean isEntityEnabled(@NotNull GraphicID graphicID)
    {
        boolean isEnabled;

        switch (graphicID)
        {
            case G_BOMB:
            case G_ROVER:
            case G_ROVER_GUN:
            case G_ROVER_WHEEL:
            case G_ROVER_BOOT:
            case G_ROVER_GUN_BARREL:
            case G_ROVER_BULLET:
            case G_TRANSPORTER:
            case G_MISSILE:
            case G_MISSILE_BASE:
            case G_MISSILE_LAUNCHER:
            case G_DEFENDER:
            case G_DEFENDER_ZAP:
            case G_DEFENDER_BULLET:
            case G_PLAYER:
            case G_LASER:
            case G_STAIR_CLIMBER:
            case G_3BALLS_UFO:
            case G_UFO_BULLET:
            case G_3LEGS_ALIEN:
            case G_ASTEROID:
            case G_DOG:
            case G_EXPLOSION12:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:
            case G_GREEN_BLOCK:
            case G_MESSAGE_BUBBLE:
            case G_BLOB:
            case G_PRIZE_BALLOON:
            case G_SPINNING_BALL:
            case G_STAR_SPINNER:
            case G_TOPSPIN:
            case G_TWINKLES:
            case G_ALIEN_WHEEL:
            case G_POWER_BEAM:
            {
                isEnabled = true;
            }
            break;

            default:
            {
                isEnabled = false;
            }
            break;
        }

        return isEnabled;
    }

    public SimpleVec2 translateMapPosToEntityWindow(GdxSprite sprite)
    {
        SimpleVec2 pos = sprite.position;

        // TODO: 07/12/2020

        return pos;
    }
}
