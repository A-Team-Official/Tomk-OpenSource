/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.feng.FontLoaders;
import net.ccbluex.liquidbounce.utils.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tomk.render.AnimationUtil;
import tomk.render.Hotbar;
import tomk.render.RoundedUtil;

import java.awt.*;
import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    public double slide, progress = 0;

    @Shadow
    @Final
    private Minecraft mc;
    int animationY = 0;


    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;
    @Shadow
    private int scrollPos;
    @Shadow

    private boolean isScrolled;
    @Shadow
    @Final
    private List<ChatLine> chatLines;
    @Shadow
    public abstract int getLineCount();
    float render1 = 0.0f;
    @Shadow
    public abstract boolean getChatOpen();
    @Shadow
    public abstract float getChatScale();
    @Shadow
    public abstract int getChatWidth();
    @Shadow
    public abstract void deleteChatLine(int p_deleteChatLine_1_);
    @Shadow
    public abstract void scroll(int p_scroll_1_);



    @Overwrite
    public void drawChat(int p_drawChat_1_) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int lvt_2_1_ = getLineCount();
            int lvt_11_1_2;
            int lvt_11_1_;

            boolean lvt_3_1_ = false;
            int lvt_4_1_ = 0;
            int lvt_5_1_ = this.drawnChatLines.size();
            float lvt_6_1_ = (this.mc.gameSettings.chatOpacity  * 0.9f) + 0.1f;
            if (lvt_5_1_ > 0) {
                if (getChatOpen()) {
                    lvt_3_1_ = true;
                }
                float lvt_7_1_ = getChatScale();
                int lvt_8_1_ = MathHelper.ceil((float) this.getChatWidth() / lvt_7_1_);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(lvt_7_1_, lvt_7_1_, 1.0F);
                int SB = 0;
                for (int lvt_9_1_ = 0; lvt_9_1_ + this.scrollPos < this.drawnChatLines.size() && lvt_9_1_ < lvt_2_1_; lvt_9_1_++) {
                    ChatLine lvt_10_1_ = this.drawnChatLines.get(lvt_9_1_ + this.scrollPos);
                    if (lvt_10_1_ != null && ((lvt_11_1_2 = p_drawChat_1_ - lvt_10_1_.getUpdatedCounter()) < 200 || lvt_3_1_)) {
                        double lvt_12_1_ = MathHelper.clamp((1.0d - (lvt_11_1_2 / 200.0d)) * 10.0d, 0.0d, 1.0d);
                        int lvt_14_1_ = (int) (255.0d * lvt_12_1_ * lvt_12_1_);
                        if (lvt_3_1_) {
                            lvt_14_1_ = 255;
                        }
                        lvt_4_1_++;
                        SB = lvt_9_1_ + 1;
                        if (((int) (lvt_14_1_ * lvt_6_1_)) > 3) {
                        }
                    }
                }

                if (this.render1 != SB * 9) {
                    this.render1 = AnimationUtil.animate(SB * 9, this.render1, 0.016f * RenderUtils.deltaTime);
                }
                if (Hotbar.render2 != (-SB) * 9) {
                    Hotbar.render2 = AnimationUtil.animate((-SB) * 9, Hotbar.render2, 0.016f * RenderUtils.deltaTime);
                }

                RoundedUtil.drawRound(0F,Hotbar.render2,lvt_8_1_+4,0F,20F,true, new Color(0, 0, 0, 50));
                RenderUtils.drawRoundGradientSideways2( 0F,  Hotbar.render2 ,lvt_8_1_ + 6, 0,hud.getRadius().get(),new Color(hud.getR().get(),hud.getG().get(),hud.getB().get(),hud.getA().get()).getRGB(),new Color(hud.getR2().get(),hud.getG2().get(),hud.getB2().get(),hud.getA().get()).getRGB());
                ScaledResolution scaledresolution = new ScaledResolution(this.mc);
                int j = scaledresolution.getScaledHeight();
                RenderUtils.startGlScissor(2, (int) ((j - 28) + Hotbar.render2), lvt_8_1_ + 10, (int) this.render1);
                for (int lvt_9_1_2 = 0; lvt_9_1_2 + this.scrollPos < this.drawnChatLines.size() && lvt_9_1_2 < lvt_2_1_; lvt_9_1_2++) {
                    ChatLine lvt_10_1_2 = this.drawnChatLines.get(lvt_9_1_2 + this.scrollPos);
                    if (lvt_10_1_2 != null && ((lvt_11_1_ = p_drawChat_1_ - lvt_10_1_2.getUpdatedCounter()) < 200 || lvt_3_1_)) {
                        double lvt_12_1_2 = MathHelper.clamp((1.0d - (lvt_11_1_ / 200.0d)) * 10.0d, 0.0d, 1.0d);
                        int lvt_14_1_2 = (int) (255.0d * lvt_12_1_2 * lvt_12_1_2);
                        if (lvt_3_1_) {
                            lvt_14_1_2 = 255;
                        }
                        int i = (int) (lvt_14_1_2 * lvt_6_1_);
                        lvt_4_1_++;
                        int lvt_16_1_ = (-lvt_9_1_2) * 9;
                        int lvt_15_1_ = 0;

                        String lvt_17_1_ = lvt_10_1_2.getChatComponent().getFormattedText();

                        FontLoaders.C16.drawString(lvt_17_1_, 4, lvt_16_1_ - 8, 16777215 + (lvt_14_1_2 << 24));
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        GlStateManager.resetColor();
                    }
                }

                RenderUtils.stopGlScissor();
                if (lvt_3_1_) {
                    int lvt_9_1_3 = FontLoaders.C16.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    int lvt_10_2_ = (lvt_5_1_ * lvt_9_1_3) + lvt_5_1_;
                    int lvt_11_1_3 = (lvt_4_1_ * lvt_9_1_3) + lvt_4_1_;
                    int lvt_12_2_ = (this.scrollPos * lvt_11_1_3) / lvt_5_1_;
                    int lvt_13_1_ = (lvt_11_1_3 * lvt_11_1_3) / lvt_10_2_;
                    if (lvt_10_2_ != lvt_11_1_3) {
                        int lvt_14_1_3 = lvt_12_2_ > 0 ? 170 : 96;
                        int lvt_15_2_ = this.isScrolled ? 13382451 : 3355562;
                        Gui.drawRect(0, -lvt_12_2_, 2, (-lvt_12_2_) - lvt_13_1_, lvt_15_2_ + (lvt_14_1_3 << 24));
                        Gui.drawRect(2, -lvt_12_2_, 1, (-lvt_12_2_) - lvt_13_1_, 13421772 + (lvt_14_1_3 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }




    // TODO: Make real fix
    /*@Inject(method = "setChatLine", at = @At("HEAD"), cancellable = true)
    private void setChatLine(IChatComponent p_setChatLine_1_, int p_setChatLine_2_, int p_setChatLine_3_, boolean p_setChatLine_4_, final CallbackInfo callbackInfo) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

        if(hud.getState() && hud.fontChatValue.asBoolean()) {
            callbackInfo.cancel();

            if (p_setChatLine_2_ != 0) {
                this.deleteChatLine(p_setChatLine_2_);
            }

            int lvt_5_1_ = MathHelper.floor_float((float)this.getChatWidth() / this.getChatScale());
            List<IChatComponent> lvt_6_1_ = GuiUtilRenderComponents.splitText(p_setChatLine_1_, lvt_5_1_, Fonts.font40, false, false);
            boolean lvt_7_1_ = this.getChatOpen();

            IChatComponent lvt_9_1_;
            for(Iterator lvt_8_1_ = lvt_6_1_.iterator(); lvt_8_1_.hasNext(); this.drawnChatLines.add(0, new ChatLine(p_setChatLine_3_, lvt_9_1_, p_setChatLine_2_))) {
                lvt_9_1_ = (IChatComponent)lvt_8_1_.next();
                if (lvt_7_1_ && this.scrollPos > 0) {
                    this.isScrolled = true;
                    this.scroll(1);
                }
            }

            while(this.drawnChatLines.size() > 100) {
                this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
            }

            if (!p_setChatLine_4_) {
                this.chatLines.add(0, new ChatLine(p_setChatLine_3_, p_setChatLine_1_, p_setChatLine_2_));

                while(this.chatLines.size() > 100) {
                    this.chatLines.remove(this.chatLines.size() - 1);
                }
            }
        }
    }*/

    @Inject(method = "getChatComponent", at = @At("HEAD"), cancellable = true)
    private void getChatComponent(int p_getChatComponent_1_, int p_getChatComponent_2_, final CallbackInfoReturnable<ITextComponent> callbackInfo) {
        final HUD hud = (HUD) LiquidBounce.moduleManager.getModule(HUD.class);

        if (hud.getState() && hud.getFontChatValue().get()) {
            if (this.getChatOpen()) {
                ScaledResolution lvt_3_1_ = new ScaledResolution(this.mc);
                int lvt_4_1_ = lvt_3_1_.getScaleFactor();
                float lvt_5_1_ = this.getChatScale();
                int lvt_6_1_ = p_getChatComponent_1_ / lvt_4_1_ - 3;
                int lvt_7_1_ = p_getChatComponent_2_ / lvt_4_1_ - 27;
                lvt_6_1_ = MathHelper.floor((float) lvt_6_1_ / lvt_5_1_);
                lvt_7_1_ = MathHelper.floor((float) lvt_7_1_ / lvt_5_1_);
                if (lvt_6_1_ >= 0 && lvt_7_1_ >= 0) {
                    int lvt_8_1_ = Math.min(this.getLineCount(), this.drawnChatLines.size());
                    if (lvt_6_1_ <= MathHelper.floor((float) this.getChatWidth() / this.getChatScale()) && lvt_7_1_ < FontLoaders.C16.FONT_HEIGHT * lvt_8_1_ + lvt_8_1_) {
                        int lvt_9_1_ = lvt_7_1_ / FontLoaders.C16.FONT_HEIGHT + this.scrollPos;
                        if (lvt_9_1_ >= 0 && lvt_9_1_ < this.drawnChatLines.size()) {
                            ChatLine lvt_10_1_ = this.drawnChatLines.get(lvt_9_1_);
                            int lvt_11_1_ = 0;

                            for (ITextComponent lvt_13_1_ : lvt_10_1_.getChatComponent()) {
                                if (lvt_13_1_ instanceof TextComponentString) {
                                    lvt_11_1_ += FontLoaders.C16.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString) lvt_13_1_).getText(), false));
                                    if (lvt_11_1_ > lvt_6_1_) {
                                        callbackInfo.setReturnValue(lvt_13_1_);
                                        return;
                                    }
                                }
                            }
                        }

                    }
                }

            }

            callbackInfo.setReturnValue(null);
        }
    }
}
