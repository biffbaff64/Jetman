package com.richikin.jetman.entities.characters;

import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.utils.logging.Trace;

public class GunTurret extends GdxSprite
{
    private float turretAngle;

    public GunTurret(App _app)
    {
        super(_app);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        isDrawable = true;
        setAction(Actions._STANDING);

        sprite.setOrigin(65, frameHeight - 17);
        turretAngle = 0.0f;
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
                setAction(Actions._DEAD);
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

    //
    // TODO: 28/01/2019 - For now, drawing and animation is taken care of
    //                    by the main RoverGun class.
    //                    This should be brought into here...
//    @Override public void animate() {}
//    @Override public void draw(final SpriteBatch spriteBatch) {}

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x = sprite.getX() + (isFlippedX ? 0 : 49);
        collisionObject.rectangle.y = sprite.getY() + 26;
        collisionObject.rectangle.width = 73;
        collisionObject.rectangle.height = 33;
    }
}
