package com.richikin.jetman.core;

import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.entities.EntityManager;
import com.richikin.jetman.entities.EntityUtils;
import com.richikin.jetman.entities.paths.PathUtils;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.jetman.physics.aabb.AABBData;
import com.richikin.jetman.physics.CollisionUtils;
import com.richikin.jetman.ui.HeadsUpDisplay;
import com.richikin.utilslib.logging.Trace;

public class LevelManager
{
    private boolean isFirstTime;

    public LevelManager()
    {
        isFirstTime = true;
    }

    /**
     * Gets the current level ready for playing.
     *
     * @param _firstTime TRUE if first call from power-up.
     */
    public void prepareCurrentLevel(boolean _firstTime)
    {
        if (App.gameProgress.isRestarting)
        {
            restartCurrentLevel();
        }
        else if (_firstTime || App.gameProgress.levelCompleted)
        {
            setupForNewLevel(_firstTime);

            App.gameProgress.resetProgress();
        }

        AppConfig.gamePaused     = false;
        AppConfig.quitToMainMenu = false;
        AppConfig.forceQuitToMenu = false;

        App.gameProgress.isRestarting   = false;
        App.gameProgress.levelCompleted = false;
        App.gameProgress.playerGameOver = false;

        //
        // Centre the camera on the player
        if (App.getPlayer() != null)
        {
            App.mapUtils.positionAt((int) App.getPlayer().sprite.getX(), (int) App.getPlayer().sprite.getY());
        }

        //
        // Reset the bars. Each level must start with full fuel and maximum time.
        App.getHud().getFuelBar().setToMaximum();
        App.getHud().getTimeBar().setToMaximum();
        App.getHud().update();

        //
        // The player is rewarded with an extra life every 4th level.
        if (App.gameProgress.getLives().getTotal() < GameConstants._MAX_LIVES)
        {
            if ((App.gameProgress.playerLevel % 4) == 0)
            {
                App.gameProgress.getLives().add(1);
            }
        }

        //
        // Reset the bars. Each level must start with full fuel and maximum time.
        if (_firstTime)
        {
            App.getHud().refillItems();
            App.getHud().update();
        }
    }

    /**
     * Set up the current level ready for starting.
     */
    public void setupForNewLevel(boolean firstTime)
    {
        Trace.__FILE_FUNC();

        App.collisionUtils.initialise();
        App.mapData.initialiseRoom();               // Load tiled map and create renderer
        App.mapCreator.createMap();                 // Process the tiled map data

        App.entityManager.initialiseForLevel();

        //
        // Create entity paths if any relevant data
        // exists in the tilemap data.
        App.pathUtils = new PathUtils();
        App.pathUtils.setup();

        Trace.finishedMessage();
    }

    public void restartCurrentLevel()
    {
        //
        // Reset positions etc.
        App.entityUtils.resetAllPositions();

        App.getPlayer().setup(false);

        if (App.gameProgress.levelCompleted)
        {
            App.entityManager.updateIndexes();
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
        for (int i=0; i< App.entityData.entityMap.size; i++)
        {
            if (App.entityData.entityMap.get(i).gid != GraphicID.G_PLAYER)
            {
                App.entityData.entityMap.removeIndex(i);
            }
        }

        App.mapData.placementTiles.clear();
        App.mapData.enemyFreeZones.clear();
        App.mapData.currentMap.dispose();
    }

    /**
     * Set up everything for a NEW game only.
     */
    public void prepareNewGame()
    {
        if (isFirstTime)
        {
            //
            // Make sure all progress counters are initialised.
            App.gameProgress.resetProgress();

            //
            // Initialise the room that the game will start in.
            App.roomManager = new RoomManager();
            App.roomManager.initialise();

            //
            // Create collision and entity controllers.
            App.collisionUtils = new CollisionUtils();
            App.entityUtils    = new EntityUtils();
            App.entityManager  = new EntityManager();
            App.hud            = new HeadsUpDisplay();

            App.cameraUtils.disableAllCameras();
            App.baseRenderer.hudGameCamera.isInUse = true;
            App.baseRenderer.isDrawingStage        = true;

            App.entityData.createData();
            AABBData.createData();
            App.entityManager.initialise();
            App.mapData.update();
            App.getHud().createHud();
        }

        isFirstTime = false;
    }
}
