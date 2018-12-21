package io.sparkled.model.animation.param

enum class ParamType {

    /**
     * Indicates that no type has been provided.
     */
    NONE,

    /**
     * A single color value.
     */
    COLOR,

    /**
     * Multiple color values.
     */
    COLORS,

    /**
     * A pair of (x, y) coordinates for a cubic bezier curve's control points, stored as an array of floats.
     */
    CUBIC_BEZIER,

    /**
     * A number, either whole or fractional.
     */
    DECIMAL,

    /**
     * A whole number.
     */
    INTEGER
}