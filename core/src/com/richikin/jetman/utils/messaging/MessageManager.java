package com.richikin.jetman.utils.messaging;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.ui.IUserInterfacePanel;

public class MessageManager
{
    static class Message
    {
        public IUserInterfacePanel panel;
        public boolean enabled;
        public String name;
    }

    private Array<Message> messages;
    private boolean managerEnabled;
    private final App app;

    public MessageManager(App _app)
    {
        this.app = _app;
    }

    public void update()
    {
        for (int i=0; i<messages.size; i++)
        {
            if (messages.get(i).enabled)
            {
                if (messages.get(i).panel.update())
                {
                    messages.removeIndex(i);
                }
            }
        }
    }

    public void draw()
    {
        for (Message msg : messages)
        {
            if (msg.enabled)
            {
                msg.panel.draw(app);
            }
        }
    }

    public void addSlidePanel(String imageName)
    {
    }

    public void closeSlidePanel()
    {
    }

    public void addZoomMessage(String imageName, int displayDelay)
    {
    }

    public void addZoomMessage(String _fileName, int _delay, int x, int y)
    {
    }

    public boolean doesPanelExist(String _nameID)
    {
        return true;
    }

    public void setPosition(String _nameID, int x, int y)
    {
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
