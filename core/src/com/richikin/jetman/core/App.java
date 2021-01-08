package com.richikin.jetman.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.assets.AssetLoader;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.EntityData;
import com.richikin.jetman.entities.EntityManager;
import com.richikin.jetman.entities.EntityUtils;
import com.richikin.jetman.entities.characters.*;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.entities.managers.*;
import com.richikin.jetman.entities.paths.PathUtils;
import com.richikin.jetman.entities.structures.MissileBase;
import com.richikin.jetman.entities.structures.Teleporter;
import com.richikin.jetman.graphics.CameraUtils;
import com.richikin.jetman.graphics.IAssets;
import com.richikin.jetman.graphics.parallax.ParallaxManager;
import com.richikin.jetman.graphics.renderers.BaseRenderer;
import com.richikin.jetman.input.InputManager;
import com.richikin.jetman.maps.MapCreator;
import com.richikin.jetman.maps.MapData;
import com.richikin.jetman.maps.MapUtils;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.jetman.screens.MainGameScreen;
import com.richikin.jetman.screens.MainMenuScreen;
import com.richikin.jetman.ui.HeadsUpDisplay;
import com.richikin.jetman.ui.PanelManager;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.logging.StateManager;

// TODO: 16/11/2020
public final class App extends LibApp
{
    // =======================================================
    // Global access references
    //
    public static MainGame        mainGame;
    public static BaseRenderer    baseRenderer;
    public static CameraUtils     cameraUtils;
    public static WorldModel      worldModel;
    public static InputManager    inputManager;
    public static MainMenuScreen  mainMenuScreen;
    public static MainGameScreen  mainGameScreen;
    public static IAssets         assets;
    public static ISettings       settings;
    public static HighScoreUtils  highScoreUtils;
    public static ParallaxManager parallaxManager;
    public static PanelManager    panelManager;
    public static LevelManager    levelManager;
    public static RoomManager     roomManager;

    //
    // Globals to be made available when MainGameScreen is active.
    // These must be released when MainGameScreen is destroyed.
    public static CollisionUtils collisionUtils;
    public static EntityUtils    entityUtils;
    public static Entities       entities;
    public static MapUtils       mapUtils;
    public static PathUtils      pathUtils;
    public static EntityData     entityData;
    public static MapData        mapData;
    public static HeadsUpDisplay hud;
    public static GameProgress   gameProgress;
    public static MapCreator     mapCreator;

    public static EntityManager         entityManager;
    public static RoverManager          roverManager;
    public static TeleportManager       teleportManager;
    public static MissileBaseManager    missileBaseManager;
    public static DefenceStationManager defenceStationManager;
    public static BombManager           bombManager;

    private App()
    {
    }

    public static void initialiseObjects()
    {
        appState = new StateManager(StateID._STATE_POWER_UP);

        settings     = new Settings();
        assets       = new AssetLoader();
        spriteBatch  = new SpriteBatch();
        cameraUtils  = new CameraUtils();
        worldModel   = new WorldModel();
        baseRenderer = new BaseRenderer();

        //
        // This needs setting here as InputManager needs access to it.
        stage = new Stage(baseRenderer.hudGameCamera.viewport, spriteBatch);

        inputManager   = new InputManager();
        panelManager   = new PanelManager();
        highScoreUtils = new HighScoreUtils();

        //
        // TODO: 19/08/2020
        // These objects should really be initialised when moving
        // from MainMenuScreen to MainGameScreen.
        mapCreator     = new MapCreator();
        entityData     = new EntityData();
        entities       = new Entities();
        mapData        = new MapData();
        mapUtils       = new MapUtils();
        gameProgress   = new GameProgress();
        mainMenuScreen = new MainMenuScreen();
        mainGameScreen = new MainGameScreen();
    }

    public static MainPlayer getPlayer()
    {
        return entities.mainPlayer;
    }

    public static Rover getRover()
    {
        return entities.rover;
    }

    public static RoverGun getGun()
    {
        return entities.roverGun;
    }

    public static Bomb getBomb()
    {
        return entities.bomb;
    }

    public static MissileBase getBase()
    {
        return entities.missileBase;
    }

    public static Teleporter getTeleporter(int index)
    {
        return entities.teleporters[index];
    }

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
}
