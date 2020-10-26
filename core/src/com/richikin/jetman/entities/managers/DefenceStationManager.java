package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.DefenceStation;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.Trace;

public class DefenceStationManager extends GenericEntityManager
{
    private SpriteDescriptor[] descriptors;

    public DefenceStationManager(App _app)
    {
        super(GraphicID.G_DEFENDER, _app);
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

            Array<SimpleVec2> coords = findMultiCoordinates(GraphicID.G_DEFENDER);

            if (coords.isEmpty())
            {
                Trace.__FILE_FUNC("WARNING: Defence Station count ZERO");
            }
            else
            {
                descriptors = new SpriteDescriptor[RoomManager._MAX_DEFENCE_STATIONS];

                for (int i = 0; i < RoomManager._MAX_DEFENCE_STATIONS; i++)
                {
                    descriptors[i]             = Entities.getDescriptor(GraphicID.G_DEFENDER);
                    descriptors[i]._PLAYMODE   = Animation.PlayMode.LOOP;
                    descriptors[i]._POSITION.x = coords.get(i).x;
                    descriptors[i]._POSITION.y = coords.get(i).y;
                    descriptors[i]._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER);
                    descriptors[i]._INDEX      = app.entityData.entityMap.size;
                    descriptors[i]._SIZE       = GameAssets.getAssetSize(GraphicID.G_DEFENDER);

                    DefenceStation teleporter = new DefenceStation(app);
                    teleporter.initialise(descriptors[i]);

                    app.entityData.addEntity(teleporter);

                    activeCount++;
                }
            }
        }
    }
}
