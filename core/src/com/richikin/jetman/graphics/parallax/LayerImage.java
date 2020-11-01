
package com.richikin.jetman.graphics.parallax;

public class LayerImage
{
    public final String layerName;
    public final float  horizontalSpeed;
    public final float  verticalSpeed;

    public LayerImage(String _layerName, float _horizontalSpeed, float _verticalSpeed)
    {
        super();

        this.layerName       = _layerName;
        this.horizontalSpeed = _horizontalSpeed;
        this.verticalSpeed   = _verticalSpeed;
    }
}
