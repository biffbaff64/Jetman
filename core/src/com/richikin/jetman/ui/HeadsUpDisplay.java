package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.HighScoreUtils;
import com.richikin.jetman.developer.Developer;
import com.richikin.jetman.graphics.parallax.ParallaxLayer;
import com.richikin.jetman.graphics.text.FontUtils;
import com.richikin.jetman.input.VirtualJoystick;
import com.richikin.utilslib.input.GameButtonRegion;
import com.richikin.utilslib.input.Switch;
import com.richikin.utilslib.input.controllers.ControllerPos;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

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
    private static final int _ACTION      = 1;
    private static final int _ATTACK      = 2;
    private static final int _PAUSE       = 3;
    private static final int _SETTINGS    = 4;
    private static final int _DEV_OPTIONS = 5;
    private static final int _CONSOLE     = 6;
    private static final int _TRUCK_ARROW = 7;
    private static final int _BASE_ARROW  = 8;
    private static final int _FUEL_BAR    = 9;
    private static final int _TIME_BAR    = 10;

    //@formatter:off
    private static final int[][] displayPos =
    {
        {  25, 1016, (720 - 695), 240, 240},    // Joystick
        {1007,   44, (720 - 677),  96,  96},    // Action
        {1129,  158, (720 - 590),  96,  96},    // Attack
        {1200, 1200, (720 - 177),  64,  64},    // Pause Button
        {  20,   20, (720 - 177),  64,  64},    // Settings Button
        {   0,    0, (720 - 101),  99,  86},    // Dev Options
        {1180, 1180, (720 - 101),  99,  86},    // Debug Console
        {  18,   18, (720 -  92),   0,   0},    // Truck Arrow
        {1218, 1218, (720 -  92),   0,   0},    // Base Arrow
        { 162,  162, (720 -  72),   0,   0},    // Fuel Bar
        { 162,  162, (720 - 101),   0,   0},    // Time Bar
    };

    private final int[][] livesDisplay =
        {
            {1070, (720 - 47)},
            {1024, (720 - 47)},
            { 978, (720 - 47)},
            { 932, (720 - 47)},
            { 886, (720 - 47)},
        };
    //@formatter:on

    public Switch buttonUp;
    public Switch buttonDown;
    public Switch buttonLeft;
    public Switch buttonRight;

    // TODO: 27/11/2020 - Are these following switches still needed ??
    public Switch buttonAction;
    public Switch buttonAttack;
    public Switch buttonX;
    public Switch buttonY;
    public Switch buttonPause;
    public Switch buttonDevOptions;

    public ImageButton ActionButton;
    public ImageButton AttackButton;
    public ImageButton PauseButton;

    public MessageManager messageManager;
    public PausePanel     pausePanel;
    public StateID        hudStateID;

    private static final int _MAX_TIMEBAR_LENGTH = 1000;
    private static final int _MAX_FUELBAR_LENGTH = 1000;

    private ProgressBar     timeBar;
    private ProgressBar     fuelBar;
    private TextureRegion   barDividerFuel;
    private TextureRegion   barDividerTime;
    private TextureRegion[] arrows;
    private TextureRegion   miniMen;
    private Texture         scorePanel;
    private int             baseArrowIndex;
    private int             truckArrowIndex;
    private boolean         showHUDControls;
    private BitmapFont      bigFont;
    private BitmapFont      midFont;
    private BitmapFont      smallFont;
    private HighScoreUtils  highScoreUtils;
    private Drawable        imageFuelLow;
    private boolean         fuelLowWarning;
    private boolean         timeLowWarning;
    private boolean         fuelLowState;
    private StopWatch       fuelLowTimer;
    private int             fuelLowDelay;

    public HeadsUpDisplay()
    {
    }

    public void createHud()
    {
        Trace.__FILE_FUNC();

        AppConfig.hudExists = false;

        scorePanel                = App.assets.loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);
        GameAssets.hudPanelWidth  = scorePanel.getWidth();
        GameAssets.hudPanelHeight = scorePanel.getHeight();

        highScoreUtils = new HighScoreUtils();
        messageManager = new MessageManager();
        pausePanel     = new PausePanel();

        timeBar = new ProgressBar(1, 0, _MAX_TIMEBAR_LENGTH, "bar9");
        fuelBar = new ProgressBar(1, 0, _MAX_FUELBAR_LENGTH, "bar9");

        barDividerFuel = App.assets.getObjectRegion("bar_divider");
        barDividerTime = App.assets.getObjectRegion("bar_divider");

        miniMen = new TextureRegion(App.assets.getObjectRegion("lives"));

        createHUDButtons();

        FontUtils fontUtils = new FontUtils();

        bigFont   = fontUtils.createFont(GameAssets._VIDEO_PHREAK_FONT, 28);
        midFont   = fontUtils.createFont(GameAssets._VIDEO_PHREAK_FONT, 22);
        smallFont = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 14);

        arrows    = new TextureRegion[3];
        arrows[0] = App.assets.getObjectRegion("arrow_left");
        arrows[1] = App.assets.getObjectRegion("arrow_right");
        arrows[2] = App.assets.getObjectRegion("arrow_down");

        truckArrowIndex = _ARROW_DOWN;
        baseArrowIndex  = _ARROW_DOWN;

        drawFuelLow(StateID._INIT);

        hudStateID = StateID._STATE_PANEL_START;

        AppConfig.hudExists = true;
    }

    public void update()
    {
        switch (hudStateID)
        {
            case _STATE_PANEL_START:
            {
                hudStateID = StateID._STATE_PANEL_UPDATE;
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if (buttonPause.isPressed())
                {
                    AppConfig.pause();
                    buttonPause.release();
                    pausePanel.setup();
                    hideControls();
                }
                else
                {
                    updateBars();
                    updateBarColours();
                    updateArrowIndicators();
                    updateDeveloperItems();

                    messageManager.update();
                }
            }
            break;

            case _STATE_PAUSED:
            {
                pausePanel.update();

                if (buttonPause.isPressed())
                {
                    AppConfig.unPause();
                    buttonPause.release();
                    pausePanel.dispose();

                    showHUDControls = true;
                    hudStateID      = StateID._STATE_PANEL_UPDATE;

                    showControls();
                    showPauseButton(true);
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
        if (!App.teleportManager.teleportActive)
        {
            if (App.getPlayer() != null)
            {
                if ((App.getPlayer().getAction() == ActionStates._FLYING) || App.getPlayer().isJumpingCrater)
                {
                    fuelBar.setSpeed(App.getPlayer().isCarrying ? 2 : 1);
                    fuelBar.updateSlowDecrement();
                }
            }

            if (App.getBase().getAction() != ActionStates._WAITING)
            {
                timeBar.updateSlowDecrement();
            }

            if (timeBar.justEmptied)
            {
//                if (App.getBase() != null)
//                {
//                    if (!App.missileBaseManager.isMissileActive)
//                    {
//                        App.getBase().setAction(ActionStates._SET_FIGHTING);
//                    }
//                }

                timeBar.refill();
            }
        }
    }

    /**
     * Updates the Fuel bar and Time bar colours, depending
     * on the length of the bars.
     */
    private void updateBarColours()
    {
        fuelLowWarning = false;
        timeLowWarning = false;

        if (fuelBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 2))
        {
            if (fuelBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 4))
            {
                fuelBar.setColor(Color.RED);
                fuelLowWarning = true;
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

        if (timeBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 2))
        {
            if (timeBar.getTotal() < (float) (_MAX_TIMEBAR_LENGTH / 4))
            {
                timeBar.setColor(Color.RED);
                timeLowWarning = true;
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
        if (App.gameProgress.baseDestroyed || App.gameProgress.roverDestroyed)
        {
            baseArrowIndex  = _ARROW_DOWN;
            truckArrowIndex = _ARROW_DOWN;
        }
        else
        {
            if (App.getBase() != null)
            {
                if (App.entityUtils.isOnScreen(App.getBase()))
                {
                    baseArrowIndex = _ARROW_DOWN;
                }
                else
                {
                    if (App.getBase().sprite.getX() < App.getPlayer().sprite.getX())
                    {
                        baseArrowIndex = _ARROW_LEFT;
                    }
                    else
                    {
                        baseArrowIndex = _ARROW_RIGHT;
                    }
                }
            }

            if (App.getRover() != null)
            {
                if (App.entityUtils.isOnScreen(App.getRover()))
                {
                    truckArrowIndex = _ARROW_DOWN;
                }
                else if (App.getRover().sprite.getX() < App.getPlayer().sprite.getX())
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

                buttonDevOptions.release();
                App.appState.set(StateID._STATE_DEVELOPER_PANEL);
            }
        }
    }

    public void render(OrthographicCamera camera, boolean canDrawControls)
    {
        if (AppConfig.hudExists)
        {
            if (AppConfig.gameScreenActive())
            {
                App.baseRenderer.parallaxForeground.render();
            }

            drawPanels();
            drawItems();

            if (canDrawControls && App.gameProgress.gameSetupDone)
            {
                drawControls(camera);
            }

            drawArrows();
            drawMessages();

            //
            // Modify the position of the front parallax layer
            // so that aligns properly with the HUD
            for (ParallaxLayer layer : new Array.ArrayIterator<>(App.baseRenderer.parallaxForeground.layers))
            {
                layer.position.set(AppConfig.hudOriginX, AppConfig.hudOriginY);
            }

            //
            // Draw the Pause panel if activated
            if (hudStateID == StateID._STATE_PAUSED)
            {
                pausePanel.draw(App.spriteBatch, camera, AppConfig.hudOriginX, AppConfig.hudOriginY);
            }
        }
    }

    public void showControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            ActionButton.setVisible(true);
            AttackButton.setVisible(true);

            getJoystick().show();

            if (App.inputManager.virtualJoystick != null)
            {
                App.inputManager.virtualJoystick.show();
            }
        }

        showHUDControls = true;
    }

    public void hideControls()
    {
        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            ActionButton.setVisible(false);
            AttackButton.setVisible(false);

            getJoystick().hide();

            if (App.inputManager.virtualJoystick != null)
            {
                App.inputManager.virtualJoystick.hide();
            }
        }

        showHUDControls = false;
    }

    public void showPauseButton(boolean state)
    {
        if (PauseButton != null)
        {
            PauseButton.setVisible(state);
            PauseButton.setDisabled(!state);
        }
    }

    public void refillItems()
    {
        App.getHud().getFuelBar().setToMaximum();
        App.getHud().getTimeBar().setToMaximum();
    }

    public void releaseDirectionButtons()
    {
        buttonRight.release();
        buttonLeft.release();
        buttonUp.release();
        buttonDown.release();
    }

    public void setStateID(StateID newState)
    {
        hudStateID = newState;
    }

    public VirtualJoystick getJoystick()
    {
        return App.inputManager.virtualJoystick;
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
        App.spriteBatch.draw(scorePanel, AppConfig.hudOriginX, AppConfig.hudOriginY + (720 - scorePanel.getHeight()));
    }

    /**
     * Draws the counters, totals, etc.
     */
    private void drawItems()
    {
        if (fuelLowWarning)
        {
            drawFuelLow(StateID._UPDATE);
        }

        fuelBar.draw((int) (AppConfig.hudOriginX + displayPos[_FUEL_BAR][_X1]), (int) (AppConfig.hudOriginY + displayPos[_FUEL_BAR][_Y]));
        timeBar.draw((int) (AppConfig.hudOriginX + displayPos[_TIME_BAR][_X1]), (int) (AppConfig.hudOriginY + displayPos[_TIME_BAR][_Y]));

        App.spriteBatch.draw
            (
                barDividerFuel,
                (AppConfig.hudOriginX + displayPos[_FUEL_BAR][_X1] + fuelBar.getTotal()),
                (AppConfig.hudOriginY + displayPos[_FUEL_BAR][_Y])
            );

        App.spriteBatch.draw
            (
                barDividerTime,
                (AppConfig.hudOriginX + displayPos[_TIME_BAR][_X1] + timeBar.getTotal()),
                (AppConfig.hudOriginY + displayPos[_TIME_BAR][_Y])
            );

        for (int i = 0; i < App.gameProgress.getLives().getTotal(); i++)
        {
            App.spriteBatch.draw
                (
                    miniMen,
                    (AppConfig.hudOriginX + livesDisplay[i][0]),
                    (AppConfig.hudOriginY + livesDisplay[i][1])
                );
        }

        bigFont.setColor(Color.WHITE);
        midFont.setColor(Color.WHITE);

        hudDebug();

        bigFont.draw
            (
                App.spriteBatch,
                String.format(Locale.UK, "%06d", App.gameProgress.getScore().getTotal()),
                AppConfig.hudOriginX + 296,
                AppConfig.hudOriginY + (720 - 4)
            );

        midFont.draw
            (
                App.spriteBatch,
                String.format(Locale.UK, "%06d", highScoreUtils.getHighScoreTable()[0].score),
                AppConfig.hudOriginX + 660,
                AppConfig.hudOriginY + (720 - 6)
            );

        //
        // This colour value was obtained via Gimp.
        midFont.setColor(Color.valueOf("8cb8edff"));
        midFont.draw
            (
                App.spriteBatch,
                String.format
                    (
                        Locale.UK,
                        "%02d",
                        App.gameProgress.playerLevel
                    ),
                AppConfig.hudOriginX + 208,
                AppConfig.hudOriginY + 714
            );
    }

    private void drawControls(OrthographicCamera camera)
    {
        if (!AppConfig.gamePaused)
        {
            if (showHUDControls)
            {
                ActionButton.setPosition(AppConfig.hudOriginX + displayPos[_ACTION][_X1], AppConfig.hudOriginY + displayPos[_ACTION][_Y]);
                AttackButton.setPosition(AppConfig.hudOriginX + displayPos[_ATTACK][_X1], AppConfig.hudOriginY + displayPos[_ATTACK][_Y]);
                PauseButton.setPosition(AppConfig.hudOriginX + displayPos[_PAUSE][_X1], AppConfig.hudOriginY + displayPos[_PAUSE][_Y]);

                if (App.inputManager.virtualJoystick != null)
                {
                    App.inputManager.virtualJoystick.getTouchpad().setPosition
                        (
                            AppConfig.hudOriginX + displayPos[_JOYSTICK][_X1],
                            AppConfig.hudOriginY + displayPos[_JOYSTICK][_Y]
                        );

                    App.inputManager.virtualJoystick.getTouchpad().setBounds
                        (
                            AppConfig.hudOriginX + displayPos[_JOYSTICK][_X1],
                            AppConfig.hudOriginY + displayPos[_JOYSTICK][_Y],
                            App.inputManager.virtualJoystick.getTouchpad().getWidth(),
                            App.inputManager.virtualJoystick.getTouchpad().getHeight()
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
        App.spriteBatch.draw(arrows[truckArrowIndex], (AppConfig.hudOriginX + displayPos[_TRUCK_ARROW][_X1]), (AppConfig.hudOriginY + displayPos[_TRUCK_ARROW][_Y]));
        App.spriteBatch.draw(arrows[baseArrowIndex], (AppConfig.hudOriginX + displayPos[_BASE_ARROW][_X1]), (AppConfig.hudOriginY + displayPos[_BASE_ARROW][_Y]));
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

    private void drawFuelLow(StateID stateID)
    {
        if (stateID == StateID._INIT)
        {
            Scene2DUtils scene2DUtils = new Scene2DUtils();

            imageFuelLow = scene2DUtils.createDrawable("fuel_low", App.assets.getTextsLoader());
            fuelLowState = true;
            fuelLowTimer = StopWatch.start();
            fuelLowDelay = 1000;
        }
        else
        {
            if (fuelLowTimer.time(TimeUnit.MILLISECONDS) > fuelLowDelay)
            {
                fuelLowState = !fuelLowState;
                fuelLowTimer.reset();
                fuelLowDelay = fuelLowState ? 750 : 250;
            }

            if (fuelLowState)
            {
                imageFuelLow.draw(App.spriteBatch, AppConfig.hudOriginX + 480, AppConfig.hudOriginY + (720 - 140), 320, 27);
            }
        }
    }

    /**
     * Creates the game screen buttons and then
     * registers them with the Scene2D Stage.
     */
    private void createHUDButtons()
    {
        buttonUp     = new Switch();
        buttonDown   = new Switch();
        buttonLeft   = new Switch();
        buttonRight  = new Switch();
        buttonX      = new Switch();
        buttonY      = new Switch();
        buttonPause  = new Switch();
        buttonAction = new Switch();
        buttonAttack = new Switch();

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            int xPos = AppConfig.virtualControllerPos == ControllerPos._LEFT ? _X1 : _X2;

            Scene2DUtils scene2DUtils = new Scene2DUtils();

            AttackButton = scene2DUtils.addButton
                (
                    "button_a",
                    "button_a_pressed",
                    displayPos[_ATTACK][xPos], displayPos[_ATTACK][_Y]
                );

            ActionButton = scene2DUtils.addButton
                (
                    "button_b",
                    "button_b_pressed",
                    displayPos[_ACTION][xPos], displayPos[_ACTION][_Y]
                );

            PauseButton = scene2DUtils.addButton
                (
                    "pause_button",
                    "pause_button_pressed",
                    displayPos[_PAUSE][xPos], displayPos[_PAUSE][_Y]
                );

            if (Developer.isDevMode())
            {
                buttonDevOptions = new GameButtonRegion
                    (
                        displayPos[_DEV_OPTIONS][xPos], displayPos[_DEV_OPTIONS][_Y],
                        displayPos[_DEV_OPTIONS][_WIDTH], displayPos[_DEV_OPTIONS][_HEIGHT]
                    );
            }

            addButtonListeners();
            hideControls();
        }

        AppConfig.gameButtonsReady = true;
    }

    private void addButtonListeners()
    {
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

        PauseButton.addListener(new ClickListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                event.handle();
                buttonPause.press();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                event.handle();
                buttonPause.release();
            }
        });
    }

    private void hudDebug()
    {
        if (Developer.isDevMode())
        {
            smallFont.setColor(Color.WHITE);
            smallFont.draw(App.spriteBatch, "DEV MODE", AppConfig.hudOriginX + 470, AppConfig.hudOriginY + (720 - 6));

            if (Developer.isGodMode())
            {
                smallFont.draw(App.spriteBatch, "GOD MODE", AppConfig.hudOriginX + 790, AppConfig.hudOriginY + (720 - 6));
            }

            int yPosition = 600;

            if (App.settings.isEnabled(Settings._SHOW_FPS))
            {
                smallFont.draw
                    (
                        App.spriteBatch,
                        "FPS  : " + Gdx.graphics.getFramesPerSecond(),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );

                yPosition -= 30;
            }

            if (App.settings.isEnabled(Settings._SHOW_DEBUG))
            {
                smallFont.draw
                    (
                        App.spriteBatch,
                        "AB Action: " + App.getPlayer().actionButton.getActionMode(),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );

                yPosition -= 30;
            }

            if (App.settings.isEnabled(Settings._MENU_HEAPS))
            {
                smallFont.draw
                    (
                        App.spriteBatch,
                        String.format
                            (
                                Locale.UK,
                                "JAVA HEAP: %3.2fMB",
                                ((((float) Gdx.app.getJavaHeap()) / 1024) / 1024)
                            ),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );

                yPosition -= 30;

                smallFont.draw
                    (
                        App.spriteBatch,
                        String.format
                            (
                                Locale.UK,
                                "NATIVE HEAP: %3.2fMB",
                                ((((float) Gdx.app.getNativeHeap()) / 1024) / 1024)
                            ),
                        AppConfig.hudOriginX + 20,
                        AppConfig.hudOriginY + yPosition
                    );
            }
        }
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        buttonAction     = null;
        buttonAttack     = null;
        buttonPause      = null;
        buttonDevOptions = null;

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            AttackButton.addAction(Actions.removeActor());
            ActionButton.addAction(Actions.removeActor());
            PauseButton.addAction(Actions.removeActor());
        }

        AttackButton = null;
        ActionButton = null;
        PauseButton  = null;

        messageManager = null;

        App.assets.unloadAsset(GameAssets._HUD_PANEL_ASSET);

        barDividerFuel = null;
        barDividerTime = null;
        scorePanel     = null;

        bigFont.dispose();
        midFont.dispose();
        smallFont.dispose();
    }
}
