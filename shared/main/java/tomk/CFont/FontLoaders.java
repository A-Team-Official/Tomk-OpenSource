//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "D:\����2\MCP 1.8.9 (1)\MCP 1.8.9\mcp918"!

//Decompiled by Procyon!

package tomk.CFont;


import net.ccbluex.liquidbounce.LiquidBounce;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public  class FontLoaders
{
    public static CFontRenderer Sans12;
    public static CFontRenderer Sans14;
    public static CFontRenderer Sans16;
    public static CFontRenderer Sans18;
    public static CFontRenderer Sans20;
    public static CFontRenderer PoppinsSemiBold16;
    public static CFontRenderer PoppinsSemiBold18;
    public static CFontRenderer PoppinsSemiBold20;
    public static CFontRenderer SFREGULAR12;
    public static CFontRenderer SFREGULAR14;
    public static CFontRenderer SFREGULAR16;
    public static CFontRenderer SFREGULAR18;
    public static CFontRenderer productsans16;
    public static CFontRenderer productsans18;
    public static CFontRenderer BoldFont12;
    public static CFontRenderer BoldFont8;
    public static CFontRenderer poppins14;
    public static CFontRenderer poppins15;
    public static CFontRenderer poppins16;
    public static CFontRenderer poppins18;
    public static CFontRenderer BoldFont14;
    public static CFontRenderer BoldFont16;
    public static CFontRenderer BoldFont10;
    public static CFontRenderer BoldFont18;
    public static CFontRenderer BoldFont20;
    public static CFontRenderer BoldFont30;
    public static CFontRenderer SFREGULAR25;
    public static CFontRenderer Sans25;
    public static CFontRenderer Sans35;
    public static CFontRenderer font40;
    public static CFontRenderer ETB20;
    public static CFontRenderer NovIcon20;
    public static CFontRenderer FluxIcon14;
    public static CFontRenderer FluxIcon16;
    public static CFontRenderer FluxIcon18;
    public static CFontRenderer FluxIcon20;
    public static CFontRenderer FluxIcon30;
    public static CFontRenderer FluxIcon40;
    public static CFontRenderer FluxIcon50;
    public static CFontRenderer Icon16;
    public static CFontRenderer JelloTitle20;
    public static CFontRenderer JelloTitle18;
    public static CFontRenderer JelloList16;
    public static CFontRenderer JelloList40;

    public static CFontRenderer NOTIFICATIONS20;
    public static CFontRenderer NOTIFICATIONS18;
    public static CFontRenderer NOTIFICATIONS16;
    public static CFontRenderer NOTIFICATIONS30;
    public static CFontRenderer siyuan20;
    public static CFontRenderer siyuan18;
    public static CFontRenderer siyuan16;
    public static CFontRenderer siyuan30;
    public static CFontRenderer tenacitybold14;
    public static CFontRenderer tenacitybold16;
    public static CFontRenderer tenacitybold18;
    public static CFontRenderer tenacitybold20;
    public static CFontRenderer tenacitybold22;
    public static CFontRenderer tenacity14;
    public static CFontRenderer tenacity16;
    public static CFontRenderer tenacity18;
    public static CFontRenderer tenacity20;
    public static CFontRenderer tenacity22;

    public static CFontRenderer verdana20;
    public static CFontRenderer verdana18;
    public static CFontRenderer verdana14;
    public static CFontRenderer verdana16;
    public static CFontRenderer verdana30;
    public static CFontRenderer verdanab20;
    public static CFontRenderer verdanab18;
    public static CFontRenderer verdanab14;
    public static CFontRenderer verdanab16;
    public static CFontRenderer verdanab17;
    public static CFontRenderer verdanab30;
    public static CFontRenderer sf_ui_display_regular12;
    public static CFontRenderer sf_ui_display_regular14;
    public static CFontRenderer sf_ui_display_regular16;
    public static CFontRenderer sf_ui_display_regular18;
    public static CFontRenderer sf_ui_display_regular20;

    public static CFontRenderer sf_ui_display_regular35;
    public static CFontRenderer C16;
    public static CFontRenderer  C18;
    public static CFontRenderer C24;
    public static CFontRenderer sf_ui_display_regular22;
    public static CFontRenderer sf_ui_display_regular25;
    public static CFontRenderer sf_ui_display_regular28;
    public static CFontRenderer Chinese18;
    public static CFontRenderer Chinese35;
    public static CFontRenderer JelloMedium_28;
    public static CFontRenderer JelloMedium_22;
    public static CFontRenderer JelloLight_24;
    public static CFontRenderer JelloLight_30;

    public static CFontRenderer Tenatiy_24;
    public static CFontRenderer posterama18;
    public static CFontRenderer posterama35;
    public static CFontRenderer JelloM20;
    public static CFontRenderer JelloM15;
    public static CFontRenderer xyz16;
    public static CFontRenderer xyz18;
    public static CFontRenderer xyz20;
    public static CFontRenderer xyz28;

    public static CFontRenderer xyz26;
    public static ArrayList<CFontRenderer> fonts;

    public static CFontRenderer getFontRender(final int size) {
        return FontLoaders.fonts.get(size - 10);
    }
    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = Files.newInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName + ".ttf").toPath());
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }
    
    static {

        FontLoaders. xyz16 = new CFontRenderer(getFont("Arial.ttf",16), true, true);
        FontLoaders. xyz18 = new CFontRenderer(getFont("Arial.ttf",18), true, true);
        FontLoaders. xyz20 = new CFontRenderer(getFont("Arial.ttf",20), true, true);
        FontLoaders. xyz28 = new CFontRenderer(getFont("Arial.ttf",28), true, true);
        FontLoaders. xyz26 = new CFontRenderer(getFont("Arial.ttf",26), true, true);
        FontLoaders.Sans12 = new CFontRenderer(getFont("GoogleSans", 12), true, true);
        FontLoaders.Sans14 = new CFontRenderer(getFont("GoogleSans", 14), true, true);
        FontLoaders.Sans16 = new CFontRenderer(getFont("GoogleSans", 16), true, true);
        FontLoaders.Sans18 = new CFontRenderer(getFont("GoogleSans", 18), true, true);
        FontLoaders.Sans20 = new CFontRenderer(getFont("GoogleSans", 20), true, true);
        FontLoaders.Chinese18 = new CFontRenderer(getFont("Chinese",18),true,true);
        FontLoaders.Chinese35 = new CFontRenderer(getFont("Chinese",35),true,true);
        FontLoaders.PoppinsSemiBold16 = new CFontRenderer(getFont("PoppinsSemiBold", 16), true, true);
        FontLoaders.PoppinsSemiBold18 = new CFontRenderer(getFont("PoppinsSemiBold", 18), true, true);
        FontLoaders.PoppinsSemiBold20 = new CFontRenderer(getFont("PoppinsSemiBold", 20), true, true);
        FontLoaders.SFREGULAR12 = new CFontRenderer(getFont("SFREGULAR", 12), true, true);
        FontLoaders.SFREGULAR14 = new CFontRenderer(getFont("SFREGULAR", 14), true, true);
        FontLoaders.SFREGULAR16 = new CFontRenderer(getFont("SFREGULAR", 16), true, true);
        FontLoaders.SFREGULAR18 = new CFontRenderer(getFont("SFREGULAR", 18), true, true);
        FontLoaders.productsans16 = new CFontRenderer(getFont("productsans", 16), true, true);
        FontLoaders.productsans18 = new CFontRenderer(getFont("productsans", 18), true, true);
        FontLoaders.BoldFont12 = new CFontRenderer(getFont("BoldFont", 14), true, true);
        FontLoaders.BoldFont8 = new CFontRenderer(getFont("BoldFont", 8), true, true);
        FontLoaders.poppins14 = new CFontRenderer(getFont("PoppinsRegular", 14), true, true);
        FontLoaders.poppins15 = new CFontRenderer(getFont("PoppinsRegular", 15), true, true);
        FontLoaders.poppins16 = new CFontRenderer(getFont("PoppinsRegular", 16), true, true);
        FontLoaders.poppins18 = new CFontRenderer(getFont("PoppinsRegular", 18), true, true);
        FontLoaders.BoldFont14 = new CFontRenderer(getFont("BoldFont", 14), true, true);
        FontLoaders.BoldFont16 = new CFontRenderer(getFont("BoldFont", 16), true, true);
        FontLoaders.BoldFont10 = new CFontRenderer(getFont("BoldFont", 10), true, true);
        FontLoaders.BoldFont18 = new CFontRenderer(getFont("BoldFont", 18), true, true);
        FontLoaders.BoldFont20 = new CFontRenderer(getFont("BoldFont", 20), true, true);
        FontLoaders.BoldFont30 = new CFontRenderer(getFont("BoldFont", 30), true, true);
        FontLoaders.SFREGULAR25 = new CFontRenderer(getFont("SFREGULAR", 25), true, true);
        FontLoaders.Sans25 = new CFontRenderer(getFont("GoogleSans", 25), true, true);
        FontLoaders.Sans35 = new CFontRenderer(getFont("GoogleSans", 35), true, true);
        FontLoaders.JelloMedium_28 = new CFontRenderer(getFont("jellomedium", 28), true, true);
        FontLoaders.JelloMedium_22 = new CFontRenderer(getFont("jellomedium", 22), true, true);
        FontLoaders.JelloLight_24 = new CFontRenderer(getFont("jellolight", 24), true, true);
        FontLoaders.JelloLight_30 = new CFontRenderer(getFont("jellolight", 30), true, true);
        FontLoaders.Tenatiy_24 = new CFontRenderer(getFont("tenacitybold", 24), true, true);
        FontLoaders.posterama18 = new CFontRenderer(getFont("posterama",18),true,true);
        FontLoaders.posterama35 = new CFontRenderer(getFont("posterama",35),true,true);
        FontLoaders.ETB20 = new CFontRenderer(getFont("ETB", 20), true, true);
        FontLoaders.NovIcon20 = new CFontRenderer(getFont("NovIcon", 20), true, true);
        FontLoaders.FluxIcon14 = new CFontRenderer(getFont("fluxicon", 16), true, true);
        FontLoaders.FluxIcon16 = new CFontRenderer(getFont("fluxicon", 16), true, true);
        FontLoaders.FluxIcon18 = new CFontRenderer(getFont("fluxicon", 18), true, true);
        FontLoaders.FluxIcon20 = new CFontRenderer(getFont("fluxicon", 25), true, true);
        FontLoaders.FluxIcon30 = new CFontRenderer(getFont("fluxicon", 41), true, true);
        FontLoaders.FluxIcon40 = new CFontRenderer(getFont("fluxicon", 40), true, true);
        FontLoaders.FluxIcon50 = new CFontRenderer(getFont("fluxicon", 50), true, true);
        FontLoaders.Icon16 = new CFontRenderer(getFont("icon", 16), true, true);
        FontLoaders.JelloTitle20 = new CFontRenderer(getFont("jellolight", 20), true, true);
        FontLoaders.JelloTitle18 = new CFontRenderer(getFont("jellolight", 18), true, true);
        FontLoaders.JelloList16 = new CFontRenderer(getFont("jelloregular", 16), true, true);
        FontLoaders.JelloList40 = new CFontRenderer(getFont("jelloregular", 21), true, true);
        FontLoaders.font40 = new CFontRenderer(getFont("regular", 21), true, true);
        FontLoaders.C16 = new CFontRenderer(getFont("misans",16),true,true);
        FontLoaders.C18 = new CFontRenderer(getFont("misans",18),true,true);
        FontLoaders.C24 = new CFontRenderer(getFont("misans",24),true,true);
        FontLoaders.NOTIFICATIONS20 = new CFontRenderer(getFont("NOTIFICATIONS", 20), true, true);
        FontLoaders.NOTIFICATIONS18 = new CFontRenderer(getFont("NOTIFICATIONS", 18), true, true);
        FontLoaders.NOTIFICATIONS16 = new CFontRenderer(getFont("NOTIFICATIONS", 16), true, true);
        FontLoaders.NOTIFICATIONS30 = new CFontRenderer(getFont("NOTIFICATIONS", 30), true, true);
        FontLoaders.siyuan20 = new CFontRenderer(getFont("siyuan", 20), true, true);
        FontLoaders.siyuan18 = new CFontRenderer(getFont("siyuan", 18), true, true);
        FontLoaders.siyuan16 = new CFontRenderer(getFont("siyuan", 16), true, true);
        FontLoaders.siyuan30 = new CFontRenderer(getFont("siyuan", 30), true, true);
        FontLoaders.tenacitybold14 = new CFontRenderer(getFont("tenacitybold", 14), true, true);
        FontLoaders.tenacitybold16 = new CFontRenderer(getFont("tenacitybold", 16), true, true);
        FontLoaders.tenacitybold18 = new CFontRenderer(getFont("tenacitybold", 18), true, true);
        FontLoaders.tenacitybold20 = new CFontRenderer(getFont("tenacitybold", 20), true, true);
        FontLoaders.tenacitybold22 = new CFontRenderer(getFont("tenacitybold", 22), true, true);
        FontLoaders.tenacity14 = new CFontRenderer(getFont("tenacity", 14), true, true);
        FontLoaders.tenacity16 = new CFontRenderer(getFont("tenacity", 16), true, true);
        FontLoaders.tenacity18 = new CFontRenderer(getFont("tenacity", 18), true, true);
        FontLoaders.tenacity20 = new CFontRenderer(getFont("tenacity", 20), true, true);
        FontLoaders.tenacity22 = new CFontRenderer(getFont("tenacity", 22), true, true);
        FontLoaders.verdana20 = new CFontRenderer(getFont("verdana", 20), true, true);
        FontLoaders.verdana18 = new CFontRenderer(getFont("verdana", 18), true, true);
        FontLoaders.verdana14 = new CFontRenderer(getFont("verdana", 14), true, true);
        FontLoaders.verdana16 = new CFontRenderer(getFont("verdana", 15), true, true);
        FontLoaders.verdana30 = new CFontRenderer(getFont("verdana", 30), true, true);
        FontLoaders.verdanab20 = new CFontRenderer(getFont("verdanab", 20), true, true);
        FontLoaders.verdanab18 = new CFontRenderer(getFont("verdanab", 18), true, true);
        FontLoaders.verdanab14 = new CFontRenderer(getFont("verdanab", 14), true, true);
        FontLoaders.verdanab16 = new CFontRenderer(getFont("verdanab", 16), true, true);
        FontLoaders.verdanab17 = new CFontRenderer(getFont("verdanab", 17), true, true);
        FontLoaders.verdanab30 = new CFontRenderer(getFont("verdanab", 30), true, true);
        FontLoaders.sf_ui_display_regular12 = new CFontRenderer(getFont("sf_ui_display_regular", 12), true, true);
        FontLoaders.sf_ui_display_regular14 = new CFontRenderer(getFont("sf_ui_display_regular", 16), true, true);
        FontLoaders.sf_ui_display_regular16 = new CFontRenderer(getFont("sf_ui_display_regular", 16), true, true);
        FontLoaders.sf_ui_display_regular18 = new CFontRenderer(getFont("sf_ui_display_regular", 18), true, true);
        FontLoaders.sf_ui_display_regular20 = new CFontRenderer(getFont("sf_ui_display_regular", 21), true, true);
        FontLoaders.sf_ui_display_regular22 = new CFontRenderer(getFont("sf_ui_display_regular", 22), true, true);
        FontLoaders.sf_ui_display_regular25 = new CFontRenderer(getFont("sf_ui_display_regular", 25), true, true);
        FontLoaders.sf_ui_display_regular28 = new CFontRenderer(getFont("sf_ui_display_regular", 28), true, true);
        FontLoaders.sf_ui_display_regular35 = new CFontRenderer(getFont("sf_ui_display_regular", 35), true, true);
        FontLoaders.JelloM20 = new CFontRenderer(getFont("jellom", 20), true, true);
        FontLoaders.JelloM15 = new CFontRenderer(getFont("jellom", 15), true, true);

        FontLoaders.fonts = new ArrayList<CFontRenderer>();
    }
}
