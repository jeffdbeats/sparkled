package io.sparkled.renderer.easing.function

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.param.ParamName
import io.sparkled.renderer.easing.EasingFunction
import io.sparkled.renderer.util.ParamUtils

class ConstantEasing : EasingFunction {

    override fun getProgress(easing: Easing, currentFrame: Int, frameCount: Int): Float {
        return ParamUtils.getDecimalValue(easing, ParamName.PERCENT) / 100f
    }
}
