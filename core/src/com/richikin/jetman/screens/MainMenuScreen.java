package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ScreenID;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.effects.StarField;
import com.richikin.jetman.ui.ExitPanel;
import com.richikin.jetman.graphics.camera.OrthoGameCamera;
import com.richikin.utilslib.logging.Trace;
import com.richikin.jetman.ui.IUIPage;

import java.util.ArrayList;

/**
 * Main class for handling all actions on the front end screen.
 */
public class MainMenuScreen extends AbstractBaseScreen
{
    private static final int _MENU_PANEL    = 0;
    private static final int _OPTIONS_PANEL = 1;
    private static final int _CREDITS_PANEL = 2;
    private static final int _EXIT_PANEL    = 3;

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
        currentPage = _MENU_PANEL;

        panels.add(_MENU_PANEL, menuPage);
        panels.add(_OPTIONS_PANEL, optionsPage);
        panels.add(_CREDITS_PANEL, new CreditsPage());

        menuPage.initialise();
        menuPage.show();

        if (AppConfig.isAndroidApp() && !App.googleServices.isSignedIn())
        {
            App.googleServices.signIn();
        }

        Trace.finishedMessage();
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
            update();

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
            AppConfig.hudOriginX = (_camera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            AppConfig.hudOriginY = (_camera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            switch (currentPage)
            {
                case _MENU_PANEL:
                case _CREDITS_PANEL:
                case _OPTIONS_PANEL:
                case _EXIT_PANEL:
                {
                    spriteBatch.draw(background, AppConfig.hudOriginX, AppConfig.hudOriginY);

                    starField.render();

                    if (exitPanel == null)
                    {
                        panels.get(currentPage).draw(spriteBatch);
                    }
                    else
                    {
                        exitPanel.draw(spriteBatch);
                    }

                    if (AppConfig.backButton.isVisible())
                    {
                        AppConfig.backButton.setPosition(AppConfig.hudOriginX + 20, AppConfig.hudOriginY + 20);
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
    public void update()
    {
        if (!GameAudio.inst().isTunePlaying(AudioData.MUS_TITLE))
        {
            GameAudio.inst().playTitleTune(true);
        }

        if (App.appState.peek() == StateID._STATE_MAIN_MENU)
        {
            switch (currentPage)
            {
                case _CREDITS_PANEL:
                {
                    panels.get(currentPage).update();

                    if (AppConfig.backButton.isChecked())
                    {
                        AppConfig.backButton.setChecked(false);
                        changePageTo(_MENU_PANEL);
                    }
                }
                break;

                case _MENU_PANEL:
                {
                    panels.get(_MENU_PANEL).update();
                }
                break;

                case _OPTIONS_PANEL:
                {
                    if (panels.get(_OPTIONS_PANEL).update())
                    {
                        AppConfig.backButton.setChecked(false);
                        changePageTo(_MENU_PANEL);
                    }
                }
                break;

                case _EXIT_PANEL:
                {
                    int option = exitPanel.update();

                    if (option == ExitPanel._YES_PRESSED)
                    {
                        exitPanel.dispose();
                        AppConfig.shutDownActive = true;
                        Gdx.app.exit();
                    }
                    else if (option == ExitPanel._NO_PRESSED)
                    {
                        exitPanel.close();
                        exitPanel = null;

                        currentPage = _MENU_PANEL;

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
                if (App.appState.peek() == StateID._STATE_MAIN_MENU)
                {
                    //
                    // Check OPTIONS button, open settings page if pressed
                    if ((menuPage.buttonOptions != null) && menuPage.buttonOptions.isChecked())
                    {
                        menuPage.buttonOptions.setChecked(false);
                        changePageTo(_OPTIONS_PANEL);
                    }

                    //
                    // Check CREDITS button, open credits page if pressed
                    if ((menuPage.buttonCredits != null) && menuPage.buttonCredits.isChecked())
                    {
                        menuPage.buttonCredits.setChecked(false);
                        changePageTo(_CREDITS_PANEL);
                    }

                    //
                    // Check EXIT button, open exit panel if pressed
                    if ((menuPage.buttonExit != null) && menuPage.buttonExit.isChecked())
                    {
                        menuPage.buttonExit.setChecked(false);
                        panels.get(currentPage).hide();

                        exitPanel = new ExitPanel();
                        exitPanel.open();

                        currentPage = _EXIT_PANEL;
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
            Trace.dbg("Unsupported game state: " + App.appState.peek());
        }
    }

    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        super.show();

        initialise();

        App.cameraUtils.resetCameraZoom();
        App.cameraUtils.disableAllCameras();

        App.baseRenderer.hudGameCamera.isInUse = true;
        App.baseRenderer.isDrawingStage        = true;

        Version.appDetails();

        AppConfig.currentScreenID = ScreenID._MAIN_MENU;
        App.appState.set(StateID._STATE_MAIN_MENU);

        App.baseRenderer.getSplashScreen().dispose();

        Trace.finishedMessage();
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
        }

        currentPage = _nextPage;

        if (panels.get(_nextPage) != null)
        {
            panels.get(currentPage).show();
        }
    }

    /**
     * Clear up all used resources
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

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

        background  = null;
        exitPanel   = null;
        optionsPage = null;
        menuPage    = null;
    }
}
