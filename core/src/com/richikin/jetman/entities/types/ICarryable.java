package com.richikin.jetman.entities.types;

public interface ICarryable
{
    void updateAttachedToPlayer();

    void updateAttachedToRover();

    void explode();

    void setCollisionListener();
}
