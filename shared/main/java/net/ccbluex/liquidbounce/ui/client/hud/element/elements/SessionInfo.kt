@file:Suppress("SENSELESS_COMPARISON")

package net.ccbluex.liquidbounce.ui.client.hud.element.elements


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import tomk.ShadowUtils
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics


/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */

@ElementInfo(name = "GameInfo")
class SessionInfo(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private val radiusValue = FloatValue("Radius", 4.25f, 0f, 10f)
    private val shadowValue = FloatValue("shadow-Value", 10F, 0f, 20f)

    private val shadowColorMode = ListValue("Shadow-Color", arrayOf("Background", "Custom"), "Background")
    private val shadowColorRedValue = IntegerValue("Shadow-Red", 0, 0, 255)
    private val shadowColorGreenValue = IntegerValue("Shadow-Green", 111, 0, 255)
    private val shadowColorBlueValue = IntegerValue("Shadow-Blue", 255, 0, 255)


    private var ms = 0.0
    private var health = 0.0
    private var speed = 0.0
    private var fps = 0.0

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        val x2 = 114
        val y2 = 100
        val durationInMillis: Long = System.currentTimeMillis() - LiquidBounce.playTimeStart





        RenderUtils.drawRoundedRect(0F,0F,x2.toFloat(),y2.toFloat(),radiusValue.get(),Color(32, 30, 30).rgb)
        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glScalef( 1F,  1F,  1F)
        GL11.glPushMatrix()
        ShadowUtils.shadow(shadowValue.get(),{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            RenderUtils.originalRoundedRect(0F,0F,x2.toFloat(),y2.toFloat(),radiusValue.get(),
                if (shadowColorMode.get().equals("background", true))
                    Color(32, 30, 30).rgb
                else
                    Color(shadowColorRedValue.get(), shadowColorGreenValue.get(), shadowColorBlueValue.get()).rgb)
            GL11.glPopMatrix()
        },{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            GlStateManager.enableBlend()
            GlStateManager.disableTexture2D()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            RenderUtils.fastRoundedRect(0F,0F,x2.toFloat(),y2.toFloat(),radiusValue.get())
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)
        Fonts.tenacitybold40.drawCenteredString("GameInfo", 114.0f / 2.0f, 5.0f, Color.WHITE.rgb)
        RenderUtils.drawRect(5.0f, 80.0f, 109.0f, 85.0f, Color.WHITE.rgb)
        RenderUtils.drawRect(5.0f, 17.0f, 109.0f, 19.0f, Color.WHITE.rgb)

        health = RenderUtils.getAnimationState2(health, health / mc.thePlayer!!.maxHealth * 55.0f, 200.0).coerceAtMost(55.0).coerceAtLeast(4.0)
        fps = RenderUtils.getAnimationState2(fps, ((mc.debugFPS/300f) * 55.0f).toDouble(), 200.0).coerceAtMost(55.0).coerceAtLeast(4.0)
        ms = RenderUtils.getAnimationState2(ms, ((EntityUtils.getPing(mc.thePlayer) / 50.0f)  * 55.0f).toDouble(), 200.0).coerceAtMost(55.0).coerceAtLeast(4.0)
        speed = RenderUtils.getAnimationState2(speed, MovementUtils.getBlockSpeed(mc.thePlayer!!) /20F * 55.0f, 200.0).coerceAtMost(55.0).coerceAtLeast(4.0)

        Fonts.tenacitybold30.drawString("Health", 7, 90, Color.WHITE.rgb);
        Fonts.tenacitybold30.drawString("Speed", 7 + Fonts.tenacitybold30.getStringWidth("Health") + 10, 90, Color.WHITE.rgb);
        Fonts.tenacitybold30.drawString("FPS", 7 + Fonts.tenacitybold30.getStringWidth("Health") + Fonts.tenacitybold30.getStringWidth("Speed") + 20, 90, Color.WHITE.rgb);
        Fonts.tenacitybold30.drawString("MS", 7 + Fonts.tenacitybold30.getStringWidth("Health") + Fonts.tenacitybold30.getStringWidth("Speed") + Fonts.tenacitybold30.getStringWidth("FPS") + 30, 90,Color.WHITE.rgb);
        RenderUtils.drawRoundedRect(10.0f, 77.0f, 15.0f,  ((77.0f - this.health).toFloat()), 2.0f,  Color(0, 95, 255).rgb);
        RenderUtils.drawRoundedRect(10.0f + Fonts.tenacitybold30.getStringWidth("Health") + 10.0f, 77.0f, 10.0f + Fonts.tenacitybold30.getStringWidth("Health") + 10.0f + 5.0f,
            (77.0f - ( this.speed)).toFloat(), 2.0f,  Color(0, 95, 255).rgb);
        RenderUtils.drawRoundedRect(10.0f + Fonts.tenacitybold30.getStringWidth("Health") + Fonts.tenacitybold30.getStringWidth("Speed") + 20.0f, 77.0f, 10.0f + Fonts.tenacitybold30.getStringWidth("Health") + Fonts.tenacitybold30.getStringWidth("Speed") + 20.0f + 5.0f,
            (77.0f - ( this.fps)).toFloat(), 2.0f,  Color(0, 95, 255).rgb);
        RenderUtils.drawRoundedRect(10.0f + Fonts.tenacitybold30.getStringWidth("Health") + Fonts.tenacitybold30.getStringWidth("Speed") + Fonts.tenacitybold30.getStringWidth("FPS") + 30.0f,
            77.0F,
            10.0f + Fonts.tenacitybold30.getStringWidth("Health") + Fonts.tenacitybold30.getStringWidth("Speed") + Fonts.tenacitybold30.getStringWidth("FPS") + 30.0f + 5.0f,
            (77.0f - ( this.ms)).toFloat(), 2.0f,  Color(0, 95, 255).rgb
        );

        return Border(0F, 0F, x2.toFloat(), y2.toFloat())
    }

}
