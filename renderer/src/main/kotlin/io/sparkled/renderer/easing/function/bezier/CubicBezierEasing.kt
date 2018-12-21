package io.sparkled.renderer.easing.function.bezier

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.renderer.easing.EasingFunction
import io.sparkled.renderer.easing.function.LinearEasing
import io.sparkled.renderer.util.ParamUtils

/**
 * An exponential easing function.
 */
class CubicBezierEasing : EasingFunction() {

    override fun getUnscaledProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        val controlPoints = ParamUtils.getCubicBezierValue(easing, ParamCode.CONTROL_POINTS)
        val cubicBezier = CubicBezier(controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3])

        val linearProgress = LinearEasing().getProgress(easing, currentFrame, frameCount)
        return cubicBezier.getValueAt(linearProgress)
    }
}