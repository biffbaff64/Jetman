/*
 *  Copyright 01/05/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.richikin.jetman.entities.characters;

import com.richikin.jetman.core.App;
import com.richikin.enumslib.GraphicID;

public class StarSpinner extends Bouncer
{
    public StarSpinner(App _app)
    {
        super(GraphicID.G_STAR_SPINNER, _app);
    }

//    @Override
//    public void initialise(SpriteDescriptor entityDescriptor)
//    {
//        create(entityDescriptor);
//
//        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);
//
//        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
//        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
//
//        distance.set(Gfx._VIEW_WIDTH * 6, 0);
//        speed.set(1 + MathUtils.random(2), 0);
//
//        if (sprite.getX() < app.getPlayer().sprite.getX())
//        {
//            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
//        }
//        else
//        {
//            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
//        }
//
//        setAction(Actions._RUNNING);
//
//        isRotating = true;
//        rotateSpeed = 4.0f;
//    }
//
//    @Override
//    public void update(int spriteNum)
//    {
//        switch (getAction())
//        {
//            case _RUNNING:
//            {
//                move();
//            }
//            break;
//
//            case _KILLED:
//            case _HURT:
//            {
//                ExplosionManager explosionManager = new ExplosionManager();
//                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this, app);
//
//                if (getAction() == Actions._KILLED)
//                {
//                    app.gameProgress.score.add(PointsManager.getPoints(gid));
//                }
//
//                setAction(Actions._EXPLODING);
//            }
//            break;
//
//            case _EXPLODING:
//            {
//            }
//            break;
//
//            case _DYING:
//            {
//                setAction(Actions._DEAD);
//            }
//            break;
//
//            default:
//            {
//                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
//            }
//            break;
//        }
//
//        animate();
//
//        updateCommon();
//    }
//
//    @Override
//    public void animate()
//    {
//        elapsedAnimTime += Gdx.graphics.getDeltaTime();
//        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
//    }
//
//    private void move()
//    {
//        sprite.translate((speed.getX() * direction.getX()), 0);
//
//        wrap();
//    }
}
