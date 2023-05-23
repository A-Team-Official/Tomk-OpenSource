package tomk.render;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import tomk.Hotbar.BlurUtils;

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
        HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (hud.getChatBlur().get()) {
            float lvt_7_1_ = guiNewChat.getChatScale();
            int lvt_8_1_ = MathHelper.ceil((float) width / lvt_7_1_);
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            GL11.glPushMatrix();
            BlurUtils.blurAreaRounded(0, scaledResolution.getScaledHeight() + Hotbar.render2 - 28F, lvt_8_1_ + 8F, scaledResolution.getScaledHeight() - 28F,hud.getChatShadowvalue().get(),hud.getChatBlurvalue().get());
            GL11.glPopMatrix();



        }
    }
}
