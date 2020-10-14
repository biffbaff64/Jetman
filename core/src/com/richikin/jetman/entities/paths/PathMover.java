
package com.richikin.jetman.entities.paths;


import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;

public interface PathMover
{
    void reset();

    void setNextMove(GdxSprite spriteObject, App app);
}
