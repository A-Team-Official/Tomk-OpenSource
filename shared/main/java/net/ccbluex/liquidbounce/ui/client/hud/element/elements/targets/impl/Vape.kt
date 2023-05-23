/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.isAnimal
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import org.lwjgl.opengl.GL11
import java.awt.Color

class Vape(inst: Target): TargetStyle("Vape", inst, false) {

    override fun drawTarget(entity: IEntityLivingBase) {
        updateAnim(entity.health)

        // background
        RenderUtils.newDrawRect(0F, 0F, 146F, 49F, getColor(Color(25, 25, 25)).rgb)
        RenderUtils.newDrawRect(1F, 1F, 145F, 48F, getColor(Color(35, 35, 35)).rgb)

        // health bar
        RenderUtils.newDrawRect(41F, 24F, 95F, 17F, getColor(Color(95, 95, 95)).rgb)
        RenderUtils.newDrawRect(41.5F, 23.5F, 94F, 17.5F, getColor(Color(35, 35, 35)).rgb)
        RenderUtils.newDrawRect(43F, 22.5F, 93F, 18.5F, getColor(Color.red.darker().darker()).rgb)
        RenderUtils.newDrawRect(43F, 23F, 43F + (easingHealth / entity.maxHealth).coerceIn(0F, 1F) * 49.5F, 18.5F, targetInstance.barColor.rgb)

        // head
        RenderUtils.newDrawRect(3F, 6F, 40F, 44F, getColor(Color(150, 150, 150)).rgb)
        RenderUtils.newDrawRect(4F, 7F, 39F, 43F, getColor(Color(0, 0, 0)).rgb)



        // armor item background
        RenderUtils.newDrawRect(41F, 26F, 59F, 44F, getColor(Color(95, 95, 95)).rgb)
        RenderUtils.newDrawRect(42F, 27F, 58F, 43F, getColor(Color(25, 25, 25)).rgb)

        RenderUtils.newDrawRect(59F, 26F, 77F, 44F, getColor(Color(95, 95, 95)).rgb)
        RenderUtils.newDrawRect(60F, 27F, 76F, 43F, getColor(Color(25, 25, 25)).rgb)

        RenderUtils.newDrawRect(77F, 26F, 95F, 44F, getColor(Color(95, 95, 95)).rgb)
        RenderUtils.newDrawRect(78F, 27F, 94F, 43F, getColor(Color(25, 25, 25)).rgb)

        RenderUtils.newDrawRect(95F, 26F, 113F, 44F, getColor(Color(95, 95, 95)).rgb)
        RenderUtils.newDrawRect(96F, 27F, 112F, 43F, getColor(Color(25, 25, 25)).rgb)



        // name
        val heal = decimalFormat.format(easingHealth)
        Fonts.font35.drawStringWithShadow(entity.name!!, 45 ,
            (17F - Fonts.font35.fontHeight).toInt(), getColor(-1).rgb)
        Fonts.sfbold28.drawString("HP:$heal",98F,20F,Color.white.rgb)
        // head
        if (mc.netHandler.getPlayerInfo(entity.uniqueID) != null) {
            // actual head
            drawHead(mc.netHandler.getPlayerInfo(entity.uniqueID)!!.locationSkin, 4, 7, 34, 34, 1F - targetInstance.getFadeProgress())

        }

        // armor items

        if (!entity.isAnimal()) {
            GL11.glPushMatrix()
            GL11.glColor4f(1f, 1f, 1f, 1f - targetInstance.getFadeProgress())
            RenderHelper.enableGUIStandardItemLighting()
            var player:IEntityPlayer? = null
            val renderItem = mc.renderItem
             player = entity.asEntityPlayer()
            var x = 42
            var y = 27
            for (index in 3 downTo 0) {
                val stack = player!!.inventory.armorInventory[index] ?: continue

                if (stack.item == null)
                    continue

                renderItem.renderItemAndEffectIntoGUI(stack, x, y)
                x += 18
            }
            var x1 = 114
            val stack2 = player!!.inventory.mainInventory[player.inventory.currentItem]
            if (player.inventory.currentItem >= 0){
                RenderUtils.newDrawRect(113F, 26F, 131F, 44F, getColor(Color(95, 95, 95)).rgb)
                RenderUtils.newDrawRect(114F, 27F, 130F, 43F, getColor(Color(25, 25, 25)).rgb)
            }
            renderItem.renderItemAndEffectIntoGUI(stack2!!, x1, y)

            RenderHelper.disableStandardItemLighting()
            GlStateManager.enableAlpha()
            GlStateManager.disableBlend()
            GlStateManager.disableLighting()
            GlStateManager.disableCull()
            GL11.glPopMatrix()
        }


    }

    override fun getBorder(entity: IEntityLivingBase?): Border? {
        return Border(0F, 0F, 146F, 49F)
    }

}