package com.richikin.jetman.entities;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.config.Settings;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.characters.Teleporter;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.entities.components.EntityManagerComponent;
import com.richikin.jetman.entities.managers.*;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.utilslib.entities.IEntityManager;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.jetman.entities.systems.RenderSystem;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class EntityManager implements IEntityManager
{
    // --------------------------------------------------
    //
    public final GraphicID[] enemies =
        {
            GraphicID.G_3BALLS_UFO,
            GraphicID.G_3LEGS_ALIEN,
            GraphicID.G_ASTEROID,
            GraphicID.G_ALIEN_WHEEL,
            GraphicID.G_BLOB,
            GraphicID.G_DOG,
            GraphicID.G_GREEN_BLOCK,
            GraphicID.G_SPINNING_BALL,
            GraphicID.G_STAIR_CLIMBER,
            GraphicID.G_STAR_SPINNER,
            GraphicID.G_TOPSPIN,
            GraphicID.G_TWINKLES,
        };

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

        initialiseManagerList();
    }

    @Override
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !AppSystem.gamePaused)
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
            if (!App.settings.isEnabled(Settings._SCROLL_DEMO))
            {
                if (_playerReady && (App.getPlayer().getAction() != ActionStates._DEAD))
                {
                    App.getPlayer().preUpdate();
                    App.getPlayer().update(_playerIndex);
                }
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!App.gameProgress.levelCompleted && !App.gameProgress.baseDestroyed)
            {
                for (final EntityManagerComponent system : App.entityData.managerList)
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
        return (AppSystem.entitiesExist && !AppSystem.quitToMainMenu);
    }

    private void initialiseManagerList()
    {
        Trace.__FILE_FUNC();

        App.roverManager            = new RoverManager();
        App.teleportManager         = new TeleportManager();
        App.missileBaseManager      = new MissileBaseManager();
        App.defenceStationManager   = new DefenceStationManager();
        App.bombManager             = new BombManager();

        _bombManagerIndex = App.entityData.addManager(App.bombManager);
        _alienManagerIndex = App.entityData.addManager(new AlienManager());
    }

    public void initialiseForLevel()
    {
        Trace.__FILE_FUNC();

        AppSystem.entitiesExist = false;

        playerManager = new PlayerManager();
        playerManager.setSpawnPoint();
        playerManager.createPlayer();

        addBackgroundEntities();

        App.roverManager.init();
        App.teleportManager.init();
        App.missileBaseManager.init();
        App.defenceStationManager.init();

        for (final EntityManagerComponent system : App.entityData.managerList)
        {
            system.init();
        }

        App.entities.setAllEnemyStatuses();

        AppSystem.entitiesExist = true;
    }

    /**
     * Background entities which are essentially just
     * decorations, such as ufos and twinkling stars.
     */
    public void addBackgroundEntities()
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
