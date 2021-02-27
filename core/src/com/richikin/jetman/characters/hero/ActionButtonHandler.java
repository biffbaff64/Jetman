package com.richikin.jetman.characters.hero;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.characters.misc.Bomb;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.maps.RoomManager;
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
        _OFFER_ABXY_A,
        _OFFER_ABXY_B,
    }

    private int        teleportNumber;
    private ActionMode actionMode;
    private ActionMode futureActionMode;

    public ActionButtonHandler()
    {
        this.actionMode         = ActionMode._NO_ACTION;
        this.futureActionMode   = ActionMode._NO_ACTION;
    }

    public void updateOfferButton()
    {
        if (actionMode == ActionMode._TELEPORTING)
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
        else if (((futureActionMode == ActionMode._BOMB_CARRY) && !App.getPlayer().collision.isBombPresent())
                || ((futureActionMode == ActionMode._GUN_CARRY) && !App.getPlayer().collision.isGunTurretPresent())
                || ((futureActionMode == ActionMode._ROVER_RIDE) && !App.getPlayer().collision.isInRoverMiddle()))
        {
            removeAction();
        }
        else if (actionMode == ActionMode._NO_ACTION)
        {
            if (App.getPlayer().collision.isTeleporterPresent())
            {
                setActionMode(ActionMode._OFFER_ABXY_B);
                setFutureActionMode(ActionMode._TELEPORTING);

                for (int i=0; i< RoomManager._MAX_TELEPORTERS; i++)
                {
                    if (App.getTeleporter(i).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle()))
                    {
                        teleportNumber = i;
                    }
                }
            }
            else if (App.getPlayer().collision.isInRoverMiddle())
            {
                setActionMode(ActionMode._OFFER_ABXY_B);
                setFutureActionMode(ActionMode._ROVER_RIDE);
            }
            else if (App.getPlayer().collision.isBombPresent())
            {
                setActionMode(ActionMode._OFFER_ABXY_B);
                setFutureActionMode(ActionMode._BOMB_CARRY);
            }
            else if (App.getPlayer().collision.isGunTurretPresent())
            {
                setActionMode(ActionMode._OFFER_ABXY_B);
                setFutureActionMode(ActionMode._GUN_CARRY);
            }
        }
    }

    public void setAction()
    {
    }

//    private void oldSetAction()
//    {
//        if ((actionMode == ActionMode._NO_ACTION)
//            || ((actionMode == ActionMode._BOMB_CARRY) && App.getPlayer().collision.isTeleporterPresent())
//            || ((actionMode == ActionMode._GUN_CARRY) && App.getPlayer().collision.isTeleporterPresent()))
//        {
//            if (App.doTransportersExist()
//                && !App.getPlayer().collision.isInRoverMiddle()
//                && App.getTeleporter(0).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle()))
//            {
//                setActionMode(ActionMode._TELEPORTING);
//
//                App.teleportManager.teleportVisual(true);
//                teleportNumber = 0;
//            }
//            else if (App.doTransportersExist()
//                && !App.getPlayer().collision.isInRoverMiddle()
//                && App.getTeleporter(1).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle()))
//            {
//                setActionMode(ActionMode._TELEPORTING);
//
//                App.teleportManager.teleportVisual(true);
//                teleportNumber = 1;
//            }
//            else
//            {
//                if ((App.getBomb() != null) && App.getPlayer().collision.isBombPresent())
//                {
//                    setActionMode(ActionMode._BOMB_CARRY);
//
//                    App.getPlayer().isCarrying       = true;
//                    App.getBomb().isAttachedToPlayer = true;
//                    App.getHud().buttonAction.release();
//                }
//                else if ((App.getGun() != null)
//                    && (!App.getGun().isAttachedToRover || App.getPlayer().isOnRoverBack)
//                    && Intersector.overlaps(App.getPlayer().getCollisionRectangle(), App.getGun().getCollisionRectangle()))
//                {
//                    setActionMode(ActionMode._GUN_CARRY);
//
//                    App.getPlayer().isCarrying      = true;
//                    App.getGun().isAttachedToPlayer = true;
//                    App.getHud().buttonAction.release();
//                }
//                else if ((App.getRover() != null)
//                    && Intersector.overlaps(App.getPlayer().getCollisionRectangle(), App.getRover().backWheel.getCollisionRectangle()))
//                {
//                    setActionMode(ActionMode._BRIDGE_CARRY);
//
//                    App.getPlayer().isCarrying = true;
//                    App.getHud().buttonAction.release();
//                }
//                else
//                {
//                    if (App.getPlayer().collision.isInRoverMiddle())
//                    {
//                        setActionMode(ActionMode._ROVER_RIDE);
//
//                        App.getPlayer().isCarrying    = false;
//                        App.getPlayer().isRidingRover = true;
//                        App.getPlayer().setAction(ActionStates._RIDING);
//                        App.getHud().buttonAction.release();
//                    }
//                }
//            }
//        }
//    }

    public int getTeleportNumber()
    {
        return teleportNumber;
    }

    private void setActionMode(ActionMode mode)
    {
        actionMode = mode;
    }

    public void setFutureActionMode(ActionMode mode)
    {
        futureActionMode = mode;
    }

    public ActionMode getActionMode()
    {
        return actionMode;
    }

    public ActionMode getFutureActionMode()
    {
        return futureActionMode;
    }

    public void removeAction()
    {
        actionMode = ActionMode._NO_ACTION;
        futureActionMode = ActionMode._NO_ACTION;
    }

    @Override
    public void dispose()
    {
        Trace.__FILE__();

        actionMode          = null;
        futureActionMode    = null;
    }
}
