package com.richikin.jetman.physics;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;
import com.richikin.jetman.physics.AABB.CollisionObject;

public interface ICollideUtils
{
    void initialise();

    CollisionObject newObject();

    CollisionObject newObject(Rectangle rectangle);

    CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID);

    void tidy();

    void debugAll();

    boolean canCollide(GdxSprite entity, GdxSprite target);

    boolean filter(short theEntityFlag, short theCollisionBoxFlag);

    TileID getMarkerTileOn(int x, int y);

    int getXBelow(GdxSprite spriteObj);

    int getYBelow(GdxSprite spriteObj);

//    CollisionObject getBoxHittingTop(GdxSprite spriteObject);
//
//    CollisionObject getBoxHittingBottom(GdxSprite spriteObject);
//
//    CollisionObject getBoxHittingLeft(GdxSprite spriteObject);
//
//    CollisionObject getBoxHittingRight(GdxSprite spriteObject);
}
