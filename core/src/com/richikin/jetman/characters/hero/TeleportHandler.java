package com.richikin.jetman.characters.hero;

import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PlayServicesID;

public class TeleportHandler
{
    private boolean readyToExit;

    public TeleportHandler()
    {
        readyToExit = false;
    }

    public void start()
    {
        App.getPlayer().isTeleporting = true;
        App.getPlayer().setAction(ActionStates._TELEPORTING);
        App.getPlayer().speed.set(App.teleportManager.targetDistance.getX() / 25, App.teleportManager.targetDistance.getY() / 25);
        App.getPlayer().collisionObject.action = ActionStates._INACTIVE;

        App.googleServices.unlockAchievement(PlayServicesID.achievement_beam_me_up.getID());

        if ((App.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._BOMB_CARRY)
            || (App.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._GUN_CARRY))
        {
            App.googleServices.unlockAchievement(PlayServicesID.achievement_courier_services.getID());
        }
    }

    public boolean update()
    {
        if (readyToExit)
        {
            if (App.entityManager.teleportBeam != null)
            {
                if (App.entityManager.teleportBeam.update())
                {
                    App.entityManager.teleportBeam.dispose();
                    App.entityManager.teleportBeam = null;

                    App.teleportManager.endTeleporting();

                    readyToExit = false;
                }
            }
        }
        else
        {
             if (App.teleportManager.targetDistance.getX() > 0)
            {
                App.getPlayer().sprite.translate
                    (
                        App.getPlayer().speed.getX() * App.teleportManager.targetDirection.getX(),
                        App.getPlayer().speed.getY() * App.teleportManager.targetDirection.getY()
                    );

                App.teleportManager.targetDistance.subX(App.getPlayer().speed.getX());
                App.teleportManager.targetDistance.subY(App.getPlayer().speed.getY());
            }
            else
            {
                //
                // Teleportation has finished, so trigger the end visual
                App.getPlayer().sprite.setX(App.getTeleporter(App.teleportManager.targetBooth).sprite.getX());
                App.getPlayer().sprite.setY(App.getTeleporter(App.teleportManager.targetBooth).sprite.getY());

                App.teleportManager.teleportVisual(false);
                readyToExit = true;
            }
        }

        return readyToExit;
    }

    /**
     * Actions performed at the end of a teleport.
     * If LJM entered the teleporter carrying a
     * bomb or rover gun, then allow drawing of
     * that object again.
     */
    public void end()
    {
        if (App.getBomb() != null)
        {
            if (App.getBomb().isAttachedToPlayer)
            {
                App.getBomb().isDrawable = true;
            }
        }

        if (App.getGun() != null)
        {
            if (App.getGun().isAttachedToPlayer)
            {
                App.getGun().isDrawable = true;
            }
        }

        App.getPlayer().collisionObject.action = ActionStates._COLLIDABLE;
    }
}
