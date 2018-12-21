package io.sparkled.renderer.util

import io.sparkled.model.animation.easing.reference.EasingTypes.LINEAR_CUBIC_BEZIER
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode
import java.awt.Color

object ParamUtils {

    fun getIntegerValue(parent: HasArguments, paramCode: ParamCode, default: Int = 0): Int {
        return getDecimalValue(parent, paramCode, default.toFloat()).toInt()
    }

    fun getDecimalValue(parent: HasArguments, paramCode: ParamCode, default: Float = 0f): Float {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) default else values[0].toFloat()
    }

    fun getColorValue(parent: HasArguments, paramCode: ParamCode, default: Color = Color.BLACK): Color {
        return getColorsValue(parent, paramCode, default)[0]
    }

    fun getColorsValue(parent: HasArguments, paramCode: ParamCode, default: Color = Color.BLACK): Array<Color> {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) {
            arrayOf(default)
        } else {
            values.map { convertColor(it) }.toTypedArray()
        }
    }

    fun getCubicBezierValue(
        parent: HasArguments,
        paramCode: ParamCode,
        default: FloatArray = LINEAR_CUBIC_BEZIER
    ): FloatArray {
        val values = getArgumentValues(parent, paramCode)
        return if (values.isEmpty()) {
            default
        } else {
            values.map { it.toFloat() }.toFloatArray()
        }
    }

    private fun getArgumentValues(parent: HasArguments, paramCode: ParamCode): List<String> {
        return parent.getArguments()[paramCode] ?: emptyList()
    }

    private fun convertColor(hexColor: String): Color {
        return Color.decode(hexColor.toLowerCase())
    }
}
