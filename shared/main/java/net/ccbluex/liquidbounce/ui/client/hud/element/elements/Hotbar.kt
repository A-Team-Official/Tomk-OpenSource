package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import tomk.hotbarutil
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList


@ElementInfo(name = "Hotbar") class Hotbar(x: Double = 40.0, y: Double = 100.0) : Element(x, y) {


    val slotlist = mutableListOf<hotbarutil>()

    private var lastSlot = -1

    init {
        for (i in 0..8) {
            val slot = hotbarutil()
            slotlist.add(slot)
        }
    }

    override fun drawElement(): Border {

        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)


        slotlist.forEachIndexed { index, hotbarutil ->

            val hover = index == mc.thePlayer!!.inventory.currentItem && mc.thePlayer!!.inventory.mainInventory[index] != null
            val scale = hotbarutil.translate.x
            val positionX = (index * 25 / scale) - 5
            val currentitem = mc.thePlayer!!.inventory.mainInventory[mc.thePlayer!!.inventory.currentItem]

            hotbarutil.size = if (hover) 1.5f else 1.0f
            hotbarutil.translate.translate(hotbarutil.size, 0f, 2.0)

            if (hover) {
                GlStateManager.pushMatrix()
                GlStateManager.scale(scale - 0.5f, scale - 0.5f, scale - 0.5f)

                try {
                    val infolist : String = mc.thePlayer!!.heldItem!!.displayName;
                    var posy = -18f
                    val font = Fonts.font40
                    font.drawString(infolist, positionX * 1.5f,  posy,-1 , true)
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                GlStateManager.popMatrix()
            }
            GlStateManager.pushMatrix()
            GlStateManager.scale(scale, scale, scale)
            RenderHelper.enableGUIStandardItemLighting()
            hotbarutil.renderHotbarItem(index, positionX, -10f, mc.timer.renderPartialTicks)
            RenderHelper.disableStandardItemLighting()
            GlStateManager.popMatrix()
        }

        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
        return Border(0f, 0f, 180f, 17f)
    }
}