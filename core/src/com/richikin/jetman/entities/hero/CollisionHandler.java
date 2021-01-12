package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.BaseEntity;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.physics.Movement;

public class CollisionHandler implements ICollisionListener, Disposable
{
    public CollisionHandler()
    {
    }

    /**
     * Called when Collision is occurring.
     *
     * @param cobjHitting The CollisionObject of the entity
     *                  in collision with.
     */
    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        if (App.getPlayer().getAction() != ActionStates._TELEPORTING)
        {
            switch (cobjHitting.gid)
            {
                // Objects that can be collided with, and which
                // make up the 'Ground' group i.e. can be stood on.
                case _GROUND:
                case _BRIDGE:
                case _CRATER:
                case G_ROVER_BOOT:
                {
//                    if (App.getPlayer().getAction() == ActionStates._FALLING_TO_GROUND)
//                    {
//                        App.getPlayer().explode();
//                        App.getPlayer().isRotating = false;
//                        App.getPlayer().rotateSpeed = 0;
//                    }
//                    else
                    {
                        if (cobjHitting.gid == App.getPlayer().collisionObject.idBottom)
                        {
                            setOnGround(cobjHitting.gid);   // Set LJM standing
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
                    if ((cobjHitting.gid == App.collisionUtils.getBoxHittingBottom(App.getPlayer()).gid)
                        && (App.getPlayer().sprite.getY() > (App.getTeleporter(0).frameHeight)))
                    {
                        setOnGround(cobjHitting.gid);       // Set LJM standing
                        checkForCrater();                   // Check for contact with any craters
                    }
                }
                break;

                case _CEILING:
                {
                }
                break;

                // Objects that can be collided with, and
                // which WILL hurt LJM.
                case G_MISSILE_BASE:
                case G_MISSILE_LAUNCHER:
                case G_DEFENDER:
                case G_DEFENDER_ZAP:
                case G_POWER_BEAM:
                case G_NO_ID:
                default:
                {
                    if ((App.getPlayer().getAction() != ActionStates._EXPLODING)
                        && (App.getPlayer().getAction() != ActionStates._FALLING_TO_GROUND)
                        && (App.getPlayer().getAction() != ActionStates._DYING))
                    {
                        App.getPlayer().strength = 0;

                        switch (cobjHitting.gid)
                        {
                            case G_MISSILE_BASE:
                            case G_MISSILE_LAUNCHER:
                            case G_POWER_BEAM:
                            case G_DEFENDER:
                            case G_DEFENDER_ZAP:
                            {
                                rebound();

                                if (App.getPlayer().isInMidAir)
                                {
                                    App.getPlayer().setAction(ActionStates._FALLING_TO_GROUND);
                                }
                                else
                                {
                                    App.getPlayer().explode();
                                }
                            }
                            break;

                            default:
                            {
                                App.getPlayer().collisionObject.contactEntity.setAction(ActionStates._HURT);
                            }
                            break;
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
            checkForGround();
            checkForFalling();
        }
    }

    public int isNextTo(GraphicID graphicID)
    {
        int nextTo = 0;

        for (BaseEntity sprite : App.entityData.entityMap)
        {
            if (sprite.gid == graphicID)
            {
                if (((GdxSprite) sprite).sprite.getBoundingRectangle().overlaps(App.getPlayer().sprite.getBoundingRectangle()))
                {
                    nextTo = ((GdxSprite) sprite).spriteNumber;
                }
            }
        }

        return nextTo;
    }

    /**
     * Returns TRUE if LJM is in contact with the bomb.
     */
    public boolean isBombPresent()
    {
        boolean isPresent = false;

        if (App.getBomb() != null)
        {
            isPresent = Intersector.overlaps
                (
                    App.getPlayer().getCollisionRectangle(),
                    App.getBomb().getCollisionRectangle()
                );
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
        GraphicID graphicID = App.getPlayer().collisionObject.idBottom;

        if ((graphicID != GraphicID._GROUND)
            && (graphicID != GraphicID._BRIDGE)
            && (graphicID != GraphicID._CRATER)
            && (graphicID != GraphicID.G_ROVER_BOOT))
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
    public boolean isInRoverMiddle()
    {
        boolean isInMiddle = false;

        if (App.doesRoverExist())
        {
            isInMiddle = (App.getPlayer().collisionObject.idBottom == GraphicID._GROUND)
                && (App.getPlayer().getCollisionRectangle().x < App.getRover().frontWheel.getPosition().x)
                && (App.getPlayer().getCollisionRectangle().x > (App.getRover().backWheel.getPosition().x + App.getRover().backWheel.frameWidth));
        }

        return isInMiddle;
    }

    /**
     * Returns TRUE if LJM is in contact with the
     * ground, bridge section, or rover boot.
     */
    private void checkForGround()
    {
        GraphicID graphicID = App.getPlayer().collisionObject.idBottom;

        switch (graphicID)
        {
            case _GROUND:
            case _BRIDGE:
            case G_ROVER_BOOT:
            case _CRATER:
            {
                setOnGround(graphicID);
            }
            break;

            case G_NO_ID:
            {
                if (App.getPlayer().getAction() == ActionStates._STANDING)
                {
                    App.getPlayer().isInMidAir = true;
                    App.getPlayer().isOnGround = false;

                    App.getPlayer().setAction(ActionStates._FALLING);
                }
            }
            break;

            default:
            {
            }
            break;
        }
    }

    /**
     *
     */
    public void setOnGround(GraphicID graphicID)
    {
        App.getPlayer().isInMidAir    = false;
        App.getPlayer().isOnGround    = true;
        App.getPlayer().isOnRoverBack = (graphicID == GraphicID.G_ROVER_BOOT);

        if (App.getPlayer().getAction() == ActionStates._FALLING)
        {
            App.getPlayer().setAction(ActionStates._STANDING);
            App.getPlayer().direction.setY(Movement._DIRECTION_STILL);
        }

        Rectangle rectangle = App.getPlayer().collisionObject.contactEntity.getCollisionRectangle();

        App.getPlayer().sprite.setY(rectangle.y + rectangle.height);
    }

    private void checkForCrater()
    {
        if (!App.getPlayer().isRidingRover)
        {
            if (App.getPlayer().collisionObject.idBottom == GraphicID._CRATER)
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

    private void rebound()
    {
        if (App.getPlayer().collisionObject.hasContactUp()
            && !App.getPlayer().collisionObject.hasContactDown())
        {
            App.getPlayer().sprite.translateY(Gfx.getTileHeight() * Movement._DIRECTION_DOWN);
        }
        else if (App.getPlayer().collisionObject.hasContactDown()
            && !App.getPlayer().collisionObject.hasContactUp())
        {
            App.getPlayer().sprite.translateY(Gfx.getTileHeight() * Movement._DIRECTION_UP);
        }

        if (App.getPlayer().collisionObject.hasContactLeft()
            && !App.getPlayer().collisionObject.hasContactRight())
        {
            App.getPlayer().sprite.translateX(Gfx.getTileWidth() * Movement._DIRECTION_RIGHT);
        }
        else if (App.getPlayer().collisionObject.hasContactRight()
            && !App.getPlayer().collisionObject.hasContactLeft())
        {
            App.getPlayer().sprite.translateX(Gfx.getTileWidth() * Movement._DIRECTION_LEFT);
        }
    }

    @Override
    public void dispose()
    {
    }
}
