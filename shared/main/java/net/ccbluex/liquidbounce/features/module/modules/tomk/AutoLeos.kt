package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.script.api.global.Chat
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.TextValue
import java.util.*

@ModuleInfo(name = "Escape", description = "", category = ModuleCategory.TOMK)
class AutoLeos : Module() {
    private val healths = FloatValue("Health", 5f, 1f, 20f)
    private val keepArmor = BoolValue("KeepArmor",false)
    private val message = BoolValue("Message",false)
    private val messages = TextValue("Messages","[LiquidBounce]")
    init {
        state = true
    }
    var wating = true
    var wating2 = true
    val timer = Timer()

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer!!.health <= healths.get()) {
            if (keepArmor.get()) {
                for (i in 0..3) {
                    val armorSlot = 3 - i
                    move(8 - armorSlot, true)
                }

                if (mc.thePlayer!!.unwrap().totalArmorValue < 4 && wating2) {
                    mc.thePlayer!!.sendChatMessage("/hub")
                    wating2 = false
                }
            } else {
                if (wating2) {
                    mc.thePlayer!!.sendChatMessage("/hub")
                    wating2 = false
                }
            }
            if (message.get() && wating) {
                mc.thePlayer!!.sendChatMessage(messages.get())
                wating = false
            }

            LiquidBounce.moduleManager[KillAura::class.java].state = false
            LiquidBounce.moduleManager[Velocity::class.java].state = false
        }
    }

    private fun move(item: Int, isArmorSlot: Boolean) {
        if (item != -1) {
            val openInventory = !classProvider.isGuiInventory(mc.currentScreen)
            if (openInventory) {
                classProvider.createCPacketEntityAction(
                        mc.thePlayer!!,
                        ICPacketEntityAction.WAction.OPEN_INVENTORY
                )
            }

            mc.playerController.windowClick(
                    mc.thePlayer!!.inventoryContainer.windowId,
                    if (isArmorSlot) item else if (item < 9) item + 36 else item,
                    0,
                    1,
                    mc.thePlayer!!
            )

            if (openInventory) {
                mc.netHandler.addToSendQueue(classProvider.createCPacketCloseWindow())
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {
        wating = true
        wating2 = true
    }

    override val tag: String?
        get() = "Health"+" "+healths.get()
}