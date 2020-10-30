
package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.Rover;
import com.richikin.jetman.entities.characters.RoverGun;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

import java.util.concurrent.TimeUnit;

public class RoverManager extends GenericEntityManager
{
    public int roverGunCount;

    private int roverCount;
    private int totalGunsUsed;
    private StopWatch spawnDelay;

    public RoverManager(App _app)
    {
        super(GraphicID.G_ROVER, _app);
    }

    @Override
    public void init()
    {
        spawnDelay      = StopWatch.start();
        totalGunsUsed   = 0;
        roverCount      = 0;
        roverGunCount   = 0;

        createRover();
        createRoverGun();
    }

    @Override
    public void update()
    {
        if (app.entityUtils.canUpdate(GraphicID.G_ROVER))
        {
            if (roverCount == 0)
            {
                createRover();
            }

            if (((spawnDelay.time(TimeUnit.MILLISECONDS) > 2000) || (totalGunsUsed == 0))
                && (roverGunCount == 0))
            {
                if (createRoverGun())
                {
                    if (totalGunsUsed > 1)
                    {
//                        app.getHud().messageManager.enable();
//                        app.getHud().messageManager.addZoomMessage("new_rovergun", 3500);
//                        app.getHud().messageManager.setPosition
//                            (
//                                "new_rovergun",
//                                185,
//                                (720 - 167)
//                            );
                    }
                }
            }
        }
    }

    /**
     * Create a Moon Rover and place in the map.
     * The co-ordinates are obtained from the MarkerTilesLayer
     * from the Tiled map.
     */
    public void createRover()
    {
        if (app.entityUtils.canUpdate(GraphicID.G_ROVER) && (roverCount == 0))
        {
            Trace.__FILE_FUNC();

            SimpleVec2 roverPos = getStartPosition();

            descriptor = Entities.getDescriptor(GraphicID.G_ROVER);
            descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_ROVER);
            descriptor._ANIM_RATE = 5f / 6f;
            descriptor._POSITION.x = roverPos.x;
            descriptor._POSITION.y = roverPos.y;
            descriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER);
            descriptor._INDEX = app.entityData.entityMap.size;

            Rover rover = new Rover(app);
            rover.initialise(descriptor);
            app.entityData.addEntity(rover);
            rover.addPartners();

            app.entityManager._roverIndex = rover.spriteNumber;

            roverCount++;
        }
    }

    public SimpleVec2 getStartPosition()
    {
        SimpleVec2 position = new SimpleVec2();

        for (SpriteDescriptor tile : app.mapData.placementTiles)
        {
            if (tile._GID.equals(GraphicID.G_ROVER))
            {
                position.set(tile._POSITION.x, tile._POSITION.y);
            }
        }

        return position;
    }

    /**
     * Create a Gun Attachment for the Moon Rover and
     * place into the map.
     * The co-ordinates are calculated, using the position
     * of the Moon Rover as a starting point.
     */
    public boolean createRoverGun()
    {
        boolean created = false;

        if (app.entityUtils.canUpdate(GraphicID.G_ROVER) && (roverGunCount == 0))
        {
            int markerX = (int) (app.getRover().getPosition().x / Gfx.getTileWidth());
            markerX += MathUtils.randomBoolean() ? -30 : 25;

            if (isValidPosition(markerX))
            {
                SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_ROVER_GUN);
                descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_ROVER_GUN);
                descriptor._ANIM_RATE = 5f / 6f;
                descriptor._POSITION.x = markerX;
                descriptor._POSITION.y = app.entityManager.playerManager.playerTileY;
                descriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER);
                descriptor._INDEX = app.entityData.entityMap.size;

                RoverGun roverGun = new RoverGun(app);
                roverGun.initialise(descriptor);
                app.entityData.addEntity(roverGun);

                app.entityManager._roverGunIndex = roverGun.spriteNumber;

                roverGun.addTurret();

                roverGunCount++;
                totalGunsUsed++;

                created = true;
            }
        }

        return created;
    }

    private boolean isValidPosition(int x)
    {
//        return !(app.collisionUtils.getMarkerTileOn(x / Gfx.getTileWidth(), 1).get() == TileID._CRATER_TILE.get());
        return true;
    }

    public void freeRoverGun()
    {
        spawnDelay.reset();

        roverGunCount = 0;
    }

    @Override
    public void free()
    {
        reset();
    }

    @Override
    public void reset()
    {
        roverCount = 0;
        roverGunCount = 0;
    }

    @Override
    public int getActiveCount()
    {
        return roverCount;
    }

    public int getGunBarrelCount()
    {
        int count = 0;

        if (app.getGun() != null)
        {
            if (app.getGun().gunTurret != null)
            {
                count++;
            }
        }

        return count;
    }
}
