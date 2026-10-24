package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class TextureHDCompassFX extends TextureFX implements TextureHDFX {
   private Minecraft mc;
   private int tileWidth = 0;
   private TexturePackBase texturePackBase = null;
   private byte[] baseImageData;
   private int[] compassIconImageData;
   private double showAngle;
   private double angleDiff;

   public TextureHDCompassFX(Minecraft var1) {
      super(Item.compass.getIconFromDamage(0));
      this.mc = var1;
      this.tileWidth = 16;
      this.setup();
   }

   @Override
   public void setTileWidth(int var1) {
      this.tileWidth = var1;
      this.setup();
   }

   @Override
   public void setTexturePackBase(TexturePackBase var1) {
      this.texturePackBase = var1;
   }

   private void setup() {
      this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
      this.compassIconImageData = new int[this.tileWidth * this.tileWidth];
      this.tileImage = 1;

      try {
         BufferedImage var1 = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
         if (this.texturePackBase != null) {
            var1 = ImageIO.read(this.texturePackBase.getResourceAsStream("/gui/items.png"));
         }

         int var2 = this.iconIndex % 16 * this.tileWidth;
         int var3 = this.iconIndex / 16 * this.tileWidth;
         var1.getRGB(var2, var3, this.tileWidth, this.tileWidth, this.compassIconImageData, 0, this.tileWidth);
         this.baseImageData = new byte[this.imageData.length];
         int var4 = this.tileWidth * this.tileWidth;

         for (int var5 = 0; var5 < var4; var5++) {
            int var6 = this.compassIconImageData[var5] >> 24 & 0xFF;
            int var7 = this.compassIconImageData[var5] >> 16 & 0xFF;
            int var8 = this.compassIconImageData[var5] >> 8 & 0xFF;
            int var9 = this.compassIconImageData[var5] >> 0 & 0xFF;
            if (this.anaglyphEnabled) {
               int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
               int var11 = (var7 * 30 + var8 * 70) / 100;
               int var12 = (var7 * 30 + var9 * 70) / 100;
               var7 = var10;
               var8 = var11;
               var9 = var12;
            }

            this.baseImageData[var5 * 4 + 0] = (byte)var7;
            this.baseImageData[var5 * 4 + 1] = (byte)var8;
            this.baseImageData[var5 * 4 + 2] = (byte)var9;
            this.baseImageData[var5 * 4 + 3] = (byte)var6;
         }
      } catch (IOException var13) {
         var13.printStackTrace();
      }
   }

   @Override
   public void onTickHD() {
      double var2 = this.tileWidth / 2 + 0.5;
      double var4 = this.tileWidth / 2 - 0.5;
      double var6 = 0.3 * (this.tileWidth / 16);
      System.arraycopy(this.baseImageData, 0, this.imageData, 0, this.imageData.length);
      double var8 = 0.0;
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         ChunkCoordinates var10 = this.mc.theWorld.getSpawnPoint();
         double var11 = var10.x - this.mc.thePlayer.posX;
         double var13 = var10.z - this.mc.thePlayer.posZ;
         var8 = (this.mc.thePlayer.rotationYaw - 90.0F) * Math.PI / 180.0 - Math.atan2(var13, var11);
         if (this.mc.theWorld.worldProvider.isNether) {
            var8 = Math.random() * (float) Math.PI * 2.0;
         }
      }

      double var27 = var8 - this.showAngle;

      while (var27 < -Math.PI) {
         var27 += Math.PI * 2;
      }

      while (var27 >= Math.PI) {
         var27 -= Math.PI * 2;
      }

      if (var27 < -1.0) {
         var27 = -1.0;
      }

      if (var27 > 1.0) {
         var27 = 1.0;
      }

      this.angleDiff += var27 * 0.1;
      this.angleDiff *= 0.8;
      this.showAngle = this.showAngle + this.angleDiff;
      double var12 = Math.sin(this.showAngle);
      double var14 = Math.cos(this.showAngle);

      for (int var16 = -4; var16 <= 4; var16++) {
         int var17 = (int)(var2 + var14 * var16 * var6);
         int var18 = (int)(var4 - var12 * var16 * var6 * 0.5);
         int var19 = var18 * this.tileWidth + var17;
         int var20 = 100;
         int var21 = 100;
         int var22 = 100;
         char var23 = 255;
         if (this.anaglyphEnabled) {
            int var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
            int var25 = (var20 * 30 + var21 * 70) / 100;
            int var26 = (var20 * 30 + var22 * 70) / 100;
            var20 = var24;
            var21 = var25;
            var22 = var26;
         }

         int var36 = var19 * 4;
         this.imageData[var36 + 0] = (byte)var20;
         this.imageData[var36 + 1] = (byte)var21;
         this.imageData[var36 + 2] = (byte)var22;
         this.imageData[var36 + 3] = (byte)var23;
      }

      for (int var28 = -8; var28 <= 16; var28++) {
         int var29 = (int)(var2 + var12 * var28 * var6);
         int var30 = (int)(var4 + var14 * var28 * var6 * 0.5);
         int var31 = var30 * this.tileWidth + var29;
         int var32 = var28 < 0 ? 100 : 255;
         int var33 = var28 < 0 ? 100 : 20;
         int var34 = var28 < 0 ? 100 : 20;
         char var35 = 255;
         if (this.anaglyphEnabled) {
            int var37 = (var32 * 30 + var33 * 59 + var34 * 11) / 100;
            int var39 = (var32 * 30 + var33 * 70) / 100;
            int var40 = (var32 * 30 + var34 * 70) / 100;
            var32 = var37;
            var33 = var39;
            var34 = var40;
         }

         int var38 = var31 * 4;
         this.imageData[var38 + 0] = (byte)var32;
         this.imageData[var38 + 1] = (byte)var33;
         this.imageData[var38 + 2] = (byte)var34;
         this.imageData[var38 + 3] = (byte)var35;
      }
   }
}
