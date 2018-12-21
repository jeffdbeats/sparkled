package io.sparkled.renderer.easing.function.bezier

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.util.ArgumentUtils.arg
import io.sparkled.util.matchers.SparkledMatchers.equalsFloatArray
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class CubicBezierEasingTest {

    @Test
    fun can_ease_0_to_100() {
        val easingFunction = CubicBezierEasing()
        val easing = Easing(
            EasingTypeCode.CUBIC_BEZIER, 0f, 100f, mapOf(
                arg(ParamCode.CONTROL_POINTS, 0f, 0f, 1f, 1f)
            )
        )

        val expected = floatArrayOf(0f, .1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f, 1f)
        val testCount = expected.size

        val actual = (0 until testCount).map { easingFunction.getProgress(easing, it, testCount) }.toFloatArray()
        assertThat(actual, equalsFloatArray(expected))
    }
}
