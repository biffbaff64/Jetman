package com.richikin.jetman.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.structures.Teleporter;
import com.richikin.jetman.entities.managers.AlienManager;
import com.richikin.jetman.entities.managers.BackgroundObjectsManager;
import com.richikin.jetman.entities.managers.BarrierManager;
import com.richikin.jetman.entities.managers.BombManager;
import com.richikin.jetman.entities.managers.DefenceStationManager;
import com.richikin.jetman.entities.managers.MissileBaseManager;
import com.richikin.jetman.entities.managers.PlayerManager;
import com.richikin.jetman.entities.managers.RoverManager;
import com.richikin.jetman.entities.managers.TeleportManager;
import com.richikin.jetman.entities.components.IEntityManagerComponent;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.IEntityManager;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.jetman.entities.systems.RenderSystem;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.utilslib.logging.Trace;

public class EntityManager implements IEntityManager
{
    // --------------------------------------------------
    //
    public static final Array<GraphicID> enemies;

    static
        {
            enemies = new Array<>();

            enemies.add(GraphicID.G_3BALLS_UFO);
            enemies.add(GraphicID.G_3LEGS_ALIEN);
            enemies.add(GraphicID.G_ASTEROID);
            enemies.add(GraphicID.G_ALIEN_WHEEL);
            enemies.add(GraphicID.G_BLOB);
            enemies.add(GraphicID.G_DOG);
            enemies.add(GraphicID.G_GREEN_BLOCK);
            enemies.add(GraphicID.G_SPINNING_BALL);
            enemies.add(GraphicID.G_STAIR_CLIMBER);
            enemies.add(GraphicID.G_STAR_SPINNER);
            enemies.add(GraphicID.G_TOPSPIN);
            enemies.add(GraphicID.G_TWINKLES);
        }

    // --------------------------------------------------
    // Indexes into manager list
    public int _bombManagerIndex;
    public int _alienManagerIndex;

    // --------------------------------------------------
    // Indexes into entity list
    public int   _playerIndex;
    public int[] _teleportIndex;

    public TeleportBeam  teleportBeam;
    public PlayerManager playerManager;
    public RenderSystem  renderSystem;

    public boolean _playerReady;

    public EntityManager()
    {
        this.renderSystem   = new RenderSystem();
        this._teleportIndex = new int[RoomManager._MAX_TELEPORTERS];
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        App.roverManager            = new RoverManager();
        App.teleportManager         = new TeleportManager();
        App.missileBaseManager      = new MissileBaseManager();
        App.defenceStationManager   = new DefenceStationManager();
        App.bombManager             = new BombManager();

//        _bombManagerIndex = App.entityData.addManager(App.bombManager);
//        _alienManagerIndex = App.entityData.addManager(new AlienManager());
    }

    @Override
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !AppConfig.gamePaused)
        {
            GdxSprite entity;

            //
            // Update all non-player entities.
            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) App.entityData.entityMap.get(i);

                if ((entity.getAction() != ActionStates._DEAD) && (entity.gid != GraphicID.G_PLAYER))
                {
                    entity.preUpdate();
                    entity.update(entity.spriteNumber);
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if (_playerReady && (App.getPlayer().getAction() != ActionStates._DEAD))
            {
                App.getPlayer().preUpdate();
                App.getPlayer().update(_playerIndex);
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!App.gameProgress.levelCompleted && !App.gameProgress.baseDestroyed)
            {
                for (final IEntityManagerComponent system : App.entityData.managerList)
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
        if (App.entityData.entityMap != null)
        {
            GdxSprite entity;

            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) App.entityData.entityMap.get(i);

                if (entity != null)
                {
                    if (entity.getAction() != ActionStates._DEAD)
                    {
                        entity.postUpdate(i);
                    }

                    //
                    // NB: entity might have died in postUpdate, which is
                    // why this next if() is not an else.

                    if (entity.getAction() == ActionStates._DEAD)
                    {
                        switch (entity.gid)
                        {
                            case G_PLAYER:
                            case G_BACKGROUND_UFO:
                            case G_TWINKLE_STAR:
                            case _CEILING:
                            case _CRATER:
                            case _GROUND:
                            {
                            }
                            break;

                            case G_TRANSPORTER:
                            case G_MISSILE:
                            case G_MISSILE_BASE:
                            case G_DEFENDER:
                            case G_ROVER:
                            case G_ROVER_BULLET:
                            case G_ROVER_WHEEL:
                            case G_DEFENDER_BULLET:
                            case G_UFO_BULLET:
                            case G_EXPLOSION12:
                            case G_EXPLOSION64:
                            case G_EXPLOSION128:
                            case G_EXPLOSION256:
                            {
                                entity.tidy(i);
                            }
                            break;

                            case G_BOMB:
                            case G_ROVER_GUN:
                            case G_ROVER_GUN_BARREL:
                            case G_ASTEROID:
                            default:
                            {
                                if (entity.gid != GraphicID.G_NO_ID)
                                {
                                    releaseEntity(entity);

                                    entity.collisionObject.kill();
                                    App.entityData.removeEntity(i);
                                }
                            }
                            break;
                        }

                        updateIndexes();
                    }
                }
            }

            App.collisionUtils.tidy();
        }
    }

    /**
     * Draw all game entities
     */
    @Override
    public void drawSprites()
    {
        if (renderSystem != null)
        {
            renderSystem.drawTeleportBeams(teleportBeam);
            renderSystem.drawSprites();
        }
    }

    @Override
    public void releaseEntity(GdxSprite entity)
    {
        switch (entity.gid)
        {
            case G_TRANSPORTER:
            {
                _teleportIndex[((Teleporter) entity).teleporterNumber] = 0;
            }
            break;

            case G_MISSILE_BASE:
            {
                App.missileBaseManager.free();
            }
            break;

            case G_BOMB:
            case G_DEFENDER:
            case G_MISSILE:
            case G_ROVER:
            {
            }
            break;

            default:
            {
                for (GraphicID gid : enemies)
                {
                    if (gid == entity.gid)
                    {
                        App.entityData.managerList.get(_alienManagerIndex).free(gid);
                    }
                }
            }
            break;
        }
    }

    public boolean doesRoverExist()
    {
        return ((App.roverManager != null)
            && (App.roverManager.getGID() == GraphicID.G_ROVER)
            && (App.roverManager.getActiveCount() > 0));
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
        _teleportIndex[0] = 0;
        _teleportIndex[1] = 0;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            entity = (GdxSprite) App.entityData.entityMap.get(i);

            if (entity != null)
            {
                if (entity.gid == GraphicID.G_TRANSPORTER)
                {
                    _teleportIndex[((Teleporter) entity).teleporterNumber] = i;
                }
                else
                {
                    if (entity.gid == GraphicID.G_PLAYER)
                    {
                        _playerIndex = i;
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

    public void initialiseForLevel()
    {
        Trace.__FILE_FUNC();

        AppConfig.entitiesExist = false;

//        playerManager = new PlayerManager();
//        playerManager.setSpawnPoint();
//        playerManager.createPlayer();

//        addBackgroundEntities();

//        App.roverManager.init();
//        App.teleportManager.init();
//        App.missileBaseManager.init();
//        App.defenceStationManager.init();

//        for (final IEntityManagerComponent system : App.entityData.managerList)
//        {
//            system.init();
//        }

//        App.entities.setAllEnemyStatuses();

//        AppConfig.entitiesExist = true;
    }

    /**
     * Background entities which are essentially just
     * decorations, such as ufos and twinkling stars.
     */
    private void addBackgroundEntities()
    {
        // --------------------------------------------------
        //
        BackgroundObjectsManager backgroundObjectsManager = new BackgroundObjectsManager();
        backgroundObjectsManager.addUFOs(2 + MathUtils.random(2));
        backgroundObjectsManager.addTwinkleStars();

        BarrierManager barrierManager = new BarrierManager();
        barrierManager.init();
        barrierManager.create();
    }

    @Override
    public void dispose()
    {
        App.entityData.dispose();
    }
}
