package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.DefenceStation;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.Trace;

public class DefenceStationManager extends GenericEntityManager
{
    private final App app;

    public DefenceStationManager(App _app)
    {
        super(GraphicID.G_DEFENDER, _app);

        this.app = _app;
    }

    @Override
    public void init()
    {
        activeCount = 0;

        createDefenceStations();
    }

    @Override
    public void update()
    {
        if (activeCount == 0)
        {
            createDefenceStations();
        }
    }

    @Override
    public void free()
    {
        activeCount = 0;
    }

    private void createDefenceStations()
    {
        Trace.__FILE_FUNC();

        if (app.entityUtils.canUpdate(GraphicID.G_DEFENDER))
        {
            activeCount = 0;

            Array<SimpleVec2> vec2 = findMultiCoordinates(GraphicID.G_DEFENDER);

            if (vec2.isEmpty())
            {
                Trace.__FILE_FUNC("WARNING: Defence Station map count ZERO");
            }
            else
            {
                addDefenceStations(vec2);

                activeCount = vec2.size;
            }
        }
    }

    /**
     * Adds the two defence stations either side
     * of the missile base.
     */
    private void addDefenceStations(Array<SimpleVec2> vec2)
    {
        Trace.__FILE_FUNC();

        SpriteDescriptor[] descriptors = new SpriteDescriptor[RoomManager._MAX_DEFENCE_STATIONS];

        //
        // Add a Defence Station to the left of the base
        descriptors[0]             = Entities.getDescriptor(GraphicID.G_DEFENDER);
        descriptors[0]._POSITION.x = vec2.get(0).x;
        descriptors[0]._POSITION.y = vec2.get(0).y;
        descriptors[0]._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER);
        descriptors[0]._SIZE       = GameAssets.getAssetSize(GraphicID.G_DEFENDER);
        descriptors[0]._INDEX      = app.entityData.entityMap.size;

        app.entityData.defenceStations = new DefenceStation[RoomManager._MAX_DEFENCE_STATIONS];

        app.entityData.defenceStations[0] = new DefenceStation(app);
        app.entityData.defenceStations[0].initialise(descriptors[0]);
        app.entityData.addEntity(app.entityData.defenceStations[0]);
        app.entityData.defenceStations[0].addZapper();

        //
        // Add a Defence Station to the right of the base
        descriptors[1] = descriptors[0];
        descriptors[1]._POSITION.x = vec2.get(1).x;
        descriptors[1]._POSITION.y = vec2.get(1).y;

        app.entityData.defenceStations[1] = new DefenceStation(app);
        app.entityData.defenceStations[1].initialise(descriptors[1]);
        app.entityData.addEntity(app.entityData.defenceStations[1]);
        app.entityData.defenceStations[1].addZapper();
    }
}
