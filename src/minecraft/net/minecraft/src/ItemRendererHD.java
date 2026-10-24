package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class ItemRendererHD extends ItemRenderer {
   private Minecraft minecraft = null;

   public ItemRendererHD(Minecraft var1) {
      super(var1);
      this.minecraft = var1;
   }

   public void renderItem(EntityLiving var1, ItemStack var2) {
      if (var2.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var2.itemID].getRenderType())) {
         super.renderItem(var1, var2);
      } else {
         int var3 = Config.getIconWidthTerrain();
         if (var3 <= 16) {
            super.renderItem(var1, var2);
         } else {
            GL11.glPushMatrix();
            if (var2.itemID < 256) {
               GL11.glBindTexture(3553, this.minecraft.renderEngine.getTexture("/terrain.png"));
               var3 = Config.getIconWidthTerrain();
            } else {
               GL11.glBindTexture(3553, this.minecraft.renderEngine.getTexture("/gui/items.png"));
               var3 = Config.getIconWidthItems();
            }

            Tessellator var4 = Tessellator.instance;
            int var5 = var1.getItemIcon(var2);
            float var6 = (var5 % 16 * 16 + 0.0F) / 256.0F;
            float var7 = (var5 % 16 * 16 + 15.99F) / 256.0F;
            float var8 = (var5 / 16 * 16 + 0.0F) / 256.0F;
            float var9 = (var5 / 16 * 16 + 15.99F) / 256.0F;
            float var10 = 1.0F;
            float var11 = 0.0F;
            float var12 = 0.3F;
            GL11.glEnable(32826);
            GL11.glTranslatef(-var11, -var12, 0.0F);
            float var13 = 1.5F;
            GL11.glScalef(var13, var13, var13);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            float var14 = 0.0625F;
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 0.0F, 1.0F);
            var4.addVertexWithUV(0.0, 0.0, 0.0, var7, var9);
            var4.addVertexWithUV(var10, 0.0, 0.0, var6, var9);
            var4.addVertexWithUV(var10, 1.0, 0.0, var6, var8);
            var4.addVertexWithUV(0.0, 1.0, 0.0, var7, var8);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 0.0F, -1.0F);
            var4.addVertexWithUV(0.0, 1.0, 0.0F - var14, var7, var8);
            var4.addVertexWithUV(var10, 1.0, 0.0F - var14, var6, var8);
            var4.addVertexWithUV(var10, 0.0, 0.0F - var14, var6, var9);
            var4.addVertexWithUV(0.0, 0.0, 0.0F - var14, var7, var9);
            var4.draw();
            float var15 = 1.0F / (32 * var3);
            float var16 = 1.0F / var3;
            var4.startDrawingQuads();
            var4.setNormal(-1.0F, 0.0F, 0.0F);

            for (int var17 = 0; var17 < var3; var17++) {
               float var18 = var17 / (var3 * 1.0F);
               float var19 = var7 + (var6 - var7) * var18 - var15;
               float var20 = var10 * var18;
               var4.addVertexWithUV(var20, 0.0, 0.0F - var14, var19, var9);
               var4.addVertexWithUV(var20, 0.0, 0.0, var19, var9);
               var4.addVertexWithUV(var20, 1.0, 0.0, var19, var8);
               var4.addVertexWithUV(var20, 1.0, 0.0F - var14, var19, var8);
            }

            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(1.0F, 0.0F, 0.0F);

            for (int var22 = 0; var22 < var3; var22++) {
               float var25 = var22 / (var3 * 1.0F);
               float var28 = var7 + (var6 - var7) * var25 - var15;
               float var31 = var10 * var25 + var16;
               var4.addVertexWithUV(var31, 1.0, 0.0F - var14, var28, var8);
               var4.addVertexWithUV(var31, 1.0, 0.0, var28, var8);
               var4.addVertexWithUV(var31, 0.0, 0.0, var28, var9);
               var4.addVertexWithUV(var31, 0.0, 0.0F - var14, var28, var9);
            }

            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 1.0F, 0.0F);

            for (int var23 = 0; var23 < var3; var23++) {
               float var26 = var23 / (var3 * 1.0F);
               float var29 = var9 + (var8 - var9) * var26 - var15;
               float var32 = var10 * var26 + var16;
               var4.addVertexWithUV(0.0, var32, 0.0, var7, var29);
               var4.addVertexWithUV(var10, var32, 0.0, var6, var29);
               var4.addVertexWithUV(var10, var32, 0.0F - var14, var6, var29);
               var4.addVertexWithUV(0.0, var32, 0.0F - var14, var7, var29);
            }

            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);

            for (int var24 = 0; var24 < var3; var24++) {
               float var27 = var24 / (var3 * 1.0F);
               float var30 = var9 + (var8 - var9) * var27 - var15;
               float var33 = var10 * var27;
               var4.addVertexWithUV(var10, var33, 0.0, var6, var30);
               var4.addVertexWithUV(0.0, var33, 0.0, var7, var30);
               var4.addVertexWithUV(0.0, var33, 0.0F - var14, var7, var30);
               var4.addVertexWithUV(var10, var33, 0.0F - var14, var6, var30);
            }

            var4.draw();
            GL11.glDisable(32826);
            GL11.glPopMatrix();
         }
      }
   }
}
