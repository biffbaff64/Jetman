package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.physics.aabb.ICollisionListener;

import java.util.concurrent.TimeUnit;

public class LaserBarrier extends GdxSprite
{
    private final Color[] colourList = new Color[]
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

    private int              colourIndex;
    private int              onTimer;
    private int              offTimer;
    private SpriteDescriptor onDescriptor;
    private SpriteDescriptor offDescriptor;
    private StopWatch        stopWatch;
    private float            restingTime;

    public LaserBarrier(final GraphicID _gid)
    {
        super(_gid);
    }

    @Override
    public void initialise(final SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        setCollisionListener();

        colourIndex = MathUtils.random(colourList.length - 1);

        setAction(ActionStates._STANDING);

        bodyCategory = Gfx.CAT_FIXED_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER_WEAPON;

        onTimer     = 20 + MathUtils.random(10);
        offTimer    = 5 + MathUtils.random(10);
        restingTime = onTimer;
        stopWatch   = StopWatch.start();

        onDescriptor = Entities.getDescriptor(GraphicID.G_POWER_BEAM);
        onDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_POWER_BEAM);
        onDescriptor._INDEX = entityDescriptor._INDEX;

        offDescriptor = Entities.getDescriptor(GraphicID.G_POWER_BEAM_SMALL);
        offDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_POWER_BEAM_SMALL);
        offDescriptor._INDEX = entityDescriptor._INDEX;
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
                        setAnimation(offDescriptor, 1.0f);
                        setAction(ActionStates._HIDING);
                        restingTime  = offTimer;

                        sprite.setSize(offDescriptor._SIZE.x, offDescriptor._SIZE.y);
                    }
                    else
                    {
                        setAnimation(onDescriptor, 1.0f);
                        setAction(ActionStates._STANDING);
                        restingTime  = onTimer;

                        sprite.setSize(onDescriptor._SIZE.x, onDescriptor._SIZE.y);
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
            public void onPositiveCollision(final GraphicID graphicID)
            {
                if (graphicID == GraphicID.G_BOMB)
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
