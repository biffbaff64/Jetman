package com.richikin.jetman.ui;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.enumslib.ActionStates;
import com.richikin.utilslib.logging.Trace;

public class MessageManager
{
    static class Message
    {
        public IUserInterfacePanel panel;
        public boolean             enabled;
        public String              name;

        public Message(IUserInterfacePanel _panel, boolean _enabled, String _name)
        {
            panel   = _panel;
            enabled = _enabled;
            name    = _name;
        }
    }

    private final Array<Message> messages;
    private       boolean        managerEnabled;

    public MessageManager()
    {
        this.messages = new Array<>();
    }

    public void update()
    {
        for (int i = 0; i < messages.size; i++)
        {
            if (messages.get(i).enabled)
            {
                if (!messages.get(i).panel.update())
                {
                    messages.removeIndex(i);
                }
            }
        }
    }

    public void draw()
    {
        for (int i = 0; i < messages.size; i++)
        {
            if (messages.get(i).enabled)
            {
                messages.get(i).panel.draw();
            }
        }
    }

    public void addSlidePanel(String imageName)
    {
        if (managerEnabled)
        {
            SlidePanel panel = new SlidePanel();

            panel.initialise(App.assets.getObjectRegion(imageName), imageName);
            panel.activate();
            panel.action = ActionStates._OPENING;

            messages.add(new Message(panel, true, imageName));
        }
    }

    public void closeSlidePanel(String _name)
    {
    }

    public void addZoomMessage(String imageName, int displayDelay)
    {
        if (managerEnabled)
        {
            IUserInterfacePanel panel = new ZoomPanel();

            if (App.assets.getTextRegion(imageName) == null)
            {
                Trace.__FILE_FUNC("ERROR: " + imageName + " not loaded!");
            }
            else
            {
                panel.initialise
                    (
                        App.assets.getTextRegion(imageName),
                        imageName,
                        /* _canPause   */(displayDelay > 0),
                        /* _bounceBack */ true
                    );
                panel.setPauseTime(displayDelay);

                messages.add(new Message(panel, true, imageName));
            }
        }
    }

    public void addZoomMessage(String _fileName, int _delay, int x, int y)
    {
        addZoomMessage(_fileName, _delay);
        setPosition(_fileName, x, y);
    }

    public boolean doesPanelExist(String _nameID)
    {
        boolean exists = false;

        for (Message msg : messages)
        {
            if (_nameID.equals(msg.name))
            {
                exists = true;
                break;
            }
        }

        return exists;
    }

    public void setPosition(String _nameID, int x, int y)
    {
        for (Message msg : messages)
        {
            if (_nameID.equals(msg.name))
            {
                msg.panel.setPosition(x, y);
            }
        }
    }

    public void enable()
    {
        managerEnabled = true;
    }

    public void disable()
    {
        managerEnabled = false;
    }

    public boolean isEnabled()
    {
        return managerEnabled;
    }
}
