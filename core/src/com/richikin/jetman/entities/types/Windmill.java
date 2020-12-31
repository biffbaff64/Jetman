package com.richikin.jetman.entities.types;

import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.characters.Blob;
import com.richikin.jetman.entities.objects.SpriteDescriptor;

public class Windmill
{
    private int[][] initPositions =
        {
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
        };

    private static final int NUM_SECTIONS = 9;

    private Blob[] blobs;

    public Windmill()
    {
    }

    public void initialise()
    {
        blobs = new Blob[NUM_SECTIONS];

        for (Blob blob : blobs)
        {
            SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_BLOB);
            descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_BLOB);
            descriptor._INDEX = App.entityData.entityMap.size;
            descriptor._POSITION.x = initPositions[index].x;
            descriptor._POSITION.y = initPositions[index].y;
            descriptor._POSITION.z = initPositions[index].z;

            blob = new Blob();
            blob.initialise(descriptor);

            App.entityData.addEntity(blob);
        }
    }
}
