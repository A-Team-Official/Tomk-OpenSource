package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotion;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import tomk.render.DrawArc;
import tomk.render.HanaBiColors;

import java.awt.*;
import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.max;


@ElementInfo(
        name = "Effects"
)
public class Effects  extends Element {
    private final Map<Integer, Integer> potionMaxDurations = new HashMap<>();

    Map<IPotion, Double> timerMap = new HashMap<>();
    int animationY = 0;
    int animationX = 0;
    float rectX = 0F;
    float giveX = 0F;

    @Override
    public Border drawElement() {
        GlStateManager.pushMatrix();
        final int tempY = mc.getThePlayer().getActivePotionEffects().size() * 18;
        if (mc.getThePlayer().getActivePotionEffects().size() == 0){
            rectX = 0F;
        }


        final ArrayList<Float> list = new ArrayList<Float>();


        int y = 2;


        animationY = (int) RenderUtils.getAnimationState2(animationY,1.2F*tempY,350);
        animationX = (int) RenderUtils.getAnimationState2(animationX,1.2F*rectX,350);

        RenderUtils.drawRoundedRect(-5F,-5F,animationX  + 2F +  Fonts.font35.getStringWidth("Effects"),animationY  + Fonts.font35.getFontHeight(),2F, new Color(32, 30, 30).getRGB());
        Fonts.font35.drawString("Effects",0F,0F,Color.WHITE.getRGB());

        final ArrayList<Integer> needRemove = new ArrayList<Integer>();
        for (final Map.Entry<Integer, Integer> entry : this.potionMaxDurations.entrySet()) {
            if (mc.getThePlayer().getActivePotionEffect(functions.getPotionById(entry.getKey())) == null) {
                needRemove.add(entry.getKey());
            }
        }
        for (final int id : needRemove) {
            this.potionMaxDurations.remove(id);
        }


        for (final IPotionEffect effect : mc.getThePlayer().getActivePotionEffects()) {
            if (!this.potionMaxDurations.containsKey(effect.getPotionID()) || this.potionMaxDurations.get(effect.getPotionID()) < effect.getDuration()) {
                this.potionMaxDurations.put(effect.getPotionID(), effect.getDuration());
            }
            final IPotion potion = functions.getPotionById(effect.getPotionID());
            final String PType = functions.formatI18n(potion.getName(), Arrays.toString(new Object[0]));
            giveX = Fonts.font35.getStringWidth(PType + " " + this.intToRomanByGreedy(effect.getAmplifier() + 1) + effect.getDurationString()) + 8F;
            list.add(giveX);
            rectX = Collections.max(list);
            int minutes;
            int seconds;
            try {
                minutes = Integer.parseInt(effect.getDurationString().split(":")[0]);
                seconds = Integer.parseInt(effect.getDurationString().split(":")[1]);
            } catch (Exception ex) {
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
            float posY = y + 20.0F;
            Fonts.font35.drawString(PType + " " + this.intToRomanByGreedy(effect.getAmplifier() + 1), 18.0F, posY - (float) Effects.mc.getFontRendererObj().getFontHeight(), ClientUtils.reAlpha(HanaBiColors.WHITE.c, 0.8F));
            Fonts.font35.drawString(effect.getDurationString(), 20.0F + Fonts.font35.getStringWidth(PType + " " + this.intToRomanByGreedy(effect.getAmplifier() + 1)), posY - (float) Effects.mc.getFontRendererObj().getFontHeight(), ClientUtils.reAlpha((new Color(200, 200, 200)).getRGB(), 0.5F));
            DrawArc.drawArc((animationX  + 2F +  Fonts.font35.getStringWidth("Effects"))-16,posY - (float) Effects.mc.getFontRendererObj().getFontHeight()+2,6, new Color(0, 95, 255).getRGB(),0,360.0f * (effect.getDuration() / (1.0f * this.potionMaxDurations.get(effect.getPotionID()))),4.6F );

            int position = y + 5;
            if (potion.getHasStatusIcon()) {
                GlStateManager.pushMatrix();
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glDepthMask(false);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                int statusIconIndex = potion.getStatusIconIndex();

                Effects.mc.getTextureManager().bindTexture(Effects.classProvider.createResourceLocation("textures/gui/container/inventory.png"));
                Effects.mc2.ingameGUI.drawTexturedModalRect(-2.0F, (float) (position), statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glEnable(2929);
                GlStateManager.popMatrix();
            }
            y += 20;
        }
        GlStateManager.popMatrix();
        return new Border(0.0F, 0.0F, 120, 30F);
    }
    private String intToRomanByGreedy(int num) {
        int[] values = new int[] { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = new String[] { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < values.length && num >= 0; ++i) {
            while (values[i] <= num) {
                num -= values[i];
                stringBuilder.append(symbols[i]);
            }
        }

        return stringBuilder.toString();
    }
}
