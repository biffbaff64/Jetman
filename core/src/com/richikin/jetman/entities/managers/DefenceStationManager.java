package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.characters.DefenceStation;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.Trace;

public class DefenceStationManager extends GenericEntityManager
{
    public DefenceStationManager()
    {
        super(GraphicID.G_DEFENDER);
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

        if (App.entityUtils.canUpdate(GraphicID.G_DEFENDER))
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
        descriptors[0]             = App.entities.getDescriptor(GraphicID.G_DEFENDER);
        descriptors[0]._POSITION.x = vec2.get(0).x;
        descriptors[0]._POSITION.y = vec2.get(0).y;
        descriptors[0]._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER);
        descriptors[0]._SIZE       = GameAssets.getAssetSize(GraphicID.G_DEFENDER);
        descriptors[0]._INDEX      = App.entityData.entityMap.size;

        App.entityData.defenceStations = new DefenceStation[RoomManager._MAX_DEFENCE_STATIONS];

        App.entityData.defenceStations[0] = new DefenceStation();
        App.entityData.defenceStations[0].initialise(descriptors[0]);
        App.entityData.addEntity(App.entityData.defenceStations[0]);
        App.entityData.defenceStations[0].addZapper();

        //
        // Add a Defence Station to the right of the base
        descriptors[1] = descriptors[0];
        descriptors[1]._POSITION.x = vec2.get(1).x;
        descriptors[1]._POSITION.y = vec2.get(1).y;

        App.entityData.defenceStations[1] = new DefenceStation();
        App.entityData.defenceStations[1].initialise(descriptors[1]);
        App.entityData.addEntity(App.entityData.defenceStations[1]);
        App.entityData.defenceStations[1].addZapper();
    }

    public void killStations()
    {
        if (App.entityData.entityMap != null)
        {
            GdxSprite currentEntity;

            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                currentEntity = (GdxSprite) App.entityData.entityMap.get(i);

                if (currentEntity.gid == GraphicID.G_DEFENDER)
                {
                    ((DefenceStation) currentEntity).explode();
                }
            }
        }
    }
}
