package com.richikin.jetman.entities.characters;

import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;

public class Teleporter extends GdxSprite
{
    public int          teleporterNumber;
    public boolean      isCollected;
    public GdxSprite    collector;

    private App app;

    public Teleporter(App _app)
    {
        super(GraphicID.G_TRANSPORTER, _app);

        this.app = _app;
    }
}
