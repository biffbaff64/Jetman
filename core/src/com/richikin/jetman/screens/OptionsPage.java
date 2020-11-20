
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
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.DeveloperPanel;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.ui.InstructionsPanel;
import com.richikin.jetman.ui.PrivacyPolicyPanel;
import com.richikin.jetman.ui.Scene2DUtils;
import com.richikin.jetman.ui.StatsPanel;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIPage;

public class OptionsPage implements IUIPage
{
    private ImageButton buttonStats;
    private ImageButton buttonPrivacy;
    private ImageButton buttonStoryLine;
    private ImageButton buttonDevOptions;
    private ImageButton buttonSignOut;

    private CheckBox  musicCheckBox;
    private TextField musicLabel;
    private Slider    musicSlider;
    private CheckBox  fxCheckBox;
    private TextField fxLabel;
    private Slider    fxSlider;
    private CheckBox  controllerCheckBox;
    private CheckBox  vibrateCheckBox;
    private CheckBox  hintsCheckBox;

    private Texture   foreground;
    private Skin      skin;

    private StatsPanel         statsPanel;
    private PrivacyPolicyPanel privacyPanel;
    private InstructionsPanel  storyPanel;
    private ScreenID           activePanel;

    private Array<Actor> actors;

    private boolean isJustFinishedOptionsPanel;
//    private boolean justFinishedStatsPanel;
//    private boolean justFinishedPrivacyPanel;
//    private boolean justFinishedStoryPanel;
    private boolean enteredDeveloperPanel;
    private boolean setupCompleted;

    /**
     * Instantiates a new Options page.
     */
    public OptionsPage()
    {
    }

    @Override
    public void initialise()
    {
        setupCompleted = false;

        AppSystem.backButton.setVisible(true);
        AppSystem.backButton.setDisabled(false);
        AppSystem.backButton.setChecked(false);

        if (AppSystem.backButton.getClickListener() != null)
        {
            AppSystem.backButton.removeListener(AppSystem.backButton.getClickListener());
        }

        foreground = App.assets.loadSingleAsset("data/options_foreground.png", Texture.class);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        actors = new Array<>();

        populateTable();

        createButtonListeners();
        createSliderListeners();
        createCheckboxListeners();

        updateSettingsOnEntry();

        activePanel = ScreenID._SETTINGS_SCREEN;

        Developer.developerPanelActive = false;
        enteredDeveloperPanel = false;
        setupCompleted = true;
        isJustFinishedOptionsPanel = false;
    }

    /**
     * Update panel.
     */
    @Override
    public boolean update()
    {
        if (AppSystem.backButton.isChecked())
        {
            switch (activePanel)
            {
                case _STATS_SCREEN:
                {
                    if (statsPanel != null)
                    {
                        statsPanel.dispose();
                    }
                    statsPanel = null;
                    isJustFinishedOptionsPanel = false;
                }
                break;

                case _PRIVACY_POLICY_SCREEN:
                {
                    if (privacyPanel != null)
                    {
                        privacyPanel.dispose();
                    }
                    privacyPanel = null;
                    isJustFinishedOptionsPanel = false;
                }
                break;

                case _INSTRUCTIONS_SCREEN:
                {
                    if (storyPanel != null)
                    {
                        storyPanel.dispose();
                    }
                    storyPanel = null;
                    isJustFinishedOptionsPanel = false;
                }
                break;

                default:
                    isJustFinishedOptionsPanel = true;
                    break;
            }

            if (!isJustFinishedOptionsPanel)
            {
                showActors(true);
                activePanel = ScreenID._SETTINGS_SCREEN;
                AppSystem.backButton.setChecked(false);
            }
        }

        if (enteredDeveloperPanel && !Developer.developerPanelActive)
        {
            enteredDeveloperPanel = false;
            showActors(true);
        }

        return isJustFinishedOptionsPanel;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:             statsPanel.draw();      break;
            case _PRIVACY_POLICY_SCREEN:    privacyPanel.draw();    break;
            case _INSTRUCTIONS_SCREEN:      storyPanel.draw();      break;
            case _DEVELOPER_PANEL:

            default:
            {
                if (Developer.developerPanelActive)
                {
                    DeveloperPanel.inst().draw(spriteBatch);
                }
                else
                {
                    if (foreground != null)
                    {
                        spriteBatch.draw(foreground, AppSystem.hudOriginX, AppSystem.hudOriginY);
                    }
                }
            }
            break;
        }
    }

    /**
     * Show.
     */
    public void show()
    {
        showActors(true);
    }

    /**
     * Hide.
     */
    public void hide()
    {
        showActors(false);
    }

    /**
     * Populate table.
     */
    private void populateTable()
    {
        Trace.__FILE_FUNC();

        // ----------
        musicSlider = Scene2DUtils.addSlider((int) AppSystem.hudOriginX + 700, (int) AppSystem.hudOriginY + (720 - 208), skin);
        musicCheckBox = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 208), Color.WHITE, skin);
        musicLabel = Scene2DUtils.addTextField("0%", (int) AppSystem.hudOriginX + 1000, (int) AppSystem.hudOriginY + (720 - 208), Color.WHITE, true, skin);
        musicLabel.setSize(64, 48);
        actors.add(musicSlider);
        actors.add(musicCheckBox);
        actors.add(musicLabel);


        // ----------
        fxSlider = Scene2DUtils.addSlider((int) AppSystem.hudOriginX + 700, (int) AppSystem.hudOriginY + (720 - 278), skin);
        fxCheckBox = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 278), Color.WHITE, skin);
        fxLabel = Scene2DUtils.addTextField("0%", (int) AppSystem.hudOriginX + 1000, (int) AppSystem.hudOriginY + (720 - 278), Color.WHITE, true, skin);
        fxLabel.setSize(64, 48);
        actors.add(fxSlider);
        actors.add(fxCheckBox);
        actors.add(fxLabel);

        // ----------
        controllerCheckBox = Scene2DUtils.addCheckBox("toggle_left", "toggle_right", (int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 428), Color.WHITE, skin);
        vibrateCheckBox = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 498), Color.WHITE, skin);
        hintsCheckBox = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 568), Color.WHITE, skin);

        actors.add(controllerCheckBox);
        actors.add(vibrateCheckBox);
        actors.add(hintsCheckBox);

        // ----------
        buttonStats = Scene2DUtils.addButton("new_stats_button", "new_stats_button_pressed", (int) AppSystem.hudOriginX + 986, (int) AppSystem.hudOriginY + (720 - 540));
        buttonPrivacy = Scene2DUtils.addButton("new_privacy_policy_button", "new_privacy_policy_button_pressed", (int) AppSystem.hudOriginX + 986, (int) AppSystem.hudOriginY + (720 - 600));
        buttonStoryLine = Scene2DUtils.addButton("new_objectives_button", "new_objectives_button_pressed", (int) AppSystem.hudOriginX + 986, (int) AppSystem.hudOriginY + (720 - 660));

        buttonStats.setSize(210, 40);
        buttonPrivacy.setSize(210, 40);
        buttonStoryLine.setSize(210, 40);

        actors.add(buttonStats);
        actors.add(buttonPrivacy);
        actors.add(buttonStoryLine);

        // ----------
        if (App.googleServices.isSignedIn())
        {
            if (Developer.isDevMode() || (Version.majorVersion == 0))
            {
                buttonSignOut = Scene2DUtils.addButton
                    (
                        "btn_google_signout_dark",
                        "btn_google_signout_dark_pressed",
                        (int) AppSystem.hudOriginX + 986,
                        (int) AppSystem.hudOriginY + (720 - 80)
                    );

                buttonSignOut.setSize(191, 46);

                actors.add(buttonSignOut);
            }
        }

        // ----------
        if (Developer.isDevMode())
        {
            buttonDevOptions = Scene2DUtils.addButton
                (
                    "new_developer_options_button",
                    "new_developer_options_button_pressed",
                    (int) AppSystem.hudOriginX + 986,
                    (int) AppSystem.hudOriginY + (720 - 420)
                );

            buttonDevOptions.setSize(210, 40);

            actors.add(buttonDevOptions);
        }

        showActors(true);
    }

    /**
     * Update settings.
     */
    private void updateSettings()
    {
        App.settings.getPrefs().putBoolean(Settings._MUSIC_ENABLED, (GameAudio.inst().getMusicVolume() != AudioData._SILENT));
        App.settings.getPrefs().putBoolean(Settings._SOUNDS_ENABLED, (GameAudio.inst().getFXVolume() != AudioData._SILENT));

        App.settings.getPrefs().putBoolean(Settings._VIBRATIONS, vibrateCheckBox.isChecked());
        App.settings.getPrefs().flush();
    }

    /**
     * Update settings on entry.
     */
    private void updateSettingsOnEntry()
    {
        vibrateCheckBox.setChecked(App.settings.isEnabled(Settings._VIBRATIONS));
        hintsCheckBox.setChecked(App.settings.isEnabled(Settings._SHOW_HINTS));

        musicSlider.setValue(GameAudio.inst().getMusicVolume());
        musicLabel.setText("" + ((int) musicSlider.getValue() * 10) + "%");
        musicCheckBox.setChecked(GameAudio.inst().getMusicVolume() > 0);

        fxSlider.setValue(GameAudio.inst().getFXVolume());
        fxLabel.setText("" + ((int) fxSlider.getValue() * 10) + "%");
        fxCheckBox.setChecked(GameAudio.inst().getFXVolume() > 0);
    }

    /**
     * Show the OptionsPage actors.
     *
     * @param _visibilty Either TRUE or FALSE
     */
    private void showActors(boolean _visibilty)
    {
        if (Developer.isDevMode())
        {
            buttonDevOptions.setVisible(_visibilty);
        }

        if (buttonSignOut != null)
        {
            buttonSignOut.setVisible(_visibilty);
        }

        buttonStats.setVisible(_visibilty);
        buttonPrivacy.setVisible(_visibilty);
        buttonStoryLine.setVisible(_visibilty);

        musicLabel.setVisible(_visibilty);
        musicSlider.setVisible(_visibilty);
        musicCheckBox.setVisible(_visibilty);

        fxLabel.setVisible(_visibilty);
        fxSlider.setVisible(_visibilty);
        fxCheckBox.setVisible(_visibilty);

        controllerCheckBox.setVisible(_visibilty);
        vibrateCheckBox.setVisible(_visibilty);
        hintsCheckBox.setVisible(_visibilty);
    }

    /**
     * Create button listeners.
     */
    private void createButtonListeners()
    {
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
                    if (statsPanel == null)
                    {
                        showActors(false);
//                        justFinishedStatsPanel = false;
                        activePanel = ScreenID._STATS_SCREEN;

                        statsPanel = new StatsPanel();
                        statsPanel.open();
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
                    if (privacyPanel == null)
                    {
                        showActors(false);
//                        justFinishedPrivacyPanel = false;
                        activePanel = ScreenID._PRIVACY_POLICY_SCREEN;

                        privacyPanel = new PrivacyPolicyPanel();
                        privacyPanel.open();
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
                      if (storyPanel == null)
                      {
                          showActors(false);
//                          justFinishedStoryPanel = false;
                          activePanel = ScreenID._INSTRUCTIONS_SCREEN;

                          storyPanel = new InstructionsPanel();
                          storyPanel.open();
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
                    App.googleServices.signOut();

                    buttonSignOut.addAction(Actions.removeActor());
                    buttonSignOut = null;
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
                    if (!Developer.developerPanelActive)
                    {
                        Developer.developerPanelActive = true;
                        enteredDeveloperPanel = true;

                        showActors(false);

                        DeveloperPanel.inst().setup();
                    }
                }
            });
        }

//        AppSystem.backButton.addListener(new ClickListener()
//        {
//            public void clicked(InputEvent event, float x, float y)
//            {
//                updateSettings();
//
//                switch (activePanel)
//                {
//                    case _SETTINGS_SCREEN:
//                    {
//                        isJustFinishedOptionsPanel = true;
//                        hide();
//                    }
//                    break;
//
//                    case _STATS_SCREEN:
//                    {
//                        justFinishedStatsPanel = true;
//                    }
//                    break;
//
//                    case _INSTRUCTIONS_SCREEN:
//                    {
//                        justFinishedStoryPanel = true;
//                    }
//                    break;
//
//                    case _PRIVACY_POLICY_SCREEN:
//                    {
//                        justFinishedPrivacyPanel = true;
//                    }
//                    break;
//
//                    default:
//                        break;
//                }
//            }
//        });
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
                        GameAudio.inst().startSound(AudioData.SFX_BEEP);

                        if (musicCheckBox.isChecked() && (musicSlider.getValue() == 0))
                        {
                            musicSlider.setValue(musicSlider.getMaxValue() / 10);
                            GameAudio.inst().setMusicVolume((int) musicSlider.getValue());
                        }
                        else if (!musicCheckBox.isChecked() && (musicSlider.getValue() > 0))
                        {
                            musicSlider.setValue(0);
                            GameAudio.inst().setMusicVolume(AudioData._SILENT);
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
                        GameAudio.inst().startSound(AudioData.SFX_BEEP);

                        if (fxCheckBox.isChecked() && (fxSlider.getValue() == 0))
                        {
                            fxSlider.setValue(fxSlider.getMaxValue() / 10);
                            GameAudio.inst().setFXVolume((int) fxSlider.getValue());
                        }
                        else if (!fxCheckBox.isChecked() && (fxSlider.getValue() > 0))
                        {
                            fxSlider.setValue(0);
                            GameAudio.inst().setFXVolume(AudioData._SILENT);
                        }
                    }
                }
            });
        }

        if (controllerCheckBox != null)
        {
            controllerCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                }
            });
        }

        if (vibrateCheckBox != null)
        {
            vibrateCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                }
            });
        }

        if (hintsCheckBox != null)
        {
            hintsCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
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
                        GameAudio.inst().setMusicVolume((int) musicSlider.getValue());

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
                        GameAudio.inst().setFXVolume((int) fxSlider.getValue());

                        long id = GameAudio.inst().startSound(AudioData.SFX_TEST_SOUND);
                        AudioData.sounds[AudioData.SFX_TEST_SOUND].setVolume(id, GameAudio.inst().getUsableFxVolume());

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
        AppSystem.backButton.setVisible(false);
        AppSystem.backButton.setDisabled(true);

        if (buttonStats != null)
        {
            buttonStats.addAction(Actions.removeActor());
            buttonStats = null;
        }

        if (buttonPrivacy != null)
        {
            buttonPrivacy.addAction(Actions.removeActor());
            buttonPrivacy = null;
        }

        if (buttonStoryLine != null)
        {
            buttonStoryLine.addAction(Actions.removeActor());
            buttonStoryLine = null;
        }

        if (buttonDevOptions != null)
        {
            buttonDevOptions.addAction(Actions.removeActor());
            buttonDevOptions = null;
        }

        if (musicLabel != null)
        {
            musicLabel.addAction(Actions.removeActor());
            musicSlider.addAction(Actions.removeActor());
            musicCheckBox.addAction(Actions.removeActor());
            musicLabel    = null;
            musicSlider   = null;
            musicCheckBox = null;
        }

        if (fxLabel != null)
        {
            fxLabel.addAction(Actions.removeActor());
            fxSlider.addAction(Actions.removeActor());
            fxCheckBox.addAction(Actions.removeActor());
            fxLabel    = null;
            fxSlider   = null;
            fxCheckBox = null;
        }

        if (controllerCheckBox != null)
        {
            controllerCheckBox.addAction(Actions.removeActor());
            controllerCheckBox = null;
        }

        if (vibrateCheckBox != null)
        {
            vibrateCheckBox.addAction(Actions.removeActor());
            vibrateCheckBox = null;
        }

        if (hintsCheckBox != null)
        {
            hintsCheckBox.addAction(Actions.removeActor());
            hintsCheckBox = null;
        }

        if (buttonSignOut != null)
        {
            buttonSignOut.addAction(Actions.removeActor());
            buttonSignOut = null;
        }

        App.assets.unloadAsset("data/settings_screen_template.png");

        foreground = null;
        skin = null;
        statsPanel = null;
        privacyPanel = null;
        storyPanel = null;
    }

    public void setActivePanel(ScreenID screenID)
    {
        activePanel = screenID;
    }

    public ScreenID getActivePanel()
    {
        return activePanel;
    }
}
