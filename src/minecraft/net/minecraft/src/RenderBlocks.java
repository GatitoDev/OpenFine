package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderBlocks {
   public IBlockAccess blockAccess;
   public int overrideBlockTexture = -1;
   public boolean flipTexture = false;
   public boolean renderAllFaces = false;
   public static boolean fancyGrass = true;
   public static boolean cfgGrassFix = true;
   public boolean field_31088_b = true;
   public int field_31087_g = 0;
   public int field_31086_h = 0;
   public int field_31085_i = 0;
   public int field_31084_j = 0;
   public int field_31083_k = 0;
   public int field_31082_l = 0;
   public boolean enableAO;
   public float lightValueOwn;
   public float aoLightValueXNeg;
   public float aoLightValueYNeg;
   public float aoLightValueZNeg;
   public float aoLightValueXPos;
   public float aoLightValueYPos;
   public float aoLightValueZPos;
   public float field_22377_m;
   public float field_22376_n;
   public float field_22375_o;
   public float field_22374_p;
   public float field_22373_q;
   public float field_22372_r;
   public float field_22371_s;
   public float field_22370_t;
   public float field_22369_u;
   public float field_22368_v;
   public float field_22367_w;
   public float field_22366_x;
   public float field_22365_y;
   public float field_22364_z;
   public float field_22362_A;
   public float field_22360_B;
   public float field_22358_C;
   public float field_22356_D;
   public float field_22354_E;
   public float field_22353_F;
   public int field_22352_G = 1;
   public float colorRedTopLeft;
   public float colorRedBottomLeft;
   public float colorRedBottomRight;
   public float colorRedTopRight;
   public float colorGreenTopLeft;
   public float colorGreenBottomLeft;
   public float colorGreenBottomRight;
   public float colorGreenTopRight;
   public float colorBlueTopLeft;
   public float colorBlueBottomLeft;
   public float colorBlueBottomRight;
   public float colorBlueTopRight;
   public boolean field_22339_T;
   public boolean field_22338_U;
   public boolean field_22337_V;
   public boolean field_22336_W;
   public boolean field_22335_X;
   public boolean field_22334_Y;
   public boolean field_22333_Z;
   public boolean field_22363_aa;
   public boolean field_22361_ab;
   public boolean field_22359_ac;
   public boolean field_22357_ad;
   public boolean field_22355_ae;
   public static float[][] redstoneColors = new float[16][];

   public RenderBlocks(IBlockAccess var1) {
      this.blockAccess = var1;
   }

   public RenderBlocks() {
   }

   public void renderBlockUsingTexture(Block var1, int var2, int var3, int var4, int var5) {
      this.overrideBlockTexture = var5;
      this.renderBlockByRenderType(var1, var2, var3, var4);
      this.overrideBlockTexture = -1;
   }

   public void func_31075_a(Block var1, int var2, int var3, int var4) {
      this.renderAllFaces = true;
      this.renderBlockByRenderType(var1, var2, var3, var4);
      this.renderAllFaces = false;
   }

   public boolean renderBlockByRenderType(Block var1, int var2, int var3, int var4) {
      int var5 = var1.getRenderType();
      var1.setBlockBoundsBasedOnState(this.blockAccess, var2, var3, var4);
      if (var5 == 0) {
         return this.renderStandardBlock(var1, var2, var3, var4);
      } else if (var5 == 4) {
         return this.renderBlockFluids(var1, var2, var3, var4);
      } else if (var5 == 13) {
         return this.renderBlockCactus(var1, var2, var3, var4);
      } else if (var5 == 1) {
         return this.renderBlockReed(var1, var2, var3, var4);
      } else if (var5 == 6) {
         return this.renderBlockCrops(var1, var2, var3, var4);
      } else if (var5 == 2) {
         return this.renderBlockTorch(var1, var2, var3, var4);
      } else if (var5 == 3) {
         return this.renderBlockFire(var1, var2, var3, var4);
      } else if (var5 == 5) {
         return this.renderBlockRedstoneWire(var1, var2, var3, var4);
      } else if (var5 == 8) {
         return this.renderBlockLadder(var1, var2, var3, var4);
      } else if (var5 == 7) {
         return this.renderBlockDoor(var1, var2, var3, var4);
      } else if (var5 == 9) {
         return this.renderBlockMinecartTrack((BlockRail)var1, var2, var3, var4);
      } else if (var5 == 10) {
         return this.renderBlockStairs(var1, var2, var3, var4);
      } else if (var5 == 11) {
         return this.renderBlockFence(var1, var2, var3, var4);
      } else if (var5 == 12) {
         return this.renderBlockLever(var1, var2, var3, var4);
      } else if (var5 == 14) {
         return this.renderBlockBed(var1, var2, var3, var4);
      } else if (var5 == 15) {
         return this.renderBlockRepeater(var1, var2, var3, var4);
      } else if (var5 == 16) {
         return this.func_31074_b(var1, var2, var3, var4, false);
      } else if (var5 == 17) {
         return this.func_31080_c(var1, var2, var3, var4, true);
      } else {
         return Config.hasModLoader() ? Config.callBoolean("ModLoader", "RenderWorldBlock", new Object[]{this, this.blockAccess, var2, var3, var4, var1, var5}) : false;
      }
   }

   private boolean renderBlockBed(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      int var7 = BlockBed.getDirectionFromMetadata(var6);
      boolean var8 = BlockBed.isBlockFootOfBed(var6);
      float var9 = 0.5F;
      float var10 = 1.0F;
      float var11 = 0.8F;
      float var12 = 0.6F;
      float var13 = var10;
      float var14 = var10;
      float var15 = var10;
      float var16 = var9;
      float var17 = var11;
      float var18 = var12;
      float var19 = var9;
      float var20 = var11;
      float var21 = var12;
      float var22 = var9;
      float var23 = var11;
      float var24 = var12;
      float var25 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      var5.setColorOpaque_F(var16 * var25, var19 * var25, var22 * var25);
      int var26 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0);
      int var27 = (var26 & 15) << 4;
      int var28 = var26 & 240;
      double var29 = var27 / 256.0F;
      double var31 = (var27 + 16 - 0.01) / 256.0;
      double var33 = var28 / 256.0F;
      double var35 = (var28 + 16 - 0.01) / 256.0;
      double var37 = var2 + var1.minX;
      double var39 = var2 + var1.maxX;
      double var41 = var3 + var1.minY + 0.1875;
      double var43 = var4 + var1.minZ;
      double var45 = var4 + var1.maxZ;
      var5.addVertexWithUV(var37, var41, var45, var29, var35);
      var5.addVertexWithUV(var37, var41, var43, var29, var33);
      var5.addVertexWithUV(var39, var41, var43, var31, var33);
      var5.addVertexWithUV(var39, var41, var45, var31, var35);
      float var47 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
      var5.setColorOpaque_F(var13 * var47, var14 * var47, var15 * var47);
      var27 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1);
      var28 = (var27 & 15) << 4;
      var29 = var27 & 240;
      double var48 = var28 / 256.0F;
      double var50 = (var28 + 16 - 0.01) / 256.0;
      double var52 = (float)var29 / 256.0F;
      double var54 = (var29 + 16.0 - 0.01) / 256.0;
      double var56 = var48;
      double var58 = var50;
      double var60 = var52;
      double var62 = var52;
      double var64 = var48;
      double var66 = var50;
      double var68 = var54;
      double var70 = var54;
      if (var7 == 0) {
         var58 = var48;
         var60 = var54;
         var64 = var50;
         var70 = var52;
      } else if (var7 == 2) {
         var56 = var50;
         var62 = var54;
         var66 = var48;
         var68 = var52;
      } else if (var7 == 3) {
         var56 = var50;
         var62 = var54;
         var66 = var48;
         var68 = var52;
         var58 = var48;
         var60 = var54;
         var64 = var50;
         var70 = var52;
      }

      double var72 = var2 + var1.minX;
      double var74 = var2 + var1.maxX;
      double var76 = var3 + var1.maxY;
      double var78 = var4 + var1.minZ;
      double var80 = var4 + var1.maxZ;
      var5.addVertexWithUV(var74, var76, var80, var64, var68);
      var5.addVertexWithUV(var74, var76, var78, var56, var60);
      var5.addVertexWithUV(var72, var76, var78, var58, var62);
      var5.addVertexWithUV(var72, var76, var80, var66, var70);
      var26 = ModelBed.headInvisibleFace[var7];
      if (var8) {
         var26 = ModelBed.headInvisibleFace[ModelBed.footInvisibleFaceRemap[var7]];
      }

      byte var85 = 4;
      switch (var7) {
         case 0:
            var85 = 5;
            break;
         case 1:
            var85 = 3;
         case 2:
         default:
            break;
         case 3:
            var85 = 2;
      }

      if (var26 != 2 && (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2))) {
         float var82 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
         if (var1.minZ > 0.0) {
            var82 = var25;
         }

         var5.setColorOpaque_F(var17 * var82, var20 * var82, var23 * var82);
         this.flipTexture = var85 == 2;
         this.renderEastFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2));
      }

      if (var26 != 3 && (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3))) {
         float var88 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
         if (var1.maxZ < 1.0) {
            var88 = var25;
         }

         var5.setColorOpaque_F(var17 * var88, var20 * var88, var23 * var88);
         this.flipTexture = var85 == 3;
         this.renderWestFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3));
      }

      if (var26 != 4 && (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4))) {
         float var89 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
         if (var1.minX > 0.0) {
            var89 = var25;
         }

         var5.setColorOpaque_F(var18 * var89, var21 * var89, var24 * var89);
         this.flipTexture = var85 == 4;
         this.renderNorthFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4));
      }

      if (var26 != 5 && (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5))) {
         float var90 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
         if (var1.maxX < 1.0) {
            var90 = var25;
         }

         var5.setColorOpaque_F(var18 * var90, var21 * var90, var24 * var90);
         this.flipTexture = var85 == 5;
         this.renderSouthFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5));
      }

      this.flipTexture = false;
      return true;
   }

   public boolean renderBlockTorch(Block var1, int var2, int var3, int var4) {
      int var5 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      Tessellator var6 = Tessellator.instance;
      float var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      if (Block.lightValue[var1.blockID] > 0) {
         var7 = 1.0F;
      }

      var6.setColorOpaque_F(var7, var7, var7);
      double var8 = 0.4F;
      double var10 = 0.5 - var8;
      double var12 = 0.2F;
      if (var5 == 1) {
         this.renderTorchAtAngle(var1, var2 - var10, var3 + var12, var4, -var8, 0.0);
      } else if (var5 == 2) {
         this.renderTorchAtAngle(var1, var2 + var10, var3 + var12, var4, var8, 0.0);
      } else if (var5 == 3) {
         this.renderTorchAtAngle(var1, var2, var3 + var12, var4 - var10, 0.0, -var8);
      } else if (var5 == 4) {
         this.renderTorchAtAngle(var1, var2, var3 + var12, var4 + var10, 0.0, var8);
      } else {
         this.renderTorchAtAngle(var1, var2, var3, var4, 0.0, 0.0);
      }

      return true;
   }

   private boolean renderBlockRepeater(Block var1, int var2, int var3, int var4) {
      int var5 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      int var6 = var5 & 3;
      int var7 = (var5 & 12) >> 2;
      this.renderStandardBlock(var1, var2, var3, var4);
      Tessellator var8 = Tessellator.instance;
      float var9 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      if (Block.lightValue[var1.blockID] > 0) {
         var9 = (var9 + 1.0F) * 0.5F;
      }

      var8.setColorOpaque_F(var9, var9, var9);
      double var10 = -0.1875;
      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = 0.0;
      double var18 = 0.0;
      switch (var6) {
         case 0:
            var18 = -0.3125;
            var14 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
            break;
         case 1:
            var16 = 0.3125;
            var12 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
            break;
         case 2:
            var18 = 0.3125;
            var14 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
            break;
         case 3:
            var16 = -0.3125;
            var12 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
      }

      this.renderTorchAtAngle(var1, var2 + var12, var3 + var10, var4 + var14, 0.0, 0.0);
      this.renderTorchAtAngle(var1, var2 + var16, var3 + var10, var4 + var18, 0.0, 0.0);
      int var20 = var1.getBlockTextureFromSide(1);
      int var21 = (var20 & 15) << 4;
      int var22 = var20 & 240;
      double var23 = var21 / 256.0F;
      double var25 = (var21 + 15.99F) / 256.0F;
      double var27 = var22 / 256.0F;
      double var29 = (var22 + 15.99F) / 256.0F;
      float var31 = 0.125F;
      float var32 = var2 + 1;
      float var33 = var2 + 1;
      float var34 = var2 + 0;
      float var35 = var2 + 0;
      float var36 = var4 + 0;
      float var37 = var4 + 1;
      float var38 = var4 + 1;
      float var39 = var4 + 0;
      float var40 = var3 + var31;
      if (var6 == 2) {
         var32 = var33 = var2 + 0;
         var34 = var35 = var2 + 1;
         var36 = var39 = var4 + 1;
         var37 = var38 = var4 + 0;
      } else if (var6 == 3) {
         var32 = var35 = var2 + 0;
         var33 = var34 = var2 + 1;
         var36 = var37 = var4 + 0;
         var38 = var39 = var4 + 1;
      } else if (var6 == 1) {
         var32 = var35 = var2 + 1;
         var33 = var34 = var2 + 0;
         var36 = var37 = var4 + 1;
         var38 = var39 = var4 + 0;
      }

      var8.addVertexWithUV(var35, var40, var39, var23, var27);
      var8.addVertexWithUV(var34, var40, var38, var23, var29);
      var8.addVertexWithUV(var33, var40, var37, var25, var29);
      var8.addVertexWithUV(var32, var40, var36, var25, var27);
      return true;
   }

   public void func_31078_d(Block var1, int var2, int var3, int var4) {
      this.renderAllFaces = true;
      this.func_31074_b(var1, var2, var3, var4, true);
      this.renderAllFaces = false;
   }

   private boolean func_31074_b(Block var1, int var2, int var3, int var4, boolean var5) {
      int var6 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      boolean var7 = var5 || (var6 & 8) != 0;
      int var8 = BlockPistonBase.getOrientation(var6);
      if (var7) {
         switch (var8) {
            case 0:
               this.field_31087_g = 3;
               this.field_31086_h = 3;
               this.field_31085_i = 3;
               this.field_31084_j = 3;
               var1.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 1:
               var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
               break;
            case 2:
               this.field_31085_i = 1;
               this.field_31084_j = 2;
               var1.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
               break;
            case 3:
               this.field_31085_i = 2;
               this.field_31084_j = 1;
               this.field_31083_k = 3;
               this.field_31082_l = 3;
               var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
               break;
            case 4:
               this.field_31087_g = 1;
               this.field_31086_h = 2;
               this.field_31083_k = 2;
               this.field_31082_l = 1;
               var1.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 5:
               this.field_31087_g = 2;
               this.field_31086_h = 1;
               this.field_31083_k = 1;
               this.field_31082_l = 2;
               var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
         }

         this.renderStandardBlock(var1, var2, var3, var4);
         this.field_31087_g = 0;
         this.field_31086_h = 0;
         this.field_31085_i = 0;
         this.field_31084_j = 0;
         this.field_31083_k = 0;
         this.field_31082_l = 0;
         var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else {
         switch (var8) {
            case 0:
               this.field_31087_g = 3;
               this.field_31086_h = 3;
               this.field_31085_i = 3;
               this.field_31084_j = 3;
            case 1:
            default:
               break;
            case 2:
               this.field_31085_i = 1;
               this.field_31084_j = 2;
               break;
            case 3:
               this.field_31085_i = 2;
               this.field_31084_j = 1;
               this.field_31083_k = 3;
               this.field_31082_l = 3;
               break;
            case 4:
               this.field_31087_g = 1;
               this.field_31086_h = 2;
               this.field_31083_k = 2;
               this.field_31082_l = 1;
               break;
            case 5:
               this.field_31087_g = 2;
               this.field_31086_h = 1;
               this.field_31083_k = 1;
               this.field_31082_l = 2;
         }

         this.renderStandardBlock(var1, var2, var3, var4);
         this.field_31087_g = 0;
         this.field_31086_h = 0;
         this.field_31085_i = 0;
         this.field_31084_j = 0;
         this.field_31083_k = 0;
         this.field_31082_l = 0;
      }

      return true;
   }

   private void func_31076_a(double var1, double var3, double var5, double var7, double var9, double var11, float var13, double var14) {
      int var16 = 108;
      if (this.overrideBlockTexture >= 0) {
         var16 = this.overrideBlockTexture;
      }

      int var17 = (var16 & 15) << 4;
      int var18 = var16 & 240;
      Tessellator var19 = Tessellator.instance;
      double var20 = (var17 + 0) / 256.0F;
      double var22 = (var18 + 0) / 256.0F;
      double var24 = (var17 + var14 - 0.01) / 256.0;
      double var26 = (var18 + 4.0F - 0.01) / 256.0;
      var19.setColorOpaque_F(var13, var13, var13);
      var19.addVertexWithUV(var1, var7, var9, var24, var22);
      var19.addVertexWithUV(var1, var5, var9, var20, var22);
      var19.addVertexWithUV(var3, var5, var11, var20, var26);
      var19.addVertexWithUV(var3, var7, var11, var24, var26);
   }

   private void func_31081_b(double var1, double var3, double var5, double var7, double var9, double var11, float var13, double var14) {
      int var16 = 108;
      if (this.overrideBlockTexture >= 0) {
         var16 = this.overrideBlockTexture;
      }

      int var17 = (var16 & 15) << 4;
      int var18 = var16 & 240;
      Tessellator var19 = Tessellator.instance;
      double var20 = (var17 + 0) / 256.0F;
      double var22 = (var18 + 0) / 256.0F;
      double var24 = (var17 + var14 - 0.01) / 256.0;
      double var26 = (var18 + 4.0F - 0.01) / 256.0;
      var19.setColorOpaque_F(var13, var13, var13);
      var19.addVertexWithUV(var1, var5, var11, var24, var22);
      var19.addVertexWithUV(var1, var5, var9, var20, var22);
      var19.addVertexWithUV(var3, var7, var9, var20, var26);
      var19.addVertexWithUV(var3, var7, var11, var24, var26);
   }

   private void func_31077_c(double var1, double var3, double var5, double var7, double var9, double var11, float var13, double var14) {
      int var16 = 108;
      if (this.overrideBlockTexture >= 0) {
         var16 = this.overrideBlockTexture;
      }

      int var17 = (var16 & 15) << 4;
      int var18 = var16 & 240;
      Tessellator var19 = Tessellator.instance;
      double var20 = (var17 + 0) / 256.0F;
      double var22 = (var18 + 0) / 256.0F;
      double var24 = (var17 + var14 - 0.01) / 256.0;
      double var26 = (var18 + 4.0F - 0.01) / 256.0;
      var19.setColorOpaque_F(var13, var13, var13);
      var19.addVertexWithUV(var3, var5, var9, var24, var22);
      var19.addVertexWithUV(var1, var5, var9, var20, var22);
      var19.addVertexWithUV(var1, var7, var11, var20, var26);
      var19.addVertexWithUV(var3, var7, var11, var24, var26);
   }

   public void func_31079_a(Block var1, int var2, int var3, int var4, boolean var5) {
      this.renderAllFaces = true;
      this.func_31080_c(var1, var2, var3, var4, var5);
      this.renderAllFaces = false;
   }

   private boolean func_31080_c(Block var1, int var2, int var3, int var4, boolean var5) {
      int var6 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      int var7 = BlockPistonExtension.getDirectionMeta(var6);
      float var8 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      float var9 = var5 ? 1.0F : 0.5F;
      double var10 = var5 ? 16.0 : 8.0;
      switch (var7) {
         case 0:
            this.field_31087_g = 3;
            this.field_31086_h = 3;
            this.field_31085_i = 3;
            this.field_31084_j = 3;
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            this.func_31076_a(var2 + 0.375F, var2 + 0.625F, var3 + 0.25F, var3 + 0.25F + var9, var4 + 0.625F, var4 + 0.625F, var8 * 0.8F, var10);
            this.func_31076_a(var2 + 0.625F, var2 + 0.375F, var3 + 0.25F, var3 + 0.25F + var9, var4 + 0.375F, var4 + 0.375F, var8 * 0.8F, var10);
            this.func_31076_a(var2 + 0.375F, var2 + 0.375F, var3 + 0.25F, var3 + 0.25F + var9, var4 + 0.375F, var4 + 0.625F, var8 * 0.6F, var10);
            this.func_31076_a(var2 + 0.625F, var2 + 0.625F, var3 + 0.25F, var3 + 0.25F + var9, var4 + 0.625F, var4 + 0.375F, var8 * 0.6F, var10);
            break;
         case 1:
            var1.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            this.func_31076_a(var2 + 0.375F, var2 + 0.625F, var3 - 0.25F + 1.0F - var9, var3 - 0.25F + 1.0F, var4 + 0.625F, var4 + 0.625F, var8 * 0.8F, var10);
            this.func_31076_a(var2 + 0.625F, var2 + 0.375F, var3 - 0.25F + 1.0F - var9, var3 - 0.25F + 1.0F, var4 + 0.375F, var4 + 0.375F, var8 * 0.8F, var10);
            this.func_31076_a(var2 + 0.375F, var2 + 0.375F, var3 - 0.25F + 1.0F - var9, var3 - 0.25F + 1.0F, var4 + 0.375F, var4 + 0.625F, var8 * 0.6F, var10);
            this.func_31076_a(var2 + 0.625F, var2 + 0.625F, var3 - 0.25F + 1.0F - var9, var3 - 0.25F + 1.0F, var4 + 0.625F, var4 + 0.375F, var8 * 0.6F, var10);
            break;
         case 2:
            this.field_31085_i = 1;
            this.field_31084_j = 2;
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
            this.renderStandardBlock(var1, var2, var3, var4);
            this.func_31081_b(var2 + 0.375F, var2 + 0.375F, var3 + 0.625F, var3 + 0.375F, var4 + 0.25F, var4 + 0.25F + var9, var8 * 0.6F, var10);
            this.func_31081_b(var2 + 0.625F, var2 + 0.625F, var3 + 0.375F, var3 + 0.625F, var4 + 0.25F, var4 + 0.25F + var9, var8 * 0.6F, var10);
            this.func_31081_b(var2 + 0.375F, var2 + 0.625F, var3 + 0.375F, var3 + 0.375F, var4 + 0.25F, var4 + 0.25F + var9, var8 * 0.5F, var10);
            this.func_31081_b(var2 + 0.625F, var2 + 0.375F, var3 + 0.625F, var3 + 0.625F, var4 + 0.25F, var4 + 0.25F + var9, var8, var10);
            break;
         case 3:
            this.field_31085_i = 2;
            this.field_31084_j = 1;
            this.field_31083_k = 3;
            this.field_31082_l = 3;
            var1.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            this.func_31081_b(var2 + 0.375F, var2 + 0.375F, var3 + 0.625F, var3 + 0.375F, var4 - 0.25F + 1.0F - var9, var4 - 0.25F + 1.0F, var8 * 0.6F, var10);
            this.func_31081_b(var2 + 0.625F, var2 + 0.625F, var3 + 0.375F, var3 + 0.625F, var4 - 0.25F + 1.0F - var9, var4 - 0.25F + 1.0F, var8 * 0.6F, var10);
            this.func_31081_b(var2 + 0.375F, var2 + 0.625F, var3 + 0.375F, var3 + 0.375F, var4 - 0.25F + 1.0F - var9, var4 - 0.25F + 1.0F, var8 * 0.5F, var10);
            this.func_31081_b(var2 + 0.625F, var2 + 0.375F, var3 + 0.625F, var3 + 0.625F, var4 - 0.25F + 1.0F - var9, var4 - 0.25F + 1.0F, var8, var10);
            break;
         case 4:
            this.field_31087_g = 1;
            this.field_31086_h = 2;
            this.field_31083_k = 2;
            this.field_31082_l = 1;
            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            this.func_31077_c(var2 + 0.25F, var2 + 0.25F + var9, var3 + 0.375F, var3 + 0.375F, var4 + 0.625F, var4 + 0.375F, var8 * 0.5F, var10);
            this.func_31077_c(var2 + 0.25F, var2 + 0.25F + var9, var3 + 0.625F, var3 + 0.625F, var4 + 0.375F, var4 + 0.625F, var8, var10);
            this.func_31077_c(var2 + 0.25F, var2 + 0.25F + var9, var3 + 0.375F, var3 + 0.625F, var4 + 0.375F, var4 + 0.375F, var8 * 0.6F, var10);
            this.func_31077_c(var2 + 0.25F, var2 + 0.25F + var9, var3 + 0.625F, var3 + 0.375F, var4 + 0.625F, var4 + 0.625F, var8 * 0.6F, var10);
            break;
         case 5:
            this.field_31087_g = 2;
            this.field_31086_h = 1;
            this.field_31083_k = 1;
            this.field_31082_l = 2;
            var1.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            this.renderStandardBlock(var1, var2, var3, var4);
            this.func_31077_c(var2 - 0.25F + 1.0F - var9, var2 - 0.25F + 1.0F, var3 + 0.375F, var3 + 0.375F, var4 + 0.625F, var4 + 0.375F, var8 * 0.5F, var10);
            this.func_31077_c(var2 - 0.25F + 1.0F - var9, var2 - 0.25F + 1.0F, var3 + 0.625F, var3 + 0.625F, var4 + 0.375F, var4 + 0.625F, var8, var10);
            this.func_31077_c(var2 - 0.25F + 1.0F - var9, var2 - 0.25F + 1.0F, var3 + 0.375F, var3 + 0.625F, var4 + 0.375F, var4 + 0.375F, var8 * 0.6F, var10);
            this.func_31077_c(var2 - 0.25F + 1.0F - var9, var2 - 0.25F + 1.0F, var3 + 0.625F, var3 + 0.375F, var4 + 0.625F, var4 + 0.625F, var8 * 0.6F, var10);
      }

      this.field_31087_g = 0;
      this.field_31086_h = 0;
      this.field_31085_i = 0;
      this.field_31084_j = 0;
      this.field_31083_k = 0;
      this.field_31082_l = 0;
      var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return true;
   }

   public boolean renderBlockLever(Block var1, int var2, int var3, int var4) {
      int var5 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      int var6 = var5 & 7;
      boolean var7 = (var5 & 8) > 0;
      Tessellator var8 = Tessellator.instance;
      boolean var9 = this.overrideBlockTexture >= 0;
      if (!var9) {
         this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
      }

      float var10 = 0.25F;
      float var11 = 0.1875F;
      float var12 = 0.1875F;
      if (var6 == 5) {
         var1.setBlockBounds(0.5F - var11, 0.0F, 0.5F - var10, 0.5F + var11, var12, 0.5F + var10);
      } else if (var6 == 6) {
         var1.setBlockBounds(0.5F - var10, 0.0F, 0.5F - var11, 0.5F + var10, var12, 0.5F + var11);
      } else if (var6 == 4) {
         var1.setBlockBounds(0.5F - var11, 0.5F - var10, 1.0F - var12, 0.5F + var11, 0.5F + var10, 1.0F);
      } else if (var6 == 3) {
         var1.setBlockBounds(0.5F - var11, 0.5F - var10, 0.0F, 0.5F + var11, 0.5F + var10, var12);
      } else if (var6 == 2) {
         var1.setBlockBounds(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0F, 0.5F + var10, 0.5F + var11);
      } else if (var6 == 1) {
         var1.setBlockBounds(0.0F, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
      }

      this.renderStandardBlock(var1, var2, var3, var4);
      if (!var9) {
         this.overrideBlockTexture = -1;
      }

      float var13 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var8.setColorOpaque_F(var13, var13, var13);
      int var14 = var1.getBlockTextureFromSide(0);
      if (this.overrideBlockTexture >= 0) {
         var14 = this.overrideBlockTexture;
      }

      int var15 = (var14 & 15) << 4;
      int var16 = var14 & 240;
      float var17 = var15 / 256.0F;
      float var18 = (var15 + 15.99F) / 256.0F;
      float var19 = var16 / 256.0F;
      float var20 = (var16 + 15.99F) / 256.0F;
      Vec3D[] var21 = new Vec3D[8];
      float var22 = 0.0625F;
      float var23 = 0.0625F;
      float var24 = 0.625F;
      var21[0] = Vec3D.createVector(-var22, 0.0, -var23);
      var21[1] = Vec3D.createVector(var22, 0.0, -var23);
      var21[2] = Vec3D.createVector(var22, 0.0, var23);
      var21[3] = Vec3D.createVector(-var22, 0.0, var23);
      var21[4] = Vec3D.createVector(-var22, var24, -var23);
      var21[5] = Vec3D.createVector(var22, var24, -var23);
      var21[6] = Vec3D.createVector(var22, var24, var23);
      var21[7] = Vec3D.createVector(-var22, var24, var23);

      for (int var25 = 0; var25 < 8; var25++) {
         if (var7) {
            var21[var25].zCoord -= 0.0625;
            var21[var25].rotateAroundX((float) (Math.PI * 2.0 / 9.0));
         } else {
            var21[var25].zCoord += 0.0625;
            var21[var25].rotateAroundX((float) (-Math.PI * 2.0 / 9.0));
         }

         if (var6 == 6) {
            var21[var25].rotateAroundY(1.570796F);
         }

         if (var6 < 5) {
            var21[var25].yCoord -= 0.375;
            var21[var25].rotateAroundX(1.570796F);
            if (var6 == 4) {
               var21[var25].rotateAroundY(0.0F);
            }

            if (var6 == 3) {
               var21[var25].rotateAroundY(3.141593F);
            }

            if (var6 == 2) {
               var21[var25].rotateAroundY(1.570796F);
            }

            if (var6 == 1) {
               var21[var25].rotateAroundY(-1.570796F);
            }

            var21[var25].xCoord += var2 + 0.5;
            var21[var25].yCoord += var3 + 0.5F;
            var21[var25].zCoord += var4 + 0.5;
         } else {
            var21[var25].xCoord += var2 + 0.5;
            var21[var25].yCoord += var3 + 0.125F;
            var21[var25].zCoord += var4 + 0.5;
         }
      }

      Vec3D var30 = null;
      Vec3D var26 = null;
      Vec3D var27 = null;
      Vec3D var28 = null;

      for (int var29 = 0; var29 < 6; var29++) {
         if (var29 == 0) {
            var17 = (var15 + 7) / 256.0F;
            var18 = (var15 + 9 - 0.01F) / 256.0F;
            var19 = (var16 + 6) / 256.0F;
            var20 = (var16 + 8 - 0.01F) / 256.0F;
         } else if (var29 == 2) {
            var17 = (var15 + 7) / 256.0F;
            var18 = (var15 + 9 - 0.01F) / 256.0F;
            var19 = (var16 + 6) / 256.0F;
            var20 = (var16 + 16 - 0.01F) / 256.0F;
         }

         if (var29 == 0) {
            var30 = var21[0];
            var26 = var21[1];
            var27 = var21[2];
            var28 = var21[3];
         } else if (var29 == 1) {
            var30 = var21[7];
            var26 = var21[6];
            var27 = var21[5];
            var28 = var21[4];
         } else if (var29 == 2) {
            var30 = var21[1];
            var26 = var21[0];
            var27 = var21[4];
            var28 = var21[5];
         } else if (var29 == 3) {
            var30 = var21[2];
            var26 = var21[1];
            var27 = var21[5];
            var28 = var21[6];
         } else if (var29 == 4) {
            var30 = var21[3];
            var26 = var21[2];
            var27 = var21[6];
            var28 = var21[7];
         } else if (var29 == 5) {
            var30 = var21[0];
            var26 = var21[3];
            var27 = var21[7];
            var28 = var21[4];
         }

         var8.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, var17, var20);
         var8.addVertexWithUV(var26.xCoord, var26.yCoord, var26.zCoord, var18, var20);
         var8.addVertexWithUV(var27.xCoord, var27.yCoord, var27.zCoord, var18, var19);
         var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, var17, var19);
      }

      return true;
   }

   public boolean renderBlockFire(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = var1.getBlockTextureFromSide(0);
      if (this.overrideBlockTexture >= 0) {
         var6 = this.overrideBlockTexture;
      }

      float var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      var5.setColorOpaque_F(var7, var7, var7);
      int var8 = (var6 & 15) << 4;
      int var9 = var6 & 240;
      double var10 = var8 / 256.0F;
      double var12 = (var8 + 15.99F) / 256.0F;
      double var14 = var9 / 256.0F;
      double var16 = (var9 + 15.99F) / 256.0F;
      float var18 = 1.4F;
      if (!this.blockAccess.isBlockNormalCube(var2, var3 - 1, var4) && !Block.fire.canBlockCatchFire(this.blockAccess, var2, var3 - 1, var4)) {
         float var56 = 0.2F;
         float var20 = 0.0625F;
         if ((var2 + var3 + var4 & 1) == 1) {
            var10 = var8 / 256.0F;
            var12 = (var8 + 15.99F) / 256.0F;
            var14 = (var9 + 16) / 256.0F;
            var16 = (var9 + 15.99F + 16.0F) / 256.0F;
         }

         if ((var2 / 2 + var3 / 2 + var4 / 2 & 1) == 1) {
            double var58 = var12;
            var12 = var10;
            var10 = var58;
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, var2 - 1, var3, var4)) {
            var5.addVertexWithUV(var2 + var56, var3 + var18 + var20, var4 + 1, var12, var14);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 1, var12, var16);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 0, var10, var16);
            var5.addVertexWithUV(var2 + var56, var3 + var18 + var20, var4 + 0, var10, var14);
            var5.addVertexWithUV(var2 + var56, var3 + var18 + var20, var4 + 0, var10, var14);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 0, var10, var16);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 1, var12, var16);
            var5.addVertexWithUV(var2 + var56, var3 + var18 + var20, var4 + 1, var12, var14);
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, var2 + 1, var3, var4)) {
            var5.addVertexWithUV(var2 + 1 - var56, var3 + var18 + var20, var4 + 0, var10, var14);
            var5.addVertexWithUV(var2 + 1 - 0, var3 + 0 + var20, var4 + 0, var10, var16);
            var5.addVertexWithUV(var2 + 1 - 0, var3 + 0 + var20, var4 + 1, var12, var16);
            var5.addVertexWithUV(var2 + 1 - var56, var3 + var18 + var20, var4 + 1, var12, var14);
            var5.addVertexWithUV(var2 + 1 - var56, var3 + var18 + var20, var4 + 1, var12, var14);
            var5.addVertexWithUV(var2 + 1 - 0, var3 + 0 + var20, var4 + 1, var12, var16);
            var5.addVertexWithUV(var2 + 1 - 0, var3 + 0 + var20, var4 + 0, var10, var16);
            var5.addVertexWithUV(var2 + 1 - var56, var3 + var18 + var20, var4 + 0, var10, var14);
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, var2, var3, var4 - 1)) {
            var5.addVertexWithUV(var2 + 0, var3 + var18 + var20, var4 + var56, var12, var14);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 0, var12, var16);
            var5.addVertexWithUV(var2 + 1, var3 + 0 + var20, var4 + 0, var10, var16);
            var5.addVertexWithUV(var2 + 1, var3 + var18 + var20, var4 + var56, var10, var14);
            var5.addVertexWithUV(var2 + 1, var3 + var18 + var20, var4 + var56, var10, var14);
            var5.addVertexWithUV(var2 + 1, var3 + 0 + var20, var4 + 0, var10, var16);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 0, var12, var16);
            var5.addVertexWithUV(var2 + 0, var3 + var18 + var20, var4 + var56, var12, var14);
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, var2, var3, var4 + 1)) {
            var5.addVertexWithUV(var2 + 1, var3 + var18 + var20, var4 + 1 - var56, var10, var14);
            var5.addVertexWithUV(var2 + 1, var3 + 0 + var20, var4 + 1 - 0, var10, var16);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 1 - 0, var12, var16);
            var5.addVertexWithUV(var2 + 0, var3 + var18 + var20, var4 + 1 - var56, var12, var14);
            var5.addVertexWithUV(var2 + 0, var3 + var18 + var20, var4 + 1 - var56, var12, var14);
            var5.addVertexWithUV(var2 + 0, var3 + 0 + var20, var4 + 1 - 0, var12, var16);
            var5.addVertexWithUV(var2 + 1, var3 + 0 + var20, var4 + 1 - 0, var10, var16);
            var5.addVertexWithUV(var2 + 1, var3 + var18 + var20, var4 + 1 - var56, var10, var14);
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, var2, var3 + 1, var4)) {
            double var59 = var2 + 0.5 + 0.5;
            double var61 = var2 + 0.5 - 0.5;
            double var63 = var4 + 0.5 + 0.5;
            double var65 = var4 + 0.5 - 0.5;
            double var67 = var2 + 0.5 - 0.5;
            double var69 = var2 + 0.5 + 0.5;
            double var71 = var4 + 0.5 - 0.5;
            double var35 = var4 + 0.5 + 0.5;
            double var37 = var8 / 256.0F;
            double var39 = (var8 + 15.99F) / 256.0F;
            double var41 = var9 / 256.0F;
            double var43 = (var9 + 15.99F) / 256.0F;
            var3++;
            float var45 = -0.2F;
            if ((var2 + var3 + var4 & 1) == 0) {
               var5.addVertexWithUV(var67, var3 + var45, var4 + 0, var39, var41);
               var5.addVertexWithUV(var59, var3 + 0, var4 + 0, var39, var43);
               var5.addVertexWithUV(var59, var3 + 0, var4 + 1, var37, var43);
               var5.addVertexWithUV(var67, var3 + var45, var4 + 1, var37, var41);
               var37 = var8 / 256.0F;
               var39 = (var8 + 15.99F) / 256.0F;
               var41 = (var9 + 16) / 256.0F;
               var43 = (var9 + 15.99F + 16.0F) / 256.0F;
               var5.addVertexWithUV(var69, var3 + var45, var4 + 1, var39, var41);
               var5.addVertexWithUV(var61, var3 + 0, var4 + 1, var39, var43);
               var5.addVertexWithUV(var61, var3 + 0, var4 + 0, var37, var43);
               var5.addVertexWithUV(var69, var3 + var45, var4 + 0, var37, var41);
            } else {
               var5.addVertexWithUV(var2 + 0, var3 + var45, var35, var39, var41);
               var5.addVertexWithUV(var2 + 0, var3 + 0, var65, var39, var43);
               var5.addVertexWithUV(var2 + 1, var3 + 0, var65, var37, var43);
               var5.addVertexWithUV(var2 + 1, var3 + var45, var35, var37, var41);
               var37 = var8 / 256.0F;
               var39 = (var8 + 15.99F) / 256.0F;
               var41 = (var9 + 16) / 256.0F;
               var43 = (var9 + 15.99F + 16.0F) / 256.0F;
               var5.addVertexWithUV(var2 + 1, var3 + var45, var71, var39, var41);
               var5.addVertexWithUV(var2 + 1, var3 + 0, var63, var39, var43);
               var5.addVertexWithUV(var2 + 0, var3 + 0, var63, var37, var43);
               var5.addVertexWithUV(var2 + 0, var3 + var45, var71, var37, var41);
            }
         }
      } else {
         double var19 = var2 + 0.5 + 0.2;
         double var21 = var2 + 0.5 - 0.2;
         double var23 = var4 + 0.5 + 0.2;
         double var25 = var4 + 0.5 - 0.2;
         double var27 = var2 + 0.5 - 0.3;
         double var29 = var2 + 0.5 + 0.3;
         double var31 = var4 + 0.5 - 0.3;
         double var33 = var4 + 0.5 + 0.3;
         var5.addVertexWithUV(var27, var3 + var18, var4 + 1, var12, var14);
         var5.addVertexWithUV(var19, var3 + 0, var4 + 1, var12, var16);
         var5.addVertexWithUV(var19, var3 + 0, var4 + 0, var10, var16);
         var5.addVertexWithUV(var27, var3 + var18, var4 + 0, var10, var14);
         var5.addVertexWithUV(var29, var3 + var18, var4 + 0, var12, var14);
         var5.addVertexWithUV(var21, var3 + 0, var4 + 0, var12, var16);
         var5.addVertexWithUV(var21, var3 + 0, var4 + 1, var10, var16);
         var5.addVertexWithUV(var29, var3 + var18, var4 + 1, var10, var14);
         var10 = var8 / 256.0F;
         var12 = (var8 + 15.99F) / 256.0F;
         var14 = (var9 + 16) / 256.0F;
         var16 = (var9 + 15.99F + 16.0F) / 256.0F;
         var5.addVertexWithUV(var2 + 1, var3 + var18, var33, var12, var14);
         var5.addVertexWithUV(var2 + 1, var3 + 0, var25, var12, var16);
         var5.addVertexWithUV(var2 + 0, var3 + 0, var25, var10, var16);
         var5.addVertexWithUV(var2 + 0, var3 + var18, var33, var10, var14);
         var5.addVertexWithUV(var2 + 0, var3 + var18, var31, var12, var14);
         var5.addVertexWithUV(var2 + 0, var3 + 0, var23, var12, var16);
         var5.addVertexWithUV(var2 + 1, var3 + 0, var23, var10, var16);
         var5.addVertexWithUV(var2 + 1, var3 + var18, var31, var10, var14);
         var19 = var2 + 0.5 - 0.5;
         var21 = var2 + 0.5 + 0.5;
         var23 = var4 + 0.5 - 0.5;
         var25 = var4 + 0.5 + 0.5;
         var27 = var2 + 0.5 - 0.4;
         var29 = var2 + 0.5 + 0.4;
         var31 = var4 + 0.5 - 0.4;
         var33 = var4 + 0.5 + 0.4;
         var5.addVertexWithUV(var27, var3 + var18, var4 + 0, var10, var14);
         var5.addVertexWithUV(var19, var3 + 0, var4 + 0, var10, var16);
         var5.addVertexWithUV(var19, var3 + 0, var4 + 1, var12, var16);
         var5.addVertexWithUV(var27, var3 + var18, var4 + 1, var12, var14);
         var5.addVertexWithUV(var29, var3 + var18, var4 + 1, var10, var14);
         var5.addVertexWithUV(var21, var3 + 0, var4 + 1, var10, var16);
         var5.addVertexWithUV(var21, var3 + 0, var4 + 0, var12, var16);
         var5.addVertexWithUV(var29, var3 + var18, var4 + 0, var12, var14);
         var10 = var8 / 256.0F;
         var12 = (var8 + 15.99F) / 256.0F;
         var14 = var9 / 256.0F;
         var16 = (var9 + 15.99F) / 256.0F;
         var5.addVertexWithUV(var2 + 0, var3 + var18, var33, var10, var14);
         var5.addVertexWithUV(var2 + 0, var3 + 0, var25, var10, var16);
         var5.addVertexWithUV(var2 + 1, var3 + 0, var25, var12, var16);
         var5.addVertexWithUV(var2 + 1, var3 + var18, var33, var12, var14);
         var5.addVertexWithUV(var2 + 1, var3 + var18, var31, var10, var14);
         var5.addVertexWithUV(var2 + 1, var3 + 0, var23, var10, var16);
         var5.addVertexWithUV(var2 + 0, var3 + 0, var23, var12, var16);
         var5.addVertexWithUV(var2 + 0, var3 + var18, var31, var12, var14);
      }

      return true;
   }

   public boolean renderBlockRedstoneWire(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      int var7 = var1.getBlockTextureFromSideAndMetadata(1, var6);
      if (this.overrideBlockTexture >= 0) {
         var7 = this.overrideBlockTexture;
      }

      float var8 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      float var9 = var6 / 15.0F;
      float var10 = var9 * 0.6F + 0.4F;
      if (var6 == 0) {
         var10 = 0.3F;
      }

      float var11 = var9 * var9 * 0.7F - 0.5F;
      float var12 = var9 * var9 * 0.6F - 0.7F;
      if (var11 < 0.0F) {
         var11 = 0.0F;
      }

      if (var12 < 0.0F) {
         var12 = 0.0F;
      }

      var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
      int var13 = (var7 & 15) << 4;
      int var14 = var7 & 240;
      double var15 = var13 / 256.0F;
      double var17 = (var13 + 15.99F) / 256.0F;
      double var19 = var14 / 256.0F;
      double var21 = (var14 + 15.99F) / 256.0F;
      boolean var23 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2 - 1, var3, var4, 1) || !this.blockAccess.isBlockNormalCube(var2 - 1, var3, var4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2 - 1, var3 - 1, var4, -1);
      boolean var24 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2 + 1, var3, var4, 3) || !this.blockAccess.isBlockNormalCube(var2 + 1, var3, var4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2 + 1, var3 - 1, var4, -1);
      boolean var25 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2, var3, var4 - 1, 2) || !this.blockAccess.isBlockNormalCube(var2, var3, var4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2, var3 - 1, var4 - 1, -1);
      boolean var26 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2, var3, var4 + 1, 0) || !this.blockAccess.isBlockNormalCube(var2, var3, var4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2, var3 - 1, var4 + 1, -1);
      if (!this.blockAccess.isBlockNormalCube(var2, var3 + 1, var4)) {
         if (this.blockAccess.isBlockNormalCube(var2 - 1, var3, var4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2 - 1, var3 + 1, var4, -1)) {
            var23 = true;
         }

         if (this.blockAccess.isBlockNormalCube(var2 + 1, var3, var4) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2 + 1, var3 + 1, var4, -1)) {
            var24 = true;
         }

         if (this.blockAccess.isBlockNormalCube(var2, var3, var4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2, var3 + 1, var4 - 1, -1)) {
            var25 = true;
         }

         if (this.blockAccess.isBlockNormalCube(var2, var3, var4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, var2, var3 + 1, var4 + 1, -1)) {
            var26 = true;
         }
      }

      float var27 = var2 + 0;
      float var28 = var2 + 1;
      float var29 = var4 + 0;
      float var30 = var4 + 1;
      byte var31 = 0;
      if ((var23 || var24) && !var25 && !var26) {
         var31 = 1;
      }

      if ((var25 || var26) && !var24 && !var23) {
         var31 = 2;
      }

      if (var31 != 0) {
         var15 = (var13 + 16) / 256.0F;
         var17 = (var13 + 16 + 15.99F) / 256.0F;
         var19 = var14 / 256.0F;
         var21 = (var14 + 15.99F) / 256.0F;
      }

      if (var31 == 0) {
         if (var24 || var25 || var26 || var23) {
            if (!var23) {
               var27 += 0.3125F;
            }

            if (!var23) {
               var15 += 0.01953125;
            }

            if (!var24) {
               var28 -= 0.3125F;
            }

            if (!var24) {
               var17 -= 0.01953125;
            }

            if (!var25) {
               var29 += 0.3125F;
            }

            if (!var25) {
               var19 += 0.01953125;
            }

            if (!var26) {
               var30 -= 0.3125F;
            }

            if (!var26) {
               var21 -= 0.01953125;
            }
         }

         var5.addVertexWithUV(var28, var3 + 0.015625F, var30, var17, var21);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var29, var17, var19);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var29, var15, var19);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var30, var15, var21);
         var5.setColorOpaque_F(var8, var8, var8);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var30, var17, var21 + 0.0625);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var29, var17, var19 + 0.0625);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var29, var15, var19 + 0.0625);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var30, var15, var21 + 0.0625);
      } else if (var31 == 1) {
         var5.addVertexWithUV(var28, var3 + 0.015625F, var30, var17, var21);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var29, var17, var19);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var29, var15, var19);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var30, var15, var21);
         var5.setColorOpaque_F(var8, var8, var8);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var30, var17, var21 + 0.0625);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var29, var17, var19 + 0.0625);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var29, var15, var19 + 0.0625);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var30, var15, var21 + 0.0625);
      } else if (var31 == 2) {
         var5.addVertexWithUV(var28, var3 + 0.015625F, var30, var17, var21);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var29, var15, var21);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var29, var15, var19);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var30, var17, var19);
         var5.setColorOpaque_F(var8, var8, var8);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var30, var17, var21 + 0.0625);
         var5.addVertexWithUV(var28, var3 + 0.015625F, var29, var15, var21 + 0.0625);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var29, var15, var19 + 0.0625);
         var5.addVertexWithUV(var27, var3 + 0.015625F, var30, var17, var19 + 0.0625);
      }

      if (!this.blockAccess.isBlockNormalCube(var2, var3 + 1, var4)) {
         double var32 = (var13 + 16) / 256.0F;
         double var34 = (var13 + 16 + 15.99F) / 256.0F;
         double var36 = var14 / 256.0F;
         double var38 = (var14 + 15.99F) / 256.0F;
         if (this.blockAccess.isBlockNormalCube(var2 - 1, var3, var4) && this.blockAccess.getBlockId(var2 - 1, var3 + 1, var4) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 1 + 0.021875F, var4 + 1, var34, var36);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 0, var4 + 1, var32, var36);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 0, var4 + 0, var32, var38);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 1 + 0.021875F, var4 + 0, var34, var38);
            var5.setColorOpaque_F(var8, var8, var8);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 1 + 0.021875F, var4 + 1, var34, var36 + 0.0625);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 0, var4 + 1, var32, var36 + 0.0625);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 0, var4 + 0, var32, var38 + 0.0625);
            var5.addVertexWithUV(var2 + 0.015625F, var3 + 1 + 0.021875F, var4 + 0, var34, var38 + 0.0625);
         }

         if (this.blockAccess.isBlockNormalCube(var2 + 1, var3, var4) && this.blockAccess.getBlockId(var2 + 1, var3 + 1, var4) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 0, var4 + 1, var32, var38);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 1 + 0.021875F, var4 + 1, var34, var38);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 1 + 0.021875F, var4 + 0, var34, var36);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 0, var4 + 0, var32, var36);
            var5.setColorOpaque_F(var8, var8, var8);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 0, var4 + 1, var32, var38 + 0.0625);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 1 + 0.021875F, var4 + 1, var34, var38 + 0.0625);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 1 + 0.021875F, var4 + 0, var34, var36 + 0.0625);
            var5.addVertexWithUV(var2 + 1 - 0.015625F, var3 + 0, var4 + 0, var32, var36 + 0.0625);
         }

         if (this.blockAccess.isBlockNormalCube(var2, var3, var4 - 1) && this.blockAccess.getBlockId(var2, var3 + 1, var4 - 1) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
            var5.addVertexWithUV(var2 + 1, var3 + 0, var4 + 0.015625F, var32, var38);
            var5.addVertexWithUV(var2 + 1, var3 + 1 + 0.021875F, var4 + 0.015625F, var34, var38);
            var5.addVertexWithUV(var2 + 0, var3 + 1 + 0.021875F, var4 + 0.015625F, var34, var36);
            var5.addVertexWithUV(var2 + 0, var3 + 0, var4 + 0.015625F, var32, var36);
            var5.setColorOpaque_F(var8, var8, var8);
            var5.addVertexWithUV(var2 + 1, var3 + 0, var4 + 0.015625F, var32, var38 + 0.0625);
            var5.addVertexWithUV(var2 + 1, var3 + 1 + 0.021875F, var4 + 0.015625F, var34, var38 + 0.0625);
            var5.addVertexWithUV(var2 + 0, var3 + 1 + 0.021875F, var4 + 0.015625F, var34, var36 + 0.0625);
            var5.addVertexWithUV(var2 + 0, var3 + 0, var4 + 0.015625F, var32, var36 + 0.0625);
         }

         if (this.blockAccess.isBlockNormalCube(var2, var3, var4 + 1) && this.blockAccess.getBlockId(var2, var3 + 1, var4 + 1) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var8 * var10, var8 * var11, var8 * var12);
            var5.addVertexWithUV(var2 + 1, var3 + 1 + 0.021875F, var4 + 1 - 0.015625F, var34, var36);
            var5.addVertexWithUV(var2 + 1, var3 + 0, var4 + 1 - 0.015625F, var32, var36);
            var5.addVertexWithUV(var2 + 0, var3 + 0, var4 + 1 - 0.015625F, var32, var38);
            var5.addVertexWithUV(var2 + 0, var3 + 1 + 0.021875F, var4 + 1 - 0.015625F, var34, var38);
            var5.setColorOpaque_F(var8, var8, var8);
            var5.addVertexWithUV(var2 + 1, var3 + 1 + 0.021875F, var4 + 1 - 0.015625F, var34, var36 + 0.0625);
            var5.addVertexWithUV(var2 + 1, var3 + 0, var4 + 1 - 0.015625F, var32, var36 + 0.0625);
            var5.addVertexWithUV(var2 + 0, var3 + 0, var4 + 1 - 0.015625F, var32, var38 + 0.0625);
            var5.addVertexWithUV(var2 + 0, var3 + 1 + 0.021875F, var4 + 1 - 0.015625F, var34, var38 + 0.0625);
         }
      }

      return true;
   }

   public boolean renderBlockMinecartTrack(BlockRail var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      int var7 = var1.getBlockTextureFromSideAndMetadata(0, var6);
      if (this.overrideBlockTexture >= 0) {
         var7 = this.overrideBlockTexture;
      }

      if (var1.getIsPowered()) {
         var6 &= 7;
      }

      float var8 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      var5.setColorOpaque_F(var8, var8, var8);
      int var9 = (var7 & 15) << 4;
      int var10 = var7 & 240;
      double var11 = var9 / 256.0F;
      double var13 = (var9 + 15.99F) / 256.0F;
      double var15 = var10 / 256.0F;
      double var17 = (var10 + 15.99F) / 256.0F;
      float var19 = 0.0625F;
      float var20 = var2 + 1;
      float var21 = var2 + 1;
      float var22 = var2 + 0;
      float var23 = var2 + 0;
      float var24 = var4 + 0;
      float var25 = var4 + 1;
      float var26 = var4 + 1;
      float var27 = var4 + 0;
      float var28 = var3 + var19;
      float var29 = var3 + var19;
      float var30 = var3 + var19;
      float var31 = var3 + var19;
      if (var6 == 1 || var6 == 2 || var6 == 3 || var6 == 7) {
         var20 = var23 = var2 + 1;
         var21 = var22 = var2 + 0;
         var24 = var25 = var4 + 1;
         var26 = var27 = var4 + 0;
      } else if (var6 == 8) {
         var20 = var21 = var2 + 0;
         var22 = var23 = var2 + 1;
         var24 = var27 = var4 + 1;
         var25 = var26 = var4 + 0;
      } else if (var6 == 9) {
         var20 = var23 = var2 + 0;
         var21 = var22 = var2 + 1;
         var24 = var25 = var4 + 0;
         var26 = var27 = var4 + 1;
      }

      if (var6 == 2 || var6 == 4) {
         var28++;
         var31++;
      } else if (var6 == 3 || var6 == 5) {
         var29++;
         var30++;
      }

      var5.addVertexWithUV(var20, var28, var24, var13, var15);
      var5.addVertexWithUV(var21, var29, var25, var13, var17);
      var5.addVertexWithUV(var22, var30, var26, var11, var17);
      var5.addVertexWithUV(var23, var31, var27, var11, var15);
      var5.addVertexWithUV(var23, var31, var27, var11, var15);
      var5.addVertexWithUV(var22, var30, var26, var11, var17);
      var5.addVertexWithUV(var21, var29, var25, var13, var17);
      var5.addVertexWithUV(var20, var28, var24, var13, var15);
      return true;
   }

   public boolean renderBlockLadder(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = var1.getBlockTextureFromSide(0);
      if (this.overrideBlockTexture >= 0) {
         var6 = this.overrideBlockTexture;
      }

      float var7 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      var5.setColorOpaque_F(var7, var7, var7);
      int var8 = (var6 & 15) << 4;
      int var9 = var6 & 240;
      double var10 = var8 / 256.0F;
      double var12 = (var8 + 15.99F) / 256.0F;
      double var14 = var9 / 256.0F;
      double var16 = (var9 + 15.99F) / 256.0F;
      int var18 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      float var19 = 0.0F;
      float var20 = 0.05F;
      if (var18 == 5) {
         var5.addVertexWithUV(var2 + var20, var3 + 1 + var19, var4 + 1 + var19, var10, var14);
         var5.addVertexWithUV(var2 + var20, var3 + 0 - var19, var4 + 1 + var19, var10, var16);
         var5.addVertexWithUV(var2 + var20, var3 + 0 - var19, var4 + 0 - var19, var12, var16);
         var5.addVertexWithUV(var2 + var20, var3 + 1 + var19, var4 + 0 - var19, var12, var14);
      }

      if (var18 == 4) {
         var5.addVertexWithUV(var2 + 1 - var20, var3 + 0 - var19, var4 + 1 + var19, var12, var16);
         var5.addVertexWithUV(var2 + 1 - var20, var3 + 1 + var19, var4 + 1 + var19, var12, var14);
         var5.addVertexWithUV(var2 + 1 - var20, var3 + 1 + var19, var4 + 0 - var19, var10, var14);
         var5.addVertexWithUV(var2 + 1 - var20, var3 + 0 - var19, var4 + 0 - var19, var10, var16);
      }

      if (var18 == 3) {
         var5.addVertexWithUV(var2 + 1 + var19, var3 + 0 - var19, var4 + var20, var12, var16);
         var5.addVertexWithUV(var2 + 1 + var19, var3 + 1 + var19, var4 + var20, var12, var14);
         var5.addVertexWithUV(var2 + 0 - var19, var3 + 1 + var19, var4 + var20, var10, var14);
         var5.addVertexWithUV(var2 + 0 - var19, var3 + 0 - var19, var4 + var20, var10, var16);
      }

      if (var18 == 2) {
         var5.addVertexWithUV(var2 + 1 + var19, var3 + 1 + var19, var4 + 1 - var20, var10, var14);
         var5.addVertexWithUV(var2 + 1 + var19, var3 + 0 - var19, var4 + 1 - var20, var10, var16);
         var5.addVertexWithUV(var2 + 0 - var19, var3 + 0 - var19, var4 + 1 - var20, var12, var16);
         var5.addVertexWithUV(var2 + 0 - var19, var3 + 1 + var19, var4 + 1 - var20, var12, var14);
      }

      return true;
   }

   public boolean renderBlockReed(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      float var6 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      int var7 = var1.colorMultiplier(this.blockAccess, var2, var3, var4);
      float var8 = (var7 >> 16 & 0xFF) / 255.0F;
      float var9 = (var7 >> 8 & 0xFF) / 255.0F;
      float var10 = (var7 & 0xFF) / 255.0F;
      if (EntityRenderer.anaglyphEnable) {
         float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
         float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
         float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
         var8 = var11;
         var9 = var12;
         var10 = var13;
      }

      var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
      double var19 = var2;
      double var20 = var3;
      double var15 = var4;
      if (var1 == Block.tallGrass) {
         long var17 = var2 * 3129871 ^ var4 * 116129781L ^ var3;
         var17 = var17 * var17 * 42317861L + var17 * 11L;
         var19 += ((float)(var17 >> 16 & 15L) / 15.0F - 0.5) * 0.5;
         var20 += ((float)(var17 >> 20 & 15L) / 15.0F - 1.0) * 0.2;
         var15 += ((float)(var17 >> 24 & 15L) / 15.0F - 0.5) * 0.5;
      }

      this.renderCrossedSquares(var1, this.blockAccess.getBlockMetadata(var2, var3, var4), var19, var20, var15);
      return true;
   }

   public boolean renderBlockCrops(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      float var6 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      var5.setColorOpaque_F(var6, var6, var6);
      this.func_1245_b(var1, this.blockAccess.getBlockMetadata(var2, var3, var4), var2, var3 - 0.0625F, var4);
      return true;
   }

   public void renderTorchAtAngle(Block var1, double var2, double var4, double var6, double var8, double var10) {
      Tessellator var12 = Tessellator.instance;
      int var13 = var1.getBlockTextureFromSide(0);
      if (this.overrideBlockTexture >= 0) {
         var13 = this.overrideBlockTexture;
      }

      int var14 = (var13 & 15) << 4;
      int var15 = var13 & 240;
      float var16 = var14 / 256.0F;
      float var17 = (var14 + 15.99F) / 256.0F;
      float var18 = var15 / 256.0F;
      float var19 = (var15 + 15.99F) / 256.0F;
      double var20 = var16 + 0.02734375;
      double var22 = var18 + 0.0234375;
      double var24 = var16 + 0.03515625;
      double var26 = var18 + 0.03125;
      var2 += 0.5;
      var6 += 0.5;
      double var28 = var2 - 0.5;
      double var30 = var2 + 0.5;
      double var32 = var6 - 0.5;
      double var34 = var6 + 0.5;
      double var36 = 0.0625;
      double var38 = 0.625;
      var12.addVertexWithUV(var2 + var8 * (1.0 - var38) - var36, var4 + var38, var6 + var10 * (1.0 - var38) - var36, var20, var22);
      var12.addVertexWithUV(var2 + var8 * (1.0 - var38) - var36, var4 + var38, var6 + var10 * (1.0 - var38) + var36, var20, var26);
      var12.addVertexWithUV(var2 + var8 * (1.0 - var38) + var36, var4 + var38, var6 + var10 * (1.0 - var38) + var36, var24, var26);
      var12.addVertexWithUV(var2 + var8 * (1.0 - var38) + var36, var4 + var38, var6 + var10 * (1.0 - var38) - var36, var24, var22);
      var12.addVertexWithUV(var2 - var36, var4 + 1.0, var32, var16, var18);
      var12.addVertexWithUV(var2 - var36 + var8, var4 + 0.0, var32 + var10, var16, var19);
      var12.addVertexWithUV(var2 - var36 + var8, var4 + 0.0, var34 + var10, var17, var19);
      var12.addVertexWithUV(var2 - var36, var4 + 1.0, var34, var17, var18);
      var12.addVertexWithUV(var2 + var36, var4 + 1.0, var34, var16, var18);
      var12.addVertexWithUV(var2 + var8 + var36, var4 + 0.0, var34 + var10, var16, var19);
      var12.addVertexWithUV(var2 + var8 + var36, var4 + 0.0, var32 + var10, var17, var19);
      var12.addVertexWithUV(var2 + var36, var4 + 1.0, var32, var17, var18);
      var12.addVertexWithUV(var28, var4 + 1.0, var6 + var36, var16, var18);
      var12.addVertexWithUV(var28 + var8, var4 + 0.0, var6 + var36 + var10, var16, var19);
      var12.addVertexWithUV(var30 + var8, var4 + 0.0, var6 + var36 + var10, var17, var19);
      var12.addVertexWithUV(var30, var4 + 1.0, var6 + var36, var17, var18);
      var12.addVertexWithUV(var30, var4 + 1.0, var6 - var36, var16, var18);
      var12.addVertexWithUV(var30 + var8, var4 + 0.0, var6 - var36 + var10, var16, var19);
      var12.addVertexWithUV(var28 + var8, var4 + 0.0, var6 - var36 + var10, var17, var19);
      var12.addVertexWithUV(var28, var4 + 1.0, var6 - var36, var17, var18);
   }

   public void renderCrossedSquares(Block var1, int var2, double var3, double var5, double var7) {
      Tessellator var9 = Tessellator.instance;
      int var10 = var1.getBlockTextureFromSideAndMetadata(0, var2);
      if (this.overrideBlockTexture >= 0) {
         var10 = this.overrideBlockTexture;
      }

      int var11 = (var10 & 15) << 4;
      int var12 = var10 & 240;
      double var13 = var11 / 256.0F;
      double var15 = (var11 + 15.99F) / 256.0F;
      double var17 = var12 / 256.0F;
      double var19 = (var12 + 15.99F) / 256.0F;
      double var21 = var3 + 0.5 - 0.45F;
      double var23 = var3 + 0.5 + 0.45F;
      double var25 = var7 + 0.5 - 0.45F;
      double var27 = var7 + 0.5 + 0.45F;
      var9.addVertexWithUV(var21, var5 + 1.0, var25, var13, var17);
      var9.addVertexWithUV(var21, var5 + 0.0, var25, var13, var19);
      var9.addVertexWithUV(var23, var5 + 0.0, var27, var15, var19);
      var9.addVertexWithUV(var23, var5 + 1.0, var27, var15, var17);
      var9.addVertexWithUV(var23, var5 + 1.0, var27, var13, var17);
      var9.addVertexWithUV(var23, var5 + 0.0, var27, var13, var19);
      var9.addVertexWithUV(var21, var5 + 0.0, var25, var15, var19);
      var9.addVertexWithUV(var21, var5 + 1.0, var25, var15, var17);
      var9.addVertexWithUV(var21, var5 + 1.0, var27, var13, var17);
      var9.addVertexWithUV(var21, var5 + 0.0, var27, var13, var19);
      var9.addVertexWithUV(var23, var5 + 0.0, var25, var15, var19);
      var9.addVertexWithUV(var23, var5 + 1.0, var25, var15, var17);
      var9.addVertexWithUV(var23, var5 + 1.0, var25, var13, var17);
      var9.addVertexWithUV(var23, var5 + 0.0, var25, var13, var19);
      var9.addVertexWithUV(var21, var5 + 0.0, var27, var15, var19);
      var9.addVertexWithUV(var21, var5 + 1.0, var27, var15, var17);
   }

   public void func_1245_b(Block var1, int var2, double var3, double var5, double var7) {
      Tessellator var9 = Tessellator.instance;
      int var10 = var1.getBlockTextureFromSideAndMetadata(0, var2);
      if (this.overrideBlockTexture >= 0) {
         var10 = this.overrideBlockTexture;
      }

      int var11 = (var10 & 15) << 4;
      int var12 = var10 & 240;
      double var13 = var11 / 256.0F;
      double var15 = (var11 + 15.99F) / 256.0F;
      double var17 = var12 / 256.0F;
      double var19 = (var12 + 15.99F) / 256.0F;
      double var21 = var3 + 0.5 - 0.25;
      double var23 = var3 + 0.5 + 0.25;
      double var25 = var7 + 0.5 - 0.5;
      double var27 = var7 + 0.5 + 0.5;
      var9.addVertexWithUV(var21, var5 + 1.0, var25, var13, var17);
      var9.addVertexWithUV(var21, var5 + 0.0, var25, var13, var19);
      var9.addVertexWithUV(var21, var5 + 0.0, var27, var15, var19);
      var9.addVertexWithUV(var21, var5 + 1.0, var27, var15, var17);
      var9.addVertexWithUV(var21, var5 + 1.0, var27, var13, var17);
      var9.addVertexWithUV(var21, var5 + 0.0, var27, var13, var19);
      var9.addVertexWithUV(var21, var5 + 0.0, var25, var15, var19);
      var9.addVertexWithUV(var21, var5 + 1.0, var25, var15, var17);
      var9.addVertexWithUV(var23, var5 + 1.0, var27, var13, var17);
      var9.addVertexWithUV(var23, var5 + 0.0, var27, var13, var19);
      var9.addVertexWithUV(var23, var5 + 0.0, var25, var15, var19);
      var9.addVertexWithUV(var23, var5 + 1.0, var25, var15, var17);
      var9.addVertexWithUV(var23, var5 + 1.0, var25, var13, var17);
      var9.addVertexWithUV(var23, var5 + 0.0, var25, var13, var19);
      var9.addVertexWithUV(var23, var5 + 0.0, var27, var15, var19);
      var9.addVertexWithUV(var23, var5 + 1.0, var27, var15, var17);
      var21 = var3 + 0.5 - 0.5;
      var23 = var3 + 0.5 + 0.5;
      var25 = var7 + 0.5 - 0.25;
      var27 = var7 + 0.5 + 0.25;
      var9.addVertexWithUV(var21, var5 + 1.0, var25, var13, var17);
      var9.addVertexWithUV(var21, var5 + 0.0, var25, var13, var19);
      var9.addVertexWithUV(var23, var5 + 0.0, var25, var15, var19);
      var9.addVertexWithUV(var23, var5 + 1.0, var25, var15, var17);
      var9.addVertexWithUV(var23, var5 + 1.0, var25, var13, var17);
      var9.addVertexWithUV(var23, var5 + 0.0, var25, var13, var19);
      var9.addVertexWithUV(var21, var5 + 0.0, var25, var15, var19);
      var9.addVertexWithUV(var21, var5 + 1.0, var25, var15, var17);
      var9.addVertexWithUV(var23, var5 + 1.0, var27, var13, var17);
      var9.addVertexWithUV(var23, var5 + 0.0, var27, var13, var19);
      var9.addVertexWithUV(var21, var5 + 0.0, var27, var15, var19);
      var9.addVertexWithUV(var21, var5 + 1.0, var27, var15, var17);
      var9.addVertexWithUV(var21, var5 + 1.0, var27, var13, var17);
      var9.addVertexWithUV(var21, var5 + 0.0, var27, var13, var19);
      var9.addVertexWithUV(var23, var5 + 0.0, var27, var15, var19);
      var9.addVertexWithUV(var23, var5 + 1.0, var27, var15, var17);
   }

   public boolean renderBlockFluids(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = var1.colorMultiplier(this.blockAccess, var2, var3, var4);
      float var7 = (var6 >> 16 & 0xFF) / 255.0F;
      float var8 = (var6 >> 8 & 0xFF) / 255.0F;
      float var9 = (var6 & 0xFF) / 255.0F;
      boolean var10 = var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1);
      boolean var11 = var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0);
      boolean[] var12 = new boolean[]{var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2), var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3), var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4), var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)};
      if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3]) {
         return false;
      }

      boolean var13 = false;
      float var14 = 0.5F;
      float var15 = 1.0F;
      float var16 = 0.8F;
      float var17 = 0.6F;
      double var18 = 0.0;
      double var20 = 1.0;
      Material var22 = var1.blockMaterial;
      int var23 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      float var24 = this.func_1224_a(var2, var3, var4, var22);
      float var25 = this.func_1224_a(var2, var3, var4 + 1, var22);
      float var26 = this.func_1224_a(var2 + 1, var3, var4 + 1, var22);
      float var27 = this.func_1224_a(var2 + 1, var3, var4, var22);
      if (this.renderAllFaces || var10) {
         var13 = true;
         int var28 = var1.getBlockTextureFromSideAndMetadata(1, var23);
         float var29 = (float)BlockFluid.getFlowDirection(this.blockAccess, var2, var3, var4, var22);
         if (var29 > -999.0F) {
            var28 = var1.getBlockTextureFromSideAndMetadata(2, var23);
         }

         int var30 = (var28 & 15) << 4;
         int var31 = var28 & 240;
         double var32 = (var30 + 8.0) / 256.0;
         double var34 = (var31 + 8.0) / 256.0;
         if (var29 < -999.0F) {
            var29 = 0.0F;
         } else {
            var32 = (var30 + 16) / 256.0F;
            var34 = (var31 + 16) / 256.0F;
         }

         float var36 = MathHelper.sin(var29) * 8.0F / 256.0F;
         float var37 = MathHelper.cos(var29) * 8.0F / 256.0F;
         float var38 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
         var5.setColorOpaque_F(var15 * var38 * var7, var15 * var38 * var8, var15 * var38 * var9);
         var5.addVertexWithUV(var2 + 0, var3 + var24, var4 + 0, var32 - var37 - var36, var34 - var37 + var36);
         var5.addVertexWithUV(var2 + 0, var3 + var25, var4 + 1, var32 - var37 + var36, var34 + var37 + var36);
         var5.addVertexWithUV(var2 + 1, var3 + var26, var4 + 1, var32 + var37 + var36, var34 + var37 - var36);
         var5.addVertexWithUV(var2 + 1, var3 + var27, var4 + 0, var32 + var37 - var36, var34 - var37 - var36);
      }

      if (this.renderAllFaces || var11) {
         float var52 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
         var5.setColorOpaque_F(var14 * var52, var14 * var52, var14 * var52);
         this.renderBottomFace(var1, var2, var3, var4, var1.getBlockTextureFromSide(0));
         var13 = true;
      }

      for (int var53 = 0; var53 < 4; var53++) {
         int var54 = var2;
         int var55 = var3;
         int var56 = var4;
         if (var53 == 0) {
            var56--;
         }

         if (var53 == 1) {
            var56++;
         }

         if (var53 == 2) {
            var54--;
         }

         if (var53 == 3) {
            var54++;
         }

         int var57 = var1.getBlockTextureFromSideAndMetadata(var53 + 2, var23);
         int var33 = (var57 & 15) << 4;
         int var58 = var57 & 240;
         if (this.renderAllFaces || var12[var53]) {
            float var35;
            float var39;
            float var40;
            float var59;
            float var60;
            float var61;
            if (var53 == 0) {
               var35 = var24;
               var59 = var27;
               var60 = var2;
               var39 = var2 + 1;
               var61 = var4;
               var40 = var4;
            } else if (var53 == 1) {
               var35 = var26;
               var59 = var25;
               var60 = var2 + 1;
               var39 = var2;
               var61 = var4 + 1;
               var40 = var4 + 1;
            } else if (var53 == 2) {
               var35 = var25;
               var59 = var24;
               var60 = var2;
               var39 = var2;
               var61 = var4 + 1;
               var40 = var4;
            } else {
               var35 = var27;
               var59 = var26;
               var60 = var2 + 1;
               var39 = var2 + 1;
               var61 = var4;
               var40 = var4 + 1;
            }

            var13 = true;
            double var41 = (var33 + 0) / 256.0F;
            double var43 = (var33 + 16 - 0.01) / 256.0;
            double var45 = (var58 + (1.0F - var35) * 16.0F) / 256.0F;
            double var47 = (var58 + (1.0F - var59) * 16.0F) / 256.0F;
            double var49 = (var58 + 16 - 0.01) / 256.0;
            float var51 = var1.getBlockBrightness(this.blockAccess, var54, var55, var56);
            if (var53 < 2) {
               var51 *= var16;
            } else {
               var51 *= var17;
            }

            var5.setColorOpaque_F(var15 * var51 * var7, var15 * var51 * var8, var15 * var51 * var9);
            var5.addVertexWithUV(var60, var3 + var35, var61, var41, var45);
            var5.addVertexWithUV(var39, var3 + var59, var40, var43, var47);
            var5.addVertexWithUV(var39, var3 + 0, var40, var43, var49);
            var5.addVertexWithUV(var60, var3 + 0, var61, var41, var49);
         }
      }

      var1.minY = var18;
      var1.maxY = var20;
      return var13;
   }

   private float func_1224_a(int var1, int var2, int var3, Material var4) {
      int var5 = 0;
      float var6 = 0.0F;

      for (int var7 = 0; var7 < 4; var7++) {
         int var8 = var1 - (var7 & 1);
         int var9 = var2;
         int var10 = var3 - (var7 >> 1 & 1);
         if (this.blockAccess.getBlockMaterial(var8, var9 + 1, var10) == var4) {
            return 1.0F;
         }

         Material var11 = this.blockAccess.getBlockMaterial(var8, var9, var10);
         if (var11 == var4) {
            int var12 = this.blockAccess.getBlockMetadata(var8, var9, var10);
            if (var12 >= 8 || var12 == 0) {
               var6 += BlockFluid.getPercentAir(var12) * 10.0F;
               var5 += 10;
            }

            var6 += BlockFluid.getPercentAir(var12);
            var5++;
         } else if (!var11.isSolid()) {
            var6++;
            var5++;
         }
      }

      return 1.0F - var6 / var5;
   }

   public void renderBlockFallingSand(Block var1, World var2, int var3, int var4, int var5) {
      float var6 = 0.5F;
      float var7 = 1.0F;
      float var8 = 0.8F;
      float var9 = 0.6F;
      Tessellator var10 = Tessellator.instance;
      var10.startDrawingQuads();
      float var11 = var1.getBlockBrightness(var2, var3, var4, var5);
      float var12 = var1.getBlockBrightness(var2, var3, var4 - 1, var5);
      if (var12 < var11) {
         var12 = var11;
      }

      var10.setColorOpaque_F(var6 * var12, var6 * var12, var6 * var12);
      this.renderBottomFace(var1, -0.5, -0.5, -0.5, var1.getBlockTextureFromSide(0));
      var12 = var1.getBlockBrightness(var2, var3, var4 + 1, var5);
      if (var12 < var11) {
         var12 = var11;
      }

      var10.setColorOpaque_F(var7 * var12, var7 * var12, var7 * var12);
      this.renderTopFace(var1, -0.5, -0.5, -0.5, var1.getBlockTextureFromSide(1));
      var12 = var1.getBlockBrightness(var2, var3, var4, var5 - 1);
      if (var12 < var11) {
         var12 = var11;
      }

      var10.setColorOpaque_F(var8 * var12, var8 * var12, var8 * var12);
      this.renderEastFace(var1, -0.5, -0.5, -0.5, var1.getBlockTextureFromSide(2));
      var12 = var1.getBlockBrightness(var2, var3, var4, var5 + 1);
      if (var12 < var11) {
         var12 = var11;
      }

      var10.setColorOpaque_F(var8 * var12, var8 * var12, var8 * var12);
      this.renderWestFace(var1, -0.5, -0.5, -0.5, var1.getBlockTextureFromSide(3));
      var12 = var1.getBlockBrightness(var2, var3 - 1, var4, var5);
      if (var12 < var11) {
         var12 = var11;
      }

      var10.setColorOpaque_F(var9 * var12, var9 * var12, var9 * var12);
      this.renderNorthFace(var1, -0.5, -0.5, -0.5, var1.getBlockTextureFromSide(4));
      var12 = var1.getBlockBrightness(var2, var3 + 1, var4, var5);
      if (var12 < var11) {
         var12 = var11;
      }

      var10.setColorOpaque_F(var9 * var12, var9 * var12, var9 * var12);
      this.renderSouthFace(var1, -0.5, -0.5, -0.5, var1.getBlockTextureFromSide(5));
      var10.draw();
   }

   public boolean renderStandardBlock(Block var1, int var2, int var3, int var4) {
      int var5 = var1.colorMultiplier(this.blockAccess, var2, var3, var4);
      float var6 = (var5 >> 16 & 0xFF) / 255.0F;
      float var7 = (var5 >> 8 & 0xFF) / 255.0F;
      float var8 = (var5 & 0xFF) / 255.0F;
      if (EntityRenderer.anaglyphEnable) {
         float var9 = (var6 * 30.0F + var7 * 59.0F + var8 * 11.0F) / 100.0F;
         float var10 = (var6 * 30.0F + var7 * 70.0F) / 100.0F;
         float var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
         var6 = var9;
         var7 = var10;
         var8 = var11;
      }

      return Minecraft.isAmbientOcclusionEnabled() ? this.renderStandardBlockWithAmbientOcclusion(var1, var2, var3, var4, var6, var7, var8) : this.renderStandardBlockWithColorMultiplier(var1, var2, var3, var4, var6, var7, var8);
   }

   public boolean renderStandardBlockWithAmbientOcclusion(Block var1, int var2, int var3, int var4, float var5, float var6, float var7) {
      this.enableAO = true;
      boolean var8 = Config.getAmbientOcclusionLevel() > 0.0F;
      boolean var9 = Config.isBetterGrass();
      boolean var10 = false;
      float var11 = this.lightValueOwn;
      float var12 = this.lightValueOwn;
      float var13 = this.lightValueOwn;
      float var14 = this.lightValueOwn;
      boolean var15 = true;
      boolean var16 = true;
      boolean var17 = true;
      boolean var18 = true;
      boolean var19 = true;
      boolean var20 = true;
      this.lightValueOwn = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      this.aoLightValueXNeg = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
      this.aoLightValueYNeg = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
      this.aoLightValueZNeg = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
      this.aoLightValueXPos = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
      this.aoLightValueYPos = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
      this.aoLightValueZPos = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
      this.field_22338_U = Block.canBlockGrass[this.blockAccess.getBlockId(var2 + 1, var3 + 1, var4)];
      this.field_22359_ac = Block.canBlockGrass[this.blockAccess.getBlockId(var2 + 1, var3 - 1, var4)];
      this.field_22334_Y = Block.canBlockGrass[this.blockAccess.getBlockId(var2 + 1, var3, var4 + 1)];
      this.field_22363_aa = Block.canBlockGrass[this.blockAccess.getBlockId(var2 + 1, var3, var4 - 1)];
      this.field_22337_V = Block.canBlockGrass[this.blockAccess.getBlockId(var2 - 1, var3 + 1, var4)];
      this.field_22357_ad = Block.canBlockGrass[this.blockAccess.getBlockId(var2 - 1, var3 - 1, var4)];
      this.field_22335_X = Block.canBlockGrass[this.blockAccess.getBlockId(var2 - 1, var3, var4 - 1)];
      this.field_22333_Z = Block.canBlockGrass[this.blockAccess.getBlockId(var2 - 1, var3, var4 + 1)];
      this.field_22336_W = Block.canBlockGrass[this.blockAccess.getBlockId(var2, var3 + 1, var4 + 1)];
      this.field_22339_T = Block.canBlockGrass[this.blockAccess.getBlockId(var2, var3 + 1, var4 - 1)];
      this.field_22355_ae = Block.canBlockGrass[this.blockAccess.getBlockId(var2, var3 - 1, var4 + 1)];
      this.field_22361_ab = Block.canBlockGrass[this.blockAccess.getBlockId(var2, var3 - 1, var4 - 1)];
      if (var1.blockIndexInTexture == 3) {
         var20 = false;
         var19 = false;
         var18 = false;
         var17 = false;
         var15 = false;
      }

      if (this.overrideBlockTexture >= 0) {
         var20 = false;
         var19 = false;
         var18 = false;
         var17 = false;
         var15 = false;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
         float var21;
         float var22;
         float var23;
         float var24;
         if (this.field_22352_G <= 0) {
            var24 = this.aoLightValueYNeg;
            var23 = this.aoLightValueYNeg;
            var22 = this.aoLightValueYNeg;
            var21 = this.aoLightValueYNeg;
         } else {
            var3--;
            this.field_22376_n = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
            this.field_22374_p = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
            this.field_22373_q = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
            this.field_22371_s = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
            if (!this.field_22361_ab && !this.field_22357_ad) {
               this.field_22377_m = this.field_22376_n;
            } else {
               this.field_22377_m = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4 - 1);
            }

            if (!this.field_22355_ae && !this.field_22357_ad) {
               this.field_22375_o = this.field_22376_n;
            } else {
               this.field_22375_o = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4 + 1);
            }

            if (!this.field_22361_ab && !this.field_22359_ac) {
               this.field_22372_r = this.field_22371_s;
            } else {
               this.field_22372_r = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4 - 1);
            }

            if (!this.field_22355_ae && !this.field_22359_ac) {
               this.field_22370_t = this.field_22371_s;
            } else {
               this.field_22370_t = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4 + 1);
            }

            var3++;
            if (var8) {
               this.field_22375_o = Config.fixAoLight(this.field_22375_o, this.aoLightValueYNeg);
               this.field_22376_n = Config.fixAoLight(this.field_22376_n, this.aoLightValueYNeg);
               this.field_22373_q = Config.fixAoLight(this.field_22373_q, this.aoLightValueYNeg);
               this.field_22370_t = Config.fixAoLight(this.field_22370_t, this.aoLightValueYNeg);
               this.field_22371_s = Config.fixAoLight(this.field_22371_s, this.aoLightValueYNeg);
               this.field_22374_p = Config.fixAoLight(this.field_22374_p, this.aoLightValueYNeg);
               this.field_22372_r = Config.fixAoLight(this.field_22372_r, this.aoLightValueYNeg);
               this.field_22377_m = Config.fixAoLight(this.field_22377_m, this.aoLightValueYNeg);
            }

            var21 = (this.field_22375_o + this.field_22376_n + this.field_22373_q + this.aoLightValueYNeg) / 4.0F;
            var24 = (this.field_22373_q + this.aoLightValueYNeg + this.field_22370_t + this.field_22371_s) / 4.0F;
            var23 = (this.aoLightValueYNeg + this.field_22374_p + this.field_22371_s + this.field_22372_r) / 4.0F;
            var22 = (this.field_22376_n + this.field_22377_m + this.aoLightValueYNeg + this.field_22374_p) / 4.0F;
         }

         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var15 ? var5 : 1.0F) * 0.5F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var15 ? var6 : 1.0F) * 0.5F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var15 ? var7 : 1.0F) * 0.5F;
         this.colorRedTopLeft *= var21;
         this.colorGreenTopLeft *= var21;
         this.colorBlueTopLeft *= var21;
         this.colorRedBottomLeft *= var22;
         this.colorGreenBottomLeft *= var22;
         this.colorBlueBottomLeft *= var22;
         this.colorRedBottomRight *= var23;
         this.colorGreenBottomRight *= var23;
         this.colorBlueBottomRight *= var23;
         this.colorRedTopRight *= var24;
         this.colorGreenTopRight *= var24;
         this.colorBlueTopRight *= var24;
         this.renderBottomFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0));
         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
         float var32;
         float var37;
         float var42;
         float var47;
         if (this.field_22352_G <= 0) {
            var47 = this.aoLightValueYPos;
            var42 = this.aoLightValueYPos;
            var37 = this.aoLightValueYPos;
            var32 = this.aoLightValueYPos;
         } else {
            var3++;
            this.field_22368_v = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
            this.field_22364_z = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
            this.field_22366_x = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
            this.field_22362_A = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
            if (!this.field_22339_T && !this.field_22337_V) {
               this.field_22369_u = this.field_22368_v;
            } else {
               this.field_22369_u = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4 - 1);
            }

            if (!this.field_22339_T && !this.field_22338_U) {
               this.field_22365_y = this.field_22364_z;
            } else {
               this.field_22365_y = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4 - 1);
            }

            if (!this.field_22336_W && !this.field_22337_V) {
               this.field_22367_w = this.field_22368_v;
            } else {
               this.field_22367_w = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4 + 1);
            }

            if (!this.field_22336_W && !this.field_22338_U) {
               this.field_22360_B = this.field_22364_z;
            } else {
               this.field_22360_B = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4 + 1);
            }

            var3--;
            if (var8) {
               this.field_22367_w = Config.fixAoLight(this.field_22367_w, this.aoLightValueYPos);
               this.field_22368_v = Config.fixAoLight(this.field_22368_v, this.aoLightValueYPos);
               this.field_22362_A = Config.fixAoLight(this.field_22362_A, this.aoLightValueYPos);
               this.field_22360_B = Config.fixAoLight(this.field_22360_B, this.aoLightValueYPos);
               this.field_22364_z = Config.fixAoLight(this.field_22364_z, this.aoLightValueYPos);
               this.field_22366_x = Config.fixAoLight(this.field_22366_x, this.aoLightValueYPos);
               this.field_22365_y = Config.fixAoLight(this.field_22365_y, this.aoLightValueYPos);
               this.field_22369_u = Config.fixAoLight(this.field_22369_u, this.aoLightValueYPos);
            }

            var47 = (this.field_22367_w + this.field_22368_v + this.field_22362_A + this.aoLightValueYPos) / 4.0F;
            var32 = (this.field_22362_A + this.aoLightValueYPos + this.field_22360_B + this.field_22364_z) / 4.0F;
            var37 = (this.aoLightValueYPos + this.field_22366_x + this.field_22364_z + this.field_22365_y) / 4.0F;
            var42 = (this.field_22368_v + this.field_22369_u + this.aoLightValueYPos + this.field_22366_x) / 4.0F;
         }

         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = var16 ? var5 : 1.0F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = var16 ? var6 : 1.0F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = var16 ? var7 : 1.0F;
         this.colorRedTopLeft *= var32;
         this.colorGreenTopLeft *= var32;
         this.colorBlueTopLeft *= var32;
         this.colorRedBottomLeft *= var37;
         this.colorGreenBottomLeft *= var37;
         this.colorBlueBottomLeft *= var37;
         this.colorRedBottomRight *= var42;
         this.colorGreenBottomRight *= var42;
         this.colorBlueBottomRight *= var42;
         this.colorRedTopRight *= var47;
         this.colorGreenTopRight *= var47;
         this.colorBlueTopRight *= var47;
         this.renderTopFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1));
         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
         float var33;
         float var38;
         float var43;
         float var48;
         if (this.field_22352_G <= 0) {
            var48 = this.aoLightValueZNeg;
            var43 = this.aoLightValueZNeg;
            var38 = this.aoLightValueZNeg;
            var33 = this.aoLightValueZNeg;
         } else {
            var4--;
            this.field_22358_C = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
            this.field_22374_p = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
            this.field_22366_x = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
            this.field_22356_D = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
            if (!this.field_22335_X && !this.field_22361_ab) {
               this.field_22377_m = this.field_22358_C;
            } else {
               this.field_22377_m = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3 - 1, var4);
            }

            if (!this.field_22335_X && !this.field_22339_T) {
               this.field_22369_u = this.field_22358_C;
            } else {
               this.field_22369_u = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3 + 1, var4);
            }

            if (!this.field_22363_aa && !this.field_22361_ab) {
               this.field_22372_r = this.field_22356_D;
            } else {
               this.field_22372_r = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3 - 1, var4);
            }

            if (!this.field_22363_aa && !this.field_22339_T) {
               this.field_22365_y = this.field_22356_D;
            } else {
               this.field_22365_y = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3 + 1, var4);
            }

            var4++;
            if (var8) {
               this.field_22358_C = Config.fixAoLight(this.field_22358_C, this.aoLightValueZNeg);
               this.field_22369_u = Config.fixAoLight(this.field_22369_u, this.aoLightValueZNeg);
               this.field_22366_x = Config.fixAoLight(this.field_22366_x, this.aoLightValueZNeg);
               this.field_22356_D = Config.fixAoLight(this.field_22356_D, this.aoLightValueZNeg);
               this.field_22365_y = Config.fixAoLight(this.field_22365_y, this.aoLightValueZNeg);
               this.field_22374_p = Config.fixAoLight(this.field_22374_p, this.aoLightValueZNeg);
               this.field_22372_r = Config.fixAoLight(this.field_22372_r, this.aoLightValueZNeg);
               this.field_22377_m = Config.fixAoLight(this.field_22377_m, this.aoLightValueZNeg);
            }

            var33 = (this.field_22358_C + this.field_22369_u + this.aoLightValueZNeg + this.field_22366_x) / 4.0F;
            var38 = (this.aoLightValueZNeg + this.field_22366_x + this.field_22356_D + this.field_22365_y) / 4.0F;
            var43 = (this.field_22374_p + this.aoLightValueZNeg + this.field_22372_r + this.field_22356_D) / 4.0F;
            var48 = (this.field_22377_m + this.field_22358_C + this.field_22374_p + this.aoLightValueZNeg) / 4.0F;
         }

         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var17 ? var5 : 1.0F) * 0.8F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var17 ? var6 : 1.0F) * 0.8F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var17 ? var7 : 1.0F) * 0.8F;
         this.colorRedTopLeft *= var33;
         this.colorGreenTopLeft *= var33;
         this.colorBlueTopLeft *= var33;
         this.colorRedBottomLeft *= var38;
         this.colorGreenBottomLeft *= var38;
         this.colorBlueBottomLeft *= var38;
         this.colorRedBottomRight *= var43;
         this.colorGreenBottomRight *= var43;
         this.colorBlueBottomRight *= var43;
         this.colorRedTopRight *= var48;
         this.colorGreenTopRight *= var48;
         this.colorBlueTopRight *= var48;
         int var25 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2);
         if (var9) {
            if (var25 == 3) {
               var25 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 2);
               if (var25 == 0) {
                  this.colorRedTopLeft *= var5;
                  this.colorRedBottomLeft *= var5;
                  this.colorRedBottomRight *= var5;
                  this.colorRedTopRight *= var5;
                  this.colorGreenTopLeft *= var6;
                  this.colorGreenBottomLeft *= var6;
                  this.colorGreenBottomRight *= var6;
                  this.colorGreenTopRight *= var6;
                  this.colorBlueTopLeft *= var7;
                  this.colorBlueBottomLeft *= var7;
                  this.colorBlueBottomRight *= var7;
                  this.colorBlueTopRight *= var7;
               }
            }

            if (var25 == 68) {
               var25 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 2);
            }
         }

         this.renderEastFace(var1, var2, var3, var4, var25);
         if (fancyGrass && var25 == 3 && this.overrideBlockTexture < 0) {
            this.colorRedTopLeft *= var5;
            this.colorRedBottomLeft *= var5;
            this.colorRedBottomRight *= var5;
            this.colorRedTopRight *= var5;
            this.colorGreenTopLeft *= var6;
            this.colorGreenBottomLeft *= var6;
            this.colorGreenBottomRight *= var6;
            this.colorGreenTopRight *= var6;
            this.colorBlueTopLeft *= var7;
            this.colorBlueBottomLeft *= var7;
            this.colorBlueBottomRight *= var7;
            this.colorBlueTopRight *= var7;
            this.renderEastFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
         float var34;
         float var39;
         float var44;
         float var49;
         if (this.field_22352_G <= 0) {
            var49 = this.aoLightValueZPos;
            var44 = this.aoLightValueZPos;
            var39 = this.aoLightValueZPos;
            var34 = this.aoLightValueZPos;
         } else {
            var4++;
            this.field_22354_E = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
            this.field_22353_F = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
            this.field_22373_q = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
            this.field_22362_A = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
            if (!this.field_22333_Z && !this.field_22355_ae) {
               this.field_22375_o = this.field_22354_E;
            } else {
               this.field_22375_o = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3 - 1, var4);
            }

            if (!this.field_22333_Z && !this.field_22336_W) {
               this.field_22367_w = this.field_22354_E;
            } else {
               this.field_22367_w = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3 + 1, var4);
            }

            if (!this.field_22334_Y && !this.field_22355_ae) {
               this.field_22370_t = this.field_22353_F;
            } else {
               this.field_22370_t = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3 - 1, var4);
            }

            if (!this.field_22334_Y && !this.field_22336_W) {
               this.field_22360_B = this.field_22353_F;
            } else {
               this.field_22360_B = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3 + 1, var4);
            }

            var4--;
            if (var8) {
               this.field_22354_E = Config.fixAoLight(this.field_22354_E, this.aoLightValueZPos);
               this.field_22367_w = Config.fixAoLight(this.field_22367_w, this.aoLightValueZPos);
               this.field_22362_A = Config.fixAoLight(this.field_22362_A, this.aoLightValueZPos);
               this.field_22353_F = Config.fixAoLight(this.field_22353_F, this.aoLightValueZPos);
               this.field_22360_B = Config.fixAoLight(this.field_22360_B, this.aoLightValueZPos);
               this.field_22373_q = Config.fixAoLight(this.field_22373_q, this.aoLightValueZPos);
               this.field_22370_t = Config.fixAoLight(this.field_22370_t, this.aoLightValueZPos);
               this.field_22375_o = Config.fixAoLight(this.field_22375_o, this.aoLightValueZPos);
            }

            var34 = (this.field_22354_E + this.field_22367_w + this.aoLightValueZPos + this.field_22362_A) / 4.0F;
            var49 = (this.aoLightValueZPos + this.field_22362_A + this.field_22353_F + this.field_22360_B) / 4.0F;
            var44 = (this.field_22373_q + this.aoLightValueZPos + this.field_22370_t + this.field_22353_F) / 4.0F;
            var39 = (this.field_22375_o + this.field_22354_E + this.field_22373_q + this.aoLightValueZPos) / 4.0F;
         }

         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var18 ? var5 : 1.0F) * 0.8F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var18 ? var6 : 1.0F) * 0.8F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var18 ? var7 : 1.0F) * 0.8F;
         this.colorRedTopLeft *= var34;
         this.colorGreenTopLeft *= var34;
         this.colorBlueTopLeft *= var34;
         this.colorRedBottomLeft *= var39;
         this.colorGreenBottomLeft *= var39;
         this.colorBlueBottomLeft *= var39;
         this.colorRedBottomRight *= var44;
         this.colorGreenBottomRight *= var44;
         this.colorBlueBottomRight *= var44;
         this.colorRedTopRight *= var49;
         this.colorGreenTopRight *= var49;
         this.colorBlueTopRight *= var49;
         int var52 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3);
         if (var9) {
            if (var52 == 3) {
               var52 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 3);
               if (var52 == 0) {
                  this.colorRedTopLeft *= var5;
                  this.colorRedBottomLeft *= var5;
                  this.colorRedBottomRight *= var5;
                  this.colorRedTopRight *= var5;
                  this.colorGreenTopLeft *= var6;
                  this.colorGreenBottomLeft *= var6;
                  this.colorGreenBottomRight *= var6;
                  this.colorGreenTopRight *= var6;
                  this.colorBlueTopLeft *= var7;
                  this.colorBlueBottomLeft *= var7;
                  this.colorBlueBottomRight *= var7;
                  this.colorBlueTopRight *= var7;
               }
            }

            if (var52 == 68) {
               var52 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 3);
            }
         }

         this.renderWestFace(var1, var2, var3, var4, var52);
         if (fancyGrass && var52 == 3 && this.overrideBlockTexture < 0) {
            this.colorRedTopLeft *= var5;
            this.colorRedBottomLeft *= var5;
            this.colorRedBottomRight *= var5;
            this.colorRedTopRight *= var5;
            this.colorGreenTopLeft *= var6;
            this.colorGreenBottomLeft *= var6;
            this.colorGreenBottomRight *= var6;
            this.colorGreenTopRight *= var6;
            this.colorBlueTopLeft *= var7;
            this.colorBlueBottomLeft *= var7;
            this.colorBlueBottomRight *= var7;
            this.colorBlueTopRight *= var7;
            this.renderWestFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
         float var35;
         float var40;
         float var45;
         float var50;
         if (this.field_22352_G <= 0) {
            var50 = this.aoLightValueXNeg;
            var45 = this.aoLightValueXNeg;
            var40 = this.aoLightValueXNeg;
            var35 = this.aoLightValueXNeg;
         } else {
            this.field_22376_n = var1.getBlockBrightness(this.blockAccess, --var2, var3 - 1, var4);
            this.field_22358_C = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
            this.field_22354_E = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
            this.field_22368_v = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
            if (!this.field_22335_X && !this.field_22357_ad) {
               this.field_22377_m = this.field_22358_C;
            } else {
               this.field_22377_m = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4 - 1);
            }

            if (!this.field_22333_Z && !this.field_22357_ad) {
               this.field_22375_o = this.field_22354_E;
            } else {
               this.field_22375_o = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4 + 1);
            }

            if (!this.field_22335_X && !this.field_22337_V) {
               this.field_22369_u = this.field_22358_C;
            } else {
               this.field_22369_u = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4 - 1);
            }

            if (!this.field_22333_Z && !this.field_22337_V) {
               this.field_22367_w = this.field_22354_E;
            } else {
               this.field_22367_w = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4 + 1);
            }

            var2++;
            if (var8) {
               this.field_22376_n = Config.fixAoLight(this.field_22376_n, this.aoLightValueXNeg);
               this.field_22375_o = Config.fixAoLight(this.field_22375_o, this.aoLightValueXNeg);
               this.field_22354_E = Config.fixAoLight(this.field_22354_E, this.aoLightValueXNeg);
               this.field_22368_v = Config.fixAoLight(this.field_22368_v, this.aoLightValueXNeg);
               this.field_22367_w = Config.fixAoLight(this.field_22367_w, this.aoLightValueXNeg);
               this.field_22358_C = Config.fixAoLight(this.field_22358_C, this.aoLightValueXNeg);
               this.field_22369_u = Config.fixAoLight(this.field_22369_u, this.aoLightValueXNeg);
               this.field_22377_m = Config.fixAoLight(this.field_22377_m, this.aoLightValueXNeg);
            }

            var50 = (this.field_22376_n + this.field_22375_o + this.aoLightValueXNeg + this.field_22354_E) / 4.0F;
            var35 = (this.aoLightValueXNeg + this.field_22354_E + this.field_22368_v + this.field_22367_w) / 4.0F;
            var40 = (this.field_22358_C + this.aoLightValueXNeg + this.field_22369_u + this.field_22368_v) / 4.0F;
            var45 = (this.field_22377_m + this.field_22376_n + this.field_22358_C + this.aoLightValueXNeg) / 4.0F;
         }

         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var19 ? var5 : 1.0F) * 0.6F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var19 ? var6 : 1.0F) * 0.6F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var19 ? var7 : 1.0F) * 0.6F;
         this.colorRedTopLeft *= var35;
         this.colorGreenTopLeft *= var35;
         this.colorBlueTopLeft *= var35;
         this.colorRedBottomLeft *= var40;
         this.colorGreenBottomLeft *= var40;
         this.colorBlueBottomLeft *= var40;
         this.colorRedBottomRight *= var45;
         this.colorGreenBottomRight *= var45;
         this.colorBlueBottomRight *= var45;
         this.colorRedTopRight *= var50;
         this.colorGreenTopRight *= var50;
         this.colorBlueTopRight *= var50;
         int var53 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4);
         if (var9) {
            if (var53 == 3) {
               var53 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 4);
               if (var53 == 0) {
                  this.colorRedTopLeft *= var5;
                  this.colorRedBottomLeft *= var5;
                  this.colorRedBottomRight *= var5;
                  this.colorRedTopRight *= var5;
                  this.colorGreenTopLeft *= var6;
                  this.colorGreenBottomLeft *= var6;
                  this.colorGreenBottomRight *= var6;
                  this.colorGreenTopRight *= var6;
                  this.colorBlueTopLeft *= var7;
                  this.colorBlueBottomLeft *= var7;
                  this.colorBlueBottomRight *= var7;
                  this.colorBlueTopRight *= var7;
               }
            }

            if (var53 == 68) {
               var53 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 4);
            }
         }

         this.renderNorthFace(var1, var2, var3, var4, var53);
         if (fancyGrass && var53 == 3 && this.overrideBlockTexture < 0) {
            this.colorRedTopLeft *= var5;
            this.colorRedBottomLeft *= var5;
            this.colorRedBottomRight *= var5;
            this.colorRedTopRight *= var5;
            this.colorGreenTopLeft *= var6;
            this.colorGreenBottomLeft *= var6;
            this.colorGreenBottomRight *= var6;
            this.colorGreenTopRight *= var6;
            this.colorBlueTopLeft *= var7;
            this.colorBlueBottomLeft *= var7;
            this.colorBlueBottomRight *= var7;
            this.colorBlueTopRight *= var7;
            this.renderNorthFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
         float var36;
         float var41;
         float var46;
         float var51;
         if (this.field_22352_G <= 0) {
            var51 = this.aoLightValueXPos;
            var46 = this.aoLightValueXPos;
            var41 = this.aoLightValueXPos;
            var36 = this.aoLightValueXPos;
         } else {
            this.field_22371_s = var1.getBlockBrightness(this.blockAccess, ++var2, var3 - 1, var4);
            this.field_22356_D = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
            this.field_22353_F = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
            this.field_22364_z = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
            if (!this.field_22359_ac && !this.field_22363_aa) {
               this.field_22372_r = this.field_22356_D;
            } else {
               this.field_22372_r = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4 - 1);
            }

            if (!this.field_22359_ac && !this.field_22334_Y) {
               this.field_22370_t = this.field_22353_F;
            } else {
               this.field_22370_t = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4 + 1);
            }

            if (!this.field_22338_U && !this.field_22363_aa) {
               this.field_22365_y = this.field_22356_D;
            } else {
               this.field_22365_y = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4 - 1);
            }

            if (!this.field_22338_U && !this.field_22334_Y) {
               this.field_22360_B = this.field_22353_F;
            } else {
               this.field_22360_B = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4 + 1);
            }

            var2--;
            if (var8) {
               this.field_22371_s = Config.fixAoLight(this.field_22371_s, this.aoLightValueXPos);
               this.field_22370_t = Config.fixAoLight(this.field_22370_t, this.aoLightValueXPos);
               this.field_22353_F = Config.fixAoLight(this.field_22353_F, this.aoLightValueXPos);
               this.field_22364_z = Config.fixAoLight(this.field_22364_z, this.aoLightValueXPos);
               this.field_22360_B = Config.fixAoLight(this.field_22360_B, this.aoLightValueXPos);
               this.field_22356_D = Config.fixAoLight(this.field_22356_D, this.aoLightValueXPos);
               this.field_22365_y = Config.fixAoLight(this.field_22365_y, this.aoLightValueXPos);
               this.field_22372_r = Config.fixAoLight(this.field_22372_r, this.aoLightValueXPos);
            }

            var36 = (this.field_22371_s + this.field_22370_t + this.aoLightValueXPos + this.field_22353_F) / 4.0F;
            var51 = (this.aoLightValueXPos + this.field_22353_F + this.field_22364_z + this.field_22360_B) / 4.0F;
            var46 = (this.field_22356_D + this.aoLightValueXPos + this.field_22365_y + this.field_22364_z) / 4.0F;
            var41 = (this.field_22372_r + this.field_22371_s + this.field_22356_D + this.aoLightValueXPos) / 4.0F;
         }

         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var20 ? var5 : 1.0F) * 0.6F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var20 ? var6 : 1.0F) * 0.6F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var20 ? var7 : 1.0F) * 0.6F;
         this.colorRedTopLeft *= var36;
         this.colorGreenTopLeft *= var36;
         this.colorBlueTopLeft *= var36;
         this.colorRedBottomLeft *= var41;
         this.colorGreenBottomLeft *= var41;
         this.colorBlueBottomLeft *= var41;
         this.colorRedBottomRight *= var46;
         this.colorGreenBottomRight *= var46;
         this.colorBlueBottomRight *= var46;
         this.colorRedTopRight *= var51;
         this.colorGreenTopRight *= var51;
         this.colorBlueTopRight *= var51;
         int var54 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5);
         if (var9) {
            if (var54 == 3) {
               var54 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 5);
               if (var54 == 0) {
                  this.colorRedTopLeft *= var5;
                  this.colorRedBottomLeft *= var5;
                  this.colorRedBottomRight *= var5;
                  this.colorRedTopRight *= var5;
                  this.colorGreenTopLeft *= var6;
                  this.colorGreenBottomLeft *= var6;
                  this.colorGreenBottomRight *= var6;
                  this.colorGreenTopRight *= var6;
                  this.colorBlueTopLeft *= var7;
                  this.colorBlueBottomLeft *= var7;
                  this.colorBlueBottomRight *= var7;
                  this.colorBlueTopRight *= var7;
               }
            }

            if (var54 == 68) {
               var54 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 5);
            }
         }

         this.renderSouthFace(var1, var2, var3, var4, var54);
         if (fancyGrass && var54 == 3 && this.overrideBlockTexture < 0) {
            this.colorRedTopLeft *= var5;
            this.colorRedBottomLeft *= var5;
            this.colorRedBottomRight *= var5;
            this.colorRedTopRight *= var5;
            this.colorGreenTopLeft *= var6;
            this.colorGreenBottomLeft *= var6;
            this.colorGreenBottomRight *= var6;
            this.colorGreenTopRight *= var6;
            this.colorBlueTopLeft *= var7;
            this.colorBlueBottomLeft *= var7;
            this.colorBlueBottomRight *= var7;
            this.colorBlueTopRight *= var7;
            this.renderSouthFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      this.enableAO = false;
      return var10;
   }

   public boolean renderStandardBlockWithColorMultiplier(Block var1, int var2, int var3, int var4, float var5, float var6, float var7) {
      this.enableAO = false;
      boolean var8 = Config.isBetterGrass();
      Tessellator var9 = Tessellator.instance;
      boolean var10 = false;
      float var11 = 0.5F;
      float var12 = 1.0F;
      float var13 = 0.8F;
      float var14 = 0.6F;
      float var15 = var12 * var5;
      float var16 = var12 * var6;
      float var17 = var12 * var7;
      float var18 = var11;
      float var19 = var13;
      float var20 = var14;
      float var21 = var11;
      float var22 = var13;
      float var23 = var14;
      float var24 = var11;
      float var25 = var13;
      float var26 = var14;
      if (var1 != Block.grass) {
         var18 *= var5;
         var19 *= var5;
         var20 *= var5;
         var21 *= var6;
         var22 *= var6;
         var23 *= var6;
         var24 *= var7;
         var25 *= var7;
         var26 *= var7;
      }

      float var27 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
         float var28 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
         var9.setColorOpaque_F(var18 * var28, var21 * var28, var24 * var28);
         this.renderBottomFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0));
         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
         float var30 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
         if (var1.maxY != 1.0 && !var1.blockMaterial.getIsLiquid()) {
            var30 = var27;
         }

         var9.setColorOpaque_F(var15 * var30, var16 * var30, var17 * var30);
         this.renderTopFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1));
         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
         float var31 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
         if (var1.minZ > 0.0) {
            var31 = var27;
         }

         var9.setColorOpaque_F(var19 * var31, var22 * var31, var25 * var31);
         int var29 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2);
         if (var8) {
            if (var29 == 3) {
               var29 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 2);
               if (var29 == 0) {
                  var9.setColorOpaque_F(var19 * var31 * var5, var22 * var31 * var6, var25 * var31 * var7);
               }
            }

            if (var29 == 68) {
               var29 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 2);
            }
         }

         this.renderEastFace(var1, var2, var3, var4, var29);
         if (fancyGrass && var29 == 3 && this.overrideBlockTexture < 0) {
            var9.setColorOpaque_F(var19 * var31 * var5, var22 * var31 * var6, var25 * var31 * var7);
            this.renderEastFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
         float var32 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
         if (var1.maxZ < 1.0) {
            var32 = var27;
         }

         var9.setColorOpaque_F(var19 * var32, var22 * var32, var25 * var32);
         int var35 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3);
         if (var8) {
            if (var35 == 3) {
               var35 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 3);
               if (var35 == 0) {
                  var9.setColorOpaque_F(var19 * var32 * var5, var22 * var32 * var6, var25 * var32 * var7);
               }
            }

            if (var35 == 68) {
               var35 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 3);
            }
         }

         this.renderWestFace(var1, var2, var3, var4, var35);
         if ((fancyGrass || Config.isBetterGrass()) && var35 == 3 && this.overrideBlockTexture < 0) {
            var9.setColorOpaque_F(var19 * var32 * var5, var22 * var32 * var6, var25 * var32 * var7);
            this.renderWestFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
         float var33 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
         if (var1.minX > 0.0) {
            var33 = var27;
         }

         var9.setColorOpaque_F(var20 * var33, var23 * var33, var26 * var33);
         int var36 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4);
         if (var8) {
            if (var36 == 3) {
               var36 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 4);
               if (var36 == 0) {
                  var9.setColorOpaque_F(var20 * var33 * var5, var23 * var33 * var6, var26 * var33 * var7);
               }
            }

            if (var36 == 68) {
               var36 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 4);
            }
         }

         this.renderNorthFace(var1, var2, var3, var4, var36);
         if ((fancyGrass || Config.isBetterGrass()) && var36 == 3 && this.overrideBlockTexture < 0) {
            var9.setColorOpaque_F(var20 * var33 * var5, var23 * var33 * var6, var26 * var33 * var7);
            this.renderNorthFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
         float var34 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
         if (var1.maxX < 1.0) {
            var34 = var27;
         }

         var9.setColorOpaque_F(var20 * var34, var23 * var34, var26 * var34);
         int var37 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5);
         if (var8) {
            if (var37 == 3) {
               var37 = Config.getSideGrassTexture(this.blockAccess, var2, var3, var4, 5);
               if (var37 == 0) {
                  var9.setColorOpaque_F(var20 * var34 * var5, var23 * var34 * var6, var26 * var34 * var7);
               }
            }

            if (var37 == 68) {
               var37 = Config.getSideSnowGrassTexture(this.blockAccess, var2, var3, var4, 5);
            }
         }

         this.renderSouthFace(var1, var2, var3, var4, var37);
         if ((fancyGrass || Config.isBetterGrass()) && var37 == 3 && this.overrideBlockTexture < 0) {
            var9.setColorOpaque_F(var20 * var34 * var5, var23 * var34 * var6, var26 * var34 * var7);
            this.renderSouthFace(var1, var2, var3, var4, 38);
         }

         var10 = true;
      }

      return var10;
   }

   public boolean renderBlockCactus(Block var1, int var2, int var3, int var4) {
      int var5 = var1.colorMultiplier(this.blockAccess, var2, var3, var4);
      float var6 = (var5 >> 16 & 0xFF) / 255.0F;
      float var7 = (var5 >> 8 & 0xFF) / 255.0F;
      float var8 = (var5 & 0xFF) / 255.0F;
      if (EntityRenderer.anaglyphEnable) {
         float var9 = (var6 * 30.0F + var7 * 59.0F + var8 * 11.0F) / 100.0F;
         float var10 = (var6 * 30.0F + var7 * 70.0F) / 100.0F;
         float var11 = (var6 * 30.0F + var8 * 70.0F) / 100.0F;
         var6 = var9;
         var7 = var10;
         var8 = var11;
      }

      return this.func_1230_b(var1, var2, var3, var4, var6, var7, var8);
   }

   public boolean func_1230_b(Block var1, int var2, int var3, int var4, float var5, float var6, float var7) {
      Tessellator var8 = Tessellator.instance;
      boolean var9 = false;
      float var10 = 0.5F;
      float var11 = 1.0F;
      float var12 = 0.8F;
      float var13 = 0.6F;
      float var14 = var10 * var5;
      float var15 = var11 * var5;
      float var16 = var12 * var5;
      float var17 = var13 * var5;
      float var18 = var10 * var6;
      float var19 = var11 * var6;
      float var20 = var12 * var6;
      float var21 = var13 * var6;
      float var22 = var10 * var7;
      float var23 = var11 * var7;
      float var24 = var12 * var7;
      float var25 = var13 * var7;
      float var26 = 0.0625F;
      float var27 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 - 1, var4, 0)) {
         float var28 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
         var8.setColorOpaque_F(var14 * var28, var18 * var28, var22 * var28);
         this.renderBottomFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0));
         var9 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3 + 1, var4, 1)) {
         float var29 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
         if (var1.maxY != 1.0 && !var1.blockMaterial.getIsLiquid()) {
            var29 = var27;
         }

         var8.setColorOpaque_F(var15 * var29, var19 * var29, var23 * var29);
         this.renderTopFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1));
         var9 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 - 1, 2)) {
         float var30 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
         if (var1.minZ > 0.0) {
            var30 = var27;
         }

         var8.setColorOpaque_F(var16 * var30, var20 * var30, var24 * var30);
         var8.setTranslationF(0.0F, 0.0F, var26);
         this.renderEastFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2));
         var8.setTranslationF(0.0F, 0.0F, -var26);
         var9 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2, var3, var4 + 1, 3)) {
         float var31 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
         if (var1.maxZ < 1.0) {
            var31 = var27;
         }

         var8.setColorOpaque_F(var16 * var31, var20 * var31, var24 * var31);
         var8.setTranslationF(0.0F, 0.0F, -var26);
         this.renderWestFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3));
         var8.setTranslationF(0.0F, 0.0F, var26);
         var9 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 - 1, var3, var4, 4)) {
         float var32 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
         if (var1.minX > 0.0) {
            var32 = var27;
         }

         var8.setColorOpaque_F(var17 * var32, var21 * var32, var25 * var32);
         var8.setTranslationF(var26, 0.0F, 0.0F);
         this.renderNorthFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4));
         var8.setTranslationF(-var26, 0.0F, 0.0F);
         var9 = true;
      }

      if (this.renderAllFaces || var1.shouldSideBeRendered(this.blockAccess, var2 + 1, var3, var4, 5)) {
         float var33 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
         if (var1.maxX < 1.0) {
            var33 = var27;
         }

         var8.setColorOpaque_F(var17 * var33, var21 * var33, var25 * var33);
         var8.setTranslationF(-var26, 0.0F, 0.0F);
         this.renderSouthFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5));
         var8.setTranslationF(var26, 0.0F, 0.0F);
         var9 = true;
      }

      return var9;
   }

   public boolean renderBlockFence(Block var1, int var2, int var3, int var4) {
      boolean var5 = false;
      float var6 = 0.375F;
      float var7 = 0.625F;
      var1.setBlockBounds(var6, 0.0F, var6, var7, 1.0F, var7);
      this.renderStandardBlock(var1, var2, var3, var4);
      var5 = true;
      boolean var8 = false;
      boolean var9 = false;
      if (this.blockAccess.getBlockId(var2 - 1, var3, var4) == var1.blockID || this.blockAccess.getBlockId(var2 + 1, var3, var4) == var1.blockID) {
         var8 = true;
      }

      if (this.blockAccess.getBlockId(var2, var3, var4 - 1) == var1.blockID || this.blockAccess.getBlockId(var2, var3, var4 + 1) == var1.blockID) {
         var9 = true;
      }

      boolean var10 = this.blockAccess.getBlockId(var2 - 1, var3, var4) == var1.blockID;
      boolean var11 = this.blockAccess.getBlockId(var2 + 1, var3, var4) == var1.blockID;
      boolean var12 = this.blockAccess.getBlockId(var2, var3, var4 - 1) == var1.blockID;
      boolean var13 = this.blockAccess.getBlockId(var2, var3, var4 + 1) == var1.blockID;
      if (!var8 && !var9) {
         var8 = true;
      }

      var6 = 0.4375F;
      var7 = 0.5625F;
      float var14 = 0.75F;
      float var15 = 0.9375F;
      float var16 = var10 ? 0.0F : var6;
      float var17 = var11 ? 1.0F : var7;
      float var18 = var12 ? 0.0F : var6;
      float var19 = var13 ? 1.0F : var7;
      if (var8) {
         var1.setBlockBounds(var16, var14, var6, var17, var15, var7);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      }

      if (var9) {
         var1.setBlockBounds(var6, var14, var18, var7, var15, var19);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      }

      var14 = 0.375F;
      var15 = 0.5625F;
      if (var8) {
         var1.setBlockBounds(var16, var14, var6, var17, var15, var7);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      }

      if (var9) {
         var1.setBlockBounds(var6, var14, var18, var7, var15, var19);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      }

      var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return var5;
   }

   public boolean renderBlockStairs(Block var1, int var2, int var3, int var4) {
      boolean var5 = false;
      int var6 = this.blockAccess.getBlockMetadata(var2, var3, var4);
      if (var6 == 0) {
         var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      } else if (var6 == 1) {
         var1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var1.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      } else if (var6 == 2) {
         var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      } else if (var6 == 3) {
         var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
         this.renderStandardBlock(var1, var2, var3, var4);
         var5 = true;
      }

      var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return var5;
   }

   public boolean renderBlockDoor(Block var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      BlockDoor var6 = (BlockDoor)var1;
      boolean var7 = false;
      float var8 = 0.5F;
      float var9 = 1.0F;
      float var10 = 0.8F;
      float var11 = 0.6F;
      float var12 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4);
      float var13 = var1.getBlockBrightness(this.blockAccess, var2, var3 - 1, var4);
      if (var6.minY > 0.0) {
         var13 = var12;
      }

      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var5.setColorOpaque_F(var8 * var13, var8 * var13, var8 * var13);
      this.renderBottomFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 0));
      var7 = true;
      var13 = var1.getBlockBrightness(this.blockAccess, var2, var3 + 1, var4);
      if (var6.maxY < 1.0) {
         var13 = var12;
      }

      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var5.setColorOpaque_F(var9 * var13, var9 * var13, var9 * var13);
      this.renderTopFace(var1, var2, var3, var4, var1.getBlockTexture(this.blockAccess, var2, var3, var4, 1));
      var7 = true;
      var13 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 - 1);
      if (var6.minZ > 0.0) {
         var13 = var12;
      }

      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var5.setColorOpaque_F(var10 * var13, var10 * var13, var10 * var13);
      int var14 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 2);
      if (var14 < 0) {
         this.flipTexture = true;
         var14 = -var14;
      }

      this.renderEastFace(var1, var2, var3, var4, var14);
      var7 = true;
      this.flipTexture = false;
      var13 = var1.getBlockBrightness(this.blockAccess, var2, var3, var4 + 1);
      if (var6.maxZ < 1.0) {
         var13 = var12;
      }

      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var5.setColorOpaque_F(var10 * var13, var10 * var13, var10 * var13);
      var14 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 3);
      if (var14 < 0) {
         this.flipTexture = true;
         var14 = -var14;
      }

      this.renderWestFace(var1, var2, var3, var4, var14);
      var7 = true;
      this.flipTexture = false;
      var13 = var1.getBlockBrightness(this.blockAccess, var2 - 1, var3, var4);
      if (var6.minX > 0.0) {
         var13 = var12;
      }

      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var5.setColorOpaque_F(var11 * var13, var11 * var13, var11 * var13);
      var14 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 4);
      if (var14 < 0) {
         this.flipTexture = true;
         var14 = -var14;
      }

      this.renderNorthFace(var1, var2, var3, var4, var14);
      var7 = true;
      this.flipTexture = false;
      var13 = var1.getBlockBrightness(this.blockAccess, var2 + 1, var3, var4);
      if (var6.maxX < 1.0) {
         var13 = var12;
      }

      if (Block.lightValue[var1.blockID] > 0) {
         var13 = 1.0F;
      }

      var5.setColorOpaque_F(var11 * var13, var11 * var13, var11 * var13);
      var14 = var1.getBlockTexture(this.blockAccess, var2, var3, var4, 5);
      if (var14 < 0) {
         this.flipTexture = true;
         var14 = -var14;
      }

      this.renderSouthFace(var1, var2, var3, var4, var14);
      var7 = true;
      this.flipTexture = false;
      return var7;
   }

   public void renderBottomFace(Block var1, double var2, double var4, double var6, int var8) {
      Tessellator var9 = Tessellator.instance;
      if (this.overrideBlockTexture >= 0) {
         var8 = this.overrideBlockTexture;
      }

      int var10 = (var8 & 15) << 4;
      int var11 = var8 & 240;
      double var12 = (var10 + var1.minX * 16.0) / 256.0;
      double var14 = (var10 + var1.maxX * 16.0 - 0.01) / 256.0;
      double var16 = (var11 + var1.minZ * 16.0) / 256.0;
      double var18 = (var11 + var1.maxZ * 16.0 - 0.01) / 256.0;
      if (var1.minX < 0.0 || var1.maxX > 1.0) {
         var12 = (var10 + 0.0F) / 256.0F;
         var14 = (var10 + 15.99F) / 256.0F;
      }

      if (var1.minZ < 0.0 || var1.maxZ > 1.0) {
         var16 = (var11 + 0.0F) / 256.0F;
         var18 = (var11 + 15.99F) / 256.0F;
      }

      double var20 = var14;
      double var22 = var12;
      double var24 = var16;
      double var26 = var18;
      if (this.field_31082_l == 2) {
         var12 = (var10 + var1.minZ * 16.0) / 256.0;
         var16 = (var11 + 16 - var1.maxX * 16.0) / 256.0;
         var14 = (var10 + var1.maxZ * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.minX * 16.0) / 256.0;
         var24 = var16;
         var26 = var18;
         var20 = var12;
         var22 = var14;
         var16 = var18;
         var18 = var24;
      } else if (this.field_31082_l == 1) {
         var12 = (var10 + 16 - var1.maxZ * 16.0) / 256.0;
         var16 = (var11 + var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.minZ * 16.0) / 256.0;
         var18 = (var11 + var1.maxX * 16.0) / 256.0;
         var20 = var14;
         var22 = var12;
         var12 = var20;
         var14 = var22;
         var24 = var18;
         var26 = var16;
      } else if (this.field_31082_l == 3) {
         var12 = (var10 + 16 - var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.maxX * 16.0 - 0.01) / 256.0;
         var16 = (var11 + 16 - var1.minZ * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.maxZ * 16.0 - 0.01) / 256.0;
         var20 = var14;
         var22 = var12;
         var24 = var16;
         var26 = var18;
      }

      double var28 = var2 + var1.minX;
      double var30 = var2 + var1.maxX;
      double var32 = var4 + var1.minY;
      double var34 = var6 + var1.minZ;
      double var36 = var6 + var1.maxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.addVertexWithUV(var28, var32, var36, var22, var26);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.addVertexWithUV(var28, var32, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.addVertexWithUV(var30, var32, var34, var20, var24);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.addVertexWithUV(var30, var32, var36, var14, var18);
      } else {
         var9.addVertexWithUV(var28, var32, var36, var22, var26);
         var9.addVertexWithUV(var28, var32, var34, var12, var16);
         var9.addVertexWithUV(var30, var32, var34, var20, var24);
         var9.addVertexWithUV(var30, var32, var36, var14, var18);
      }
   }

   public void renderTopFace(Block var1, double var2, double var4, double var6, int var8) {
      Tessellator var9 = Tessellator.instance;
      if (this.overrideBlockTexture >= 0) {
         var8 = this.overrideBlockTexture;
      }

      int var10 = (var8 & 15) << 4;
      int var11 = var8 & 240;
      double var12 = (var10 + var1.minX * 16.0) / 256.0;
      double var14 = (var10 + var1.maxX * 16.0 - 0.01) / 256.0;
      double var16 = (var11 + var1.minZ * 16.0) / 256.0;
      double var18 = (var11 + var1.maxZ * 16.0 - 0.01) / 256.0;
      if (var1.minX < 0.0 || var1.maxX > 1.0) {
         var12 = (var10 + 0.0F) / 256.0F;
         var14 = (var10 + 15.99F) / 256.0F;
      }

      if (var1.minZ < 0.0 || var1.maxZ > 1.0) {
         var16 = (var11 + 0.0F) / 256.0F;
         var18 = (var11 + 15.99F) / 256.0F;
      }

      double var20 = var14;
      double var22 = var12;
      double var24 = var16;
      double var26 = var18;
      if (this.field_31083_k == 1) {
         var12 = (var10 + var1.minZ * 16.0) / 256.0;
         var16 = (var11 + 16 - var1.maxX * 16.0) / 256.0;
         var14 = (var10 + var1.maxZ * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.minX * 16.0) / 256.0;
         var24 = var16;
         var26 = var18;
         var20 = var12;
         var22 = var14;
         var16 = var18;
         var18 = var24;
      } else if (this.field_31083_k == 2) {
         var12 = (var10 + 16 - var1.maxZ * 16.0) / 256.0;
         var16 = (var11 + var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.minZ * 16.0) / 256.0;
         var18 = (var11 + var1.maxX * 16.0) / 256.0;
         var20 = var14;
         var22 = var12;
         var12 = var20;
         var14 = var22;
         var24 = var18;
         var26 = var16;
      } else if (this.field_31083_k == 3) {
         var12 = (var10 + 16 - var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.maxX * 16.0 - 0.01) / 256.0;
         var16 = (var11 + 16 - var1.minZ * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.maxZ * 16.0 - 0.01) / 256.0;
         var20 = var14;
         var22 = var12;
         var24 = var16;
         var26 = var18;
      }

      double var28 = var2 + var1.minX;
      double var30 = var2 + var1.maxX;
      double var32 = var4 + var1.maxY;
      double var34 = var6 + var1.minZ;
      double var36 = var6 + var1.maxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.addVertexWithUV(var30, var32, var36, var14, var18);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.addVertexWithUV(var30, var32, var34, var20, var24);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.addVertexWithUV(var28, var32, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.addVertexWithUV(var28, var32, var36, var22, var26);
      } else {
         var9.addVertexWithUV(var30, var32, var36, var14, var18);
         var9.addVertexWithUV(var30, var32, var34, var20, var24);
         var9.addVertexWithUV(var28, var32, var34, var12, var16);
         var9.addVertexWithUV(var28, var32, var36, var22, var26);
      }
   }

   public void renderEastFace(Block var1, double var2, double var4, double var6, int var8) {
      Tessellator var9 = Tessellator.instance;
      if (this.overrideBlockTexture >= 0) {
         var8 = this.overrideBlockTexture;
      }

      int var10 = (var8 & 15) << 4;
      int var11 = var8 & 240;
      double var12 = (var10 + var1.minX * 16.0) / 256.0;
      double var14 = (var10 + var1.maxX * 16.0 - 0.01) / 256.0;
      double var16 = (var11 + 16 - var1.maxY * 16.0) / 256.0;
      double var18 = (var11 + 16 - var1.minY * 16.0 - 0.01) / 256.0;
      if (this.flipTexture) {
         double var20 = var12;
         var12 = var14;
         var14 = var20;
      }

      if (var1.minX < 0.0 || var1.maxX > 1.0) {
         var12 = (var10 + 0.0F) / 256.0F;
         var14 = (var10 + 15.99F) / 256.0F;
      }

      if (var1.minY < 0.0 || var1.maxY > 1.0) {
         var16 = (var11 + 0.0F) / 256.0F;
         var18 = (var11 + 15.99F) / 256.0F;
      }

      double var42 = var14;
      double var22 = var12;
      double var24 = var16;
      double var26 = var18;
      if (this.field_31087_g == 2) {
         var12 = (var10 + var1.minY * 16.0) / 256.0;
         var16 = (var11 + 16 - var1.minX * 16.0) / 256.0;
         var14 = (var10 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.maxX * 16.0) / 256.0;
         var24 = var16;
         var26 = var18;
         var42 = var12;
         var22 = var14;
         var16 = var18;
         var18 = var24;
      } else if (this.field_31087_g == 1) {
         var12 = (var10 + 16 - var1.maxY * 16.0) / 256.0;
         var16 = (var11 + var1.maxX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.minY * 16.0) / 256.0;
         var18 = (var11 + var1.minX * 16.0) / 256.0;
         var42 = var14;
         var22 = var12;
         var12 = var42;
         var14 = var22;
         var24 = var18;
         var26 = var16;
      } else if (this.field_31087_g == 3) {
         var12 = (var10 + 16 - var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.maxX * 16.0 - 0.01) / 256.0;
         var16 = (var11 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + var1.minY * 16.0 - 0.01) / 256.0;
         var42 = var14;
         var22 = var12;
         var24 = var16;
         var26 = var18;
      }

      double var28 = var2 + var1.minX;
      double var30 = var2 + var1.maxX;
      double var32 = var4 + var1.minY;
      double var34 = var4 + var1.maxY;
      double var36 = var6 + var1.minZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.addVertexWithUV(var28, var34, var36, var42, var24);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.addVertexWithUV(var30, var34, var36, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.addVertexWithUV(var30, var32, var36, var22, var26);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.addVertexWithUV(var28, var32, var36, var14, var18);
      } else {
         var9.addVertexWithUV(var28, var34, var36, var42, var24);
         var9.addVertexWithUV(var30, var34, var36, var12, var16);
         var9.addVertexWithUV(var30, var32, var36, var22, var26);
         var9.addVertexWithUV(var28, var32, var36, var14, var18);
      }
   }

   public void renderWestFace(Block var1, double var2, double var4, double var6, int var8) {
      Tessellator var9 = Tessellator.instance;
      if (this.overrideBlockTexture >= 0) {
         var8 = this.overrideBlockTexture;
      }

      int var10 = (var8 & 15) << 4;
      int var11 = var8 & 240;
      double var12 = (var10 + var1.minX * 16.0) / 256.0;
      double var14 = (var10 + var1.maxX * 16.0 - 0.01) / 256.0;
      double var16 = (var11 + 16 - var1.maxY * 16.0) / 256.0;
      double var18 = (var11 + 16 - var1.minY * 16.0 - 0.01) / 256.0;
      if (this.flipTexture) {
         double var20 = var12;
         var12 = var14;
         var14 = var20;
      }

      if (var1.minX < 0.0 || var1.maxX > 1.0) {
         var12 = (var10 + 0.0F) / 256.0F;
         var14 = (var10 + 15.99F) / 256.0F;
      }

      if (var1.minY < 0.0 || var1.maxY > 1.0) {
         var16 = (var11 + 0.0F) / 256.0F;
         var18 = (var11 + 15.99F) / 256.0F;
      }

      double var42 = var14;
      double var22 = var12;
      double var24 = var16;
      double var26 = var18;
      if (this.field_31086_h == 1) {
         var12 = (var10 + var1.minY * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.minX * 16.0) / 256.0;
         var14 = (var10 + var1.maxY * 16.0) / 256.0;
         var16 = (var11 + 16 - var1.maxX * 16.0) / 256.0;
         var24 = var16;
         var26 = var18;
         var42 = var12;
         var22 = var14;
         var16 = var18;
         var18 = var24;
      } else if (this.field_31086_h == 2) {
         var12 = (var10 + 16 - var1.maxY * 16.0) / 256.0;
         var16 = (var11 + var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.minY * 16.0) / 256.0;
         var18 = (var11 + var1.maxX * 16.0) / 256.0;
         var42 = var14;
         var22 = var12;
         var12 = var42;
         var14 = var22;
         var24 = var18;
         var26 = var16;
      } else if (this.field_31086_h == 3) {
         var12 = (var10 + 16 - var1.minX * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.maxX * 16.0 - 0.01) / 256.0;
         var16 = (var11 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + var1.minY * 16.0 - 0.01) / 256.0;
         var42 = var14;
         var22 = var12;
         var24 = var16;
         var26 = var18;
      }

      double var28 = var2 + var1.minX;
      double var30 = var2 + var1.maxX;
      double var32 = var4 + var1.minY;
      double var34 = var4 + var1.maxY;
      double var36 = var6 + var1.maxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.addVertexWithUV(var28, var34, var36, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.addVertexWithUV(var28, var32, var36, var22, var26);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.addVertexWithUV(var30, var32, var36, var14, var18);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.addVertexWithUV(var30, var34, var36, var42, var24);
      } else {
         var9.addVertexWithUV(var28, var34, var36, var12, var16);
         var9.addVertexWithUV(var28, var32, var36, var22, var26);
         var9.addVertexWithUV(var30, var32, var36, var14, var18);
         var9.addVertexWithUV(var30, var34, var36, var42, var24);
      }
   }

   public void renderNorthFace(Block var1, double var2, double var4, double var6, int var8) {
      Tessellator var9 = Tessellator.instance;
      if (this.overrideBlockTexture >= 0) {
         var8 = this.overrideBlockTexture;
      }

      int var10 = (var8 & 15) << 4;
      int var11 = var8 & 240;
      double var12 = (var10 + var1.minZ * 16.0) / 256.0;
      double var14 = (var10 + var1.maxZ * 16.0 - 0.01) / 256.0;
      double var16 = (var11 + 16 - var1.maxY * 16.0) / 256.0;
      double var18 = (var11 + 16 - var1.minY * 16.0 - 0.01) / 256.0;
      if (this.flipTexture) {
         double var20 = var12;
         var12 = var14;
         var14 = var20;
      }

      if (var1.minZ < 0.0 || var1.maxZ > 1.0) {
         var12 = (var10 + 0.0F) / 256.0F;
         var14 = (var10 + 15.99F) / 256.0F;
      }

      if (var1.minY < 0.0 || var1.maxY > 1.0) {
         var16 = (var11 + 0.0F) / 256.0F;
         var18 = (var11 + 15.99F) / 256.0F;
      }

      double var42 = var14;
      double var22 = var12;
      double var24 = var16;
      double var26 = var18;
      if (this.field_31084_j == 1) {
         var12 = (var10 + var1.minY * 16.0) / 256.0;
         var16 = (var11 + 16 - var1.maxZ * 16.0) / 256.0;
         var14 = (var10 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.minZ * 16.0) / 256.0;
         var24 = var16;
         var26 = var18;
         var42 = var12;
         var22 = var14;
         var16 = var18;
         var18 = var24;
      } else if (this.field_31084_j == 2) {
         var12 = (var10 + 16 - var1.maxY * 16.0) / 256.0;
         var16 = (var11 + var1.minZ * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.minY * 16.0) / 256.0;
         var18 = (var11 + var1.maxZ * 16.0) / 256.0;
         var42 = var14;
         var22 = var12;
         var12 = var42;
         var14 = var22;
         var24 = var18;
         var26 = var16;
      } else if (this.field_31084_j == 3) {
         var12 = (var10 + 16 - var1.minZ * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.maxZ * 16.0 - 0.01) / 256.0;
         var16 = (var11 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + var1.minY * 16.0 - 0.01) / 256.0;
         var42 = var14;
         var22 = var12;
         var24 = var16;
         var26 = var18;
      }

      double var28 = var2 + var1.minX;
      double var30 = var4 + var1.minY;
      double var32 = var4 + var1.maxY;
      double var34 = var6 + var1.minZ;
      double var36 = var6 + var1.maxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.addVertexWithUV(var28, var32, var36, var42, var24);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.addVertexWithUV(var28, var32, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.addVertexWithUV(var28, var30, var34, var22, var26);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.addVertexWithUV(var28, var30, var36, var14, var18);
      } else {
         var9.addVertexWithUV(var28, var32, var36, var42, var24);
         var9.addVertexWithUV(var28, var32, var34, var12, var16);
         var9.addVertexWithUV(var28, var30, var34, var22, var26);
         var9.addVertexWithUV(var28, var30, var36, var14, var18);
      }
   }

   public void renderSouthFace(Block var1, double var2, double var4, double var6, int var8) {
      Tessellator var9 = Tessellator.instance;
      if (this.overrideBlockTexture >= 0) {
         var8 = this.overrideBlockTexture;
      }

      int var10 = (var8 & 15) << 4;
      int var11 = var8 & 240;
      double var12 = (var10 + var1.minZ * 16.0) / 256.0;
      double var14 = (var10 + var1.maxZ * 16.0 - 0.01) / 256.0;
      double var16 = (var11 + 16 - var1.maxY * 16.0) / 256.0;
      double var18 = (var11 + 16 - var1.minY * 16.0 - 0.01) / 256.0;
      if (this.flipTexture) {
         double var20 = var12;
         var12 = var14;
         var14 = var20;
      }

      if (var1.minZ < 0.0 || var1.maxZ > 1.0) {
         var12 = (var10 + 0.0F) / 256.0F;
         var14 = (var10 + 15.99F) / 256.0F;
      }

      if (var1.minY < 0.0 || var1.maxY > 1.0) {
         var16 = (var11 + 0.0F) / 256.0F;
         var18 = (var11 + 15.99F) / 256.0F;
      }

      double var42 = var14;
      double var22 = var12;
      double var24 = var16;
      double var26 = var18;
      if (this.field_31085_i == 2) {
         var12 = (var10 + var1.minY * 16.0) / 256.0;
         var16 = (var11 + 16 - var1.minZ * 16.0) / 256.0;
         var14 = (var10 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + 16 - var1.maxZ * 16.0) / 256.0;
         var24 = var16;
         var26 = var18;
         var42 = var12;
         var22 = var14;
         var16 = var18;
         var18 = var24;
      } else if (this.field_31085_i == 1) {
         var12 = (var10 + 16 - var1.maxY * 16.0) / 256.0;
         var16 = (var11 + var1.maxZ * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.minY * 16.0) / 256.0;
         var18 = (var11 + var1.minZ * 16.0) / 256.0;
         var42 = var14;
         var22 = var12;
         var12 = var42;
         var14 = var22;
         var24 = var18;
         var26 = var16;
      } else if (this.field_31085_i == 3) {
         var12 = (var10 + 16 - var1.minZ * 16.0) / 256.0;
         var14 = (var10 + 16 - var1.maxZ * 16.0 - 0.01) / 256.0;
         var16 = (var11 + var1.maxY * 16.0) / 256.0;
         var18 = (var11 + var1.minY * 16.0 - 0.01) / 256.0;
         var42 = var14;
         var22 = var12;
         var24 = var16;
         var26 = var18;
      }

      double var28 = var2 + var1.maxX;
      double var30 = var4 + var1.minY;
      double var32 = var4 + var1.maxY;
      double var34 = var6 + var1.minZ;
      double var36 = var6 + var1.maxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.addVertexWithUV(var28, var30, var36, var22, var26);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.addVertexWithUV(var28, var30, var34, var14, var18);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.addVertexWithUV(var28, var32, var34, var42, var24);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.addVertexWithUV(var28, var32, var36, var12, var16);
      } else {
         var9.addVertexWithUV(var28, var30, var36, var22, var26);
         var9.addVertexWithUV(var28, var30, var34, var14, var18);
         var9.addVertexWithUV(var28, var32, var34, var42, var24);
         var9.addVertexWithUV(var28, var32, var36, var12, var16);
      }
   }

   public void renderBlockOnInventory(Block var1, int var2, float var3) {
      Tessellator var4 = Tessellator.instance;
      if (this.field_31088_b) {
         int var5 = var1.getRenderColor(var2);
         float var6 = (var5 >> 16 & 0xFF) / 255.0F;
         float var7 = (var5 >> 8 & 0xFF) / 255.0F;
         float var8 = (var5 & 0xFF) / 255.0F;
         GL11.glColor4f(var6 * var3, var7 * var3, var8 * var3, 1.0F);
      }

      int var9 = var1.getRenderType();
      if (var9 != 0 && var9 != 16) {
         if (var9 == 1) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderCrossedSquares(var1, var2, -0.5, -0.5, -0.5);
            var4.draw();
         } else if (var9 == 13) {
            var1.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var10 = 0.0625F;
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBottomFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(0));
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 1.0F, 0.0F);
            this.renderTopFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(1));
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 0.0F, -1.0F);
            var4.setTranslationF(0.0F, 0.0F, var10);
            this.renderEastFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(2));
            var4.setTranslationF(0.0F, 0.0F, -var10);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 0.0F, 1.0F);
            var4.setTranslationF(0.0F, 0.0F, -var10);
            this.renderWestFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(3));
            var4.setTranslationF(0.0F, 0.0F, var10);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(-1.0F, 0.0F, 0.0F);
            var4.setTranslationF(var10, 0.0F, 0.0F);
            this.renderNorthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(4));
            var4.setTranslationF(-var10, 0.0F, 0.0F);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(1.0F, 0.0F, 0.0F);
            var4.setTranslationF(-var10, 0.0F, 0.0F);
            this.renderSouthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(5));
            var4.setTranslationF(var10, 0.0F, 0.0F);
            var4.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         } else if (var9 == 6) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.func_1245_b(var1, var2, -0.5, -0.5, -0.5);
            var4.draw();
         } else if (var9 == 2) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderTorchAtAngle(var1, -0.5, -0.5, -0.5, 0.0, 0.0);
            var4.draw();
         } else if (var9 == 10) {
            for (int var11 = 0; var11 < 2; var11++) {
               if (var11 == 0) {
                  var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
               }

               if (var11 == 1) {
                  var1.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderBottomFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(0));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderTopFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(1));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderEastFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderWestFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(3));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderNorthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(4));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderSouthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(5));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
         } else if (var9 == 11) {
            for (int var12 = 0; var12 < 4; var12++) {
               float var13 = 0.125F;
               if (var12 == 0) {
                  var1.setBlockBounds(0.5F - var13, 0.0F, 0.0F, 0.5F + var13, 1.0F, var13 * 2.0F);
               }

               if (var12 == 1) {
                  var1.setBlockBounds(0.5F - var13, 0.0F, 1.0F - var13 * 2.0F, 0.5F + var13, 1.0F, 1.0F);
               }

               var13 = 0.0625F;
               if (var12 == 2) {
                  var1.setBlockBounds(0.5F - var13, 1.0F - var13 * 3.0F, -var13 * 2.0F, 0.5F + var13, 1.0F - var13, 1.0F + var13 * 2.0F);
               }

               if (var12 == 3) {
                  var1.setBlockBounds(0.5F - var13, 0.5F - var13 * 3.0F, -var13 * 2.0F, 0.5F + var13, 0.5F - var13, 1.0F + var13 * 2.0F);
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderBottomFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(0));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderTopFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(1));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderEastFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderWestFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(3));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderNorthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(4));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderSouthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSide(5));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            var1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         } else if (Config.hasModLoader()) {
            Config.callVoid("ModLoader", "RenderInvBlock", new Object[]{this, var1, var2, var9});
         }
      } else {
         if (var9 == 16) {
            var2 = 1;
         }

         var1.setBlockBoundsForItemRender();
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         var4.startDrawingQuads();
         var4.setNormal(0.0F, -1.0F, 0.0F);
         this.renderBottomFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSideAndMetadata(0, var2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(0.0F, 1.0F, 0.0F);
         this.renderTopFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSideAndMetadata(1, var2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(0.0F, 0.0F, -1.0F);
         this.renderEastFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSideAndMetadata(2, var2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(0.0F, 0.0F, 1.0F);
         this.renderWestFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSideAndMetadata(3, var2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(-1.0F, 0.0F, 0.0F);
         this.renderNorthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSideAndMetadata(4, var2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(1.0F, 0.0F, 0.0F);
         this.renderSouthFace(var1, 0.0, 0.0, 0.0, var1.getBlockTextureFromSideAndMetadata(5, var2));
         var4.draw();
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      }
   }

   public static boolean renderItemIn3d(int var0) {
      if (var0 == 0) {
         return true;
      } else if (var0 == 13) {
         return true;
      } else if (var0 == 10) {
         return true;
      } else if (var0 == 11) {
         return true;
      } else if (var0 == 16) {
         return true;
      } else {
         return Config.hasModLoader() ? Config.callBoolean("ModLoader", "RenderBlockIsItemFull3D", new Object[]{var0}) : false;
      }
   }

   static {
      for (int var0 = 0; var0 < redstoneColors.length; var0++) {
         float var1 = var0 / 15.0F;
         float var2 = var1 * 0.6F + 0.4F;
         if (var0 == 0) {
            var1 = 0.0F;
         }

         float var3 = var1 * var1 * 0.7F - 0.5F;
         float var4 = var1 * var1 * 0.6F - 0.7F;
         if (var3 < 0.0F) {
            var3 = 0.0F;
         }

         if (var4 < 0.0F) {
            var4 = 0.0F;
         }

         redstoneColors[var0] = new float[]{var2, var3, var4};
      }
   }
}
