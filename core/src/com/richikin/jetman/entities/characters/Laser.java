package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
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

    public Laser()
    {
        super(GraphicID.G_LASER);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_ENEMY_WEAPON;

        float x;

        if (descriptor._PARENT.lookingAt.getX() == Movement._DIRECTION_LEFT)
        {
            x = descriptor._PARENT.sprite.getX() - frameWidth;

            distance.set(x - App.mapData.mapPosition.getX(), 0);
        }
        else
        {
            x = descriptor._PARENT.sprite.getX() + (descriptor._PARENT.collisionObject.rectangle.getWidth() + 1);

            distance.set((App.mapData.mapPosition.getX() + Gfx._VIEW_WIDTH) - x, 0);
        }

        float y = (descriptor._PARENT.sprite.getY() + (descriptor._PARENT.frameHeight / 2f)) - 2;

        sprite.setPosition(x, y);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        direction.set(descriptor._PARENT.lookingAt.getX(), 0);
        speed.set(Math.max((App.getPlayer().speed.getX() + 12), 24), 0);

        sprite.setColor(colourList[((MainPlayer) descriptor._PARENT).laserColour]);

        setAction(ActionStates._RUNNING);

        addDynamicPhysicsBody();

        GameAudio.inst().startSound(AudioData.SFX_LASER);
    }

    @Override
    public void update(int spriteNum)
    {
        if (distance.getX() <= 0)
        {
            setAction(ActionStates._DEAD);
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
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }
}
