package tomk.main.gui.ui;

import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.feng.FontLoaders;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.*;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import oh.yalan.NativeClass;
import org.lwjgl.opengl.GL11;
import tomk.main.gui.MusicManager;
import tomk.render.DrawArc;

import java.awt.*;

import static tomk.SuperLib.reAlpha;

@NativeClass

public enum MusicOverlayRenderer {
    INSTANCE;

    public String downloadProgress = "0";

    public long readedSecs = 0;
    public long totalSecs = 0;

    public float animation = 0;

    public MSTimer timer = new MSTimer();

    public boolean firstTime = true;

    double x2 = 0F;

    public void renderOverlay() {
        int addonX = 10;
        int addonY = 50;
        float x1 = addonX + Fonts.Chinese18.getStringWidth(MusicManager.INSTANCE.lrcCur) + 180F;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (x1 > 200){
            x2 = RenderUtils.getAnimationState2(x2,x1,400);
        }else {
            x2 = RenderUtils.getAnimationState2(x2,200,400);

        }



        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            readedSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds();
            totalSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds();
        }

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            RenderUtils.drawRoundedRect( (float) (addonX),  (float)(addonY+8+2),(float)(x2),addonY+8+2 + 40F,4F,new Color(32, 30, 30).getRGB());

            Fonts.Chinese16.drawCenteredString( MusicManager.INSTANCE.getCurrentTrack().name , (x1 / 2F)+3F, addonY+15+6, Color.WHITE.getRGB());
            Fonts.Chinese18.drawCenteredString(  MusicManager.INSTANCE.lrcCur,  (x1/2F)+3F, addonY+15+6+1 + Fonts.Chinese18.getHeight() + 2F, 0xffffffff);

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                final IResourceLocation icon = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation(MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id).toString());
                RenderUtils.drawImage(icon, 4 + addonX,   addonY, 29, 29);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }

            try {
                float currentProgress = (float) (MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / Math.max(1, MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds())) * 100;
                DrawArc.drawArc(18 + addonX, 15 + addonY, 16, Color.WHITE.getRGB(), 0, 360, 4);
                DrawArc.drawArc(18 + addonX, 15 + addonY, 16, Color.BLUE.getRGB(), 180, 180 + (currentProgress * 3.6f), 4);
            } catch (Exception ignored) {
            }
        }



        if ((MusicManager.showMsg)) {
            if (firstTime) {
                timer.reset();
                firstTime = false;
            }

            FontRenderer wqy = Minecraft.getMinecraft().fontRenderer;
            FontRenderer sans = Minecraft.getMinecraft().fontRenderer;

            float width1 = wqy.getStringWidth(MusicManager.INSTANCE.getCurrentTrack().name);
            float width2 = sans.getStringWidth("Now playing");
            float allWidth = (Math.max(Math.max(width1, width2), 150));

            RenderUtils.drawRect(sr.getScaledWidth() - animation, 5, sr.getScaledWidth(), 40, reAlpha(Color.BLACK.getRGB(), 0.7f));

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                final IResourceLocation icon = WrapperImpl.INSTANCE.getClassProvider().createResourceLocation(MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id).toString());
                RenderUtils.drawImage((IResourceLocation) icon, (int) (sr.getScaledWidth() - animation + 5), 8, 28, 28);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }

            DrawArc.drawArc(sr.getScaledWidth() - animation - 31 + 50, 22, 14, Color.WHITE.getRGB(), 0, 360, 2);

            sans.drawString("Now playing", (int) (sr.getScaledWidth() - animation - 12 + 50), 8, Color.WHITE.getRGB());
            wqy.drawString(MusicManager.INSTANCE.getCurrentTrack().name, (int) (sr.getScaledWidth() - animation - 12 + 50), 26, Color.WHITE.getRGB());

            if (timer.hasTimePassed(5000)) {
                this.animation = (float) RenderUtils.getAnimationStateSmooth(0, animation, 10.0f / Minecraft.getDebugFPS());
                if (this.animation <= 0) {
                    MusicManager.showMsg = false;
                    firstTime = true;
                }
            } else {
                this.animation = (float) RenderUtils.getAnimationStateSmooth(allWidth, animation, 10.0f / Minecraft.getDebugFPS());
            }

        }

        GlStateManager.resetColor();
    }

    public String formatSeconds(int seconds) {
        String rstl = "";
        int mins = seconds / 60;
        if (mins < 10) {
            rstl += "0";
        }
        rstl += mins + ":";
        seconds %= 60;
        if (seconds < 10) {
            rstl += "0";
        }
        rstl += seconds;
        return rstl;
    }
}
