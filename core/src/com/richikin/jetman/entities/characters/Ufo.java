package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.Movement;

public class Ufo extends GdxSprite
{
    public Ufo(final App _app)
    {
        super(GraphicID.G_BACKGROUND_UFO, _app);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        setAction(Actions._RUNNING);
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
        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);
    }

    private void setUFOPosition()
    {
        float xPos;

        do
        {
            xPos = MathUtils.random(Gfx.getMapWidth());
        }
        while (app.mapData.viewportBox.contains(xPos, (app.mapData.mapPosition.getY() + 10)));

        sprite.setX(xPos);

        float yPos = (float) (Gfx._VIEW_HEIGHT / 2);

        yPos += MathUtils.random(Gfx._VIEW_HEIGHT * 0.66f);

        sprite.setY(yPos);

        direction.setX(MathUtils.randomBoolean() ? Movement._DIRECTION_LEFT : Movement._DIRECTION_RIGHT);
        speed.set(0.2f + MathUtils.random(0.15f), 0);
    }
}
