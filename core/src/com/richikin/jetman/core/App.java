package com.richikin.jetman.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.richikin.jetman.assets.Assets;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.entities.EntityData;
import com.richikin.jetman.entities.EntityManager;
import com.richikin.jetman.entities.EntityUtils;
import com.richikin.jetman.entities.characters.MainPlayer;
import com.richikin.jetman.entities.characters.Rover;
import com.richikin.jetman.entities.managers.MissileBaseManager;
import com.richikin.jetman.entities.paths.PathUtils;
import com.richikin.jetman.graphics.camera.CameraUtils;
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
import com.richikin.jetman.utils.Developer;
import com.richikin.jetman.utils.google.AdsController;
import com.richikin.jetman.utils.google.PlayServices;

public abstract class App extends com.badlogic.gdx.Game
{
    // =======================================================
    // Global access references
    //
    public SpriteBatch   spriteBatch;
    public Stage         stage;
    public WorldModel    worldModel;
    public Assets        assets;
    public BaseRenderer  baseRenderer;
    public CameraUtils   cameraUtils;
    public AdsController adsController;
    public PlayServices  googleServices;
    public Settings      settings;
    public InputManager  inputManager;
    public StateManager  appState;

    // TODO: 08/08/2020 - Can these be moved into a SceneManager class?
    public MainMenuScreen mainMenuScreen;
    public MainGameScreen mainGameScreen;

    //
    // Globals to be made available when MainGameScreen is active.
    // These must be released when MainGameScreen is destroyed.
    public LevelManager       levelManager;
    public EntityData         entityData;
    public EntityUtils        entityUtils;
    public MapData            mapData;
    public HeadsUpDisplay     hud;
    public RoomManager        roomManager;
    public EntityManager      entityManager;
    public GameProgress       gameProgress;
    public MapUtils           mapUtils;
    public PanelManager       panelManager;
    public MapCreator         mapCreator;
    public PathUtils          pathUtils;
    public MissileBaseManager missileBaseManager;

    public MainPlayer getPlayer()
    {
        MainPlayer player = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.size > 0)
            && (entityData.entityMap.get(entityManager._playerIndex) != null)
            && (entityData.entityMap.get(entityManager._playerIndex) instanceof MainPlayer))
        {
            player = ((MainPlayer) entityData.entityMap.get(entityManager._playerIndex));
        }

        return player;
    }

    public Rover getRover()
    {
        Rover rover = null;

        if ((entityData.entityMap != null)
            && (entityData.entityMap.get(entityManager._roverIndex) != null)
            && (entityData.entityMap.get(entityManager._roverIndex) instanceof Rover))
        {
            rover = ((Rover) entityData.entityMap.get(entityManager._roverIndex));
        }

        return rover;
    }

    /**
     * Returns the current number of lives left. This is done via
     * a method so that _GOD_MODE can return _MAX_LIVES.
     *
     * @return the lives.
     */
    public int getLives()
    {
        int lives;

        if (Developer.isDevMode() && settings.isEnabled(Settings._GOD_MODE))
        {
            lives = GameConstants._MAX_LIVES;
        }
        else
        {
            lives = gameProgress.lives.getTotal();
        }

        return lives;
    }

    /**
     * Returns the currently active game level.
     *
     * @return the level
     */
    public int getLevel()
    {
        return gameProgress.playerLevel;
    }

    public HeadsUpDisplay getHud()
    {
        return hud;
    }
}
