package verify;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Test extends MinecraftInstance implements Listenable {
    @Override
    public boolean handleEvents() {
        return true;
    }
    private final MSTimer connectTimer = new MSTimer();
    private boolean firstuse = true;

    private static void check() throws Exception {
        assert MySQLDemo.getname != null;
        if (MySQLDemo.x2onlineinfo()){
            JOptionPane.showMessageDialog(null,"管理员要求你强制下线","Error", JOptionPane.WARNING_MESSAGE);
            mc.shutdown();
        }

    }

    @EventTarget
    public String onUpdate(UpdateEvent event) throws Exception {
        if (firstuse){
            check();
            firstuse = false;
        }
        if(connectTimer.hasTimePassed(60000)) {
            check();
            connectTimer.reset();

        }
        return null;
    }
}