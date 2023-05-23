/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import org.lwjgl.opengl.GL11
import tomk.render.BlendUtils
import java.awt.Color

class Exhibition(inst: Target): TargetStyle("Exhibition", inst, false) {

    override fun drawTarget(entity: IEntityLivingBase) {
        val font = Fonts.sfbold35
        val minWidth = 126F.coerceAtLeast(47F + font.getStringWidth(entity.name!!))

        RenderUtils.drawExhiRect(0F, 0F, minWidth, 45F, 1F - targetInstance.getFadeProgress())

        RenderUtils.drawRect(2.5F, 2.5F, 42.5F, 42.5F, getColor(Color(59, 59, 59)).rgb)
        RenderUtils.drawRect(3F, 3F, 42F, 42F, getColor(Color(19, 19, 19)).rgb)

        GL11.glColor4f(1f, 1f, 1f, 1f - targetInstance.getFadeProgress())
        RenderUtils.drawEntityOnScreen(22, 40, 16, entity)

        font.drawString(entity.name!!, 46, 5, getColor(-1).rgb)

        val barLength = 70F * (entity.health / entity.maxHealth).coerceIn(0F, 1F)
        RenderUtils.drawRect(45F, 14F, 45F + 70F, 18F, getColor(BlendUtils.getHealthColor(entity.health, entity.maxHealth).darker()).rgb)
        RenderUtils.drawRect(45F, 14F, 45F + barLength, 18F, getColor(BlendUtils.getHealthColor(entity.health, entity.maxHealth)).rgb)

        for (i in 0..9)
            RenderUtils.drawRectBasedBorder(45F + i * 7F, 14F, 45F + (i + 1) * 7F, 18F, 0.5F, getColor(Color.black).rgb)

        Fonts.sfbold35.drawString("HP:${entity.health.toInt()} | Dist:${mc.thePlayer!!.getDistanceToEntityBox(entity).toInt()}", 45F, 21F, getColor(-1).rgb)

        GlStateManager.resetColor()
        GL11.glPushMatrix()
        GL11.glColor4f(1f, 1f, 1f, 1f - targetInstance.getFadeProgress())
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderHelper.enableGUIStandardItemLighting()

        val renderItem = mc.renderItem

        var x = 45
        var y = 28

        val player = entity.asEntityPlayer()
        for (index in 3 downTo 0) {
            val stack = player.inventory.armorInventory[index] ?: continue

            if (stack.item == null)
                continue

            renderItem.renderItemIntoGUI(stack, x, y)
            renderItem.renderItemOverlays(mc.fontRendererObj, stack, x, y)
            RenderUtils.drawExhiEnchants(stack, x.toFloat(), y.toFloat())

            x += 16
        }

        val mainStack = player.heldItem
        if (mainStack != null && mainStack.item != null) {
            renderItem.renderItemIntoGUI(mainStack, x, y)
            renderItem.renderItemOverlays(mc.fontRendererObj, mainStack, x, y)
            RenderUtils.drawExhiEnchants(mainStack, x.toFloat(), y.toFloat())
        }

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.enableAlpha()
        GlStateManager.disableBlend()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GL11.glPopMatrix()
    }

    override fun getBorder(entity: IEntityLivingBase?): Border? {
        entity ?: return Border(0F, 0F, 126F, 45F)

        val font = Fonts.sfbold35
        val minWidth = 126F.coerceAtLeast(47F + font.getStringWidth(entity.name!!))

        return Border(0F, 0F, minWidth, 45F)
    }

}