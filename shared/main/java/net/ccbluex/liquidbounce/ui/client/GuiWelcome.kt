package net.ccbluex.liquidbounce.ui.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen

import net.ccbluex.liquidbounce.ui.font.Fonts

import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import oh.yalan.NativeClass
import tomk.AnimationUtils
import tomk.CFont.CFontRenderer
import tomk.CFont.FontLoaders
import tomk.ParticleEngine
import tomk.TimerUtil
import tomk.Translate
import verify.MySQLDemo
import verify.WbxMain
import java.awt.Color

@NativeClass
class GuiWelcome : WrappedGuiScreen() {
    var pe: ParticleEngine = ParticleEngine()
    var rev = false
    var skiped = false
    var anim = 0.0
    var anim2 = 0.0
    var anim3 = ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toDouble()
    var translate: Translate = Translate(0F, ScaledResolution(Minecraft.getMinecraft()).scaledHeight.toFloat())
    var translate2: Translate = Translate(
        ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toFloat(),
        ScaledResolution(Minecraft.getMinecraft()).scaledHeight.toFloat()
    )

    override fun initGui() {
        timer.reset()
        translate = Translate(0F, ScaledResolution(Minecraft.getMinecraft()).scaledHeight.toFloat())
        translate2 = Translate(
            ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toFloat(),
            ScaledResolution(Minecraft.getMinecraft()).scaledHeight.toFloat()
        )
    }

    override fun keyTyped(var1: Char, var2: Int) {}
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            skiped = true
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (!timer.hasTimeElapsed(500)) {
            anim3 = ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toDouble()
            anim2 = anim3
            anim = anim2
            rev = true
        }
        if (rev) {
            anim = AnimationUtils.animate(
                ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toDouble(),
                anim,
                (if (skiped) 12.0f else 6.0f) / Minecraft.getDebugFPS().toDouble()
            )
            anim2 = AnimationUtils.animate(
                ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toDouble(),
                anim2,
                (if (skiped) 8.0f else 4.0f) / Minecraft.getDebugFPS().toDouble()
            )
            anim3 = AnimationUtils.animate(
                ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toDouble(),
                anim3,
                (if (skiped) 11.0f else 5.5f) / Minecraft.getDebugFPS().toDouble()
            )
        } else {
            anim = AnimationUtils.animate(0.0, anim, (if (skiped) 6.0f else 3.0f) / Minecraft.getDebugFPS().toDouble())
            anim2 = AnimationUtils.animate(0.0, anim2, (if (skiped) 10.0f else 5.0f) / Minecraft.getDebugFPS().toDouble())
            anim3 = AnimationUtils.animate(0.0, anim3, (if (skiped) 9f else 4.5f) / Minecraft.getDebugFPS().toDouble())
        }
        val sr = classProvider.createScaledResolution(mc)
        val fontwel2 = net.ccbluex.liquidbounce.feng.FontLoaders.C20
        val fontwel: CFontRenderer = FontLoaders.Sans35
        representedScreen.drawBackground(0)
        pe.render(0.0F, 0.0F)
        if (!timer.hasTimeElapsed(3500)) {
            translate.interpolate(sr.scaledWidth.toFloat() / 2, sr.scaledHeight.toFloat() / 2 - 3f, 0.14)
            translate2.interpolate(
                sr.scaledWidth.toFloat() / 2,
                sr.scaledHeight.toFloat() / 2 + fontwel.getHeight(),
                0.14
            )
        }
        fontwel.drawCenteredStringWithShadow(
            "Welcome back to " + LiquidBounce.CLIENT_NAME,
            translate.getX(),
            translate.getY(),
            Color(255, 255, 255).rgb
        )
        fontwel2.drawCenteredString(
            "欢迎回来," + MySQLDemo.getname,
            translate2.getX() + 10.toDouble(),
            translate2.getY() + 10.toDouble(),
            Color(255, 255, 255).rgb
        )
        fontwel2.drawCenteredString(
            "你的用户组为:" + WbxMain.Group,
            translate2.getX() + 20.toDouble(),
            translate2.getY() + 20.toDouble(),
            Color(255, 255, 255).rgb
        )
        RenderUtils.drawRect(-10f, -10f, anim.toFloat(), (sr.scaledHeight + 10).toFloat(), Color(203, 50, 255).rgb)
        RenderUtils.drawRect(-10f, -10f, anim3.toFloat(), (sr.scaledHeight + 10).toFloat(), Color(0, 217, 255).rgb)
        RenderUtils.drawRect(-10f, -10f, anim2.toFloat(), (sr.scaledHeight + 10).toFloat(), Color(47, 47, 47).rgb)
        if (timer.hasTimeElapsed(3500)) {
            translate.interpolate(
                0F, (ScaledResolution(Minecraft.getMinecraft()).scaledHeight + 5).toFloat(),
                0.14.toFloat().toDouble()
            )
            translate2.interpolate(
                ScaledResolution(Minecraft.getMinecraft()).scaledWidth.toFloat(),
                ScaledResolution(Minecraft.getMinecraft()).scaledHeight + 5.toFloat(),
                0.14.toFloat().toDouble()
            )
        }
        //mc.displayGuiScreen(new GuiMainMenu());
        if (timer.hasTimeElapsed(4000) || skiped) {
            rev = true
            if (anim2 >= representedScreen.width - 5) {
                mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiMainMenu()))
            }
        } else {
            rev = false
        }
    }

    companion object {
        private val timer: TimerUtil = TimerUtil()
    }
}