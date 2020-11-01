package com.richikin.utilslib.graphics.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TextUtils
{
    private static BitmapFont font;

    public static void setFont(String _fontAsset, int _size, Color _colour)
    {
        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont(_fontAsset, _size, _colour);
    }

    public static void drawText(String _message, float _x, float _y, SpriteBatch spriteBatch)
    {
        if (font != null)
        {
            font.draw(spriteBatch, _message, _x, _y);
        }
    }
}
