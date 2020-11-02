package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.graphics.effects.StarField;
import com.richikin.jetman.input.UIButtons;
import com.richikin.jetman.ui.ExitPanel;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.states.StateID;
import com.richikin.utilslib.states.StateManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;
import com.richikin.utilslib.ui.IUIPage;

import java.util.ArrayList;

/**
 * Main class for handling all actions on the front end screen.
 */
//@formatter:off
public class MainMenuScreen extends AbstractBaseScreen
{
    private static final int _MENU_PAGE         = 0;
    private static final int _HISCORE_PAGE      = 1;
    private static final int _CREDITS_PAGE      = 2;
    private static final int _NUM_MAIN_PAGES    = 3;
    private static final int _OPTIONS_PAGE      = 3;
    private static final int _EXIT_PAGE         = 4;

    private ExitPanel         exitPanel;
    private OptionsPage       optionsPage;
    private StateManager      gameState;
    private Texture           background;
    private StarField         starField;
    private ArrayList<IUIPage> panels;
    private int               currentPage;

    public MainMenuScreen(final App _app)
    {
        super(_app);

        this.gameState = new StateManager();
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        optionsPage     = new OptionsPage(app);
        panels          = new ArrayList<>();
        starField       = new StarField(app);

        panels.add(_MENU_PAGE, new MenuPage(app));
        panels.add(_HISCORE_PAGE, new HiscorePage(app));
        panels.add(_CREDITS_PAGE, new CreditsPage(app));
        panels.add(_OPTIONS_PAGE, optionsPage);

        if (AppConfig.isAndroidApp())
        {
            app.googleServices.signInSilently();
        }

        app.mapData.mapPosition.set(0, 0);
    }

    /**
     * Update and draw the screen.
     *
     * @param delta elapsed time since last update
     */
    @Override
    public void render(final float delta)
    {
        super.update();

        if (app.appState.peek() == StateID._STATE_MAIN_MENU)
        {
            StateID tempState;

            if ((tempState = update(app.appState).peek()) != StateID._STATE_MAIN_MENU)
            {
                gameState.set(tempState);
            }

            super.render(delta);
        }
    }

    private StateManager update(final StateManager state)
    {
        if (!Sfx.inst().isTunePlaying(Sfx.inst().MUS_TITLE))
        {
            Sfx.inst().playTitleTune(true);
        }

        if (state.get() == StateID._STATE_MAIN_MENU)
        {
            switch (currentPage)
            {
                case _MENU_PAGE:
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                {
                    if (panels.get(currentPage).update())
                    {
                        panels.get(currentPage).reset();

                        changePageTo((currentPage + 1) % _NUM_MAIN_PAGES);
                    }
                }
                break;

                case _OPTIONS_PAGE:
                {
                    optionsPage.update();

                    if (!AppConfig.optionsPageActive)
                    {
                        changePageTo(_MENU_PAGE);
                    }
                }
                break;

                case _EXIT_PAGE:
                {
                    int option = exitPanel.update();

                    if (option == exitPanel._YES_PRESSED)
                    {
                        exitPanel.dispose();
                        AppConfig.shutDownActive = true;

                        Gdx.app.exit();
                    }
                    else if (option == exitPanel._NO_PRESSED)
                    {
                        exitPanel.close();
                        exitPanel = null;

                        currentPage = _MENU_PAGE;

                        panels.get(currentPage).show();
                    }
                }
                break;

                default:
                {
                    // TODO: 09/01/2019 - Add error handling here for illegal panel
                }
                break;
            }

            //
            // If currently showing Hiscore or Credits pages, return to menupage
            // if the screen is tapped (or controller start button pressed)
            if (UIButtons.fullScreenButton.isPressed || UIButtons.controllerFirePressed || UIButtons.controllerStartPressed)
            {
                if ((currentPage == _HISCORE_PAGE) || (currentPage == _CREDITS_PAGE))
                {
                    changePageTo(_MENU_PAGE);

                    UIButtons.controllerFirePressed = false;
                    UIButtons.controllerStartPressed = false;
                }

                UIButtons.fullScreenButton.release();
            }

            //
            // Start button(s) check
            if (((UIButtons.button1PStart != null) && UIButtons.button1PStart.isPressed)
                || ((UIButtons.button2PStart != null) && UIButtons.button2PStart.isPressed))
            {
                Trace.divider('#', 100);
                Trace.dbg(" ***** START PRESSED ***** ");
                Trace.divider('#', 100);

                Sfx.inst().playTitleTune(false);

                app.gameProgress.activePlayers = (UIButtons.button1PStart.isPressed) ?
                    Player._PLAYER_ONE :
                    Player._PLAYER_TWO;

                UIButtons.button1PStart.release();
                UIButtons.button2PStart.release();

                app.mainGameScreen.reset();
                app.setScreen(app.mainGameScreen);
            }
            else
            {
                // If we're still on the title screen...
                if (state.get() == StateID._STATE_MAIN_MENU)
                {
                    //
                    // Check OPTIONS button, open settings page if pressed
                    if ((UIButtons.buttonOptions != null) && UIButtons.buttonOptions.isPressed)
                    {
                        changePageTo(_OPTIONS_PAGE);

                        UIButtons.buttonOptions.release();
                    }

                    //
                    // Check EXIT button, open exit panel if pressed
                    if ((UIButtons.buttonExit != null) && UIButtons.buttonExit.isPressed)
                    {
                        panels.get(currentPage).hide();

                        exitPanel = new ExitPanel(app);
                        exitPanel.open();

                        currentPage = _EXIT_PAGE;

                        UIButtons.buttonExit.release();
                    }

                    //
                    // Check GOOGLE SIGN-IN button
                    if ((UIButtons.buttonGoogle != null) && UIButtons.buttonGoogle.isPressed)
                    {
                        UIButtons.buttonGoogle.release();

                        if (!app.googleServices.isSignedIn())
                        {
                            app.googleServices.signIn();
                        }
                    }
                }
            }
        }
        else
        {
            Trace.__FILE_FUNC();
            Trace.dbg("Unsupported game state: " + state.get());
        }

        return state;
    }

    /**
     * Draw the currently active page.
     *
     * @param spriteBatch The spritebatch to use.
     * @param _camera  The camera to use.
     */
    public void draw(final SpriteBatch spriteBatch, final OrthoGameCamera _camera)
    {
        if (gameState.get() == StateID._STATE_MAIN_MENU)
        {
            float originX = (_camera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            float originY = (_camera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            switch (currentPage)
            {
                case _MENU_PAGE:
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                case _OPTIONS_PAGE:
                case _EXIT_PAGE:
                {
                    if ("Sprite Cam".equals(_camera.name))
                    {
                        if (background != null)
                        {
                            spriteBatch.draw(background, originX, originY);
                        }

                        starField.render();
                    }
                    else
                    {
                        if (exitPanel == null)
                        {
                            panels.get(currentPage).draw(spriteBatch, originX, originY);
                        }
                        else
                        {
                            exitPanel.draw(spriteBatch, _camera.camera);
                        }
                    }
                }
                break;

                default:
                {
                    Trace.__FILE_FUNC("ERROR: Illegal panel: " + currentPage);
                }
                break;
            }
        }
    }

    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        if (gameState == null)
        {
            this.gameState = new StateManager();
        }

        app.currentScreenID = ScreenID._TITLE_SCREEN;
        gameState.set(StateID._STATE_MAIN_MENU);

        super.show();

        initialise();

        app.cameraUtils.resetCameraZoom();
        app.cameraUtils.disableAllCameras();
        app.baseRenderer.spriteGameCamera.isInUse   = true;
        app.baseRenderer.hudGameCamera.isInUse      = true;
        app.baseRenderer.isDrawingStage             = true;

        currentPage = (app.highScoreUtils.canAddNewEntry()) ? _HISCORE_PAGE : _MENU_PAGE;

        panels.get(currentPage).show();

        Version.appDetails(app);
    }

    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        super.hide();

        dispose();
    }

    /**
     * Gets the current game state.
     *
     * @return StateManager value.
     */
    @Override
    public StateManager getGameState()
    {
        return gameState;
    }

    @Override
    public void loadImages()
    {
        background = app.assets.loadSingleAsset("data/empty_screen_dark.png", Texture.class);
    }

    /**
     * Clear up all used resources
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        super.dispose();

        hideAllPages();

        app.assets.unloadAsset("empty_screen_dark.png");
        background = null;

        starField.dispose();
        starField = null;

        exitPanel   = null;
        optionsPage = null;
        gameState   = null;
    }

    /**
     * Calls the hide method for main front end pages.
     */
    private void hideAllPages()
    {
        if (panels != null)
        {
            for (final IUIPage page : panels)
            {
                page.hide();
                page.dispose();
            }

            panels.clear();
            panels = null;
        }
    }

    private void changePageTo(final int _nextPage)
    {
        if (panels.get(currentPage) != null)
        {
            panels.get(currentPage).hide();
        }

        currentPage = _nextPage;

        if (panels.get(_nextPage) != null)
        {
            panels.get(currentPage).show();
        }
    }
}
