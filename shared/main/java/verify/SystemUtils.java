package verify;

import java.awt.*;
import java.awt.TrayIcon.MessageType;

public class SystemUtils {

    public static boolean main(String Title, String Text, MessageType type) throws AWTException {
        if (SystemTray.isSupported()) {
            new SystemUtils();
            displayTray(Title, Text, type);
            return false;
        } else {
            System.err.println("LiquidBounce By CCBlueX");
            return false;
        }
    }

    public static void displayTray(String Title, String Text, MessageType type) throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");

        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);
        trayIcon.displayMessage(Title, Text, type);
    }
}
