package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ScreenID;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.effects.StarField;
import com.richikin.jetman.ui.ExitPanel;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.graphics.camera.OrthoGameCamera;
import com.richikin.utilslib.input.controllers.ControllerData;
import com.richikin.utilslib.logging.StateManager;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIPage;

import java.util.ArrayList;

/**
 * Main class for handling all actions on the front end screen.
 */
public class MainMenuScreen extends AbstractBaseScreen
{
    private static final int _MENU_PAGE      = 0;
    private static final int _OPTIONS_PAGE   = 1;
    private static final int _HISCORE_PAGE   = 2;
    private static final int _CREDITS_PAGE   = 3;
    private static final int _NUM_MAIN_PAGES = 4;
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

        App.mapData.mapPosition.set(0, 0);

        optionsPage = new OptionsPage();
        menuPage    = new MenuPage();
        panels      = new ArrayList<>();
        starField   = new StarField();

        panels.add(_MENU_PAGE, menuPage);
        panels.add(_OPTIONS_PAGE, optionsPage);
        panels.add(_HISCORE_PAGE, new HiscorePage());
        panels.add(_CREDITS_PAGE, new CreditsPage());

        menuPage.initialise();
        menuPage.show();

        if (AppSystem.isAndroidApp())
        {
            App.googleServices.signInSilently();
        }
    }

    private StateManager update(final StateManager state)
    {
        if (!GameAudio.inst().isTunePlaying(AudioData.MUS_TITLE))
        {
            GameAudio.inst().playTitleTune(true);
        }

        if (state.peek() == StateID._STATE_MAIN_MENU)
        {
            switch (currentPage)
            {
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                {
                    panels.get(currentPage).update();

                    if (AppSystem.backButton.isChecked())
                    {
                        AppSystem.backButton.setChecked(false);
                        changePageTo(_MENU_PAGE);
                    }
                }
                break;

                case _MENU_PAGE:
                {
                    panels.get(_MENU_PAGE).update();
                }
                break;

                case _OPTIONS_PAGE:
                {
                    if (panels.get(_OPTIONS_PAGE).update())
                    {
                        AppSystem.backButton.setChecked(false);
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
                    Trace.__FILE_FUNC("ERROR:  illegal page - " + currentPage);
                }
                break;
            }

            //
            // If currently showing Hiscore or Credits pages, return to menupage
            // if the screen is tapped (or controller start button pressed)
//            if (AppSystem.fullScreenButton.isPressed()
//                || AppSystem.backButton.isChecked()
//                || ControllerData.controllerFirePressed
//                || ControllerData.controllerStartPressed)
//            {
//                if ((currentPage != _OPTIONS_PAGE)
//                    || (((OptionsPage) panels.get(_OPTIONS_PAGE)).getActivePanel() == ScreenID._SETTINGS_SCREEN))
//                {
//                    changePageTo(_MENU_PAGE);
//                }
//
//                ControllerData.controllerFirePressed  = false;
//                ControllerData.controllerStartPressed = false;
//                AppSystem.fullScreenButton.release();
//                AppSystem.backButton.setChecked(false);
//            }

            //
            // Start button check
            if ((menuPage.buttonStart != null) && menuPage.buttonStart.isChecked())
            {
                Trace.divider('#', 100);
                Trace.dbg(" ***** START PRESSED ***** ");
                Trace.divider('#', 100);

                GameAudio.inst().playTitleTune(false);

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
                        menuPage.buttonOptions.setChecked(false);
                        changePageTo(_OPTIONS_PAGE);
                    }

                    //
                    // Check HISCORES button, open hiscores page if pressed
                    if ((menuPage.buttonHiScores != null) && menuPage.buttonHiScores.isChecked())
                    {
                        menuPage.buttonHiScores.setChecked(false);
                        changePageTo(_HISCORE_PAGE);
                    }

                    //
                    // Check CREDITS button, open credits page if pressed
                    if ((menuPage.buttonCredits != null) && menuPage.buttonCredits.isChecked())
                    {
                        menuPage.buttonCredits.setChecked(false);
                        changePageTo(_CREDITS_PAGE);
                    }

                    //
                    // Check EXIT button, open exit panel if pressed
                    if ((menuPage.buttonExit != null) && menuPage.buttonExit.isChecked())
                    {
                        menuPage.buttonExit.setChecked(false);
                        panels.get(currentPage).hide();

                        exitPanel = new ExitPanel();
                        exitPanel.open();

                        currentPage = _EXIT_PAGE;
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
            AppSystem.hudOriginX = (_camera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            AppSystem.hudOriginY = (_camera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            switch (currentPage)
            {
                case _MENU_PAGE:
                case _HISCORE_PAGE:
                case _CREDITS_PAGE:
                case _OPTIONS_PAGE:
                case _EXIT_PAGE:
                {
                    spriteBatch.draw(background, AppSystem.hudOriginX, AppSystem.hudOriginY);

                    starField.render();

                    if (exitPanel == null)
                    {
                        panels.get(currentPage).draw(spriteBatch);
                    }
                    else
                    {
                        exitPanel.draw(spriteBatch);
                    }

                    if (AppSystem.backButton.isVisible())
                    {
                        AppSystem.backButton.setPosition(AppSystem.hudOriginX + 20, AppSystem.hudOriginY + 20);
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

        currentPage = _MENU_PAGE;

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

    public MenuPage getMenuPage()
    {
        return menuPage;
    }

    /**
     * Closes down the current page, and
     * switches to a new one.
     *
     * @param _nextPage The ID of the next page.
     */
    private void changePageTo(int _nextPage)
    {
        if (panels.get(currentPage) != null)
        {
            panels.get(currentPage).hide();
            panels.get(currentPage).dispose();
        }

        currentPage = _nextPage;

        if (panels.get(_nextPage) != null)
        {
            panels.get(currentPage).initialise();
            panels.get(currentPage).show();
        }
    }

    /**
     * Clear up all used resources
     */
    @Override
    public void dispose()
    {
        super.dispose();

        App.assets.unloadAsset("empty_screen_dark.png");

        menuPage.hide();
        menuPage.dispose();

        starField.dispose();
        starField = null;

        if (panels != null)
        {
            panels.clear();
            panels = null;
        }

        background = null;
        exitPanel   = null;
        optionsPage = null;
        menuPage = null;
    }
}
