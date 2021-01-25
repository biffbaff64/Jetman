package com.richikin.jetman.ui.panels;

import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.physics.Movement;
import com.richikin.enumslib.StateID;

public class PanelManager
{
    private IUserInterfacePanel currentPanel;
    private boolean             panelActive;
    private boolean             managerEnabled;

    public PanelManager()
    {
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
            currentPanel.draw();
        }
    }

    public void addSlidePanel(String imageName)
    {
        if (managerEnabled)
        {
            Trace.__FILE_FUNC(imageName);

            SlidePanel panel = new SlidePanel();

            panel.initialise(App.assets.getObjectRegion(imageName), imageName);
            panel.activate();
            panel.action = ActionStates._OPENING;

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
                    new SimpleVec2F(currentPanel.getPosition().getX(), currentPanel.getPosition().getY()),
                    new SimpleVec2F(0, currentPanel.getHeight() + 50),
                    new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                    new SimpleVec2F(0, 40)
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

            if (App.assets.getTextRegion(imageName) == null)
            {
                Trace.__FILE_FUNC("ERROR: " + imageName + " not loaded!");
            }

            panel.initialise
                (
                    App.assets.getTextRegion(imageName),
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
        boolean exists;

        try
        {
            exists = _panelName.equals(currentPanel.getNameID());
        }
        catch (Exception npe)
        {
            exists = false;
        }

        return exists;
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
