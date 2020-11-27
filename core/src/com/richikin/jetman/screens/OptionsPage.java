
package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.richikin.enumslib.ScreenID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.ui.DeveloperPanel;
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
    private ImageButton        buttonStats;
    private ImageButton        buttonPrivacy;
    private ImageButton        buttonStoryLine;
    private ImageButton        buttonDevOptions;
    private ImageButton        buttonSignOut;
    private CheckBox           musicCheckBox;
    private CheckBox           fxCheckBox;
    private CheckBox           vibrateCheckBox;
    private CheckBox           hintsCheckBox;
    private Texture            foreground;
    private Skin               skin;
    private StatsPanel         statsPanel;
    private PrivacyPolicyPanel privacyPanel;
    private InstructionsPanel  storyPanel;
    private ScreenID           activePanel;
    private boolean            isJustFinishedOptionsPanel;
    private boolean            enteredDeveloperPanel;
    private boolean            setupCompleted;

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

        if (AppSystem.currentScreenID == ScreenID._MAIN_MENU)
        {
            AppSystem.backButton.setVisible(true);
            AppSystem.backButton.setDisabled(false);
            AppSystem.backButton.setChecked(false);

            if (AppSystem.backButton.getClickListener() != null)
            {
                AppSystem.backButton.removeListener(AppSystem.backButton.getClickListener());
            }
        }

        foreground = App.assets.loadSingleAsset(GameAssets._OPTIONS_PANEL_ASSET, Texture.class);

        skin = new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET));

        populateTable();
        createButtonListeners();
        createCheckboxListeners();
        updateSettingsOnEntry();

        activePanel = ScreenID._SETTINGS_SCREEN;

        Developer.developerPanelActive = false;
        enteredDeveloperPanel          = false;
        setupCompleted                 = true;
        isJustFinishedOptionsPanel     = false;
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
                    statsPanel                 = null;
                    isJustFinishedOptionsPanel = false;
                }
                break;

                case _PRIVACY_POLICY_SCREEN:
                {
                    if (privacyPanel != null)
                    {
                        privacyPanel.dispose();
                    }
                    privacyPanel               = null;
                    isJustFinishedOptionsPanel = false;
                }
                break;

                case _INSTRUCTIONS_SCREEN:
                {
                    if (storyPanel != null)
                    {
                        storyPanel.dispose();
                    }
                    storyPanel                 = null;
                    isJustFinishedOptionsPanel = false;
                }
                break;

                default:
                    updateSettings();
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

            if (AppSystem.currentScreenID == ScreenID._MAIN_MENU)
            {
                AppSystem.backButton.setVisible(true);
                AppSystem.backButton.setDisabled(false);
            }
        }

        return isJustFinishedOptionsPanel;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:
            {
                statsPanel.draw();
            }
            break;

            case _PRIVACY_POLICY_SCREEN:
            {
                privacyPanel.draw();
            }
            break;

            case _INSTRUCTIONS_SCREEN:
            {
                storyPanel.draw();
            }
            break;

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
        musicCheckBox   = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 800, (int) AppSystem.hudOriginY + (720 - 279), Color.WHITE, skin);
        fxCheckBox      = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 800, (int) AppSystem.hudOriginY + (720 - 330), Color.WHITE, skin);
        vibrateCheckBox = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 800, (int) AppSystem.hudOriginY + (720 - 382), Color.WHITE, skin);
        hintsCheckBox   = Scene2DUtils.addCheckBox("toggle_on", "toggle_off", (int) AppSystem.hudOriginX + 800, (int) AppSystem.hudOriginY + (720 - 435), Color.WHITE, skin);

        // ----------
        if (AppSystem.currentScreenID == ScreenID._MAIN_MENU)
        {
            buttonPrivacy   = Scene2DUtils.addButton("new_privacy_policy_button", "new_privacy_policy_button_pressed", (int) AppSystem.hudOriginX + 401, (int) AppSystem.hudOriginY + (720 - 511));
            buttonStoryLine = Scene2DUtils.addButton("new_objectives_button", "new_objectives_button_pressed", (int) AppSystem.hudOriginX + 401, (int) AppSystem.hudOriginY + (720 - 558));

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
        }

        // ----------
        if (Developer.isDevMode())
        {
            buttonDevOptions = Scene2DUtils.addButton
                (
                    "new_developer_options_button",
                    "new_developer_options_button_pressed",
                    (int) AppSystem.hudOriginX + 648,
                    (int) AppSystem.hudOriginY + (720 - 511)
                );

            buttonStats = Scene2DUtils.addButton
                (
                    "new_stats_button",
                    "new_stats_button_pressed",
                    (int) AppSystem.hudOriginX + 648,
                    (int) AppSystem.hudOriginY + (720 - 558)
                );
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

        musicCheckBox.setChecked(GameAudio.inst().getMusicVolume() > 0);
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
            buttonStats.setVisible(_visibilty);
        }

        if (buttonSignOut != null)
        {
            buttonSignOut.setVisible(_visibilty);
        }

        if (buttonPrivacy != null)
        {
            buttonPrivacy.setVisible(_visibilty);
        }

        if (buttonStoryLine != null)
        {
            buttonStoryLine.setVisible(_visibilty);
        }

        musicCheckBox.setVisible(_visibilty);
        fxCheckBox.setVisible(_visibilty);
        vibrateCheckBox.setVisible(_visibilty);
        hintsCheckBox.setVisible(_visibilty);
    }

    /**
     * Create button listeners.
     */
    private void createButtonListeners()
    {
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
                        activePanel = ScreenID._STATS_SCREEN;

                        statsPanel = new StatsPanel();
                        statsPanel.open();
                    }
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
                        enteredDeveloperPanel          = true;

                        showActors(false);

                        AppSystem.backButton.setVisible(false);
                        AppSystem.backButton.setDisabled(true);

                        DeveloperPanel.inst().setup();
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

                        if (musicCheckBox.isChecked())
                        {
                            GameAudio.inst().setMusicVolume(AudioData._MAX_VOLUME);
                        }
                        else if (!musicCheckBox.isChecked())
                        {
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

                        if (fxCheckBox.isChecked())
                        {
                            GameAudio.inst().setFXVolume(AudioData._MAX_VOLUME);
                        }
                        else if (!fxCheckBox.isChecked())
                        {
                            GameAudio.inst().setFXVolume(AudioData._SILENT);
                        }
                    }
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

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

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

        musicCheckBox.addAction(Actions.removeActor());
        musicCheckBox = null;
        fxCheckBox.addAction(Actions.removeActor());
        fxCheckBox = null;

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

        App.assets.unloadAsset(GameAssets._OPTIONS_PANEL_ASSET);

        foreground   = null;
        skin         = null;
        statsPanel   = null;
        privacyPanel = null;
        storyPanel   = null;
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
