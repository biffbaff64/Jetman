package com.richikin.jetman.entities.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.GenericCollisionListener;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.paths.Circular;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.physics.Proximity;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public class Tracker extends GdxSprite
{
    private Circular  circular;
    private Proximity proximity;
    private StopWatch stopWatch;
    private float restingTime;

    public Tracker(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        circular = new Circular();
        proximity = new Proximity();

        animation.setFrameDuration(0.5f / 6f);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;

        distance.set(Gfx._VIEW_WIDTH * 6, 0);
        speed.set(1 + MathUtils.random(2), 0);

        if (sprite.getX() < App.getPlayer().sprite.getX())
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
        else
        {
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }

        sprite.setOrigin(initXYZ.getX() + (frameWidth / 2), initXYZ.getY() + (frameHeight / 2));

        setAction(ActionStates._RUNNING);
        stopWatch = StopWatch.start();

        addCollisionListener(new GenericCollisionListener(this));
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                //
                // If proximity testing is enabled and this entity
                // is roughly above the player, then pause horizontal
                // movement for a short period...
                if (!proximity.isSilent())
                {
                    if(proximity.isRoughlyAbove(this, App.getPlayer()))
                    {
                        stopWatch.reset();
                        restingTime = 5000;
                        proximity.setSilent(true);
                        setAction(ActionStates._CIRCLING);
                    }
                }

                if (getAction() == ActionStates._RUNNING)
                {
                    //
                    // ...otherwise, keep the horizontal movement updating
                    circular.setXOffset(circular.getXOffset() + (speed.getX() * direction.getX()));
                    circular.setNextMove(this);

                    wrap();

                    //
                    // If this entity has moved outside the players view box,
                    // re-enable
                    if (proximity.isSilent())
                    {
                        if (!Intersector.overlaps(sprite.getBoundingRectangle(), App.getPlayer().viewBox.getRectangle()))
                        {
                            proximity.setSilent(false);
                        }
                    }
                }
            }
            break;

            case _CIRCLING:
            {
                circular.setNextMove(this);

                wrap();

                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setAction(ActionStates._RUNNING);
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);

                if (getAction() == ActionStates._KILLED)
                {
                    App.gameProgress.stackPush(GameProgress.Stack._SCORE, PointsManager.getPoints(gid));
                }

                setAction(ActionStates._EXPLODING);
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
                circular = null;
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        sprite.setFlip(isFlippedX, false);

        super.draw(spriteBatch);
    }

    private void checkY()
    {
        if ((sprite.getY() + frameHeight) < App.getPlayer().topEdge)
        {
            speed.setY(1);
            direction.setY(Movement._DIRECTION_UP);
        }
        else
        {
            speed.setY(0);
            direction.setY(Movement._DIRECTION_STILL);
        }
    }
}
