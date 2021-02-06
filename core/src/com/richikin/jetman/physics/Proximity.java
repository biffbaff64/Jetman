/*
 * *****************************************************************************
 *    Copyright 27/03/2017 See AUTHORS file.
 *    <p>
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    <p>
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ***************************************************************************
 *
 */

package com.richikin.jetman.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.richikin.jetman.entities.objects.GdxSprite;

public class Proximity
{
    // TODO: 06/02/2021 - Why are these not local to methods?
    private final Rectangle proximityArea;
    private final Vector2   tmp1;
    private final Vector2   tmp2;
    private final Vector2   start;
    private final Vector2   stop;

    private boolean isSilent;

    public Proximity()
    {
        this.proximityArea = new Rectangle();
        this.tmp1 = new Vector2();
        this.tmp2 = new Vector2();
        this.start = new Vector2();
        this.stop = new Vector2();

        this.isSilent = false;
    }

    public boolean isNear(final GdxSprite parent, final GdxSprite target)
    {
        boolean near = false;

        proximityArea.set(parent.sprite.getBoundingRectangle());

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        proximityArea.setWidth(proximityArea.getWidth() * 3);
        proximityArea.setHeight(proximityArea.getHeight() * 2);

        if (proximityArea.overlaps(target.sprite.getBoundingRectangle()))
        {
            near = true;
        }

        return near;
    }

    public boolean isNearX(final GdxSprite parent, final GdxSprite target)
    {
        boolean near = false;

        proximityArea.set(parent.sprite.getBoundingRectangle());

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        proximityArea.setWidth(proximityArea.getWidth() * 3);
        // proximityArea.height stays the same

        if (proximityArea.overlaps(target.sprite.getBoundingRectangle()))
        {
            near = true;
        }

        return near;
    }

    public boolean isNearY(final GdxSprite parent, final GdxSprite target)
    {
        boolean near = false;

        proximityArea.set(parent.sprite.getBoundingRectangle());

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        // proximityArea.width stays the same
        proximityArea.setHeight(proximityArea.getHeight() * 2);

        if (proximityArea.overlaps(target.sprite.getBoundingRectangle()))
        {
            near = true;
        }

        return near;
    }

    public boolean isRoughlyAbove(final GdxSprite parent, final GdxSprite target)
    {
        boolean above = false;

        proximityArea.set(parent.sprite.getBoundingRectangle());

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        // proximityArea.width stays the same
        proximityArea.setHeight(proximityArea.getHeight() * 2);

        Rectangle targetRectangle = target.sprite.getBoundingRectangle();

        targetRectangle.y = proximityArea.y;

        if (proximityArea.overlaps(targetRectangle))
        {
            above = true;
        }

        return above;
    }

    public boolean isVeryClose(final GdxSprite parent, final GdxSprite target)
    {
        boolean near = false;

        proximityArea.set(parent.sprite.getBoundingRectangle());

        proximityArea.x -= (proximityArea.getWidth() / 2);
        proximityArea.y += (proximityArea.getHeight() / 2);
        proximityArea.setWidth(proximityArea.getWidth() * 2);
        proximityArea.setHeight(proximityArea.getHeight() * 2);

        if (proximityArea.overlaps(target.sprite.getBoundingRectangle()))
        {
            near = true;
        }

        return near;
    }

    public boolean isInLineOfSight(final GdxSprite ent1, final GdxSprite ent2, final int step)
    {
        return isInLineOfSight(ent1, ent2, step, 1000);
    }

    public boolean isInLineOfSight(final GdxSprite ent1, final GdxSprite ent2, final int step, final float maxDist)
    {
        start.set(ent1.sprite.getX(), ent1.sprite.getY());
        stop.set(ent2.sprite.getX(), ent2.sprite.getY());

        float dist = 0;

        tmp1.set(start);

        tmp2.x = stop.x - start.x;
        tmp2.y = stop.y - start.y;

        tmp2.nor().scl(step);

        boolean processing = true;
        boolean retFlag = false;

        while (processing)
        {
            tmp1.x += tmp2.x;
            tmp1.y += tmp2.y;

            if (Vector2.dst2(tmp1.x, tmp1.y, stop.x, stop.y) < (step * step * 1.2f))
            {
                retFlag = true;
                processing = false;
            }

            if (dist > maxDist)
            {
                retFlag = false;
                processing = false;
            }

            dist += step;
        }

        return retFlag;
    }

    public boolean isSilent()
    {
        return isSilent;
    }

    public void setSilent(final boolean silent)
    {
        isSilent = silent;
    }
}
