package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.Trace;

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

    public DefenderBullet(GraphicID _gid, App _app)
    {
        super(_gid, _app);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        if (gid == GraphicID.G_ROVER_BULLET)
        {
//            float angle = Math.abs(app.getGun().gunTurret.sprite.getRotation());
//
//            speed.setX(8 * MathUtils.cosDeg(angle));
//            speed.setY(8 * MathUtils.sinDeg(angle));
//
//            direction.setX
//                (
//                    app.getGun().isFlippedX
//                        ? Movement._DIRECTION_LEFT
//                        : Movement._DIRECTION_RIGHT
//                );
//
//            direction.setY
//                (
//                    (app.getGun().gunTurret.sprite.getRotation() != 0.0f)
//                        ? Movement._DIRECTION_UP
//                        : Movement._DIRECTION_STILL
//                );
//
//            if (app.getRover().isFlippedX)
//            {
//                sprite.setPosition
//                    (
//                        (app.getGun().gunTurret.sprite.getX() + 46),
//                        (app.getGun().gunTurret.sprite.getY() + 28)
//                    );
//            }
//            else
//            {
//                sprite.setPosition
//                    (
//                        (app.getGun().gunTurret.sprite.getX() + 51),
//                        (app.getGun().gunTurret.sprite.getY() + 28)
//                    );
//            }
//
//            float count = (68.0f * MathUtils.cosDeg(angle));
//
//            while (count > 0)
//            {
//                sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));
//
//                count -= speed.getX();
//            }

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

        setAction(Actions._RUNNING);
        colourIndex = 0;
    }

    @Override
    public void update(int spriteNum)
    {
        if (getAction() == Actions._RUNNING)
        {
            if (distance.isEmpty())
            {
                setAction(Actions._DEAD);
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
        else
        {
            Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }
}
