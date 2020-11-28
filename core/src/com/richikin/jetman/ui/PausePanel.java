package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.ui.DefaultPanel;

public class PausePanel extends DefaultPanel
{
    public CheckBox    buttonMusicVolume;
    public CheckBox    buttonFXVolume;
    public CheckBox    buttonVibrations;
    public CheckBox    buttonGameHints;
    public ImageButton buttonHome;

    private static final int _MUSIC   = 0;
    private static final int _FX      = 1;
    private static final int _VIBRATE = 2;
    private static final int _HINTS   = 3;
    private static final int _EXIT    = 4;

    private static final int[][] displayPos =
        {
            {800, (720 - 269), 55, 24},   // Music
            {800, (720 - 320), 55, 24},   // FX
            {800, (720 - 372), 55, 24},   // FX
            {800, (720 - 425), 55, 24},   // FX
            {530, (720 - 482), 220, 34},   // Exit
        };

    private Texture pausePanel;

    public PausePanel()
    {
    }

    @Override
    public void setup()
    {
        pausePanel = App.assets.loadSingleAsset(GameAssets._PAUSE_PANEL_ASSET, Texture.class);

        Skin skin = new Skin(Gdx.files.internal(GameAssets._UISKIN_ASSET));

        buttonMusicVolume = Scene2DUtils.addCheckBox
            (
                "toggle_on",
                "toggle_off",
                (int) AppSystem.hudOriginX + displayPos[_MUSIC][0],
                (int) AppSystem.hudOriginY + displayPos[_MUSIC][1],
                Color.WHITE,
                skin
            );

        buttonFXVolume = Scene2DUtils.addCheckBox
            (
                "toggle_on",
                "toggle_off",
                (int) AppSystem.hudOriginX + displayPos[_FX][0],
                (int) AppSystem.hudOriginY + displayPos[_FX][1],
                Color.WHITE,
                skin
            );

        buttonVibrations = Scene2DUtils.addCheckBox
            (
                "toggle_on",
                "toggle_off",
                (int) AppSystem.hudOriginX + displayPos[_VIBRATE][0],
                (int) AppSystem.hudOriginY + displayPos[_VIBRATE][1],
                Color.WHITE,
                skin
            );

        buttonGameHints = Scene2DUtils.addCheckBox
            (
                "toggle_on",
                "toggle_off",
                (int) AppSystem.hudOriginX + displayPos[_HINTS][0],
                (int) AppSystem.hudOriginY + displayPos[_HINTS][1],
                Color.WHITE,
                skin
            );

        buttonHome = Scene2DUtils.addButton
            (
                "button_quit_to_title",
                "button_quit_to_title_pressed",
                (int) AppSystem.hudOriginX + displayPos[_EXIT][0],
                (int) AppSystem.hudOriginY + displayPos[_EXIT][1]
            );

        buttonMusicVolume.setChecked(GameAudio.inst().getMusicVolume() != 0);
        buttonFXVolume.setChecked(GameAudio.inst().getFXVolume() != 0);
        buttonVibrations.setChecked(App.settings.isEnabled(Settings._VIBRATIONS));
        buttonGameHints.setChecked(App.settings.isEnabled(Settings._SHOW_HINTS));
    }

    @Override
    public boolean update()
    {
        if (buttonMusicVolume.isChecked() && (GameAudio.inst().getMusicVolume() > 0))
        {
            GameAudio.inst().saveMusicVolume();
            GameAudio.inst().setMusicVolume(0);
        }
        else
        {
            if (!buttonMusicVolume.isChecked() && (GameAudio.inst().getMusicVolume() == 0))
            {
                GameAudio.inst().setMusicVolume(GameAudio.inst().getMusicVolumeSave());
            }
        }

        if (buttonFXVolume.isChecked() && (GameAudio.inst().getFXVolume() > 0))
        {
            GameAudio.inst().saveFXVolume();
            GameAudio.inst().setFXVolume(0);
        }
        else
        {
            if (!buttonFXVolume.isChecked() && (GameAudio.inst().getFXVolume() == 0))
            {
                GameAudio.inst().setFXVolume(GameAudio.inst().getFXVolumeSave());
            }
        }

        App.settings.getPrefs().putBoolean(Settings._VIBRATIONS, buttonVibrations.isChecked());
        App.settings.getPrefs().putBoolean(Settings._SHOW_HINTS, buttonGameHints.isChecked());

        if (buttonHome.isChecked())
        {
            setQuitToTitle();

            App.appState.set(StateID._STATE_LEVEL_RETRY);
            AppSystem.quitToMainMenu = true;
        }

        return false;
    }

    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera, float originX, float originY)
    {
        if (pausePanel != null)
        {
            spriteBatch.draw(pausePanel, originX, originY);
        }
    }

    private void setQuitToTitle()
    {
        if (App.getPlayer() != null)
        {
            App.getPlayer().setAction(ActionStates._DEAD);
        }

        App.gameProgress.toMinimum();

        App.getHud().getFuelBar().setToMinimum();
        App.getHud().getTimeBar().setToMinimum();

        buttonHome.setChecked(false);

        dispose();
    }

    @Override
    public void dispose()
    {
        if (buttonHome != null)
        {
            buttonHome.addAction(Actions.removeActor());
        }

        if (buttonMusicVolume != null)
        {
            buttonMusicVolume.addAction(Actions.removeActor());
        }

        if (buttonFXVolume != null)
        {
            buttonFXVolume.addAction(Actions.removeActor());
        }

        if (buttonVibrations != null)
        {
            buttonVibrations.addAction(Actions.removeActor());
        }

        if (buttonGameHints != null)
        {
            buttonGameHints.addAction(Actions.removeActor());
        }

        buttonHome        = null;
        buttonMusicVolume = null;
        buttonFXVolume    = null;
        buttonVibrations  = null;
        buttonGameHints   = null;
        pausePanel        = null;
    }
}
