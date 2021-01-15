
package com.richikin.jetman.characters.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.TileID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.characters.misc.Rover;
import com.richikin.jetman.characters.misc.RoverGun;
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

    public RoverManager()
    {
        super(GraphicID.G_ROVER);
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
        if (App.entityUtils.canUpdate(GraphicID.G_ROVER))
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
                        App.getHud().messageManager.addZoomMessage("new_rovergun", 3500);
                        App.getHud().messageManager.setPosition
                            (
                                "new_rovergun",
                                (int) AppConfig.hudOriginX + 185,
                                (int) AppConfig.hudOriginY + (720 - 167)
                            );
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
        if (App.entityUtils.canUpdate(GraphicID.G_ROVER) && (roverCount == 0))
        {
            Trace.__FILE_FUNC();

            SimpleVec2 roverPos = getStartPosition();

            descriptor = App.entities.getDescriptor(GraphicID.G_ROVER);
            descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_ROVER);
            descriptor._ANIM_RATE = 5f / 6f;
            descriptor._POSITION.x = roverPos.x;
            descriptor._POSITION.y = roverPos.y;
            descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_ROVER);
            descriptor._INDEX = App.entityData.entityMap.size;

            App.entities.rover = new Rover();
            App.entities.rover.initialise(descriptor);
            App.entityData.addEntity(App.entities.rover);
            App.entities.rover.addPartners();

            roverCount++;
        }
    }

    public SimpleVec2 getStartPosition()
    {
        SimpleVec2 position = new SimpleVec2();

        for (SpriteDescriptor tile : App.mapData.placementTiles)
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

        if (App.entityUtils.canUpdate(GraphicID.G_ROVER) && (roverGunCount == 0))
        {
            int markerX = (int) (App.getRover().getPosition().x / Gfx.getTileWidth());
            markerX += MathUtils.randomBoolean() ? -30 : 25;

            if (isValidPosition(markerX))
            {
                SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_ROVER_GUN);
                descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_ROVER_GUN);
                descriptor._ANIM_RATE = 5f / 6f;
                descriptor._POSITION.x = markerX;
                descriptor._POSITION.y = App.entityManager.playerManager.playerTileY;
                descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_ROVER);
                descriptor._INDEX = App.entityData.entityMap.size;

                App.entities.roverGun = new RoverGun();
                App.entities.roverGun.initialise(descriptor);
                App.entityData.addEntity(App.entities.roverGun);

                App.entities.roverGun.addTurret();

                roverGunCount++;
                totalGunsUsed++;

                created = true;
            }
        }

        return created;
    }

    @SuppressWarnings("SameReturnValue")
    private boolean isValidPosition(int x)
    {
        return !(App.collisionUtils.getMarkerTileOn(x / Gfx.getTileWidth(), 1).get() == TileID._CRATER_TILE.get());
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

        if (App.getGun() != null)
        {
            if (App.getGun().gunTurret != null)
            {
                count++;
            }
        }

        return count;
    }
}
