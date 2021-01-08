package com.richikin.jetman.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class PlayerControlSystem extends IteratingSystem
{
    public PlayerControlSystem(Family family)
    {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {

    }
}
