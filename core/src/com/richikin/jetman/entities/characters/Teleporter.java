package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;
import com.richikin.jetman.physics.aabb.ICollisionListener;

public class Teleporter extends GdxSprite
{
    public boolean   hasPlayerInside;
    public int       teleporterNumber;
    public boolean   isCollected;
    public GdxSprite collector;

    public Teleporter()
    {
        super(GraphicID.G_TRANSPORTER);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        setAction(ActionStates._STANDING);

        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        isAnimating     = true;
        isCollected     = false;
        isDrawable      = true;
        hasPlayerInside = false;

        bodyCategory = Gfx.CAT_TELEPORTER;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_GROUND;

        addDynamicPhysicsBody();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            {
                if (collisionObject.idBottom == GraphicID.G_NO_ID)
                {
                    speed.y = 0;
                    setAction(ActionStates._FALLING);
                }
            }
            break;

            case _HELD:
            {
                if (collector != null)
                {
                    sprite.setPosition
                        (
                            collector.getPosition().x,
                            collector.getPosition().y - collector.frameHeight
                        );
                }
            }
            break;

            case _FALLING:
            {
                isCollected = false;
                collector   = null;

                speed.y += 0.2f;

                sprite.translate(Movement._DIRECTION_STILL, (speed.getY() * Movement._DIRECTION_DOWN));
            }
            break;

            case _HURT:
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
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX();
        collisionObject.rectangle.y      = sprite.getY() - 4;
        collisionObject.rectangle.width  = frameWidth;
        collisionObject.rectangle.height = frameHeight + 4;
    }

    @Override
    public void animate()
    {
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void tidy(int _index)
    {
        App.entityManager._teleportIndex[teleporterNumber] = 0;
        collisionObject.kill();
        App.entityData.removeEntity(_index);
    }

    private void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(CollisionObject cobjHitting)
            {
                if (getAction() == ActionStates._FALLING)
                {
                    GraphicID contactID = App.collisionUtils.getBoxHittingBottom(App.getTeleporter(teleporterNumber)).gid;

                    if (contactID == GraphicID._GROUND)
                    {
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);
                        setAction(ActionStates._STANDING);
                    }
                }
//                else
//                {
//                    hasPlayerInside = collisionObject.rectangle.contains
//                                        (
//                                            App.getPlayer().getCollisionRectangle()
//                                        );
//                }
            }

            @Override
            public void onNegativeCollision()
            {
            }

            @Override
            public void dispose()
            {
            }
        });
    }
}
