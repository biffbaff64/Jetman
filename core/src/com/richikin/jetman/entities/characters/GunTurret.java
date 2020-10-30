package com.richikin.jetman.entities.characters;

import com.richikin.enumslib.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class GunTurret extends GdxSprite
{
    private float turretAngle;

    public GunTurret(App _app)
    {
        super(GraphicID.G_ROVER_GUN_BARREL, _app);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

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
