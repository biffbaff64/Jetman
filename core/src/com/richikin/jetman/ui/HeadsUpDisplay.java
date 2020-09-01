package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.parallax.ParallaxLayer;
import com.richikin.jetman.graphics.text.FontUtils;
import com.richikin.jetman.input.VirtualJoystick;
import com.richikin.jetman.input.buttons.GDXButton;
import com.richikin.jetman.input.buttons.GameButton;
import com.richikin.jetman.input.buttons.GameButtonRegion;
import com.richikin.jetman.input.buttons.Switch;
import com.richikin.jetman.input.objects.ControllerPos;
import com.richikin.jetman.utils.Developer;
import com.richikin.jetman.utils.logging.Trace;

import java.util.Locale;

public class HeadsUpDisplay implements Disposable
{
    public StateID   hudStateID;
    public GDXButton buttonUp;
    public GDXButton buttonDown;
    public GDXButton buttonLeft;
    public GDXButton buttonRight;
    public GDXButton buttonA;
    public GDXButton buttonB;
    public GDXButton buttonX;
    public GDXButton buttonY;
    public GDXButton buttonPause;
    public Switch    buttonDevOptions;

    public MessageManager messageManager;
    public PausePanel     pausePanel;

    private static final int _ARROW_LEFT  = 0;
    private static final int _ARROW_RIGHT = 1;
    private static final int _ARROW_DOWN  = 2;

    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;
    private static final int _X_DIR  = 3;
    private static final int _Y_DIR  = 4;

    private static final int _JOYSTICK_INIT = 0;
    private static final int _ATTACK_INIT   = 1;
    private static final int _ACTION_INIT   = 2;
    private static final int _JOYSTICK      = 3;
    private static final int _ATTACK        = 4;
    private static final int _ACTION        = 5;
    private static final int _PAUSE         = 6;
    private static final int _DEV_OPTIONS   = 7;
    private static final int _CONSOLE       = 8;

    private static final int[][] displayPos = new int[][]
        {
            {-241, 1281, (720 - 695), 240, 240},    // Joystick Init
            {1281, -241, (720 - 624), 96, 96},    // Attack Init
            {1281, -241, (720 - 687), 96, 96},    // Action Init

            {25, 1016, (720 - 695), 240, 240},    // Joystick
            {1141, 44, (720 - 624), 96, 96},    // Attack
            {1027, 158, (720 - 687), 96, 96},    // Action

            {1206, 1206, (720 - 177), 66, 66},    // Pause Button

            {0, 0, (720 - 101), 99, 86},    // Dev Options
            {1180, 1180, (720 - 101), 99, 86},    // Debug Console
        };

    private final int[][] livesDisplay = new int[][]
        {
            {1070, (720 - 47)},
            {1024, (720 - 47)},
            { 978, (720 - 47)},
            { 932, (720 - 47)},
            { 886, (720 - 47)},
        };

    private static final int _MAX_TIMEBAR_LENGTH = 1000;
    private static final int _MAX_FUELBAR_LENGTH = 1000;

    private float           originX;
    private float           originY;
    private StateID         stateID;
    private ProgressBar     timeBar;
    private ProgressBar     fuelBar;
    private Texture         scorePanel;
    private TextureRegion   barDividerFuel;
    private TextureRegion   barDividerTime;
    private TextureRegion   miniMen;
    private TextureRegion[] arrows;
    private int             baseArrowIndex;
    private int             truckArrowIndex;
    private VirtualJoystick joystick;
    private boolean         showHUDControls;
    private BitmapFont      bigFont;
    private BitmapFont      midFont;
    private BitmapFont      smallFont;

    private final App app;

    public HeadsUpDisplay(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    public void createHud()
    {
        AppConfig.hudExists = false;

        app.gameProgress.resetProgress();

        scorePanel = app.assets.loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);

        createVirtualJoystick();
        createHUDButtons();

        // The game time bar.
        timeBar = new ProgressBar(1, 100, 0, _MAX_TIMEBAR_LENGTH, "bar9", app);

        // The jet pack fuel bar.
        fuelBar = new ProgressBar(1, 70, 0, _MAX_FUELBAR_LENGTH, "bar9", app);

        barDividerFuel = app.assets.getObjectRegion("bar_divider");
        barDividerTime = app.assets.getObjectRegion("bar_divider");

        miniMen = new TextureRegion(app.assets.getObjectRegion("lives"));

        FontUtils fontUtils = new FontUtils();

        bigFont   = fontUtils.createFont("data/fonts/videophreak.ttf", 28);
        midFont   = fontUtils.createFont("data/fonts/videophreak.ttf", 22);
        smallFont = fontUtils.createFont("data/fonts/videophreak.ttf", 14);

        arrows    = new TextureRegion[3];
        arrows[0] = app.assets.getObjectRegion("arrow_left");
        arrows[1] = app.assets.getObjectRegion("arrow_right");
        arrows[2] = app.assets.getObjectRegion("arrow_down");

        truckArrowIndex = _ARROW_LEFT;
        baseArrowIndex  = _ARROW_RIGHT;

        stateID = StateID._STATE_PANEL_START;

        AppConfig.hudExists = true;
    }

    public void update()
    {
        switch (stateID)
        {
            case _STATE_PANEL_START:
            {
                stateID = StateID._STATE_PANEL_UPDATE;
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                updateBars();
                updateArrowIndicators();
                updateDeveloperItems();

//                if (messageManager.isEnabled())
//                {
//                    messageManager.updateMessage();
//
//                    if (!messageManager.doesPanelExist("ZoomPanel")
//                        && (app.mainGameScreen.gameState.get() != StateID._STATE_GAME)
//                        && app.missileBaseManager.isMissileActive)
//                    {
//                        app.mainGameScreen.getGameState().set(StateID._STATE_GAME);
//                    }
//                }
            }
            break;

            case _STATE_PAUSED:
            {
            }
            break;

            default:
            {
            }
            break;
        }
    }

    /**
     * Update the fuel bar and Launch timer bar.
     * The fuel bar decrements if LJM is flying, or jumping a crater.
     * The Launch Timer bar slowly decrements down to zero. Once
     * the bar is empty the missiles are launched.
     */
    private void updateBars()
    {
//        if (!app.teleportManager.teleportActive)
//        {
//            if (UIButtons.buttonUp.isPressed || app.getPlayer().isJumpingCrater)
//            {
//                fuelBar.setSpeed(app.getPlayer().isCarrying ? 2 : 1);
//                fuelBar.updateSlowDecrement();
//            }
//
//            if (app.getBase() != null)
//            {
//                if (app.getBase().spriteAction != Actions._WAITING)
//                {
//                    if (app.preferences.isEnabled(Preferences._PROGRESS_BARS))
//                    {
                        timeBar.updateSlowDecrement();
//                    }
//
//                    if (timeBar.justEmptied)
//                    {
//                        if (!app.missileBaseManager.isMissileActive)
//                        {
//                            app.getBase().spriteAction = Actions._FIGHTING;
//                        }
//                    }
//                }
//            }
//        }

        updateBarColours();
    }

    /**
     * Updates the Fuel bar and Time bar colours, depending
     * on the length of the bars.
     */
    private void updateBarColours()
    {
        if (fuelBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 3))
        {
            if (fuelBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 8))
            {
                fuelBar.setColor(Color.RED);
            }
            else
            {
                fuelBar.setColor(Color.ORANGE);
            }
        }
        else
        {
            fuelBar.setColor(Color.GREEN);
        }

        if (timeBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 3))
        {
            if (timeBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 8))
            {
                timeBar.setColor(Color.RED);
            }
            else
            {
                timeBar.setColor(Color.ORANGE);
            }
        }
        else
        {
            timeBar.setColor(Color.YELLOW);
        }
    }

    /**
     * Updates the two arrow head indicators on the HUD, which indicate
     * which direction to go to find the missile base and Rover.
     */
    private void updateArrowIndicators()
    {
        if (app.gameProgress.baseDestroyed || app.gameProgress.roverDestroyed)
        {
            baseArrowIndex = _ARROW_DOWN;
            truckArrowIndex = _ARROW_DOWN;
        }
        else
        {
//            app.missileBaseManager.checkNearestBase();
//
//            if ((app.getBase() != null) && app.entityUtils.isOnScreen(app.getBase()))
//            {
//                baseArrowIndex = _ARROW_DOWN;
//            }
//            else if (app.missileBaseManager.nearestBasePosition < app.getPlayer().sprite.getX())
//            {
//                baseArrowIndex = _ARROW_LEFT;
//            }
//            else
            {
                baseArrowIndex = _ARROW_RIGHT;
            }

            if (app.getRover() != null)
            {
                if (app.entityUtils.isOnScreen(app.getRover()))
                {
                    truckArrowIndex = _ARROW_DOWN;
                }
                else if (app.getRover().sprite.getX() < app.getPlayer().sprite.getX())
                {
                    truckArrowIndex = _ARROW_LEFT;
                }
                else
                {
                    truckArrowIndex = _ARROW_RIGHT;
                }
            }
            else
            {
                truckArrowIndex = _ARROW_LEFT;
            }
        }
    }

    private void updateDeveloperItems()
    {
//        if (Developer._DEVMODE && !AppConfig.gamePaused)
//        {
//            // DevOptions button which activates the Dev Settings panel
//            if ((buttonDevOptions != null) && buttonDevOptions.isPressed && !AppConfig.developerPanelActive)
//            {
//                AppConfig.developerPanelActive = true;
//
//                if (AppConfig.debugConsoleActive)
//                {
//                    app.debugWindow.close();
//                }
//
//                app.developerPanel.setup();
//                buttonDevOptions.release();
//                app.mainGameScreen.getGameState().set(StateID._STATE_DEVELOPER_PANEL);
//            }
//        }
    }

    public void render(OrthographicCamera camera, boolean _canDrawControls)
    {
        if (AppConfig.hudExists)
        {
            originX = (camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            originY = (camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            drawPanels();
            drawItems();

            if (_canDrawControls && app.gameProgress.gameSetupDone)
            {
                drawControls(camera);
            }

            drawArrows();
            drawMessages();

            //
            // Modify the position of the front parallax layer
            // so that aligns properly with the HUD
            for (ParallaxLayer layer : new Array.ArrayIterator<>(app.baseRenderer.parallaxForeground.layers))
            {
                layer.position.set(originX, originY);
            }

            //
            // The foreground parallax is the row of rocks
            // in front of the main ground
            app.baseRenderer.parallaxForeground.render();

            //
            // Draw the Pause panel if activated
//            if (stateID == StateID._STATE_PAUSED)
//            {
//                pausePanel.draw(app.spriteBatch, camera, originX, originY);
//            }
        }
    }

    public void showControls()
    {
        showHUDControls = true;

        if (joystick != null)
        {
            joystick.show();
        }
    }

    public void hideControls()
    {
        showHUDControls = false;

        if (joystick != null)
        {
            joystick.hide();
        }
    }

    public void refillItems()
    {
        app.getHud().getFuelBar().setToMaximum();
        app.getHud().getTimeBar().setToMaximum();
    }

    public void releaseDirectionButtons()
    {
    }

    public void setStateID(StateID _newState)
    {
        hudStateID = _newState;
    }

    public ProgressBar getTimeBar()
    {
        return timeBar;
    }

    public ProgressBar getFuelBar()
    {
        return fuelBar;
    }

    /**
     * Draws the HUD background panels.
     */
    private void drawPanels()
    {
        app.spriteBatch.draw(scorePanel, originX, originY + (720 - scorePanel.getHeight()));
    }

    /**
     * Draws the counters, totals, etc.
     */
    private void drawItems()
    {
        fuelBar.draw((int) (originX + 150), (int) (originY + (Gfx._HUD_HEIGHT - 72)));
        timeBar.draw((int) (originX + 150), (int) (originY + (Gfx._HUD_HEIGHT - 101)));

        app.spriteBatch.draw(barDividerFuel, (originX + 150 + fuelBar.getTotal()), (originY + (Gfx._HUD_HEIGHT - 72)));
        app.spriteBatch.draw(barDividerTime, (originX + 150 + timeBar.getTotal()), (originY + (Gfx._HUD_HEIGHT - 101)));

        for (int i = 0; i < app.gameProgress.lives.getTotal(); i++)
        {
            app.spriteBatch.draw
                (
                    miniMen,
                    (originX + livesDisplay[i][0]),
                    (originY + livesDisplay[i][1])
                );
        }

        bigFont.setColor(Color.WHITE);
        midFont.setColor(Color.WHITE);

        if (Developer.isDevMode())
        {
            smallFont.setColor(Color.WHITE);
            smallFont.draw(app.spriteBatch, "DEV MODE", originX + 470, originY + (720 - 6));

            if (Developer.isGodMode())
            {
                smallFont.draw(app.spriteBatch, "GOD MODE", originX + 790, originY + (720 - 6));
            }

            smallFont.draw(app.spriteBatch, "FPS  : " + Gdx.graphics.getFramesPerSecond(), originX + 20, originY + 608);
        }

        bigFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%06d", app.gameProgress.score.getTotal()),
                originX + 300,
                originY + (720 - 8)
            );

        midFont.draw
            (
                app.spriteBatch,
//                String.format(Locale.UK, "%06d", app.highScoreUtils.getHighScoreTable()[0].score),
                String.format(Locale.UK, "%06d", 0),
                originX + 668,
                originY + (720 - 6)
            );

        midFont.setColor(Color.valueOf("8cb8edff"));
        midFont.draw
            (
                app.spriteBatch,
                String.format
                    (
                        Locale.UK,
                        "%02d",
                        app.gameProgress.playerLevel
                    ),
                originX + 208,
                originY + 714
            );
    }

    private void drawControls(OrthographicCamera camera)
    {
        if (!AppConfig.gamePaused)
        {
            if (showHUDControls)
            {
                ((GameButton) buttonA).draw();
                ((GameButton) buttonB).draw();

                if (joystick != null)
                {
                    int xPos = AppConfig.virtualControllerPos == ControllerPos._LEFT ? _X1 : _X2;

                    joystick.getTouchpad().setPosition
                        (
                            originX + displayPos[_JOYSTICK][xPos],
                            originY + displayPos[_JOYSTICK][_Y]
                        );

                    joystick.getTouchpad().setBounds
                        (
                            originX + displayPos[_JOYSTICK][xPos],
                            originY + displayPos[_JOYSTICK][_Y],
                            displayPos[_JOYSTICK][_WIDTH],
                            displayPos[_JOYSTICK][_HEIGHT]
                        );
                }
            }
        }

        ((GameButton) buttonPause).draw();
    }

    private void drawArrows()
    {
        app.spriteBatch.draw(arrows[truckArrowIndex], (originX + 24), (originY + (720 - 92)));
        app.spriteBatch.draw(arrows[baseArrowIndex], (originX + 1218), (originY + (720 - 92)));
    }

    private void drawMessages()
    {
//        if (!AppConfig.gamePaused)
//        {
//            if ((app.mainGameScreen.getGameState().get() == StateID._STATE_GAME_FINISHED)
//                && (app.mainGameScreen.completedPanel != null))
//            {
//                app.mainGameScreen.completedPanel.draw();
//            }
//            else if (messageManager.messageActive)
//            {
//                messageManager.draw();
//            }
//        }
    }

    private void createVirtualJoystick()
    {
//        joystick = new VirtualJoystick(app);
//        joystick.create();
    }

    private void createHUDButtons()
    {
        int xPos = AppConfig.virtualControllerPos == ControllerPos._LEFT ? _X1 : _X2;

        buttonB = new GameButton
            (
                app.assets.getButtonRegion("button_fire"),
                app.assets.getButtonRegion("button_fire_pressed"),
                displayPos[_ATTACK][xPos], displayPos[_ATTACK][_Y],
                app
            );

        buttonA = new GameButton
            (
                app.assets.getButtonRegion("button_drop"),
                app.assets.getButtonRegion("button_drop_pressed"),
                displayPos[_ACTION][xPos], displayPos[_ACTION][_Y],
                app
            );

        buttonPause = new GameButton
            (
                app.assets.getButtonRegion("button_pause"),
                app.assets.getButtonRegion("button_pause"),
                displayPos[_PAUSE][xPos], displayPos[_PAUSE][_Y],
                app
            );

        if (Developer.isDevMode())
        {
            buttonDevOptions = new GameButtonRegion
                (
                    displayPos[_DEV_OPTIONS][xPos], displayPos[_DEV_OPTIONS][_Y],
                    displayPos[_DEV_OPTIONS][_WIDTH], displayPos[_DEV_OPTIONS][_HEIGHT],
                    app
                );
        }

        hideControls();

        AppConfig.gameButtonsReady = true;
    }

    @Override
    public void dispose()
    {
    }
}
