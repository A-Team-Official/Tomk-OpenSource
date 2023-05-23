package net.ccbluex.liquidbounce.features.module.modules.tomk;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ModuleInfo(name = "PotionRender" , description = "Vape" ,category = ModuleCategory.RENDER)
public class PotionRender extends Module {
    private final Map<Integer, Integer> potionMaxDurations = new HashMap<>();
    Map<IPotion, Double> timerMap = new HashMap<>();
    private int x;

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        IScaledResolution sr = classProvider.createScaledResolution(mc);
        float width = sr.getScaledWidth();
        float height = sr.getScaledHeight();
        this.renderPotionStatusNew((int) width, (int) height);
    }

    public void renderPotionStatusNew(final int width, final int height) {
            this.x = 0;
            final int length;
            final int tempY = (length = HUD.mc.getThePlayer().getActivePotionEffects().size()) * -30;
            if (length != 0) {
                RenderUtils.drawRoundedRect(width - 120, height - 30 + tempY, width - 10, height - 30, 3, new Color(0, 0, 0, 100).getRGB());
            }
            final ArrayList<Integer> needRemove = new ArrayList<Integer>();
            for (final Map.Entry<Integer, Integer> entry : this.potionMaxDurations.entrySet()) {
                if (HUD.mc.getThePlayer().getActivePotionEffect(functions.getPotionById(entry.getKey())) == null) {
                    needRemove.add(entry.getKey());
                }
            }
            for (final int id : needRemove) {
                this.potionMaxDurations.remove(id);
            }
            for (final IPotionEffect effect : HUD.mc.getThePlayer().getActivePotionEffects()) {
                if (!this.potionMaxDurations.containsKey(effect.getPotionID()) || this.potionMaxDurations.get(effect.getPotionID()) < effect.getDuration()) {
                    this.potionMaxDurations.put(effect.getPotionID(), effect.getDuration());
                }
                final IPotion potion = functions.getPotionById(effect.getPotionID());
                final String PType = functions.formatI18n(potion.getName(), Arrays.toString(new Object[0]));
                int minutes;
                int seconds;
                try {
                    minutes = Integer.parseInt(effect.getDurationString().split(":")[0]);
                    seconds = Integer.parseInt(effect.getDurationString().split(":")[1]);
                }
                catch (Exception ex) {
                    minutes = 0;
                    seconds = 0;
                }
                final double total = minutes * 60 + seconds;
                if (!this.timerMap.containsKey(potion)) {
                    this.timerMap.put(potion, total);
                }
                if (this.timerMap.get(potion) == 0.0 || total > this.timerMap.get(potion)) {
                    this.timerMap.replace(potion, total);
                }
                final int color = Colors.blendColors(new float[] { 0.0f, 0.5f, 1.0f }, new Color[] { new Color(250, 50, 56), new Color(236, 129, 44), new Color(5, 134, 105) }, effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID()))).getRGB();
                final int x1 = (int)((width - 6) * 1.33f);
                final int y1 = (int)((height - 52 - HUD.mc.getFontRendererObj().getFontHeight() + this.x + 5) * 1.33f);
                final float rectX = width - 120 + 110.0f * (effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID())));
                if (potion.getHasStatusIcon()) {
                    classProvider.getGlStateManager().pushMatrix();
                    GL11.glDisable(2929);
                    GL11.glEnable(3042);
                    GL11.glDepthMask(false);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    final int index = potion.getStatusIconIndex();
                    final IResourceLocation location = classProvider.createResourceLocation("textures/gui/container/inventory.png");
                    HUD.mc.getTextureManager().bindTexture(location);
                    GlStateManager.scale(0.75, 0.75, 0.75);
                    mc2.ingameGUI.drawTexturedModalRect(x1 - 138, y1 + 8, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                    GL11.glDepthMask(true);
                    GL11.glDisable(3042);
                    GL11.glEnable(2929);
                    GlStateManager.popMatrix();
                }
                final int y2 = height - HUD.mc.getFontRendererObj().getFontHeight() + this.x - 38;
                RenderUtils.drawArc(width - 104.75f, y2 + 2.5f, 10.0, new Color(22, 28, 15).getRGB(), 0, 360.0, 3);
                RenderUtils.drawArc(width - 104.75f, y2 + 2.5f, 10.0, color, 0, 360.0f * (effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID()))), 3);
                Fonts.font35.drawString(PType.replaceAll("§.", ""), width - 85.0f, y2 - HUD.mc.getFontRendererObj().getFontHeight() + 2, -1);
                RenderUtils.drawRect(width - 91.0f, y2 - 3.0f, width - 89.5f, y2 + 10.0f, new Color(255, 255, 255, 100).getRGB());
                Fonts.font35.drawString(effect.getDurationString().replaceAll("§.", ""), width - 85.0f, y2 + 6.5f, color);
                this.x -= 30;
        }
    }
}
