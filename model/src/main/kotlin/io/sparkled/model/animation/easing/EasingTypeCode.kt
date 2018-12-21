package io.sparkled.model.animation.easing

enum class EasingTypeCode(val displayName: String) {

    /**
     * Indicates that no type has been provided.
     */
    NONE("None"),

    /**
     * A custom cubic bezier curve.
     */
    CUBIC_BEZIER("Cubic Bezier"),

    /**
     * An easing with very fast acceleration.
     */
    EXPO_OUT("Exponential (Out)"),

    /**
     * A simple, linear easing.
     */
    LINEAR("Linear")
}