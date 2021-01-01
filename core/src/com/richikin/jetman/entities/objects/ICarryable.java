package com.richikin.jetman.entities.objects;

public interface ICarryable
{
    void updateAttachedToPlayer();

    void updateAttachedToRover();

    void explode();

    void setCollisionListener();
}
