package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.TwinkleStar;
import com.richikin.jetman.entities.characters.Ufo;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;

public class BackgroundObjectsManager
{
    private final App app;

    public BackgroundObjectsManager(App _app)
    {
        this.app = _app;
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
            SpriteDescriptor entityDescriptor = Entities.getDescriptor(GraphicID.G_BACKGROUND_UFO);
            entityDescriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_BACKGROUND_UFO);
            entityDescriptor._POSITION.x = 0;
            entityDescriptor._POSITION.y = 0;
            entityDescriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_BACKGROUND_UFO);
            entityDescriptor._INDEX      = app.entityData.entityMap.size;

            Ufo ufo = new Ufo(app);
            ufo.initialise(entityDescriptor);

            app.entityData.addEntity(ufo);
        }
    }

    private final int[][] twinklestarPositions =
        {
            { 3,  7},    // 0
            { 6, 16},    // 1
            {10, 20},    // 2
            {11, 10},    // 3
            {15, 17},    // 4
            {19,  7},    // 5
            {22, 13},    // 6
            {29, 19},    // 7
            {29,  8},    // 8
            {34, 14},    // 9
            {41,  7},    // 10
            {41, 18},    // 11
            {44, 13},    // 12
            {51,  8},    // 13
            {51, 18},    // 14
            {56, 11},    // 15
            {53, 15},    // 16
        };

    public void addTwinkleStars()
    {
        for (int[] position : twinklestarPositions)
        {
            String asset = (MathUtils.random(100) < 50) ? GameAssets._TWINKLE_STAR1_ASSET : GameAssets._TWINKLE_STAR2_ASSET;

            SpriteDescriptor entityDescriptor = Entities.getDescriptor(GraphicID.G_TWINKLE_STAR);
            entityDescriptor._ASSET      = asset;
            entityDescriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_TWINKLE_STAR);
            entityDescriptor._POSITION.x = position[0];
            entityDescriptor._POSITION.y = (Gfx._VIEW_HEIGHT / Gfx.getTileHeight()) - position[1];
            entityDescriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_TWINKLE_STAR);
            entityDescriptor._INDEX      = app.entityData.entityMap.size;

            TwinkleStar twinkleStar = new TwinkleStar(app);
            twinkleStar.initialise(entityDescriptor);

            app.entityData.addEntity(twinkleStar);
        }
    }
}
