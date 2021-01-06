package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.richikin.enumslib.GraphicID;

public class IDComponent implements Component
{
    public GraphicID gid  = GraphicID.G_NO_ID;
    public GraphicID type = GraphicID.G_NO_ID;
}
