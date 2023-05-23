package tomk.main.gui.utils;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.minecraft.client.Minecraft;
import oh.yalan.NativeClass;
import tomk.render.AnimationUtil;

@NativeClass

public class SpectrumUtil implements Listenable {

    public SpectrumUtil(){
        LiquidBounce.eventManager.registerListener(this);
    }
    float[] spectrum;

    public void updateSpectrum(float[] spectrum) {
        if (this.spectrum != null){
            for (int i = 0; i < spectrum.length; i++) {
                // 如果少于给定值就更新
                if (this.spectrum[i] < spectrum[i])
                this.spectrum[i] = AnimationUtil.moveUD(this.spectrum[i],spectrum[i],30f / Minecraft.getDebugFPS(),28f / Minecraft.getDebugFPS());
            }
        }else {
            this.spectrum = spectrum;
        }
    }

    // 获取频谱
    public float[] getSpectrum() {
        if (spectrum != null) {
            return spectrum;
        } else {
            return new float[]{0f};
        }
    }

    @EventTarget
    public void onRender(Render2DEvent e){
        if (spectrum != null) {
            for (int i = 0; i < spectrum.length; i++) {
                // 慢慢的把频谱降到0
                spectrum[i] = AnimationUtil.moveUD(spectrum[i], 0, 2f / Minecraft.getDebugFPS(), 1f / Minecraft.getDebugFPS());
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return false;
    }
}
