package com.richikin.jetman.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.ecs.components.SpriteComponent;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem
{
    // an array used to allow sorting of images allowing
    // us to draw images on top of each other
    private Array<Entity> renderQueue;

    // a comparator to sort images based on the
    // z position of the transfromComponent
    private Comparator<Entity> comparator;

    // component mappers to get components from entities
    private ComponentMapper<SpriteComponent> spriteMapper;

    public RenderingSystem(SpriteBatch spriteBatch)
    {
        // gets all entities with a SpriteComponent
        super(Family.all(SpriteComponent.class).get(), new ZComparator());

        spriteMapper = ComponentMapper.getFor(SpriteComponent.class);
        renderQueue  = new Array<>();
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);

        // Sort the renderQueue base on Z index
        renderQueue.sort(comparator);

        for (Entity entity : renderQueue)
        {
            SpriteComponent sprite = spriteMapper.get(entity);

            if ((sprite != null) && sprite.isDrawable)
            {
                sprite.sprite.draw(App.spriteBatch);
            }
        }

        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        renderQueue.add(entity);
    }
}
