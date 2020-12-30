package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.TwinkleStar;
import com.richikin.jetman.entities.characters.Ufo;
import com.richikin.enumslib.GraphicID;

public class BackgroundObjectsManager
{
    public BackgroundObjectsManager()
    {
    }

    /**
     * Add a number of background UFOs
     *
     * @param numUfos The number of UFOs to add
     */
    public void addUFOs(int numUfos)
    {
        for (int i = 0; i < numUfos; i++)
        {
            SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_BACKGROUND_UFO);
            descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_BACKGROUND_UFO);
            descriptor._POSITION.x = 0;
            descriptor._POSITION.y = 0;
            descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_BACKGROUND_UFO);
            descriptor._INDEX      = App.entityData.entityMap.size;

            Ufo ufo = new Ufo();
            ufo.initialise(descriptor);

            App.entityData.addEntity(ufo);
        }
    }

    private final int[][] twinklestarPositions =
        {
            { 3, 12},    // 0
            { 6, 16},    // 1
            {10, 19},    // 2
            {11, 11},    // 3
            {15, 17},    // 4
            {19, 12},    // 5
            {22, 13},    // 6
            {29, 19},    // 7
            {29, 13},    // 8
            {34, 14},    // 9
            {41, 12},    // 10
            {41, 18},    // 11
            {44, 13},    // 12
            {51, 13},    // 13
            {51, 18},    // 14
            {56, 11},    // 15
            {53, 15},    // 16
        };

    public void addTwinkleStars()
    {
        for (int[] position : twinklestarPositions)
        {
            String asset = (MathUtils.random(100) < 50) ? GameAssets._TWINKLE_STAR1_ASSET : GameAssets._TWINKLE_STAR2_ASSET;

            SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_TWINKLE_STAR);
            descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_TWINKLE_STAR);
            descriptor._POSITION.x = position[0];
            descriptor._POSITION.y = position[1];
            descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_TWINKLE_STAR);
            descriptor._INDEX      = App.entityData.entityMap.size;

            TwinkleStar twinkleStar = new TwinkleStar();
            twinkleStar.initialise(descriptor);

            App.entityData.addEntity(twinkleStar);
        }
    }

    public void removeBackgroundEntities()
    {
        for (int i=0; i<App.entityData.entityMap.size; i++)
        {
            if ((App.entityData.entityMap.get(i).gid == GraphicID.G_TWINKLE_STAR)
                || (App.entityData.entityMap.get(i).gid == GraphicID.G_BACKGROUND_UFO))
            {
                App.entityData.entityMap.get(i).setAction(ActionStates._DEAD);
            }
        }
    }
}
