package com.richikin.jetman.core;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.entities.EntityManager;
import com.richikin.jetman.entities.EntityUtils;
import com.richikin.jetman.entities.paths.PathUtils;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.jetman.physics.aabb.AABBData;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.jetman.ui.HeadsUpDisplay;
import com.richikin.jetman.utils.logging.Trace;

public class LevelManager
{
    private       boolean isFirstTime;
    private final App     app;

    public LevelManager(App _app)
    {
        app         = _app;
        isFirstTime = true;
    }

    /**
     * Gets the current level ready for playing.
     *
     * @param _firstTime TRUE if first call from power-up.
     */
    public void prepareCurrentLevel(boolean _firstTime)
    {
        if (app.gameProgress.isRestarting)
        {
            restartCurrentLevel();
        }
        else if (_firstTime || app.gameProgress.levelCompleted)
        {
            setupForNewLevel(_firstTime);

            app.gameProgress.resetProgress();
        }

        AppConfig.gamePaused      = false;
        AppConfig.quitToMainMenu  = false;
        AppConfig.forceQuitToMenu = false;

        app.gameProgress.isRestarting   = false;
        app.gameProgress.levelCompleted = false;
        app.gameProgress.playerGameOver = false;

        //
        // Centre the camera on the player
        if (app.getPlayer() != null)
        {
            app.mapUtils.positionAt((int) app.getPlayer().sprite.getX(), (int) app.getPlayer().sprite.getY());
        }

        //
        // Reset the bars. Each level must start with full fuel and maximum time.
        app.getHud().getFuelBar().setToMaximum();
        app.getHud().getTimeBar().setToMaximum();
        app.getHud().update();

        //
        // The player is rewarded with an extra life every 4th level.
        if (app.gameProgress.lives.getTotal() < GameConstants._MAX_LIVES)
        {
            if ((app.gameProgress.playerLevel % 4) == 0)
            {
                app.gameProgress.lives.add(1);
            }
        }

        //
        // Reset the bars. Each level must start with full fuel and maximum time.
        if (_firstTime)
        {
            app.getHud().refillItems();
            app.getHud().update();
        }
    }

    /**
     * Set up the current level ready for starting.
     */
    public void setupForNewLevel(boolean firstTime)
    {
        Trace.__FILE_FUNC();

        app.collisionUtils.initialise();
        app.mapData.initialiseRoom();               // Load tiled map and create renderer
        app.mapCreator.createMap();                 // Process the tiled map data

        app.entityManager.initialiseForLevel();

        //
        // Create entity paths if any relevant data
        // exists in the tilemap data.
        app.pathUtils = new PathUtils();
        app.pathUtils.setup(app);

        Trace.finishedMessage();
    }

    public void restartCurrentLevel()
    {
        //
        // Reset positions etc.
        app.entityUtils.resetAllPositions();

        app.getPlayer().setup(false);

        if (app.gameProgress.levelCompleted)
        {
            app.entityManager.updateIndexes();
        }
    }

    /**
     * Actions to perform when a level
     * has been completed.
     * <p>
     * Remove all entities/pickups/etc from the level, but
     * make sure that the main player is untouched.
     */
    public void closeCurrentLevel()
    {
        // TODO: 12/08/2020 - Change this so that it removes all entities that are not MainPlayer
        app.entityData.entityMap.setSize(1);
        app.mapData.placementTiles.clear();

        app.mapData.enemyFreeZones.clear();
        app.mapData.currentMap.dispose();
    }

    /**
     * Set up everything for a NEW game only.
     */
    public void prepareNewGame()
    {
        if (isFirstTime)
        {
            //
            // Initialise the room that the game will start in.
            app.roomManager = new RoomManager(app);
            app.roomManager.initialise();

            //
            // Make sure all progress counters are initialised.
            app.gameProgress.resetProgress();

            //
            // Create collision and entity controllers.
            app.collisionUtils = new CollisionUtils(app);
            app.entityUtils    = new EntityUtils(app);
            app.entityManager  = new EntityManager(app);
            app.hud            = new HeadsUpDisplay(app);

            app.cameraUtils.disableAllCameras();
            app.baseRenderer.hudGameCamera.isInUse = true;
            app.baseRenderer.isDrawingStage        = true;

            app.entityData.createData();
            AABBData.createData();
            app.entityManager.initialise();
            app.mapData.update();
            app.getHud().createHud();
        }

        isFirstTime = false;
    }
}
