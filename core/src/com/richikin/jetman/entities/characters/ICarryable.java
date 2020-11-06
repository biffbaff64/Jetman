package com.richikin.jetman.entities.characters;

public interface ICarryable
{
    void updateAttachedToPlayer();

    void updateAttachedToRover();

    void explode();

    void setCollisionListener();
}
