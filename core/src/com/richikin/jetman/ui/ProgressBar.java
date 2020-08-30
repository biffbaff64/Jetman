
package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.jetman.maths.ItemF;
import com.richikin.jetman.utils.logging.StopWatch;

@SuppressWarnings("WeakerAccess")
public class ProgressBar extends ItemF implements Disposable
{
    private static final int _DEFAULT_BAR_HEIGHT = 26;

    public boolean   justEmptied;
    public boolean   isAutoRefilling;
    public float     height;
    public float     scale;

    private NinePatch ninePatch;
    private final App app;

    public ProgressBar(int _speed, int delay, int size, int maxSize, String texture, App _app)
    {
        this.app = _app;

        ninePatch = new NinePatch(_app.assets.getObjectRegion(texture), 1, 1, 1, 1);

        this.minimum         = 0;
        this.maximum         = maxSize;
        this.refillAmount    = 0;
        this.stopWatch       = StopWatch.start();
        this.total           = size;
        this.height          = _DEFAULT_BAR_HEIGHT;
        this.refillAmount    = maxSize;
        this.justEmptied     = false;
        this.isAutoRefilling = false;
        this.scale           = 1;
    }

    public void draw(int x, int y)
    {
        if (total > 0)
        {
            ninePatch.draw(app.spriteBatch, x, y, total * scale, height);
        }
    }

    public void setColor(Color color)
    {
        this.ninePatch.setColor(color);
    }

    @Override
    public void dispose()
    {
        ninePatch = null;
        stopWatch = null;
    }
}
