@file:Suppress("SENSELESS_COMPARISON")

package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.tomk.AutoL
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.renderer.GlStateManager
import oh.yalan.NativeClass
import org.lwjgl.opengl.GL11
import tomk.CFont.FontLoaders
import tomk.Recorder
import tomk.ShadowUtils
import tomk.render.DrawArc
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*

/**
 * CustomHUD Armor element
 *
 * Shows a horizontal display of current armor
 */
@NativeClass
@ElementInfo(name = "GameInfo2")
class SessionInfo2(x: Double = 10.0, y: Double = 10.0, scale: Float = 1F) : Element(x, y, scale) {
    private val radiusValue = FloatValue("Radius", 4.25f, 0f, 10f)
    private val shadowValue = FloatValue("shadow-Value", 10F, 0f, 20f)
    private val shadowColorMode = ListValue("Shadow-Color", arrayOf("Background", "Custom"), "Background")
    private val shadowColorRedValue = IntegerValue("Shadow-Red", 0, 0, 255)
    private val shadowColorGreenValue = IntegerValue("Shadow-Green", 111, 0, 255)
    private val shadowColorBlueValue = IntegerValue("Shadow-Blue", 255, 0, 255)

    var minute = 0L
    var second = 0L
    val time2 = MSTimer()
    val time1 = MSTimer()

    /**
     * Draw element
     */
    override fun drawElement(): Border {
        val x2 = 145.0
        val y2 = (Fonts.font35.fontHeight * 5 + 18)*1.15
        val durationInMillis: Long = System.currentTimeMillis() - LiquidBounce.playTimeStart
        val hour = durationInMillis / (1000 * 60 * 60) % 24
        var arc = 0F

        if (time2.hasTimePassed(1000L)){
            if (second == 59L){
                minute += 1L
            }
            second += 1L
            time2.reset()
        }

        if (second > 60L){
            second = 0L
        }

        if (minute != 59L){
            if (minute != 0L) {
                arc = (360.0 / (59L / minute)).toFloat()
            }else{
                arc = 0F

            }
        }else{
            minute = 0L
            second = 0L

        }



        RenderUtils.drawRoundedRect(-4F,0F,x2.toFloat(),y2.toFloat(),radiusValue.get(),Color(32, 30, 30).rgb)

        GL11.glTranslated(-renderX, -renderY, 0.0)
        GL11.glScalef( 1F,  1F,  1F)
        GL11.glPushMatrix()
        ShadowUtils.shadow(shadowValue.get(),{
            GL11.glPushMatrix()
            GL11.glTranslated(renderX, renderY, 0.0)
            GL11.glScalef(scale, scale, scale)
            RenderUtils.originalRoundedRect(-2F,0F,x2.toFloat(),y2.toFloat(),radiusValue.get(),
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
            RenderUtils.fastRoundedRect(-2F,0F,x2.toFloat(),y2.toFloat(),radiusValue.get())
            GlStateManager.enableTexture2D()
            GlStateManager.disableBlend()
            GL11.glPopMatrix()
        })
        GL11.glPopMatrix()
        GL11.glScalef(scale, scale, scale)
        GL11.glTranslated(renderX, renderY, 0.0)


        FontLoaders.tenacitybold20.drawString("Session", 2F,2.5F,Color.WHITE.rgb)
        FontLoaders.tenacitybold20.drawString("Play Time", x2.toFloat() - 2F -FontLoaders.tenacity22.getStringWidth("Play Time") ,2.5F,Color.WHITE.rgb)
        FontLoaders.tenacitybold18.drawCenteredString("$minute:$second", (x2.toFloat() - 2F )-(FontLoaders.tenacity22.getStringWidth("Play Time") / 2F).toDouble() , (y2 / 2F - 2F).toFloat()+3F.toDouble(),Color.WHITE.rgb)

        RenderUtils.drawRect((x2.toFloat() - 2F -FontLoaders.tenacity22.getStringWidth("Play Time")),(Fonts.font35.fontHeight + 2.5F + 0.0).toFloat(),x2.toFloat()-2F,Fonts.font35.fontHeight + 2.5F + 1.16f,Color.WHITE.rgb)
        DrawArc.drawArc(
            (x2.toFloat() - 2F )-(FontLoaders.tenacity22.getStringWidth("Play Time") / 2F) ,
            (y2 / 2F).toFloat()+3F,
            22.0,
            Color.WHITE.rgb,
            0,
            360.0,
            6f
        )
        DrawArc.drawArc(
            (x2.toFloat() - 2F )-(FontLoaders.tenacity22.getStringWidth("Play Time") / 2F) ,
            (y2 / 2F).toFloat()+3F,
            22.0,
            Color(0, 95, 255).rgb,
            0,
            arc.toDouble(),
            6f
        )

        RenderUtils.drawRect(2F,(Fonts.font35.fontHeight + 2.5F + 0.0).toFloat(),2F + (FontLoaders.tenacity22
            .getStringWidth("Session")).toFloat(),Fonts.font35.fontHeight + 2.5F + 1.16f,Color.WHITE.rgb)
        Fonts.font35.drawStringWithShadow("Players Killed: ", 2,
            (  Fonts.font35.fontHeight * 2 * 1.15 + 3F ).toInt(), Color.WHITE.rgb)
        Fonts.font35.drawStringWithShadow(Recorder.killCounts.toString(),
            (4F + Fonts.font35.getStringWidth(("Players Killed: ").toString())).toInt(), (  Fonts.font35.fontHeight * 2 * 1.15 + 4f).toInt(), Color.WHITE.rgb)
          Fonts.font35.drawStringWithShadow("Win: " , 2,
            (  Fonts.font35.fontHeight * 3 * 1.15  + 3F + 4f ).toInt(), Color.WHITE.rgb)
        Fonts.font35.drawStringWithShadow(Recorder.win.toString(),
            (4F + Fonts.font35.getStringWidth(("Win: ").toString())).toInt(),   (  Fonts.font35.fontHeight * 3 * 1.15 + 4f*2).toInt(), Color.WHITE.rgb)
          Fonts.font35.drawStringWithShadow("Total: " , 2,
            (  Fonts.font35.fontHeight * 4 * 1.15 + 3F + 4f*2).toInt(), Color.WHITE.rgb)
        Fonts.font35.drawStringWithShadow(Recorder.totalPlayed.toString(),
            (4F + Fonts.font35.getStringWidth(("Total: ").toString())).toInt(), (  Fonts.font35.fontHeight * 4 * 1.15 + 4f*3).toInt(), Color.WHITE.rgb)



    return Border(-2F, 0F, x2.toFloat(), y2.toFloat())
    }

}
