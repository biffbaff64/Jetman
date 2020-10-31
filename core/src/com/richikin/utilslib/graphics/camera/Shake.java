
package com.richikin.utilslib.graphics.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class Shake
{
    public static boolean screenShakeEnabled;

    private static float shakeRadius;
    private static float randomAngle;
    private static float elapsedTime;
    private static float shakeDuration;
    private static float shakeIntensity;
    private static boolean screenShakeAllowed;

    public static void start()
    {
        start(30, 450, 10);
    }

    public static void start(float radius, float duration, float intensity)
    {
        if (screenShakeAllowed)
        {
            if (!screenShakeEnabled)
            {
                shakeDuration = duration / 1000f;
                shakeRadius = radius;
                shakeIntensity = intensity;

                elapsedTime = 0;
                randomAngle = MathUtils.random.nextFloat() % 360f;
                screenShakeEnabled = true;
            }
        }
    }

    public static void update(float delta, OrthographicCamera camera)
    {
        if (!screenShakeAllowed)
        {
            reset();
        }
        else
        {
            if(screenShakeEnabled)
            {
                // Only shake when required.
                if (elapsedTime < shakeDuration)
                {
                    // Calculate the amount of shake based on how long it has been shaking already
                    float currentPower = shakeIntensity * camera.zoom * ((shakeDuration - elapsedTime) / shakeDuration);

                    float x = (MathUtils.random.nextFloat() - 0.5f) * currentPower;
                    float y = (MathUtils.random.nextFloat() - 0.5f) * currentPower;

                    camera.translate(-x, -y);

                    // Increase the elapsedTime time by the delta provided.
                    elapsedTime += delta;
                }
                else
                {
                    reset();
                }
            }
        }
    }

    public static void reset()
    {
        screenShakeEnabled = false;
    }

    public static void setAllowed(boolean allowed)
    {
        screenShakeAllowed = allowed;
    }
}
