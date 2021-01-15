package com.richikin.jetman.characters.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.physics.Movement;

public class Ufo extends GdxSprite
{
    public Ufo()
    {
        super(GraphicID.G_BACKGROUND_UFO);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        setAction(ActionStates._RUNNING);
        sprite.setColor(Color.WHITE);

        bodyCategory = Gfx.CAT_SCENERY;
        collidesWith = Gfx.CAT_NOTHING;

        elapsedAnimTime = 0;
        isDrawable = true;
        isAnimating = true;

        setUFOPosition();
    }

    @Override
    public void update(int spriteNum)
    {
        if ((sprite.getX() <= 0) || (sprite.getX() >= Gfx.mapWidth))
        {
            sprite.setY(((float) Gfx._VIEW_HEIGHT / 2) + MathUtils.random(Gfx._VIEW_HEIGHT / 3));
            direction.toggleX();
            speed.setX(0.2f + MathUtils.random(0.2f));
        }

        sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());

        updateCommon();
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

//    @Override
//    public void draw(SpriteBatch spriteBatch)
//    {
//        spriteBatch.draw
//            (
//                App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true),
//                (App.mapData.mapPosition.x + (sprite.getX() - (Gfx._VIEW_WIDTH / 2.0f))),
//                (App.mapData.mapPosition.y + (sprite.getY() - (Gfx._VIEW_HEIGHT / 2.0f)))
//            );
//
//        elapsedAnimTime += Gdx.graphics.getDeltaTime();
//    }

    private void setUFOPosition()
    {
        float xPos;

        do
        {
            xPos = MathUtils.random(Gfx.getMapWidth());
        }
        while (App.mapData.viewportBox.contains(xPos, (App.mapData.mapPosition.getY() + 10)));

        sprite.setX(xPos);

        float yPos = MathUtils.random(Gfx._VIEW_HEIGHT * 0.4f) + Gfx._VIEW_HALF_HEIGHT;

        sprite.setY(yPos);

        direction.setX(MathUtils.randomBoolean() ? Movement._DIRECTION_LEFT : Movement._DIRECTION_RIGHT);
        speed.set(0.2f + MathUtils.random(0.15f), 0);
    }
}
