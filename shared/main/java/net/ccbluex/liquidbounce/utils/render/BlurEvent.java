package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import tomk.Hotbar.BlurUtils;
import tomk.ShadowUtils;
import tomk.render.Hotbar;
import tomk.render.RoundedUtil;

import java.awt.*;

public class BlurEvent implements Listenable {
    @Override
    public boolean handleEvents() {
        return true;
    }
    static GuiNewChat guiNewChat = new GuiNewChat(Minecraft.getMinecraft());
    static float width = guiNewChat.getChatWidth();
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float lvt_7_1_ = guiNewChat.getChatScale();
        int lvt_8_1_ = MathHelper.ceil((float) width / lvt_7_1_);
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getChatBlur().get()) {
            GL11.glPushMatrix();
            BlurUtils.blurAreaRounded(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 28F, lvt_8_1_ + 8F, scaledResolution.getScaledHeight() - 28F,hud.getRadius().get(),hud.getChatBlurvalue().get());
            GL11.glPopMatrix();

        }
        GL11.glPushMatrix();
        if (hud.getChatShadow().get()) {
            ShadowUtils.INSTANCE.shadow(hud.getChatShadowvalue().get(), () -> {
                GL11.glPushMatrix();
                RoundedUtil.drawRound(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 27F, lvt_8_1_ + 7.2F, (scaledResolution.getScaledHeight() - 27F) - (scaledResolution.getScaledHeight() + Hotbar.render2 - 24F), hud.getRadius().get() + 1.5f, new Color(0, 0, 0));
                GL11.glPopMatrix();

                return null;
            }, () -> {
                GL11.glPushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableTexture2D();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RoundedUtil.drawRound(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 27F, lvt_8_1_ + 7.2F, (scaledResolution.getScaledHeight() - 27F) - (scaledResolution.getScaledHeight() + Hotbar.render2 - 24F), hud.getRadius().get() + 1.5f, new Color(0, 0, 0));
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GL11.glPopMatrix();
                return null;
            });
        }
        GL11.glPopMatrix();
    }
}
