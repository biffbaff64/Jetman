
package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.graphics.camera.Zoom;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.ui.DefaultPanel;
import com.richikin.utilslib.ui.IUserInterfacePanel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * A Stationary panel which zooms in and out
 */
public class ZoomPanel extends DefaultPanel implements IUserInterfacePanel
{
    private final static int    _DEFAULT_PAUSE_TIME = 1500;
    private final static float  _DEFAULT_SPEED      = 0.04f;
    private final static float  _DEFAULT_SPEED_INC  = 0.01f;
    private final static float  _MAXIMUM_ZOOM       = 8.0f;
    private final static float  _MINIMUM_ZOOM       = 0.1f;
    private final static float  _PAUSED_ZOOM        = 0.75f;

    private StopWatch    stopWatch;
    private float        zoomSpeed;
    private boolean      isFinished;
    private boolean      canPause;       // enable / disable pausing
    private int          pauseTime;      // How long to pause for. Default is 1500ms.
    private boolean      bounceBack;     // bounces back out of view after pausing
    private Zoom         zoom;

    public ZoomPanel()
    {
        super();
    }

    @Override
    public void initialise(final TextureRegion _region, final String _nameID, @NotNull final Object ... args)
    {
        textureRegion   = _region;
        nameID          = _nameID;
        canPause        = Boolean.parseBoolean(args[0].toString());
        pauseTime       = _DEFAULT_PAUSE_TIME;
        bounceBack      = Boolean.parseBoolean(args[1].toString());
        isFinished      = false;
        stopWatch       = StopWatch.start();
        zoom            = new Zoom();
        zoomSpeed       = _DEFAULT_SPEED;

        setState(StateID._STATE_ZOOM_IN);

        setPosition
            (
                (float) ((Gfx._HUD_WIDTH - textureRegion.getRegionWidth()) / 2),
                (float) ((Gfx._HUD_HEIGHT - textureRegion.getRegionHeight()) / 2)
            );
    }

    @Override
    public boolean update()
    {
        if (!isFinished)
        {
            switch (getState())
            {
                //
                // Zoom the panel into view
                case _STATE_ZOOM_IN:
                {
                    zoom.in(zoomSpeed);

                    if (zoom.getZoomValue() > _PAUSED_ZOOM)
                    {
                        if (canPause)
                        {
                            setState(StateID._STATE_PAUSED);
                            stopWatch.reset();
                        }
                        else
                        {
                            if (bounceBack)
                            {
                                setState(StateID._STATE_ZOOM_OUT);
                            }
                            else
                            {
                                setState(StateID._STATE_CLOSING);
                            }
                        }
                    }

                    zoomSpeed += _DEFAULT_SPEED_INC;
                }
                break;

                case _STATE_PANEL_UPDATE:
                {
                    //
                    // Do nothing while until the caller changes
                    // the state.
                }
                break;

                //
                // Pause for a short while before continuing
                case _STATE_PAUSED:
                {
                    if (stopWatch.time(TimeUnit.MILLISECONDS) >= pauseTime)
                    {
                        if (bounceBack)
                        {
                            setState(StateID._STATE_ZOOM_OUT);
                        }
                        else
                        {
                            setState(StateID._STATE_CLOSING);
                        }

                        zoomSpeed = _DEFAULT_SPEED;
                    }
                }
                break;

                //
                // Shrink the panel back down to its original size
                case _STATE_ZOOM_OUT:
                {
                    zoom.out(zoomSpeed);

                    if (zoom.getZoomValue() <= _MINIMUM_ZOOM)
                    {
                        dispose();

                        isFinished = true;
                    }

                    zoomSpeed += _DEFAULT_SPEED_INC;
                }
                break;

                //
                // Alternative ending, panel continues zooming in
                // to fill the screen before closing.
                case _STATE_CLOSING:
                {
                    zoom.in(zoomSpeed);

                    if (zoom.getZoomValue() > _MAXIMUM_ZOOM)
                    {
                        dispose();

                        isFinished = true;
                    }

                    zoomSpeed += _DEFAULT_SPEED_INC;
                }
                break;

                default:
                    break;
            }
        }

        return isFinished;
    }

    @Override
    public void draw()
    {
        if (!isFinished)
        {
            App.spriteBatch.draw
                (
                    textureRegion,
                    getPosition().x,
                    getPosition().y,
                    (float) textureRegion.getRegionWidth() / 2,
                    (float) textureRegion.getRegionHeight() / 2,
                    textureRegion.getRegionWidth(),
                    textureRegion.getRegionHeight(),
                    zoom.getZoomValue(),
                    zoom.getZoomValue(),
                    0.0f
                );
        }
    }

    public void forceZoomOut()
    {
        zoomSpeed = _DEFAULT_SPEED;

        setState(StateID._STATE_ZOOM_OUT);
    }

    public void setPauseTime(final int _time)
    {
        pauseTime = _time;
    }
}
