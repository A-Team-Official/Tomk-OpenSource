package tomk.Hotbar;

import net.ccbluex.liquidbounce.LiquidBounce;

import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.feng.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import tomk.ColorManager;

import java.awt.*;

public class Hotbar {
    static HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
    public static void render(ScaledResolution sr, int itemX, float partialTicks) {
        if (hud.getColorItem().get()) {
            RenderUtils.drawRect(itemX, sr.getScaledHeight() - 23, itemX + 22, sr.getScaledHeight() - 21, ColorManager.astolfoRainbow(0, 0, 0));
        }
    }

    public static void drawGuiBackground(double s) {
        if (s <= 0.1 || s > 1.0) return;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        if (!hud.getBlurValue().get())
            BlurUtils.blurArea(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), (float) (70.0 * s));
        RenderUtils.drawGradientSidewaysV(-4 * scaledResolution.getScaledHeight(), scaledResolution.getScaledHeight() / 2, scaledResolution.getScaledWidth() * 4, scaledResolution.getScaledHeight() + 150 + (2 * scaledResolution.getScaledHeight() * (1 - s)), new Color(0, 0, 0, 0).getRGB(), new Color(0, 165, 255, (int) (255 * s)).getRGB());
        FontLoaders.C24.drawCenteredString(LiquidBounce.CLIENT_NAME + " CN", scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() - 40.0, new Color(255, 255, 255, (int) (255 * s)).getRGB());
        GL11.glPopMatrix();
    }
}
