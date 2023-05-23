package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import tomk.main.gui.ui.GuiCloudMusic;

@ModuleInfo(name = "MusicPlayer",description = "CNM",category = ModuleCategory.RENDER,canEnable = false)
public class MusicPlayer extends Module {

    public MusicPlayer(){

    }
    @Override
    public void onEnable(){
        mc2.displayGuiScreen(new GuiCloudMusic());
        super.onEnable();
    }

}
