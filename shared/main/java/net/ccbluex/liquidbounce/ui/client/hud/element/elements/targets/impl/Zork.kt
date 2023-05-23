package net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.impl

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.targets.TargetStyle
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils

import org.lwjgl.opengl.GL11
import tomk.Stencil
import tomk.render.RoundedUtil
import java.awt.Color

class Zork(inst: Target)  : TargetStyle("Zork",inst,true){
    private var width = 0F


    override fun drawTarget(entity: IEntityLivingBase) {
        updateAnim(entity.health)
        width = 26f + Fonts.sfbold35.getStringWidth(entity.name.toString()).coerceAtLeast(90)
        val size = 24f
        val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        var hurtPercent = mc.thePlayer!!.hurtTime / 10F
        if (killAura.target != null) {
            hurtPercent = killAura.target!!.hurtTime / 10f
        }
        val scale = if (hurtPercent == 0f) {
            1f
        } else if (hurtPercent < 0.5f) {
            1 - (0.2f * hurtPercent * 2)
        } else {
            0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
        }
        GL11.glPushMatrix()
        GL11.glTranslatef(5f, 5f, 0f)
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)

        RoundedUtil.drawRound(0f, 0f, width, 40F, 5.0f, Color(29, 29, 31, 255))
        val playerInfo = mc.netHandler.getPlayerInfo(entity.uniqueID)
        if (playerInfo != null) {
            Stencil.write(false)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            RenderUtils.fastRoundedRect(6F, 6F, 30F, 30F, 5F)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            Stencil.erase(true)
            drawHead(playerInfo.locationSkin, 6, 6, 24, 24, 1F - targetInstance.getFadeProgress())
            Stencil.dispose()

        }
        RoundedUtil.drawRound(
            34F,
            20F,
            (width - 42) * (easingHealth / entity.maxHealth),
            2F,
            0.8F,
            Color(0, 95, 255)
        )

        Fonts.font35.drawString(entity.name.toString(), 34F, 18F - Fonts.font35.fontHeight, Color.WHITE.rgb)


        RoundedUtil.drawRound(
            34F,
            28F,
            (width - 42) * (mc.thePlayer!!.totalArmorValue2 / 20F).coerceIn(0F, 1F),
            2F,
            0.8F,
            Color(255, 255, 255)
            )
        GL11.glPopMatrix()
    }



    override fun getBorder(entity: IEntityLivingBase?): Border {
        return Border(0f,0f,width,40f)

    }
}