package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FloatValue
import java.awt.Color
@ElementInfo(name = "Armor")
class Armor(x: Double = -8.0, y: Double = 57.0, scale: Float = 1F):Element() {
    private val radiusValue = FloatValue("Radius", 4.25f, 0f, 10f)
    private var antimY = 0.0
    override fun drawElement(): Border {

        val width = 70F
        var height = 0F
        var y = 1

        for (index in 0..3) {
            if(mc.thePlayer!!.inventory.armorInventory[index]!!.item != null)
                height += 25
        }
        if (mc.playerController.isNotCreative) {
            antimY = RenderUtils.getAnimationState2(antimY, height.toDouble(), 250.0)

            RenderUtils.drawRoundedRect(
                    0F,
                    0F,
                    width,
                    (0 + antimY).toFloat(),
                    radiusValue.get(),
                    Color(32, 30, 30).rgb
            )
            for (index in 3 downTo 0) {
                val player = mc.thePlayer
                val stack = player!!.inventory.armorInventory[index] ?: continue
                val armorValue = (((stack.maxDamage - stack.itemDamage).toFloat() / stack.maxDamage) * 100f).toDouble()
                val armorValue2 = (((stack.maxDamage - stack.itemDamage).toFloat() / stack.maxDamage))
                Fonts.sfbold28.drawString(Math.round(armorValue).toString() + "%", 29, y + 6, Color.WHITE.rgb)
                RenderUtils.renderItemStack(stack, 5, y + 5)
                RenderUtils.drawRect(22.5.toInt(), y + 10, 23.5.toInt(), y + 20, Color.WHITE.rgb)
                RenderUtils.originalRoundedRect(
                        30F, (Fonts.sfbold28.fontHeight + y + 5).toFloat(),
                        30F + armorValue2 * 30F, (Fonts.sfbold28.fontHeight + y + 5 + 3.5).toFloat(),
                        1F, if (armorValue2 > 0.5) Color(0, 95, 255).rgb    else       Color(135, 4, 4).rgb )
                y += 25
            }
        }
        return Border(0F,0F,width,100F)
    }
}

