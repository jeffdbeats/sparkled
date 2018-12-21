package io.sparkled.model.animation.easing.reference

import io.sparkled.model.animation.easing.EasingType
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.Param
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.util.ParamUtils.param
import java.util.Arrays

object EasingTypes {

    val LINEAR_CUBIC_BEZIER = floatArrayOf(0f, 0f, 1f, 1f)

    private val TYPES = Arrays.asList(
        easingType(EasingTypeCode.CUBIC_BEZIER, param(ParamCode.CONTROL_POINTS, ParamType.CUBIC_BEZIER, *LINEAR_CUBIC_BEZIER.toTypedArray())),
        easingType(EasingTypeCode.EXPO_OUT),
        easingType(EasingTypeCode.LINEAR)
    )

    fun get(): List<EasingType> {
        return TYPES
    }

    private fun easingType(code: EasingTypeCode, vararg params: Param): EasingType {
        return EasingType(code, code.displayName, listOf(*params))
    }
}