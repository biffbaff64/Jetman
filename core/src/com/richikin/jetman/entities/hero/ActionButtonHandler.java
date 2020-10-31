package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.Trace;

public class ActionButtonHandler implements Disposable
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

    private int        teleportNumber;
    private ActionMode actionMode;
    private ActionMode previousActionMode;

    private final App app;

    public ActionButtonHandler(App _app)
    {
        this.app = _app;

        this.actionMode         = ActionMode._NO_ACTION;
        this.previousActionMode = ActionMode._NO_ACTION;
    }

    public void setAction()
    {
        if ((actionMode == ActionMode._NO_ACTION)
            || ((actionMode == ActionMode._BOMB_CARRY) && app.getPlayer().collision.isTeleporterPresent())
            || ((actionMode == ActionMode._GUN_CARRY) && app.getPlayer().collision.isTeleporterPresent()))
        {
            if (app.doTransportersExist()
                && !app.getPlayer().collision.isInRoverMiddle()
                && app.getTeleporter(0).getCollisionRectangle().contains(app.getPlayer().getCollisionRectangle()))
            {
                setActionMode(ActionMode._TELEPORTING);

                app.teleportManager.teleportVisual(true);
                teleportNumber = 0;
            }
            else if (app.doTransportersExist()
                && !app.getPlayer().collision.isInRoverMiddle()
                && app.getTeleporter(1).getCollisionRectangle().contains(app.getPlayer().getCollisionRectangle()))
            {
                setActionMode(ActionMode._TELEPORTING);

                app.teleportManager.teleportVisual(true);
                teleportNumber = 1;
            }
            else
            {
                if ((app.getBomb() != null) && app.getPlayer().collision.isBombPresent())
                {
                    setActionMode(ActionMode._BOMB_CARRY);

                    app.getPlayer().isCarrying       = true;
                    app.getBomb().isAttachedToPlayer = true;
                    app.getHud().buttonAction.release();
                }
                else if ((app.getGun() != null)
                    && (!app.getGun().isAttachedToRover || app.getPlayer().isOnRoverBack)
                    && Intersector.overlaps(app.getPlayer().getCollisionRectangle(), app.getGun().getCollisionRectangle()))
                {
                    setActionMode(ActionMode._GUN_CARRY);

                    app.getPlayer().isCarrying      = true;
                    app.getGun().isAttachedToPlayer = true;
                    app.getHud().buttonAction.release();
                }
                else if ((app.getRover() != null)
                    && Intersector.overlaps(app.getPlayer().getCollisionRectangle(), app.getRover().backWheel.getCollisionRectangle()))
                {
                    setActionMode(ActionMode._BRIDGE_CARRY);

                    app.getPlayer().isCarrying = true;
                    app.getHud().buttonAction.release();
                }
                else
                {
                    if (app.getPlayer().collision.isInRoverMiddle())
                    {
                        setActionMode(ActionMode._ROVER_RIDE);

                        app.getPlayer().isCarrying    = false;
                        app.getPlayer().isRidingRover = true;
                        app.getPlayer().setAction(ActionStates._RIDING);
                        app.getHud().buttonAction.release();
                    }
                }
            }
        }
    }

    public void update()
    {
        switch (actionMode)
        {
            case _TELEPORTING:
            {
                if (app.entityManager.teleportBeam != null)
                {
                    if (app.entityManager.teleportBeam.update())
                    {
                        app.entityManager.teleportBeam.dispose();
                        app.entityManager.teleportBeam = null;

                        app.teleportManager.setTeleporting(teleportNumber);
                    }
                }
            }
            break;

            case _BOMB_CARRY:
            case _GUN_CARRY:
            case _BRIDGE_CARRY:
            case _ROVER_RIDE:
            case _NO_ACTION:
            default:
            {
            }
            break;
        }
    }

    public ActionMode getActionMode()
    {
        return actionMode;
    }

    public ActionMode getPreviousActionMode()
    {
        return previousActionMode;
    }

    private void setActionMode(ActionMode mode)
    {
        previousActionMode = actionMode;
        actionMode         = mode;
    }

    public void removeAction()
    {
        if (previousActionMode != ActionMode._NO_ACTION)
        {
            setActionMode(previousActionMode);

            previousActionMode = ActionMode._NO_ACTION;
        }
        else
        {
            actionMode = ActionMode._NO_ACTION;
        }
    }

    @Override
    public void dispose()
    {
        actionMode         = null;
        previousActionMode = null;
    }
}
