
package com.richikin.jetman.graphics.parallax;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.maths.Box;
import com.richikin.jetman.maths.XYSetF;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Movement;

public class ParallaxLayer implements Disposable
{
    public final String        name;
    public final TextureRegion textureRegion;
    public final XYSetF        offset;
    public final XYSetF        position;
    public final boolean       isActive;

    public Direction direction;
    public float     xSpeed;
    public float     ySpeed;

    private final Box imageBox;
    private final App app;

    public ParallaxLayer(String textureName, App _app)
    {
        this.app = _app;

        this.name = textureName;

        Texture texture = app.assets.loadSingleAsset(textureName, Texture.class);

        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        textureRegion = new TextureRegion(texture);
        imageBox      = new Box(texture.getWidth(), texture.getHeight());
        offset        = new XYSetF(0f, texture.getHeight() - Gfx._VIEW_HEIGHT);
        position      = new XYSetF(0f, 0f);
        direction     = new Direction();
        isActive      = true;
        xSpeed        = 0.0f;
        ySpeed        = 0.0f;

        setTextureRegion();
    }

    public void draw()
    {
        if (isActive)
        {
            app.spriteBatch.draw(textureRegion, position.getX(), position.getY());
        }
    }

    public void scrollLayer(int xDirection, int yDirection)
    {
        if (isActive)
        {
            boolean isChanged = false;

            if (xDirection != Movement._DIRECTION_STILL)
            {
                offset.addXWrapped(xSpeed * xDirection, 0.0f, imageBox.width - Gfx._VIEW_WIDTH);
                isChanged = true;
            }

//            if (xDirection == Movement._DIRECTION_LEFT)
//            {
//                offset.addXWrapped(xSpeed, 0.0f, imageBox.width - Gfx._VIEW_WIDTH);
//                isChanged = true;
//            }
//            else
//            {
//                if (xDirection == Movement._DIRECTION_RIGHT)
//                {
//                    offset.subXWrapped(xSpeed, 0.0f, imageBox.width - Gfx._VIEW_WIDTH);
//                    isChanged = true;
//                }
//            }

            if (isChanged)
            {
                setTextureRegion();
            }
        }
    }

    public void setTextureRegion()
    {
        textureRegion.setRegion
            (
                (int) offset.getX(),
                (int) offset.getY(),
                Gfx._VIEW_WIDTH,
                Gfx._VIEW_HEIGHT
            );
    }

    public void reset()
    {
        offset.set(0, 0);
        position.set(0, 0);
        direction.standStill();
    }

    @Override
    public void dispose()
    {
        app.assets.unloadAsset(name);
        textureRegion.getTexture().dispose();
    }
}
