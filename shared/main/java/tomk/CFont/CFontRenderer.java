package tomk.CFont;

import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;
import tomk.CFont.CFont;
import tomk.LanguageManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CFontRenderer extends CFont {

    protected CFont.CharData[] boldChars = new CharData[256];
    protected CharData[] italicChars = new CharData[256];
    protected CharData[] boldItalicChars = new CharData[256];
    private final int[] colorCode = new int[32];
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawStringWithShadow(String text, double x, double y, int color) {
        float shadowWidth = this.drawString(text, x + 0.5D, y + 0.5D, color, true);

        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(String text, float x, float y, int color) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        RenderUtils.color(color);
        return this.drawString(text, (double) x, (double) y, color, false);
    }
    public float drawString(String text, int x, int y, int color) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        RenderUtils.color(color);
        return this.drawString(text, (double) x, (double) y, color, false);
    }
    public float drawCenteredString(String text, double x, double y, int color) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        return this.drawString(text, (float) (x - (double) ((float) (this.getStringWidth(text) / 2))), (float) y, color);
    }
    public float drawCenteredString(String text, float x, float y, int color) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        return this.drawString(text, (float) (x - (double) ((float) (this.getStringWidth(text) / 2))), (float) y, color);
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
        return this.drawStringWithShadow(text, (double) (x - (float) (this.getStringWidth(text) / 2)), (double) y, color);
    }
    public static boolean isChinese(char c) {
        String s = String.valueOf(c);
        if(!"1234567890abcdefghijklmnopqrstuvwxyz!<>@#$%^&*()-_=+[]{}|\\/'\",.~`".contains(s.toLowerCase()))
            return true;
        else{
            return false;
        }
    }

    public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
        return this.drawStringWithShadow(text, x - (double) (this.getStringWidth(text) / 2), y, color);
    }
    public static float DisplayFont(String str, float x, float y, int color, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str=" "+str;
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                color = getColor(String.valueOf(str.toCharArray()[iF + 1]));
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                font.drawString(s, x-0.5f, y+1, color);
                x += (float)font.getStringWidth(s);
            } else{
                Fonts.font40.drawString(s, x+0.5f, y+1, color);
                x += (float)Fonts.font40.getStringWidth(s);
            }
        }
        return x;
    }
    public  float DisplayFont(CFontRenderer font,String str, int x, float y, int color) {
        return DisplayFont(str,x,y,color,font);
    }

    public  float DisplayFonts(CFontRenderer font,String str, int x, int y, int color) {
        return DisplayFont(str,x,y,color,font);
    }

    public float DisplayFonts(String str, float x, float y, int color, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str=" "+str;
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                color = getColor(String.valueOf(str.toCharArray()[iF + 1]));
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                font.drawString(s, x-0.5f, y+1, color);
                x += (float)font.getStringWidth(s);
            } else{
                Fonts.font40.drawString(s, x+0.5f, y+1, color);
                x += (float)Fonts.font40.getStringWidth(s);
            }
        }
        return x;
    }
    public float DisplayFont2(CFontRenderer font,String str, int x, int y, int color,boolean shadow) {
        if(shadow)
            return DisplayFont(str,x,y,color,shadow,font);
        else{
            return DisplayFont(str,x,y,color,font);
        }
    }
    public static int getColor(String str) {
        switch(str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    return (new Color(0, 0, 0)).getRGB();
                }
                break;
            case 49:
                if (str.equals("1")) {
                    return (new Color(0, 0, 189)).getRGB();
                }
                break;
            case 50:
                if (str.equals("2")) {
                    return (new Color(0, 192, 0)).getRGB();
                }
                break;
            case 51:
                if (str.equals("3")) {
                    return (new Color(0, 190, 190)).getRGB();
                }
                break;
            case 52:
                if (str.equals("4")) {
                    return (new Color(190, 0, 0)).getRGB();
                }
                break;
            case 53:
                if (str.equals("5")) {
                    return (new Color(189, 0, 188)).getRGB();
                }
                break;
            case 54:
                if (str.equals("6")) {
                    return (new Color(218, 163, 47)).getRGB();
                }
                break;
            case 55:
                if (str.equals("7")) {
                    return (new Color(190, 190, 190)).getRGB();
                }
                break;
            case 56:
                if (str.equals("8")) {
                    return (new Color(63, 63, 63)).getRGB();
                }
                break;
            case 57:
                if (str.equals("9")) {
                    return (new Color(63, 64, 253)).getRGB();
                }
                break;
            case 97:
                if (str.equals("a")) {
                    return (new Color(63, 254, 63)).getRGB();
                }
                break;
            case 98:
                if (str.equals("b")) {
                    return (new Color(62, 255, 254)).getRGB();
                }
                break;
            case 99:
                if (str.equals("c")) {
                    return (new Color(254, 61, 62)).getRGB();
                }
                break;
            case 100:
                if (str.equals("d")) {
                    return (new Color(255, 64, 255)).getRGB();
                }
                break;
            case 101:
                if (str.equals("e")) {
                    return (new Color(254, 254, 62)).getRGB();
                }
                break;
            case 102:
                if (str.equals("f")) {
                    return (new Color(255, 255, 255)).getRGB();
                }
        }

        return (new Color(255, 255, 255)).getRGB();
    }
    public static float DisplayFont(String str, float x, float y, int color,boolean shadow, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        str=" "+str;
        //ClientUtils.INSTANCE.displayAlert(str);
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                color = getColor(String.valueOf(str.toCharArray()[iF + 1]));
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                font.drawString(s, x+0.5f, y+1.5f, new Color(0,0,0,100).getRGB());
                font.drawString(s, x-0.5f, y+0.5f, color);
                x += (float)font.getStringWidth(s);
            } else {
                Fonts.font40.drawString(s, x+1.5f, y+2, new Color(0,0,0,50).getRGB());
                Fonts.font40.drawString(s, x+0.5f, y+1, color);
                x += (float)Fonts.font40.getStringWidth(s);
            }
        }
        return x;
        //return font.drawString(str, x, y, color);
    }
    public int DisplayFontWidths(String str, CFontRenderer font) {
        str = LanguageManager.INSTANCE.get(LanguageManager.INSTANCE.replace(str));
        int x=0;
        for(int iF = 0; iF < str.length(); ++iF) {
            String s = String.valueOf(str.toCharArray()[iF]);
            if (s.contains("§") && iF + 1 <= str.length()) {
                iF++;
            } else if (!isChinese(s.charAt(0))) {
                x += (float)font.getStringWidth(s);
            } else {
                x += (float) Fonts.font40.getStringWidth(s);
            }
        }
        return x+5;
    }
    public float drawString(String text, double x, double y, int color, boolean shadow) {
        GlStateManager.enableBlend();
        GlStateManager.disableBlend();
        --x;
        if (text == null) {
            return 0.0F;
        } else {
            if (color == 553648127) {
                color = 16777215;
            }

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (shadow) {
                color = (new Color(0, 0, 0)).getRGB();
            }

            CharData[] currentData = this.charData;
            float alpha = (float) (color >> 24 & 255) / 255.0F;
            boolean randomCase = false;
            boolean bold = false;
            boolean italic = false;
            boolean strikethrough = false;
            boolean underline = false;
            boolean render = true;

            x *= 2.0D;
            y = (y - 3.0D) * 2.0D;
            if (render) {
                GL11.glPushMatrix();
                GlStateManager.scale(0.5D, 0.5D, 0.5D);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, alpha);
                int size = text.length();

                GlStateManager.enableTexture2D();
                GlStateManager.bindTexture(this.tex.getGlTextureId());
                GL11.glBindTexture(3553, this.tex.getGlTextureId());

                for (int i = 0; i < size; ++i) {
                    char character = text.charAt(i);

                    if (character == 167 && i < size) {
                        int colorIndex = 21;

                        try {
                            colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }

                        if (colorIndex < 16) {
                            bold = false;
                            italic = false;
                            randomCase = false;
                            underline = false;
                            strikethrough = false;
                            GlStateManager.bindTexture(this.tex.getGlTextureId());
                            currentData = this.charData;
                            if (colorIndex < 0 || colorIndex > 15) {
                                colorIndex = 15;
                            }

                            if (shadow) {
                                colorIndex += 16;
                            }

                            int colorcode = this.colorCode[colorIndex];

                            GlStateManager.color((float) (colorcode >> 16 & 255) / 255.0F, (float) (colorcode >> 8 & 255) / 255.0F, (float) (colorcode & 255) / 255.0F, alpha);
                        } else if (colorIndex == 16) {
                            randomCase = true;
                        } else if (colorIndex == 17) {
                            bold = true;
                            if (italic) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GlStateManager.bindTexture(this.texBold.getGlTextureId());
                                currentData = this.boldChars;
                            }
                        } else if (colorIndex == 18) {
                            strikethrough = true;
                        } else if (colorIndex == 19) {
                            underline = true;
                        } else if (colorIndex == 20) {
                            italic = true;
                            if (bold) {
                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                currentData = this.boldItalicChars;
                            } else {
                                GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                                currentData = this.italicChars;
                            }
                        } else if (colorIndex == 21) {
                            bold = false;
                            italic = false;
                            randomCase = false;
                            underline = false;
                            strikethrough = false;
                            GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, alpha);
                            GlStateManager.bindTexture(this.tex.getGlTextureId());
                            currentData = this.charData;
                        }

                        ++i;
                    } else if (character < currentData.length && character >= 0) {
                        GL11.glBegin(4);
                        this.drawChar(currentData, character, (float) x, (float) y);
                        GL11.glEnd();
                        if (strikethrough) {
                            this.drawLine(x, y + (double) (currentData[character].height / 2), x + (double) currentData[character].width - 8.0D, y + (double) (currentData[character].height / 2), 1.0F);
                        }

                        if (underline) {
                            this.drawLine(x, y + (double) currentData[character].height - 2.0D, x + (double) currentData[character].width - 8.0D, y + (double) currentData[character].height - 2.0D, 1.0F);
                        }

                        x += (double) (currentData[character].width - 8 + this.charOffset);
                    }
                }

                GL11.glHint(3155, 4352);
                GL11.glPopMatrix();
            }

            return (float) x / 2.0F;
        }
    }

    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        } else {
            int width = 0;
            CharData[] currentData = this.charData;
            boolean bold = false;
            boolean italic = false;
            int size = text.length();

            for (int i = 0; i < size; ++i) {
                char character = text.charAt(i);

                if (character == 167 && i < size) {
                    int colorIndex = "0123456789abcdefklmnor".indexOf(character);

                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                    } else if (colorIndex == 17) {
                        bold = true;
                        currentData = italic ? this.boldItalicChars : this.boldChars;
                    } else if (colorIndex == 20) {
                        italic = true;
                        currentData = bold ? this.boldItalicChars : this.italicChars;
                    } else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        currentData = this.charData;
                    }

                    ++i;
                } else if (character < currentData.length && character >= 0) {
                    width += currentData[character].width - 8 + this.charOffset;
                }
            }

            return width / 2;
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
    }

    private void drawLine(double x, double y, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List wrapWords(String text, double width) {
        ArrayList finalWords = new ArrayList();

        if ((double) this.getStringWidth(text) > width) {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            String[] arrstring = words;
            int n = words.length;

            for (int n2 = 0; n2 < n; ++n2) {
                String word = arrstring[n2];

                for (int s = 0; s < word.toCharArray().length; ++s) {
                    char c = word.toCharArray()[s];

                    if (c == 167 && s < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[s + 1];
                    }
                }

                if ((double) this.getStringWidth(currentWord + word + " ") < width) {
                    currentWord = currentWord + word + " ";
                } else {
                    finalWords.add(currentWord);
                    currentWord = 167 + lastColorCode + word + " ";
                }
            }

            if (currentWord.length() > 0) {
                if ((double) this.getStringWidth(currentWord) < width) {
                    finalWords.add(167 + lastColorCode + currentWord + " ");
                    currentWord = "";
                } else {
                    Iterator iterator = this.formatString(currentWord, width).iterator();

                    while (iterator.hasNext()) {
                        String s = (String) iterator.next();

                        finalWords.add(s);
                    }
                }
            }
        } else {
            finalWords.add(text);
        }

        return finalWords;
    }

    public List formatString(String string, double width) {
        ArrayList finalWords = new ArrayList();
        String currentWord = "";
        char lastColorCode = '\uffff';
        char[] chars = string.toCharArray();

        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];

            if (c == 167 && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }

            if ((double) this.getStringWidth(currentWord + c) < width) {
                currentWord = currentWord + c;
            } else {
                finalWords.add(currentWord);
                currentWord = 167 + lastColorCode + String.valueOf(c);
            }
        }

        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }

        return finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;

            if (index == 6) {
                red += 85;
            }

            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }

    }
}
