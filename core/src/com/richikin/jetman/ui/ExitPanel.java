package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class ExitPanel implements Disposable
{
    public final int _NONE_PRESSED = 0;
    public final int _NO_PRESSED   = 1;
    public final int _YES_PRESSED  = 2;

    public ImageButton buttonYes;
    public ImageButton buttonNo;

    private Texture   panel;
    private Texture   darkLayer;
    private int       action;
    private StopWatch stopWatch;
    private boolean   showYes;
    private boolean   firstTime;

    private ImageButton.ImageButtonStyle styleRedYes;
    private ImageButton.ImageButtonStyle styleRedNo;
    private ImageButton.ImageButtonStyle styleBlueYes;
    private ImageButton.ImageButtonStyle styleBlueNo;

    private static final int _YES = 0;
    private static final int _NO  = 1;

    private static final int[][] displayPos =
        {
            {480, (720 - 520), 142, 66},   // Yes
            {668, (720 - 520), 142, 66},   // No
        };

    public ExitPanel()
    {
    }

    public void open()
    {
        setup();
    }

    public void close()
    {
        dispose();
    }

    public int update()
    {
        if ((stopWatch.time(TimeUnit.MILLISECONDS) > 750) || firstTime)
        {
            if (showYes)
            {
                buttonYes.setStyle(styleBlueYes);
                buttonNo.setStyle(styleRedNo);
            }
            else
            {
                buttonYes.setStyle(styleRedYes);
                buttonNo.setStyle(styleBlueNo);
            }

            showYes   = !showYes;
            firstTime = false;

            stopWatch.reset();
        }

        return action;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(darkLayer, AppSystem.hudOriginX, AppSystem.hudOriginY);
        spriteBatch.draw(panel, AppSystem.hudOriginX, AppSystem.hudOriginY);
    }

    private void setup()
    {
        panel     = App.assets.loadSingleAsset("data/exit_screen.png", Texture.class);
        darkLayer = App.assets.loadSingleAsset("data/dark_screen.png", Texture.class);

        buttonYes = Scene2DUtils.addButton
            (
                "button_yes_blue",
                "button_yes_pressed",
                (int) AppSystem.hudOriginX + displayPos[_YES][0],
                (int) AppSystem.hudOriginY + displayPos[_YES][1]
            );

        buttonNo = Scene2DUtils.addButton
            (
                "button_no_red",
                "button_no_pressed",
                (int) AppSystem.hudOriginX + displayPos[_NO][0],
                (int) AppSystem.hudOriginY + displayPos[_NO][1]
            );

        buttonYes.setZIndex(1);
        buttonNo.setZIndex(1);

        action    = _NONE_PRESSED;
        showYes   = true;
        firstTime = true;
        stopWatch = StopWatch.start();

        buttonYes.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                action = _YES_PRESSED;
            }
        });

        buttonNo.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                action = _NO_PRESSED;
            }
        });

        styleRedYes = new ImageButton.ImageButtonStyle();
        styleRedNo = new ImageButton.ImageButtonStyle();
        styleRedYes.up = new TextureRegionDrawable(App.assets.getButtonRegion("button_yes_red"));
        styleRedNo.up = new TextureRegionDrawable(App.assets.getButtonRegion("button_no_red"));

        styleBlueYes = new ImageButton.ImageButtonStyle();
        styleBlueNo = new ImageButton.ImageButtonStyle();
        styleBlueYes.up = new TextureRegionDrawable(App.assets.getButtonRegion("button_yes_blue"));
        styleBlueNo.up = new TextureRegionDrawable(App.assets.getButtonRegion("button_no_blue"));
    }

    @Override
    public void dispose()
    {
        buttonYes.addAction(Actions.removeActor());
        buttonNo.addAction(Actions.removeActor());

        App.assets.unloadAsset("data/exit_screen.png");
        panel = null;

        App.assets.unloadAsset("data/dark_screen.png");
        darkLayer = null;

        buttonYes = null;
        buttonNo  = null;

        styleRedYes = null;
        styleRedNo = null;
        styleBlueYes = null;
        styleBlueNo = null;
    }
}
