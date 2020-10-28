package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.paths.Circular;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
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

    public Tracker(GraphicID graphicID, App _app)
    {
        super(graphicID, _app);
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

        if (sprite.getX() < app.getPlayer().sprite.getX())
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
        else
        {
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }

        sprite.setOrigin(initXYZ.getX() + (frameWidth / 2), initXYZ.getY() + (frameHeight / 2));

        setAction(Actions._RUNNING);
        stopWatch = StopWatch.start();
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
                    if(proximity.isRoughlyAbove(this, app.getPlayer()))
                    {
                        stopWatch.reset();
                        restingTime = 5000;
                        proximity.setSilent(true);
                        setAction(Actions._CIRCLING);
                    }
                }

                if (getAction() == Actions._RUNNING)
                {
                    //
                    // ...otherwise, keep the horizontal movement updating
                    circular.setXOffset(circular.getXOffset() + (speed.getX() * direction.getX()));
                    circular.setNextMove(this, app);

                    wrap();

                    //
                    // If this entity has moved outside the players view box,
                    // re-enable
                    if (proximity.isSilent())
                    {
                        if (!Intersector.overlaps(sprite.getBoundingRectangle(), app.getPlayer().viewBox.getRectangle()))
                        {
                            proximity.setSilent(false);
                        }
                    }
                }
            }
            break;

            case _CIRCLING:
            {
                circular.setNextMove(this, app);

                wrap();

                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    setAction(Actions._RUNNING);
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this, app);

                if (getAction() == Actions._KILLED)
                {
                    app.gameProgress.score.add(PointsManager.getPoints(gid));
                }

                setAction(Actions._EXPLODING);
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(Actions._DEAD);
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
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        sprite.setFlip(isFlippedX, false);

        super.draw(spriteBatch);
    }

    private void checkY()
    {
        if ((sprite.getY() + frameHeight) < app.getPlayer().topEdge)
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
