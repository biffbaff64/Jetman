package com.richikin.jetman.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.richikin.jetman.ecs.components.AnimationComponent;
import com.richikin.jetman.ecs.components.SpriteComponent;

public class AnimationSystem extends IteratingSystem
{
    ComponentMapper<AnimationComponent> animationMapper;
    ComponentMapper<SpriteComponent>    spriteMapper;

    public AnimationSystem()
    {
        super(Family.all(AnimationComponent.class, SpriteComponent.class).get());

        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
        spriteMapper    = ComponentMapper.getFor(SpriteComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        AnimationComponent anim = animationMapper.get(entity);
        SpriteComponent sprite = spriteMapper.get(entity);

        if (sprite.isAnimating)
        {
            sprite.sprite.setRegion(anim.animation.getKeyFrame(anim.elapsedAnimTime, anim.isLooping));
            anim.elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }
}
