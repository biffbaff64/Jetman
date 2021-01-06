package com.richikin.jetman.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderingSystem extends SortedIteratingSystem
{
    public RenderingSystem(SpriteBatch spriteBatch)
    {
        // gets all entities with a TransofmComponent and TextureComponent
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {

    }
}
