package com.richikin.utilslib.core;

import com.richikin.utilslib.logging.StateManager;

public interface IControlLoop
{
    void initialise();

    void update(StateManager gameState);
}
