package com.richikin.jetman.graphics.parallax;

import com.richikin.jetman.core.App;

public class ParallaxManager
{
    private final App app;

    public ParallaxManager(App _app)
    {
        this.app = _app;
    }

    public void scroll()
    {
        if ((app.mapData.mapPosition.getX() > app.mapData.minScrollX)
            && (app.mapData.mapPosition.getX() < app.mapData.maxScrollX))
        {
            if (app.mapData.previousMapPosition.getX() < app.mapData.mapPosition.getX())
            {
                app.baseRenderer.parallaxMiddle.scrollLayersLeft();
                app.baseRenderer.parallaxMiddle.scrollLayersLeft();
            }
            else if (app.mapData.previousMapPosition.getX() > app.mapData.mapPosition.getX())
            {
                app.baseRenderer.parallaxMiddle.scrollLayersRight();
                app.baseRenderer.parallaxMiddle.scrollLayersRight();
            }
        }

        if ((app.mapData.mapPosition.getY() > app.mapData.minScrollY)
            && (app.mapData.mapPosition.getY() < app.mapData.maxScrollY))
        {
            if (app.mapData.previousMapPosition.getY() < app.mapData.mapPosition.getY())
            {
                app.baseRenderer.parallaxMiddle.scrollLayersDown();
                app.baseRenderer.parallaxMiddle.scrollLayersDown();
            }
            else if (app.mapData.previousMapPosition.getY() > app.mapData.mapPosition.getY())
            {
                app.baseRenderer.parallaxMiddle.scrollLayersUp();
                app.baseRenderer.parallaxMiddle.scrollLayersUp();
            }
        }
    }
}
