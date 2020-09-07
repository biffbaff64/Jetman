package com.richikin.jetman.entities.hero;

import com.richikin.jetman.core.App;

public class ActionButtonHandler
{
    public enum ActionMode
    {
        _NO_ACTION,
        _BOMB_CARRY,
        _GUN_CARRY,
        _BRIDGE_CARRY,
        _ROVER_RIDE,
        _TELEPORTING,
    }

    private       int        teleportNumber;
    private       ActionMode actionMode;
    private       ActionMode previousActionMode;
    private final App        app;

    public ActionButtonHandler(App _app)
    {
        this.app = _app;

        this.actionMode         = ActionMode._NO_ACTION;
        this.previousActionMode = ActionMode._NO_ACTION;
    }

    public ActionMode getActionMode()
    {
        return actionMode;
    }

    public void setAction()
    {
    }

    public void removeAction()
    {
    }
}
