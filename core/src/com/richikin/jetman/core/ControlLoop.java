package com.richikin.jetman.core;

import com.richikin.utilslib.states.StateManager;

public interface ControlLoop
{
    void initialise();

    void update(StateManager gameState);
}
