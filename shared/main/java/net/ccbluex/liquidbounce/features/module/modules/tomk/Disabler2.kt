/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.tomk

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.server.SPacketPlayerPosLook

@ModuleInfo(name = "Disabler", description = "Spoofs your ping to a given value.", category = ModuleCategory.TOMK)
class Disabler : Module() {

    private val modeValue = ListValue("PacketMode", arrayOf("Lag", "Plt"), "Lag")

    private val noairValue = BoolValue("Flag-NoAir", false)

    private val rangeValue = FloatValue("Flag-Range", 4.4F, 1F, 8F)

    private val onAuraValue = BoolValue("Flag-OnAura", false)

    private val groundValue = BoolValue("Flag-Ground", false)

    private val slowValue = BoolValue("Flag-Slowdown", false)
    private val slowYValue = BoolValue("Flag-SlowdownY", false)


    override fun onDisable() {

    }

    @EventTarget
    fun onPacket(event: PacketEvent) {

        if (modeValue.get().equals("Plt", ignoreCase = true)) {
            val packet = event.packet

            if (classProvider.isCPacketPlayer(packet))
                packet.asCPacketPlayer().onGround = false
        } else {

            val packet = event.packet.unwrap()
            var ka = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura)
            if (this.onAuraValue.get() &&
                ka.target == null)
                return

            if (packet is SPacketPlayerPosLook) {
                if (this.noairValue.get() &&
                    !mc.thePlayer!!.onGround)
                    return
                val x = packet.x - mc.thePlayer!!.posX
                val y = packet.y - mc.thePlayer!!.posY
                val z = packet.z - mc.thePlayer!!.posZ
                val diff = Math.sqrt(x * x + y * y + z * z)
                if (diff <= this.rangeValue.get()) {
                    event.cancelEvent()
                    mc.netHandler.addToSendQueue(
                        classProvider.createCPacketPlayerPosLook(
                            packet.x,
                            packet.y,
                            packet.z,
                            packet.getYaw(),
                            packet.getPitch(),
                            this.groundValue.get()
                        )
                    )
                    if (this.slowValue.get()) {
                        mc.thePlayer!!.motionX = 0.0
                        if (this.slowYValue.get()) {
                            mc.thePlayer!!.motionY = 0.0
                        }
                        mc.thePlayer!!.motionZ = 0.0
                    }
                }
            }
        }
    }

    override val tag: String
        get() = this.modeValue.get()
}