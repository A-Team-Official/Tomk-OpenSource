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
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager

class Slowly(inst: Target): TargetStyle("Slowly", inst, true) {

    override fun drawTarget(entity: IEntityLivingBase) {
        val font = Fonts.font35
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name!!)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F

        updateAnim(entity.health)

        RenderUtils.drawRect(0F, 0F, 32F + length, 36F, targetInstance.bgColor.rgb)

        if (mc.netHandler.getPlayerInfo(entity.uniqueID) != null) 
            drawHead(mc.netHandler.getPlayerInfo(entity.uniqueID)!!.locationSkin, 1, 1, 30, 30, 1F - targetInstance.getFadeProgress())

        font.drawStringWithShadow(entity.name!!, 33, 2, getColor(-1).rgb)
        font.drawStringWithShadow(healthString,
            (length + 31F - font.getStringWidth(healthString)).toInt(), 22, targetInstance.barColor.rgb)

        RenderUtils.drawRect(0F, 32F, (easingHealth / entity.maxHealth.toFloat()).coerceIn(0F, entity.maxHealth.toFloat()) * (length + 32F), 36F, targetInstance.barColor.rgb)
    }

    override fun handleBlur(entity: IEntityLivingBase) {
        val font = Fonts.font35
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name!!)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderUtils.quickDrawRect(0F, 0F, 32F + length, 36F)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    override fun handleShadowCut(entity: IEntityLivingBase) = handleBlur(entity)

    override fun handleShadow(entity: IEntityLivingBase) {
        val font = Fonts.font35
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name!!)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F

        RenderUtils.newDrawRect(0F, 0F, 32F + length, 36F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: IEntityLivingBase?): Border? {
        entity ?: return Border(0F, 0F, 102F, 36F)
        val font = Fonts.font35
        val healthString = "${decimalFormat2.format(entity.health)} ❤"
        val length = 60.coerceAtLeast(font.getStringWidth(entity.name!!)).coerceAtLeast(font.getStringWidth(healthString)).toFloat() + 10F
        return Border(0F, 0F, 32F + length, 36F)
    }

}