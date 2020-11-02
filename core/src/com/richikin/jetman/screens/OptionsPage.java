
package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.ui.InstructionsPanel;
import com.richikin.jetman.ui.PrivacyPolicyPanel;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.input.ControllerPos;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.ui.IUIPage;

@SuppressWarnings("WeakerAccess")
public class OptionsPage implements IUIPage
{
    private ImageButton buttonExit;
    private ImageButton buttonStats;
    private ImageButton buttonPrivacy;
    private ImageButton buttonStoryLine;
    private ImageButton testButton;
    private ImageButton buttonDevOptions;
    private ImageButton buttonPositionLeft;
    private ImageButton buttonPositionRight;
    private ImageButton buttonSignOut;

    private Label     positionLabel;
    private CheckBox  musicCheckBox;
    private CheckBox  fxCheckBox;
    private CheckBox  vibrateCheckBox;
    private CheckBox  hintsCheckBox;
    private TextField musicLabel;
    private TextField fxLabel;
    private Slider    musicSlider;
    private Slider    fxSlider;
    private Texture   foreground;
    private Skin      skin;

    private StatsPanel         statsPanel;
    private PrivacyPolicyPanel privacyPanel;
    private DeveloperTests    testPanel;
    private InstructionsPanel storyPanel;
    private ScreenID          activePanel;

    private boolean justFinishedStatsPanel;
    private boolean justFinishedPrivacyPanel;
    private boolean justFinishedStoryPanel;
    private boolean justFinishedTestPanel;
    private boolean enteredDeveloperPanel;
    private boolean setupCompleted;

    private final App app;

    /**
     * Instantiates a new Options page.
     *
     * @param _app the game
     */
    public OptionsPage(App _app)
    {
        this.app = _app;
    }

    /**
     * Update panel.
     */
    @Override
    public boolean update()
    {
        if (AppConfig.optionsPageActive)
        {
            if (activePanel == ScreenID._TEST_PANEL)
            {
                testPanel.updatePanel();
            }
        }

        if (justFinishedStatsPanel)
        {
            if (statsPanel != null)
            {
                statsPanel.dispose();
            }

            justFinishedStatsPanel = false;
            statsPanel = null;
            activePanel = ScreenID._SETTINGS_SCREEN;

            showActors(true);
        }

        if (justFinishedPrivacyPanel)
        {
            if (privacyPanel != null)
            {
                privacyPanel.dispose();
            }

            justFinishedPrivacyPanel = false;
            privacyPanel = null;
            activePanel = ScreenID._SETTINGS_SCREEN;

            showActors(true);
        }

        if (justFinishedStoryPanel)
        {
            if (storyPanel != null)
            {
                storyPanel.dispose();
            }

            justFinishedStoryPanel = false;
            storyPanel = null;
            activePanel = ScreenID._SETTINGS_SCREEN;

            showActors(true);
        }

        if (justFinishedTestPanel)
        {
            if (testPanel != null)
            {
                testPanel.clearUp();
            }

            justFinishedTestPanel = false;
            testPanel = null;
            activePanel = ScreenID._SETTINGS_SCREEN;

            showActors(true);
        }

        if (enteredDeveloperPanel && !AppConfig.developerPanelActive)
        {
            enteredDeveloperPanel = false;
            showActors(true);
        }

        return false;
    }

    /**
     * Draw. 664 154
     *
     * @param spriteBatch the sprite batch
     */
    public void draw(SpriteBatch spriteBatch, float originX, float originY)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:             statsPanel.draw();      break;
            case _PRIVACY_POLICY_SCREEN:    privacyPanel.draw();    break;
            case _INSTRUCTIONS_SCREEN:      storyPanel.draw();      break;
            case _TEST_PANEL:               testPanel.draw();       break;

            default:
            {
                if (AppConfig.developerPanelActive)
                {
                    app.developerPanel.draw(spriteBatch);
                }
                else
                {
                    if (foreground != null)
                    {
                        spriteBatch.draw(foreground, originX, originY);
                    }
                }
            }
            break;
        }
    }

    @Override
    public void reset()
    {
    }

    /**
     * Show.
     */
    public void show()
    {
        setupCompleted = false;

        foreground = app.assets.loadSingleAsset("data/options_foreground.png", Texture.class);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        populateTable();

        createButtonListeners();
        createSliderListeners();
        createCheckboxListeners();

        updateSettingsOnEntry();

        activePanel = ScreenID._SETTINGS_SCREEN;

        AppConfig.optionsPageActive = true;
        AppConfig.developerPanelActive = false;

        enteredDeveloperPanel = false;
        setupCompleted = true;
    }

    /**
     * Hide.
     */
    public void hide()
    {
        if (AppConfig.optionsPageActive)
        {
            if (Developer._DEVMODE)
            {
                if (testButton != null)
                {
                    testButton.addAction(Actions.removeActor());
                    buttonDevOptions.addAction(Actions.removeActor());
                    testButton = null;
                    buttonDevOptions = null;
                }
            }

            buttonExit.addAction(Actions.removeActor());
            buttonStats.addAction(Actions.removeActor());
            buttonPrivacy.addAction(Actions.removeActor());
            buttonStoryLine.addAction(Actions.removeActor());
            buttonExit = null;
            buttonStats = null;
            buttonPrivacy = null;
            buttonStoryLine = null;

            musicLabel.addAction(Actions.removeActor());
            musicSlider.addAction(Actions.removeActor());
            musicCheckBox.addAction(Actions.removeActor());
            musicLabel = null;
            musicSlider = null;
            musicCheckBox = null;

            fxLabel.addAction(Actions.removeActor());
            fxSlider.addAction(Actions.removeActor());
            fxCheckBox.addAction(Actions.removeActor());
            fxLabel = null;
            fxSlider = null;
            fxCheckBox = null;

            vibrateCheckBox.addAction(Actions.removeActor());
            hintsCheckBox.addAction(Actions.removeActor());
            vibrateCheckBox = null;
            hintsCheckBox = null;

            buttonPositionLeft.addAction(Actions.removeActor());
            buttonPositionRight.addAction(Actions.removeActor());
            positionLabel.addAction(Actions.removeActor());
            buttonPositionLeft = null;
            buttonPositionRight = null;
            positionLabel = null;

            if (buttonSignOut != null)
            {
                buttonSignOut.addAction(Actions.removeActor());
            }
            buttonSignOut = null;

            app.assets.unloadAsset("data/settings_screen_template.png");

            foreground = null;
            skin = null;
            statsPanel = null;
            privacyPanel = null;
            testPanel = null;

            AppConfig.optionsPageActive = false;
        }
    }

    /**
     * Populate table.
     */
    private void populateTable()
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        // ----------
        musicSlider = scene2DUtils.addSlider(700, (720 - 208), skin);
        musicCheckBox = scene2DUtils.addCheckBox(600, (720 - 208), Color.WHITE, skin);
        musicLabel = scene2DUtils.addTextField("0%", 1000, (720 - 208), Color.WHITE, true, skin);
        musicLabel.setSize(64, 48);

        // ----------
        fxSlider = scene2DUtils.addSlider(700, (720 - 278), skin);
        fxCheckBox = scene2DUtils.addCheckBox(600, (720 - 278), Color.WHITE, skin);
        fxLabel = scene2DUtils.addTextField("0%", 1000, (720 - 278), Color.WHITE, true, skin);
        fxLabel.setSize(64, 48);

        // ----------
        buttonPositionLeft = scene2DUtils.addButton("arrow_left_blue", "arrow_left_blue_pressed", 600, (720 - 432));
        buttonPositionRight = scene2DUtils.addButton("arrow_right_blue", "arrow_right_blue_pressed", 800, (720 - 432));
        positionLabel = scene2DUtils.addLabel(AppConfig.controllerPos.getText(), 660, (720 - 420), 20,
            Color.WHITE, GameAssets._ORBITRON_BOLD_FONT);
        positionLabel.setSize(160, 25);
        positionLabel.setAlignment(Align.center);
        app.stage.addActor(positionLabel);

        buttonPositionLeft.setSize(56, 56);
        buttonPositionRight.setSize(56, 56);

        // ----------
        vibrateCheckBox = scene2DUtils.addCheckBox(600, (720 - 498), Color.WHITE, skin);
        hintsCheckBox = scene2DUtils.addCheckBox(600, (720 - 572), Color.WHITE, skin);

        // ----------
        buttonStats = scene2DUtils.addButton("new_stats_button", "new_stats_button_pressed", 986, (720 - 540));
        buttonPrivacy = scene2DUtils.addButton("new_privacy_policy_button", "new_privacy_policy_button_pressed", 986, (720 - 600));
        buttonStoryLine = scene2DUtils.addButton("new_objectives_button", "new_objectives_button_pressed", 986, (720 - 660));
        buttonExit = scene2DUtils.addButton("new_back_button", "new_back_button_pressed", 20, (720 - 100));

        buttonStats.setSize(210, 40);
        buttonPrivacy.setSize(210, 40);
        buttonStoryLine.setSize(210, 40);
        buttonExit.setSize(128, 64);

        // ----------
        if (app.googleServices.isSignedIn())
        {
            //noinspection ConstantConditions
            if (Developer._DEVMODE || (Version.majorVersion == 0))
            {
                buttonSignOut = scene2DUtils.addButton
                    (
                        "btn_google_signout_dark",
                        "btn_google_signout_dark_pressed",
                        986,
                        (720 - 80)
                    );

                buttonSignOut.setSize(191, 46);
            }
        }

        // ----------
        if (Developer._DEVMODE)
        {
            testButton = scene2DUtils.addButton
                (
                    "new_test_access_button",
                    "new_test_access_button_pressed",
                    986,
                    (720 - 480)
                );

            buttonDevOptions = scene2DUtils.addButton
                (
                    "new_developer_options_button",
                    "new_developer_options_button_pressed",
                    986,
                    (720 - 420)
                );

            testButton.setSize(210, 40);
            buttonDevOptions.setSize(210, 40);
        }

        showActors(true);
    }

    /**
     * Update settings.
     */
    private void updateSettings()
    {
        app.preferences.prefs.putBoolean(Preferences._MUSIC_ENABLED, (Sfx.inst().getMusicVolume() != Sfx.inst()._SILENT));
        app.preferences.prefs.putBoolean(Preferences._SOUNDS_ENABLED, (Sfx.inst().getFXVolume() != Sfx.inst()._SILENT));

        Sfx.inst().setMusicVolume((int) musicSlider.getValue());
        Sfx.inst().setFXVolume((int) fxSlider.getValue());

        app.preferences.prefs.putBoolean(Preferences._SHOW_HINTS, hintsCheckBox.isChecked());
        app.preferences.prefs.putBoolean(Preferences._VIBRATIONS, vibrateCheckBox.isChecked());
        app.preferences.prefs.putBoolean(Preferences._ON_SCREEN_CONTROLLER, true);
        app.preferences.prefs.putBoolean(Preferences._SHOW_GAME_BUTTONS, true);
        app.preferences.prefs.putBoolean(Preferences._EXTERNAL_CONTROLLER, false);

        app.preferences.prefs.flush();

        if (app.preferences.isEnabled(Preferences._EXTERNAL_CONTROLLER))
        {
            if (!AppConfig.controllersFitted && (app.inputManager.getGameController() != null))
            {
                app.inputManager.getGameController().setup();
            }
        }
    }

    /**
     * Update settings on entry.
     */
    private void updateSettingsOnEntry()
    {
        vibrateCheckBox.setChecked(app.preferences.isEnabled(Preferences._VIBRATIONS));
        hintsCheckBox.setChecked(app.preferences.isEnabled(Preferences._SHOW_HINTS));

        musicSlider.setValue(Sfx.inst().getMusicVolume());
        musicLabel.setText("" + ((int) musicSlider.getValue() * 10) + "%");
        musicCheckBox.setChecked(Sfx.inst().getMusicVolume() > 0);

        fxSlider.setValue(Sfx.inst().getFXVolume());
        fxLabel.setText("" + ((int) fxSlider.getValue() * 10) + "%");
        fxCheckBox.setChecked(Sfx.inst().getFXVolume() > 0);
    }

    /**
     * Show the OptionsPage actors.
     *
     * @param _visibilty Either TRUE or FALSE
     */
    private void showActors(boolean _visibilty)
    {
        if (Developer._DEVMODE)
        {
            testButton.setVisible(_visibilty);
            buttonDevOptions.setVisible(_visibilty);
        }

        if (buttonSignOut != null)
        {
            buttonSignOut.setVisible(_visibilty);
        }

        buttonExit.setVisible(_visibilty);
        buttonStats.setVisible(_visibilty);
        buttonPrivacy.setVisible(_visibilty);
        buttonStoryLine.setVisible(_visibilty);

        musicLabel.setVisible(_visibilty);
        musicSlider.setVisible(_visibilty);
        musicCheckBox.setVisible(_visibilty);

        fxLabel.setVisible(_visibilty);
        fxSlider.setVisible(_visibilty);
        fxCheckBox.setVisible(_visibilty);

        vibrateCheckBox.setVisible(_visibilty);
        hintsCheckBox.setVisible(_visibilty);

        buttonPositionLeft.setVisible(_visibilty);
        buttonPositionRight.setVisible(_visibilty);
        positionLabel.setVisible(_visibilty);
    }

    /**
     * Create button listeners.
     */
    private void createButtonListeners()
    {
        if (buttonPositionLeft != null)
        {
            buttonPositionLeft.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (app.preferences.prefs.getInteger(Preferences._CONTROLLER_POS) == Movement._DIRECTION_RIGHT)
                    {
                        AppConfig.controllerPos = ControllerPos._LEFT;
                    }
                    else
                    {
                        AppConfig.controllerPos = ControllerPos._RIGHT;
                    }

                    positionLabel.setText(AppConfig.controllerPos.getText());

                    app.preferences.prefs.putInteger(Preferences._CONTROLLER_POS, AppConfig.controllerPos.getValue());
                    app.preferences.prefs.flush();
                }
            });
        }

        if (buttonPositionRight != null)
        {
            buttonPositionRight.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (app.preferences.prefs.getInteger(Preferences._CONTROLLER_POS) == Movement._DIRECTION_RIGHT)
                    {
                        AppConfig.controllerPos = ControllerPos._LEFT;
                    }
                    else
                    {
                        AppConfig.controllerPos = ControllerPos._RIGHT;
                    }

                    positionLabel.setText(AppConfig.controllerPos.getText());

                    app.preferences.prefs.putInteger(Preferences._CONTROLLER_POS, AppConfig.controllerPos.getValue());
                    app.preferences.prefs.flush();
                }
            });
        }

        /*
         * Statistics button.
         * Displays the in-game statistics.
         */
        if (buttonStats != null)
        {
            buttonStats.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (statsPanel == null)
                    {
                        showActors(false);
                        justFinishedStatsPanel = false;
                        activePanel = ScreenID._STATS_SCREEN;

                        statsPanel = new StatsPanel(app);
                        statsPanel.setXOffset(0);
                        statsPanel.setYOffset(0);
                        statsPanel.open();

                        buttonExit.setVisible(true);
                    }
                }
            });
        }

        /*
         * Privacy policy button.
         * Displays the privacy policy on screen, for
         * the players reference.
         */
        if (buttonPrivacy != null)
        {
            buttonPrivacy.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (privacyPanel == null)
                    {
                        showActors(false);
                        justFinishedPrivacyPanel = false;
                        activePanel = ScreenID._PRIVACY_POLICY_SCREEN;

                        privacyPanel = new PrivacyPolicyPanel(app);
                        privacyPanel.xOffset = 0;
                        privacyPanel.yOffset = 0;
                        privacyPanel.open();

                        buttonExit.setVisible(true);
                    }
                }
            });
        }

        /*
         * Instructions button.
         * Displays the Instructions / Game objectives on
         * screen, for the players reference.
         */
        if (buttonStoryLine != null)
        {
            buttonStoryLine.addListener(new ClickListener()
              {
                  public void clicked(InputEvent event, float x, float y)
                  {
                      Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                      if (storyPanel == null)
                      {
                          showActors(false);
                          justFinishedStoryPanel = false;
                          activePanel = ScreenID._INSTRUCTIONS_SCREEN;

                          storyPanel = new InstructionsPanel(app);
                          storyPanel.xOffset = 0;
                          storyPanel.yOffset = 0;
                          storyPanel.open();

                          buttonExit.setVisible(true);
                      }
                  }
              });
        }

        if (buttonSignOut != null)
        {
            buttonSignOut.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    app.googleServices.signOut();

                    buttonSignOut.addAction(Actions.removeActor());
                    buttonSignOut = null;
                }
            });
        }

        /*
         * Test Button.
         * Provides a button for accessing DEV MODE ONLY tests
         */
        if (testButton != null)
        {
            testButton.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    showActors(false);
                    justFinishedTestPanel = false;
                    activePanel = ScreenID._TEST_PANEL;

                    testPanel = new DeveloperTests(app);
                    testPanel.xOffset = 0;
                    testPanel.yOffset = 0;
                    testPanel.setup();

                    buttonExit.setVisible(true);
                }
            });
        }

        /*
         * Developer Options Button.
         * Provides a button for accessing DEV MODE ONLY game option settings
         */
        if (buttonDevOptions != null)
        {
            buttonDevOptions.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    if (!AppConfig.developerPanelActive)
                    {
                        AppConfig.developerPanelActive = true;
                        enteredDeveloperPanel = true;

                        showActors(false);

                        app.developerPanel.setup();
                    }
                }
            });
        }

        if (buttonExit != null)
        {
            buttonExit.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    updateSettings();

                    switch (activePanel)
                    {
                        case _SETTINGS_SCREEN:
                        {
                            hide();
                        }
                        break;

                        case _STATS_SCREEN:
                        {
                            justFinishedStatsPanel = true;
                        }
                        break;

                        case _INSTRUCTIONS_SCREEN:
                        {
                            justFinishedStoryPanel = true;
                        }
                        break;

                        case _PRIVACY_POLICY_SCREEN:
                        {
                            justFinishedPrivacyPanel = true;
                        }
                        break;

                        case _TEST_PANEL:
                        {
                            justFinishedTestPanel = true;
                        }
                        break;

                        default:
                            break;
                    }
                }
            });
        }
    }

    /**
     * Create checkbox listeners.
     */
    private void createCheckboxListeners()
    {
        if (musicCheckBox != null)
        {
            musicCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                        if (musicCheckBox.isChecked() && (musicSlider.getValue() == 0))
                        {
                            musicSlider.setValue(musicSlider.getMaxValue() / 10);
                            Sfx.inst().setMusicVolume((int) musicSlider.getValue());
                        }
                        else if (!musicCheckBox.isChecked() && (musicSlider.getValue() > 0))
                        {
                            musicSlider.setValue(0);
                            Sfx.inst().setMusicVolume(Sfx.inst()._SILENT);
                        }
                    }
                }
            });
        }

        if (fxCheckBox != null)
        {
            fxCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                        if (fxCheckBox.isChecked() && (fxSlider.getValue() == 0))
                        {
                            fxSlider.setValue(fxSlider.getMaxValue() / 10);
                            Sfx.inst().setFXVolume((int) fxSlider.getValue());
                        }
                        else if (!fxCheckBox.isChecked() && (fxSlider.getValue() > 0))
                        {
                            fxSlider.setValue(0);
                            Sfx.inst().setFXVolume(Sfx.inst()._SILENT);
                        }
                    }
                }
            });
        }
    }

    /**
     * Create slider listeners.
     */
    private void createSliderListeners()
    {
        if (musicSlider != null)
        {
            musicSlider.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        musicLabel.setText("" + ((int) musicSlider.getValue() * 10) + "%");
                        Sfx.inst().setMusicVolume((int) musicSlider.getValue());

                        musicCheckBox.setChecked(musicSlider.getValue() > 0);

                        updateSettings();
                    }
                }
            });
        }

        if (fxSlider != null)
        {
            fxSlider.addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (setupCompleted)
                    {
                        fxLabel.setText("" + ((int) fxSlider.getValue() * 10) + "%");
                        Sfx.inst().setFXVolume((int) fxSlider.getValue());

                        long id = Sfx.inst().startSound(Sfx.inst().SFX_TEST_SOUND);
                        Sfx.inst().sounds[Sfx.inst().SFX_TEST_SOUND].setVolume(id, Sfx.inst().getUsableFxVolume());

                        fxCheckBox.setChecked(fxSlider.getValue() > 0);

                        updateSettings();
                    }
                }
            });
        }
    }

    @Override
    public void dispose()
    {
    }
}
