package net.minecraft.src;

public class TextureHDWaterFX extends TextureFX implements TextureHDFX {
   private TexturePackBase texturePackBase;
   private int tileWidth = 0;
   protected float[] buf1;
   protected float[] buf2;
   protected float[] buf3;
   protected float[] buf4;
   private int tickCounter;

   public TextureHDWaterFX() {
      super(Block.waterMoving.blockIndexInTexture);
      this.tileWidth = 16;
      this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
      this.buf1 = new float[this.tileWidth * this.tileWidth];
      this.buf2 = new float[this.tileWidth * this.tileWidth];
      this.buf3 = new float[this.tileWidth * this.tileWidth];
      this.buf4 = new float[this.tileWidth * this.tileWidth];
      this.tickCounter = 0;
   }

   @Override
   public void setTileWidth(int var1) {
      if (var1 > Config.getMaxDynamicTileWidth()) {
         var1 = Config.getMaxDynamicTileWidth();
      }

      this.tileWidth = var1;
      this.imageData = new byte[var1 * var1 * 4];
      this.buf1 = new float[var1 * var1];
      this.buf2 = new float[var1 * var1];
      this.buf3 = new float[var1 * var1];
      this.buf4 = new float[var1 * var1];
      this.tickCounter = 0;
   }

   @Override
   public void setTexturePackBase(TexturePackBase var1) {
      this.texturePackBase = var1;
   }

   @Override
   public void onTickHD() {
      if (!Config.isAnimatedWater()) {
         this.imageData = null;
      }

      if (this.imageData != null) {
         this.tickCounter++;
         int var1 = this.tileWidth - 1;

         for (int var2 = 0; var2 < this.tileWidth; var2++) {
            for (int var3 = 0; var3 < this.tileWidth; var3++) {
               float var4 = 0.0F;

               for (int var5 = var2 - 1; var5 <= var2 + 1; var5++) {
                  int var6 = var5 & var1;
                  int var7 = var3 & var1;
                  var4 += this.buf1[var6 + var7 * this.tileWidth];
               }

               this.buf2[var2 + var3 * this.tileWidth] = var4 / 3.3F + this.buf3[var2 + var3 * this.tileWidth] * 0.8F;
            }
         }

         for (int var13 = 0; var13 < this.tileWidth; var13++) {
            for (int var15 = 0; var15 < this.tileWidth; var15++) {
               this.buf3[var13 + var15 * this.tileWidth] = this.buf3[var13 + var15 * this.tileWidth] + this.buf4[var13 + var15 * this.tileWidth] * 0.05F;
               if (this.buf3[var13 + var15 * this.tileWidth] < 0.0F) {
                  this.buf3[var13 + var15 * this.tileWidth] = 0.0F;
               }

               this.buf4[var13 + var15 * this.tileWidth] = this.buf4[var13 + var15 * this.tileWidth] - 0.1F;
               if (Math.random() < 0.05) {
                  this.buf4[var13 + var15 * this.tileWidth] = 0.5F;
               }
            }
         }

         float[] var14 = this.buf2;
         this.buf2 = this.buf1;
         this.buf1 = var14;

         for (int var16 = 0; var16 < this.tileWidth * this.tileWidth; var16++) {
            float var17 = this.buf1[var16];
            if (var17 > 1.0F) {
               var17 = 1.0F;
            }

            if (var17 < 0.0F) {
               var17 = 0.0F;
            }

            float var18 = var17 * var17;
            int var19 = (int)(32.0F + var18 * 32.0F);
            int var20 = (int)(50.0F + var18 * 64.0F);
            int var8 = 255;
            int var9 = (int)(146.0F + var18 * 50.0F);
            if (this.anaglyphEnabled) {
               int var10 = (var19 * 30 + var20 * 59 + var8 * 11) / 100;
               int var11 = (var19 * 30 + var20 * 70) / 100;
               int var12 = (var19 * 30 + var8 * 70) / 100;
               var19 = var10;
               var20 = var11;
               var8 = var12;
            }

            this.imageData[var16 * 4 + 0] = (byte)var19;
            this.imageData[var16 * 4 + 1] = (byte)var20;
            this.imageData[var16 * 4 + 2] = (byte)var8;
            this.imageData[var16 * 4 + 3] = (byte)var9;
         }
      }
   }
}
