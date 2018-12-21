package io.sparkled.renderer.easing.function.bezier

import io.sparkled.model.util.MathUtils.constrain

/**
 * Based on https://github.com/codesoup/android-cubic-bezier-interpolator.
 */
class CubicBezier
constructor(
    private val startX: Float,
    private val startY: Float,
    private val endX: Float,
    private val endY: Float
) {
    private var a = Point(0f, 0f)
    private var b = Point(0f, 0f)
    private var c = Point(0f, 0f)

    init {
        if (startX < 0 || startX > 1) {
            throw IllegalArgumentException("startX value must be in the range [0, 1]")
        } else if (endX < 0 || endX > 1) {
            throw IllegalArgumentException("endX value must be in the range [0, 1]")
        }
    }

    fun getValueAt(time: Float): Float {
        return getBezierCoordinateY(getXForTime(time))
    }

    private fun getBezierCoordinateY(time: Float): Float {
        c.y = 3 * startY
        b.y = 3 * (endY - startY) - c.y
        a.y = 1 - c.y - b.y
        return time * (c.y + time * (b.y + time * a.y))
    }

    private fun getXForTime(time: Float): Float {
        var x = time
        var z: Float

        for (i in 1..13) {
            z = getBezierCoordinateX(x) - time
            if (Math.abs(z) < 1e-3) {
                break
            }
            x -= z / getXDerivative(x)
        }

        return constrain(x, 0f, 1f)
    }

    private fun getBezierCoordinateX(time: Float): Float {
        c.x = 3 * startX
        b.x = 3 * (endX - startX) - c.x
        a.x = 1 - c.x - b.x

        return time * (c.x + time * (b.x + time * a.x))
    }

    private fun getXDerivative(t: Float): Float {
        return c.x + t * (2 * b.x + 3 * a.x * t)
    }

    private data class Point(var x: Float, var y: Float)
}