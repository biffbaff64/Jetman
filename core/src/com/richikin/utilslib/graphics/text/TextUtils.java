package com.richikin.utilslib.graphics.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TextUtils
{
    private static BitmapFont font;

    public static void setFont(String fontAsset, int size, Color colour)
    {
        com.richikin.utilslib.graphics.text.FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont(fontAsset, size, colour);
    }

    public static void drawText(String message, float x, float y, SpriteBatch spriteBatch)
    {
        if (font != null)
        {
            font.draw(spriteBatch, message, x, y);
        }
    }
}
