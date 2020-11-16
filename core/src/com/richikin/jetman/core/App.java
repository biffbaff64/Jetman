package com.richikin.jetman.core;

import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.entities.EntityData;
import com.richikin.jetman.entities.EntityManager;
import com.richikin.jetman.entities.EntityUtils;
import com.richikin.jetman.entities.characters.*;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.entities.managers.*;
import com.richikin.jetman.entities.paths.PathUtils;
import com.richikin.jetman.graphics.CameraUtils;
import com.richikin.jetman.graphics.parallax.ParallaxManager;
import com.richikin.jetman.graphics.renderers.BaseRenderer;
import com.richikin.jetman.input.InputManager;
import com.richikin.jetman.maps.MapCreator;
import com.richikin.jetman.maps.MapData;
import com.richikin.jetman.maps.MapUtils;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.jetman.screens.MainGameScreen;
import com.richikin.jetman.screens.MainMenuScreen;
import com.richikin.jetman.ui.HeadsUpDisplay;
import com.richikin.jetman.ui.PanelManager;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.core.ISettings;

// TODO: 16/11/2020
public final class App extends LibApp
{
    // =======================================================
    // Global access references
    //
    public static MainGame       mainGame;
    public static BaseRenderer   baseRenderer;
    public static CameraUtils    cameraUtils;
    public static WorldModel     worldModel;
    public static InputManager   inputManager;
    public static MainMenuScreen mainMenuScreen;
    public static MainGameScreen mainGameScreen;

    //
    // Globals to be made available when MainGameScreen is active.
    // These must be released when MainGameScreen is destroyed.
    public static EntityUtils           entityUtils;
    public static MapUtils              mapUtils;
    public static PathUtils             pathUtils;
    public static EntityData            entityData;
    public static MapData               mapData;
    public static HeadsUpDisplay        hud;
    public static GameProgress          gameProgress;
    public static MapCreator            mapCreator;
    public static ParallaxManager       parallaxManager;
    public static PanelManager          panelManager;
    public static LevelManager          levelManager;
    public static RoomManager           roomManager;
    public static EntityManager         entityManager;
    public static RoverManager          roverManager;
    public static TeleportManager       teleportManager;
    public static MissileBaseManager    missileBaseManager;
    public static DefenceStationManager defenceStationManager;
    public static BombManager           bombManager;

    public static boolean optionsPageActive;

    private App() {}

    public static void initialise()
    {
    }

    public static MainPlayer getPlayer()
    {
        MainPlayer player = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.size > 0)
            && (entityData.entityMap.get(entityManager._playerIndex) != null)
            && (entityData.entityMap.get(entityManager._playerIndex) instanceof MainPlayer))
        {
            // TODO: 16/11/2020
            player = ((MainPlayer) entityData.entityMap.get(entityManager._playerIndex));
        }

        return player;
    }

    public static Rover getRover()
    {
        Rover rover = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.get(entityManager._roverIndex) != null)
            && (entityData.entityMap.get(entityManager._roverIndex) instanceof Rover))
        {
            // TODO: 16/11/2020
            rover = ((Rover) entityData.entityMap.get(entityManager._roverIndex));
        }

        return rover;
    }

    public static boolean doesRoverExist()
    {
        return ((roverManager != null)
            && (roverManager.getGID() == GraphicID.G_ROVER)
            && (roverManager.getActiveCount() > 0));
    }

    public static RoverGun getGun()
    {
        RoverGun gun = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.get(entityManager._roverGunIndex) != null)
            && (entityData.entityMap.get(entityManager._roverGunIndex) instanceof RoverGun))
        {
            // TODO: 16/11/2020
            gun = ((RoverGun) entityData.entityMap.get(entityManager._roverGunIndex));
        }

        return gun;
    }

    public static Bomb getBomb()
    {
        Bomb bomb = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.get(entityManager._bombIndex) != null)
            && (entityData.entityMap.get(entityManager._bombIndex) instanceof Bomb))
        {
            // TODO: 16/11/2020
            bomb = ((Bomb) entityData.entityMap.get(entityManager._bombIndex));
        }

        return bomb;
    }

    public static MissileBase getBase()
    {
        MissileBase base = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.get(entityManager._missileBaseIndex) != null)
            && (entityData.entityMap.get(entityManager._missileBaseIndex) instanceof MissileBase))
        {
            // TODO: 16/11/2020
            base = ((MissileBase) entityData.entityMap.get(entityManager._missileBaseIndex));
        }

        return base;
    }

    public static Teleporter getTeleporter(int index)
    {
        Teleporter teleporter = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.get(entityManager._teleportIndex[index]) != null)
            && (entityData.entityMap.get(entityManager._teleportIndex[index]).gid == GraphicID.G_TRANSPORTER))
        {
            // TODO: 16/11/2020
            teleporter = (Teleporter) entityData.entityMap.get(entityManager._teleportIndex[index]);
        }

        return teleporter;
    }

    /**
     * Returns the current number of lives left. This is done via
     * a method so that _GOD_MODE can return _MAX_LIVES.
     *
     * @return the lives.
     */
    public static int getLives()
    {
        int lives;

        if (Developer.isDevMode() && Developer.isGodMode())
        {
            lives = GameConstants._MAX_LIVES;
        }
        else
        {
            lives = gameProgress.getLives().getTotal();
        }

        return lives;
    }

    /**
     * Returns the currently active game level.
     *
     * @return the level
     */
    public static int getLevel()
    {
        return gameProgress.playerLevel;
    }

    public static boolean doTransportersExist()
    {
        return ((teleportManager != null) && (teleportManager.getActiveCount() > 0));
    }

    public static HeadsUpDisplay getHud()
    {
        return hud;
    }

    public static com.badlogic.gdx.Preferences getPrefs()
    {
        return settings.getPrefs();
    }
}
