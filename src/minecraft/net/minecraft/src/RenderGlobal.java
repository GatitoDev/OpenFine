package net.minecraft.src;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

public class RenderGlobal implements IWorldAccess {
   private long lastMovedTime = System.currentTimeMillis();
   public List tileEntities;
   private World worldObj;
   private RenderEngine renderEngine;
   private List worldRenderersToUpdate;
   private WorldRenderer[] sortedWorldRenderers;
   private WorldRenderer[] worldRenderers;
   private int renderChunksWide;
   private int renderChunksTall;
   private int renderChunksDeep;
   private int glRenderListBase;
   private Minecraft mc;
   private RenderBlocks globalRenderBlocks;
   private IntBuffer glOcclusionQueryBase;
   private boolean occlusionEnabled;
   private int cloudOffsetX;
   private int starGLCallList;
   private int glSkyList;
   private int glSkyList2;
   private int minBlockX;
   private int minBlockY;
   private int minBlockZ;
   private int maxBlockX;
   private int maxBlockY;
   private int maxBlockZ;
   private int renderDistance;
   private int renderEntitiesStartupCounter;
   private int countEntitiesTotal;
   private int countEntitiesRendered;
   private int countEntitiesHidden;
   int[] dummyBuf50k;
   IntBuffer occlusionResult;
   private int renderersLoaded;
   private int renderersBeingClipped;
   private int renderersBeingOccluded;
   private int renderersBeingRendered;
   private int renderersSkippingRenderPass;
   private int worldRenderersCheckIndex;
   private IntBuffer field_22019_aY;
   int dummyInt0;
   int glDummyList;
   double prevSortX;
   double prevSortY;
   double prevSortZ;
   public float damagePartialTime;
   int frustrumCheckOffset;
   double prevReposX;
   double prevReposY;
   double prevReposZ;
   private long lastUpdateStartTimeNs = 0L;
   private long updateStartTimeNs = 0L;
   private long updateTimeNs = 10000000L;
   private WorldRenderer currentUpdateRenderer = null;
   private int renderersUpdated = 0;
   private int renderersFound = 0;

   public RenderGlobal(Minecraft var1, RenderEngine var2) {
      this.tileEntities = new ArrayList();
      this.worldRenderersToUpdate = new ArrayList();
      this.occlusionEnabled = false;
      this.cloudOffsetX = 0;
      this.renderDistance = -1;
      this.renderEntitiesStartupCounter = 2;
      this.dummyBuf50k = new int[50000];
      this.occlusionResult = GLAllocation.createDirectIntBuffer(64);
      this.field_22019_aY = BufferUtils.createIntBuffer(65536);
      this.dummyInt0 = 0;
      this.glDummyList = GLAllocation.generateDisplayLists(1);
      this.prevSortX = -9999.0;
      this.prevSortY = -9999.0;
      this.prevSortZ = -9999.0;
      this.frustrumCheckOffset = 0;
      this.mc = var1;
      this.renderEngine = var2;
      byte var3 = 64;
      this.glRenderListBase = GLAllocation.generateDisplayLists(var3 * var3 * var3 * 3);
      this.occlusionEnabled = var1.getOpenGlCapsChecker().checkARBOcclusion();
      if (this.occlusionEnabled) {
         ((Buffer)this.occlusionResult).clear();
         this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(var3 * var3 * var3);
         ((Buffer)this.glOcclusionQueryBase).clear();
         ((Buffer)this.glOcclusionQueryBase).position(0);
         ((Buffer)this.glOcclusionQueryBase).limit(var3 * var3 * var3);
         ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
      }

      this.starGLCallList = GLAllocation.generateDisplayLists(3);
      GL11.glPushMatrix();
      GL11.glNewList(this.starGLCallList, 4864);
      this.renderStars();
      GL11.glEndList();
      GL11.glPopMatrix();
      Tessellator var4 = Tessellator.instance;
      this.glSkyList = this.starGLCallList + 1;
      GL11.glNewList(this.glSkyList, 4864);
      byte var5 = 64;
      int var6 = 256 / var5 + 2;
      float var7 = 16.0F;

      for (int var8 = -var5 * var6; var8 <= var5 * var6; var8 += var5) {
         for (int var9 = -var5 * var6; var9 <= var5 * var6; var9 += var5) {
            var4.startDrawingQuads();
            var4.addVertex(var8 + 0, var7, var9 + 0);
            var4.addVertex(var8 + var5, var7, var9 + 0);
            var4.addVertex(var8 + var5, var7, var9 + var5);
            var4.addVertex(var8 + 0, var7, var9 + var5);
            var4.draw();
         }
      }

      GL11.glEndList();
      this.glSkyList2 = this.starGLCallList + 2;
      GL11.glNewList(this.glSkyList2, 4864);
      var7 = -16.0F;
      var4.startDrawingQuads();

      for (int var11 = -var5 * var6; var11 <= var5 * var6; var11 += var5) {
         for (int var12 = -var5 * var6; var12 <= var5 * var6; var12 += var5) {
            var4.addVertex(var11 + var5, var7, var12 + 0);
            var4.addVertex(var11 + 0, var7, var12 + 0);
            var4.addVertex(var11 + 0, var7, var12 + var5);
            var4.addVertex(var11 + var5, var7, var12 + var5);
         }
      }

      var4.draw();
      GL11.glEndList();
   }

   private void renderStars() {
      Random var1 = new Random(10842L);
      Tessellator var2 = Tessellator.instance;
      var2.startDrawingQuads();

      for (int var3 = 0; var3 < 1500; var3++) {
         double var4 = var1.nextFloat() * 2.0F - 1.0F;
         double var6 = var1.nextFloat() * 2.0F - 1.0F;
         double var8 = var1.nextFloat() * 2.0F - 1.0F;
         double var10 = 0.25F + var1.nextFloat() * 0.25F;
         double var12 = var4 * var4 + var6 * var6 + var8 * var8;
         if (!(var12 >= 1.0) && !(var12 <= 0.01)) {
            var12 = 1.0 / Math.sqrt(var12);
            var4 *= var12;
            var6 *= var12;
            var8 *= var12;
            double var14 = var4 * 100.0;
            double var16 = var6 * 100.0;
            double var18 = var8 * 100.0;
            double var20 = Math.atan2(var4, var8);
            double var22 = Math.sin(var20);
            double var24 = Math.cos(var20);
            double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
            double var28 = Math.sin(var26);
            double var30 = Math.cos(var26);
            double var32 = var1.nextDouble() * Math.PI * 2.0;
            double var34 = Math.sin(var32);
            double var36 = Math.cos(var32);

            for (int var38 = 0; var38 < 4; var38++) {
               double var39 = 0.0;
               double var41 = ((var38 & 2) - 1) * var10;
               double var43 = ((var38 + 1 & 2) - 1) * var10;
               double var45 = var41 * var36 - var43 * var34;
               double var47 = var43 * var36 + var41 * var34;
               double var49 = var45 * var28 + var39 * var30;
               double var51 = var39 * var28 - var45 * var30;
               double var53 = var51 * var22 - var47 * var24;
               double var55 = var47 * var22 + var51 * var24;
               var2.addVertex(var14 + var53, var16 + var49, var18 + var55);
            }
         }
      }

      var2.draw();
   }

   public void changeWorld(World var1) {
      if (this.worldObj != null) {
         this.worldObj.removeWorldAccess(this);
      }

      this.prevSortX = -9999.0;
      this.prevSortY = -9999.0;
      this.prevSortZ = -9999.0;
      RenderManager.instance.func_852_a(var1);
      this.worldObj = var1;
      this.globalRenderBlocks = new RenderBlocks(var1);
      if (var1 != null) {
         var1.addWorldAccess(this);
         this.loadRenderers();
      }
   }

   public void loadRenderers() {
      Block.leaves.setGraphicsLevel(Config.isTreesFancy());
      this.renderDistance = this.mc.gameSettings.renderDistance;
      if (this.worldRenderers != null) {
         for (int var1 = 0; var1 < this.worldRenderers.length; var1++) {
            this.worldRenderers[var1].func_1204_c();
         }
      }

      int var8 = 64 << 3 - this.renderDistance;
      if (Config.isLoadChunksFar()) {
         var8 = 512;
      }

      if (Config.isFarView()) {
         if (var8 < 512) {
            var8 *= 3;
         } else {
            var8 *= 2;
         }
      }

      var8 += Config.getPreloadedChunks() * 2 * 16;
      if (!Config.isFarView() && var8 > 400) {
         var8 = 400;
      }

      this.prevReposX = -9999.0;
      this.prevReposY = -9999.0;
      this.prevReposZ = -9999.0;
      this.renderChunksWide = var8 / 16 + 1;
      this.renderChunksTall = 8;
      this.renderChunksDeep = var8 / 16 + 1;
      this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
      this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
      int var2 = 0;
      int var3 = 0;
      this.minBlockX = 0;
      this.minBlockY = 0;
      this.minBlockZ = 0;
      this.maxBlockX = this.renderChunksWide;
      this.maxBlockY = this.renderChunksTall;
      this.maxBlockZ = this.renderChunksDeep;

      for (int var4 = 0; var4 < this.worldRenderersToUpdate.size(); var4++) {
         WorldRenderer var5 = (WorldRenderer)this.worldRenderersToUpdate.get(var4);
         if (var5 != null) {
            var5.needsUpdate = false;
         }
      }

      this.worldRenderersToUpdate.clear();
      this.tileEntities.clear();

      for (int var10 = 0; var10 < this.renderChunksWide; var10++) {
         for (int var12 = 0; var12 < this.renderChunksTall; var12++) {
            for (int var6 = 0; var6 < this.renderChunksDeep; var6++) {
               int var7 = (var6 * this.renderChunksTall + var12) * this.renderChunksWide + var10;
               this.worldRenderers[var7] = new WorldRenderer(this.worldObj, this.tileEntities, var10 * 16, var12 * 16, var6 * 16, 16, this.glRenderListBase + var2);
               if (this.occlusionEnabled) {
                  this.worldRenderers[var7].glOcclusionQuery = this.glOcclusionQueryBase.get(var3);
               }

               this.worldRenderers[var7].isWaitingOnOcclusionQuery = false;
               this.worldRenderers[var7].isVisible = true;
               this.worldRenderers[var7].isInFrustum = false;
               this.worldRenderers[var7].chunkIndex = var3++;
               this.worldRenderers[var7].markDirty();
               this.sortedWorldRenderers[var7] = this.worldRenderers[var7];
               this.worldRenderersToUpdate.add(this.worldRenderers[var7]);
               var2 += 3;
            }
         }
      }

      if (this.worldObj != null) {
         EntityLiving var11 = this.mc.renderViewEntity;
         if (var11 == null) {
            var11 = this.mc.thePlayer;
         }

         if (var11 != null) {
            this.markRenderersForNewPosition(MathHelper.floor_double(var11.posX), MathHelper.floor_double(var11.posY), MathHelper.floor_double(var11.posZ));
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var11));
         }
      }

      this.renderEntitiesStartupCounter = 2;
   }

   public void renderEntities(Vec3D var1, ICamera var2, float var3) {
      if (this.renderEntitiesStartupCounter > 0) {
         this.renderEntitiesStartupCounter--;
      } else {
         TileEntityRenderer.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, var3);
         RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.gameSettings, var3);
         this.countEntitiesTotal = 0;
         this.countEntitiesRendered = 0;
         this.countEntitiesHidden = 0;
         EntityLiving var4 = this.mc.renderViewEntity;
         RenderManager.renderPosX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * var3;
         RenderManager.renderPosY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * var3;
         RenderManager.renderPosZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * var3;
         TileEntityRenderer.staticPlayerX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * var3;
         TileEntityRenderer.staticPlayerY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * var3;
         TileEntityRenderer.staticPlayerZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * var3;
         List var5 = this.worldObj.getLoadedEntityList();
         this.countEntitiesTotal = var5.size();

         for (int var6 = 0; var6 < this.worldObj.weatherEffects.size(); var6++) {
            Entity var7 = (Entity)this.worldObj.weatherEffects.get(var6);
            this.countEntitiesRendered++;
            if (var7.isInRangeToRenderVec3D(var1)) {
               RenderManager.instance.renderEntity(var7, var3);
            }
         }

         for (int var9 = 0; var9 < var5.size(); var9++) {
            Entity var11 = (Entity)var5.get(var9);
            if (var11.isInRangeToRenderVec3D(var1) && (var11.ignoreFrustumCheck || var2.isBoundingBoxInFrustum(var11.boundingBox)) && (var11 != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView || this.mc.renderViewEntity.isPlayerSleeping())) {
               int var8 = MathHelper.floor_double(var11.posY);
               if (var8 < 0) {
                  var8 = 0;
               }

               if (var8 >= 128) {
                  var8 = 127;
               }

               if (this.worldObj.blockExists(MathHelper.floor_double(var11.posX), var8, MathHelper.floor_double(var11.posZ))) {
                  this.countEntitiesRendered++;
                  RenderManager.instance.renderEntity(var11, var3);
               }
            }
         }

         for (int var10 = 0; var10 < this.tileEntities.size(); var10++) {
            TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(var10), var3);
         }
      }
   }

   public String getDebugInfoRenders() {
      return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
   }

   public String getDebugInfoEntities() {
      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
   }

   private void markRenderersForNewPosition(int var1, int var2, int var3) {
      var1 -= 8;
      var2 -= 8;
      var3 -= 8;
      this.minBlockX = Integer.MAX_VALUE;
      this.minBlockY = Integer.MAX_VALUE;
      this.minBlockZ = Integer.MAX_VALUE;
      this.maxBlockX = Integer.MIN_VALUE;
      this.maxBlockY = Integer.MIN_VALUE;
      this.maxBlockZ = Integer.MIN_VALUE;
      int var4 = this.renderChunksWide * 16;
      int var5 = var4 / 2;

      for (int var6 = 0; var6 < this.renderChunksWide; var6++) {
         int var7 = var6 * 16;
         int var8 = var7 + var5 - var1;
         if (var8 < 0) {
            var8 -= var4 - 1;
         }

         var8 /= var4;
         var7 -= var8 * var4;
         if (var7 < this.minBlockX) {
            this.minBlockX = var7;
         }

         if (var7 > this.maxBlockX) {
            this.maxBlockX = var7;
         }

         for (int var9 = 0; var9 < this.renderChunksDeep; var9++) {
            int var10 = var9 * 16;
            int var11 = var10 + var5 - var3;
            if (var11 < 0) {
               var11 -= var4 - 1;
            }

            var11 /= var4;
            var10 -= var11 * var4;
            if (var10 < this.minBlockZ) {
               this.minBlockZ = var10;
            }

            if (var10 > this.maxBlockZ) {
               this.maxBlockZ = var10;
            }

            for (int var12 = 0; var12 < this.renderChunksTall; var12++) {
               int var13 = var12 * 16;
               if (var13 < this.minBlockY) {
                  this.minBlockY = var13;
               }

               if (var13 > this.maxBlockY) {
                  this.maxBlockY = var13;
               }

               WorldRenderer var14 = this.worldRenderers[(var9 * this.renderChunksTall + var12) * this.renderChunksWide + var6];
               boolean var15 = var14.needsUpdate;
               var14.setPosition(var7, var13, var10);
               if (!var15 && var14.needsUpdate) {
                  this.worldRenderersToUpdate.add(var14);
               }
            }
         }
      }
   }

   public int sortAndRender(EntityLiving var1, int var2, double var3) {
      if (this.worldRenderersToUpdate.size() < 10) {
         int var5 = 10;

         for (int var6 = 0; var6 < var5; var6++) {
            this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
            WorldRenderer var7 = this.worldRenderers[this.worldRenderersCheckIndex];
            if (var7.needsUpdate && !this.worldRenderersToUpdate.contains(var7)) {
               this.worldRenderersToUpdate.add(var7);
            }
         }
      }

      if (this.mc.gameSettings.renderDistance != this.renderDistance && !Config.isLoadChunksFar()) {
         this.loadRenderers();
      }

      if (var2 == 0) {
         this.renderersLoaded = 0;
         this.renderersBeingClipped = 0;
         this.renderersBeingOccluded = 0;
         this.renderersBeingRendered = 0;
         this.renderersSkippingRenderPass = 0;
      }

      double var39 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var3;
      double var40 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var3;
      double var9 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var3;
      double var11 = var1.posX - this.prevSortX;
      double var13 = var1.posY - this.prevSortY;
      double var15 = var1.posZ - this.prevSortZ;
      double var17 = var11 * var11 + var13 * var13 + var15 * var15;
      if (var17 > 16.0) {
         this.prevSortX = var1.posX;
         this.prevSortY = var1.posY;
         this.prevSortZ = var1.posZ;
         int var19 = Config.getPreloadedChunks() * 16;
         double var20 = var1.posX - this.prevReposX;
         double var22 = var1.posY - this.prevReposY;
         double var24 = var1.posZ - this.prevReposZ;
         double var26 = var20 * var20 + var22 * var22 + var24 * var24;
         if (var26 > var19 * var19 + 16.0) {
            this.prevReposX = var1.posX;
            this.prevReposY = var1.posY;
            this.prevReposZ = var1.posZ;
            this.markRenderersForNewPosition(MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ));
         }

         Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var1));
      }

      if (this.mc.gameSettings.ofSmoothFps && var2 == 0) {
         GL11.glFinish();
      }

      if (this.mc.gameSettings.ofSmoothInput && var2 == 0) {
         Config.sleep(1L);
      }

      double var47 = var1.posX;
      double var48 = var1.posZ;
      if (Math.abs(var47 - WorldRenderer.chunkOffsetX) > 2000.0 || Math.abs(var48 - WorldRenderer.chunkOffsetZ) > 2000.0) {
         WorldRenderer.chunkOffsetX = var47;
         WorldRenderer.chunkOffsetZ = var48;
         this.loadRenderers();
      }

      int var41 = 0;
      int var43 = 0;
      if (this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && var2 == 0) {
         int var21 = 0;
         int var44 = 20;
         this.checkOcclusionQueryResult(var21, var44, var1.posX, var1.posY, var1.posZ);

         for (int var23 = var21; var23 < var44; var23++) {
            this.sortedWorldRenderers[var23].isVisible = true;
         }

         var41 += this.renderSortedRenderers(var21, var44, var2, var3);
         int var45 = var44;
         int var46 = 0;
         int var25 = 30;
         int var47Chunks = this.renderChunksWide / 2;

         while (var45 < this.sortedWorldRenderers.length) {
            int var27 = var45;
            if (var46 < var47Chunks) {
               var46++;
            } else {
               var46--;
            }

            var45 = var27 + var46 * var25;
            if (var45 <= var27) {
               var45 = var27 + 10;
            }

            if (var45 > this.sortedWorldRenderers.length) {
               var45 = this.sortedWorldRenderers.length;
            }

            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(3008);
            GL11.glDisable(2912);
            GL11.glColorMask(false, false, false, false);
            GL11.glDepthMask(false);
            this.checkOcclusionQueryResult(var27, var45, var1.posX, var1.posY, var1.posZ);
            GL11.glPushMatrix();
            float var28 = 0.0F;
            float var29 = 0.0F;
            float var30 = 0.0F;

            for (int var31 = var27; var31 < var45; var31++) {
               WorldRenderer var32 = this.sortedWorldRenderers[var31];
               if (var32.skipAllRenderPasses()) {
                  var32.isInFrustum = false;
               } else if (var32.isUpdating) {
                  var32.isVisible = true;
               } else if (var32.isInFrustum) {
                  if (Config.isOcclusionFancy() && !var32.isInFrustrumFully) {
                     var32.isVisible = true;
                  } else if (var32.isInFrustum && !var32.isWaitingOnOcclusionQuery) {
                     if (var32.isVisibleFromPosition) {
                        float var33 = Math.abs((float)(var32.visibleFromX - var1.posX));
                        float var34 = Math.abs((float)(var32.visibleFromY - var1.posY));
                        float var35 = Math.abs((float)(var32.visibleFromZ - var1.posZ));
                        float var36 = var33 + var34 + var35;
                        if (var36 < 10.0 + var31 / 1000.0) {
                           var32.isVisible = true;
                           continue;
                        }

                        var32.isVisibleFromPosition = false;
                     }

                     float var48Val = (float)(var32.posXMinus - var39);
                     float var49 = (float)(var32.posYMinus - var40);
                     float var50 = (float)(var32.posZMinus - var9);
                     float var51 = var48Val - var28;
                     float var37 = var49 - var29;
                     float var38 = var50 - var30;
                     if (var51 != 0.0F || var37 != 0.0F || var38 != 0.0F) {
                        GL11.glTranslatef(var51, var37, var38);
                        var28 += var51;
                        var29 += var37;
                        var30 += var38;
                     }

                     ARBOcclusionQuery.glBeginQueryARB(35092, var32.glOcclusionQuery);
                     var32.callOcclusionQueryList();
                     ARBOcclusionQuery.glEndQueryARB(35092);
                     var32.isWaitingOnOcclusionQuery = true;
                     var43++;
                  }
               }
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, true);
            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glEnable(2912);
            var41 += this.renderSortedRenderers(var27, var45, var2, var3);
         }
      } else {
         var41 += this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var2, var3);
      }

      return var41;
   }

   private void checkOcclusionQueryResult(int var1, int var2, double var3, double var5, double var7) {
      for (int var9 = var1; var9 < var2; var9++) {
         WorldRenderer var10 = this.sortedWorldRenderers[var9];
         if (var10.isWaitingOnOcclusionQuery) {
            ((Buffer)this.occlusionResult).clear();
            ARBOcclusionQuery.glGetQueryObjectuARB(var10.glOcclusionQuery, 34919, this.occlusionResult);
            if (this.occlusionResult.get(0) != 0) {
               var10.isWaitingOnOcclusionQuery = false;
               ((Buffer)this.occlusionResult).clear();
               ARBOcclusionQuery.glGetQueryObjectuARB(var10.glOcclusionQuery, 34918, this.occlusionResult);
               boolean var11 = var10.isVisible;
               var10.isVisible = this.occlusionResult.get(0) > 0;
               if (var11 && var10.isVisible) {
                  var10.isVisibleFromPosition = true;
                  var10.visibleFromX = var3;
                  var10.visibleFromY = var5;
                  var10.visibleFromZ = var7;
               }
            }
         }
      }
   }

   private int renderSortedRenderers(int var1, int var2, int var3, double var4) {
      ((Buffer)this.field_22019_aY).clear();
      int var6 = 0;

      for (int var7 = var1; var7 < var2; var7++) {
         if (var3 == 0) {
            this.renderersLoaded++;
            if (this.sortedWorldRenderers[var7].skipRenderPass[var3]) {
               this.renderersSkippingRenderPass++;
            } else if (!this.sortedWorldRenderers[var7].isInFrustum) {
               this.renderersBeingClipped++;
            } else if (this.occlusionEnabled && !this.sortedWorldRenderers[var7].isVisible) {
               this.renderersBeingOccluded++;
            } else {
               this.renderersBeingRendered++;
            }
         }

         if (!this.sortedWorldRenderers[var7].skipRenderPass[var3] && this.sortedWorldRenderers[var7].isInFrustum && (!this.occlusionEnabled || this.sortedWorldRenderers[var7].isVisible)) {
            int var8 = this.sortedWorldRenderers[var7].getGLCallListForPass(var3);
            if (var8 >= 0) {
               this.field_22019_aY.put(var8);
               var6++;
            }
         }
      }

      ((Buffer)this.field_22019_aY).flip();
      EntityLiving var14 = this.mc.renderViewEntity;
      double var15 = var14.lastTickPosX + (var14.posX - var14.lastTickPosX) * var4 - WorldRenderer.chunkOffsetX;
      double var10 = var14.lastTickPosY + (var14.posY - var14.lastTickPosY) * var4;
      double var12 = var14.lastTickPosZ + (var14.posZ - var14.lastTickPosZ) * var4 - WorldRenderer.chunkOffsetZ;
      GL11.glTranslatef((float)(-var15), (float)(-var10), (float)(-var12));
      GL11.glCallLists(this.field_22019_aY);
      GL11.glTranslatef((float)var15, (float)var10, (float)var12);
      return var6;
   }

   public void renderAllRenderLists(int var1, double var2) {
   }

   public void updateClouds() {
      this.cloudOffsetX++;
   }

   public void renderSky(float var1) {
      if (!this.mc.theWorld.worldProvider.isNether) {
         GL11.glDisable(3553);
         Vec3D var2 = this.worldObj.getSkyColor(this.mc.renderViewEntity, var1);
         float var3 = (float)var2.xCoord;
         float var4 = (float)var2.yCoord;
         float var5 = (float)var2.zCoord;
         if (this.mc.gameSettings.anaglyph) {
            float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
            float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            float var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
            var3 = var6;
            var4 = var7;
            var5 = var8;
         }

         GL11.glColor3f(var3, var4, var5);
         Tessellator var17 = Tessellator.instance;
         GL11.glDepthMask(false);
         GL11.glEnable(2912);
         GL11.glColor3f(var3, var4, var5);
         if (Config.isSkyEnabled()) {
            GL11.glCallList(this.glSkyList);
         }

         GL11.glDisable(2912);
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         RenderHelper.disableStandardItemLighting();
         float[] var18 = this.worldObj.worldProvider.calcSunriseSunsetColors(this.worldObj.getCelestialAngle(var1), var1);
         if (var18 != null && Config.isSkyEnabled()) {
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            float var19 = this.worldObj.getCelestialAngle(var1);
            GL11.glRotatef(var19 <= 0.5F ? 0.0F : 180.0F, 0.0F, 0.0F, 1.0F);
            float var9 = var18[0];
            float var10 = var18[1];
            float var11 = var18[2];
            if (this.mc.gameSettings.anaglyph) {
               float var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
               float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
               float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
               var9 = var12;
               var10 = var13;
               var11 = var14;
            }

            var17.startDrawing(6);
            var17.setColorRGBA_F(var9, var10, var11, var18[3]);
            var17.addVertex(0.0, 100.0, 0.0);
            int var24 = 16;
            var17.setColorRGBA_F(var18[0], var18[1], var18[2], 0.0F);

            for (int var27 = 0; var27 <= var24; var27++) {
               float var29 = var27 * 3.141593F * 2.0F / var24;
               float var15 = MathHelper.sin(var29);
               float var16 = MathHelper.cos(var29);
               var17.addVertex(var15 * 120.0F, var16 * 120.0F, -var16 * 40.0F * var18[3]);
            }

            var17.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(7424);
         }

         GL11.glEnable(3553);
         GL11.glBlendFunc(770, 1);
         GL11.glPushMatrix();
         float var20 = 1.0F - this.worldObj.getRainStrength(var1);
         float var21 = 0.0F;
         float var22 = 0.0F;
         float var23 = 0.0F;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, var20);
         GL11.glTranslatef(var21, var22, var23);
         GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(this.worldObj.getCelestialAngle(var1) * 360.0F, 1.0F, 0.0F, 0.0F);
         float var25 = 30.0F;
         GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain/sun.png"));
         var17.startDrawingQuads();
         var17.addVertexWithUV(-var25, 100.0, -var25, 0.0, 0.0);
         var17.addVertexWithUV(var25, 100.0, -var25, 1.0, 0.0);
         var17.addVertexWithUV(var25, 100.0, var25, 1.0, 1.0);
         var17.addVertexWithUV(-var25, 100.0, var25, 0.0, 1.0);
         var17.draw();
         var25 = 20.0F;
         GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain/moon.png"));
         var17.startDrawingQuads();
         var17.addVertexWithUV(-var25, -100.0, var25, 1.0, 1.0);
         var17.addVertexWithUV(var25, -100.0, var25, 0.0, 1.0);
         var17.addVertexWithUV(var25, -100.0, -var25, 0.0, 0.0);
         var17.addVertexWithUV(-var25, -100.0, -var25, 1.0, 0.0);
         var17.draw();
         GL11.glDisable(3553);
         float var28 = this.worldObj.getStarBrightness(var1) * var20;
         if (var28 > 0.0F) {
            GL11.glColor4f(var28, var28, var28, var28);
            if (Config.isStarsEnabled()) {
               GL11.glCallList(this.starGLCallList);
            }
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(3042);
         GL11.glEnable(3008);
         GL11.glEnable(2912);
         GL11.glPopMatrix();
         if (this.worldObj.worldProvider.func_28112_c()) {
            GL11.glColor3f(var3 * 0.2F + 0.04F, var4 * 0.2F + 0.04F, var5 * 0.6F + 0.1F);
         } else {
            GL11.glColor3f(var3, var4, var5);
         }

         GL11.glDisable(3553);
         if (Config.isSkyEnabled()) {
            GL11.glCallList(this.glSkyList2);
         }

         GL11.glEnable(3553);
         GL11.glDepthMask(true);
      }
   }

   public void renderClouds(float var1) {
      if (!this.mc.theWorld.worldProvider.isNether) {
         if (this.mc.gameSettings.ofClouds != 3) {
            if (Config.isCloudsFancy()) {
               this.renderCloudsFancy(var1);
            } else {
               GL11.glDisable(2884);
               float var2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * var1);
               byte var3 = 32;
               int var4 = 256 / var3;
               Tessellator var5 = Tessellator.instance;
               GL11.glBindTexture(3553, this.renderEngine.getTexture("/environment/clouds.png"));
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               Vec3D var6 = this.worldObj.drawClouds(var1);
               float var7 = (float)var6.xCoord;
               float var8 = (float)var6.yCoord;
               float var9 = (float)var6.zCoord;
               if (this.mc.gameSettings.anaglyph) {
                  float var10 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
                  float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
                  float var12 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
                  var7 = var10;
                  var8 = var11;
                  var9 = var12;
               }

               float var22 = 4.882813E-4F;
               double var23 = this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * var1 + (this.cloudOffsetX + var1) * 0.03F;
               double var13 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * var1;
               int var15 = MathHelper.floor_double(var23 / 2048.0);
               int var16 = MathHelper.floor_double(var13 / 2048.0);
               var23 -= var15 * 2048;
               var13 -= var16 * 2048;
               float var17 = this.worldObj.worldProvider.getCloudHeight() - var2 + 0.33F;
               var17 += this.mc.gameSettings.ofCloudsHeight * 25.0F;
               float var18 = (float)(var23 * var22);
               float var19 = (float)(var13 * var22);
               var5.startDrawingQuads();
               var5.setColorRGBA_F(var7, var8, var9, 0.8F);

               for (int var20 = -var3 * var4; var20 < var3 * var4; var20 += var3) {
                  for (int var21 = -var3 * var4; var21 < var3 * var4; var21 += var3) {
                     var5.addVertexWithUV(var20 + 0, var17, var21 + var3, (var20 + 0) * var22 + var18, (var21 + var3) * var22 + var19);
                     var5.addVertexWithUV(var20 + var3, var17, var21 + var3, (var20 + var3) * var22 + var18, (var21 + var3) * var22 + var19);
                     var5.addVertexWithUV(var20 + var3, var17, var21 + 0, (var20 + var3) * var22 + var18, (var21 + 0) * var22 + var19);
                     var5.addVertexWithUV(var20 + 0, var17, var21 + 0, (var20 + 0) * var22 + var18, (var21 + 0) * var22 + var19);
                  }
               }

               var5.draw();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glDisable(3042);
               GL11.glEnable(2884);
            }
         }
      }
   }

   public boolean hasCloudFog(double var1, double var3, double var5, float var7) {
      return false;
   }

   public void renderCloudsFancy(float var1) {
      GL11.glDisable(2884);
      float var2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * var1);
      Tessellator var3 = Tessellator.instance;
      float var4 = 12.0F;
      float var5 = 4.0F;
      double var6 = (this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * var1 + (this.cloudOffsetX + var1) * 0.03F) / var4;
      double var8 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * var1) / var4 + 0.33F;
      float var10 = this.worldObj.worldProvider.getCloudHeight() - var2 + 0.33F;
      var10 += this.mc.gameSettings.ofCloudsHeight * 25.0F;
      int var11 = MathHelper.floor_double(var6 / 2048.0);
      int var12 = MathHelper.floor_double(var8 / 2048.0);
      var6 -= var11 * 2048;
      var8 -= var12 * 2048;
      GL11.glBindTexture(3553, this.renderEngine.getTexture("/environment/clouds.png"));
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      Vec3D var13 = this.worldObj.drawClouds(var1);
      float var14 = (float)var13.xCoord;
      float var15 = (float)var13.yCoord;
      float var16 = (float)var13.zCoord;
      if (this.mc.gameSettings.anaglyph) {
         float var17 = (var14 * 30.0F + var15 * 59.0F + var16 * 11.0F) / 100.0F;
         float var18 = (var14 * 30.0F + var15 * 70.0F) / 100.0F;
         float var19 = (var14 * 30.0F + var16 * 70.0F) / 100.0F;
         var14 = var17;
         var15 = var18;
         var16 = var19;
      }

      float var38 = (float)(var6 * 0.0);
      float var40 = (float)(var8 * 0.0);
      float var42 = 0.00390625F;
      var38 = MathHelper.floor_double(var6) * var42;
      var40 = MathHelper.floor_double(var8) * var42;
      float var20 = (float)(var6 - MathHelper.floor_double(var6));
      float var21 = (float)(var8 - MathHelper.floor_double(var8));
      int var22 = 8;
      byte var23 = 3;
      float var24 = 9.765625E-4F;
      GL11.glScalef(var4, 1.0F, var4);

      for (int var25 = 0; var25 < 2; var25++) {
         if (var25 == 0) {
            GL11.glColorMask(false, false, false, false);
         } else if (this.mc.gameSettings.anaglyph) {
            if (EntityRenderer.anaglyphField == 0) {
               GL11.glColorMask(false, true, true, true);
            } else {
               GL11.glColorMask(true, false, false, true);
            }
         } else {
            GL11.glColorMask(true, true, true, true);
         }

         double var26 = 0.02;

         for (int var28 = -var23 + 1; var28 <= var23; var28++) {
            for (int var29 = -var23 + 1; var29 <= var23; var29++) {
               var3.startDrawingQuads();
               float var30 = var28 * var22;
               float var31 = var29 * var22;
               float var32 = var30 - var20;
               float var33 = var31 - var21;
               var3.setColorRGBA_F(var14 * 0.9F, var15 * 0.9F, var16 * 0.9F, 0.8F);
               if (var28 > -1) {
                  var3.setNormal(-1.0F, 0.0F, 0.0F);

                  for (int var34 = 0; var34 < var22; var34++) {
                     var3.addVertexWithUV(var32 + var34 + 0.0F, var10 + 0.0F + var26, var33 + var22, (var30 + var34 + 0.5F) * var42 + var38, (var31 + var22) * var42 + var40);
                     var3.addVertexWithUV(var32 + var34 + 0.0F, var10 + var5 - var26, var33 + var22, (var30 + var34 + 0.5F) * var42 + var38, (var31 + var22) * var42 + var40);
                     var3.addVertexWithUV(var32 + var34 + 0.0F, var10 + var5 - var26, var33 + 0.0F, (var30 + var34 + 0.5F) * var42 + var38, (var31 + 0.0F) * var42 + var40);
                     var3.addVertexWithUV(var32 + var34 + 0.0F, var10 + 0.0F + var26, var33 + 0.0F, (var30 + var34 + 0.5F) * var42 + var38, (var31 + 0.0F) * var42 + var40);
                  }
               }

               if (var28 <= 1) {
                  var3.setNormal(1.0F, 0.0F, 0.0F);

                  for (int var43 = 0; var43 < var22; var43++) {
                     var3.addVertexWithUV(var32 + var43 + 1.0F - var24, var10 + 0.0F + var26, var33 + var22, (var30 + var43 + 0.5F) * var42 + var38, (var31 + var22) * var42 + var40);
                     var3.addVertexWithUV(var32 + var43 + 1.0F - var24, var10 + var5 - var26, var33 + var22, (var30 + var43 + 0.5F) * var42 + var38, (var31 + var22) * var42 + var40);
                     var3.addVertexWithUV(var32 + var43 + 1.0F - var24, var10 + var5 - var26, var33 + 0.0F, (var30 + var43 + 0.5F) * var42 + var38, (var31 + 0.0F) * var42 + var40);
                     var3.addVertexWithUV(var32 + var43 + 1.0F - var24, var10 + 0.0F + var26, var33 + 0.0F, (var30 + var43 + 0.5F) * var42 + var38, (var31 + 0.0F) * var42 + var40);
                  }
               }

               var3.setColorRGBA_F(var14 * 0.8F, var15 * 0.8F, var16 * 0.8F, 0.8F);
               if (var29 > -1) {
                  var3.setNormal(0.0F, 0.0F, -1.0F);

                  for (int var44 = 0; var44 < var22; var44++) {
                     var3.addVertexWithUV(var32 + 0.0F, var10 + var5 - var26, var33 + var44 + 0.0F, (var30 + 0.0F) * var42 + var38, (var31 + var44 + 0.5F) * var42 + var40);
                     var3.addVertexWithUV(var32 + var22, var10 + var5 - var26, var33 + var44 + 0.0F, (var30 + var22) * var42 + var38, (var31 + var44 + 0.5F) * var42 + var40);
                     var3.addVertexWithUV(var32 + var22, var10 + 0.0F + var26, var33 + var44 + 0.0F, (var30 + var22) * var42 + var38, (var31 + var44 + 0.5F) * var42 + var40);
                     var3.addVertexWithUV(var32 + 0.0F, var10 + 0.0F + var26, var33 + var44 + 0.0F, (var30 + 0.0F) * var42 + var38, (var31 + var44 + 0.5F) * var42 + var40);
                  }
               }

               if (var29 <= 1) {
                  var3.setNormal(0.0F, 0.0F, 1.0F);

                  for (int var45 = 0; var45 < var22; var45++) {
                     var3.addVertexWithUV(var32 + 0.0F, var10 + var5 - var26, var33 + var45 + 1.0F - var24, (var30 + 0.0F) * var42 + var38, (var31 + var45 + 0.5F) * var42 + var40);
                     var3.addVertexWithUV(var32 + var22, var10 + var5 - var26, var33 + var45 + 1.0F - var24, (var30 + var22) * var42 + var38, (var31 + var45 + 0.5F) * var42 + var40);
                     var3.addVertexWithUV(var32 + var22, var10 + 0.0F + var26, var33 + var45 + 1.0F - var24, (var30 + var22) * var42 + var38, (var31 + var45 + 0.5F) * var42 + var40);
                     var3.addVertexWithUV(var32 + 0.0F, var10 + 0.0F + var26, var33 + var45 + 1.0F - var24, (var30 + 0.0F) * var42 + var38, (var31 + var45 + 0.5F) * var42 + var40);
                  }
               }

               if (var10 > -var5 - 1.0F) {
                  var3.setColorRGBA_F(var14 * 0.7F, var15 * 0.7F, var16 * 0.7F, 0.8F);
                  var3.setNormal(0.0F, -1.0F, 0.0F);
                  var3.addVertexWithUV(var32 + 0.0F, var10 + 0.0F, var33 + var22, (var30 + 0.0F) * var42 + var38, (var31 + var22) * var42 + var40);
                  var3.addVertexWithUV(var32 + var22, var10 + 0.0F, var33 + var22, (var30 + var22) * var42 + var38, (var31 + var22) * var42 + var40);
                  var3.addVertexWithUV(var32 + var22, var10 + 0.0F, var33 + 0.0F, (var30 + var22) * var42 + var38, (var31 + 0.0F) * var42 + var40);
                  var3.addVertexWithUV(var32 + 0.0F, var10 + 0.0F, var33 + 0.0F, (var30 + 0.0F) * var42 + var38, (var31 + 0.0F) * var42 + var40);
               }

               if (var10 <= var5 + 1.0F) {
                  var3.setColorRGBA_F(var14, var15, var16, 0.8F);
                  var3.setNormal(0.0F, 1.0F, 0.0F);
                  var3.addVertexWithUV(var32 + 0.0F, var10 + var5 - var24, var33 + var22, (var30 + 0.0F) * var42 + var38, (var31 + var22) * var42 + var40);
                  var3.addVertexWithUV(var32 + var22, var10 + var5 - var24, var33 + var22, (var30 + var22) * var42 + var38, (var31 + var22) * var42 + var40);
                  var3.addVertexWithUV(var32 + var22, var10 + var5 - var24, var33 + 0.0F, (var30 + var22) * var42 + var38, (var31 + 0.0F) * var42 + var40);
                  var3.addVertexWithUV(var32 + 0.0F, var10 + var5 - var24, var33 + 0.0F, (var30 + 0.0F) * var42 + var38, (var31 + 0.0F) * var42 + var40);
               }

               var3.draw();
            }
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GL11.glEnable(2884);
   }

   public boolean updateRenderers(EntityLiving var1, boolean var2) {
      this.lastUpdateStartTimeNs = this.updateStartTimeNs;
      this.updateStartTimeNs = System.nanoTime();
      long var3 = this.updateStartTimeNs + this.updateTimeNs;
      int var5 = Config.getUpdatesPerFrame();
      if (Config.isDynamicUpdates() && !this.isMoving(var1)) {
         var5 *= 3;
      }

      this.renderersUpdated = 0;

      do {
         this.renderersFound = 0;
         this.updateRenderersImpl(var1, var2);
      } while (this.renderersFound > 0 && System.nanoTime() - var3 < 0L);

      if (this.renderersFound > 0) {
         var5 = Math.min(var5, this.renderersFound);
         long var6 = 400000L;
         if (this.renderersUpdated > var5) {
            this.updateTimeNs -= 2L * var6;
         }

         if (this.renderersUpdated < var5) {
            this.updateTimeNs += var6;
         }
      } else {
         this.updateTimeNs = 0L;
         this.updateTimeNs -= 200000L;
      }

      if (this.updateTimeNs < 0L) {
         this.updateTimeNs = 0L;
      }

      return this.renderersUpdated > 0;
   }

   private void updateRenderersImpl(EntityLiving var1, boolean var2) {
      this.renderersFound = 0;
      boolean var3 = true;
      if (this.currentUpdateRenderer != null) {
         this.renderersFound++;
         var3 = this.updateRenderer(this.currentUpdateRenderer);
         if (var3) {
            this.renderersUpdated++;
         }
      }

      if (this.worldRenderersToUpdate.size() > 0) {
         int var4 = 4;
         WorldRenderer var5 = null;
         float var6 = Float.MAX_VALUE;
         int var7 = -1;

         for (int var8 = 0; var8 < this.worldRenderersToUpdate.size(); var8++) {
            WorldRenderer var9 = (WorldRenderer)this.worldRenderersToUpdate.get(var8);
            if (var9 != null) {
               this.renderersFound++;
               if (!var9.needsUpdate) {
                  this.worldRenderersToUpdate.set(var8, null);
               } else {
                  float var10 = var9.distanceToEntitySquared(var1);
                  if (var10 <= 256.0F && this.isActingNow()) {
                     var9.updateRenderer();
                     var9.needsUpdate = false;
                     this.worldRenderersToUpdate.set(var8, null);
                     this.renderersUpdated++;
                  } else {
                     if (!var9.isInFrustum) {
                        var10 *= var4;
                     }

                     if (var5 == null) {
                        var5 = var9;
                        var6 = var10;
                        var7 = var8;
                     } else if (var10 < var6) {
                        var5 = var9;
                        var6 = var10;
                        var7 = var8;
                     }
                  }
               }
            }
         }

         if (this.currentUpdateRenderer == null || var3) {
            if (var5 != null) {
               this.worldRenderersToUpdate.set(var7, null);
               if (!this.updateRenderer(var5)) {
                  return;
               }

               this.renderersUpdated++;
               if (System.nanoTime() > this.updateStartTimeNs + this.updateTimeNs) {
                  return;
               }

               float var13 = var6 / 5.0F;

               for (int var15 = 0; var15 < this.worldRenderersToUpdate.size(); var15++) {
                  WorldRenderer var18 = (WorldRenderer)this.worldRenderersToUpdate.get(var15);
                  if (var18 != null) {
                     float var11 = var18.distanceToEntitySquared(var1);
                     if (!var18.isInFrustum) {
                        var11 *= var4;
                     }

                     float var12 = Math.abs(var11 - var6);
                     if (var12 < var13) {
                        this.worldRenderersToUpdate.set(var15, null);
                        if (!this.updateRenderer(var18)) {
                           return;
                        }

                        this.renderersUpdated++;
                        if (System.nanoTime() > this.updateStartTimeNs + this.updateTimeNs) {
                           break;
                        }
                     }
                  }
               }
            }

            if (this.renderersFound == 0) {
               this.worldRenderersToUpdate.clear();
            }

            if (this.worldRenderersToUpdate.size() > 100 && this.renderersFound < this.worldRenderersToUpdate.size() * 4 / 5) {
               int var14 = 0;

               for (int var16 = 0; var16 < this.worldRenderersToUpdate.size(); var16++) {
                  Object var19 = this.worldRenderersToUpdate.get(var16);
                  if (var19 != null && var16 != var14) {
                     this.worldRenderersToUpdate.set(var14, var19);
                     var14++;
                  }
               }

               for (int var17 = this.worldRenderersToUpdate.size() - 1; var17 >= var14; var17--) {
                  this.worldRenderersToUpdate.remove(var17);
               }
            }
         }
      }
   }

   private boolean updateRenderer(WorldRenderer var1) {
      long var2 = this.updateStartTimeNs + this.updateTimeNs;
      var1.needsUpdate = false;
      boolean var4 = var1.updateRenderer(var2);
      if (!var4) {
         this.currentUpdateRenderer = var1;
         return false;
      } else {
         var1.finishUpdate();
         this.currentUpdateRenderer = null;
         return true;
      }
   }

   private boolean isMoving(EntityLiving var1) {
      boolean var2 = this.isMovingNow(var1);
      if (var2) {
         this.lastMovedTime = System.currentTimeMillis();
         return true;
      } else {
         return System.currentTimeMillis() - this.lastMovedTime < 2000L;
      }
   }

   private boolean isMovingNow(EntityLiving var1) {
      double var2 = 0.001;
      if (var1.isJumping) {
         return true;
      } else if (var1.isSneaking()) {
         return true;
      } else if (var1.prevSwingProgress > var2) {
         return true;
      } else if (this.mc.mouseHelper.deltaX != 0) {
         return true;
      } else if (this.mc.mouseHelper.deltaY != 0) {
         return true;
      } else if (Math.abs(var1.posX - var1.prevPosX) > var2) {
         return true;
      } else {
         return Math.abs(var1.posY - var1.prevPosY) > var2 ? true : Math.abs(var1.posZ - var1.prevPosZ) > var2;
      }
   }

   private boolean isActingNow() {
      return Mouse.isButtonDown(0) ? true : Mouse.isButtonDown(1);
   }

   public void drawBlockBreaking(EntityPlayer var1, MovingObjectPosition var2, int var3, ItemStack var4, float var5) {
      Tessellator var6 = Tessellator.instance;
      GL11.glEnable(3042);
      GL11.glEnable(3008);
      GL11.glBlendFunc(770, 1);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
      if (var3 == 0) {
         if (this.damagePartialTime > 0.0F) {
            GL11.glBlendFunc(774, 768);
            int var7 = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553, var7);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            int var8 = this.worldObj.getBlockId(var2.blockX, var2.blockY, var2.blockZ);
            Block var9 = var8 <= 0 ? null : Block.blocksList[var8];
            GL11.glDisable(3008);
            GL11.glPolygonOffset(-3.0F, -3.0F);
            GL11.glEnable(32823);
            double var10 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var5;
            double var12 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var5;
            double var14 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var5;
            if (var9 == null) {
               var9 = Block.stone;
            }

            GL11.glEnable(3008);
            var6.startDrawingQuads();
            var6.setTranslationD(-var10, -var12, -var14);
            var6.disableColor();
            this.globalRenderBlocks.renderBlockUsingTexture(var9, var2.blockX, var2.blockY, var2.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
            var6.draw();
            var6.setTranslationD(0.0, 0.0, 0.0);
            GL11.glDisable(3008);
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glDisable(32823);
            GL11.glEnable(3008);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
         }
      } else if (var4 != null) {
         GL11.glBlendFunc(770, 771);
         float var16 = MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
         GL11.glColor4f(var16, var16, var16, MathHelper.sin((float)System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
         int var17 = this.renderEngine.getTexture("/terrain.png");
         GL11.glBindTexture(3553, var17);
         int var18 = var2.blockX;
         int var20 = var2.blockY;
         int var11 = var2.blockZ;
         if (var2.sideHit == 0) {
            var20--;
         }

         if (var2.sideHit == 1) {
            var20++;
         }

         if (var2.sideHit == 2) {
            var11--;
         }

         if (var2.sideHit == 3) {
            var11++;
         }

         if (var2.sideHit == 4) {
            var18--;
         }

         if (var2.sideHit == 5) {
            var18++;
         }
      }

      GL11.glDisable(3042);
      GL11.glDisable(3008);
   }

   public void drawSelectionBox(EntityPlayer var1, MovingObjectPosition var2, int var3, ItemStack var4, float var5) {
      if (var3 == 0 && var2.typeOfHit == EnumMovingObjectType.TILE) {
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
         GL11.glLineWidth(2.0F);
         GL11.glDisable(3553);
         GL11.glDepthMask(false);
         float var6 = 0.002F;
         int var7 = this.worldObj.getBlockId(var2.blockX, var2.blockY, var2.blockZ);
         if (var7 > 0) {
            Block.blocksList[var7].setBlockBoundsBasedOnState(this.worldObj, var2.blockX, var2.blockY, var2.blockZ);
            double var8 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * var5;
            double var10 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * var5;
            double var12 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * var5;
            this.drawOutlinedBoundingBox(Block.blocksList[var7].getSelectedBoundingBoxFromPool(this.worldObj, var2.blockX, var2.blockY, var2.blockZ).expand(var6, var6, var6).getOffsetBoundingBox(-var8, -var10, -var12));
         }

         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glDisable(3042);
      }
   }

   private void drawOutlinedBoundingBox(AxisAlignedBB var1) {
      Tessellator var2 = Tessellator.instance;
      var2.startDrawing(3);
      var2.addVertex(var1.minX, var1.minY, var1.minZ);
      var2.addVertex(var1.maxX, var1.minY, var1.minZ);
      var2.addVertex(var1.maxX, var1.minY, var1.maxZ);
      var2.addVertex(var1.minX, var1.minY, var1.maxZ);
      var2.addVertex(var1.minX, var1.minY, var1.minZ);
      var2.draw();
      var2.startDrawing(3);
      var2.addVertex(var1.minX, var1.maxY, var1.minZ);
      var2.addVertex(var1.maxX, var1.maxY, var1.minZ);
      var2.addVertex(var1.maxX, var1.maxY, var1.maxZ);
      var2.addVertex(var1.minX, var1.maxY, var1.maxZ);
      var2.addVertex(var1.minX, var1.maxY, var1.minZ);
      var2.draw();
      var2.startDrawing(1);
      var2.addVertex(var1.minX, var1.minY, var1.minZ);
      var2.addVertex(var1.minX, var1.maxY, var1.minZ);
      var2.addVertex(var1.maxX, var1.minY, var1.minZ);
      var2.addVertex(var1.maxX, var1.maxY, var1.minZ);
      var2.addVertex(var1.maxX, var1.minY, var1.maxZ);
      var2.addVertex(var1.maxX, var1.maxY, var1.maxZ);
      var2.addVertex(var1.minX, var1.minY, var1.maxZ);
      var2.addVertex(var1.minX, var1.maxY, var1.maxZ);
      var2.draw();
   }

   public void func_949_a(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7 = MathHelper.bucketInt(var1, 16);
      int var8 = MathHelper.bucketInt(var2, 16);
      int var9 = MathHelper.bucketInt(var3, 16);
      int var10 = MathHelper.bucketInt(var4, 16);
      int var11 = MathHelper.bucketInt(var5, 16);
      int var12 = MathHelper.bucketInt(var6, 16);

      for (int var13 = var7; var13 <= var10; var13++) {
         int var14 = var13 % this.renderChunksWide;
         if (var14 < 0) {
            var14 += this.renderChunksWide;
         }

         for (int var15 = var8; var15 <= var11; var15++) {
            int var16 = var15 % this.renderChunksTall;
            if (var16 < 0) {
               var16 += this.renderChunksTall;
            }

            for (int var17 = var9; var17 <= var12; var17++) {
               int var18 = var17 % this.renderChunksDeep;
               if (var18 < 0) {
                  var18 += this.renderChunksDeep;
               }

               int var19 = (var18 * this.renderChunksTall + var16) * this.renderChunksWide + var14;
               WorldRenderer var20 = this.worldRenderers[var19];
               if (!var20.needsUpdate) {
                  this.worldRenderersToUpdate.add(var20);
                  var20.markDirty();
               }
            }
         }
      }
   }

   public void markBlockAndNeighborsNeedsUpdate(int var1, int var2, int var3) {
      this.func_949_a(var1 - 1, var2 - 1, var3 - 1, var1 + 1, var2 + 1, var3 + 1);
   }

   public void markBlockRangeNeedsUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.func_949_a(var1 - 1, var2 - 1, var3 - 1, var4 + 1, var5 + 1, var6 + 1);
   }

   public void clipRenderersByFrustrum(ICamera var1, float var2) {
      for (int var3 = 0; var3 < this.worldRenderers.length; var3++) {
         if (!this.worldRenderers[var3].skipAllRenderPasses()) {
            this.worldRenderers[var3].updateInFrustrum(var1);
         }
      }

      this.frustrumCheckOffset++;
   }

   public void playRecord(String var1, int var2, int var3, int var4) {
      if (var1 != null) {
         this.mc.ingameGUI.setRecordPlayingMessage("C418 - " + var1);
      }

      this.mc.sndManager.playStreaming(var1, var2, var3, var4, 1.0F, 1.0F);
   }

   public void playSound(String var1, double var2, double var4, double var6, float var8, float var9) {
      float var10 = 16.0F;
      if (var8 > 1.0F) {
         var10 *= var8;
      }

      if (this.mc.renderViewEntity.getDistanceSq(var2, var4, var6) < var10 * var10) {
         this.mc.sndManager.playSound(var1, (float)var2, (float)var4, (float)var6, var8, var9);
      }
   }

   public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
         double var14 = this.mc.renderViewEntity.posX - var2;
         double var16 = this.mc.renderViewEntity.posY - var4;
         double var18 = this.mc.renderViewEntity.posZ - var6;
         double var20 = 16.0;
         if (!(var14 * var14 + var16 * var16 + var18 * var18 > var20 * var20)) {
            if (var1.equals("bubble")) {
               this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if (var1.equals("smoke")) {
               if (Config.isAnimatedSmoke()) {
                  this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var4, var6, var8, var10, var12));
               }
            } else if (var1.equals("note")) {
               this.mc.effectRenderer.addEffect(new EntityNoteFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if (var1.equals("portal")) {
               this.mc.effectRenderer.addEffect(new EntityPortalFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if (var1.equals("explode")) {
               if (Config.isAnimatedExplosion()) {
                  this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, var2, var4, var6, var8, var10, var12));
               }
            } else if (var1.equals("flame")) {
               if (Config.isAnimatedFlame()) {
                  this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, var2, var4, var6, var8, var10, var12));
               }
            } else if (var1.equals("lava")) {
               this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, var2, var4, var6));
            } else if (var1.equals("footstep")) {
               this.mc.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.worldObj, var2, var4, var6));
            } else if (var1.equals("splash")) {
               this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if (var1.equals("largesmoke")) {
               if (Config.isAnimatedSmoke()) {
                  this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, var2, var4, var6, var8, var10, var12, 2.5F));
               }
            } else if (var1.equals("reddust")) {
               if (Config.isAnimatedRedstone()) {
                  this.mc.effectRenderer.addEffect(new EntityReddustFX(this.worldObj, var2, var4, var6, (float)var8, (float)var10, (float)var12));
               }
            } else if (var1.equals("snowballpoof")) {
               this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, var2, var4, var6, Item.snowball));
            } else if (var1.equals("snowshovel")) {
               this.mc.effectRenderer.addEffect(new EntitySnowShovelFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            } else if (var1.equals("slime")) {
               this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, var2, var4, var6, Item.slimeBall));
            } else if (var1.equals("heart")) {
               this.mc.effectRenderer.addEffect(new EntityHeartFX(this.worldObj, var2, var4, var6, var8, var10, var12));
            }
         }
      }
   }

   public void obtainEntitySkin(Entity var1) {
      var1.updateCloak();
      if (var1.skinUrl != null) {
         this.renderEngine.obtainImageData(var1.skinUrl, new ImageBufferDownload());
      }

      if (var1.cloakUrl != null) {
         this.renderEngine.obtainImageData(var1.cloakUrl, new ImageBufferDownload());
      }
   }

   public void releaseEntitySkin(Entity var1) {
      if (var1.skinUrl != null) {
         this.renderEngine.releaseImageData(var1.skinUrl);
      }

      if (var1.cloakUrl != null) {
         this.renderEngine.releaseImageData(var1.cloakUrl);
      }
   }

   public void updateAllRenderers() {
      if (this.worldRenderers != null) {
         for (int var1 = 0; var1 < this.worldRenderers.length; var1++) {
            if (this.worldRenderers[var1].isChunkLit && !this.worldRenderers[var1].needsUpdate) {
               this.worldRenderersToUpdate.add(this.worldRenderers[var1]);
               this.worldRenderers[var1].markDirty();
            }
         }
      }
   }

   public void setAllRenderesVisible() {
      if (this.worldRenderers != null) {
         for (int var1 = 0; var1 < this.worldRenderers.length; var1++) {
            this.worldRenderers[var1].isVisible = true;
         }
      }
   }

   public void doNothingWithTileEntity(int var1, int var2, int var3, TileEntity var4) {
   }

   public void func_28137_f() {
      GLAllocation.deleteDisplayLists(this.glRenderListBase);
   }

   public void playAuxSFX(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
      Random var7 = this.worldObj.rand;
      switch (var2) {
         case 1000:
            this.worldObj.playSoundEffect(var3, var4, var5, "random.click", 1.0F, 1.0F);
            break;
         case 1001:
            this.worldObj.playSoundEffect(var3, var4, var5, "random.click", 1.0F, 1.2F);
            break;
         case 1002:
            this.worldObj.playSoundEffect(var3, var4, var5, "random.bow", 1.0F, 1.2F);
            break;
         case 1003:
            if (Math.random() < 0.5) {
               this.worldObj.playSoundEffect(var3 + 0.5, var4 + 0.5, var5 + 0.5, "random.door_open", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            } else {
               this.worldObj.playSoundEffect(var3 + 0.5, var4 + 0.5, var5 + 0.5, "random.door_close", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            break;
         case 1004:
            this.worldObj.playSoundEffect(var3 + 0.5F, var4 + 0.5F, var5 + 0.5F, "random.fizz", 0.5F, 2.6F + (var7.nextFloat() - var7.nextFloat()) * 0.8F);
            break;
         case 1005:
            if (Item.itemsList[var6] instanceof ItemRecord) {
               this.worldObj.playRecord(((ItemRecord)Item.itemsList[var6]).recordName, var3, var4, var5);
            } else {
               this.worldObj.playRecord(null, var3, var4, var5);
            }
            break;
         case 2000:
            int var8 = var6 % 3 - 1;
            int var9 = var6 / 3 % 3 - 1;
            double var10 = var3 + var8 * 0.6 + 0.5;
            double var12 = var4 + 0.5;
            double var14 = var5 + var9 * 0.6 + 0.5;

            for (int var31 = 0; var31 < 10; var31++) {
               double var32 = var7.nextDouble() * 0.2 + 0.01;
               double var19 = var10 + var8 * 0.01 + (var7.nextDouble() - 0.5) * var9 * 0.5;
               double var21 = var12 + (var7.nextDouble() - 0.5) * 0.5;
               double var23 = var14 + var9 * 0.01 + (var7.nextDouble() - 0.5) * var8 * 0.5;
               double var25 = var8 * var32 + var7.nextGaussian() * 0.01;
               double var27 = -0.03 + var7.nextGaussian() * 0.01;
               double var29 = var9 * var32 + var7.nextGaussian() * 0.01;
               this.spawnParticle("smoke", var19, var21, var23, var25, var27, var29);
            }
            break;
         case 2001:
            int var16 = var6 & 0xFF;
            if (var16 > 0) {
               Block var17 = Block.blocksList[var16];
               this.mc.sndManager.playSound(var17.stepSound.stepSoundDir(), var3 + 0.5F, var4 + 0.5F, var5 + 0.5F, (var17.stepSound.getVolume() + 1.0F) / 2.0F, var17.stepSound.getPitch() * 0.8F);
            }

            this.mc.effectRenderer.addBlockDestroyEffects(var3, var4, var5, var6 & 0xFF, var6 >> 8 & 0xFF);
      }
   }

   public int renderAllSortedRenderers(int var1, double var2) {
      return this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var1, var2);
   }
}