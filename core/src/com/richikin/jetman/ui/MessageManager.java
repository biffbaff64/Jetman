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
        if (isEnabled())
        {
            for (int i = 0; i < messages.size; i++)
            {
                if (messages.get(i).enabled)
                {
                    // Returns TRUE if finished...
                    if (messages.get(i).panel.update())
                    {
                        messages.removeIndex(i);
                    }
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
        SlidePanel panel = new SlidePanel();

        panel.initialise(App.assets.getObjectRegion(imageName), imageName);
        panel.activate();
        panel.action = ActionStates._OPENING;

        messages.add(new Message(panel, true, imageName));

        enable();
    }

    @SuppressWarnings("EmptyMethod")
    public void closeSlidePanel(String name)
    {
    }

    public void addZoomMessage(String imageName, int displayDelay)
    {
        if (App.assets.getTextRegion(imageName) == null)
        {
            Trace.__FILE_FUNC("ERROR: " + imageName + " not loaded!");
        }
        else
        {
            Trace.__FILE_FUNC(imageName);

            IUserInterfacePanel panel = new ZoomPanel();

            panel.initialise
                (
                    App.assets.getTextRegion(imageName),
                    imageName,
                    /* _canPause   */(displayDelay > 0),
                    /* _bounceBack */ true
                );
            panel.setPauseTime(displayDelay);

            messages.add(new Message(panel, true, imageName));

            enable();

            Trace.dbg("Finished: Active messages = " + messages.size);
        }
    }

    public void addZoomMessage(String fileName, int delay, int x, int y)
    {
        Trace.__FILE_FUNC(fileName);

        addZoomMessage(fileName, delay);
        setPosition(fileName, x, y);
        enable();
    }

    public boolean doesPanelExist(String nameID)
    {
        boolean exists = false;

        for (Message msg : messages)
        {
            if (nameID.equals(msg.name))
            {
                exists = true;
            }
        }

        return exists;
    }

    public void setPosition(String nameID, int x, int y)
    {
        for (Message msg : messages)
        {
            if (nameID.equals(msg.name))
            {
                msg.panel.setPosition(x, y);
            }
        }
    }

    private void enable()
    {
        managerEnabled = true;
    }

    private void disable()
    {
        managerEnabled = false;
    }

    public boolean isEnabled()
    {
        return managerEnabled;
    }
}
