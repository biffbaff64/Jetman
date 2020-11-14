
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
import com.richikin.utilslib.ui.IUIPage;

public class OptionsPage implements IUIPage
{
    private ImageButton buttonExit;
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

    private boolean justFinishedStatsPanel;
    private boolean justFinishedPrivacyPanel;
    private boolean justFinishedStoryPanel;
    private boolean enteredDeveloperPanel;
    private boolean setupCompleted;

    /**
     * Instantiates a new Options page.
     */
    public OptionsPage()
    {
    }

    /**
     * Update panel.
     */
    @Override
    public boolean update()
    {
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

        if (enteredDeveloperPanel && !Developer.developerPanelActive)
        {
            enteredDeveloperPanel = false;
            showActors(true);
        }

        return false;
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

        foreground = App.assets.loadSingleAsset("data/options_foreground.png", Texture.class);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        populateTable();

        createButtonListeners();
        createSliderListeners();
        createCheckboxListeners();

        updateSettingsOnEntry();

        activePanel = ScreenID._SETTINGS_SCREEN;

        App.optionsPageActive = true;
        Developer.developerPanelActive = false;

        enteredDeveloperPanel = false;
        setupCompleted = true;
    }

    /**
     * Hide.
     */
    public void hide()
    {
        if (App.optionsPageActive)
        {
            buttonExit.addAction(Actions.removeActor());
            buttonStats.addAction(Actions.removeActor());
            buttonPrivacy.addAction(Actions.removeActor());
            buttonStoryLine.addAction(Actions.removeActor());
            buttonDevOptions.addAction(Actions.removeActor());
            buttonExit = null;
            buttonStats = null;
            buttonPrivacy = null;
            buttonStoryLine = null;
            buttonDevOptions = null;

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

            controllerCheckBox.addAction(Actions.removeActor());
            vibrateCheckBox.addAction(Actions.removeActor());
            hintsCheckBox.addAction(Actions.removeActor());
            controllerCheckBox = null;
            vibrateCheckBox = null;
            hintsCheckBox = null;

            if (buttonSignOut != null)
            {
                buttonSignOut.addAction(Actions.removeActor());
            }
            buttonSignOut = null;

            App.assets.unloadAsset("data/settings_screen_template.png");

            foreground = null;
            skin = null;
            statsPanel = null;
            privacyPanel = null;
            storyPanel = null;

            App.optionsPageActive = false;
        }
    }

    /**
     * Populate table.
     */
    private void populateTable()
    {
        Scene2DUtils.setup();

        // ----------
        musicSlider = Scene2DUtils.addSlider((int) AppSystem.hudOriginX + 700, (int) AppSystem.hudOriginY + (720 - 208), skin);
        musicCheckBox = Scene2DUtils.addCheckBox((int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 208), Color.WHITE, skin);
        musicLabel = Scene2DUtils.addTextField("0%", (int) AppSystem.hudOriginX + 1000, (int) AppSystem.hudOriginY + (720 - 208), Color.WHITE, true, skin);
        musicLabel.setSize(64, 48);

        // ----------
        fxSlider = Scene2DUtils.addSlider((int) AppSystem.hudOriginX + 700, (int) AppSystem.hudOriginY + (720 - 278), skin);
        fxCheckBox = Scene2DUtils.addCheckBox((int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 278), Color.WHITE, skin);
        fxLabel = Scene2DUtils.addTextField("0%", (int) AppSystem.hudOriginX + 1000, (int) AppSystem.hudOriginY + (720 - 278), Color.WHITE, true, skin);
        fxLabel.setSize(64, 48);

        // ----------
        controllerCheckBox = Scene2DUtils.addCheckBox((int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 428), Color.WHITE, skin);
        vibrateCheckBox = Scene2DUtils.addCheckBox((int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 498), Color.WHITE, skin);
        hintsCheckBox = Scene2DUtils.addCheckBox((int) AppSystem.hudOriginX + 600, (int) AppSystem.hudOriginY + (720 - 568), Color.WHITE, skin);

        // ----------
        buttonStats = Scene2DUtils.addButton("new_stats_button", "new_stats_button_pressed", (int) AppSystem.hudOriginX + 986, (int) AppSystem.hudOriginY + (720 - 540));
        buttonPrivacy = Scene2DUtils.addButton("new_privacy_policy_button", "new_privacy_policy_button_pressed", (int) AppSystem.hudOriginX + 986, (int) AppSystem.hudOriginY + (720 - 600));
        buttonStoryLine = Scene2DUtils.addButton("new_objectives_button", "new_objectives_button_pressed", (int) AppSystem.hudOriginX + 986, (int) AppSystem.hudOriginY + (720 - 660));
        buttonExit = Scene2DUtils.addButton("new_back_button", "new_back_button_pressed", (int) AppSystem.hudOriginX + 20, (int) AppSystem.hudOriginY + (720 - 100));

        buttonStats.setSize(210, 40);
        buttonPrivacy.setSize(210, 40);
        buttonStoryLine.setSize(210, 40);
        buttonExit.setSize(128, 64);

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
        }

        showActors(true);
    }

    /**
     * Update settings.
     */
    private void updateSettings()
    {
        App.getPrefs().putBoolean(Settings._MUSIC_ENABLED, (GameAudio.inst().getMusicVolume() != AudioData._SILENT));
        App.getPrefs().putBoolean(Settings._SOUNDS_ENABLED, (GameAudio.inst().getFXVolume() != AudioData._SILENT));

        GameAudio.inst().setMusicVolume((int) musicSlider.getValue());
        GameAudio.inst().setFXVolume((int) fxSlider.getValue());

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
                        justFinishedStatsPanel = false;
                        activePanel = ScreenID._STATS_SCREEN;

                        statsPanel = new StatsPanel();
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
                    if (privacyPanel == null)
                    {
                        showActors(false);
                        justFinishedPrivacyPanel = false;
                        activePanel = ScreenID._PRIVACY_POLICY_SCREEN;

                        privacyPanel = new PrivacyPolicyPanel();
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
                      if (storyPanel == null)
                      {
                          showActors(false);
                          justFinishedStoryPanel = false;
                          activePanel = ScreenID._INSTRUCTIONS_SCREEN;

                          storyPanel = new InstructionsPanel();
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

        if (buttonExit != null)
        {
            buttonExit.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
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
    }
}
