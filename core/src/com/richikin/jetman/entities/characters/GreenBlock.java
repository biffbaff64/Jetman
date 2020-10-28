
package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class GreenBlock extends GdxSprite
{
    public GreenBlock(App _app)
    {
        super(GraphicID.G_GREEN_BLOCK, _app);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        animation.setFrameDuration(0.5f / 6f);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;

        speed.setX(MathUtils.random(Gfx._VIEW_WIDTH * 0.0015f, Gfx._VIEW_WIDTH * 0.003f));
        speed.setY(0);

        setAction(Actions._RUNNING);

        if (sprite.getX() < app.getPlayer().sprite.getX())
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
        else
        {
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                sprite.translate((speed.getX() * direction.getX()), 0);

                wrap();

                isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);
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
        switch (getAction())
        {
            case _KILLED:
            case _EXPLODING:
            {
            }
            break;

            case _RUNNING:
            default:
            {
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
                sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
            }
            break;
        }
    }
}
