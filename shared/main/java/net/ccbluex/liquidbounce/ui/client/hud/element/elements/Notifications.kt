/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.Side
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer
import net.ccbluex.liquidbounce.utils.MinecraftInstance
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import tomk.CFont.FontLoaders
import tomk.EaseUtils
import tomk.render.RoundedUtil
import java.awt.Color

/**
 * CustomHUD Notification element
 */
@ElementInfo(name = "Notifications", single = true)
class Notifications(x: Double = 3.0, y: Double = 11.0, scale: Float = 1F,
                    side: Side = Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN)) : Element(x, y, scale, side) {

    /**
     * Example notification for CustomHUD designer
     */
    private val exampleNotification = Notification("Notification", "This is an example notification.", NotifyType.INFO)

    /**
     * Draw element
     */
    override fun drawElement(): Border? {
        // bypass java.util.ConcurrentModificationException
        LiquidBounce.hud.notifications.map { it }.forEachIndexed { index, notify ->
            GL11.glPushMatrix()
            val font = Fonts.font40

            if(notify.drawNotification(index,font)){
                LiquidBounce.hud.notifications.remove(notify)
            }

            GL11.glPopMatrix()
        }
        val hud = LiquidBounce.moduleManager.getModule(HUD::class.java) as HUD
        fun getTBorder() : Border =  when(hud.notificationStyle.get()) {
            "Tomk" -> Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(),0F,0F)
            "Windows11" ->  Border(-exampleNotification.width2.toFloat(), -exampleNotification.height.toFloat(),0F,0F)
            else -> Border(-exampleNotification.width.toFloat(), -exampleNotification.height.toFloat(), 0F, 0F)
        }
        if (classProvider.isGuiHudDesigner((mc.currentScreen))) {
            if (!LiquidBounce.hud.notifications.contains(exampleNotification))
                LiquidBounce.hud.addNotification(exampleNotification)

            exampleNotification.fadeState = FadeState.STAY
            exampleNotification.displayTime = System.currentTimeMillis()
//            exampleNotification.x = exampleNotification.textLength + 8F

            return getTBorder()
        }

        return null
    }

}

class Notification(val title: String, val content: String, val type: NotifyType, val time: Int=1500, val animeTime: Int=500) {
    var width = 100.coerceAtLeast(FontLoaders.JelloM20.getStringWidth(content) + 22)
    var width2 = 100.coerceAtLeast(FontLoaders.JelloM20.getStringWidth(content) + 12)

    var height = 45
    var fadeState = FadeState.IN
    var x =0f
    private val widthSpacing = 25f
    var y =0f
    val height2 = 28

    var nowY=-height
    var displayTime=System.currentTimeMillis()
    var animeXTime=System.currentTimeMillis()
    var animeYTime=System.currentTimeMillis()
    var notifWidth = 17+Math.max(FontLoaders.tenacitybold18.getStringWidth(content),FontLoaders.tenacitybold20.getStringWidth(title)) + widthSpacing

    var notifX: Float = (notifWidth + 5)

    /**
     * Draw notification
     */

    fun drawNotification(index: Int, font: GameFontRenderer):Boolean {

        val realY = -(index + 1) * height
        val nowTime = System.currentTimeMillis()

        //Y-Axis Animation
        if (nowY != realY) {
            var pct = (nowTime - animeYTime) / animeTime.toDouble()
            if (pct > 1) {
                nowY = realY
                pct = 1.0
            } else {
                pct = EaseUtils.easeOutExpo(pct)
            }
            GL11.glTranslated(0.0, (realY - nowY) * pct, 0.0)
        } else {
            animeYTime = nowTime
        }
        GL11.glTranslated(0.0, nowY.toDouble(), 0.0)

        //X-Axis Animation
        var pct = (nowTime - animeXTime) / animeTime.toDouble()
        when (fadeState) {
            FadeState.IN -> {
                if (pct > 1) {
                    fadeState = FadeState.STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = EaseUtils.easeOutExpo(pct)
            }

            FadeState.STAY -> {
                pct = 1.0
                if ((nowTime - animeXTime) > time) {
                    fadeState = FadeState.OUT
                    animeXTime = nowTime
                }
            }

            FadeState.OUT -> {
                if (pct > 1) {
                    fadeState = FadeState.END
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = 1 - EaseUtils.easeInExpo(pct)
            }

            FadeState.END -> {
                return true
            }
        }
        val hud = LiquidBounce.moduleManager[HUD::class.java] as HUD
        when (hud.notificationStyle.get()) {
            "Windows11" ->{
                GL11.glTranslated(width2 - (width2 * pct), 0.0, 0.0)
                GL11.glTranslatef(-width2.toFloat(), 0F, 0F)
                var s1 = ""
                when (type) {
                    NotifyType.ERROR -> {
                        s1 = "liquidbounce/notification/securityAndMaintenance_Error.png"
                    }

                    NotifyType.INFO -> {
                        s1 = "liquidbounce/notification/securityAndMaintenance_Alert.png"
                    }

                    NotifyType.SUCCESS -> {
                        s1 = "liquidbounce/notification/securityAndMaintenance.png"

                    }

                    NotifyType.WARNING -> {
                        s1 = "liquidbounce/notification/securityAndMaintenance_Alert.png"
                    }
                }
                RoundedUtil.drawRound(
                    0.0f,
                    0.0f,
                    width2.toFloat(),
                    height2.toFloat(),
                    4.0f,
                    Color(230, 230, 230, 255)
                )
                val f  = 5F
                Fonts.SFUI35.drawString(title, 30.0f, f, Color.BLACK.rgb, false)
                Fonts.font30.drawString(
                    content,
                    30.0f,
                    5F + Fonts.SFUI35.fontHeight + 2F,
                    Color(0, 0, 0, 255).rgb,
                    false
                )
                RenderUtils.drawImage(MinecraftInstance.classProvider.createResourceLocation(s1),4,5,20,20)


            }
            "TOMK" -> {
                GL11.glTranslated(width - (width * pct), 0.0, 0.0)
                GL11.glTranslatef(-width.toFloat(), 0F, 0F)
                var s1 = ""
                var renderColor = Color(79, 216, 7)
                when (type) {
                    NotifyType.ERROR -> {
                        s1 = "B"
                        renderColor = Color(255, 68, 50)
                    }

                    NotifyType.INFO -> {
                        s1 = "C"
                        renderColor = Color(7, 68, 255)
                    }

                    NotifyType.SUCCESS -> {
                        s1 = "A"
                        renderColor = Color(79, 216, 7)

                    }

                    NotifyType.WARNING -> {
                        s1 = "D"
                        renderColor = Color(220, 225, 6)
                    }
                }


                RoundedUtil.drawRound(
                        0.0f,
                        -1.0f,
                        width.toFloat(),
                        height.toFloat() - 10.0f,
                        3.0f,
                        Color(29, 29, 31, 255)
                )
                val icon60

                        : GameFontRenderer? = Fonts.ico1
                val color = Color.WHITE
                if (icon60 != null) {
                    icon60.drawString(s1, 7, 11, color.rgb)
                }
                val font35 = Fonts.sfbold35

                val s2 = title
                val f: Float = ((font.fontHeight.toFloat() / 2.0f).toDouble() + 5.0).toFloat()
                font35.drawString(s2, 28.0f, f, color.rgb, false)
                Fonts.sfbold28.drawString(
                        content,
                        28.0f,
                        ((font.fontHeight.toFloat() / 2.0f).toDouble() + 15.0).toFloat(),
                        Color(255, 255, 255, 100).rgb,
                        false
                )
                RenderUtils.drawGoodCircle(
                        (width - 6).toDouble(),
                        (font.fontHeight.toFloat() / 2.0f).toDouble() + 12.0,
                        2.0f,
                        renderColor.rgb
                )

            }

        }
        GlStateManager.resetColor()
        return false
    }

}


enum class NotifyType() {
    SUCCESS,
    ERROR,
    WARNING,
    INFO;
}


enum class FadeState { IN, STAY, OUT, END }

