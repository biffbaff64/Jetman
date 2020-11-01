package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.parallax.ParallaxLayer;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.jetman.input.VirtualJoystick;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.input.*;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.misc.HighScoreUtils;
import com.richikin.utilslib.states.StateID;
import com.richikin.utilslib.ui.ProgressBar;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HeadsUpDisplay implements Disposable
{
    private static final int _ARROW_LEFT  = 0;
    private static final int _ARROW_RIGHT = 1;
    private static final int _ARROW_DOWN  = 2;

    private static final int _X1     = 0;
    private static final int _X2     = 1;
    private static final int _Y      = 2;
    private static final int _WIDTH  = 3;
    private static final int _HEIGHT = 4;

    private static final int _JOYSTICK    = 0;
    private static final int _ATTACK      = 1;
    private static final int _ACTION      = 2;
    private static final int _PAUSE       = 3;
    private static final int _DEV_OPTIONS = 4;
    private static final int _CONSOLE     = 5;
    private static final int _TRUCK_ARROW = 6;
    private static final int _BASE_ARROW  = 7;

    //@formatter:off
    private static final int[][] displayPos = new int[][]
        {
            {  25, 1016, (720 - 695), 240, 240},    // Joystick
            { 987,   44, (720 - 687),  96,  96},    // Attack
            {1109,  158, (720 - 600),  96,  96},    // Action
            {1206, 1206, (720 - 177),  66,  66},    // Pause Button
            {   0,    0, (720 - 101),  99,  86},    // Dev Options
            {1180, 1180, (720 - 101),  99,  86},    // Debug Console
            {  24,   24, (720 -  92),   0,   0},    // Truck Arrow
            {1218, 1218, (720 -  92),   0,   0},    // Base Arrow
        };

    private final int[][] livesDisplay = new int[][]
        {
            {1070, (720 - 47)},
            {1024, (720 - 47)},
            { 978, (720 - 47)},
            { 932, (720 - 47)},
            { 886, (720 - 47)},
        };
    //@formatter:on

    public GDXButton buttonUp;
    public GDXButton buttonDown;
    public GDXButton buttonLeft;
    public GDXButton buttonRight;
    public GDXButton buttonAction;
    public GDXButton buttonAttack;
    public GDXButton buttonX;
    public GDXButton buttonY;
    public Switch    buttonPause;
    public Switch    buttonDevOptions;

    public ImageButton ActionButton;
    public ImageButton AttackButton;

    public MessageManager messageManager;
    public PausePanel     pausePanel;
    public StateID        hudStateID;

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
    private boolean         showHUDControls;
    private BitmapFont      bigFont;
    private BitmapFont      midFont;
    private BitmapFont      smallFont;
    private HighScoreUtils  highScoreUtils;

    private final App app;

    public HeadsUpDisplay(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    public void createHud()
    {
        Trace.__FILE_FUNC();

        AppConfig.hudExists = false;

        scorePanel                = app.assets.loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);
        GameAssets.hudPanelWidth  = scorePanel.getWidth();
        GameAssets.hudPanelHeight = scorePanel.getHeight();

        highScoreUtils = new HighScoreUtils();
        messageManager = new MessageManager(app);
        pausePanel     = new PausePanel(app);

        timeBar = new ProgressBar(1, 100, 0, _MAX_TIMEBAR_LENGTH, "bar9", app);
        fuelBar = new ProgressBar(1, 70, 0, _MAX_FUELBAR_LENGTH, "bar9", app);

        barDividerFuel = app.assets.getObjectRegion("bar_divider");
        barDividerTime = app.assets.getObjectRegion("bar_divider");

        miniMen = new TextureRegion(app.assets.getObjectRegion("lives"));

        createHUDButtons();

        FontUtils fontUtils = new FontUtils();

        bigFont   = fontUtils.createFont("data/fonts/videophreak.ttf", 28);
        midFont   = fontUtils.createFont("data/fonts/videophreak.ttf", 22);
        smallFont = fontUtils.createFont("data/fonts/videophreak.ttf", 14);

        arrows    = new TextureRegion[3];
        arrows[0] = app.assets.getObjectRegion("arrow_left");
        arrows[1] = app.assets.getObjectRegion("arrow_right");
        arrows[2] = app.assets.getObjectRegion("arrow_down");

        truckArrowIndex = _ARROW_DOWN;
        baseArrowIndex  = _ARROW_DOWN;

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
                if (buttonPause.isPressed())
                {
                    AppConfig.pause();
                    buttonPause.release();
                }

                if (Developer.isDevMode())
                {
                    developmentHUDUpdate();
                }
                else
                {
                    updateBars();
                }

                updateArrowIndicators();
                updateDeveloperItems();

                if (messageManager.isEnabled())
                {
                    messageManager.update();

                    if (!messageManager.doesPanelExist("ZoomPanel")
                        && (app.appState.peek() != StateID._STATE_GAME)
                        && app.missileBaseManager.isMissileActive)
                    {
                        app.appState.set(StateID._STATE_GAME);
                    }
                }
            }
            break;

            case _STATE_PAUSED:
            {
                pausePanel.update();

                if (buttonPause.isPressed())
                {
                    AppConfig.unPause();

                    showHUDControls = true;

                    buttonPause.release();

                    stateID = StateID._STATE_PANEL_UPDATE;
                }
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
        if (!app.teleportManager.teleportActive)
        {
            if ((app.getPlayer().getAction() == ActionStates._FLYING) || app.getPlayer().isJumpingCrater)
            {
                fuelBar.setSpeed(app.getPlayer().isCarrying ? 2 : 1);
                fuelBar.updateSlowDecrement();
            }

            if (app.getBase() != null)
            {
                if (app.getBase().getAction() != ActionStates._WAITING)
                {
                    timeBar.updateSlowDecrement();
                }

                if (timeBar.justEmptied)
                {
                    if (!app.missileBaseManager.isMissileActive)
                    {
                        app.getBase().setAction(ActionStates._FIGHTING);
                    }
                }
            }
        }

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
            baseArrowIndex  = _ARROW_DOWN;
            truckArrowIndex = _ARROW_DOWN;
        }
        else
        {
            if (app.getBase() != null)
            {
                if (app.entityUtils.isOnScreen(app.getBase()))
                {
                    baseArrowIndex = _ARROW_DOWN;
                }
                else
                {
                    if (app.getBase().sprite.getX() < app.getPlayer().sprite.getX())
                    {
                        baseArrowIndex = _ARROW_LEFT;
                    }
                    else
                    {
                        baseArrowIndex = _ARROW_RIGHT;
                    }
                }
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

    /**
     * Checks for activation of the developer settings panel.
     */
    private void updateDeveloperItems()
    {
        if (Developer.isDevMode() && !AppConfig.gamePaused)
        {
            // DevOptions button which activates the Dev Settings panel
            if ((buttonDevOptions != null) && buttonDevOptions.isPressed() && !Developer.developerPanelActive)
            {
                Developer.developerPanelActive = true;

//                app.developerPanel.setup();
                buttonDevOptions.release();
                app.appState.set(StateID._STATE_DEVELOPER_PANEL);
            }
        }
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
            // Draw the Pause panel if activated
            if (stateID == StateID._STATE_PAUSED)
            {
                pausePanel.draw(app.spriteBatch, camera, originX, originY);
            }
        }
    }

    public void showControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            ActionButton.addAction(Actions.show());
            AttackButton.addAction(Actions.show());

            ActionButton.setVisible(true);
            AttackButton.setVisible(true);

            if (app.inputManager.virtualJoystick != null)
            {
                app.inputManager.virtualJoystick.show();
            }
        }

        if (Developer.isDevMode())
        {
            buttonDevOptions.setDrawable(true);
        }

        showHUDControls = true;
    }

    public void hideControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            ActionButton.addAction(Actions.hide());
            AttackButton.addAction(Actions.hide());

            ActionButton.setVisible(false);
            AttackButton.setVisible(false);

            if (app.inputManager.virtualJoystick != null)
            {
                app.inputManager.virtualJoystick.hide();
            }
        }

        if (Developer.isDevMode())
        {
            buttonDevOptions.setDrawable(true);
        }

        showHUDControls = false;
    }

    public void refillItems()
    {
        app.getHud().getFuelBar().setToMaximum();
        app.getHud().getTimeBar().setToMaximum();
    }

    public void releaseDirectionButtons()
    {
        buttonRight.release();
        buttonLeft.release();
        buttonUp.release();
        buttonDown.release();
    }

    public void setStateID(StateID _newState)
    {
        hudStateID = _newState;
    }

    public VirtualJoystick getJoystick()
    {
        return app.inputManager.virtualJoystick;
    }

    public com.richikin.utilslib.ui.ProgressBar getTimeBar()
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

            smallFont.draw(app.spriteBatch, "FPS  : " + Gdx.graphics.getFramesPerSecond(), originX + 20, originY + 600);
            smallFont.draw(app.spriteBatch, "ActionButton  : " + app.getPlayer().actionButton.getActionMode().name(), originX + 20, originY + 570);
        }

        bigFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%06d", app.gameProgress.score.getTotal()),
                originX + 296,
                originY + (720 - 6)
            );

        midFont.draw
            (
                app.spriteBatch,
                String.format(Locale.UK, "%06d", highScoreUtils.getHighScoreTable()[0].score),
                originX + 668,
                originY + (720 - 6)
            );

        //
        // This colour value was obtained via Gimp.
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
                ActionButton.setPosition(originX + displayPos[_ACTION][_X1], originY + displayPos[_ACTION][_Y]);
                AttackButton.setPosition(originX + displayPos[_ATTACK][_X1], originY + displayPos[_ATTACK][_Y]);

                if (app.inputManager.virtualJoystick != null)
                {
                    app.inputManager.virtualJoystick.getTouchpad().setPosition
                        (
                            originX + displayPos[_JOYSTICK][_X1],
                            originY + displayPos[_JOYSTICK][_Y]
                        );

                    app.inputManager.virtualJoystick.getTouchpad().setBounds
                        (
                            originX + displayPos[_JOYSTICK][_X1],
                            originY + displayPos[_JOYSTICK][_Y],
                            app.inputManager.virtualJoystick.getTouchpad().getWidth(),
                            app.inputManager.virtualJoystick.getTouchpad().getHeight()
                        );
                }
            }
        }
    }

    /**
     * Draw the Indicator arrows.
     * These show in which direction the Moon Rover and Missile Base are.
     */
    private void drawArrows()
    {
        app.spriteBatch.draw(arrows[truckArrowIndex], (originX + displayPos[_TRUCK_ARROW][_X1]), (originY + displayPos[_TRUCK_ARROW][_Y]));
        app.spriteBatch.draw(arrows[baseArrowIndex], (originX + displayPos[_BASE_ARROW][_X1]), (originY + displayPos[_BASE_ARROW][_Y]));
    }

    private void drawMessages()
    {
        if (!AppConfig.gamePaused)
        {
            if (messageManager.isEnabled())
            {
                messageManager.draw();
            }
        }
    }

    /**
     * Creates the game screen buttons and then
     * registers them with the Scene2D Stage.
     */
    private void createHUDButtons()
    {
        Scene2DUtils.setup(app);

        buttonUp    = new Switch();
        buttonDown  = new Switch();
        buttonLeft  = new Switch();
        buttonRight = new Switch();

        buttonX = new Switch();
        buttonY = new Switch();

        buttonPause = new Switch();

        int xPos = AppConfig.virtualControllerPos == ControllerPos._LEFT ? _X1 : _X2;

        buttonAction = new Switch();
        buttonAttack = new Switch();

        ActionButton = Scene2DUtils.addButton
            (
                "button_a",
                "button_a_pressed",
                displayPos[_ACTION][xPos], displayPos[_ACTION][_Y]
            );

        AttackButton = Scene2DUtils.addButton
            (
                "button_b",
                "button_b_pressed",
                displayPos[_ATTACK][xPos], displayPos[_ATTACK][_Y]
            );

        if (Developer.isDevMode())
        {
            buttonDevOptions = new GameButtonRegion
                (
                    displayPos[_DEV_OPTIONS][xPos], displayPos[_DEV_OPTIONS][_Y],
                    displayPos[_DEV_OPTIONS][_WIDTH], displayPos[_DEV_OPTIONS][_HEIGHT]
                );
        }

        hideControls();

        AppConfig.gameButtonsReady = true;

        ActionButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                event.handle();
                buttonAction.press();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                event.handle();
                buttonAction.release();
            }
        });

        AttackButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                event.handle();
                buttonAttack.press();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                event.handle();
                buttonAttack.release();
            }
        });
    }

    StopWatch scoreStopwatch = StopWatch.start();
    StopWatch livesStopwatch = StopWatch.start();

    private void developmentHUDUpdate()
    {
        if (Developer.isDevMode())
        {
            if (scoreStopwatch.time(TimeUnit.MILLISECONDS) > 50)
            {
                app.gameProgress.score.add
                    (
                        MathUtils.random(100),
                        GameConstants._MAX_SCORE
                    );

                scoreStopwatch.reset();
            }

            if (livesStopwatch.time(TimeUnit.MILLISECONDS) > 750)
            {
                app.gameProgress.lives.subtract(1, GameConstants._MAX_LIVES);

                livesStopwatch.reset();
            }

            fuelBar.setSpeed(1);
            timeBar.setSpeed(2);
            fuelBar.updateSlowDecrementWithWrap((int) fuelBar.getMax());
            timeBar.updateSlowDecrementWithWrap((int) timeBar.getMax());
            updateBarColours();
        }
    }

    @Override
    public void dispose()
    {
        buttonAction = null;
        buttonAttack = null;
        buttonPause  = null;
        buttonDevOptions = null;

        messageManager = null;

        app.assets.unloadAsset(GameAssets._HUD_PANEL_ASSET);

        barDividerFuel = null;
        barDividerTime = null;
        scorePanel     = null;

        bigFont.dispose();
        midFont.dispose();
        smallFont.dispose();
    }
}
