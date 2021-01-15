package com.richikin.jetman.characters.misc;

import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class GunTurret extends GdxSprite
{
    public GunTurret()
    {
        super(GraphicID.G_ROVER_GUN_BARREL);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        isDrawable = true;
        setAction(ActionStates._STANDING);

        sprite.setOrigin(65, frameHeight - 17);

        addDynamicPhysicsBody();
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            case _HURT:
            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
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
        App.entityData.removeEntity(index);
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x = sprite.getX() + (isFlippedX ? 0 : 49);
        collisionObject.rectangle.y = sprite.getY() + 26;
        collisionObject.rectangle.width = 73;
        collisionObject.rectangle.height = 33;
    }
}
