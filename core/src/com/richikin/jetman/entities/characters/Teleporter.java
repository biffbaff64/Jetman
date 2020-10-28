package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class Teleporter extends GdxSprite
{
    public int          teleporterNumber;
    public boolean      isCollected;
    public GdxSprite    collector;

    private final App app;

    public Teleporter(App _app)
    {
        super(GraphicID.G_TRANSPORTER, _app);

        this.app = _app;
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        setAction(Actions._STANDING);

        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        isAnimating = true;
        isCollected = false;
        isDrawable  = true;

        bodyCategory = Gfx.CAT_TELEPORTER;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_GROUND;

        setCollisionListener();
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
                    setAction(Actions._FALLING);
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
                collector = null;

                speed.y += 0.2f;

                sprite.translate(Movement._DIRECTION_STILL, (speed.getY() * Movement._DIRECTION_DOWN));
            }
            break;

            case _HURT:
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

    @Override
    public void animate()
    {
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void tidy(int _index)
    {
        app.entityManager._teleportIndex[teleporterNumber] = 0;
        collisionObject.kill();
        app.entityData.removeEntity(_index);
    }

    private void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(GraphicID graphicID)
            {
                if (getAction() == Actions._FALLING)
                {
                    GraphicID contactID = app.collisionUtils.getBoxHittingBottom(app.getBomb()).gid;

                    if (contactID == GraphicID._GROUND)
                    {
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);
                        setAction(Actions._STANDING);
                    }
                }
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
