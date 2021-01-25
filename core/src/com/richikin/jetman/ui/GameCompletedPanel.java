package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.ui.panels.ZoomPanel;
import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class GameCompletedPanel implements Disposable
{
    private Texture   background;
    private ZoomPanel zoomPanel;
    private StopWatch stopWatch;
    private float     originX;
    private float     originY;

    public GameCompletedPanel()
    {
    }

    public void setup()
    {
        originX = (App.baseRenderer.hudGameCamera.camera.position.x - (float) (Gfx._VIEW_WIDTH / 2));
        originY = (App.baseRenderer.hudGameCamera.camera.position.y - (float) (Gfx._VIEW_HEIGHT / 2));

        background = App.assets.loadSingleAsset("data/dark_screen.png", Texture.class);

        zoomPanel = new ZoomPanel();
        zoomPanel.initialise
            (
                App.assets.getObjectRegion("completed_panel"),
                "ZoomPanel",
                true,
                true
            );

        stopWatch = StopWatch.start();
    }

    public boolean update()
    {
        if (zoomPanel.update() && (zoomPanel.getState() == StateID._STATE_ZOOM_OUT))
        {
            return true;
        }
        else
        {
            if (stopWatch.time(TimeUnit.SECONDS) >= 30)
            {
                zoomPanel.setState(StateID._STATE_ZOOM_OUT);
            }
        }

        return false;
    }

    public void draw()
    {
        App.spriteBatch.draw(background, originX, originY);

        zoomPanel.draw();
    }

    @Override
    public void dispose()
    {
        zoomPanel.dispose();
        zoomPanel = null;

        background = null;
        stopWatch = null;
    }
}
