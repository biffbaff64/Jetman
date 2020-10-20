
package com.richikin.jetman.graphics.camera;

import com.richikin.jetman.core.App;

public class CameraUtils
{
    private final App app;

    public CameraUtils(App _app)
    {
        this.app = _app;
    }

    public void resetCameraZoom()
    {
        app.baseRenderer.parallaxGameCamera.camera.update();
        app.baseRenderer.tiledGameCamera.camera.update();
        app.baseRenderer.spriteGameCamera.camera.update();
        app.baseRenderer.hudGameCamera.camera.update();

        app.baseRenderer.gameZoom.stop();
        app.baseRenderer.hudZoom.stop();

        app.baseRenderer.parallaxGameCamera.camera.zoom = 1.0f;
        app.baseRenderer.tiledGameCamera.camera.zoom    = 1.0f;
        app.baseRenderer.spriteGameCamera.camera.zoom   = 1.0f;
        app.baseRenderer.hudGameCamera.camera.zoom      = 1.0f;
    }

    public void enableAllCameras()
    {
        app.baseRenderer.parallaxGameCamera.isInUse = true;
        app.baseRenderer.tiledGameCamera.isInUse    = true;
        app.baseRenderer.spriteGameCamera.isInUse   = true;
        app.baseRenderer.hudGameCamera.isInUse      = true;

        app.baseRenderer.isDrawingStage = true;
    }

    public void disableAllCameras()
    {
        app.baseRenderer.parallaxGameCamera.isInUse = false;
        app.baseRenderer.tiledGameCamera.isInUse    = false;
        app.baseRenderer.spriteGameCamera.isInUse   = false;
        app.baseRenderer.hudGameCamera.isInUse      = false;

        app.baseRenderer.isDrawingStage = false;
    }
}
