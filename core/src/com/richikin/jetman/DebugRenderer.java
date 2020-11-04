
package com.richikin.jetman;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.physics.aabb.CollisionRect;
import com.richikin.utilslib.logging.Meters;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.utilslib.logging.Trace;
import com.richikin.jetman.physics.aabb.AABBData;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.jetman.config.Settings;

public class DebugRenderer implements Disposable
{
    private static TextureRegion debugTextureRegion;
    private static BitmapFont    font;

    public static void setup(String _debugFont)
    {
        debugTextureRegion = new TextureRegion();

        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont(_debugFont, 15, Color.WHITE);
    }

    public static void drawText(String _message, float _x, float _y)
    {
        font.draw(LibApp.spriteBatch, _message, _x, _y);
    }

    public static void drawBoxes()
    {
        if (LibApp.settings.isEnabled(Settings._TILE_BOXES))
        {
            drawTileLayerBoxes();
        }

        if (LibApp.settings.isEnabled(Settings._SPRITE_BOXES))
        {
            drawSpriteCollisionBoxes();
        }
    }

    private static void drawTileLayerBoxes()
    {
        TextureRegion debugTextureRegion;
        Rectangle     debugRectangle = new Rectangle();

        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            CollisionObject collisionObject = AABBData.boxes().get(i);

            switch (collisionObject.gid)
            {
                case _GROUND:
                case _CRATER:
                {
                    debugRectangle.set(collisionObject.rectangle);

                    if (collisionObject.action == ActionStates._COLLIDABLE)
                    {
                        debugTextureRegion = LibApp.assets.getObjectRegion("solid_red32x32");
                    }
                    else if (collisionObject.action == ActionStates._COLLIDING)
                    {
                        debugTextureRegion = LibApp.assets.getObjectRegion("solid_blue32x32");
                    }
                    else
                    {
                        debugTextureRegion = LibApp.assets.getObjectRegion("solid_white32x32");
                    }

                    drawRect
                        (
                            debugTextureRegion,
                            (int) debugRectangle.x,
                            (int) debugRectangle.y,
                            (int) debugRectangle.width,
                            (int) debugRectangle.height,
                            4
                        );
                }
                break;

                default:
                    break;
            }
        }
    }

    private static void drawSpriteCollisionBoxes()
    {
        TextureRegion debugTextureRegion;
        CollisionRect debugRectangle = new CollisionRect(GraphicID.G_NO_ID);

        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            CollisionObject collisionObject = AABBData.boxes().get(i);

            if (collisionObject.type == GraphicID._ENTITY)
            {
                debugRectangle.set(AABBData.boxes().get(i).rectangle);

                if (collisionObject.rectangle.colour == Color.BLUE)
                {
                    debugTextureRegion = LibApp.assets.getObjectRegion("solid_blue32x32");
                }
                else if ((collisionObject.rectangle.colour == Color.RED) || (collisionObject.action == ActionStates._COLLIDING))
                {
                    debugTextureRegion = LibApp.assets.getObjectRegion("solid_red32x32");
                }
                else if (collisionObject.rectangle.colour == Color.YELLOW)
                {
                    debugTextureRegion = LibApp.assets.getObjectRegion("solid_yellow32x32");
                }
                else if (collisionObject.rectangle.colour == Color.GREEN)
                {
                    debugTextureRegion = LibApp.assets.getObjectRegion("solid_green32x32");
                }
                else
                {
                    debugTextureRegion = LibApp.assets.getObjectRegion("solid_white32x32");
                }

                drawRect
                    (
                        debugTextureRegion,
                        (int) debugRectangle.x,
                        (int) debugRectangle.y,
                        (int) debugRectangle.width,
                        (int) debugRectangle.height,
                        4
                    );
            }
        }
    }

    public static void drawRect(int x, int y, int width, int height, int thickness)
    {
        debugTextureRegion = LibApp.assets.getObjectRegion("solid_red32x32");

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    public static void drawRect(int x, int y, int width, int height, int thickness, Color color)
    {
        String asset;

        if (color == Color.BLUE)
        {
            asset = "solid_blue32x32";
        }
        else if (color == Color.RED)
        {
            asset = "solid_red32x32";
        }
        else if (color == Color.YELLOW)
        {
            asset = "solid_yellow32x32";
        }
        else if (color == Color.GREEN)
        {
            asset = "solid_green32x32";
        }
        else
        {
            asset = "solid_white32x32";
        }

        debugTextureRegion = LibApp.assets.getObjectRegion(asset);

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    private static void drawRect(TextureRegion textureRegion, int x, int y, int width, int height, int thickness)
    {
        try
        {
            LibApp.spriteBatch.draw(textureRegion, x, y, width, thickness);
            LibApp.spriteBatch.draw(textureRegion, x, y, thickness, height);
            LibApp.spriteBatch.draw(textureRegion, x, (y + height) - thickness, width, thickness);
            LibApp.spriteBatch.draw(textureRegion, (x + width) - thickness, y, thickness, height);
        }
        catch (NullPointerException exception)
        {
            Trace.__FILE_FUNC("NullPointerException !");
            Trace.dbg("textureRegion: " + textureRegion);
            Trace.dbg("x: " + x);
            Trace.dbg("y: " + y);
            Trace.dbg("width: " + width);
            Trace.dbg("height: " + height);
            Trace.dbg("thickness: " + thickness);
            Trace.dbg("From: " + new Exception().getStackTrace()[1].getClassName());

            Stats.incMeter(Meters._NULL_POINTER_EXCEPTION.get());
        }
    }

    @Override
    public void dispose()
    {
        debugTextureRegion.getTexture().dispose();
        debugTextureRegion = null;
    }
}
