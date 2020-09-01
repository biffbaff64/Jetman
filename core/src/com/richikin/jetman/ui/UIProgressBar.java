
package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.Color;

interface UIProgressBar
{
    void draw(int x, int y);

    void updateSlowDecrement();

    void updateSlowDecrementWithWrap(int wrap);

    boolean updateSlowIncrement();

    void setColor(Color color);

    void setSpeed(float speed);

    float getSpeed();

    void setSubInterval(int subInterval);

    void setAddInterval(int addInterval);
}
