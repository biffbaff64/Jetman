
package com.richikin.utilslib.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.utilslib.maths.SimpleVec2F;

public class SimpleDrawable
{
    public final TextureRegion image;
    public final SimpleVec2F   position;
    public final int           width;
    public final int           height;

    public SimpleDrawable()
    {
        this.width      = 16;
        this.height     = 16;
        this.image      = new TextureRegion();
        this.position   = new SimpleVec2F();
    }

    public SimpleDrawable(TextureRegion image, float x, float y)
    {
        this.image      = image;
        this.position   = new SimpleVec2F(x, y);
        this.width      = image.getRegionWidth();
        this.height     = image.getRegionHeight();
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(image, position.getX(), position.getY(), width, height);
    }
}
