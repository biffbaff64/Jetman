
package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.paths.StairsPath;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class StairClimber extends GdxSprite
{
    private StopWatch stopWatch;
    private StairsPath stairsPath;
    private App        app;

    public StairClimber(App _app)
    {
        super(GraphicID.G_STAIR_CLIMBER, _app);

        this.app = _app;

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        animation.setFrameDuration(0.25f / 6f);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        stairsPath = new StairsPath();
        stairsPath.setNextPathData(this);

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
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
                sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

                move();
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
    }
}
