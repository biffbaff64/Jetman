package com.richikin.jetman.core;

public interface ControlLoop
{
    void initialise();

    void update(StateManager gameState);
}
