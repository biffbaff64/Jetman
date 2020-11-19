package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.core.App;
import com.richikin.jetman.input.buttons.GameButton;
import com.richikin.utilslib.ui.DefaultPanel;

public class PausePanel extends DefaultPanel
{
    public GameButton buttonMusicVolume;
    public GameButton buttonFXVolume;
    public GameButton buttonHome;

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

        buttonMusicVolume = new GameButton
            (
                App.assets.getButtonRegion("buttonMusicOn"),
                App.assets.getButtonRegion("buttonMusicOff"),
                displayPos[_MUSIC][0], displayPos[_MUSIC][1]
            );

        buttonFXVolume = new GameButton
            (
                App.assets.getButtonRegion("buttonFXOn"),
                App.assets.getButtonRegion("buttonFXOff"),
                displayPos[_FX][0], displayPos[_FX][1]
            );

        buttonHome = new GameButton
            (
                App.assets.getButtonRegion("buttonHome"),
                App.assets.getButtonRegion("buttonHomePressed"),
                displayPos[_EXIT][0], displayPos[_EXIT][1]
            );

        buttonMusicVolume.setSize(96, 96);
        buttonFXVolume.setSize(96, 96);
        buttonHome.setSize(96, 96);

        buttonMusicVolume.pressConditional(GameAudio.inst().getMusicVolume() == 0);
        buttonFXVolume.pressConditional(GameAudio.inst().getFXVolume() == 0);
    }

    @Override
    public boolean update()
    {
        return false;
    }

    public void draw(SpriteBatch spriteBatch, OrthographicCamera camera, float originX, float originY)
    {
        spriteBatch.draw(pauseMessage, originX + displayPos[_MSG][0], originY + displayPos[_MSG][1]);

        buttonMusicVolume.draw();
        buttonFXVolume.draw();
        buttonHome.draw();
    }

    @Override
    public void dispose()
    {
        buttonHome.dispose();
        buttonMusicVolume.dispose();
        buttonFXVolume.dispose();

        buttonHome = null;
        buttonMusicVolume = null;
        buttonFXVolume = null;
        pauseMessage = null;
    }
}
