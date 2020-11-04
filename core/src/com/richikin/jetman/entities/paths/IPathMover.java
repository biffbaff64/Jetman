
package com.richikin.jetman.entities.paths;

import com.richikin.jetman.entities.objects.GdxSprite;

public interface IPathMover
{
    void reset();

    void setNextMove(GdxSprite spriteObject);
}
