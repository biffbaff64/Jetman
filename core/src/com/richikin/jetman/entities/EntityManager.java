package com.richikin.jetman.entities;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.characters.Teleporter;
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
    public int _laserManagerIndex;
    public int _bombManagerIndex;
    public int _alienManagerIndex;

    // --------------------------------------------------
    // Indexes into entity list
    public int   _playerIndex;
    public int   _roverIndex;
    public int   _roverGunIndex;
    public int   _bombIndex;
    public int   _missileBaseIndex;
    public int[] _teleportIndex;

    public TeleportBeam  teleportBeam;
    public PlayerManager playerManager;
    public RenderSystem  renderSystem;

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

        initialiseManagerList();
    }

    @Override
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !AppConfig.gamePaused)
        {
            GdxSprite entity;

            //
            // Update all non-player entities.
            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                entity = (GdxSprite) app.entityData.entityMap.get(i);

                if ((entity.getAction() != ActionStates._DEAD)
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
                if (_playerReady && (app.getPlayer().getAction() != ActionStates._DEAD))
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
                            case G_ASTEROID:
                            default:
                            {
                                if (entity.gid != GraphicID.G_NO_ID)
                                {
                                    releaseEntity(entity);

                                    entity.collisionObject.kill();
                                    app.entityData.removeEntity(i);
                                }
                            }
                            break;
                        }

                        updateIndexes();
                    }
                }
            }

            app.collisionUtils.tidy();
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
                app.missileBaseManager.free();
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
                        app.entityData.managerList.get(_alienManagerIndex).free(gid);
                    }
                }
            }
            break;
        }
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
                    _teleportIndex[((Teleporter) entity).teleporterNumber] = i;
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

    private void initialiseManagerList()
    {
        Trace.__FILE_FUNC();

        app.roverManager            = new RoverManager(app);
        app.teleportManager         = new TeleportManager(app);
        app.missileBaseManager      = new MissileBaseManager(app);
        app.defenceStationManager   = new DefenceStationManager(app);
        app.bombManager             = new BombManager(app);

        _bombManagerIndex = app.entityData.addManager(app.bombManager);
        _alienManagerIndex = app.entityData.addManager(new AlienManager(app));
    }

    public void initialiseForLevel()
    {
        Trace.__FILE_FUNC();

        AppConfig.entitiesExist = false;

        playerManager = new PlayerManager(app);
        playerManager.setSpawnPoint();
        playerManager.createPlayer();

        app.roverManager.init();
        app.teleportManager.init();
        app.missileBaseManager.init();
        app.defenceStationManager.init();

        for (final EntityManagerComponent system : app.entityData.managerList)
        {
            system.init();
        }

        AppConfig.entitiesExist = true;
    }

    public void addBackgroundEntities()
    {
        BackgroundObjectsManager manager = new BackgroundObjectsManager(app);
        manager.addUFOs(6 + MathUtils.random(4));
        manager.addTwinkleStars();
    }

    @Override
    public void dispose()
    {
    }
}
