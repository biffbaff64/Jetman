
package com.richikin.jetman.graphics.parallax;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.logging.Trace;

public class ParallaxBackground implements Disposable
{
    public final Array<ParallaxLayer> layers;

    public ParallaxBackground()
    {
        this.layers = new Array<>();
    }

    public void setupLayers(LayerImage[] layerImage)
    {
        layers.clear();
        addLayers(layerImage);
    }

    public void render()
    {
        for (int layer = 0; layer < layers.size; layer++)
        {
            layers.get(layer).draw();
        }
    }

    public void addLayers(LayerImage[] layerImage)
    {
        for (int layer = 0; layer < layerImage.length; layer++)
        {
            layers.add(new ParallaxLayer(layerImage[layer].layerName));

            layers.get(layer).xSpeed = layerImage[layer].horizontalSpeed;
            layers.get(layer).ySpeed = layerImage[layer].verticalSpeed;
        }
    }

    public void addLayerGroup(String[] layerGroup)
    {
        for (int layer = 0, arraySize = layerGroup.length; layer < arraySize; layer++)
        {
            layers.add(new ParallaxLayer(layerGroup[layer]));
            layers.get(layer).setTextureRegion();
        }
    }

    public void addLayerGroupSpeeds(float[][] speeds)
    {
        if ((speeds[0].length != layers.size) || (speeds[0].length != speeds[1].length))
        {
            Trace.__FILE_FUNC_WithDivider();
            Trace.dbg("ERROR: SPEED and LAYERS arrays size mismatch.");
        }

        int arraySize = speeds[0].length;

        for (int layer = 0; layer < arraySize; layer++)
        {
            layers.get(layer).xSpeed = speeds[0][layer];
            layers.get(layer).ySpeed = speeds[1][layer];
        }
    }

    public void scrollLayersUp()
    {
        for (int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_STILL, Movement._DIRECTION_UP);
        }
    }

    public void scrollLayersDown()
    {
        for (int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN);
        }
    }

    public void scrollLayersLeft()
    {
        for (int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }
    }

    public void scrollLayersRight()
    {
        for (int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        for (int i = 0; i < layers.size; i++)
        {
            layers.get(i).dispose();
            layers.removeIndex(i);
        }
    }
}
