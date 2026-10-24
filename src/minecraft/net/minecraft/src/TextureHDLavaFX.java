package net.minecraft.src;

public class TextureHDLavaFX extends TextureFX implements TextureHDFX {
   private TexturePackBase texturePackBase;
   private int tileWidth = 0;
   protected float[] buf1;
   protected float[] buf2;
   protected float[] buf3;
   protected float[] buf4;

   public TextureHDLavaFX() {
      super(Block.lavaMoving.blockIndexInTexture);
      this.tileWidth = 16;
      this.imageData = new byte[this.tileWidth * this.tileWidth * 4];
      this.buf1 = new float[this.tileWidth * this.tileWidth];
      this.buf2 = new float[this.tileWidth * this.tileWidth];
      this.buf3 = new float[this.tileWidth * this.tileWidth];
      this.buf4 = new float[this.tileWidth * this.tileWidth];
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
   }

   @Override
   public void setTexturePackBase(TexturePackBase var1) {
      this.texturePackBase = var1;
   }

   @Override
   public void onTickHD() {
      if (!Config.isAnimatedLava()) {
         this.imageData = null;
      }

      if (this.imageData != null) {
         int var1 = this.tileWidth - 1;

         for (int var2 = 0; var2 < this.tileWidth; var2++) {
            for (int var3 = 0; var3 < this.tileWidth; var3++) {
               float var4 = 0.0F;
               int var5 = (int)(MathHelper.sin(var3 * 3.141593F * 2.0F / 16.0F) * 1.2F);
               int var6 = (int)(MathHelper.sin(var2 * 3.141593F * 2.0F / 16.0F) * 1.2F);

               for (int var7 = var2 - 1; var7 <= var2 + 1; var7++) {
                  for (int var8 = var3 - 1; var8 <= var3 + 1; var8++) {
                     int var9 = var7 + var5 & var1;
                     int var10 = var8 + var6 & var1;
                     var4 += this.buf1[var9 + var10 * this.tileWidth];
                  }
               }

               this.buf2[var2 + var3 * this.tileWidth] = var4 / 10.0F + (this.buf3[(var2 + 0 & var1) + (var3 + 0 & var1) * this.tileWidth] + this.buf3[(var2 + 1 & var1) + (var3 + 0 & var1) * this.tileWidth] + this.buf3[(var2 + 1 & var1) + (var3 + 1 & var1) * this.tileWidth] + this.buf3[(var2 + 0 & var1) + (var3 + 1 & var1) * this.tileWidth]) / 4.0F * 0.8F;
               this.buf3[var2 + var3 * this.tileWidth] = this.buf3[var2 + var3 * this.tileWidth] + this.buf4[var2 + var3 * this.tileWidth] * 0.01F;
               if (this.buf3[var2 + var3 * this.tileWidth] < 0.0F) {
                  this.buf3[var2 + var3 * this.tileWidth] = 0.0F;
               }

               this.buf4[var2 + var3 * this.tileWidth] = this.buf4[var2 + var3 * this.tileWidth] - 0.06F;
               if (Math.random() < 0.005) {
                  this.buf4[var2 + var3 * this.tileWidth] = 1.5F;
               }
            }
         }

         float[] var13 = this.buf2;
         this.buf2 = this.buf1;
         this.buf1 = var13;
         int var14 = this.tileWidth * this.tileWidth;

         for (int var15 = 0; var15 < var14; var15++) {
            float var16 = this.buf1[var15] * 2.0F;
            if (var16 > 1.0F) {
               var16 = 1.0F;
            }

            if (var16 < 0.0F) {
               var16 = 0.0F;
            }

            float var17 = var16;
            int var18 = (int)(var17 * 100.0F + 155.0F);
            int var19 = (int)(var17 * var17 * 255.0F);
            int var20 = (int)(var17 * var17 * var17 * var17 * 128.0F);
            if (this.anaglyphEnabled) {
               int var21 = (var18 * 30 + var19 * 59 + var20 * 11) / 100;
               int var11 = (var18 * 30 + var19 * 70) / 100;
               int var12 = (var18 * 30 + var20 * 70) / 100;
               var18 = var21;
               var19 = var11;
               var20 = var12;
            }

            this.imageData[var15 * 4 + 0] = (byte)var18;
            this.imageData[var15 * 4 + 1] = (byte)var19;
            this.imageData[var15 * 4 + 2] = (byte)var20;
            this.imageData[var15 * 4 + 3] = -1;
         }
      }
   }
}
