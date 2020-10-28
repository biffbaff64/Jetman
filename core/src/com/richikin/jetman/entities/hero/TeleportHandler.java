package com.richikin.jetman.entities.hero;

import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;

public class TeleportHandler
{
    private       boolean readyToExit;
    private final App     app;

    public TeleportHandler(App _app)
    {
        this.app = _app;

        readyToExit = false;
    }

    public void start()
    {
        app.getPlayer().isTeleporting = true;
        app.getPlayer().setAction(Actions._TELEPORTING);
        app.getPlayer().speed.set(app.teleportManager.targetDistance.getX() / 25, app.teleportManager.targetDistance.getY() / 25);
        app.getPlayer().collisionObject.action = Actions._INACTIVE;
    }

    public boolean update()
    {
        if (readyToExit)
        {
            if (app.entityManager.teleportBeam != null)
            {
                if (app.entityManager.teleportBeam.update())
                {
                    app.entityManager.teleportBeam.dispose();
                    app.entityManager.teleportBeam = null;

                    app.teleportManager.endTeleporting();

                    readyToExit = false;
                }
            }
        }
        else
        {
            if (app.teleportManager.targetDistance.getX() > 0)
            {
                app.getPlayer().sprite.translate
                    (
                        app.getPlayer().speed.getX() * app.teleportManager.targetDirection.getX(),
                        app.getPlayer().speed.getY() * app.teleportManager.targetDirection.getY()
                    );

                app.teleportManager.targetDistance.subX(app.getPlayer().speed.getX());
                app.teleportManager.targetDistance.subY(app.getPlayer().speed.getY());
            }
            else
            {
                //
                // Teleportation has finished, so trigger the end visual
                app.getPlayer().sprite.setX(app.getTeleporter(app.teleportManager.targetBooth).sprite.getX());
                app.getPlayer().sprite.setY(app.getTeleporter(app.teleportManager.targetBooth).sprite.getY());

                app.teleportManager.teleportVisual(false);
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
        if (app.getBomb() != null)
        {
            if (app.getBomb().isAttachedToPlayer)
            {
                app.getBomb().isDrawable = true;
            }
        }

        if (app.getGun() != null)
        {
            if (app.getGun().isAttachedToPlayer)
            {
                app.getGun().isDrawable = true;
            }
        }

        app.getPlayer().collisionObject.action = Actions._COLLIDABLE;

//        app.googleServices.unlockAchievement(PlayServicesID.achievement_beam_me_up.getID());

        if ((app.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._BOMB_CARRY)
            || (app.getPlayer().actionButton.getActionMode() == ActionButtonHandler.ActionMode._GUN_CARRY))
        {
//            app.googleServices.unlockAchievement(PlayServicesID.achievement_courier_services.getID());
        }
    }
}
