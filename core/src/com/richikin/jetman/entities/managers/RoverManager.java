
package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.characters.Rover;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.MarkerTile;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.utils.logging.StopWatch;

import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess", "unused"})
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
            SimpleVec2 roverPos = getStartPosition();

            super.create
                (
                    GameAssets._ROVER_ASSET,
                    GameAssets._ROVER_FRAMES,
                    Animation.PlayMode.NORMAL,
                    roverPos.x,
                    roverPos.y
                );

            Rover rover = new Rover(app);
            rover.initialise(entityDescriptor);
            rover.addPartners();
            app.entityData.addEntity(rover);

//            if (Settings.areGameHintsActive(app))
//            {
//                EntityDescriptor descriptor   = new EntityDescriptor();
//                descriptor._ASSET             = app.assets.getAnimationsAtlas().findRegion("enter_rover");
//                descriptor._FRAMES            = 1;
//                descriptor._PLAYMODE          = Animation.PlayMode.NORMAL;
//                descriptor._ENEMY             = false;
//                descriptor._X                 += 2;
//                descriptor._Y                 += 6;
//                descriptor._Z                 = Gfx._MAXIMUM_Z_DEPTH;
//                descriptor._INDEX             = app.entityData.entityMap.size;
//
//                InfoBox infoBox = new InfoBox(app);
//                infoBox.initialise(descriptor);
//
//                app.entityData.addEntity(infoBox);
//            }

            app.entityManager._roverIndex = rover.spriteNumber;

            roverCount++;
        }
    }

    public SimpleVec2 getStartPosition()
    {
        SimpleVec2 position = new SimpleVec2();

        for (MarkerTile tile : app.mapCreator.placementTiles)
        {
            if (tile._GID.equals(GraphicID.G_ROVER))
            {
                position.set(tile._X, tile._Y);
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
            int markerX = (((Gfx.getMapWidth() / 2) + Gfx._VIEW_WIDTH) / Gfx.getTileWidth())
                        + MathUtils.random(10, 20);

            if (isValidPosition(markerX))
            {
                EntityDescriptor descriptor = new EntityDescriptor();
                descriptor._ASSET             = app.assets.getAnimationRegion(GameAssets._ROVER_GUN_ASSET);
                descriptor._FRAMES            = GameAssets._ROVER_GUN_FRAMES;
                descriptor._PLAYMODE          = Animation.PlayMode.NORMAL;
                descriptor._X                 = markerX;
                descriptor._Y                 = app.entityManager.playerManager.playerTileY;
                descriptor._Z                 = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER_GUN);
                descriptor._INDEX             = app.entityData.entityMap.size;

//                RoverGun roverGun = new RoverGun(app);
//                roverGun.initialise(descriptor);
//
//                app.entityData.addEntity(roverGun);
//
//                app.entityManager._roverGunIndex = roverGun.spriteNumber;
//
//                roverGun.addTurret();

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

//        if (app.getGun() != null)
//        {
//            if (app.getGun().gunTurret != null)
//            {
//                count++;
//            }
//        }

        return count;
    }
}
