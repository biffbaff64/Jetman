package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.SimpleVec3;
import com.richikin.utilslib.maths.XYSetF;
import com.richikin.utilslib.physics.Direction;

public class TransformComponent implements Component
{
    public final SimpleVec3  position  = new SimpleVec3();
    public final SimpleVec2F scale     = new SimpleVec2F(1.0f, 1.0f);
    public final Direction   direction = new Direction();
    public final Direction   lookingAt = new Direction();
    public final SimpleVec2F speed     = new SimpleVec2F();
    public final XYSetF      distance  = new XYSetF();
}
