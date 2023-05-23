package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "KillFix", description = "Tomk",category = ModuleCategory.COMBAT)
class KillFix : Module() {
    private val hurttime: IntegerValue =  IntegerValue("hurttime", 9, 1, 10)
    private val hurttime2: IntegerValue = IntegerValue("hurttime2", 10, 1, 10)
    private val AirRange: FloatValue = FloatValue("AirRange", 3f, 0f, 5f)
    private val GroundRange: FloatValue = FloatValue("GroundRange", 3.5f, 0f, 5f)
    private val Debug= BoolValue("Debug", true)
    var ticks=0
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer!!.isAirBorne){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.rangeValue.set(AirRange.get())
            killAura.BlockRangeValue.set(AirRange.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka range "+AirRange.value.toString())            }

        }
        if (mc.thePlayer!!.onGround){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.rangeValue.set(GroundRange.get())
            killAura.BlockRangeValue.set(GroundRange.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka range "+GroundRange.value.toString())
            }
        }
        ticks++
        if (ticks ==1){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.hurtTimeValue.set(hurttime.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime "+hurttime.value.toString())
            }
        }
        if (ticks ==2){
            val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
            killAura.hurtTimeValue.set(hurttime2.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime "+hurttime2.value.toString())
            }
        }
        if (ticks ==3){
            ticks=0
        }
        if (mc.thePlayer!!.isAirBorne){
            val Aura = LiquidBounce.moduleManager[Aura::class.java] as Aura
            Aura.rangeValue.set(AirRange.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka range "+AirRange.value.toString())            }

        }
        if (mc.thePlayer!!.onGround){
            val Aura = LiquidBounce.moduleManager[Aura::class.java] as Aura
            Aura.rangeValue.set(GroundRange.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka range "+GroundRange.value.toString())
            }
        }
        ticks++
        if (ticks ==1){
            val Aura = LiquidBounce.moduleManager[Aura::class.java] as Aura
            Aura.hurtTimeValue.set(hurttime.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime "+hurttime.value.toString())
            }
        }
        if (ticks ==2){
            val Aura = LiquidBounce.moduleManager[Aura::class.java] as Aura
            Aura.hurtTimeValue.set(hurttime2.get())
            if(Debug.get()){
                ClientUtils.displayChatMessage("Kafix -> set ka hurttime "+hurttime2.value.toString())
            }
        }
        if (ticks ==3){
            ticks=0
        }
    }




}
