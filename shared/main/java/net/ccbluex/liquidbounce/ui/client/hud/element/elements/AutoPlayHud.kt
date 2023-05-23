package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.LiquidBounce

import net.ccbluex.liquidbounce.features.module.modules.tomk.AutoGG
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import org.lwjgl.opengl.GL11
import oh.yalan.NativeClass
import net.ccbluex.liquidbounce.features.module.modules.tomk.FadeState.*
import tomk.EaseUtils
import java.awt.Color
@NativeClass
@ElementInfo(name = "AutoPlayHud")
class AutoPlayHud(x: Double = 130.0, y: Double = 20.0, scale: Float = 1F):Element(x,y, scale) {

    val autoPlay = LiquidBounce.moduleManager[AutoGG::class.java] as AutoGG

    var animeXTime = System.currentTimeMillis()
    var animeYTime = System.currentTimeMillis()
    var nowY = 30
    override fun drawElement(): Border? {
        val width =
            Fonts.font35.getStringWidth("Game over will send you in next game in 5s") + 25F + 5F

        var nowTime = System.currentTimeMillis()
        val realY = 2 * 30

        var pct = (nowTime - animeXTime) / 500.toDouble()
        when (autoPlay.fadeState) {
            FRIST ->{
                if (pct > 1) {
                    autoPlay.fadeState = IN
                    animeXTime = nowTime
                    pct = 1.0
                }
            }
            IN -> {
                if (pct > 1) {
                    autoPlay.fadeState = STAY
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct =  EaseUtils.easeOutBack(pct)
            }

            STAY -> {
                if ((nowTime - animeXTime) > 5 * 1000) {
                    autoPlay.fadeState = OUT
                    animeXTime = nowTime
                }
                pct = 1.0


            }

            OUT -> {
                if (pct > 1) {
                    autoPlay.fadeState = END
                    animeXTime = nowTime
                    pct = 1.0
                }
                pct = 1 - EaseUtils.easeInBack(pct)
            }

            END -> {
                pct = 0.0
            }
            NO ->{
                pct = 0.0
            }
        }

        if(!classProvider.isGuiHudDesigner(mc.currentScreen)){
            val img = "liquidbounce/notification/info.png"
            GL11.glScaled(pct, pct, pct)
            GL11.glTranslatef(-width.toFloat()/2 , -30.toFloat()/2, 0F)

            RenderUtils.originalRoundedRect(0F, 0F, width, 30F, 15F, Color(0, 150, 255, 150).rgb)
            RenderUtils.drawImage(classProvider.createResourceLocation(img), 3, 5, 20, 20)
            RenderUtils.drawCircle(13F, 15F, 7.5F, 0, 360)
            Fonts.font35.drawString(
                "Game over will send you in next game in 5s",
                25,
                20,
                Color.WHITE.rgb
            )
            Fonts.font40.drawCenteredString(
                "Info",
                width / 2,
                5F,
                Color.WHITE.rgb
            )
        }else {
            return Border(0F, 0F, width, 30F)
        }
        return Border(0F, 0F, width, 30F)


    }

}
