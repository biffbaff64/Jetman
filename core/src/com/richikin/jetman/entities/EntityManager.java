package com.richikin.jetman.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.components.EntityManagerComponent;
import com.richikin.jetman.entities.managers.BackgroundObjectsManager;
import com.richikin.jetman.entities.managers.PlayerManager;
import com.richikin.jetman.entities.objects.IEntityManager;
import com.richikin.jetman.entities.rootobjects.GameEntity;
import com.richikin.jetman.entities.systems.RenderSystem;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.AABB.AABBData;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.Trace;

public class EntityManager implements IEntityManager
{
    //
    //
    public final GraphicID[] enemies =
        {
            GraphicID.G_3BALLS,
            GraphicID.G_3BALLS_UFO,
            GraphicID.G_UFO_BULLET,
            GraphicID.G_3LEGS_ALIEN,
            GraphicID.G_TOPSPIN,
            GraphicID.G_TWINKLES,
            GraphicID.G_STAR_SPINNER,
            GraphicID.G_ASTEROID,
            GraphicID.G_DOG,
            GraphicID.G_GREEN_BLOCK,
            GraphicID.G_SPINNING_BALL,
            GraphicID.G_BLOB,
            GraphicID.G_ALIEN_WHEEL,
        };

    //
    // Indexes into manager list
    public int _bombManagerIndex;
    public int _roverManagerIndex;
    public int _asteroidManagerIndex;
    public int _greenblockManagerIndex;
    public int _blobManagerIndex;
    public int _wheelManagerIndex;
    public int _dogManagerIndex;
    public int _spinningBallManagerIndex;
    public int _spinStarManagerIndex;
    public int _3ballsManagerIndex;
    public int _3ballsUFOManagerIndex;
    public int _3legsUFOManagerIndex;
    public int _topspinnerManagerIndex;
    public int _twinkleManagerIndex;

    //
    // Indexes into entity list
    public int   _playerIndex;
    public int   _roverIndex;
    public int   _roverGunIndex;
    public int   _bombIndex;
    public int   _missileBaseIndex;
    public int[] _teleportIndex;

    public RenderSystem renderSystem;

    public boolean _playerReady;

    private final App app;

    public EntityManager(App _app)
    {
        this.app = _app;

        this.renderSystem   = new RenderSystem(_app);
        this._teleportIndex = new int[2];
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !AppConfig.gamePaused)
        {
            GdxSprite entity;

            //
            // Update all non-player entities.
            // TODO: 14/05/2019 - Should Rover and connected entities be excluded here also?
            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) app.entityData.entityMap.get(i);

                if ((entity.getSpriteAction() != Actions._DEAD)
                    && (entity.gid != GraphicID.G_PLAYER))
                {
                    entity.preUpdate();
                    entity.update(entity.spriteNumber);
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if (!app.settings.isEnabled(Settings._SCROLL_DEMO))
            {
                if (_playerReady && (app.getPlayer().getSpriteAction() != Actions._DEAD))
                {
                    app.getPlayer().preUpdate();
                    app.getPlayer().update(_playerIndex);
                }
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!app.gameProgress.levelCompleted && !app.gameProgress.baseDestroyed)
            {
                for (final EntityManagerComponent system : app.entityData.managerList)
                {
                    system.update();
                }
            }
        }
    }

    /**
     * Entity Tidy actions.
     * These are actions performed at the end
     * of each update.
     */
    @Override
    public void tidySprites()
    {
        if (app.entityData.entityMap != null)
        {
            GdxSprite entity;

            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) app.entityData.entityMap.get(i);

                if (entity != null)
                {
                    if (entity.getSpriteAction() == Actions._DEAD)
                    {
                        switch (entity.gid)
                        {
                            case G_PLAYER:
                            case G_UFO:
                            case G_TWINKLE_STAR:
                            case _GROUND:
                            {
                            }
                            break;

                            case G_TRANSPORTER:
                            {
//                                _teleportIndex[((Teleporter) entity).teleporterNumber] = 0;
//
//                                entity.collisionObject.kill();
//                                app.entityData.removeMarkerTile(i);
//                                app.entityData.removeEntity(i);
                            }
                            break;

                            case G_MISSILE:
                            {
//                                if (!app.gameProgress.roverDestroyed
//                                    && !app.gameProgress.baseDestroyed
//                                    && (entity.direction.getY() != Movement._DIRECTION_UP))
//                                {
//                                    app.getHud().getTimeBar().setToMaximum();
//                                    app.getHud().getFuelBar().setToMaximum();
//                                    app.getHud().update();
//
//                                    app.getBase().spriteAction = Actions._STANDING;
//                                }
//
//                                entity.collisionObject.kill();
//                                app.entityData.removeMarkerTile(i);
//                                app.entityData.removeEntity(i);
                            }
                            break;

                            case G_BOMB:
                            case G_ROVER_GUN:
                            {
//                                releaseEntity(entity);
//
//                                entity.collisionObject.kill();
//                                app.entityData.removeMarkerTile(i);
//                                app.entityData.removeEntity(i);
                            }
                            break;

                            case G_ASTEROID:
                            {
//                                releaseEntity(entity);
//
//                                entity.collisionObject.kill();
//                                app.entityData.removeEntity(i);
                            }
                            break;

                            case G_MISSILE_BASE:
                            {
//                                app.missileBaseManager.free();
//
//                                entity.collisionObject.kill();
//                                app.entityData.removeMarkerTile(i);
//                                app.entityData.removeEntity(i);
                            }
                            break;

                            case G_DEFENDER:
                            case G_ROVER:
                            {
//                                entity.collisionObject.kill();
//                                app.entityData.removeMarkerTile(i);
//                                app.entityData.removeEntity(i);
                            }
                            break;

                            default:
                            {
//                                if ((entity.gid != GraphicID.G_NO_ID) && (entity.collisionObject != null))
//                                {
//                                    releaseEntity(entity);
//
//                                    entity.collisionObject.kill();
//                                    app.entityData.removeMarkerTile(i);
//                                    app.entityData.removeEntity(i);
//                                }
                            }
                            break;
                        }

                        updateIndexes();
                    }
                    else
                    {
                        entity.postUpdate(i);
                    }
                }
            }

            //            app.collisionUtils.tidy();
        }
    }

    /**
     * Draw all game entities
     */
    @Override
    public void drawSprites()
    {
        //        renderSystem.drawTeleportBeams(teleportBeam);
        renderSystem.drawSprites();
    }

    @Override
    public void releaseEntity(GdxSprite entity)
    {
    }

    /**
     * Update the indexes into the entity map
     * for the main entities
     */
    @Override
    public void updateIndexes()
    {
        GdxSprite entity;

        _playerIndex      = 0;
        _roverIndex       = 0;
        _roverGunIndex    = 0;
        _bombIndex        = 0;
        _missileBaseIndex = 0;
        _teleportIndex[0] = 0;
        _teleportIndex[1] = 0;

        for (int i = 0; i < app.entityData.entityMap.size; i++)
        {
            entity = (GdxSprite) app.entityData.entityMap.get(i);

            if (entity != null)
            {
                if (entity.gid == GraphicID.G_TRANSPORTER)
                {
                    //                    _teleportIndex[((Teleporter) entity).teleporterNumber] = i;
                }
                else
                {
                    switch (entity.gid)
                    {
                        case G_PLAYER:
                            _playerIndex = i;
                            break;
                        case G_ROVER:
                            _roverIndex = i;
                            break;
                        case G_ROVER_GUN:
                            _roverGunIndex = i;
                            break;
                        case G_BOMB:
                            _bombIndex = i;
                            break;
                        case G_MISSILE_BASE:
                            _missileBaseIndex = i;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public boolean isEntityUpdateAllowed()
    {
        return (AppConfig.entitiesExist && !AppConfig.quitToMainMenu);
    }

    public void initialiseManagerList()
    {
        Trace.__FILE_FUNC();
    }

    public void initialisePlayer()
    {
        PlayerManager playerManager = new PlayerManager(app);
        playerManager.setSpawnPoint();
        playerManager.createPlayer();
    }

    public void initialiseForLevel()
    {
        BackgroundObjectsManager manager = new BackgroundObjectsManager(app);

        manager.addUFOs(6 + MathUtils.random(4));
        manager.addTwinkleStars();

        if (AABBData.boxes().size == 0)
        {
            //            app.mapCreator.addDummyCollisionObject();
            initialisePlayer();
        }
    }

    @Override
    public void dispose()
    {
    }
}
