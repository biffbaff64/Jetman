package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component
{
    public Animation<TextureRegion> animation;
    public float                    elapsedAnimTime;
    public TextureRegion[]          animFrames;
}
