package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.structures.LaserBarrier;
import com.richikin.utilslib.maths.SimpleVec2;

public class BarrierManager extends GenericEntityManager
{
    public BarrierManager()
    {
        super(GraphicID.G_POWER_BEAM);
    }

    @Override
    public void init()
    {
        activeCount = 0;
    }

    @Override
    public void create()
    {
        Array<SimpleVec2> coords = findMultiCoordinates(GraphicID.G_POWER_BEAM);

        for (SimpleVec2 coord : coords)
        {
            descriptor             = App.entities.getDescriptor(GraphicID.G_POWER_BEAM);
            descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_POWER_BEAM);
            descriptor._POSITION.x = coord.x;
            descriptor._POSITION.y = coord.y;
            descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_POWER_BEAM);
            descriptor._INDEX      = App.entityData.entityMap.size;

            LaserBarrier laserBarrier = new LaserBarrier(GraphicID.G_POWER_BEAM);
            laserBarrier.initialise(descriptor);
            App.entityData.addEntity(laserBarrier);

            activeCount++;
        }
    }

    @Override
    public void free()
    {
        reset();
    }

    @Override
    public void reset()
    {
        activeCount = 0;
    }
}
