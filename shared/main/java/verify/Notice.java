package verify;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import verify.HttpUtil;

import java.io.IOException;

public class Notice extends MinecraftInstance implements Listenable {
    private final MSTimer connectTimer = new MSTimer();
    private boolean notice = true;

    public String url = HttpUtil.webget("https://tomk.oss-cn-hangzhou.aliyuncs.com//notice.txt");

    public Notice() throws IOException {
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
    private void check(){
        ClientUtils.displayChatMessage(url);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (notice){
            check();
            notice = false;
        }else {
            if (connectTimer.hasTimePassed(600000)) {
                check();
                connectTimer.reset();
            }
        }
    }
}
