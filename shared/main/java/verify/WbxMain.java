package verify;


import net.ccbluex.liquidbounce.LiquidBounce;
import tomk.render.EnumChatFormatting;


public class WbxMain {
    public static String Name = LiquidBounce.CLIENT_NAME;
    public static String Group = "";
    public static String version = "";
    public static String username;

    private static WbxMain INSTANCE;
    public static boolean isCheck = false;



    public static WbxMain getInstance() {
        try {
            if (INSTANCE == null) INSTANCE = new WbxMain();
            return INSTANCE;
        } catch (Throwable t) {
            //    ClientUtils.getLogger().warn(t);
            throw t;
        }
    }

    public static boolean setcheck(Boolean i){
        return isCheck = i;

    }

    public static int cheakuser() {
        String checkGroup = MySQLDemo.X2Group();
        int cheak = 0;
        try {
            if (checkGroup.equals("USER")){
                WbxMain.Group = EnumChatFormatting.GREEN + "USER";
                cheak = 0;
                return cheak;
            }
            if (checkGroup.equals("Contributor")) {
                WbxMain.Group = EnumChatFormatting.YELLOW + "Contributor";
                cheak = 1;
                return cheak;
            }
            if (checkGroup.equals("DEV")) {
                WbxMain.Group = EnumChatFormatting.YELLOW + "DevTeam";
                cheak = 2;
                return cheak;
            }
            if (checkGroup.equals("Tester")) {
                WbxMain.Group = EnumChatFormatting.RED + "Tester";
                cheak = 3;

                return cheak;
            }
        } catch (Exception exception) {
            System.out.println("Error");
            WbxMain.Group = EnumChatFormatting.GREEN + "USER";
            cheak = 0;
            return cheak;
        }
        WbxMain.Group = EnumChatFormatting.GREEN + "USER";
        cheak = 0;
        return cheak;
    }
}