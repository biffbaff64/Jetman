package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.jetman.physics.aabb.ICollisionListener;

import java.util.concurrent.TimeUnit;

public class LaserBarrier extends GdxSprite
{
    private final Color[] colourList =
        {
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.GREEN,
            Color.CYAN,
            Color.MAROON,
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.GREEN,
            Color.CYAN,
            Color.MAROON,
        };

    private int       colourIndex;
    private int       onTimer;
    private int       offTimer;
    private StopWatch stopWatch;
    private float     restingTime;
    private int       frameNumber;

    public LaserBarrier(final GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(final SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        setCollisionListener();

        colourIndex = MathUtils.random(colourList.length - 1);

        setAction(ActionStates._STANDING);

        bodyCategory = Gfx.CAT_FIXED_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE;

        onTimer     = 20 + MathUtils.random(10);
        offTimer    = 5 + MathUtils.random(10);
        restingTime = onTimer;
        stopWatch   = StopWatch.start();
        isAnimating = false;
        frameNumber = 0;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            case _HIDING:
            {
                sprite.setColor(colourList[colourIndex]);

                if (++colourIndex >= colourList.length)
                {
                    colourIndex = 0;
                }

                if (stopWatch.time(TimeUnit.SECONDS) > restingTime)
                {
                    if (getAction() == ActionStates._STANDING)
                    {
                        setAction(ActionStates._HIDING);
                        frameNumber = 1;
                        restingTime = offTimer;
                    }
                    else
                    {
                        setAction(ActionStates._STANDING);
                        frameNumber = 0;
                        restingTime = onTimer;
                    }

                    stopWatch.reset();
                }
            }
            break;

            case _EXPLODING:
            {
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
    public void animate()
    {
        sprite.setRegion(animFrames[frameNumber]);
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX();
        collisionObject.rectangle.y      = sprite.getY() + 1;
        collisionObject.rectangle.width  = frameWidth;
        collisionObject.rectangle.height = frameHeight - 1;
    }

    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this);

        setAction(ActionStates._EXPLODING);
        elapsedAnimTime = 0;
    }

    private Sprite getSprite()
    {
        return this.sprite;
    }

    private void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(CollisionObject cobjHitting)
            {
                if (cobjHitting.gid == GraphicID.G_BOMB)
                {
                    if (App.getBomb().sprite.getY() < getSprite().getY())
                    {
                        explode();
                        App.getBomb().explode();
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
