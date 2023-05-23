package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.TextValue

@ModuleInfo(name = "Title", category = ModuleCategory.TOMK, description = "Tomk")
class Title : Module(){
    private val fakeNameValue = TextValue("SetTitle", "Liquidbounce")
    private val CopywritingValue = TextValue("Copywriting", "Vic-1")

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        org.lwjgl.opengl.Display.setTitle(fakeNameValue.get()+ "   "+CopywritingValue.get())
    }
}