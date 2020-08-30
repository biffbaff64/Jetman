package com.richikin.jetman.ui;

import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.physics.Speed;
import com.richikin.jetman.utils.logging.Trace;

public class PanelManager
{
    private IUserInterfacePanel currentPanel;
    private boolean   panelActive;
    private boolean   managerEnabled;
    private final App app;

    public PanelManager(App _app)
    {
        this.app = _app;

        panelActive    = false;
        managerEnabled = false;
    }

    /**
     * Update the current message.
     * The current message is always the
     * one at position zero.
     */
    public void updateMessage()
    {
        if (currentPanel != null)
        {
            panelActive = !currentPanel.update();

            if (!panelActive)
            {
                setManagerEnabled(false);
                currentPanel.dispose();
                currentPanel = null;
            }
        }
    }

    public void draw()
    {
        if (currentPanel != null)
        {
            currentPanel.draw(app);
        }
    }

    public void addSlidePanel(String imageName)
    {
        if (managerEnabled)
        {
            Trace.__FILE_FUNC(imageName);

            SlidePanel panel = new SlidePanel();

            panel.initialise(app.assets.getObjectRegion(imageName), imageName);
            panel.activate();
            panel.action = Actions._OPENING;

            if (currentPanel != null)
            {
                currentPanel.forceZoomOut();
            }
            else
            {
                currentPanel = panel;
            }
        }
    }

    public void closeSlidePanel()
    {
        if (currentPanel.getState() == StateID._UPDATE)
        {
            currentPanel.set
                (
                    new SimpleVec2F(currentPanel.getPosition().x, currentPanel.getPosition().y),
                    new SimpleVec2F(0, currentPanel.getHeight() + 50),
                    new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    new Speed(0, 40)
                );

            currentPanel.setState(StateID._STATE_CLOSING);
        }
    }

    public void addZoomMessage(String imageName, int displayDelay)
    {
        if (managerEnabled)
        {
            Trace.__FILE_FUNC(imageName);

            IUserInterfacePanel panel = new ZoomPanel();

            if (app.assets.getTextRegion(imageName) == null)
            {
                Trace.__FILE_FUNC("ERROR: " + imageName + " not loaded!");
            }

            panel.initialise
                (
                    app.assets.getTextRegion(imageName),
                    imageName,
                    /* _canPause   */(displayDelay > 0),
                    /* _bounceBack */ true
                );
            panel.setPauseTime(displayDelay);

            if (currentPanel != null)
            {
                currentPanel.forceZoomOut();
            }
            else
            {
                currentPanel = panel;
            }
        }
    }

    public void addZoomMessage(String _fileName, int _delay, int x, int y)
    {
        setManagerEnabled(true);
        addZoomMessage(_fileName, _delay);
        setPosition(_fileName, x, y);
    }

    public void setPosition(String _nameID, int x, int y)
    {
        if ((currentPanel != null) && doesPanelExist(_nameID))
        {
            currentPanel.setPosition(x, y);
        }
    }

    public boolean doesPanelExist(String _panelName)
    {
        return false;
    }

    public IUserInterfacePanel getCurrentPanel()
    {
        return currentPanel;
    }

    public void setPanelActive(boolean _active)
    {
        panelActive = _active;
    }

    public void setManagerEnabled(boolean _managerEnabled)
    {
        managerEnabled = _managerEnabled;
    }
}
