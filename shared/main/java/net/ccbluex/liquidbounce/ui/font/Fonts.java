/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.*;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import tomk.CFont.FontLoaders;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Fonts extends MinecraftInstance {


    private static final HashMap<FontInfo, GameFontRenderer> CUSTOM_FONT_RENDERERS = new HashMap<>();
    @FontDetails(fontName = "Light", fontSize = 32)
    public static GameFontRenderer font32;
    @FontDetails(fontName = "sfbold100", fontSize = 40)
    public static GameFontRenderer sfbold100;
    @FontDetails(fontName = "sfbold80", fontSize = 40)
    public static GameFontRenderer sfbold80;
    @FontDetails(fontName = "sfbold40", fontSize = 40)
    public static GameFontRenderer sfbold40;
    @FontDetails(fontName = "sfbold35", fontSize = 40)
    public static GameFontRenderer sfbold35;
    @FontDetails(fontName = "sfbold30", fontSize = 40)
    public static GameFontRenderer sfbold30;
    @FontDetails(fontName = "sfbold28", fontSize = 40)
    public static GameFontRenderer sfbold28;
    @FontDetails(fontName = "Tenacitybold", fontSize = 18)
    public static GameFontRenderer tenacitybold35;
    @FontDetails(fontName = "Tenacitybold", fontSize = 18)
    public static GameFontRenderer tenacitybold30;
    @FontDetails(fontName = "Tenacitybold", fontSize = 20)
    public static GameFontRenderer tenacitybold40;
    @FontDetails(fontName = "Tenacitybold", fontSize = 21)
    public static GameFontRenderer tenacitybold43;
    @FontDetails(fontName = "Tenacitycheck", fontSize = 60)
    public static GameFontRenderer tenacitycheck60;
    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static GameFontRenderer font35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 35)
    public static GameFontRenderer font31;
    @FontDetails(fontName = "Roboto Medium", fontSize = 40)
    public static GameFontRenderer font40;
    @FontDetails(fontName = "Roboto Bold", fontSize = 180)
    public static GameFontRenderer fontBold180;
    @FontDetails(fontName = "Ico", fontSize = 60)
    public static GameFontRenderer ico1;
    @FontDetails(fontName = "Ico2", fontSize = 50)
    public static GameFontRenderer ico2;
    @FontDetails(fontName = "micon15", fontSize = 15)
    public static GameFontRenderer micon15;
    @FontDetails(fontName = "micon30", fontSize = 30)
    public static GameFontRenderer micon30;
    @FontDetails(fontName = "Chinese", fontSize = 16)
    public static GameFontRenderer Chinese16;
    @FontDetails(fontName = "Chinese", fontSize = 18)
    public static GameFontRenderer Chinese18;
    @FontDetails(fontName = "Chinese", fontSize = 18)
    public static GameFontRenderer Chinese17;
    @FontDetails(fontName = "SFUI35", fontSize = 35)
    public static GameFontRenderer SFUI35;
    @FontDetails(fontName = "Roboto Medium", fontSize = 30)
    public static GameFontRenderer font30;
    @FontDetails(fontName = "Mon", fontSize = 30)
    public static GameFontRenderer mon60;
    public static void loadver() {
        font30 = new GameFontRenderer(getchinese( 30));
        font31 = (new GameFontRenderer(gett(35)));
        Chinese17 =  (new GameFontRenderer(gett(37)));
        ico2 = new GameFontRenderer(gets2(50));
    }
    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");
        downloadFonts();
        Chinese16 =  (new GameFontRenderer(getsfbold(37)));
        font35 = (new GameFontRenderer(getsfbold(35)));
        Chinese18 =  (new GameFontRenderer(getsfbold(38)));
        SFUI35 =new GameFontRenderer(getFont("sfui.ttf", 40));
        font32 = (new GameFontRenderer(getFont("sfui.ttf",38)));
        sfbold100 = (new GameFontRenderer(getsfbold(100)));
        sfbold80 = (new GameFontRenderer(getsfbold(80)));
        sfbold40 = (new GameFontRenderer(getsfbold(40)));
        sfbold35 = (new GameFontRenderer(getsfbold(35)));
        sfbold30 = (new GameFontRenderer(getsfbold(30)));
        sfbold28 = (new GameFontRenderer(getsfbold(28)));
        tenacitybold35 = (new GameFontRenderer(gett(35)));
        tenacitybold30 = (new GameFontRenderer(gett(30)));
        tenacitybold40 = (new GameFontRenderer(gett(40)));
        tenacitybold43 = (new GameFontRenderer(gett(43)));
        tenacitycheck60 = (new GameFontRenderer(getf(60)));
        ico1 = (new GameFontRenderer(gets(60)));
        micon15 = (new GameFontRenderer(gets3(25)));
        micon30 = (new GameFontRenderer(gets3(40)));

        font40 = (new GameFontRenderer(getFont("sfui.ttf", 40)));
        fontBold180 = (new GameFontRenderer(getFont("sfui.ttf", 180)));
        net.ccbluex.liquidbounce.feng.FontLoaders.initFonts();

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    Font font = getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());

                    CUSTOM_FONT_RENDERERS.put(new FontInfo(font), (new GameFontRenderer(font)));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidBounce.fileManager.fontsDir, "roboto.zip");

            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download(LiquidBounce.CLIENT_CLOUD + "/fonts/Roboto.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Font getComfortaa(int size) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/misans.ttf")).getInputStream()).deriveFont(0, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getsfbold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/fz.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }
    private static Font gett(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/tenacitybold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }

    private static Font getchinese(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/fz.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }
    private static Font getf(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/tenacitycheck.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }
    private static Font gets(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/icon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }
    private static Font gets3(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/micon.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }
    private static Font gets2(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("liquidbounce/font/icon2.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }
    public static GameFontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                Object o = field.get(null);

                if (o instanceof GameFontRenderer) {
                    FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (GameFontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return CUSTOM_FONT_RENDERERS.getOrDefault(new FontInfo(name, size), Fonts.font35);
    }

    public static FontInfo getFontDetails(final GameFontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<FontInfo, GameFontRenderer> entry : CUSTOM_FONT_RENDERERS.entrySet()) {
            if (entry.getValue() == fontRenderer)
                return entry.getKey();
        }

        return null;
    }

    public static List<GameFontRenderer> getFonts() {
        final List<GameFontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof GameFontRenderer) fonts.add((GameFontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS.values());

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }

    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return name;
        }

        public int getFontSize() {
            return fontSize;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FontInfo fontInfo = (FontInfo) o;

            if (fontSize != fontInfo.fontSize) return false;
            return Objects.equals(name, fontInfo.name);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + fontSize;
            return result;
        }
    }

}