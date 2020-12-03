package com.richikin.jetman.graphics.camera;

import com.badlogic.gdx.math.Vector3;
import com.richikin.utilslib.maths.SimpleVec3F;

public interface IGameCamera
{
    void setPosition(SimpleVec3F _position);

    void setPosition(SimpleVec3F _position, float _zoom);

    void setPosition(SimpleVec3F _position, float _zoom, boolean _shake);

    Vector3 getPosition();

    void updatePosition(float targetX, float targetY);

    void lerpTo(SimpleVec3F _position, float _speed);

    void lerpTo(SimpleVec3F _position, float _speed, float _zoom, boolean _shake);

    void resizeViewport(int _width, int _height, boolean _centerCamera);

    void setCameraZoom(float _zoom);

    float getCameraZoom();

    void setZoomDefault(float _zoom);

    float getDefaultZoom();

    void reset();
}
