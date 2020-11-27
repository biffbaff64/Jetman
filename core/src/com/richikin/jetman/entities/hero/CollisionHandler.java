package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.physics.Movement;
import com.richikin.jetman.physics.aabb.AABBUtils;
import com.richikin.utilslib.physics.aabb.ICollisionListener;
import com.richikin.utilslib.Developer;
import com.richikin.utilslib.logging.Trace;

public class CollisionHandler implements ICollisionListener, Disposable
{
    public CollisionHandler()
    {
    }

    /**
     * Called when Collision is occurring.
     *
     * @param graphicID The GraphicdID of the entity
     *                  in collision with.
     */
    @Override
    public void onPositiveCollision(GraphicID graphicID)
    {
        if (App.getPlayer().getAction() != ActionStates._TELEPORTING)
        {
            switch (graphicID)
            {
                // Objects that can be collided with, and which
                // make up the 'Ground' group i.e. can be stood on.
                case _GROUND:
                case _CEILING:
                case _BRIDGE:
                case _CRATER:
                case G_ROVER_BOOT:
                {
                    if ((App.getPlayer().getAction() == ActionStates._FALLING_TO_GROUND)
                        && (graphicID != GraphicID.G_ROVER_BOOT))
                    {
                        App.getPlayer().explode();

                        App.getPlayer().isRotating = false;
                        App.getPlayer().rotateSpeed = 0;
                    }
                    else
                    {
                        if (graphicID == App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid)
                        {
                            setOnGround(graphicID);         // Set LJM standing
                            checkForCrater();               // Check for contact with any craters
                        }
                    }
                }
                break;

                // Other objects that can be collided with, but
                // that don't cause damage to LJM.
                case G_TRANSPORTER:
                {
                    // TODO: 12/12/2018 - LJM needs to be able to pick up Teleporters.
                    if ((graphicID == App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid)
                        && (App.getPlayer().sprite.getY() > (App.getTeleporter(0).frameHeight)))
                    {
                        setOnGround(graphicID);         // Set LJM standing
                        checkForCrater();               // Check for contact with any craters
                    }
                }
                break;

                // Objects that can be collided with, and
                // which WILL hurt LJM.
                case G_NO_ID:
                default:
                {
                    if ((App.getPlayer().getAction() != ActionStates._EXPLODING)
                        && (App.getPlayer().getAction() != ActionStates._DYING))
                    {
                        App.getPlayer().strength = 0;

                        if ((graphicID != GraphicID.G_MISSILE_BASE) && (graphicID != GraphicID.G_MISSILE_LAUNCHER))
                        {
                            App.getPlayer().collisionObject.contactEntity.setAction(ActionStates._HURT);
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * Called when there is no collision occurring.
     */
    @Override
    public void onNegativeCollision()
    {
        if (App.getPlayer().getAction() != ActionStates._TELEPORTING)
        {
            checkForFalling();
            checkForGround();
        }
    }

    /**
     * Returns TRUE if LJM is in contact with the bomb.
     */
    public boolean isBombPresent()
    {
        boolean isPresent = false;

        if (App.getBomb() != null)
        {
            isPresent = Intersector.overlaps(App.getPlayer().getCollisionRectangle(), App.getBomb().getCollisionRectangle());
        }

        return isPresent;
    }

    /**
     * Returns TRUE if LJM is in contact with a Teleporter booth.
     */
    public boolean isTeleporterPresent()
    {
        boolean isPresent = false;

        if (App.doTransportersExist())
        {
            isPresent = App.getTeleporter(0).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle())
                || (App.getTeleporter(1).getCollisionRectangle().contains(App.getPlayer().getCollisionRectangle()));
        }

        return isPresent;
    }

    /**
     *
     */
    private void checkForFalling()
    {
        if (App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid == GraphicID.G_NO_ID)
        {
            App.getPlayer().isInMidAir = true;
            App.getPlayer().isOnGround = false;

            if (App.getPlayer().getAction() == ActionStates._STANDING)
            {
                App.getPlayer().setAction(ActionStates._FALLING);
            }
        }
    }

    /**
     * Returns TRUE if LJM is standing in front of
     * the Moon Rover, in the middle between the wheels.
     */
    boolean isInRoverMiddle()
    {
        boolean isInMiddle = false;

        if (App.entityManager.doesRoverExist())
        {
            isInMiddle = AABBUtils.contains(App.getRover(), App.getPlayer())
                        && !AABBUtils.overlaps(App.getRover().frontWheel, App.getPlayer())
                        && !AABBUtils.overlaps(App.getRover().backWheel, App.getPlayer());
        }

        return isInMiddle;
    }

    /**
     *
     */
    private void setOnGround(GraphicID graphicID)
    {
        App.getPlayer().isInMidAir    = false;
        App.getPlayer().isOnGround    = true;
        App.getPlayer().isOnRoverBack = (graphicID == GraphicID.G_ROVER_BOOT);

        if (App.getPlayer().getAction() == ActionStates._FALLING)
        {
            App.getPlayer().setAction(ActionStates._STANDING);
            App.getPlayer().direction.setY(Movement._DIRECTION_STILL);
        }

        Rectangle rectangle = App.collisionUtils.getBoxHittingBottom(App.getPlayer()).rectangle;

        App.getPlayer().sprite.setY(rectangle.y + rectangle.height);
    }

    /**
     * Returns TRUE if LJM is in contact with the
     * ground, bridge section, or rover boot.
     */
    private void checkForGround()
    {
        GraphicID graphicID = App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid;

        switch (graphicID)
        {
            case _GROUND:
            case _BRIDGE:
            case G_ROVER_BOOT:
            {
                Trace.__FILE_FUNC_LINE();

                App.getPlayer().isInMidAir = false;
                App.getPlayer().isOnGround = true;
                App.getPlayer().isOnRoverBack = (graphicID == GraphicID.G_ROVER_BOOT);

                if (App.getPlayer().getAction() == ActionStates._FALLING)
                {
                    App.getPlayer().setAction(ActionStates._STANDING);
                    App.getPlayer().direction.setY(Movement._DIRECTION_STILL);
                }

                Rectangle rectangle = App.collisionUtils.getBoxHittingBottom(App.getPlayer()).rectangle;

                App.getPlayer().sprite.setY(rectangle.y + rectangle.height);
            }
            break;

            default:
            {
                App.getPlayer().isInMidAir = true;
                App.getPlayer().isOnGround = false;

                if (App.getPlayer().getAction() == ActionStates._STANDING)
                {
                    App.getPlayer().setAction(ActionStates._FALLING);
                }
            }
            break;
        }
    }

    private void checkForCrater()
    {
        if (!App.getPlayer().isRidingRover)
        {
            if (App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid == GraphicID._CRATER)
            {
                if (!App.getPlayer().isMovingX)
                {
                    App.getPlayer().setAction(ActionStates._HOVERING);
                }

                App.getPlayer().isJumpingCrater = true;
            }
            else
            {
                App.getPlayer().isJumpingCrater = false;
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
