package com.richikin.jetman.characters.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.characters.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.logging.Trace;

public class DefenderBullet extends GdxSprite
{
    private final Color[] colours = new Color[]
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
            };

    private int colourIndex;

    public DefenderBullet(GraphicID _gid)
    {
        super(_gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        if (gid == GraphicID.G_ROVER_BULLET)
        {
            float angle = Math.abs(App.getGun().gunTurret.sprite.getRotation());

            speed.setX(8 * MathUtils.cosDeg(angle));
            speed.setY(8 * MathUtils.sinDeg(angle));

            direction.setX
                (
                    App.getGun().isFlippedX
                        ? Movement._DIRECTION_LEFT
                        : Movement._DIRECTION_RIGHT
                );

            direction.setY
                (
                    (App.getGun().gunTurret.sprite.getRotation() != 0.0f)
                        ? Movement._DIRECTION_UP
                        : Movement._DIRECTION_STILL
                );

            if (App.getRover().isFlippedX)
            {
                sprite.setPosition
                    (
                        (App.getGun().gunTurret.sprite.getX() + 46),
                        (App.getGun().gunTurret.sprite.getY() + 28)
                    );
            }
            else
            {
                sprite.setPosition
                    (
                        (App.getGun().gunTurret.sprite.getX() + 51),
                        (App.getGun().gunTurret.sprite.getY() + 28)
                    );
            }

            float count = (68.0f * MathUtils.cosDeg(angle));

            while (count > 0)
            {
                sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

                count -= speed.getX();
            }

            bodyCategory = Gfx.CAT_PLAYER_WEAPON;
            collidesWith = Gfx.CAT_FIXED_ENEMY | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_ENEMY_WEAPON;
        }
        else
        {
            if (MathUtils.random(100) < 50)
            {
                direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP);
            }
            else
            {
                direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_UP);
            }

            sprite.setScale(0.8f + MathUtils.random(0.4f));

            float angle = 0.25f + MathUtils.random(0.6f);

            speed.setX((Gfx._VIEW_WIDTH * 0.004f) * angle);
            speed.setY(Gfx._VIEW_HEIGHT * 0.004f);

            bodyCategory = Gfx.CAT_MOBILE_ENEMY;
            collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
        }

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        if (speed.isEmpty())
        {
            distance.set(0, 0);
        }
        else
        {
            distance.set((float) Gfx._VIEW_WIDTH / 2, (float) Gfx._VIEW_HEIGHT / 2);
        }

        addDynamicPhysicsBody();

        setAction(ActionStates._RUNNING);
        colourIndex = 0;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                if (distance.isEmpty())
                {
                    setAction(ActionStates._DEAD);
                }
                else
                {
                    sprite.setColor(colours[colourIndex]);

                    if (++colourIndex >= colours.length)
                    {
                        colourIndex = 0;
                    }

                    sprite.translateX(speed.getX() * direction.getX());
                    sprite.translateY(speed.getY() * direction.getY());

                    distance.subX(speed.getX());
                    distance.subY(speed.getY());
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION12, this);

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
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void tidy(int index)
    {
        App.missileBaseManager.releaseSparkler();
        App.entityData.removeEntity(index);
    }
}
