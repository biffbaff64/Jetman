package com.richikin.jetman.physics;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;
import com.richikin.jetman.physics.AABB.AABBData;
import com.richikin.jetman.physics.AABB.CollisionObject;
import com.richikin.jetman.utils.logging.Trace;

public class CollisionUtils implements ICollideUtils, Disposable
{
    private final App app;

    /**
     * Instantiates a new CollisionUtils object.
     *
     * @param _app the game
     */
    public CollisionUtils(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    /**
     * Initialise these utils.
     */
    public void initialise()
    {
        AABBData.initialise();
    }

    /**
     * Get a new collision object.
     *
     * @return the collision object
     */
    public CollisionObject newObject()
    {
        return new CollisionObject();
    }

    /**
     * Get a new collision object.
     *
     * @param rectangle the rectangle
     *
     * @return the collision object
     */
    public CollisionObject newObject(Rectangle rectangle)
    {
        return new CollisionObject(rectangle);
    }

    /**
     * Get a new object collision object.
     *
     * @param x         the x
     * @param y         the y
     * @param width     the width
     * @param height    the height
     * @param graphicID the graphic id
     *
     * @return the collision object
     */
    public CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        return new CollisionObject(x, y, width, height, graphicID);
    }

    /**
     * Tidy ALL currently active collision objects.
     */
    public void tidy()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            if (AABBData.boxes().get(i).action == Actions._DEAD)
            {
                AABBData.remove(i);
            }
        }
    }

    /**
     * Tidy (Kill) ALL currently active collision objects.
     */
    private void tidyAll()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            AABBData.boxes().get(i).action = Actions._DEAD;
        }

        tidy();
    }

    /**
     * Dump debug information for all
     */
    public void debugAll()
    {
        for (int i = 0; i < AABBData.boxes().size; i++)
        {
//            AABBData.boxes().get(i).debug();
        }
    }

    /**
     * @param entity the entity
     * @param target the target
     *
     * @return the boolean
     */
    public boolean canCollide(GdxSprite entity, GdxSprite target)
    {
        return ((entity.collidesWith & target.bodyCategory) != 0)
            && ((target.collidesWith & entity.bodyCategory) != 0)
            && (entity.spriteNumber != target.spriteNumber);
//            && (entity.collisionObject.collisionArrayIndex != target.collisionObject.collisionArrayIndex);
    }

    /**
     * @param theEntityFlag         The entitiy's bodyCategory flag
     * @param theCollisionBoxFlag   The collidesWith flag of the entity to test against.
     *
     * @return TRUE if the two entities are able to collide.
     */
    public boolean filter(short theEntityFlag, short theCollisionBoxFlag)
    {
        return ((theEntityFlag & theCollisionBoxFlag) != 0);
    }

    /**
     * Gets marker tile on.
     *
     * @param x the x
     * @param y the y
     *
     * @return the marker tile on
     */
    public TileID getMarkerTileOn(int x, int y)
    {
        TileID tileID = TileID._UNKNOWN;

        for (SpriteDescriptor placementTile : app.mapData.placementTiles)
        {
            if (placementTile._BOX.contains(x, y))
            {
                tileID = placementTile._TILE;
            }
        }

        return tileID;
    }

    /**
     *
     *
     * @param spriteObj the sprite obj
     *
     * @return
     */
    public int getXBelow(GdxSprite spriteObj)
    {
        return (int) ((spriteObj.getCollisionRectangle().getX() + (Gfx.getTileWidth() / 2)) / Gfx.getTileWidth());
    }

    /**
     *
     *
     * @param spriteObj the sprite obj
     *
     * @return
     */
    public int getYBelow(GdxSprite spriteObj)
    {
        int y;

        y = (int) ((spriteObj.sprite.getY() - (spriteObj.sprite.getY() % Gfx.getTileHeight())) / Gfx.getTileHeight());

        if ((spriteObj.sprite.getY() % Gfx.getTileHeight()) == 0)
        {
            y--;
        }

        return y;
    }

    /**
     * Gets box hitting top.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting top
     */
    public CollisionObject getBoxHittingTop(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingTop);
    }

    /**
     * Gets box hitting bottom.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting bottom
     */
    public CollisionObject getBoxHittingBottom(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingBottom);
    }

    /**
     * Gets box hitting left.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting left
     */
    public CollisionObject getBoxHittingLeft(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingLeft);
    }

    /**
     * Gets box hitting right.
     *
     * @param spriteObject the sprite object
     *
     * @return the box hitting right
     */
    public CollisionObject getBoxHittingRight(GdxSprite spriteObject)
    {
        return AABBData.boxes().get(spriteObject.collisionObject.boxHittingRight);
    }

    @Override
    public void dispose()
    {
        tidyAll();
    }
}
