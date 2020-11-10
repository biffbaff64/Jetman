
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
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.ui.Scene2DUtils;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.IUIPage;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MenuPage implements IUIPage, Disposable
{
    public ImageButton buttonStart;
    public ImageButton buttonOptions;
    public ImageButton buttonExit;
    public ImageButton buttonGoogle;
    public ImageButton backButton;

    private Texture   foreground;
    private StopWatch stopWatch;
    private Image     decoration;
    private Label     javaHeapLabel;
    private Label     nativeHeapLabel;
    private Label     versionLabel;

    private int menuIndex;
    private int menuLoop;

    public MenuPage()
    {
        foreground = App.assets.loadSingleAsset("data/title_background.png", Texture.class);

        addMenu();
        addClickListeners();

        this.stopWatch = StopWatch.start();
    }

    @Override
    public void reset()
    {
        menuIndex = 0;
        menuLoop  = 0;
    }

    @Override
    public void show()
    {
        showItems(true);

        stopWatch.reset();

        menuIndex = 0;
        menuLoop  = 0;
    }

    @Override
    public void hide()
    {
        showItems(false);
    }

    @Override
    public boolean update()
    {
        boolean menuClosed = false;

        if (menuLoop >= 5)
        {
            menuClosed = true;
        }
        else
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= 1000)
            {
                stopWatch.reset();

                if (menuIndex == 0)
                {
                    menuLoop++;
                }
            }

            updateGoogleButton();
        }

        return menuClosed;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float originX, float originY)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, originX, originY);
        }

        menuPageDebug();
    }

    private void addMenu()
    {
        final float originX = -((float) (Gfx._HUD_WIDTH / 2));
        final float originY = -((float) (Gfx._HUD_HEIGHT / 2));

        Scene2DUtils.setup();

        buttonStart = Scene2DUtils.makeImageButton("buttonStart", "buttonStart_pressed");
        buttonStart.setPosition((int) originX + 482, (int) originY + (720 - 417));
        buttonStart.setVisible(true);
        buttonStart.setZIndex(1);
        App.stage.addActor(buttonStart);

        buttonOptions = Scene2DUtils.addButton("buttonOptions", "buttonOptions_pressed", (int) originX + 545, (int) originY + (720 - 516));
        buttonExit    = Scene2DUtils.addButton("buttonExit", "buttonExit_pressed", (int) originX + 591, (int) originY + (720 - 609));
        buttonStart.setZIndex(1);
        buttonOptions.setZIndex(1);
        buttonExit.setZIndex(1);

        backButton = Scene2DUtils.addButton("new_back_button", "new_back_button_pressed", 0, 0);

        if (Developer.isDevMode() && App.settings.isEnabled(Settings._MENU_HEAPS))
        {
            Trace.dbg("Adding Heap Usage debug...");

            javaHeapLabel   = Scene2DUtils.addLabel("JAVA HEAP: ", (int) originX + 40, (int) originY + (720 - 400), 20, Color.WHITE, GameAssets._BENZOIC_FONT);
            nativeHeapLabel = Scene2DUtils.addLabel("NATIVE HEAP: ", (int) originX + 40, (int) originY + (720 - 425), 20, Color.WHITE, GameAssets._BENZOIC_FONT);

            App.stage.addActor(javaHeapLabel);
            App.stage.addActor(nativeHeapLabel);
            javaHeapLabel.setZIndex(1);
            nativeHeapLabel.setZIndex(1);
        }

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        versionLabel = Scene2DUtils.addLabel
            (
                Version.getAppVersion(),
                (int) (originX + 10),
                (int) (originY + (720 - 30)),
                Color.WHITE,
                skin
            );
        versionLabel.setVisible(true);
        versionLabel.setZIndex(1);

        addDateSpecificItems(originX, originY);
    }

    private void addDateSpecificItems(float originX, float originY)
    {
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
        buttonStart.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
//                Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                buttonStart.setChecked(true);
            }
        });

        buttonOptions.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
//                Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                buttonOptions.setChecked(true);
            }
        });

        buttonExit.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
//                Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                buttonExit.setChecked(true);
            }
        });
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
            createGoogleButton();
        }

        if ((buttonGoogle != null) && App.googleServices.isSignedIn())
        {
            buttonGoogle.addAction(Actions.removeActor());
            buttonGoogle = null;
        }
    }

    /**
     * Create the 'Sign in with Google' button.
     * This button will be shown if auto sign-in
     * fails, allowing the player to manually sign
     * in to Google Play Services.
     */
    private void createGoogleButton()
    {
        if (App.googleServices.isEnabled() && !App.googleServices.isSignedIn())
        {
            Scene2DUtils.setup();

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
//                    Sfx.inst().startSound(Sfx.inst().SFX_BEEP);

                    buttonGoogle.setChecked(true);
                }
            });
        }
    }

    /**
     * Sets visibility of all rlevant actors.
     *
     * @param _visible boolean visibility setting.
     */
    private void showItems(boolean _visible)
    {
        buttonStart.setVisible(_visible);
        buttonOptions.setVisible(_visible);
        buttonExit.setVisible(_visible);

        if (decoration != null)
        {
            decoration.setVisible(_visible);
        }

        if (buttonGoogle != null)
        {
            buttonGoogle.setVisible(_visible);
        }

        if (Developer.isDevMode() && App.settings.isEnabled(Settings._MENU_HEAPS))
        {
            if (javaHeapLabel != null)
            {
                javaHeapLabel.setVisible(_visible);
            }

            if (nativeHeapLabel != null)
            {
                nativeHeapLabel.setVisible(_visible);
            }
        }
    }

    @Override
    public void dispose()
    {
        buttonStart.addAction(Actions.removeActor());
        buttonOptions.addAction(Actions.removeActor());
        buttonExit.addAction(Actions.removeActor());

        buttonStart   = null;
        buttonOptions = null;
        buttonExit    = null;

        if (Developer.isDevMode() && App.settings.isEnabled(Settings._MENU_HEAPS))
        {
            if (javaHeapLabel != null)
            {
                javaHeapLabel.addAction(Actions.removeActor());
                javaHeapLabel = null;
            }

            if (nativeHeapLabel != null)
            {
                nativeHeapLabel.addAction(Actions.removeActor());
                nativeHeapLabel = null;
            }
        }

        if (decoration != null)
        {
            decoration.addAction(Actions.removeActor());
            decoration = null;
        }

        versionLabel.addAction(Actions.removeActor());
        versionLabel = null;

        App.assets.unloadAsset("data/title_background.png");

        foreground = null;
        stopWatch  = null;
    }

    private void menuPageDebug()
    {
        if (Developer.isDevMode() && App.settings.isEnabled(Settings._MENU_HEAPS))
        {
            if (javaHeapLabel != null)
            {
                javaHeapLabel.setText
                    (
                        String.format
                            (
                                Locale.UK,
                                "JAVA HEAP: %3.2fMB",
                                ((((float) Gdx.app.getJavaHeap()) / 1024) / 1024)
                            )
                    );
            }

            if (nativeHeapLabel != null)
            {
                nativeHeapLabel.setText
                    (
                        String.format
                            (
                                Locale.UK,
                                "NATIVE HEAP: %3.2fMB",
                                ((((float) Gdx.app.getNativeHeap()) / 1024) / 1024)
                            )
                    );
            }
        }
    }
}
