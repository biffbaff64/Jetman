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
        y = 2;

        if ((App.gameProgress.activeCraterCount < App.roomManager.getMaxAllowed(GraphicID._CRATER))
            && (App.mapData.mapBox.contains((x + 3) * Gfx.getTileWidth(), Gfx.getTileHeight())))
        {
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
     *
     * @return boolean.
     */
    public boolean canMakeCrater(GdxSprite spriteObject)
    {
        boolean isOk = true;

        //
        // No craters allowed in mid-air
        if (spriteObject.collisionObject.idBottom == GraphicID.G_NO_ID)
        {
            isOk = false;
        }

        //
        // No craters allowed under Rovers
        if ((spriteObject.gid != GraphicID.G_ROVER) && (App.getRover() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getRover().getCollisionRectangle()))
            {
                isOk =  false;
            }
        }

        //
        // No craters allowed under Rover Guns
        if ((spriteObject.gid != GraphicID.G_ROVER_GUN) && (App.getGun() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getGun().getCollisionRectangle()))
            {
                isOk =  false;
            }
        }

        //
        // No craters allowed under Bombs
        if ((spriteObject.gid != GraphicID.G_BOMB) && (App.getBomb() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getBomb().getCollisionRectangle()))
            {
                isOk =  false;
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
                isOk =  false;
            }
        }

        //
        // No craters allowed under Laser Barriers
        if (spriteObject.gid != GraphicID.G_POWER_BEAM)
        {
            if (App.collisionUtils.getBoxHittingBottom(spriteObject).gid == GraphicID.G_POWER_BEAM)
            {
                isOk = false;
            }
        }

        //
        // LJM can leave a crater behind if he falls to his death
        // from a great height.
        if ((spriteObject.gid != GraphicID.G_PLAYER) && (App.getPlayer() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), App.getPlayer().getCollisionRectangle()))
            {
                isOk =  false;
            }
        }

        //
        // No need for null check here. Bridges and Craters either exist or don't,
        // there's no sprite to check.
        if ((spriteObject.collisionObject.idBottom == GraphicID._BRIDGE)
            || (spriteObject.collisionObject.idBottom == GraphicID._CRATER))
        {
            isOk =  false;
        }

        //
        // No craters allowed if there is already a crater next
        // to where this one would be created.


        return isOk;
    }
}
