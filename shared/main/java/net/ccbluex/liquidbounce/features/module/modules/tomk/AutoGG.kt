package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.player.InventoryCleaner
import net.ccbluex.liquidbounce.features.module.modules.world.ChestStealer
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.SPacketChat
import tomk.Recorder.totalPlayed
import tomk.Recorder.win

@ModuleInfo(name = "AutoGG", category = ModuleCategory.TOMK, description = "Tomk")
class AutoGG : Module() {
    var fadeState = FadeState.NO
    private val mode = ListValue("GG-Mode", arrayOf("HYT4V4","HYTSKY"),"HYT4V4")
    private val textValue = TextValue("Text", "GG")
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()

        if (packet is SPacketChat) {
            val text = packet.chatComponent.unformattedText
            when (mode.get()) {
                "HYT4V4" -> {
                    if (text.contains("恭喜", true)) {
                        mc.thePlayer!!.sendChatMessage(textValue.get())
                        win++
                        fadeState = FadeState.FRIST


                    }
                    if (text.contains("游戏开始", true)) {
                        totalPlayed++
                    }
                }
                "HYTSKY"->{
                    if (text.contains("你现在是观察者状态. 按E打开菜单.", true)) {
                        mc.thePlayer!!.sendChatMessage(textValue.get())
                        win++
                        fadeState = FadeState.FRIST
                        LiquidBounce.moduleManager[ChestStealer::class.java].state = false
                        LiquidBounce.moduleManager[InventoryCleaner::class.java].state = false

                    }
                    if (text.contains("开始倒计时:", true)) {
                        totalPlayed++
                        LiquidBounce.moduleManager[ChestStealer::class.java].state = true
                        LiquidBounce.moduleManager[InventoryCleaner::class.java].state = true
                    }
                }
            }
        }
    }
    override val tag: String
        get() = "HuaYuTing"
}
enum class FadeState { FRIST,IN, STAY, OUT, END,NO }
