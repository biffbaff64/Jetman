package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.ui.DefaultPanel;

public class PausePanel extends DefaultPanel
{
    public ImageButton buttonMusicVolume;
    public ImageButton buttonFXVolume;
    public ImageButton buttonHome;

    private static final int _MSG   = 0;
    private static final int _MUSIC = 1;
    private static final int _FX    = 2;
    private static final int _EXIT  = 3;

    private static final int[][] displayPos =
        {
            {128, (720 - 354), 96, 96},   // Message
            {450, (720 - 500), 96, 96},   // Music
            {600, (720 - 500), 96, 96},   // FX
            {750, (720 - 500), 96, 96},   // Exit
        };

    private TextureRegion pauseMessage;

    public PausePanel()
    {
    }

    @Override
    public void setup()
    {
        pauseMessage = App.assets.getTextRegion("paused");

        buttonMusicVolume = Scene2DUtils.addButton
            (
                "buttonMusicOn",
                "buttonMusicOff",
                (int) AppSystem.hudOriginX + displayPos[_MUSIC][0],
                (int) AppSystem.hudOriginY + displayPos[_MUSIC][1]
            );

        buttonFXVolume = Scene2DUtils.addButton
            (
                "buttonFXOn",
                "buttonFXOff",
                (int) AppSystem.hudOriginX + displayPos[_FX][0],
                (int) AppSystem.hudOriginY + displayPos[_FX][1]
            );

        buttonHome = Scene2DUtils.addButton
            (
                "buttonHome",
                "buttonHomePressed",
                (int) AppSystem.hudOriginX + displayPos[_EXIT][0],
                (int) AppSystem.hudOriginY + displayPos[_EXIT][1]
            );

        buttonMusicVolume.setSize(displayPos[_MUSIC][2], displayPos[_MUSIC][3]);
        buttonFXVolume.setSize(displayPos[_FX][2], displayPos[_FX][3]);
        buttonHome.setSize(displayPos[_EXIT][2], displayPos[_EXIT][3]);

        buttonMusicVolume.setChecked(GameAudio.inst().getMusicVolume() != 0);
        buttonFXVolume.setChecked(GameAudio.inst().getFXVolume() != 0);
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
        if (pauseMessage != null)
        {
            spriteBatch.draw(pauseMessage, originX + displayPos[_MSG][0], originY + displayPos[_MSG][1]);
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

        buttonHome = null;
        buttonMusicVolume = null;
        buttonFXVolume = null;
        pauseMessage = null;
    }
}
