package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.richikin.enumslib.ActionStates;

public class StateComponent implements Component
{
    private ActionStates entityAction;

    public ActionStates getAction()
    {
        return entityAction;
    }

    public void setAction(ActionStates newAction)
    {
        entityAction = newAction;
    }
}
