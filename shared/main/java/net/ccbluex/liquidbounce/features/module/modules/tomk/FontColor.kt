package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue


/**
 *
 * by Lynn.
 * @Date 2022/10/6
 */
@ModuleInfo(name="FontColor", description = "FontColor",category = ModuleCategory.TOMK, canEnable = false)
class FontColor : Module(){

    companion object {
        @JvmField
        val a = IntegerValue("A", 255, 0, 255)
        @JvmField
        val r = IntegerValue("Red", 255, 0, 255)
        @JvmField
        val g = IntegerValue("Green", 255, 0, 255)
        @JvmField
        val b = IntegerValue("Blue", 255, 0, 255)
        @JvmField
        val r2= IntegerValue("Red2", 255, 0, 255)
        @JvmField
        val g2= IntegerValue("Green2", 255, 0, 255)
        @JvmField
        val b2 = IntegerValue("Blue2", 255, 0, 255)
        @JvmField
        val r3= IntegerValue("Red3", 255, 0, 255)
        @JvmField
        val g3= IntegerValue("Green3", 255, 0, 255)
        @JvmField
        val b3 = IntegerValue("Blue3", 255, 0, 255)
        @JvmField
        val r4= IntegerValue("Red4", 255, 0, 255)
        @JvmField
        val g4= IntegerValue("Green4", 255, 0, 255)
        @JvmField
        val b4 = IntegerValue("Blue4", 255, 0, 255)
    }
}