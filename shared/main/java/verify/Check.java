package verify;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import verify.HttpUtil;

import javax.swing.*;
import java.io.IOException;

public class Check extends MinecraftInstance implements Listenable  {

    public boolean handleEvents() {
        return true;
    }
    private final MSTimer connectTimer = new MSTimer();

    private void check() throws IOException {
        String url = HttpUtil.webget("https://tomk.oss-cn-hangzhou.aliyuncs.com/check.txt");
        if (url.contains("YES")) {
            System.out.println("OK");
        } else {
            JOptionPane.showMessageDialog(null, "管理员要求全员下线");
            mc.shutdown();
            System.exit(0);

        }

    }
    @EventTarget
    public String onUpdate(UpdateEvent event) throws IOException {
        if(connectTimer.hasTimePassed(180000)) {
            check();
            connectTimer.reset();
    }
        return null;
    }
}





