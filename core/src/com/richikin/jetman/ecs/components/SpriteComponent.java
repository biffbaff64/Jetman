package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component
{
    public Sprite sprite;

    public float   rotation    = 0.0f;
    public boolean isFlippedX  = false;
    public boolean isFlippedY  = false;
    public boolean isDrawable  = false;
    public boolean isAnimating = false;

    public int frameWidth;
    public int frameHeight;
}
