package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.utilslib.states.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.physics.Movement;

public class Laser extends GdxSprite
{
    private final Color[] colourList = new Color[]
        {
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.WHITE,
            Color.ORANGE,
            Color.YELLOW,
            Color.YELLOW,
            Color.GREEN,
            Color.WHITE,
            Color.CYAN,
            Color.WHITE,
            Color.YELLOW,
        };

    private final App app;

    public Laser(App _app)
    {
        super(GraphicID.G_LASER, _app);

        this.app = _app;

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_ENEMY_WEAPON;
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        float x;

        if (descriptor._PARENT.lookingAt.getX() == Movement._DIRECTION_LEFT)
        {
            x = descriptor._PARENT.sprite.getX() - frameWidth;

            distance.set(x - app.mapData.mapPosition.getX(), 0);
        }
        else
        {
            x = descriptor._PARENT.sprite.getX() + (descriptor._PARENT.collisionObject.rectangle.getWidth() + 1);

            distance.set((app.mapData.mapPosition.getX() + Gfx._VIEW_WIDTH) - x, 0);
        }

        float y = (descriptor._PARENT.sprite.getY() + (descriptor._PARENT.frameHeight / 2)) - 2;

        sprite.setPosition(x, y);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        direction.set(descriptor._PARENT.lookingAt.getX(), 0);
        speed.set(Math.max((app.getPlayer().speed.getX() + 12), 24), 0);

        sprite.setColor(colourList[((MainPlayer) descriptor._PARENT).laserColour]);

        setAction(Actions._RUNNING);
    }

    @Override
    public void update(int spriteNum)
    {
        if (distance.getX() <= 0)
        {
            setAction(Actions._DEAD);
        }
        else
        {
            sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));
            distance.subX(speed.getX());
        }

        isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);

        animate();

        updateCommon();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        sprite.setFlip(isFlippedX, isFlippedY);

        super.draw(spriteBatch);
    }

    @Override
    public void animate()
    {
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }
}
