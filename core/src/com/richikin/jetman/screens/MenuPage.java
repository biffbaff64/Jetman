
package com.richikin.jetman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.ui.Scene2DUtils;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIPage;

import java.util.Calendar;
import java.util.Date;

public class MenuPage implements IUIPage, Disposable
{
    public ImageButton buttonStart;
    public ImageButton buttonOptions;
    public ImageButton buttonHiScores;
    public ImageButton buttonCredits;
    public ImageButton buttonExit;
    public ImageButton buttonGoogle;

    private Texture   foreground;
    private Image     decoration;

    public MenuPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.__FILE_FUNC();

        foreground = App.assets.loadSingleAsset("data/title_background.png", Texture.class);

        populateMenuScreen();
        addClickListeners();
    }

    @Override
    public boolean update()
    {
        updateGoogleButton();

        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, AppSystem.hudOriginX, AppSystem.hudOriginY);
        }
    }

    @Override
    public void show()
    {
        Trace.__FILE_FUNC();

        showItems(true);
    }

    @Override
    public void hide()
    {
        Trace.__FILE_FUNC();

        showItems(false);
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        if (buttonStart != null) buttonStart.addAction(Actions.removeActor());
        if (buttonOptions != null) buttonOptions.addAction(Actions.removeActor());
        if (buttonHiScores != null) buttonHiScores.addAction(Actions.removeActor());
        if (buttonCredits != null) buttonCredits.addAction(Actions.removeActor());
        if (buttonExit != null) buttonExit.addAction(Actions.removeActor());

        buttonStart   = null;
        buttonOptions = null;
        buttonHiScores = null;
        buttonCredits = null;
        buttonExit    = null;

        if (decoration != null)
        {
            decoration.addAction(Actions.removeActor());
            decoration = null;
        }

        App.assets.unloadAsset("data/title_background.png");

        foreground = null;
    }

    private void populateMenuScreen()
    {
        Trace.__FILE_FUNC();

        AppSystem.hudOriginX = (float) -(Gfx._HUD_WIDTH / 2);
        AppSystem.hudOriginY = (float) -(Gfx._HUD_HEIGHT / 2);

        buttonStart = Scene2DUtils.addButton("buttonStart", "buttonStart_pressed", (int) AppSystem.hudOriginX + 515, (int) AppSystem.hudOriginY + (720 - 379));
        buttonOptions = Scene2DUtils.addButton("buttonOptions", "buttonOptions_pressed", (int) AppSystem.hudOriginX + 558, (int) AppSystem.hudOriginY + (720 - 437));
        buttonExit    = Scene2DUtils.addButton("buttonExit", "buttonExit_pressed", (int) AppSystem.hudOriginX + 596, (int) AppSystem.hudOriginY + (720 - 614));
        buttonHiScores = Scene2DUtils.addButton("button_hiscores", "button_hiscores_pressed", (int) AppSystem.hudOriginX + 543, (int) AppSystem.hudOriginY + (720 - 496));
        buttonCredits = Scene2DUtils.addButton("button_credits", "button_credits_pressed", (int) AppSystem.hudOriginX + 558, (int) AppSystem.hudOriginY + (720 - 554));

        addDateSpecificItems(AppSystem.hudOriginX, AppSystem.hudOriginY);
    }

    private void addDateSpecificItems(float originX, float originY)
    {
        Trace.__FILE_FUNC();

        Date     date     = new Date(TimeUtils.millis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER)
        {
            if (calendar.get(Calendar.DAY_OF_MONTH) == 11)
            {
                decoration = Scene2DUtils.makeObjectsImage("poppy");
                decoration.setPosition(originX + 1160, originY + (720 - 90));
                App.stage.addActor(decoration);
            }
        }
        else
        {
            if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER)
            {
                if (calendar.get(Calendar.DAY_OF_MONTH) == 25)
                {
                    decoration = Scene2DUtils.makeObjectsImage("xmas_tree");
                    decoration.setPosition(originX + 1075, originY + (720 - 342));
                    App.stage.addActor(decoration);
                }
            }
        }
    }

    private void addClickListeners()
    {
        Trace.__FILE_FUNC();

        if (buttonStart != null)
        {
            buttonStart.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonStart.setChecked(true);
                }
            });
        }

        if (buttonOptions != null)
        {
            buttonOptions.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonOptions.setChecked(true);
                }
            });
        }

        if (buttonHiScores != null)
        {
            buttonHiScores.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonHiScores.setChecked(true);
                }
            });
        }

        if (buttonCredits != null)
        {
            buttonCredits.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonCredits.setChecked(true);
                }
            });
        }

        if (buttonExit != null)
        {
            buttonExit.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    GameAudio.inst().startSound(AudioData.SFX_BEEP);

                    buttonExit.setChecked(true);
                }
            });
        }
    }

    /**
     * Creates the Google button if needed.
     * Also removes the button if the game
     * has signed in to google play services.
     */
    private void updateGoogleButton()
    {
        if ((buttonGoogle == null) && !App.googleServices.isSignedIn())
        {
            /*
             * Create the 'Sign in with Google' button.
             * This button will be shown if auto sign-in
             * fails, allowing the player to manually sign
             * in to Google Play Services.
             */
            if (App.googleServices.isEnabled() && !App.googleServices.isSignedIn())
            {
                buttonGoogle = Scene2DUtils.addButton
                    (
                        "btn_google_signin_dark",
                        "btn_google_signin_dark_pressed",
                        1040,
                        30
                    );

                buttonGoogle.setZIndex(1);

                buttonGoogle.addListener(new ClickListener()
                {
                    public void clicked(InputEvent event, float x, float y)
                    {
                        GameAudio.inst().startSound(AudioData.SFX_BEEP);

                        buttonGoogle.setChecked(true);
                    }
                });
            }
        }

        if ((buttonGoogle != null) && App.googleServices.isSignedIn())
        {
            buttonGoogle.addAction(Actions.removeActor());
            buttonGoogle = null;
        }
    }

    /**
     * Sets visibility of all rlevant actors.
     *
     * @param _visible boolean visibility setting.
     */
    private void showItems(boolean _visible)
    {
        if (buttonStart != null) buttonStart.setVisible(_visible);
        if (buttonOptions != null) buttonOptions.setVisible(_visible);
        if (buttonHiScores != null) buttonHiScores.setVisible(_visible);
        if (buttonCredits != null) buttonCredits.setVisible(_visible);
        if (buttonExit != null) buttonExit.setVisible(_visible);

        if (decoration != null)
        {
            decoration.setVisible(_visible);
        }

        if (buttonGoogle != null)
        {
            buttonGoogle.setVisible(_visible);
        }
    }
}
