
package com.richikin.jetman.entities.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.GenericCollisionListener;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.paths.StairsPath;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class StairClimber extends GdxSprite
{
    private StopWatch stopWatch;
    private       StairsPath stairsPath;

    public StairClimber(GraphicID _gid)
    {
        super(_gid);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        animation.setFrameDuration(0.25f / 6f);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;

        stairsPath = new StairsPath();
        stairsPath.setNextPathData(this);

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
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
                sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

                move();

                if (collisionObject.isHittingPlayer)
                {
                    setAction(ActionStates._HURT);
                    elapsedAnimTime = 0;
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
                bodyCategory = Gfx.CAT_NOTHING;
                collidesWith = Gfx.CAT_NOTHING;
            }
            break;

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

        int directionXStore = direction.getX();

        updateCommon();

        if (direction.getX() != directionXStore)
        {
            stairsPath.directionReset.setX(direction.getX());
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        sprite.setFlip(isFlippedX, false);

        super.draw(spriteBatch);
    }

    private void move()
    {
        if (distance.isEmpty())
        {
            stairsPath.setNextPathData(this);
        }

        sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

        distance.subX(speed.getX());
        distance.subY(speed.getY());

        isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);

        wrap();
    }
}
