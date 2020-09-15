package com.richikin.jetman.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.EntityDef;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.maps.MarkerTile;

public class GenericEntityBuilder
{
    private final App app;

    public GenericEntityBuilder(App _app)
    {
        this.app = _app;
    }

    public void processPlacementTiles()
    {
        EntityDescriptor descriptor;

        for (MarkerTile tile : app.mapCreator.placementTiles)
        {
            descriptor = createDescriptor(tile, Entities.getEntityDef(tile._GID));
        }
    }

    public EntityDescriptor createDescriptor(MarkerTile _markerTile, EntityDef _entityDef)
    {
        EntityDescriptor descriptor = new EntityDescriptor
            (
                _markerTile._X,
                _markerTile._Y,
                app.entityUtils.getInitialZPosition(_markerTile._GID),
                app.assets.getAnimationRegion(_markerTile._ASSET),
                _entityDef.frames,
                Animation.PlayMode.LOOP
            );

        descriptor._GID   = _markerTile._GID;
        descriptor._INDEX = app.entityData.entityMap.size;

        return descriptor;
    }
}
