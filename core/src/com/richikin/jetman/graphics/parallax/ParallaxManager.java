package com.richikin.jetman.graphics.parallax;

import com.richikin.jetman.core.App;

public class ParallaxManager
{
    public ParallaxManager()
    {
    }

    public void scroll()
    {
        if ((App.mapData.mapPosition.getX() > App.mapData.minScrollX)
            && (App.mapData.mapPosition.getX() < App.mapData.maxScrollX))
        {
            if (App.mapData.previousMapPosition.getX() > App.mapData.mapPosition.getX())
            {
                App.baseRenderer.parallaxBackground.scrollLayersLeft();
                App.baseRenderer.parallaxForeground.scrollLayersLeft();
            }
            else if (App.mapData.previousMapPosition.getX() < App.mapData.mapPosition.getX())
            {
                App.baseRenderer.parallaxBackground.scrollLayersRight();
                App.baseRenderer.parallaxForeground.scrollLayersRight();
            }
        }

        if ((App.mapData.mapPosition.getY() > App.mapData.minScrollY)
            && (App.mapData.mapPosition.getY() < App.mapData.maxScrollY))
        {
            if (App.mapData.previousMapPosition.getY() > App.mapData.mapPosition.getY())
            {
                App.baseRenderer.parallaxBackground.scrollLayersDown();
                App.baseRenderer.parallaxForeground.scrollLayersDown();
            }
            else if (App.mapData.previousMapPosition.getY() < App.mapData.mapPosition.getY())
            {
                App.baseRenderer.parallaxBackground.scrollLayersUp();
                App.baseRenderer.parallaxForeground.scrollLayersUp();
            }
        }
    }
}
