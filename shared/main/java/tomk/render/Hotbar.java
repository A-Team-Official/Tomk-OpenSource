package tomk.render;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.feng.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import tomk.ColorManager;
import tomk.Hotbar.BlurUtils;

import java.awt.*;
import java.util.List;

public class Hotbar {
    public static float render2;
    static GuiNewChat guiNewChat = new GuiNewChat(Minecraft.getMinecraft());
    static float width = guiNewChat.getChatWidth();
    @Final
    static List<ChatLine> drawnChatLines;
    public static float x = 0F;
    public static float y = 0F;
    static HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    public static void render(ScaledResolution sr, int itemX, float partialTicks) {
        if (hud.getColorItem().get()) {
            RenderUtils.drawRect(itemX, sr.getScaledHeight() - 23, itemX + 22, sr.getScaledHeight() - 21, ColorManager.astolfoRainbow(0, 0, 0));
        }
    }
    public static void BlurHelper(){
            float lvt_7_1_ = guiNewChat.getChatScale();
            int lvt_8_1_ = MathHelper.ceil((float) width / lvt_7_1_);
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        BlurUtils.blurArea(0, 0, (scaledResolution.getScaledWidth() / 2F) - lvt_8_1_ + 6F , scaledResolution.getScaledHeight()-Hotbar.render2, 10F);
        GL11.glPopMatrix();





    }


    public static void drawGuiBackground(double s) {

        if (s <= 0.1 || s > 1.0) return;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        if (!hud.getBlurValue().get())
            BlurUtils.blurArea(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), (float) (70.0 * s));
        RenderUtils.drawGradientSidewaysV(-4 * scaledResolution.getScaledHeight(), scaledResolution.getScaledHeight() / 2, scaledResolution.getScaledWidth() * 4, scaledResolution.getScaledHeight() + 150 + (2 * scaledResolution.getScaledHeight() * (1 - s)), new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, (int) (255 * s)).getRGB());
        FontLoaders.C24.drawCenteredString(LiquidBounce.CLIENT_NAME , scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() - 40.0, new Color(255, 255, 255, (int) (255 * s)).getRGB());
        GL11.glPopMatrix();
    }
}
