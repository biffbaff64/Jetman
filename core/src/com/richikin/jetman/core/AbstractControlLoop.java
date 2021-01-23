package com.richikin.jetman.core;

import com.richikin.utilslib.core.IControlLoop;

public abstract class AbstractControlLoop implements IControlLoop
{
    public AbstractControlLoop()
    {
    }

    @Override
    public abstract void update();
}
