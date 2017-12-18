package net.chrisparton.sparkled.renderer.easing.function;

import net.chrisparton.sparkled.model.animation.easing.Easing;
import net.chrisparton.sparkled.renderer.easing.EasingFunction;

/**
 * An easing function that interpolates uniformly between 0 and 1.
 */
public class LinearEasing implements EasingFunction {
    @Override
    public float getProgress(Easing easing, float currentFrame, float durationFrames) {
        return currentFrame / (durationFrames - 1);
    }
}