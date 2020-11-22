
package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class GreenBlock extends GdxSprite
{
    public GreenBlock()
    {
        super(GraphicID.G_GREEN_BLOCK);
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

        setAction(ActionStates._RUNNING);

        if (sprite.getX() < App.getPlayer().sprite.getX())
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
                explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this);

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
                sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
            }
            break;
        }
    }
}
