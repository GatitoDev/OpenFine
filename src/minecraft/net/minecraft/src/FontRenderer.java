package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Formatter;
import java.util.Random;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
   private int[] field_22009_h;
   private byte[] charTexWidths;
   private byte[] charPixelWidths;
   private byte[] unicodeWidth;
   private int[] charTexIds = new int[256];
   private int basicTexID;
   private int lastBoundTexID;
   private RenderEngine tex;
   private float xPos;
   private float yPos;
   private Random random;
   private int imgWidth = 128;
   private int imgHeight = 128;
   private int charWidth = 8;
   private int charHeight = 8;
   private String textureName;
   private GameSettings gameSettings;

   public FontRenderer(GameSettings var1, String var2, RenderEngine var3) {
      this.field_22009_h = new int[32];
      this.charTexWidths = new byte[256];
      this.charPixelWidths = new byte[256];
      this.unicodeWidth = new byte[65536];
      this.random = new Random();
      this.tex = var3;
      this.textureName = var2;
      this.gameSettings = var1;
      this.init();
   }

   private void init() {
      BufferedImage var1;
      try {
         if (Config.getMinecraft() != null) {
            var1 = ImageIO.read(Config.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream(this.textureName));
            InputStream var2 = Config.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream("/font/glyph_sizes.bin");
            if (var2 != null) {
               var2.read(this.unicodeWidth);
            }
         } else {
            var1 = ImageIO.read(RenderChicken.class.getResourceAsStream(this.textureName));
            InputStream var12 = RenderChicken.class.getResourceAsStream("/font/glyph_sizes.bin");
            if (var12 != null) {
               var12.read(this.unicodeWidth);
            }
         }
      } catch (IOException var11) {
         throw new RuntimeException(var11);
      }

      this.imgWidth = var1.getWidth();
      this.imgHeight = var1.getHeight();
      this.charWidth = this.imgWidth / 16;
      this.charHeight = this.imgHeight / 16;
      int[] var13 = new int[this.imgWidth * this.imgHeight];
      var1.getRGB(0, 0, this.imgWidth, this.imgHeight, var13, 0, this.imgWidth);

      for (int var3 = 0; var3 < 256; var3++) {
         int var4 = var3 % 16;
         int var5 = var3 / 16;

         int var6;
         for (var6 = this.charWidth - 1; var6 >= 0; var6--) {
            int var7 = var4 * this.charWidth + var6;
            boolean var8 = true;

            for (int var9 = 0; var9 < this.charHeight && var8; var9++) {
               int var10 = var13[var7 + (var5 * this.charHeight + var9) * this.imgWidth] & 0xFF;
               if (var10 > 0) {
                  var8 = false;
               }
            }

            if (!var8) {
               break;
            }
         }

         if (var3 == 32) {
            var6 = 2;
         }

         this.charTexWidths[var3] = (byte)(var6 + 2);
         this.charPixelWidths[var3] = (byte)((var6 + 2) * 128 / this.imgWidth);
      }

      this.basicTexID = this.tex.allocateAndSetupTexture(var1);

      for (int var14 = 0; var14 < 32; var14++) {
         int var15 = (var14 >> 3 & 1) * 85;
         int var16 = (var14 >> 2 & 1) * 170 + var15;
         int var17 = (var14 >> 1 & 1) * 170 + var15;
         int var18 = (var14 >> 0 & 1) * 170 + var15;
         if (var14 == 6) {
            var16 += 85;
         }

         if (this.gameSettings.anaglyph) {
            int var19 = (var16 * 30 + var17 * 59 + var18 * 11) / 100;
            int var20 = (var16 * 30 + var17 * 70) / 100;
            int var21 = (var16 * 30 + var18 * 70) / 100;
            var16 = var19;
            var17 = var20;
            var18 = var21;
         }

         if (var14 >= 16) {
            var16 /= 4;
            var17 /= 4;
            var18 /= 4;
         }

         this.field_22009_h[var14] = (var16 & 0xFF) << 16 | (var17 & 0xFF) << 8 | var18 & 0xFF;
      }
   }

   private void func_22002_b(int var1) {
      float var2 = var1 % 16 * this.charWidth;
      float var3 = var1 / 16 * this.charHeight;
      if (this.lastBoundTexID != this.basicTexID) {
         GL11.glBindTexture(3553, this.basicTexID);
         this.lastBoundTexID = this.basicTexID;
      }

      float var4 = this.charTexWidths[var1] - 0.01F;
      float var5 = this.charPixelWidths[var1];
      GL11.glBegin(5);
      GL11.glTexCoord2f(var2 / this.imgWidth, var3 / this.imgHeight);
      GL11.glVertex3f(this.xPos, this.yPos, 0.0F);
      GL11.glTexCoord2f(var2 / this.imgWidth, (var3 + this.charHeight) / this.imgHeight);
      GL11.glVertex3f(this.xPos, this.yPos + 8.0F, 0.0F);
      GL11.glTexCoord2f((var2 + var4) / this.imgWidth, var3 / this.imgHeight);
      GL11.glVertex3f(this.xPos + var5, this.yPos, 0.0F);
      GL11.glTexCoord2f((var2 + var4) / this.imgWidth, (var3 + this.charHeight) / this.imgHeight);
      GL11.glVertex3f(this.xPos + var5, this.yPos + 8.0F, 0.0F);
      GL11.glEnd();
      this.xPos += var5;
   }

   private void func_22003_b(int var1) {
      StringBuilder var2 = new StringBuilder();
      new Formatter(var2).format("/font/glyph_%02X.png", var1);

      BufferedImage var3;
      try {
         if (Config.getMinecraft() != null) {
            var3 = ImageIO.read(Config.getMinecraft().texturePackList.selectedTexturePack.getResourceAsStream(var2.toString()));
         } else {
            var3 = ImageIO.read(PositionTextureVertex.class.getResourceAsStream(var2.toString()));
         }
      } catch (IOException var5) {
         throw new RuntimeException(var5);
      }

      this.charTexIds[var1] = this.tex.allocateAndSetupTexture(var3);
      this.lastBoundTexID = this.charTexIds[var1];
   }

   private String func_22001_u(int var1) {
      char[] var2 = new char[var1];

      while (var1-- != 0) {
         int var3 = this.random.nextInt() & 15;
         if (var3 == 0) {
            var2[var1] = 167;
         } else {
            var2[var1] = (char)(48 + var3);
         }
      }

      return new String(var2);
   }

   private void func_22004_c(char var1) {
      if (this.unicodeWidth[var1] != 0) {
         int var2 = var1 / 256;
         if (this.charTexIds[var2] == 0) {
            this.func_22003_b(var2);
         }

         if (this.lastBoundTexID != this.charTexIds[var2]) {
            GL11.glBindTexture(3553, this.charTexIds[var2]);
            this.lastBoundTexID = this.charTexIds[var2];
         }

         int var3 = this.unicodeWidth[var1] >> 4;
         int var4 = this.unicodeWidth[var1] & 15;
         float var5;
         float var6;
         if (var4 > 7) {
            var6 = 16.0F;
            var5 = 0.0F;
         } else {
            var6 = var4 + 1;
            var5 = var3;
         }

         float var7 = var1 % 16 * 16 + var5;
         float var8 = (var1 & 255) / 16 * 16;
         float var9 = var6 - var5 - 0.02F;
         GL11.glBegin(5);
         GL11.glTexCoord2f(var7 / 256.0F, var8 / 256.0F);
         GL11.glVertex3f(this.xPos, this.yPos, 0.0F);
         GL11.glTexCoord2f(var7 / 256.0F, (var8 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.xPos, this.yPos + 7.99F, 0.0F);
         GL11.glTexCoord2f((var7 + var9) / 256.0F, var8 / 256.0F);
         GL11.glVertex3f(this.xPos + var9 / 2.0F, this.yPos, 0.0F);
         GL11.glTexCoord2f((var7 + var9) / 256.0F, (var8 + 15.98F) / 256.0F);
         GL11.glVertex3f(this.xPos + var9 / 2.0F, this.yPos + 7.99F, 0.0F);
         GL11.glEnd();
         this.xPos += (var6 - var5) / 2.0F + 1.0F;
      }
   }

   private void renderStringImpl(String var1, boolean var2) {
      for (int var3 = 0; var3 < var1.length(); var3++) {
         char var4 = var1.charAt(var3);
         if (var4 == 167 && var3 + 1 < var1.length()) {
            int var7 = "0123456789abcdef".indexOf(var1.toLowerCase().charAt(var3 + 1));
            if (var7 < 0 || var7 > 15) {
               var7 = 15;
            }

            if (var2) {
               var7 += 16;
            }

            int var6 = this.field_22009_h[var7];
            GL11.glColor3f((var6 >> 16) / 255.0F, (var6 >> 8 & 0xFF) / 255.0F, (var6 & 0xFF) / 255.0F);
            var3++;
         } else {
            int var5 = ChatAllowedCharacters.allowedCharacters.indexOf(var4);
            if (var4 == ' ') {
               this.xPos += 4.0F;
            } else if (var5 > 0) {
               this.func_22002_b(var5 + 32);
            } else {
               this.func_22004_c(var4);
            }
         }
      }
   }

   private void renderString(String var1, int var2, int var3, int var4, boolean var5) {
      this.checkUpdated();
      if (var1 != null) {
         this.lastBoundTexID = 0;
         if ((var4 & 0xFF000000) == 0) {
            var4 |= -16777216;
         }

         if (var5) {
            var4 = (var4 & 16579836) >> 2 | var4 & 0xFF000000;
         }

         GL11.glColor4f((var4 >> 16 & 0xFF) / 255.0F, (var4 >> 8 & 0xFF) / 255.0F, (var4 & 0xFF) / 255.0F, (var4 >> 24 & 0xFF) / 255.0F);
         this.xPos = var2;
         this.yPos = var3;
         this.renderStringImpl(var1, var5);
      }
   }

   public void drawStringWithShadow(String var1, int var2, int var3, int var4) {
      this.renderString(var1, var2 + 1, var3 + 1, var4, true);
      this.renderString(var1, var2, var3, var4, false);
   }

   public void drawString(String var1, int var2, int var3, int var4) {
      this.renderString(var1, var2, var3, var4, false);
   }

   public int getStringWidth(String var1) {
      this.checkUpdated();
      if (var1 == null) {
         return 0;
      }

      int var2 = 0;

      for (int var3 = 0; var3 < var1.length(); var3++) {
         char var4 = var1.charAt(var3);
         if (var4 == 167) {
            var3++;
         } else {
            int var5 = ChatAllowedCharacters.allowedCharacters.indexOf(var4);
            if (var5 >= 0) {
               var2 += this.charPixelWidths[var5 + 32];
            } else if (this.unicodeWidth[var4] != 0) {
               int var6 = this.unicodeWidth[var4] >> 4;
               int var7 = this.unicodeWidth[var4] & 15;
               if (var7 > 7) {
                  var7 = 15;
                  var6 = 0;
               }

               var7++;
               var2 += (var7 - var6) / 2 + 1;
            }
         }
      }

      return var2;
   }

   private void checkUpdated() {
      if (!Config.isFontRendererUpdated()) {
         this.init();
         Config.setFontRendererUpdated(true);
      }
   }

   public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
      this.checkUpdated();
      String[] var6 = var1.split("\n");
      if (var6.length > 1) {
         for (int var11 = 0; var11 < var6.length; var11++) {
            this.drawSplitString(var6[var11], var2, var3, var4, var5);
            var3 += this.splitStringWidth(var6[var11], var4);
         }
      } else {
         String[] var7 = var1.split(" ");
         int var8 = 0;

         while (var8 < var7.length) {
            String var9 = var7[var8++] + " ";

            while (var8 < var7.length && this.getStringWidth(var9 + var7[var8]) < var4) {
               var9 = var9 + var7[var8++] + " ";
            }

            while (this.getStringWidth(var9) > var4) {
               int var10 = 0;

               while (this.getStringWidth(var9.substring(0, var10 + 1)) <= var4) {
                  var10++;
               }

               if (var9.substring(0, var10).trim().length() > 0) {
                  this.drawString(var9.substring(0, var10), var2, var3, var5);
                  var3 += this.charHeight;
               }

               var9 = var9.substring(var10);
            }

            if (var9.trim().length() > 0) {
               this.drawString(var9, var2, var3, var5);
               var3 += this.charHeight;
            }
         }
      }
   }

   public int splitStringWidth(String var1, int var2) {
      this.checkUpdated();
      String[] var3 = var1.split("\n");
      if (var3.length > 1) {
         int var9 = 0;

         for (int var10 = 0; var10 < var3.length; var10++) {
            var9 += this.splitStringWidth(var3[var10], var2);
         }

         return var9;
      } else {
         String[] var4 = var1.split(" ");
         int var5 = 0;
         int var6 = 0;

         while (var5 < var4.length) {
            String var7 = var4[var5++] + " ";

            while (var5 < var4.length && this.getStringWidth(var7 + var4[var5]) < var2) {
               var7 = var7 + var4[var5++] + " ";
            }

            while (this.getStringWidth(var7) > var2) {
               int var8 = 0;

               while (this.getStringWidth(var7.substring(0, var8 + 1)) <= var2) {
                  var8++;
               }

               if (var7.substring(0, var8).trim().length() > 0) {
                  var6 += this.charHeight;
               }

               var7 = var7.substring(var8);
            }

            if (var7.trim().length() > 0) {
               var6 += this.charHeight;
            }
         }

         if (var6 < 8) {
            var6 += this.charHeight;
         }

         return var6;
      }
   }
}
