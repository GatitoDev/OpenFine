package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class WorldRenderer {
   public World worldObj;
   private int glRenderList = -1;
   private static Tessellator tessellator = Tessellator.instance;
   public static int chunksUpdated = 0;
   public int posX;
   public int posY;
   public int posZ;
   public int sizeWidth;
   public int sizeHeight;
   public int sizeDepth;
   public int posXMinus;
   public int posYMinus;
   public int posZMinus;
   public int posXClip;
   public int posYClip;
   public int posZClip;
   public boolean isInFrustum = false;
   public boolean[] skipRenderPass = new boolean[2];
   public int posXPlus;
   public int posYPlus;
   public int posZPlus;
   public float rendererRadius;
   public boolean needsUpdate;
   public AxisAlignedBB rendererBoundingBox;
   public int chunkIndex;
   public boolean isVisible = true;
   public boolean isWaitingOnOcclusionQuery;
   public int glOcclusionQuery;
   public boolean isChunkLit;
   private boolean isInitialized = false;
   public List tileEntityRenderers = new ArrayList();
   private List tileEntities;
   public boolean isVisibleFromPosition = false;
   public double visibleFromX;
   public double visibleFromY;
   public double visibleFromZ;
   private boolean needsBoxUpdate = false;
   public boolean isInFrustrumFully = false;
   public boolean isUpdating = false;
   private WrUpdateState updateState = new WrUpdateState();
   public int activeSet = 0;
   public int[] activeListIndex = new int[]{0, 0};
   public int[][][] glWorkLists = new int[2][2][16];
   public boolean[] tempSkipRenderPass = new boolean[2];
   static double chunkOffsetX = 0.0;
   static double chunkOffsetZ = 0.0;

   public WorldRenderer(World var1, List var2, int var3, int var4, int var5, int var6, int var7) {
      this.worldObj = var1;
      this.tileEntities = var2;
      this.sizeWidth = this.sizeHeight = this.sizeDepth = var6;
      this.rendererRadius = MathHelper.sqrt_float(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth) / 2.0F;
      this.glRenderList = var7;
      int var8 = 393216 + 64 * (this.glRenderList / 3);

      for (int var9 = 0; var9 < 2; var9++) {
         int var10 = var8 + var9 * 2 * 16;

         for (int var11 = 0; var11 < 2; var11++) {
            int var12 = var10 + var11 * 16;

            for (int var13 = 0; var13 < 16; var13++) {
               this.glWorkLists[var9][var11][var13] = var12 + var13;
            }
         }
      }

      this.posX = -999;
      this.setPosition(var3, var4, var5);
      this.needsUpdate = false;
   }

   public void setPosition(int var1, int var2, int var3) {
      if (var1 != this.posX || var2 != this.posY || var3 != this.posZ) {
         if (this.isUpdating) {
            this.updateRenderer();
         }

         this.setDontDraw();
         this.posX = var1;
         this.posY = var2;
         this.posZ = var3;
         this.posXPlus = var1 + this.sizeWidth / 2;
         this.posYPlus = var2 + this.sizeHeight / 2;
         this.posZPlus = var3 + this.sizeDepth / 2;
         this.posXClip = var1 & 1023;
         this.posYClip = var2;
         this.posZClip = var3 & 1023;
         this.posXMinus = var1 - this.posXClip;
         this.posYMinus = var2 - this.posYClip;
         this.posZMinus = var3 - this.posZClip;
         float var4 = 0.0F;
         this.rendererBoundingBox = AxisAlignedBB.getBoundingBox(var1 - var4, var2 - var4, var3 - var4, var1 + this.sizeWidth + var4, var2 + this.sizeHeight + var4, var3 + this.sizeDepth + var4);
         this.needsBoxUpdate = true;
         this.markDirty();
         this.isVisibleFromPosition = false;
      }
   }

   private void setupGLTranslation() {
      GL11.glTranslatef(this.posXClip, this.posYClip, this.posZClip);
   }

   public void updateRenderer() {
      if (this.worldObj != null) {
         this.updateRenderer(0L);
         this.finishUpdate();
      }
   }

   public void finishUpdate() {
      for (int var1 = 0; var1 < 2; var1++) {
         if (!this.skipRenderPass[var1]) {
            GL11.glNewList(this.glRenderList + var1, 4864);

            for (int var2 = 0; var2 <= this.activeListIndex[var1]; var2++) {
               int var3 = this.glWorkLists[this.activeSet][var1][var2];
               GL11.glCallList(var3);
            }

            GL11.glEndList();
         }
      }

      if (this.activeSet == 0) {
         this.activeSet = 1;
      } else {
         this.activeSet = 0;
      }

      for (int var4 = 0; var4 < 2; var4++) {
         if (!this.skipRenderPass[var4]) {
            for (int var6 = 0; var6 <= this.activeListIndex[var4]; var6++) {
               int var7 = this.glWorkLists[this.activeSet][var4][var6];
               GL11.glNewList(var7, 4864);
               GL11.glEndList();
            }
         }
      }

      for (int var5 = 0; var5 < 2; var5++) {
         this.activeListIndex[var5] = 0;
      }
   }

   public boolean updateRenderer(long var1) {
      if (this.worldObj == null) {
         return true;
      }

      this.needsUpdate = false;
      if (!this.isUpdating) {
         if (this.needsBoxUpdate) {
            float var3 = 0.0F;
            GL11.glNewList(this.glRenderList + 2, 4864);
            RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool(this.posXClip - var3, this.posYClip - var3, this.posZClip - var3, this.posXClip + this.sizeWidth + var3, this.posYClip + this.sizeHeight + var3, this.posZClip + this.sizeDepth + var3));
            GL11.glEndList();
            this.needsBoxUpdate = false;
         }

         Object var22 = Config.getFieldValue("LightCache", "cache");
         if (var22 != null) {
            Config.callVoid(var22, "clear", new Object[0]);
            Config.callVoid("BlockCoord", "resetPool", new Object[0]);
         }

         Chunk.isLit = false;
      }

      int var23 = this.posX;
      int var4 = this.posY;
      int var5 = this.posZ;
      int var6 = this.posX + this.sizeWidth;
      int var7 = this.posY + this.sizeHeight;
      int var8 = this.posZ + this.sizeDepth;
      ChunkCache var9 = null;
      RenderBlocks var10 = null;
      HashSet var11 = null;
      if (!this.isUpdating) {
         for (int var12 = 0; var12 < 2; var12++) {
            this.tempSkipRenderPass[var12] = true;
         }

         byte var13 = 1;
         var9 = new ChunkCache(this.worldObj, var23 - var13, var4 - var13, var5 - var13, var6 + var13, var7 + var13, var8 + var13);
         var10 = new RenderBlocks(var9);
         var11 = new HashSet();
         var11.addAll(this.tileEntityRenderers);
         this.tileEntityRenderers.clear();
      }

      for (int var24 = 0; var24 < 2; var24++) {
         boolean var25 = false;
         boolean var14 = false;
         boolean var15 = false;

         for (int var16 = var4; var16 < var7; var16++) {
            if (this.isUpdating) {
               this.isUpdating = false;
               var9 = this.updateState.chunkcache;
               var10 = this.updateState.renderblocks;
               var11 = this.updateState.setOldEntityRenders;
               var24 = this.updateState.renderPass;
               var16 = this.updateState.y;
               var25 = this.updateState.flag;
               var14 = this.updateState.hasRenderedBlocks;
               var15 = this.updateState.hasGlList;
               if (var15) {
                  GL11.glNewList(this.glWorkLists[this.activeSet][var24][this.activeListIndex[var24]], 4864);
                  tessellator.setRenderingChunk(true);
                  tessellator.startDrawingQuads();
                  tessellator.setTranslationD(-chunkOffsetX, 0.0, -chunkOffsetZ);
               }
            } else if (var15 && var1 != 0L && System.nanoTime() - var1 > 0L && this.activeListIndex[var24] < 15) {
               tessellator.draw();
               GL11.glEndList();
               tessellator.setRenderingChunk(false);
               tessellator.setTranslationD(0.0, 0.0, 0.0);
               this.activeListIndex[var24]++;
               this.updateState.chunkcache = var9;
               this.updateState.renderblocks = var10;
               this.updateState.setOldEntityRenders = var11;
               this.updateState.renderPass = var24;
               this.updateState.y = var16;
               this.updateState.flag = var25;
               this.updateState.hasRenderedBlocks = var14;
               this.updateState.hasGlList = var15;
               this.isUpdating = true;
               return false;
            }

            for (int var17 = var5; var17 < var8; var17++) {
               for (int var18 = var23; var18 < var6; var18++) {
                  int var19 = var9.getBlockId(var18, var16, var17);
                  if (var19 > 0) {
                     if (!var15) {
                        var15 = true;
                        GL11.glNewList(this.glWorkLists[this.activeSet][var24][this.activeListIndex[var24]], 4864);
                        tessellator.setRenderingChunk(true);
                        tessellator.startDrawingQuads();
                        tessellator.setTranslationD(-chunkOffsetX, 0.0, -chunkOffsetZ);
                     }

                     if (var24 == 0 && Block.isBlockContainer[var19]) {
                        TileEntity var20 = var9.getBlockTileEntity(var18, var16, var17);
                        if (TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
                           this.tileEntityRenderers.add(var20);
                        }
                     }

                     Block var27 = Block.blocksList[var19];
                     int var21 = var27.getRenderBlockPass();
                     if (var21 != var24) {
                        var25 = true;
                     } else if (var21 == var24) {
                        var14 |= var10.renderBlockByRenderType(var27, var18, var16, var17);
                     }
                  }
               }
            }
         }

         if (var15) {
            tessellator.draw();
            GL11.glEndList();
            tessellator.setRenderingChunk(false);
            tessellator.setTranslationD(0.0, 0.0, 0.0);
         } else {
            var14 = false;
         }

         if (var14) {
            this.tempSkipRenderPass[var24] = false;
         }

         if (!var25) {
            break;
         }
      }

      HashSet var26 = new HashSet();
      var26.addAll(this.tileEntityRenderers);
      var26.removeAll(var11);
      this.tileEntities.addAll(var26);
      var11.removeAll(this.tileEntityRenderers);
      this.tileEntities.removeAll(var11);
      this.isChunkLit = Chunk.isLit;
      this.isInitialized = true;
      chunksUpdated++;
      this.isVisible = true;
      this.isVisibleFromPosition = false;
      this.skipRenderPass[0] = this.tempSkipRenderPass[0];
      this.skipRenderPass[1] = this.tempSkipRenderPass[1];
      this.isUpdating = false;
      return true;
   }

   public float distanceToEntitySquared(Entity var1) {
      float var2 = (float)(var1.posX - this.posXPlus);
      float var3 = (float)(var1.posY - this.posYPlus);
      float var4 = (float)(var1.posZ - this.posZPlus);
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   public void setDontDraw() {
      for (int var1 = 0; var1 < 2; var1++) {
         this.skipRenderPass[var1] = true;
      }

      this.isInFrustum = false;
      this.isInitialized = false;
   }

   public void func_1204_c() {
      this.setDontDraw();
      this.worldObj = null;
   }

   public int getGLCallListForPass(int var1) {
      return !this.isInFrustum ? -1 : (!this.skipRenderPass[var1] ? this.glRenderList + var1 : -1);
   }

   public void updateInFrustrum(ICamera var1) {
      this.isInFrustum = var1.isBoundingBoxInFrustum(this.rendererBoundingBox);
      if (this.isInFrustum && Config.isOcclusionEnabled() && Config.isOcclusionFancy()) {
         this.isInFrustrumFully = var1.isBoundingBoxInFrustumFully(this.rendererBoundingBox);
      } else {
         this.isInFrustrumFully = false;
      }
   }

   public void callOcclusionQueryList() {
      GL11.glCallList(this.glRenderList + 2);
   }

   public boolean skipAllRenderPasses() {
      return !this.isInitialized ? false : this.skipRenderPass[0] && this.skipRenderPass[1];
   }

   public void markDirty() {
      this.needsUpdate = true;
   }
}