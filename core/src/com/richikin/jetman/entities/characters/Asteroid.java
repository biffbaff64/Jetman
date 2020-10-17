package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.StopWatch;
import com.richikin.jetman.utils.logging.Trace;

public class Asteroid extends GdxSprite
{
    //
    // Table of scaling values.
    // This is used to create asteroids
    // of varying sizes
    private static final float[] sizes =
        {
            1.5f, 1.25f, 1.0f,
        };

    private StopWatch stopWatch;

    public Asteroid(App _app)
    {
        super(GraphicID.G_ASTEROID, _app);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        sprite.setScale(sizes[MathUtils.random(sizes.length - 1)]);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_SCENERY | Gfx.CAT_VEHICLE;

        speed.setX(Gfx._VIEW_WIDTH * 0.004f);
        speed.setY(speed.getX() * (0.35f + MathUtils.random(0.9f)));

        direction.setX((sprite.getX() < app.getPlayer().sprite.getX()) ? Movement._DIRECTION_RIGHT : Movement._DIRECTION_LEFT);
        direction.setY(Movement._DIRECTION_DOWN);

        setAction(Actions._RUNNING);
        stopWatch       = StopWatch.start();
        isRotating      = true;
        rotateSpeed     = (MathUtils.random(5, 8) * (direction.getX() * -1));
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                sprite.translateX(speed.getX() * direction.getX());
                sprite.translateY(speed.getY() * direction.getY());

                wrap();

                if ((collisionObject.idBottom == GraphicID._GROUND)
                    || collisionObject.isHittingPlayer)
                {
                    setAction(Actions._HURT);
                    elapsedAnimTime = 0;
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this, app);

                if (getAction() == Actions._KILLED)
                {
                    app.gameProgress.score.add(PointsManager.getPoints(gid));
                }

                setAction(Actions._EXPLODING);
            }
            break;

            case _EXPLODING:
            {
                //
                // Do nothing while waiting for the explosion to finish
            }
            break;

            case _DYING:
            {
//                CraterManager craterManager = new CraterManager(app);
//
//                if ((collisionObject.idBottom == GraphicID._GROUND)
//                    && (craterManager.canMakeCrater(this, false)))
//                {
//                    int x = (int) (sprite.getX() / Gfx.getTileWidth());
//                    int y = (int) (app.getPlayer().sprite.getY() / Gfx.getTileHeight()) - 1;
//
//                    craterManager.makeCrater(x, y);
//                }

                setAction(Actions._DEAD);
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
        switch (getAction())
        {
            case _STANDING:
            case _RUNNING:
            {
                if (animation != null)
                {
                    sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                    elapsedAnimTime += Gdx.graphics.getDeltaTime();
                }
            }
            break;

            case _KILLED:
            case _EXPLODING:
            default:
            {
            }
            break;
        }
    }

    @Override
    public void updateCollisionBox()
    {
        float width = (frameWidth * sprite.getScaleX());
        float height = (frameWidth * sprite.getScaleY());

        collisionObject.rectangle.x         = sprite.getX() + ((frameWidth - width) / 2);
        collisionObject.rectangle.y         = sprite.getY() + ((frameHeight - height) / 2);
        collisionObject.rectangle.width     = width;
        collisionObject.rectangle.height    = height;
    }
}
