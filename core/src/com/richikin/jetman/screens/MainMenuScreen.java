package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.effects.StarField;
import com.richikin.jetman.ui.ExitPanel;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;
import com.richikin.utilslib.input.controllers.ControllerData;
import com.richikin.utilslib.logging.Trace;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.logging.StateManager;
import com.richikin.utilslib.ui.IUIPage;

import java.util.ArrayList;

/**
 * Main class for handling all actions on the front end screen.
 */
public class MainMenuScreen extends AbstractBaseScreen
{
    private static final int _MENU_PAGE      = 0;
    private static final int _HISCORE_PAGE   = 1;
    private static final int _CREDITS_PAGE   = 2;
    private static final int _NUM_MAIN_PAGES = 3;
    private static final int _OPTIONS_PAGE   = 3;
    private static final int _EXIT_PAGE      = 4;

    private ExitPanel          exitPanel;
    private OptionsPage        optionsPage;
    private MenuPage           menuPage;
    private Texture            background;
    private StarField          starField;
    private ArrayList<IUIPage> panels;
    private int                currentPage;

    public MainMenuScreen()
    {
        super();
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        optionsPage = new OptionsPage();
        menuPage    = new MenuPage();
        panels      = new ArrayList<>();
        starField   = new StarField();

        panels.add(_MENU_PAGE, menuPage);
        panels.add(_HISCORE_PAGE, new HiscorePage());
        panels.add(_CREDITS_PAGE, new CreditsPage());
        panels.add(_OPTIONS_PAGE, optionsPage);

        if (AppSystem.isAndroidApp())
        {
            App.googleServices.signInSilently();
        }

        App.mapData.mapPosition.set(0, 0);
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

        if (App.appState.peek() == StateID._STATE_MAIN_MENU)
        {
            StateID tempState;

            if ((tempState = update(App.appState).peek()) != StateID._STATE_MAIN_MENU)
            {
                App.appState.set(tempState);
            }

            super.render(delta);
        }
    }

    private StateManager update(final StateManager state)
    {
//        if (!Sfx.inst().isTunePlaying(Sfx.inst().MUS_TITLE))
//        {
//            Sfx.inst().playTitleTune(true);
//        }

        if (state.peek() == StateID._STATE_MAIN_MENU)
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

                    if (!App.optionsPageActive)
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
                        AppSystem.shutDownActive = true;

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
            if (AppSystem.fullScreenButton.isPressed()
                || ControllerData.controllerFirePressed
                || ControllerData.controllerStartPressed)
            {
                if ((currentPage == _HISCORE_PAGE) || (currentPage == _CREDITS_PAGE))
                {
                    changePageTo(_MENU_PAGE);

                    ControllerData.controllerFirePressed  = false;
                    ControllerData.controllerStartPressed = false;
                }

                AppSystem.fullScreenButton.release();
            }

            //
            // Start button check
            if ((menuPage.buttonStart != null) && menuPage.buttonStart.isChecked())
            {
                Trace.divider('#', 100);
                Trace.dbg(" ***** START PRESSED ***** ");
                Trace.divider('#', 100);

//                Sfx.inst().playTitleTune(false);

                menuPage.buttonStart.setChecked(false);

                App.mainGameScreen.reset();
                App.mainGame.setScreen(App.mainGameScreen);
            }
            else
            {
                // If we're still on the title screen...
                if (state.peek() == StateID._STATE_MAIN_MENU)
                {
                    //
                    // Check OPTIONS button, open settings page if pressed
                    if ((menuPage.buttonOptions != null) && menuPage.buttonOptions.isChecked())
                    {
                        changePageTo(_OPTIONS_PAGE);

                        menuPage.buttonOptions.setChecked(false);
                    }

                    //
                    // Check EXIT button, open exit panel if pressed
                    if ((menuPage.buttonExit != null) && menuPage.buttonExit.isChecked())
                    {
                        panels.get(currentPage).hide();

                        exitPanel = new ExitPanel();
                        exitPanel.open();

                        currentPage = _EXIT_PAGE;

                        menuPage.buttonExit.setChecked(false);
                    }

                    //
                    // Check GOOGLE SIGN-IN button
                    if ((menuPage.buttonGoogle != null) && menuPage.buttonGoogle.isChecked())
                    {
                        menuPage.buttonGoogle.setChecked(false);

                        if (!App.googleServices.isSignedIn())
                        {
                            App.googleServices.signIn();
                        }
                    }
                }
            }
        }
        else
        {
            Trace.__FILE_FUNC();
            Trace.dbg("Unsupported game state: " + state.peek());
        }

        return state;
    }

    /**
     * Draw the currently active page.
     *
     * @param spriteBatch The spritebatch to use.
     * @param _camera     The camera to use.
     */
    public void draw(final SpriteBatch spriteBatch, final OrthoGameCamera _camera)
    {
        if (App.appState.peek() == StateID._STATE_MAIN_MENU)
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
                    spriteBatch.draw(background, originX, originY);

                    starField.render();

                    if (exitPanel == null)
                    {
                        panels.get(currentPage).draw(spriteBatch, originX, originY);
                    }
                    else
                    {
                        exitPanel.draw(spriteBatch, _camera.camera);
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

        AppSystem.currentScreenID = ScreenID._MAIN_MENU;
        App.appState.set(StateID._STATE_MAIN_MENU);

        super.show();

        initialise();

        App.cameraUtils.resetCameraZoom();
        App.cameraUtils.disableAllCameras();
        App.baseRenderer.spriteGameCamera.isInUse = true;
        App.baseRenderer.hudGameCamera.isInUse    = true;
        App.baseRenderer.isDrawingStage           = true;

        currentPage = (App.highScoreUtils.canAddNewEntry(App.gameProgress.score)) ? _HISCORE_PAGE : _MENU_PAGE;

        panels.get(currentPage).show();

        Version.appDetails();
    }

    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        super.hide();

        dispose();
    }

    @Override
    public void loadImages()
    {
        background = App.assets.loadSingleAsset("data/empty_screen_dark.png", Texture.class);
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

        App.assets.unloadAsset("empty_screen_dark.png");
        background = null;

        starField.dispose();
        starField = null;

        exitPanel   = null;
        optionsPage = null;
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
