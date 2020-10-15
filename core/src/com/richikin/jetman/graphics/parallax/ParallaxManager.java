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
            if (app.mapData.previousMapPosition.getX() > app.mapData.mapPosition.getX())
            {
                app.baseRenderer.parallaxBackground.scrollLayersLeft();
                app.baseRenderer.parallaxMiddle.scrollLayersLeft();
                app.baseRenderer.parallaxForeground.scrollLayersLeft();
            }
            else if (app.mapData.previousMapPosition.getX() < app.mapData.mapPosition.getX())
            {
                app.baseRenderer.parallaxBackground.scrollLayersRight();
                app.baseRenderer.parallaxMiddle.scrollLayersRight();
                app.baseRenderer.parallaxForeground.scrollLayersRight();
            }
        }

        if ((app.mapData.mapPosition.getY() > app.mapData.minScrollY)
            && (app.mapData.mapPosition.getY() < app.mapData.maxScrollY))
        {
            if (app.mapData.previousMapPosition.getY() > app.mapData.mapPosition.getY())
            {
                app.baseRenderer.parallaxBackground.scrollLayersDown();
                app.baseRenderer.parallaxMiddle.scrollLayersDown();
                app.baseRenderer.parallaxForeground.scrollLayersDown();
            }
            else if (app.mapData.previousMapPosition.getY() < app.mapData.mapPosition.getY())
            {
                app.baseRenderer.parallaxBackground.scrollLayersUp();
                app.baseRenderer.parallaxMiddle.scrollLayersUp();
                app.baseRenderer.parallaxForeground.scrollLayersUp();
            }
        }
    }
}
