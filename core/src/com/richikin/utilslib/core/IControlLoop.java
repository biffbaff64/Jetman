package com.richikin.utilslib.core;

import com.richikin.utilslib.states.StateManager;

public interface IControlLoop
{
    void initialise();

    void update(StateManager gameState);
}
