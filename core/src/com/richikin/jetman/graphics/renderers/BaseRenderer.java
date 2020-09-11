package com.richikin.jetman.graphics.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;
import com.richikin.jetman.graphics.camera.Zoom;
import com.richikin.jetman.graphics.parallax.ParallaxBackground;
import com.richikin.jetman.graphics.parallax.ParallaxManager;
import com.richikin.jetman.maths.SimpleVec3F;
import com.richikin.jetman.utils.Developer;
import com.richikin.jetman.utils.logging.Trace;

public class BaseRenderer implements Disposable
{
    public OrthoGameCamera hudGameCamera;
    public OrthoGameCamera spriteGameCamera;
    public OrthoGameCamera tiledGameCamera;
    public OrthoGameCamera parallaxGameCamera;
    public OrthoGameCamera backgroundCamera;

    public ParallaxBackground parallaxBackground;
    public ParallaxBackground parallaxMiddle;
    public ParallaxBackground parallaxForeground;
    public Zoom               gameZoom;
    public Zoom               hudZoom;
    public boolean            isDrawingStage;

    private WorldRenderer worldRenderer;
    private HUDRenderer   hudRenderer;
    private SimpleVec3F   cameraPos;

    private final App app;

    public BaseRenderer(App _app)
    {
        this.app = _app;

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
        parallaxGameCamera = new OrthoGameCamera(Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT, "Parallax Cam", app);
        parallaxGameCamera.setExtendedViewport();
        parallaxGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        parallaxGameCamera.camera.update();
        parallaxBackground  = new ParallaxBackground(app);
        parallaxMiddle      = new ParallaxBackground(app);
        parallaxForeground  = new ParallaxBackground(app);
        app.parallaxManager = new ParallaxManager(app);

        Trace.__FILE_FUNC("Parallax Cam");
        Trace.dbg("Width  : " + parallaxGameCamera.camera.viewportWidth);
        Trace.dbg("Height : " + parallaxGameCamera.camera.viewportHeight);

        // --------------------------------------
        // Camera for background entities that exist between
        // groups of parallax background layers, mainly
        // TwinkleStars and UFOs.
        backgroundCamera = new OrthoGameCamera(Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT, "Background Cam", app);
        backgroundCamera.setExtendedViewport();
        backgroundCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        backgroundCamera.camera.update();

        // --------------------------------------
        // Camera for displaying TiledMap game maps.
        tiledGameCamera = new OrthoGameCamera(Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT, "Tiled Cam", app);
        tiledGameCamera.setExtendedViewport();
        tiledGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        tiledGameCamera.camera.update();

        // --------------------------------------
        // Camera for displaying game sprites
        spriteGameCamera = new OrthoGameCamera(Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT, "Sprite Cam", app);
        spriteGameCamera.setExtendedViewport();
        spriteGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        spriteGameCamera.camera.update();

        // --------------------------------------
        // Camera for displaying the HUD
        hudGameCamera = new OrthoGameCamera(Gfx._HUD_SCENE_WIDTH, Gfx._HUD_SCENE_HEIGHT, "Hud Cam", app);
        hudGameCamera.setExtendedViewport();
        hudGameCamera.setZoomDefault(Gfx._DEFAULT_ZOOM);
        hudGameCamera.camera.position.set(0, 0, 0);
        hudGameCamera.camera.update();

        gameZoom      = new Zoom();
        hudZoom       = new Zoom();
        worldRenderer = new WorldRenderer(app);
        hudRenderer   = new HUDRenderer(app);
        cameraPos     = new SimpleVec3F();

        isDrawingStage         = false;
        AppConfig.camerasReady = true;
    }

    /**
     * Process all cameras.
     */
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //
        // Set the positioning reference point for the cameras. Cameras
        // will centre on the main character.
        if (AppConfig.gameScreenActive())
        {
            if ((app.getPlayer() != null)
                && app.appState.after(StateID._STATE_SETUP)
                && !app.settings.isEnabled(Settings._SCROLL_DEMO))
            {
                if (app.getPlayer().isRidingRover)
                {
                    app.mapUtils.positionAt
                        (
                            (int) (app.getRover().sprite.getX() + 96),
                            (int) (app.getRover().sprite.getY())
                        );
                }
                else
                {
                    app.mapUtils.positionAt
                        (
                            (int) (app.getPlayer().sprite.getX()),
                            (int) (app.getPlayer().sprite.getY())
                        );
                }
            }
        }
        else
        {
            app.mapData.mapPosition.set(0, 0);
        }

        app.spriteBatch.enableBlending();

        // ----- Draw the first set of Parallax Layers, if enabled -----
        if (parallaxGameCamera.isInUse)
        {
            parallaxGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(parallaxGameCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.x = (float) (Gfx._VIEW_WIDTH / 2);
            cameraPos.y = (float) (Gfx._VIEW_HEIGHT / 2);
            cameraPos.z = 0;

            parallaxGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);

            parallaxBackground.render();

            app.spriteBatch.end();
        }

        // ----- Draw the background, ufos, twinkle stars, if enabled -----
        if (backgroundCamera.isInUse)
        {
            backgroundCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(backgroundCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.x = (float) (Gfx._VIEW_WIDTH / 2);
            cameraPos.y = (float) (Gfx._VIEW_HEIGHT / 2);
            cameraPos.z = 0;

            backgroundCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);

            app.entityManager.renderSystem.drawBackgroundSprites();

            app.spriteBatch.end();
        }

        // ----- Draw the remaining Parallax Layers, if enabled -----
        if (parallaxGameCamera.isInUse)
        {
            parallaxGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(parallaxGameCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.x = (float) (Gfx._VIEW_WIDTH / 2);
            cameraPos.y = (float) (Gfx._VIEW_HEIGHT / 2);
            cameraPos.z = 0;

            parallaxGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);

            parallaxMiddle.render();

            app.spriteBatch.end();
        }

        // ----- Draw the TiledMap, if enabled -----
        if (tiledGameCamera.isInUse)
        {
            tiledGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(tiledGameCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.x = (float) (app.mapData.mapPosition.getX() + (Gfx._VIEW_WIDTH / 2));
            cameraPos.y = (float) (app.mapData.mapPosition.getY() + (Gfx._VIEW_HEIGHT / 2));
            cameraPos.z = 0;

            if (tiledGameCamera.isLerpingEnabled)
            {
                tiledGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
            }
            else
            {
                tiledGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), true);
            }

            app.mapData.render(tiledGameCamera.camera);

            //
            // Deleted but, for future reference, the
            // MarkerTile layer was drawn here...

            app.spriteBatch.end();
        }

        // ----- Draw the game sprites, if enabled -----
        if (spriteGameCamera.isInUse)
        {
            spriteGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(spriteGameCamera.camera.combined);
            app.spriteBatch.begin();

            if (AppConfig.gameScreenActive())
            {
                cameraPos.x = (float) (app.mapData.mapPosition.getX() + (Gfx._VIEW_WIDTH / 2));
                cameraPos.y = (float) (app.mapData.mapPosition.getY() + (Gfx._VIEW_HEIGHT / 2));
                cameraPos.z = 0;

                if (spriteGameCamera.isLerpingEnabled)
                {
                    spriteGameCamera.lerpTo(cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true);
                }
                else
                {
                    spriteGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
                }
            }
            else
            {
                cameraPos.x = (float) app.mapData.mapPosition.getX();
                cameraPos.y = (float) app.mapData.mapPosition.getY();
                cameraPos.z = 0;

                spriteGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);
            }

            if (!app.settings.isEnabled(Settings._USING_ASHLEY_ECS))
            {
                if (!Developer.developerPanelActive)
                {
                    worldRenderer.render(app.spriteBatch, spriteGameCamera);
                }
            }

            app.spriteBatch.end();
        }

        // ----- Draw the HUD and any related objects, if enabled -----
        if (hudGameCamera.isInUse)
        {
            hudGameCamera.viewport.apply();
            app.spriteBatch.setProjectionMatrix(hudGameCamera.camera.combined);
            app.spriteBatch.begin();

            cameraPos.setEmpty();

            hudGameCamera.setPosition(cameraPos, hudZoom.getZoomValue(), false);

            hudRenderer.render(app.spriteBatch, hudGameCamera);

            app.spriteBatch.end();
        }

        // ----- Draw the Stage, if enabled -----
        if (isDrawingStage)
        {
            app.stage.act(Math.min(Gdx.graphics.getDeltaTime(), Gfx._STEP_TIME));
            app.stage.draw();
        }

        if (canDebugCameras)
        {
            Trace.__FILE_FUNC("ParallaxGameCamera: " + parallaxGameCamera.viewport.getScreenX() + ", " + parallaxGameCamera.viewport.getScreenWidth());
            Trace.__FILE_FUNC("BackgroundCamera: " + backgroundCamera.viewport.getScreenX() + ", " + backgroundCamera.viewport.getScreenWidth());
            Trace.__FILE_FUNC("TiledGameCamera: " + tiledGameCamera.viewport.getScreenX() + ", " + tiledGameCamera.viewport.getScreenWidth());
            Trace.__FILE_FUNC("SpriteGameCamera: " + spriteGameCamera.viewport.getScreenX() + ", " + spriteGameCamera.viewport.getScreenWidth());
            Trace.__FILE_FUNC("HudGameCamera: " + hudGameCamera.viewport.getScreenX() + ", " + hudGameCamera.viewport.getScreenWidth());
            canDebugCameras = false;
        }

        app.worldModel.drawDebugMatrix();
    }

    boolean canDebugCameras = true;

    public void resizeCameras(int _width, int _height)
    {
        parallaxGameCamera.resizeViewport(_width, _height, true);
        tiledGameCamera.resizeViewport(_width, _height, true);
        spriteGameCamera.resizeViewport(_width, _height, true);
        hudGameCamera.resizeViewport(_width, _height, true);
    }

    @Override
    public void dispose()
    {
        parallaxGameCamera.dispose();
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        hudGameCamera.dispose();

        parallaxBackground = null;

        gameZoom = null;
        hudZoom  = null;

        worldRenderer = null;
        hudRenderer   = null;
    }
}
