package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.SimpleVec3;

public class TransformComponent implements Component
{
    public final SimpleVec3  position = new SimpleVec3();
    public final SimpleVec2F scale    = new SimpleVec2F(1.0f, 1.0f);
}
