package net.minecraft.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class RenderEngine {
   public static boolean useMipmaps = false;
   private HashMap<String, Integer> textureMap;
   private HashMap<String, int[]> field_28151_c;
   private HashMap<Integer, BufferedImage> textureNameToImageMap;
   private IntBuffer singleIntBuffer;
   private ByteBuffer imageData;
   private List<TextureFX> textureList;
   private Map<String, ThreadDownloadImageData> urlToImageDataMap;
   private GameSettings options;
   private boolean clampTexture;
   private boolean blurTexture;
   private TexturePackList texturePack;
   private BufferedImage missingTextureImage;
   private int terrainTextureId = -1;
   private int guiItemsTextureId = -1;
   private boolean hdTexturesInstalled = false;
   private Map<Integer, Dimension> textureDimensionsMap = new HashMap<Integer, Dimension>();
   private Map<String, byte[]> textureDataMap = new HashMap<String, byte[]>();
   private int tickCounter = 0;
   private ByteBuffer[] mipImageDatas;
   private boolean dynamicTexturesUpdated = false;

   public RenderEngine(TexturePackList var1, GameSettings var2) {
      this.textureMap = new HashMap<String, Integer>();
      this.field_28151_c = new HashMap<String, int[]>();
      this.textureNameToImageMap = new HashMap<Integer, BufferedImage>();
      this.singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
      this.allocateImageData(256);
      this.textureList = new ArrayList<TextureFX>();
      this.urlToImageDataMap = new HashMap<String, ThreadDownloadImageData>();
      this.clampTexture = false;
      this.blurTexture = false;
      this.missingTextureImage = new BufferedImage(64, 64, 2);
      this.texturePack = var1;
      this.options = var2;
      Graphics var3 = this.missingTextureImage.getGraphics();
      var3.setColor(Color.WHITE);
      var3.fillRect(0, 0, 64, 64);
      var3.setColor(Color.BLACK);
      var3.drawString("missingtex", 1, 10);
      var3.dispose();
   }

   public int[] func_28149_a(String var1) {
      TexturePackBase var2 = this.texturePack.selectedTexturePack;
      int[] var3 = (int[])this.field_28151_c.get(var1);
      if (var3 != null) {
         return var3;
      }

      try {
         int[] var7 = null;
         if (var1.startsWith("##")) {
            var7 = this.func_28148_b(this.unwrapImageByColumns(this.readTextureImage(var2.getResourceAsStream(var1.substring(2)))));
         } else if (var1.startsWith("%clamp%")) {
            this.clampTexture = true;
            var7 = this.func_28148_b(this.readTextureImage(var2.getResourceAsStream(var1.substring(7))));
            this.clampTexture = false;
         } else if (var1.startsWith("%blur%")) {
            this.blurTexture = true;
            var7 = this.func_28148_b(this.readTextureImage(var2.getResourceAsStream(var1.substring(6))));
            this.blurTexture = false;
         } else {
            InputStream var5 = var2.getResourceAsStream(var1);
            if (var5 == null) {
               var7 = this.func_28148_b(this.missingTextureImage);
            } else {
               var7 = this.func_28148_b(this.readTextureImage(var5));
            }
         }

         this.field_28151_c.put(var1, var7);
         return var7;
      } catch (IOException var6) {
         var6.printStackTrace();
         int[] var4 = this.func_28148_b(this.missingTextureImage);
         this.field_28151_c.put(var1, var4);
         return var4;
      }
   }

   private int[] func_28148_b(BufferedImage var1) {
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int[] var4 = new int[var2 * var3];
      var1.getRGB(0, 0, var2, var3, var4, 0, var2);
      return var4;
   }

   private int[] func_28147_a(BufferedImage var1, int[] var2) {
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      var1.getRGB(0, 0, var3, var4, var2, 0, var3);
      return var2;
   }

   public int getTexture(String var1) {
      TexturePackBase var2 = this.texturePack.selectedTexturePack;
      Integer var3 = (Integer)this.textureMap.get(var1);
      if (var3 != null) {
         return var3;
      }

      try {
         ((Buffer)this.singleIntBuffer).clear();
         GLAllocation.generateTextureNames(this.singleIntBuffer);
         int var7 = this.singleIntBuffer.get(0);
         if (var1.startsWith("##")) {
            this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(var2.getResourceAsStream(var1.substring(2)))), var7);
         } else if (var1.startsWith("%clamp%")) {
            this.clampTexture = true;
            this.setupTexture(this.readTextureImage(var2.getResourceAsStream(var1.substring(7))), var7);
            this.clampTexture = false;
         } else if (var1.startsWith("%blur%")) {
            this.blurTexture = true;
            this.setupTexture(this.readTextureImage(var2.getResourceAsStream(var1.substring(6))), var7);
            this.blurTexture = false;
         } else {
            InputStream var5 = var2.getResourceAsStream(var1);
            if (var5 == null) {
               this.setupTexture(this.missingTextureImage, var7);
            } else {
               if (var1.equals("/terrain.png")) {
                  this.terrainTextureId = var7;
               }

               if (var1.equals("/gui/items.png")) {
                  this.guiItemsTextureId = var7;
               }

               this.setupTexture(this.readTextureImage(var5), var7);
            }
         }

         this.textureMap.put(var1, var7);
         return var7;
      } catch (IOException var6) {
         var6.printStackTrace();
         GLAllocation.generateTextureNames(this.singleIntBuffer);
         int var4 = this.singleIntBuffer.get(0);
         this.setupTexture(this.missingTextureImage, var4);
         this.textureMap.put(var1, var4);
         return var4;
      }
   }

   private BufferedImage unwrapImageByColumns(BufferedImage var1) {
      int var2 = var1.getWidth() / 16;
      BufferedImage var3 = new BufferedImage(16, var1.getHeight() * var2, 2);
      Graphics var4 = var3.getGraphics();

      for (int var5 = 0; var5 < var2; var5++) {
         var4.drawImage(var1, -var5 * 16, var5 * var1.getHeight(), null);
      }

      var4.dispose();
      return var3;
   }

   public int allocateAndSetupTexture(BufferedImage var1) {
      ((Buffer)this.singleIntBuffer).clear();
      GLAllocation.generateTextureNames(this.singleIntBuffer);
      int var2 = this.singleIntBuffer.get(0);
      this.setupTexture(var1, var2);
      this.textureNameToImageMap.put(var2, var1);
      return var2;
   }

   public void setupTexture(BufferedImage var1, int var2) {
      GL11.glBindTexture(3553, var2);
      useMipmaps = Config.isUseMipmaps();
      if (useMipmaps && var2 != this.guiItemsTextureId) {
         int var3 = Config.getMipmapType();
         GL11.glTexParameteri(3553, 10241, var3);
         GL11.glTexParameteri(3553, 10240, 9728);
         if (GLContext.getCapabilities().OpenGL12) {
            GL11.glTexParameteri(3553, 33084, 0);
            int var4 = Config.getMipmapLevel();
            if (var4 >= 4) {
               int var5 = Math.min(var1.getWidth(), var1.getHeight());
               var4 = this.getMaxMipmapLevel(var5) - 4;
               if (var4 < 0) {
                  var4 = 0;
               }
            }

            GL11.glTexParameteri(3553, 33085, var4);
         }
      } else {
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

      if (this.blurTexture) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      }

      if (this.clampTexture) {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      int var15 = var1.getWidth();
      int var16 = var1.getHeight();
      this.setTextureDimension(var2, new Dimension(var15, var16));
      int[] var17 = new int[var15 * var16];
      byte[] var6 = new byte[var15 * var16 * 4];
      var1.getRGB(0, 0, var15, var16, var17, 0, var15);

      for (int var7 = 0; var7 < var17.length; var7++) {
         int var8 = var17[var7] >> 24 & 0xFF;
         int var9 = var17[var7] >> 16 & 0xFF;
         int var10 = var17[var7] >> 8 & 0xFF;
         int var11 = var17[var7] & 0xFF;
         if (this.options != null && this.options.anaglyph) {
            int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
            int var13 = (var9 * 30 + var10 * 70) / 100;
            int var14 = (var9 * 30 + var11 * 70) / 100;
            var9 = var12;
            var10 = var13;
            var11 = var14;
         }

         if (var8 == 0) {
            var9 = 255;
            var10 = 255;
            var11 = 255;
         }

         var6[var7 * 4 + 0] = (byte)var9;
         var6[var7 * 4 + 1] = (byte)var10;
         var6[var7 * 4 + 2] = (byte)var11;
         var6[var7 * 4 + 3] = (byte)var8;
      }

      this.checkImageDataSize(var15);
      ((Buffer)this.imageData).clear();
      this.imageData.put(var6);
      ((Buffer)this.imageData).position(0).limit(var6.length);
      GL11.glTexImage2D(3553, 0, 6408, var15, var16, 0, 6408, 5121, this.imageData);
      if (useMipmaps) {
         this.generateMipMaps(this.imageData, var15, var16);
      }
   }

   private void generateMipMaps(ByteBuffer var1, int var2, int var3) {
      ByteBuffer var4 = var1;

      for (int var5 = 1; var5 <= 16; var5++) {
         int var6 = var2 >> var5 - 1;
         int var7 = var2 >> var5;
         int var8 = var3 >> var5;
         if (var7 <= 0 || var8 <= 0) {
            break;
         }

         ByteBuffer var9 = this.mipImageDatas[var5 - 1];

         for (int var10 = 0; var10 < var7; var10++) {
            for (int var11 = 0; var11 < var8; var11++) {
               int var12 = var4.getInt((var10 * 2 + 0 + (var11 * 2 + 0) * var6) * 4);
               int var13 = var4.getInt((var10 * 2 + 1 + (var11 * 2 + 0) * var6) * 4);
               int var14 = var4.getInt((var10 * 2 + 1 + (var11 * 2 + 1) * var6) * 4);
               int var15 = var4.getInt((var10 * 2 + 0 + (var11 * 2 + 1) * var6) * 4);
               int var16 = this.weightedAverageColor(var12, var13, var14, var15);
               var9.putInt((var10 + var11 * var7) * 4, var16);
            }
         }

         GL11.glTexImage2D(3553, var5, 6408, var7, var8, 0, 6408, 5121, var9);
         var4 = var9;
      }
   }

   public void func_28150_a(int[] var1, int var2, int var3, int var4) {
      GL11.glBindTexture(3553, var4);
      if (useMipmaps) {
         GL11.glTexParameteri(3553, 10241, 9986);
         GL11.glTexParameteri(3553, 10240, 9728);
      } else {
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

      if (this.blurTexture) {
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      }

      if (this.clampTexture) {
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

      byte[] var5 = new byte[var2 * var3 * 4];

      for (int var6 = 0; var6 < var1.length; var6++) {
         int var7 = var1[var6] >> 24 & 0xFF;
         int var8 = var1[var6] >> 16 & 0xFF;
         int var9 = var1[var6] >> 8 & 0xFF;
         int var10 = var1[var6] & 0xFF;
         if (this.options != null && this.options.anaglyph) {
            int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
            int var12 = (var8 * 30 + var9 * 70) / 100;
            int var13 = (var8 * 30 + var10 * 70) / 100;
            var8 = var11;
            var9 = var12;
            var10 = var13;
         }

         var5[var6 * 4 + 0] = (byte)var8;
         var5[var6 * 4 + 1] = (byte)var9;
         var5[var6 * 4 + 2] = (byte)var10;
         var5[var6 * 4 + 3] = (byte)var7;
      }

      ((Buffer)this.imageData).clear();
      this.imageData.put(var5);
      ((Buffer)this.imageData).position(0).limit(var5.length);
      GL11.glTexSubImage2D(3553, 0, 0, 0, var2, var3, 6408, 5121, this.imageData);
   }

   public void deleteTexture(int var1) {
      this.textureNameToImageMap.remove(var1);
      ((Buffer)this.singleIntBuffer).clear();
      this.singleIntBuffer.put(var1);
      ((Buffer)this.singleIntBuffer).flip();
      GL11.glDeleteTextures(this.singleIntBuffer);
   }

   public int getTextureForDownloadableImage(String var1, String var2) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(var1);
      if (var3 != null && var3.image != null && !var3.textureSetupComplete) {
         if (var3.textureName < 0) {
            var3.textureName = this.allocateAndSetupTexture(var3.image);
         } else {
            this.setupTexture(var3.image, var3.textureName);
         }

         var3.textureSetupComplete = true;
      }

      if (var3 != null && var3.textureName >= 0) {
         return var3.textureName;
      } else {
         return var2 == null ? -1 : this.getTexture(var2);
      }
   }

   public ThreadDownloadImageData obtainImageData(String var1, ImageBuffer var2) {
      ThreadDownloadImageData var3 = (ThreadDownloadImageData)this.urlToImageDataMap.get(var1);
      if (var3 == null) {
         this.urlToImageDataMap.put(var1, new ThreadDownloadImageData(var1, var2));
      } else {
         var3.referenceCount++;
      }

      return var3;
   }

   public void releaseImageData(String var1) {
      ThreadDownloadImageData var2 = (ThreadDownloadImageData)this.urlToImageDataMap.get(var1);
      if (var2 != null) {
         var2.referenceCount--;
         if (var2.referenceCount == 0) {
            if (var2.textureName >= 0) {
               this.deleteTexture(var2.textureName);
            }

            this.urlToImageDataMap.remove(var1);
         }
      }
   }

   public void registerTextureFX(TextureFX var1) {
      for (int var2 = 0; var2 < this.textureList.size(); var2++) {
         TextureFX var3 = (TextureFX)this.textureList.get(var2);
         if (var3.tileImage == var1.tileImage && var3.iconIndex == var1.iconIndex) {
            this.textureList.remove(var2);
            var2--;
            Config.dbg("Texture removed: " + var3 + ", image: " + var3.tileImage + ", index: " + var3.iconIndex);
         }
      }

      this.textureList.add(var1);
      var1.onTick();
      Config.dbg("Texture registered: " + var1 + ", image: " + var1.tileImage + ", index: " + var1.iconIndex);
      this.dynamicTexturesUpdated = false;
   }

   private void generateMipMapsSub(int var1, int var2, int var3, int var4, ByteBuffer var5, int var6, boolean var7) {
      ByteBuffer var8 = var5;

      for (int var9 = 1; var9 <= 16; var9++) {
         int var10 = var3 >> var9 - 1;
         int var11 = var3 >> var9;
         int var12 = var4 >> var9;
         int var13 = var1 >> var9;
         int var14 = var2 >> var9;
         if (var11 <= 0 || var12 <= 0) {
            break;
         }

         ByteBuffer var15 = this.mipImageDatas[var9 - 1];

         for (int var16 = 0; var16 < var11; var16++) {
            for (int var17 = 0; var17 < var12; var17++) {
               int var18 = var8.getInt((var16 * 2 + 0 + (var17 * 2 + 0) * var10) * 4);
               int var19 = var8.getInt((var16 * 2 + 1 + (var17 * 2 + 0) * var10) * 4);
               int var20 = var8.getInt((var16 * 2 + 1 + (var17 * 2 + 1) * var10) * 4);
               int var21 = var8.getInt((var16 * 2 + 0 + (var17 * 2 + 1) * var10) * 4);
               int var22;
               if (var7) {
                  var22 = this.averageColor(this.averageColor(var18, var19), this.averageColor(var20, var21));
               } else {
                  var22 = this.weightedAverageColor(var18, var19, var20, var21);
               }

               var15.putInt((var16 + var17 * var11) * 4, var22);
            }
         }

         for (int var23 = 0; var23 < var6; var23++) {
            for (int var24 = 0; var24 < var6; var24++) {
               int var25 = var23 * var11;
               int var26 = var24 * var12;
               GL11.glTexSubImage2D(3553, var9, var13 + var25, var14 + var26, var11, var12, 6408, 5121, var15);
            }
         }

         var8 = var15;
      }
   }

   public void updateDynamicTextures() {
      this.checkHdTextures();
      this.tickCounter++;
      this.terrainTextureId = this.getTexture("/terrain.png");
      this.guiItemsTextureId = this.getTexture("/gui/items.png");

      for (int var1 = 0; var1 < this.textureList.size(); var1++) {
         TextureFX var2 = (TextureFX)this.textureList.get(var1);
         var2.anaglyphEnabled = this.options.anaglyph;
         if (!var2.getClass().getName().equals("ModTextureStatic") || !this.dynamicTexturesUpdated) {
            int var3 = 0;
            if (var2.tileImage == 0) {
               var3 = this.terrainTextureId;
            } else {
               var3 = this.guiItemsTextureId;
            }

            Dimension var4 = this.getTextureDimensions(var3);
            if (var4 == null) {
               throw new IllegalArgumentException("Unknown dimensions for texture id: " + var3);
            }

            int var5 = var4.width / 16;
            int var6 = var4.height / 16;
            this.checkImageDataSize(var4.width);
            ((Buffer)this.imageData).limit(0);
            boolean var7 = this.updateCustomTexture(var2, this.imageData, var4.width / 16);
            if (!var7 || this.imageData.limit() > 0) {
               if (this.imageData.limit() <= 0) {
                  boolean var8 = this.updateDefaultTexture(var2, this.imageData, var4.width / 16);
                  if (var8 && this.imageData.limit() <= 0) {
                     continue;
                  }
               }

               if (this.imageData.limit() <= 0) {
                  var2.onTick();
                  if (var2.imageData == null) {
                     continue;
                  }

                  int var16 = var5 * var6 * 4;
                  if (var2.imageData.length == var16) {
                     ((Buffer)this.imageData).clear();
                     this.imageData.put(var2.imageData);
                     ((Buffer)this.imageData).position(0).limit(var2.imageData.length);
                  } else {
                     this.copyScaled(var2.imageData, this.imageData, var5);
                  }
               }

               var2.bindImage(this);
               boolean var17 = this.scalesWithFastColor(var2);

               for (int var9 = 0; var9 < var2.tileSize; var9++) {
                  for (int var10 = 0; var10 < var2.tileSize; var10++) {
                     int var11 = var2.iconIndex % 16 * var5 + var9 * var5;
                     int var12 = var2.iconIndex / 16 * var6 + var10 * var6;
                     GL11.glTexSubImage2D(3553, 0, var11, var12, var5, var6, 6408, 5121, this.imageData);
                     if (useMipmaps && var9 == 0 && var10 == 0) {
                        this.generateMipMapsSub(var11, var12, var5, var6, this.imageData, var2.tileSize, var17);
                     }
                  }
               }
            }
         }
      }

      this.dynamicTexturesUpdated = true;

      for (int var13 = 0; var13 < this.textureList.size(); var13++) {
         TextureFX var14 = (TextureFX)this.textureList.get(var13);
         if (var14.textureId > 0) {
            ((Buffer)this.imageData).clear();
            this.imageData.put(var14.imageData);
            ((Buffer)this.imageData).position(0).limit(var14.imageData.length);
            GL11.glBindTexture(3553, var14.textureId);
            GL11.glTexSubImage2D(3553, 0, 0, 0, 16, 16, 6408, 5121, this.imageData);
            if (useMipmaps) {
               this.generateMipMapsSub(0, 0, 16, 16, this.imageData, var14.tileSize, false);
            }
         }
      }
   }

   private int averageColor(int var1, int var2) {
      int var3 = (var1 & 0xFF000000) >> 24 & 0xFF;
      int var4 = (var2 & 0xFF000000) >> 24 & 0xFF;
      return (var3 + var4 >> 1 << 24) + ((var1 & 16711422) + (var2 & 16711422) >> 1);
   }

   private int weightedAverageColor(int var1, int var2, int var3, int var4) {
      int var5 = this.weightedAverageColor(var1, var2);
      int var6 = this.weightedAverageColor(var3, var4);
      return this.weightedAverageColor(var5, var6);
   }

   private int weightedAverageColor(int var1, int var2) {
      int var3 = (var1 & 0xFF000000) >> 24 & 0xFF;
      int var4 = (var2 & 0xFF000000) >> 24 & 0xFF;
      int var5 = (var3 + var4) / 2;
      if (var3 == 0 && var4 == 0) {
         var3 = 1;
         var4 = 1;
      } else {
         if (var3 == 0) {
            var1 = var2;
            var5 /= 2;
         }

         if (var4 == 0) {
            var2 = var1;
            var5 /= 2;
         }
      }

      int var6 = (var1 >> 16 & 0xFF) * var3;
      int var7 = (var1 >> 8 & 0xFF) * var3;
      int var8 = (var1 & 0xFF) * var3;
      int var9 = (var2 >> 16 & 0xFF) * var4;
      int var10 = (var2 >> 8 & 0xFF) * var4;
      int var11 = (var2 & 0xFF) * var4;
      int var12 = (var6 + var9) / (var3 + var4);
      int var13 = (var7 + var10) / (var3 + var4);
      int var14 = (var8 + var11) / (var3 + var4);
      return var5 << 24 | var12 << 16 | var13 << 8 | var14;
   }

   public void refreshTextures() {
      this.textureDataMap.clear();
      this.dynamicTexturesUpdated = false;
      Config.setFontRendererUpdated(false);
      TexturePackBase var1 = this.texturePack.selectedTexturePack;

      for (int var2 : this.textureNameToImageMap.keySet()) {
         BufferedImage var3 = (BufferedImage)this.textureNameToImageMap.get(var2);
         this.setupTexture(var3, var2);
      }

      for (ThreadDownloadImageData var5 : this.urlToImageDataMap.values()) {
         var5.textureSetupComplete = false;
      }

      for (String var13 : this.textureMap.keySet()) {
         try {
            BufferedImage var6;
            if (var13.startsWith("##")) {
               var6 = this.unwrapImageByColumns(this.readTextureImage(var1.getResourceAsStream(var13.substring(2))));
            } else if (var13.startsWith("%clamp%")) {
               this.clampTexture = true;
               var6 = this.readTextureImage(var1.getResourceAsStream(var13.substring(7)));
            } else if (var13.startsWith("%blur%")) {
               this.blurTexture = true;
               var6 = this.readTextureImage(var1.getResourceAsStream(var13.substring(6)));
            } else {
               var6 = this.readTextureImage(var1.getResourceAsStream(var13));
            }

            int var7 = (Integer)this.textureMap.get(var13);
            this.setupTexture(var6, var7);
            this.blurTexture = false;
            this.clampTexture = false;
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }

      for (String var14 : this.field_28151_c.keySet()) {
         try {
            BufferedImage var15;
            if (var14.startsWith("##")) {
               var15 = this.unwrapImageByColumns(this.readTextureImage(var1.getResourceAsStream(var14.substring(2))));
            } else if (var14.startsWith("%clamp%")) {
               this.clampTexture = true;
               var15 = this.readTextureImage(var1.getResourceAsStream(var14.substring(7)));
            } else if (var14.startsWith("%blur%")) {
               this.blurTexture = true;
               var15 = this.readTextureImage(var1.getResourceAsStream(var14.substring(6)));
            } else {
               var15 = this.readTextureImage(var1.getResourceAsStream(var14));
            }

            this.func_28147_a(var15, (int[])this.field_28151_c.get(var14));
            this.blurTexture = false;
            this.clampTexture = false;
         } catch (IOException var8) {
            var8.printStackTrace();
         }
      }
   }

   private BufferedImage readTextureImage(InputStream var1) throws IOException {
      BufferedImage var2 = ImageIO.read(var1);
      var1.close();
      return var2;
   }

   public void bindTexture(int var1) {
      if (var1 >= 0) {
         GL11.glBindTexture(3553, var1);
      }
   }

   private void setTextureDimension(int var1, Dimension var2) {
      this.textureDimensionsMap.put(new Integer(var1), var2);
      if (var1 == this.terrainTextureId) {
         Config.setIconWidthTerrain(var2.width / 16);
         this.updateDinamicTextures(0, var2);
      }

      if (var1 == this.guiItemsTextureId) {
         Config.setIconWidthItems(var2.width / 16);
         this.updateDinamicTextures(1, var2);
      }
   }

   private Dimension getTextureDimensions(int var1) {
      return (Dimension)this.textureDimensionsMap.get(new Integer(var1));
   }

   private void updateDinamicTextures(int var1, Dimension var2) {
      this.checkHdTextures();

      for (int var3 = 0; var3 < this.textureList.size(); var3++) {
         TextureFX var4 = (TextureFX)this.textureList.get(var3);
         if (var4.tileImage == var1 && var4 instanceof TextureHDFX) {
            TextureHDFX var5 = (TextureHDFX)var4;
            var5.setTexturePackBase(this.texturePack.selectedTexturePack);
            var5.setTileWidth(var2.width / 16);
            var5.onTickHD();
         }
      }
   }

   public boolean updateCustomTexture(TextureFX var1, ByteBuffer var2, int var3) {
      if (var1.iconIndex == Block.waterStill.blockIndexInTexture) {
         return Config.isGeneratedWater() ? false : this.updateCustomTexture(var1, "/custom_water_still.png", var2, var3, Config.isAnimatedWater(), 1);
      } else if (var1.iconIndex == Block.waterStill.blockIndexInTexture + 1) {
         return Config.isGeneratedWater() ? false : this.updateCustomTexture(var1, "/custom_water_flowing.png", var2, var3, Config.isAnimatedWater(), 1);
      } else if (var1.iconIndex == Block.lavaStill.blockIndexInTexture) {
         return Config.isGeneratedLava() ? false : this.updateCustomTexture(var1, "/custom_lava_still.png", var2, var3, Config.isAnimatedLava(), 1);
      } else if (var1.iconIndex == Block.lavaStill.blockIndexInTexture + 1) {
         return Config.isGeneratedLava() ? false : this.updateCustomTexture(var1, "/custom_lava_flowing.png", var2, var3, Config.isAnimatedLava(), 1);
      } else if (var1.iconIndex == Block.portal.blockIndexInTexture) {
         return this.updateCustomTexture(var1, "/custom_portal.png", var2, var3, Config.isAnimatedPortal(), 1);
      } else if (var1.iconIndex == Block.fire.blockIndexInTexture) {
         return this.updateCustomTexture(var1, "/custom_fire_n_s.png", var2, var3, Config.isAnimatedFire(), 1);
      } else {
         return var1.iconIndex == Block.fire.blockIndexInTexture + 16 ? this.updateCustomTexture(var1, "/custom_fire_e_w.png", var2, var3, Config.isAnimatedFire(), 1) : false;
      }
   }

   private boolean updateDefaultTexture(TextureFX var1, ByteBuffer var2, int var3) {
      if (this.texturePack.selectedTexturePack instanceof TexturePackDefault) {
         return false;
      } else if (var1.iconIndex == Block.waterStill.blockIndexInTexture) {
         return Config.isGeneratedWater() ? false : this.updateDefaultTexture(var1, var2, var3, false, 1);
      } else if (var1.iconIndex == Block.waterStill.blockIndexInTexture + 1) {
         return Config.isGeneratedWater() ? false : this.updateDefaultTexture(var1, var2, var3, Config.isAnimatedWater(), 1);
      } else if (var1.iconIndex == Block.lavaStill.blockIndexInTexture) {
         return Config.isGeneratedLava() ? false : this.updateDefaultTexture(var1, var2, var3, false, 1);
      } else if (var1.iconIndex == Block.lavaStill.blockIndexInTexture + 1) {
         return Config.isGeneratedLava() ? false : this.updateDefaultTexture(var1, var2, var3, Config.isAnimatedLava(), 3);
      } else {
         return false;
      }
   }

   private boolean updateDefaultTexture(TextureFX var1, ByteBuffer var2, int var3, boolean var4, int var5) {
      int var6 = var1.iconIndex;
      if (!var4 && this.dynamicTexturesUpdated) {
         return true;
      }

      byte[] var7 = this.getTerrainIconData(var6, var3);
      if (var7 == null) {
         return false;
      }

      ((Buffer)var2).clear();
      int var8 = var7.length;
      if (var4) {
         int var9 = var3 - this.tickCounter / var5 % var3;
         int var10 = var9 * var3 * 4;
         var2.put(var7, var10, var8 - var10);
         var2.put(var7, 0, var10);
      } else {
         var2.put(var7, 0, var8);
      }

      ((Buffer)var2).position(0).limit(var8);
      return true;
   }

   private boolean updateCustomTexture(TextureFX var1, String var2, ByteBuffer var3, int var4, boolean var5, int var6) {
      byte[] var7 = this.getCustomTextureData(var2, var4);
      if (var7 == null) {
         return false;
      }

      if (!var5 && this.dynamicTexturesUpdated) {
         return true;
      }

      int var8 = var4 * var4 * 4;
      int var9 = var7.length / var8;
      int var10 = this.tickCounter / var6 % var9;
      int var11 = 0;
      if (var5) {
         var11 = var8 * var10;
      }

      ((Buffer)var3).clear();
      var3.put(var7, var11, var8);
      ((Buffer)var3).position(0).limit(var8);
      return true;
   }

   private byte[] getTerrainIconData(int var1, int var2) {
      String var3 = "Tile-" + var1;
      byte[] var4 = this.getCustomTextureData(var3, var2);
      if (var4 != null) {
         return var4;
      }

      byte[] var5 = this.getCustomTextureData("/terrain.png", var2 * 16);
      if (var5 == null) {
         return null;
      }

      var4 = new byte[var2 * var2 * 4];
      int var6 = var1 % 16;
      int var7 = var1 / 16;
      int var8 = var6 * var2;
      int var9 = var7 * var2;
      
      for (int var12 = 0; var12 < var2; var12++) {
         int var13 = var9 + var12;

         for (int var14 = 0; var14 < var2; var14++) {
            int var15 = var8 + var14;
            int var16 = 4 * (var15 + var13 * var2 * 16);
            int var17 = 4 * (var14 + var12 * var2);
            var4[var17 + 0] = var5[var16 + 0];
            var4[var17 + 1] = var5[var16 + 1];
            var4[var17 + 2] = var5[var16 + 2];
            var4[var17 + 3] = var5[var16 + 3];
         }
      }

      this.setCustomTextureData(var3, var4);
      return var4;
   }

   public byte[] getCustomTextureData(String var1, int var2) {
      byte[] var3 = (byte[])this.textureDataMap.get(var1);
      if (var3 == null) {
         if (this.textureDataMap.containsKey(var1)) {
            return null;
         }

         var3 = this.loadImage(var1, var2);
         this.textureDataMap.put(var1, var3);
      }

      return var3;
   }

   private void setCustomTextureData(String var1, byte[] var2) {
      this.textureDataMap.put(var1, var2);
   }

   private byte[] loadImage(String var1, int var2) {
      try {
         TexturePackBase var3 = this.texturePack.selectedTexturePack;
         if (var3 == null) {
            return null;
         }

         InputStream var4 = var3.getResourceAsStream(var1);
         if (var4 == null) {
            return null;
         }

         BufferedImage var5 = this.readTextureImage(var4);
         if (var5 == null) {
            return null;
         }

         if (var2 > 0 && var5.getWidth() != var2) {
            double var6 = var5.getHeight() / var5.getWidth();
            int var8 = (int)(var2 * var6);
            var5 = scaleBufferedImage(var5, var2, var8);
         }

         int var19 = var5.getWidth();
         int var7 = var5.getHeight();
         int[] var20 = new int[var19 * var7];
         byte[] var9 = new byte[var19 * var7 * 4];
         var5.getRGB(0, 0, var19, var7, var20, 0, var19);

         for (int var10 = 0; var10 < var20.length; var10++) {
            int var11 = var20[var10] >> 24 & 0xFF;
            int var12 = var20[var10] >> 16 & 0xFF;
            int var13 = var20[var10] >> 8 & 0xFF;
            int var14 = var20[var10] & 0xFF;
            if (this.options != null && this.options.anaglyph) {
               int var15 = (var12 * 30 + var13 * 59 + var14 * 11) / 100;
               int var16 = (var12 * 30 + var13 * 70) / 100;
               int var17 = (var12 * 30 + var14 * 70) / 100;
               var12 = var15;
               var13 = var16;
               var14 = var17;
            }

            var9[var10 * 4 + 0] = (byte)var12;
            var9[var10 * 4 + 1] = (byte)var13;
            var9[var10 * 4 + 2] = (byte)var14;
            var9[var10 * 4 + 3] = (byte)var11;
         }

         return var9;
      } catch (Exception var18) {
         var18.printStackTrace();
         return null;
      }
   }

   public static BufferedImage scaleBufferedImage(BufferedImage var0, int var1, int var2) {
      BufferedImage var3 = new BufferedImage(var1, var2, 2);
      Graphics2D var4 = var3.createGraphics();
      var4.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      var4.drawImage(var0, 0, 0, var1, var2, null);
      return var3;
   }

   private void checkImageDataSize(int var1) {
      if (this.imageData != null) {
         int var2 = var1 * var1 * 4;
         if (this.imageData.capacity() >= var2) {
            return;
         }
      }

      this.allocateImageData(var1);
   }

   private void allocateImageData(int var1) {
      int var2 = var1 * var1 * 4;
      this.imageData = GLAllocation.createDirectByteBuffer(var2);
      List<ByteBuffer> var3 = new ArrayList<ByteBuffer>();

      for (int var4 = var1 / 2; var4 > 0; var4 /= 2) {
         int var5 = var4 * var4 * 4;
         ByteBuffer var6 = GLAllocation.createDirectByteBuffer(var5);
         var3.add(var6);
      }

      this.mipImageDatas = (ByteBuffer[]) var3.toArray(new ByteBuffer[var3.size()]);
   }

   public void checkHdTextures() {
      if (!this.hdTexturesInstalled) {
         Minecraft var1 = Config.getMinecraft();
         if (var1 != null) {
            this.registerTextureFX(new TextureHDLavaFX());
            this.registerTextureFX(new TextureHDWaterFX());
            this.registerTextureFX(new TextureHDPortalFX());
            this.registerTextureFX(new TextureHDCompassFX(var1));
            this.registerTextureFX(new TextureHDWatchFX(var1));
            this.registerTextureFX(new TextureHDWaterFlowFX());
            this.registerTextureFX(new TextureHDLavaFlowFX());
            this.registerTextureFX(new TextureHDFlamesFX(0));
            this.registerTextureFX(new TextureHDFlamesFX(1));
            this.hdTexturesInstalled = true;
         }
      }
   }

   private int getMaxMipmapLevel(int var1) {
      int var2;
      for (var2 = 0; var1 > 0; var2++) {
         var1 /= 2;
      }

      return var2 - 1;
   }

   private void copyScaled(byte[] var1, ByteBuffer var2, int var3) {
      int var4 = (int)Math.sqrt(var1.length / 4);
      int var5 = var3 / var4;
      byte[] var6 = new byte[4];
      ((Buffer)var2).clear();
      if (var5 > 1) {
         for (int var8 = 0; var8 < var4; var8++) {
            int var9 = var8 * var4;
            int var10 = var8 * var5;
            int var11 = var10 * var3;

            for (int var12 = 0; var12 < var4; var12++) {
               int var13 = (var12 + var9) * 4;
               var6[0] = var1[var13];
               var6[1] = var1[var13 + 1];
               var6[2] = var1[var13 + 2];
               var6[3] = var1[var13 + 3];
               int var14 = var12 * var5;
               int var15 = var14 + var11;

               for (int var16 = 0; var16 < var5; var16++) {
                  int var17 = var15 + var16 * var3;
                  ((Buffer)var2).position(var17 * 4);

                  for (int var18 = 0; var18 < var5; var18++) {
                     var2.put(var6);
                  }
               }
            }
         }
      }

      ((Buffer)var2).position(0).limit(var3 * var3 * 4);
   }

   private boolean scalesWithFastColor(TextureFX var1) {
      return !var1.getClass().getName().equals("ModTextureStatic");
   }
}