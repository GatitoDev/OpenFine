package net.minecraft.src;

import java.util.Random;

public class TextureHDFlamesFX extends TextureFX implements TextureHDFX {
   private int tileWidth = 0;
   private int fireHeight = 0;
   protected float[] buf1;
   protected float[] buf2;
   private Random random = new Random();

   public TextureHDFlamesFX(int var1) {
      super(Block.fire.blockIndexInTexture + var1 * 16);
      this.tileWidth = 16;
      this.fireHeight = this.tileWidth + this.tileWidth / 4;
      this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
      this.buf1 = new float[this.tileWidth * this.fireHeight];
      this.buf2 = new float[this.tileWidth * this.fireHeight];
   }

   @Override
   public void setTileWidth(int var1) {
      if (var1 > Config.getMaxDynamicTileWidth()) {
         var1 = Config.getMaxDynamicTileWidth();
      }

      this.tileWidth = var1;
      this.fireHeight = var1 + var1 / 4;
      this.imageData = new byte[var1 * var1 * 4];
      this.buf1 = new float[var1 * this.fireHeight];
      this.buf2 = new float[var1 * this.fireHeight];
   }

   @Override
   public void setTexturePackBase(TexturePackBase var1) {
   }

   @Override
   public void onTickHD() {
      if (!Config.isAnimatedFire()) {
         this.imageData = null;
      }

      if (this.imageData != null) {
         float var1 = 1.01F + 0.8F / this.tileWidth;
         float var2 = 3.0F + this.tileWidth / 16.0F;

         for (int var3 = 0; var3 < this.tileWidth; var3++) {
            for (int var4 = 0; var4 < this.fireHeight; var4++) {
               int var5 = this.fireHeight - this.tileWidth / 8;
               float var6 = this.buf1[var3 + (var4 + 1) % this.fireHeight * this.tileWidth] * var5;

               for (int var7 = var3 - 1; var7 <= var3 + 1; var7++) {
                  for (int var8 = var4; var8 <= var4 + 1; var8++) {
                     int var9 = var7;
                     int var10 = var8;
                     if (var9 >= 0 && var10 >= 0 && var9 < this.tileWidth && var10 < this.fireHeight) {
                        var6 += this.buf1[var9 + var10 * this.tileWidth];
                     }

                     var5++;
                  }
               }

               this.buf2[var3 + var4 * this.tileWidth] = var6 / (var5 * var1);
               if (var4 >= this.fireHeight - this.tileWidth / 16) {
                  this.buf2[var3 + var4 * this.tileWidth] = this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat() * var2 + this.random.nextFloat() * 0.1F + 0.2F;
               }
            }
         }

         float[] var15 = this.buf2;
         this.buf2 = this.buf1;
         this.buf1 = var15;
         int var16 = this.tileWidth * this.tileWidth;

         for (int var17 = 0; var17 < var16; var17++) {
            float var18 = this.buf1[var17] * 1.8F;
            if (var18 > 1.0F) {
               var18 = 1.0F;
            }

            if (var18 < 0.0F) {
               var18 = 0.0F;
            }

            float var19 = var18;
            int var21 = (int)(var19 * 155.0F + 100.0F);
            int var22 = (int)(var19 * var19 * 255.0F);
            int var23 = (int)(var19 * var19 * var19 * var19 * var19 * var19 * var19 * var19 * var19 * var19 * 255.0F);
            char var11 = 255;
            if (var19 < 0.5F) {
               var11 = 0;
            }

            var19 = (var19 - 0.5F) * 2.0F;
            if (this.anaglyphEnabled) {
               int var12 = (var21 * 30 + var22 * 59 + var23 * 11) / 100;
               int var13 = (var21 * 30 + var22 * 70) / 100;
               int var14 = (var21 * 30 + var23 * 70) / 100;
               var21 = var12;
               var22 = var13;
               var23 = var14;
            }

            int var24 = var17 * 4;
            this.imageData[var24 + 0] = (byte)var21;
            this.imageData[var24 + 1] = (byte)var22;
            this.imageData[var24 + 2] = (byte)var23;
            this.imageData[var24 + 3] = (byte)var11;
         }
      }
   }
}
