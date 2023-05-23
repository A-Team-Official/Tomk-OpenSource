package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.FloatValue

@ModuleInfo(name = "AutoBlock", description = "Tomk",category = ModuleCategory.COMBAT)
class AutoBlock : Module() {
    private var blockrange = FloatValue("BlockRange", 5f, 0f, 8f)
    private var blocking = false

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val aura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        if (aura.target != null) {
            if (mc.thePlayer!!.getDistanceToEntity(aura.target!!) <= blockrange.get()) {
                mc.gameSettings.keyBindUseItem.pressed = true
                blocking = true
            }
        } else if (blocking) {
            mc.gameSettings.keyBindUseItem.pressed = false
            blocking = false
        }
    }
}