package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import tomk.render.RoundedUtil
import java.awt.Color
import kotlin.math.abs

class LiqTomk(inst: Target): TargetStyle("LiqTomk", inst, true) {

    private var lastTarget: IEntityLivingBase? = null
    override fun drawTarget(entity: IEntityLivingBase) {

        val font = Fonts.sfbold35
        val healthString = "${decimalFormat2.format(entity.health)} "

        if (entity != lastTarget || easingHealth < 0 || easingHealth > entity.maxHealth ||
                abs(easingHealth - entity.health) < 0.01) {
            easingHealth = entity.health
        }
        val width = (38 + Fonts.sfbold40.getStringWidth(entity.name!!))
                .coerceAtLeast(118)
                .toFloat()

        // Draw rect box
        RenderUtils.drawRoundedRect(0F, 0F, width, 32F, 7F, targetInstance.bgColor.rgb)
        // Health bar
        RoundedUtil.drawRound(
                37F,
                25.5F,
                (width - 42) * (easingHealth / entity.maxHealth),
                2F,
                0.8F,
                Color(255, 255, 255)
        )
        updateAnim(entity.health)
        // Name
        Fonts.tenacitybold40.drawString(entity.name!!, 37, 3, getColor(-1).rgb)
        // HP
        GL11.glPushMatrix()
        GL11.glScalef(1F,1F,1F)
        font.drawStringWithShadow(healthString + "HP", 37, 17.5.toInt(), Color(255,255,255).rgb)
        GL11.glPopMatrix()

        GlStateManager.resetColor()
        RenderUtils.drawEntityOnScreen(18, 28, 12, entity)

        lastTarget = entity
    }
    override fun handleShadowCut(entity: IEntityLivingBase) = handleBlur(entity)

    override fun handleShadow(entity: IEntityLivingBase) {
        val width = (38 + Fonts.sfbold40.getStringWidth(entity.name!!))
                .coerceAtLeast(118)
                .toFloat()

        RenderUtils.newDrawRect(0F, 0F, width, 32F, shadowOpaque.rgb)
    }

    override fun getBorder(entity: IEntityLivingBase?): Border? {
        entity ?: return Border(0F, 0F, 118F, 32F)
        val width = (38 + Fonts.sfbold40.getStringWidth(entity.name!!))
                .coerceAtLeast(118)
                .toFloat()
        return Border(0F, 0F, width, 32F)
    }

}