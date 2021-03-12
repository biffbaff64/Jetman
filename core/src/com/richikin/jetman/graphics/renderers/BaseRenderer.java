package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;
import com.richikin.jetman.graphics.camera.ViewportType;
import com.richikin.jetman.graphics.camera.Zoom;
import com.richikin.jetman.graphics.parallax.ParallaxBackground;
import com.richikin.jetman.graphics.parallax.ParallaxManager;
import com.richikin.jetman.screens.LoadingScreen;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec3F;

public class BaseRenderer implements Disposable
{
    private static final int PLAYER_ROVER_OFFSET = 130;

    public OrthoGameCamera hudGameCamera;
    public OrthoGameCamera spriteGameCamera;
    public OrthoGameCamera tiledGameCamera;
    public OrthoGameCamera parallaxGameCamera;

    public ParallaxBackground parallaxBackground;
    public ParallaxBackground parallaxForeground;
    public Zoom               gameZoom;
    public Zoom               hudZoom;
    public boolean            isDrawingStage;

    private LoadingScreen loadingScreen;
    private WorldRenderer worldRenderer;
    private HUDRenderer   hudRenderer;
    private SimpleVec3F   cameraPos;

    public BaseRenderer()
    {
        createCameras();
    }

    /**
     * Create all game cameras and
     * associated viewports.
     */
    private void createCameras()
    {
        Trace.__FILE_FUNC();

        AppConfig.camerasReady = false;

        // --------------------------------------
        // Camera for parallax scrolling backgrounds
        parallaxGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Parallax Cam"
            );

        parallaxBackground  = new ParallaxBackground();
        parallaxForeground  = new ParallaxBackground();
        App.parallaxManager = new ParallaxManager();

        // --------------------------------------
        // Camera for displaying TiledMap game maps.
        tiledGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Tiled Cam"
            );

        // --------------------------------------
        // Camera for displaying game sprites
        spriteGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Sprite Cam"
            );

        // --------------------------------------
        // Camera for displaying the HUD
        hudGameCamera = new OrthoGameCamera
            (
                Gfx._HUD_SCENE_WIDTH, Gfx._HUD_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Hud Cam"
            );

        gameZoom      = new Zoom();
        hudZoom       = new Zoom();
        worldRenderer = new WorldRenderer();
        hudRenderer   = new HUDRenderer();
        cameraPos     = new SimpleVec3F();
        loadingScreen = new LoadingScreen();

        isDrawingStage         = true;
        AppConfig.camerasReady = true;
    }

    /**
     * Process all cameras.
     */
    public void render()
    {
        ScreenUtils.clear(Color.BLACK, false);

        //
        // Set the positioning reference point for the cameras. Cameras
        // will centre on the main character.
        if (AppConfig.gameScreenActive())
        {
            if ((App.getPlayer() != null) && App.appState.after(StateID._STATE_SETUP))
            {
                if (App.getPlayer().isRidingRover)
                {
                    App.mapUtils.positionAt
                        (
                            (int) (App.getRover().sprite.getX() + PLAYER_ROVER_OFFSET),
                            (int) (App.getRover().sprite.getY())
                        );
                }
                else
                {
                    App.mapUtils.positionAt
                        (
                            (int) (App.getPlayer().sprite.getX()),
                            (int) (App.getPlayer().sprite.getY())
                        );
                }
            }
        }
        else
        {
            App.mapData.mapPosition.set(0, 0);
        }

        App.spriteBatch.enableBlending();

        processParallaxCamera();
        processTiledMapCamera();
        processSpritesCamera();
        processHUDCamera();

        // ----- Draw the Stage, if enabled -----
        if (isDrawingStage && (App.stage != null))
        {
            App.stage.act(Math.min(Gdx.graphics.getDeltaTime(), Gfx._STEP_TIME));
            App.stage.draw();
        }

        gameZoom.stop();
        hudZoom.stop();

        App.worldModel.drawDebugMatrix();
    }

    private void processParallaxCamera()
    {
        if (parallaxGameCamera.isInUse)
        {
            parallaxGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(parallaxGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = parallaxGameCamera.camera.viewportWidth / 2;
            cameraPos.y = parallaxGameCamera.camera.viewportHeight / 2;
            cameraPos.z = 0;

            parallaxGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
            parallaxBackground.render();

            App.entityManager.renderSystem.drawBackgroundSprites();
            App.spriteBatch.end();
        }
    }

    private void processTiledMapCamera()
    {
        if (tiledGameCamera.isInUse)
        {
            tiledGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(tiledGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = (App.mapData.mapPosition.getX() + (tiledGameCamera.camera.viewportWidth / 2));
            cameraPos.y = (App.mapData.mapPosition.getY() + (tiledGameCamera.camera.viewportHeight / 2));
            cameraPos.z = 0;

            if (tiledGameCamera.isLerpingEnabled)
            {
                tiledGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
            }
            else
            {
                tiledGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), true);
            }

            App.mapData.render(tiledGameCamera.camera);
            App.spriteBatch.end();
        }
    }

    private void processSpritesCamera()
    {
        if (spriteGameCamera.isInUse)
        {
            spriteGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(spriteGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = (App.mapData.mapPosition.getX() + (spriteGameCamera.camera.viewportWidth / 2));
            cameraPos.y = (App.mapData.mapPosition.getY() + (spriteGameCamera.camera.viewportHeight / 2));
            cameraPos.z = 0;

            if (spriteGameCamera.isLerpingEnabled)
            {
                spriteGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
            }
            else
            {
                spriteGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
            }

            worldRenderer.render(App.spriteBatch, spriteGameCamera);

            App.spriteBatch.end();
        }
    }

    /**
     * Draw the HUD and any related objects, if enabled.
     * The Front End should only be using this camera.
     */
    private void processHUDCamera()
    {
        if (hudGameCamera.isInUse)
        {
            hudGameCamera.viewport.apply();
            App.spriteBatch.setProjectionMatrix(hudGameCamera.camera.combined);
            App.spriteBatch.begin();

            cameraPos.x = (hudGameCamera.camera.viewportWidth / 2);
            cameraPos.y = (hudGameCamera.camera.viewportHeight / 2);
            cameraPos.z = 0;

            hudGameCamera.setPosition(cameraPos, hudZoom.getZoomValue(), false);

            hudRenderer.render(App.spriteBatch, hudGameCamera);

            App.spriteBatch.end();
        }
    }

    public LoadingScreen getSplashScreen()
    {
        return loadingScreen;
    }

    public void resizeCameras(int width, int height)
    {
        parallaxGameCamera.resizeViewport(width, height, true);
        tiledGameCamera.resizeViewport(width, height, true);
        spriteGameCamera.resizeViewport(width, height, true);
        hudGameCamera.resizeViewport(width, height, true);
    }

    @Override
    public void dispose()
    {
        parallaxGameCamera.dispose();
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        hudGameCamera.dispose();

        parallaxBackground.dispose();
        parallaxForeground.dispose();

        parallaxBackground = null;
        parallaxForeground = null;

        gameZoom = null;
        hudZoom  = null;

        worldRenderer = null;
        hudRenderer   = null;
    }
}
