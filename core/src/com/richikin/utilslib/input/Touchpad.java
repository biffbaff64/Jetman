
package com.richikin.utilslib.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.richikin.utilslib.logging.Trace;

/**
 * An on-screen joystick. The movement area of the joystick is circular,
 * centered on the touchpad, and its size determined by the smaller
 * touchpad dimension.
 * <p>
 * The preferred size of the touchpad is determined by the background.
 * <p>
 * {@link ChangeListener.ChangeEvent} is fired when the touchpad knob
 * is moved. Cancelling the event will move the knob to where it was
 * previously.
 */
public class Touchpad extends Widget
{
    private static final float DEFAULT_DEADZONE_RADIUS = 10.0f;

    private final Circle  knobBounds     = new Circle(0, 0, 0);
    private final Circle  touchBounds    = new Circle(0, 0, 0);
    private final Circle  deadzoneBounds = new Circle(0, 0, 0);
    private final Vector2 knobPosition   = new Vector2();
    private final Vector2 knobPercent    = new Vector2();
    private final Vector2 padCentre      = new Vector2();

    private TouchpadStyle style;
    private float         deadzoneRadius;

    private boolean touched;
    private boolean resetOnTouchUp = false;

    /**
     * @param _deadzoneRadius The distance in pixels from the center of the
     *                       touchpad required for the knob to be moved.
     */
    public Touchpad(float _deadzoneRadius, Skin skin)
    {
        this(_deadzoneRadius, skin.get(TouchpadStyle.class));
    }

    /**
     * @param _deadzoneRadius The distance in pixels from the center of the
     *                       touchpad required for the knob to be moved.
     */
    public Touchpad(float _deadzoneRadius, Skin skin, String styleName)
    {
        this(_deadzoneRadius, skin.get(styleName, TouchpadStyle.class));
    }

    /**
     * @param _deadzoneRadius The distance in pixels from the center of the
     *                       touchpad required for the knob to be moved.
     */
    public Touchpad(float _deadzoneRadius, TouchpadStyle _style)
    {
        if (_deadzoneRadius < 0)
        {
            if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            {
                throw new IllegalArgumentException("deadzoneRadius must be > 0");
            }
            else
            {
                _deadzoneRadius = DEFAULT_DEADZONE_RADIUS;
            }
        }

        this.deadzoneRadius = _deadzoneRadius;

        knobPosition.set(getWidth() / 2f, getHeight() / 2f);

        Trace.__FILE_FUNC("knobPosition: " + knobPosition.toString());

        setStyle(_style);
        setSize(getPrefWidth(), getPrefHeight());

        addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                if (touched)
                {
                    return false;
                }

                touched = true;
                calculatePositionAndValue(x, y, false);

                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer)
            {
                calculatePositionAndValue(x, y, false);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                touched = false;
                calculatePositionAndValue(x, y, resetOnTouchUp);
            }
        });
    }

    public Vector2 getKnobPosition()
    {
        return knobPosition;
    }

    private void calculatePositionAndValue(float x, float y, boolean isTouchUp)
    {
        float oldPositionX = knobPosition.x;
        float oldPositionY = knobPosition.y;
        float oldPercentX  = knobPercent.x;
        float oldPercentY  = knobPercent.y;
        float centerX      = knobBounds.x;
        float centerY      = knobBounds.y;

        knobPosition.set(centerX, centerY);
        knobPercent.set(0f, 0f);

        if (!isTouchUp)
        {
            if (!deadzoneBounds.contains(x, y))
            {
                knobPercent.set((x - centerX) / knobBounds.radius, (y - centerY) / knobBounds.radius);

                float length = knobPercent.len();

                if (length > 1)
                {
                    knobPercent.scl(1 / length);
                }

                if (knobBounds.contains(x, y))
                {
                    knobPosition.set(x, y);
                }
                else
                {
                    knobPosition.set(knobPercent).nor().scl(knobBounds.radius).add(knobBounds.x, knobBounds.y);
                }
            }
        }

        if ((oldPercentX != knobPercent.x) || (oldPercentY != knobPercent.y))
        {
            ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);

            if (fire(changeEvent))
            {
                knobPercent.set(oldPercentX, oldPercentY);
                knobPosition.set(oldPositionX, oldPositionY);
            }

            Pools.free(changeEvent);
        }
    }

    public void setKnobPosition(float x, float y)
    {
        calculatePositionAndValue(x, y, false);
    }

    public void setStyle(TouchpadStyle _style)
    {
        if (_style == null)
        {
            if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            {
                throw new IllegalArgumentException("style cannot be null");
            }
            else
            {
                _style = new TouchpadStyle();
            }
        }

        this.style = _style;

        invalidateHierarchy();
    }

    /**
     * Returns the touchpad's style. Modifying the returned style may not
     * have an effect until {@link #setStyle(TouchpadStyle)} is called.
     */
    public TouchpadStyle getStyle()
    {
        return style;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable)
    {
        if (touchable && (this.getTouchable() != Touchable.enabled))
        {
            return null;
        }

        return touchBounds.contains(x, y) ? this : null;
    }

    @Override
    public void layout()
    {
        // Recalculate pad and deadzone bounds
        float halfWidth  = getWidth() / 2;
        float halfHeight = getHeight() / 2;
        float radius     = Math.min(halfWidth, halfHeight);

        touchBounds.set(halfWidth, halfHeight, radius);

        if (style.knob != null)
        {
            radius -= Math.max(style.knob.getMinWidth(), style.knob.getMinHeight()) / 2;
        }

        knobBounds.set(halfWidth, halfHeight, radius);
        deadzoneBounds.set(halfWidth, halfHeight, deadzoneRadius);

        // Recalculate pad values and knob position
        knobPosition.set(halfWidth, halfHeight);
        knobPercent.set(0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        validate();

        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);

        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();

        final Drawable bg = style.background;

        if (bg != null)
        {
            bg.draw(batch, x, y, w, h);
        }

        final Drawable knob = style.knob;

        if (knob != null)
        {
            x += knobPosition.x - (knob.getMinWidth() / 2f);
            y += knobPosition.y - (knob.getMinHeight() / 2f);

            knob.draw(batch, x, y, knob.getMinWidth(), knob.getMinHeight());
        }
    }

    @Override
    public float getPrefWidth()
    {
        return (style.background != null) ? style.background.getMinWidth() : 0;
    }

    @Override
    public float getPrefHeight()
    {
        return (style.background != null) ? style.background.getMinHeight() : 0;
    }

    public Vector2 getPadCentre()
    {
        return new Vector2
            (
                getX() + (getWidth() / 2),
                getY() + (getHeight() / 2)
            );
    }

    public boolean isTouched()
    {
        return touched;
    }

    public boolean getResetOnTouchUp()
    {
        return resetOnTouchUp;
    }

    /**
     * @param reset Whether to reset the knob to the center on touch up.
     */
    public void setResetOnTouchUp(boolean reset)
    {
        this.resetOnTouchUp = reset;
    }

    /**
     * @param _deadzoneRadius The distance in pixels from the center of the
     *                       touchpad required for the knob to be moved.
     */
    public void setDeadzone(float _deadzoneRadius)
    {
        if (_deadzoneRadius < 0)
        {
            //
            // NB:  Previous version registered an illegal argument exception here.
            //      I have chosen to remove this on non-Desktop builds, and replace it
            //      with setting deadzoneRadius to a default value. This is to remove
            //      the possibility of an app crash.
            //      The exception will still occur on Desktop builds.

            if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            {
                throw new IllegalArgumentException("deadzoneRadius must be > 0");
            }
            else
            {
                _deadzoneRadius = DEFAULT_DEADZONE_RADIUS;
            }
        }

        this.deadzoneRadius = _deadzoneRadius;
        invalidate();
    }

    /**
     * Returns the x-position of the knob relative to the center of the widget. The positive direction is right.
     */
    public float getKnobX()
    {
        return knobPosition.x;
    }

    /**
     * Returns the y-position of the knob relative to the center of the widget. The positive direction is up.
     */
    public float getKnobY()
    {
        return knobPosition.y;
    }

    /**
     * Returns the x-position of the knob as a percentage from the center of the touchpad to the edge of the circular movement
     * area. The positive direction is right.
     */
    public float getKnobPercentX()
    {
        return knobPercent.x;
    }

    /**
     * Returns the y-position of the knob as a percentage from the center of the touchpad to the edge of the circular movement
     * area. The positive direction is up.
     */
    public float getKnobPercentY()
    {
        return knobPercent.y;
    }

    public Circle getKnobBounds()
    {
        return knobBounds;
    }

    /**
     * The style for a {@link Touchpad}.
     *
     * @author Josh Street
     */
    public static class TouchpadStyle
    {
        /**
         * Stretched in both directions. Optional.
         */
        public Drawable background;

        /**
         * Optional.
         */
        public Drawable knob;

        public TouchpadStyle()
        {
        }

        public TouchpadStyle(Drawable background, Drawable knob)
        {
            this.background = background;
            this.knob = knob;
        }

        public TouchpadStyle(TouchpadStyle style)
        {
            this.background = style.background;
            this.knob = style.knob;
        }
    }
}
