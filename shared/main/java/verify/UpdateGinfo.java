package verify;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;

import java.util.Objects;

import static net.ccbluex.liquidbounce.utils.MinecraftInstance.mc;

public class UpdateGinfo implements Listenable {
    private final MSTimer connectTimer = new MSTimer();
    private final MSTimer connectTimer2 = new MSTimer();

    private boolean firstuse = true;

    @Override
    public boolean handleEvents() {
        return true;
    }

    private static void update() throws Exception {
        String server = "SinglePlayer";
        if (!mc.isSingleplayer()){
            server = Objects.requireNonNull(mc.getCurrentServerData()).getServerIP();
        }
        MySQLDemo.Update14(Objects.requireNonNull(mc.getThePlayer()).getName(),server);

    }

    @EventTarget
    public void onUpdate(UpdateEvent event) throws Exception {
        if (firstuse){
            update();
            firstuse = false;
        }
        if (connectTimer2.hasTimePassed(300000 )){
            ClientUtils.displayChatMessage("当前"+ LiquidBounce.CLIENT_NAME +"白名单\n" + MySQLDemo.X2Gname());
            connectTimer2.reset();
        }
        if (connectTimer.hasTimePassed(30000)){
            update();
            connectTimer.reset();
        }
    }
}
