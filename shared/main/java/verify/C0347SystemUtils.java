package verify;

import java.awt.*;

/* renamed from: net.ccbluex.liquidbounce.utils.SystemUtils.SystemUtils */
/* loaded from: New1.jar:net/ccbluex/liquidbounce/utils/SystemUtils/SystemUtils.class */
public class C0347SystemUtils {
    public static boolean main(String Title, String Text, TrayIcon.MessageType type) throws AWTException {
        if (SystemTray.isSupported()) {
            new C0347SystemUtils();
            displayTray(Title, Text, type);
            return false;
        }
        return false;
    }

    public static void displayTray(String Title, String Text, TrayIcon.MessageType type) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("icon.png"), "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(Title, Text, type);
    }
}
