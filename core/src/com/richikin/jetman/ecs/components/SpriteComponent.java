package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component
{
    public Sprite sprite;

    public int spriteNumber;
    public int strength;

    public float   rotation    = 0.0f;
    public boolean isRotating  = false;
    public float   rotateSpeed = 0.0f;

    public boolean isFlippedX  = false;
    public boolean isFlippedY  = false;
    public boolean isDrawable  = false;
    public boolean isAnimating = false;
    public boolean isLinked    = false;
    public boolean isEnemy     = false;

    public int frameWidth;
    public int frameHeight;
}
