package com.richikin.jetman.characters.hero;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;

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

    public ActionButtonHandler()
    {
        this.actionMode         = ActionMode._NO_ACTION;
        this.previousActionMode = ActionMode._NO_ACTION;
    }

    public void update()
    {
        switch (actionMode)
        {
            case _TELEPORTING:
            {
                if (App.entityManager.teleportBeam != null)
                {
                    if (App.entityManager.teleportBeam.update())
                    {
                        App.entityManager.teleportBeam.dispose();
                        App.entityManager.teleportBeam = null;

                        App.teleportManager.setTeleporting(teleportNumber);
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

    public void setAction()
    {
        if ((actionMode == ActionMode._NO_ACTION)
            || ((actionMode == ActionMode._BOMB_CARRY) && App.getPlayer().collision.isTeleporterPresent())
            || ((actionMode == ActionMode._GUN_CARRY) && App.getPlayer().collision.isTeleporterPresent()))
        {
            if (App.doTransportersExist()
                && !App.getPlayer().collision.isInRoverMiddle()
                && App.getTeleporter(0).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle()))
            {
                setActionMode(ActionMode._TELEPORTING);

                App.teleportManager.teleportVisual(true);
                teleportNumber = 0;
            }
            else if (App.doTransportersExist()
                && !App.getPlayer().collision.isInRoverMiddle()
                && App.getTeleporter(1).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle()))
            {
                setActionMode(ActionMode._TELEPORTING);

                App.teleportManager.teleportVisual(true);
                teleportNumber = 1;
            }
            else
            {
                if ((App.getBomb() != null) && App.getPlayer().collision.isBombPresent())
                {
                    setActionMode(ActionMode._BOMB_CARRY);

                    App.getPlayer().isCarrying       = true;
                    App.getBomb().isAttachedToPlayer = true;
                    App.getHud().buttonAction.release();
                }
                else if ((App.getGun() != null)
                    && (!App.getGun().isAttachedToRover || App.getPlayer().isOnRoverBack)
                    && Intersector.overlaps(App.getPlayer().getCollisionRectangle(), App.getGun().getCollisionRectangle()))
                {
                    setActionMode(ActionMode._GUN_CARRY);

                    App.getPlayer().isCarrying      = true;
                    App.getGun().isAttachedToPlayer = true;
                    App.getHud().buttonAction.release();
                }
                else if ((App.getRover() != null)
                    && Intersector.overlaps(App.getPlayer().getCollisionRectangle(), App.getRover().backWheel.getCollisionRectangle()))
                {
                    setActionMode(ActionMode._BRIDGE_CARRY);

                    App.getPlayer().isCarrying = true;
                    App.getHud().buttonAction.release();
                }
                else
                {
                    if (App.getPlayer().collision.isInRoverMiddle())
                    {
                        setActionMode(ActionMode._ROVER_RIDE);

                        App.getPlayer().isCarrying    = false;
                        App.getPlayer().isRidingRover = true;
                        App.getPlayer().setAction(ActionStates._RIDING);
                        App.getHud().buttonAction.release();
                    }
                }
            }
        }
    }

    public int getTeleportNumber()
    {
        return teleportNumber;
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
