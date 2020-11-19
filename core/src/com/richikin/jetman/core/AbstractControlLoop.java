package com.richikin.jetman.core;

import com.richikin.utilslib.core.IControlLoop;
import com.richikin.utilslib.logging.StateManager;

public abstract class AbstractControlLoop implements IControlLoop
{
    public AbstractControlLoop()
    {
    }

    @Override
    public abstract void initialise();

    @Override
    public abstract void update();
}
