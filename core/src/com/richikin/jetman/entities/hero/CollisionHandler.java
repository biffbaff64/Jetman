package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.AABBUtils;
import com.richikin.utilslib.physics.aabb.ICollisionListener;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.logging.Trace;

public class CollisionHandler implements ICollisionListener, Disposable
{
    private final App app;

    public CollisionHandler(App _app)
    {
        this.app = _app;
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
        if (app.getPlayer().getAction() != Actions._TELEPORTING)
        {
            switch (graphicID)
            {
                // Objects that can be collided with, and which
                // make up the 'Ground' group i.e. can be stood on.
                case _GROUND:
                case _BRIDGE:
                case _CRATER:
                case G_ROVER_BOOT:
                {
                    if ((app.getPlayer().getAction() == Actions._FALLING_TO_GROUND)
                        && (graphicID != GraphicID.G_ROVER_BOOT))
                    {
                        app.getPlayer().explode();

                        app.getPlayer().isRotating = false;
                        app.getPlayer().rotateSpeed = 0;
                    }
                    else
                    {
                        if (graphicID == app.collisionUtils.getBoxHittingBottom(app.getPlayer()).gid)
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
                    if ((graphicID == app.collisionUtils.getBoxHittingBottom(app.getPlayer()).gid)
                        && (app.getPlayer().sprite.getY() > (app.getTeleporter(0).frameHeight)))
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
                    if (!Developer.isGodMode())
                    {
                        if ((app.getPlayer().getAction() != Actions._EXPLODING)
                            && (app.getPlayer().getAction() != Actions._DYING))
                        {
                            app.getPlayer().kill();

                            if ((graphicID != GraphicID.G_MISSILE_BASE) && (graphicID != GraphicID.G_MISSILE_LAUNCHER))
                            {
                                app.getPlayer().collisionObject.contactEntity.setAction(Actions._HURT);
                            }
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
        if (app.getPlayer().getAction() != Actions._TELEPORTING)
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

        if (app.getBomb() != null)
        {
            isPresent = Intersector.overlaps(app.getPlayer().getCollisionRectangle(), app.getBomb().getCollisionRectangle());
        }

        return isPresent;
    }

    /**
     * Returns TRUE if LJM is in contact with a Teleporter booth.
     */
    public boolean isTeleporterPresent()
    {
        boolean isPresent = false;

        if (app.doTransportersExist())
        {
            isPresent = app.getTeleporter(0).getCollisionRectangle().contains(app.getPlayer().getCollisionRectangle())
                || (app.getTeleporter(1).getCollisionRectangle().contains(app.getPlayer().getCollisionRectangle()));
        }

        return isPresent;
    }

    /**
     *
     */
    private void checkForFalling()
    {
        if (app.collisionUtils.getBoxHittingBottom(app.getPlayer()).gid == GraphicID.G_NO_ID)
        {
            app.getPlayer().isInMidAir = true;
            app.getPlayer().isOnGround = false;

            if (app.getPlayer().getAction() == Actions._STANDING)
            {
                app.getPlayer().setAction(Actions._FALLING);
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

        if (app.doesRoverExist())
        {
            isInMiddle = AABBUtils.contains(app.getRover(), app.getPlayer())
                        && !AABBUtils.overlaps(app.getRover().frontWheel, app.getPlayer())
                        && !AABBUtils.overlaps(app.getRover().backWheel, app.getPlayer());
        }

        return isInMiddle;
    }

    /**
     *
     */
    private void setOnGround(GraphicID graphicID)
    {
        app.getPlayer().isInMidAir    = false;
        app.getPlayer().isOnGround    = true;
        app.getPlayer().isOnRoverBack = (graphicID == GraphicID.G_ROVER_BOOT);

        if (app.getPlayer().getAction() == Actions._FALLING)
        {
            app.getPlayer().setAction(Actions._STANDING);
            app.getPlayer().direction.setY(Movement._DIRECTION_STILL);
        }

        Rectangle rectangle = app.collisionUtils.getBoxHittingBottom(app.getPlayer()).rectangle;

        app.getPlayer().sprite.setY(rectangle.y + rectangle.height);
    }

    /**
     * Returns TRUE if LJM is in contact with the
     * ground, bridge section, or rover boot.
     */
    private void checkForGround()
    {
        GraphicID graphicID = app.collisionUtils.getBoxHittingBottom(app.getPlayer()).gid;

        switch (graphicID)
        {
            case _GROUND:
            case _BRIDGE:
            case G_ROVER_BOOT:
            {
                Trace.__FILE_FUNC_LINE();

                app.getPlayer().isInMidAir = false;
                app.getPlayer().isOnGround = true;
                app.getPlayer().isOnRoverBack = (graphicID == GraphicID.G_ROVER_BOOT);

                if (app.getPlayer().getAction() == Actions._FALLING)
                {
                    app.getPlayer().setAction(Actions._STANDING);
                    app.getPlayer().direction.setY(Movement._DIRECTION_STILL);
                }

                Rectangle rectangle = app.collisionUtils.getBoxHittingBottom(app.getPlayer()).rectangle;

                app.getPlayer().sprite.setY(rectangle.y + rectangle.height);
            }
            break;

            default:
            {
                app.getPlayer().isInMidAir = true;
                app.getPlayer().isOnGround = false;

                if (app.getPlayer().getAction() == Actions._STANDING)
                {
                    app.getPlayer().setAction(Actions._FALLING);
                }
            }
            break;
        }
    }

    private void checkForCrater()
    {
        if (!app.getPlayer().isRidingRover)
        {
            if (app.collisionUtils.getBoxHittingBottom(app.getPlayer()).gid == GraphicID._CRATER)
            {
                if (!app.getPlayer().isMovingX)
                {
                    app.getPlayer().setAction(Actions._HOVERING);
                }

                app.getPlayer().isJumpingCrater = true;
            }
            else
            {
                app.getPlayer().isJumpingCrater = false;
            }
        }
    }

    @Override
    public void dispose()
    {
    }
}
