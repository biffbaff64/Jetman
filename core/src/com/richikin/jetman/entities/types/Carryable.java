package com.richikin.jetman.entities.types;

import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.ICarryable;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;

public class Carryable extends GdxSprite implements ICarryable
{
    public SimpleVec2F releaseXY;
    public boolean     isAttachedToRover;
    public boolean     isAttachedToPlayer;

    public Carryable(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        //
        // Extending classes must set these to appropriate values
        bodyCategory = Gfx.CAT_NOTHING;
        collidesWith = Gfx.CAT_NOTHING;

        setAction(ActionStates._STANDING);

        distance.set(0, 0);

        releaseXY = new SimpleVec2F();
        isAttachedToRover  = false;
        isAttachedToPlayer = false;

        setCollisionListener();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            {
                if (isAttachedToPlayer)
                {
                    updateAttachedToPlayer();
                }
                else
                {
                    if (isAttachedToRover)
                    {
                        updateAttachedToRover();
                    }
                }
            }
            break;

            case _FALLING:
            {
                isAttachedToRover  = false;
                isAttachedToPlayer = false;

                direction.setY(Movement._DIRECTION_DOWN);
                speed.addY(0.2f);

                sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));
            }
            break;

            case _HURT:
            {
                setAction(ActionStates._DEAD);
            }

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
                releaseXY = null;
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void tidy(int index)
    {
    }

    @Override
    public void updateAttachedToPlayer()
    {
        if (App.getPlayer().isOnGround
            || (App.getPlayer().sprite.getY() < (initXYZ.getY() + frameHeight)))
        {
            sprite.setPosition(App.getPlayer().sprite.getX(), App.getPlayer().sprite.getY());
        }
        else
        {
            sprite.setPosition(App.getPlayer().sprite.getX(), (App.getPlayer().sprite.getY() - (frameHeight / 2f)));
        }

        isAttachedToRover = false;
    }

    @Override
    public void updateAttachedToRover()
    {
        if (App.getRover().lookingAt.getX() == Movement._DIRECTION_LEFT)
        {
            sprite.setPosition(App.getRover().sprite.getX() + 194, App.getRover().sprite.getY() + (131 - 43));
        }
        else
        {
            sprite.setPosition(App.getRover().sprite.getX() + 67, App.getRover().sprite.getY() + (131 - 43));
        }

        isAttachedToPlayer = false;
    }

    @Override
    public void explode()
    {
    }

    @Override
    public void setCollisionListener()
    {
    }
}
