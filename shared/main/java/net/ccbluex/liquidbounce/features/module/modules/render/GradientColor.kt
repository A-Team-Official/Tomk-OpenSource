package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue

@ModuleInfo(name = "GradientColor", description = "Custom your Target and Text GidentColor", category = ModuleCategory.RENDER, canEnable = true,array = false)
object GradientColor: Module(){
    val colorModeValue = ListValue("ColorStyle", arrayOf( "Rainbow","ColorMixer"), "ColorMixer")
    val colormode = ListValue("MixerChange", arrayOf("Blue&Purple","Purple&Gold","PurpleBlue","Flux","Blue&Gold","Tenacity","Orange","Custom"),"Custom")
    val gradientAmountValue = IntegerValue("Gradient-Amount", 30, 1, 50)
    val SecValue = IntegerValue("Seconds", 2, 1, 10)
    val distanceValue = IntegerValue("Line-Distance", 100, 0, 200)
    val RainbowDistance =  IntegerValue("Rainbow-Distance", 10, 1, 90)
    val Alpha = IntegerValue("Text-Alpha",20,0,255)
    val Alpha1 = IntegerValue("SessionInfo-Alpha",20,0,255)
    val Alpha2 = IntegerValue("Scoreboard-Alpha",60,0,255)
    val Alpha3 = IntegerValue("KeyStateShow-Alpha",40,0,255)
    val speed = IntegerValue("HotbarSpeed", 0, 0, 400)
    init {
        state = true
    }
}