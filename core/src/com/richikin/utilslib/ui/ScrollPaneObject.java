
package com.richikin.utilslib.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.Vec2Boolean;
import com.richikin.utilslib.maths.XYSetF;

public class ScrollPaneObject
{
    public final Table       table;
    public final Skin        skin;
    public final SimpleVec2F size;
    public final XYSetF      position;
    public final Vec2Boolean fadeDisable;
    public final boolean     scrollFadeBars;
    public final String      name;

    public ScrollPaneObject()
    {
        table           = new Table();
        skin            = new Skin();
        size            = new SimpleVec2F();
        position        = new XYSetF();
        fadeDisable     = new Vec2Boolean(false, false);
        scrollFadeBars  = false;
        name            = "Undefined";
    }
}
