package net.chrisparton.sparkled.renderer.easing;

import net.chrisparton.sparkled.model.animation.easing.EasingTypeCode;
import net.chrisparton.sparkled.renderer.easing.function.ConstantEasing;
import net.chrisparton.sparkled.renderer.easing.function.LinearEasing;

import java.util.HashMap;
import java.util.Map;

public class EasingFunctions {

    private static Map<EasingTypeCode, EasingFunction> functions = new HashMap<>();

    static {
        functions.put(EasingTypeCode.LINEAR, new LinearEasing());
        functions.put(EasingTypeCode.CONSTANT, new ConstantEasing());
    }

    public static EasingFunction get(EasingTypeCode easingType) {
        return functions.get(easingType);
    }
}