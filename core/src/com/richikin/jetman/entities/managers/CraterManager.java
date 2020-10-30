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

    private final App app;

    public CraterManager(App _app)
    {
        this.app = _app;
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
        if ((app.gameProgress.activeCraterCount < app.roomManager.getMaxAllowed(GraphicID._CRATER))
            && (app.mapData.mapBox.contains((x + 3) * Gfx.getTileWidth(), Gfx.getTileHeight())))
        {
            Trace.__FILE_FUNC();

            TiledMapTileLayer.Cell cell;
            TiledMapTileSet floorTileSet = app.mapData.currentMap.getTileSets().getTileSet("items");

            int type = MathUtils.random(craters.length - 1);

            for (int row = 0; row < 2; row++)
            {
                for (int column = 0; column < craters[type][row].length; column++)
                {
                    cell = new TiledMapTileLayer.Cell();
                    cell.setTile(floorTileSet.getTile(craters[type][row][column] + 244));
                    app.mapData.gameTilesLayer.setCell(x + column, y - row, cell);

                    // TODO: 25/10/2020 - need to add an appropriate collision box here
                }
            }

            app.gameProgress.activeCraterCount++;
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
        if ((spriteObject.gid != GraphicID.G_ROVER) && (app.getRover() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), app.getRover().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No craters allowed under Rover Guns
        if ((spriteObject.gid != GraphicID.G_ROVER_GUN) && (app.getGun() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), app.getGun().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No craters allowed under Bombs
        if ((spriteObject.gid != GraphicID.G_BOMB) && (app.getBomb() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), app.getBomb().getCollisionRectangle()))
            {
                return false;
            }
        }

        //
        // No craters allowed under Teleporters
        if ((spriteObject.gid != GraphicID.G_TRANSPORTER) && app.doTransportersExist())
        {
            if ((Intersector.overlaps(spriteObject.getCollisionRectangle(),
                app.getTeleporter(0).getCollisionRectangle())
                || (Intersector.overlaps(spriteObject.getCollisionRectangle(),
                app.getTeleporter(1).getCollisionRectangle()))))
            {
                return false;
            }
        }

        //
        // LJM can leave a crater behind if he falls to his death
        // from a great height.
        if ((spriteObject.gid != GraphicID.G_PLAYER) && (app.getPlayer() != null))
        {
            if (Intersector.overlaps(spriteObject.getCollisionRectangle(), app.getPlayer().getCollisionRectangle()))
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
