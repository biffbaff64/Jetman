
package com.richikin.jetman.graphics;

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

    public SimpleDrawable(TextureRegion _image, float _x, float _y)
    {
        this.image      = _image;
        this.position   = new SimpleVec2F(_x, _y);
        this.width      = _image.getRegionWidth();
        this.height     = _image.getRegionHeight();
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(image, position.getX(), position.getY(), width, height);
    }
}
