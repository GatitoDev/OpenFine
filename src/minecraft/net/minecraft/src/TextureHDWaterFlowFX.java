package net.minecraft.src;

public class TextureHDWaterFlowFX extends TextureFX implements TextureHDFX {
   private TexturePackBase texturePackBase;
   private int tileWidth = 16;
   protected float[] buf1;
   protected float[] buf2;
   protected float[] buf3;
   protected float[] buf4;
   private int tickCounter;

   public TextureHDWaterFlowFX() {
      super(Block.waterMoving.blockIndexInTexture + 1);
      this.tileWidth = 16;
      this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
      this.buf1 = new float[this.tileWidth * this.tileWidth];
      this.buf2 = new float[this.tileWidth * this.tileWidth];
      this.buf3 = new float[this.tileWidth * this.tileWidth];
      this.buf4 = new float[this.tileWidth * this.tileWidth];
      this.tileSize = 2;
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

               for (int var5 = var3 - 2; var5 <= var3; var5++) {
                  int var6 = var2 & var1;
                  int var7 = var5 & var1;
                  var4 += this.buf1[var6 + var7 * this.tileWidth];
               }

               this.buf2[var2 + var3 * this.tileWidth] = var4 / 3.2F + this.buf3[var2 + var3 * this.tileWidth] * 0.8F;
            }
         }

         for (int var14 = 0; var14 < this.tileWidth; var14++) {
            for (int var16 = 0; var16 < this.tileWidth; var16++) {
               this.buf3[var14 + var16 * this.tileWidth] = this.buf3[var14 + var16 * this.tileWidth] + this.buf4[var14 + var16 * this.tileWidth] * 0.05F;
               if (this.buf3[var14 + var16 * this.tileWidth] < 0.0F) {
                  this.buf3[var14 + var16 * this.tileWidth] = 0.0F;
               }

               this.buf4[var14 + var16 * this.tileWidth] = this.buf4[var14 + var16 * this.tileWidth] - 0.3F;
               if (Math.random() < 0.2) {
                  this.buf4[var14 + var16 * this.tileWidth] = 0.5F;
               }
            }
         }

         float[] var15 = this.buf2;
         this.buf2 = this.buf1;
         this.buf1 = var15;
         int var17 = this.tileWidth * this.tileWidth - 1;

         for (int var18 = 0; var18 < this.tileWidth * this.tileWidth; var18++) {
            float var19 = this.buf1[var18 - this.tickCounter * this.tileWidth & var17];
            if (var19 > 1.0F) {
               var19 = 1.0F;
            }

            if (var19 < 0.0F) {
               var19 = 0.0F;
            }

            float var20 = var19 * var19;
            int var21 = (int)(32.0F + var20 * 32.0F);
            int var8 = (int)(50.0F + var20 * 64.0F);
            int var9 = 255;
            int var10 = (int)(146.0F + var20 * 50.0F);
            if (this.anaglyphEnabled) {
               int var11 = (var21 * 30 + var8 * 59 + var9 * 11) / 100;
               int var12 = (var21 * 30 + var8 * 70) / 100;
               int var13 = (var21 * 30 + var9 * 70) / 100;
               var21 = var11;
               var8 = var12;
               var9 = var13;
            }

            this.imageData[var18 * 4 + 0] = (byte)var21;
            this.imageData[var18 * 4 + 1] = (byte)var8;
            this.imageData[var18 * 4 + 2] = (byte)var9;
            this.imageData[var18 * 4 + 3] = (byte)var10;
         }
      }
   }
}
