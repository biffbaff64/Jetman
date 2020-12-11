package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;

public class TeleportBeam implements Disposable
{
    private static class BeamData
    {
        public final SimpleVec2  startPos;
        public final SimpleVec2  distance;
        public final SimpleVec2  speed;
        public final SimpleVec2  direction;
        public final float   angle;
        public final boolean isActive;

        BeamData(SimpleVec2 _pos,
                 SimpleVec2 _distance,
                 SimpleVec2 _speed,
                 SimpleVec2 _direction,
                 float _angle,
                 @SuppressWarnings("SameParameterValue") boolean _isActive)
        {
            this.startPos  = _pos;
            this.distance  = _distance;
            this.speed     = _speed;
            this.direction = _direction;
            this.angle     = _angle;
            this.isActive  = _isActive;
        }
    }

    private final BeamData[] beams = new BeamData[]
        {
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(934, 0),
                new SimpleVec2(24, 0),
                new SimpleVec2(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL),
                90.0f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(1008, 336),
                new SimpleVec2(24, 8),
                new SimpleVec2(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN),
                67.5f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(662, 662),
                new SimpleVec2(16, 16),
                new SimpleVec2(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN),
                45.0f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(336, 1008),
                new SimpleVec2(8, 24),
                new SimpleVec2(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN),
                22.5f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(0, 858),
                new SimpleVec2(0, 20),
                new SimpleVec2(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                0.0f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(336, 1008),
                new SimpleVec2(8, 24),
                new SimpleVec2(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN),
                -22.5f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(662, 662),
                new SimpleVec2(16, 16),
                new SimpleVec2(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN),
                -45.0f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(1008, 336),
                new SimpleVec2(24, 8),
                new SimpleVec2(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN),
                -67.5f,
                true
            ),
            // ---------------------------------------------------------------
            new BeamData(
                new SimpleVec2(640, (720 - 606)),
                new SimpleVec2(934, 0),
                new SimpleVec2(24, 0),
                new SimpleVec2(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL),
                -90.0f,
                true
            ),
        };

    private final Color[] colours = new Color[]
        {
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.GREEN,
            Color.CYAN,
            Color.MAROON,
        };

    private Sprite[]     beamSprites;
    private SimpleVec2[] directions;
    private SimpleVec2[]  distances;
    private SimpleVec2F[] positions;

    private       int[] colorIndex;

    public TeleportBeam()
    {
    }

    public void entryVisual()
    {
        initialise(true);
    }

    public void exitVisual()
    {
        initialise(false);
    }

    private void initialise(boolean _enteredTeleporter)
    {
        beamSprites = new Sprite[beams.length];
        directions  = new SimpleVec2[beams.length];
        distances   = new SimpleVec2[beams.length];
        positions   = new SimpleVec2F[beams.length];
        colorIndex  = new int[beams.length];

        TextureRegion textureRegion = App.assets.getAnimationRegion(GameAssets._TRANSPORTER_BEAM_ASSET);

        int frameWidth  = textureRegion.getRegionWidth();
        int frameHeight = textureRegion.getRegionHeight();

        float originX = (App.baseRenderer.spriteGameCamera.camera.position.x - Gfx._VIEW_HALF_WIDTH);
        float originY = (App.baseRenderer.spriteGameCamera.camera.position.y - Gfx._VIEW_HALF_HEIGHT);

        for (int i = 0; i < beams.length; i++)
        {
            beamSprites[i] = new Sprite();

            beamSprites[i].setRegion(textureRegion);
            beamSprites[i].setRotation(beams[i].angle);

            directions[i] = new SimpleVec2(beams[i].direction);
            distances[i]  = new SimpleVec2(beams[i].distance);

            int startXPos = (int) (App.getPlayer().sprite.getX() - originX);

            positions[i] = new SimpleVec2F
                (
                    originX + startXPos,
                    originY + beams[i].startPos.getY()
                );

            if (!_enteredTeleporter)
            {
                positions[i].addX((beams[i].distance.getX() * (directions[i].getX() * -1)));
                positions[i].addY((beams[i].distance.getY() * (directions[i].getY() * -1)));
            }
            else
            {
                directions[i].x *= -1;
                directions[i].y *= -1;
            }

            beamSprites[i].setPosition(positions[i].getX(), positions[i].getY());

            beamSprites[i].setSize(frameWidth, frameHeight);
            beamSprites[i].setBounds(positions[i].getX(), positions[i].getY(), frameWidth, frameHeight);
            beamSprites[i].setOrigin((float) frameWidth / 2, 0);
            beamSprites[i].setColor(colours[MathUtils.random(colours.length - 1)]);

            colorIndex[i] = i;
        }
    }

    public boolean update()
    {
        int completedBeams = 0;

        for (int i = 0; i < beams.length; i++)
        {
            beamSprites[i].setColor(colours[colorIndex[i]]);

            if (++colorIndex[i] >= colours.length)
            {
                colorIndex[i] = 0;
            }

            if ((distances[i].getX() > 0) || (distances[i].getY() > 0))
            {
                beamSprites[i].translate
                    (
                        beams[i].speed.getX() * directions[i].getX(),
                        beams[i].speed.getY() * directions[i].getY()
                    );

                distances[i].subX(beams[i].speed.getX());
                distances[i].subY(beams[i].speed.getY());
            }
            else
            {
                completedBeams++;
            }
        }

        return (completedBeams >= beams.length);
    }

    public void draw(SpriteBatch spriteBatch)
    {
        for (int i = 0; i < beams.length; i++)
        {
            if (beams[i].isActive)
            {
                beamSprites[i].draw(spriteBatch);
            }
        }
    }

    @Override
    public void dispose()
    {
        for (int i = 0; i < beams.length; i++)
        {
            beamSprites[i] = null;
        }

        beamSprites = null;
        directions  = null;
        distances   = null;
        positions   = null;
    }
}

