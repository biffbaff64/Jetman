package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class CraterManager
{
    private final int[][][] craters =
        {
            {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
            },
            {
                { 9, 10, 11, 12},
                {13, 14, 15, 16},
            },
            {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
            },
        };

    public CraterManager()
    {
    }

    /**C
     * Add a crater into the TiledMap at the
     * specified co-ordinates.
     *
     * @param x     X Co-ordinate (in tiles, not pixels).
     * @param y     X Co-ordinate (in tiles, not pixels).
     */
    public void makeCrater(int x, int y)
    {
        if ((App.gameProgress.activeCraterCount < App.roomManager.getMaxAllowed(GraphicID._CRATER))
            && (App.mapData.mapBox.contains((x + 3) * Gfx.getTileWidth(), Gfx.getTileHeight())))
        {
            Trace.__FILE_FUNC();

            TiledMapTileLayer.Cell cell;
            TiledMapTileSet floorTileSet = App.mapData.currentMap.getTileSets().getTileSet("items");

            int type = MathUtils.random(craters.length - 1);

            for (int row = 0; row < 2; row++)
            {
                for (int column = 0; column < craters[type][row].length; column++)
                {
                    cell = new TiledMapTileLayer.Cell();
                    cell.setTile(floorTileSet.getTile(craters[type][row][column] + 244));
                    App.mapData.gameTilesLayer.setCell(x + column, y - row, cell);

                    // TODO: 25/10/2020 - need to add an appropriate collision box here
                }
            }

            App.gameProgress.activeCraterCount++;
        }
    }

    /**
     * Returns TRUE if a crater is allowed to be created.
     * This method should be called when, for instance,
     * an Asteroid hits the ground and explodes. This is to
     * allow the asteroid to leave a crater behind.
     *
     * @param spriteObject  The GdxSprite making the crater
     * @param force         TRUE to force crater creation if
     *                      the position is valid.
     *
     * @return boolean.
     */
    public boolean canMakeCrater(GdxSprite spriteObject, boolean force)
    {
        //
        // No craters allowed under Rovers
        if ((spriteObject.gid != GraphicID.G_ROVER) && (App.getRover() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getRover().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No craters allowed under Rover Guns
        if ((spriteObject.gid != GraphicID.G_ROVER_GUN) && (App.getGun() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getGun().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No craters allowed under Bombs
        if ((spriteObject.gid != GraphicID.G_BOMB) && (App.getBomb() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getBomb().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No craters allowed under Teleporters
        if ((spriteObject.gid != GraphicID.G_TRANSPORTER) && App.doTransportersExist())
        {
            if ((Intersector.overlaps(spriteObject.getCollisionRectangle(),
                App.getTeleporter(0).getCollisionRectangle())
                || (Intersector.overlaps(spriteObject.getCollisionRectangle(),
                App.getTeleporter(1).getCollisionRectangle()))))
            {
                return false;
            }
        }

        //
        // LJM can leave a crater behind if he falls to his death
        // from a great height.
        if ((spriteObject.gid != GraphicID.G_PLAYER) && (App.getPlayer() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getPlayer().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No need for null check here. Bridges either exist or don't,
        // there's no sprite to check.
        if (spriteObject.collisionObject.idBottom == GraphicID._BRIDGE)
        {
            return false;
        }

        //
        // No craters allowed if there is already a crater next
        // to where this one would be created.


        return (force || (MathUtils.random(100) < 5));
    }
}
