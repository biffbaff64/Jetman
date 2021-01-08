package com.richikin.jetman.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class PhysicsSystem extends IteratingSystem
{
    public PhysicsSystem(Family family)
    {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {

    }
}
