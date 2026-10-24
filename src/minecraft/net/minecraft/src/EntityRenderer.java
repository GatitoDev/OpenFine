package net.minecraft.src;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class EntityRenderer {
   public static boolean anaglyphEnable = false;
   public static int anaglyphField;
   private Minecraft mc;
   private float farPlaneDistance;
   public ItemRenderer itemRenderer;
   private int rendererUpdateCount;
   private Entity pointedEntity;
   private MouseFilter mouseFilterXAxis;
   private MouseFilter mouseFilterYAxis;
   private MouseFilter mouseFilterDummy1;
   private MouseFilter mouseFilterDummy2;
   private MouseFilter mouseFilterDummy3;
   private MouseFilter mouseFilterDummy4;
   private float thirdPersonDistance;
   private float thirdPersonDistanceTemp;
   private float debugCamYaw;
   private float prevDebugCamYaw;
   private float debugCamPitch;
   private float prevDebugCamPitch;
   private float debugCamFOV;
   private float prevDebugCamFOV;
   private float camRoll;
   private float prevCamRoll;
   private boolean cloudFog;
   private double cameraZoom;
   private double cameraYaw;
   private double cameraPitch;
   private long prevFrameTime;
   private long renderEndNanoTime;
   private Random random;
   private int rainSoundCounter;
   volatile int unusedVolatile0;
   volatile int unusedVolatile1;
   FloatBuffer fogColorBuffer;
   float fogColorRed;
   float fogColorGreen;
   float fogColorBlue;
   private float fogColor2;
   private float fogColor1;
   private WorldProvider updatedWorldProvider = null;
   private boolean showDebugInfo = false;
   private boolean zoomMode = false;

   public EntityRenderer(Minecraft var1) {
      this.farPlaneDistance = 0.0F;
      this.pointedEntity = null;
      this.mouseFilterXAxis = new MouseFilter();
      this.mouseFilterYAxis = new MouseFilter();
      this.mouseFilterDummy1 = new MouseFilter();
      this.mouseFilterDummy2 = new MouseFilter();
      this.mouseFilterDummy3 = new MouseFilter();
      this.mouseFilterDummy4 = new MouseFilter();
      this.thirdPersonDistance = 4.0F;
      this.thirdPersonDistanceTemp = 4.0F;
      this.debugCamYaw = 0.0F;
      this.prevDebugCamYaw = 0.0F;
      this.debugCamPitch = 0.0F;
      this.prevDebugCamPitch = 0.0F;
      this.debugCamFOV = 0.0F;
      this.prevDebugCamFOV = 0.0F;
      this.camRoll = 0.0F;
      this.prevCamRoll = 0.0F;
      this.cloudFog = false;
      this.cameraZoom = 1.0;
      this.cameraYaw = 0.0;
      this.cameraPitch = 0.0;
      this.prevFrameTime = System.currentTimeMillis();
      this.renderEndNanoTime = 0L;
      this.random = new Random();
      this.rainSoundCounter = 0;
      this.unusedVolatile0 = 0;
      this.unusedVolatile1 = 0;
      this.fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
      this.mc = var1;
      this.itemRenderer = new ItemRenderer(var1);
   }

   public void updateRenderer() {
      this.fogColor2 = this.fogColor1;
      this.thirdPersonDistanceTemp = this.thirdPersonDistance;
      this.prevDebugCamYaw = this.debugCamYaw;
      this.prevDebugCamPitch = this.debugCamPitch;
      this.prevDebugCamFOV = this.debugCamFOV;
      this.prevCamRoll = this.camRoll;
      if (this.mc.renderViewEntity == null) {
         this.mc.renderViewEntity = this.mc.thePlayer;
      }

      float var1 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
      float var2 = (3 - this.mc.gameSettings.renderDistance) / 3.0F;
      float var3 = var1 * (1.0F - var2) + var2;
      this.fogColor1 = this.fogColor1 + (var3 - this.fogColor1) * 0.1F;
      this.rendererUpdateCount++;
      this.itemRenderer.updateEquippedItem();
      this.addRainParticles();
   }

   public void getMouseOver(float var1) {
      if (this.mc.renderViewEntity != null) {
         if (this.mc.theWorld != null) {
            double var2 = this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(var2, var1);
            double var4 = var2;
            Vec3D var6 = this.mc.renderViewEntity.getPosition(var1);
            if (this.mc.objectMouseOver != null) {
               var4 = this.mc.objectMouseOver.hitVec.distanceTo(var6);
            }

            if (this.mc.playerController instanceof PlayerControllerTest) {
               var2 = 32.0;
               var4 = 32.0;
            } else {
               if (var4 > 3.0) {
                  var4 = 3.0;
               }

               var2 = var4;
            }

            Vec3D var7 = this.mc.renderViewEntity.getLook(var1);
            Vec3D var8 = var6.addVector(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2);
            this.pointedEntity = null;
            float var9 = 1.0F;
            List var10 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(var7.xCoord * var2, var7.yCoord * var2, var7.zCoord * var2).expand(var9, var9, var9));
            double var11 = 0.0;

            for (int var13 = 0; var13 < var10.size(); var13++) {
               Entity var14 = (Entity)var10.get(var13);
               if (var14.canBeCollidedWith()) {
                  float var15 = var14.getCollisionBorderSize();
                  AxisAlignedBB var16 = var14.boundingBox.expand(var15, var15, var15);
                  MovingObjectPosition var17 = var16.calculateIntercept(var6, var8);
                  if (var16.isVecInside(var6)) {
                     if (0.0 < var11 || var11 == 0.0) {
                        this.pointedEntity = var14;
                        var11 = 0.0;
                     }
                  } else if (var17 != null) {
                     double var18 = var6.distanceTo(var17.hitVec);
                     if (var18 < var11 || var11 == 0.0) {
                        this.pointedEntity = var14;
                        var11 = var18;
                     }
                  }
               }
            }

            if (this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
               this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
            }
         }
      }
   }

   private float getFOVModifier(float var1, boolean applyModifiers) {
      EntityLiving var2 = this.mc.renderViewEntity;
      float var3 = 70.0F;
      if (var2.isInsideOfMaterial(Material.water)) {
         var3 = 60.0F;
      }

      if (applyModifiers) {
         var3 += this.mc.gameSettings.fieldOfView * 40.0F;

         if (Keyboard.isKeyDown(this.mc.gameSettings.ofKeyBindZoom.keyCode)) {
            if (!this.zoomMode) {
               this.zoomMode = true;
               this.mc.gameSettings.smoothCamera = true;
            }

            if (this.zoomMode) {
               var3 /= 4.0F;
            }
         } else if (this.zoomMode) {
            this.zoomMode = false;
            this.mc.gameSettings.smoothCamera = false;
         }
      }

      if (var2.health <= 0) {
         float var4 = var2.deathTime + var1;
         var3 /= (1.0F - 500.0F / (var4 + 500.0F)) * 2.0F + 1.0F;
      }

      return var3 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * var1;
   }

   private void hurtCameraEffect(float var1) {
      EntityLiving var2 = this.mc.renderViewEntity;
      float var3 = var2.hurtTime - var1;
      if (var2.health <= 0) {
         float var4 = var2.deathTime + var1;
         GL11.glRotatef(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
      }

      if (!(var3 < 0.0F)) {
         var3 /= var2.maxHurtTime;
         var3 = MathHelper.sin(var3 * var3 * var3 * var3 * 3.141593F);
         float var7 = var2.attackedAtYaw;
         GL11.glRotatef(-var7, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(var7, 0.0F, 1.0F, 0.0F);
      }
   }

   private void setupViewBobbing(float var1) {
      if (this.mc.renderViewEntity instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)this.mc.renderViewEntity;
         float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
         float var4 = -(var2.distanceWalkedModified + var3 * var1);
         float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * var1;
         float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * var1;
         GL11.glTranslatef(MathHelper.sin(var4 * 3.141593F) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * 3.141593F) * var5), 0.0F);
         GL11.glRotatef(MathHelper.sin(var4 * 3.141593F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(Math.abs(MathHelper.cos(var4 * 3.141593F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
      }
   }

   private void orientCamera(float var1) {
      EntityLiving var2 = this.mc.renderViewEntity;
      float var3 = var2.yOffset - 1.62F;
      double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * var1;
      double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * var1 - var3;
      double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * var1;
      GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * var1, 0.0F, 0.0F, 1.0F);
      if (var2.isPlayerSleeping()) {
         var3 = (float)(var3 + 1.0);
         GL11.glTranslatef(0.0F, 0.3F, 0.0F);
         if (!this.mc.gameSettings.debugCamEnable) {
            int var10 = this.mc.theWorld.getBlockId(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
            if (var10 == Block.blockBed.blockID) {
               int var11 = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posY), MathHelper.floor_double(var2.posZ));
               int var12 = var11 & 3;
               GL11.glRotatef(var12 * 90, 0.0F, 1.0F, 0.0F);
            }

            GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, -1.0F, 0.0F);
            GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, -1.0F, 0.0F, 0.0F);
         }
      } else if (this.mc.gameSettings.thirdPersonView) {
         double var30 = this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * var1;
         if (this.mc.gameSettings.debugCamEnable) {
            float var31 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * var1;
            float var13 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * var1;
            GL11.glTranslatef(0.0F, 0.0F, (float)(-var30));
            GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var31, 0.0F, 1.0F, 0.0F);
         } else {
            float var32 = var2.rotationYaw;
            float var33 = var2.rotationPitch;
            double var14 = -MathHelper.sin(var32 / 180.0F * 3.141593F) * MathHelper.cos(var33 / 180.0F * 3.141593F) * var30;
            double var16 = MathHelper.cos(var32 / 180.0F * 3.141593F) * MathHelper.cos(var33 / 180.0F * 3.141593F) * var30;
            double var18 = -MathHelper.sin(var33 / 180.0F * 3.141593F) * var30;

            for (int var20 = 0; var20 < 8; var20++) {
               float var21 = (var20 & 1) * 2 - 1;
               float var22 = (var20 >> 1 & 1) * 2 - 1;
               float var23 = (var20 >> 2 & 1) * 2 - 1;
               var21 *= 0.1F;
               var22 *= 0.1F;
               var23 *= 0.1F;
               MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(var4 + var21, var6 + var22, var8 + var23), Vec3D.createVector(var4 - var14 + var21 + var23, var6 - var18 + var22, var8 - var16 + var23));
               if (var24 != null) {
                  double var25 = var24.hitVec.distanceTo(Vec3D.createVector(var4, var6, var8));
                  if (var25 < var30) {
                     var30 = var25;
                  }
               }
            }

            GL11.glRotatef(var2.rotationPitch - var33, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.rotationYaw - var32, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, (float)(-var30));
            GL11.glRotatef(var32 - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var33 - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GL11.glTranslatef(0.0F, 0.0F, -0.1F);
      }

      if (!this.mc.gameSettings.debugCamEnable) {
         GL11.glRotatef(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * var1, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * var1 + 180.0F, 0.0F, 1.0F, 0.0F);
      }

      GL11.glTranslatef(0.0F, var3, 0.0F);
      var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * var1;
      var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * var1 - var3;
      var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * var1;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, var1);
   }

   private void setupCameraTransform(float var1, int var2) {
      this.farPlaneDistance = 32 << 3 - this.mc.gameSettings.renderDistance;
      if (Config.isFarView()) {
         if (this.farPlaneDistance < 256.0F) {
            this.farPlaneDistance *= 3.0F;
         } else {
            this.farPlaneDistance *= 2.0F;
         }
      }

      if (Config.isFogFancy()) {
         this.farPlaneDistance *= 0.95F;
      } else {
         this.farPlaneDistance *= 0.83F;
      }

      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      float var3 = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
         GL11.glTranslatef(-(var2 * 2 - 1) * var3, 0.0F, 0.0F);
      }

      if (this.cameraZoom != 1.0) {
         GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
         GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0);
         GLU.gluPerspective(this.getFOVModifier(var1, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
      } else {
         GLU.gluPerspective(this.getFOVModifier(var1, true), (float)this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      if (this.mc.gameSettings.anaglyph) {
         GL11.glTranslatef((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.hurtCameraEffect(var1);
      if (this.mc.gameSettings.viewBobbing) {
         this.setupViewBobbing(var1);
      }

      float var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * var1;
      if (var4 > 0.0F) {
         float var5 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
         var5 *= var5;
         GL11.glRotatef((this.rendererUpdateCount + var1) * 20.0F, 0.0F, 1.0F, 1.0F);
         GL11.glScalef(1.0F / var5, 1.0F, 1.0F);
         GL11.glRotatef(-(this.rendererUpdateCount + var1) * 20.0F, 0.0F, 1.0F, 1.0F);
      }

      this.orientCamera(var1);
   }

   private void renderHand(float var1, int var2) {
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      float var3 = 0.07F;
      if (this.mc.gameSettings.anaglyph) {
         GL11.glTranslatef(-(var2 * 2 - 1) * var3, 0.0F, 0.0F);
      }

      GLU.gluPerspective(this.getFOVModifier(var1, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      if (this.mc.gameSettings.anaglyph) {
         GL11.glTranslatef((var2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      GL11.glPushMatrix();
      this.hurtCameraEffect(var1);
      if (this.mc.gameSettings.viewBobbing) {
         this.setupViewBobbing(var1);
      }

      if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI) {
         this.itemRenderer.renderItemInFirstPerson(var1);
      }

      GL11.glPopMatrix();
      if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping()) {
         this.itemRenderer.renderOverlays(var1);
         this.hurtCameraEffect(var1);
      }

      if (this.mc.gameSettings.viewBobbing) {
         this.setupViewBobbing(var1);
      }
   }

   public void updateCameraAndRender(float var1) {
      World var2 = this.mc.theWorld;
      if (var2 != null && var2.worldProvider != null && this.updatedWorldProvider != var2.worldProvider) {
         this.updateWorldLightLevels();
         this.updatedWorldProvider = this.mc.theWorld.worldProvider;
      }

      Minecraft.hasPaidCheckTime = 0L;
      RenderBlocks.fancyGrass = Config.isGrassFancy();
      if (Config.isBetterGrassFancy()) {
         RenderBlocks.fancyGrass = true;
      }

      Block.leaves.setGraphicsLevel(Config.isTreesFancy());
      Config.setMinecraft(this.mc);
      if (Config.getIconWidthTerrain() > 16 && !(this.itemRenderer instanceof ItemRendererHD)) {
         this.itemRenderer = new ItemRendererHD(this.mc);
         RenderManager.instance.itemRenderer = this.itemRenderer;
      }

      if (var2 != null) {
         var2.autosavePeriod = this.mc.gameSettings.ofAutoSaveTicks;
      }

      if (!Config.isWeatherEnabled() && var2 != null && var2.worldInfo != null) {
         var2.worldInfo.setRaining(false);
      }

      if (var2 != null) {
         long var3 = var2.getWorldTime();
         long var5 = var3 % 24000L;
         if (Config.isTimeDayOnly()) {
            if (var5 <= 1000L) {
               var2.setWorldTime(var3 - var5 + 1001L);
            }

            if (var5 >= 11000L) {
               var2.setWorldTime(var3 - var5 + 24001L);
            }
         }

         if (Config.isTimeNightOnly()) {
            if (var5 <= 14000L) {
               var2.setWorldTime(var3 - var5 + 14001L);
            }

            if (var5 >= 22000L) {
               var2.setWorldTime(var3 - var5 + 24000L + 14001L);
            }
         }
      }

      if (!Display.isActive()) {
         if (System.currentTimeMillis() - this.prevFrameTime > 500L) {
            this.mc.displayInGameMenu();
         }
      } else {
         this.prevFrameTime = System.currentTimeMillis();
      }

      if (this.mc.inGameHasFocus) {
         this.mc.mouseHelper.mouseXYChange();
         float var14 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float var4 = var14 * var14 * var14 * 8.0F;
         float var17 = this.mc.mouseHelper.deltaX * var4;
         float var6 = this.mc.mouseHelper.deltaY * var4;
         int var7 = 1;
         if (this.mc.gameSettings.invertMouse) {
            var7 = -1;
         }

         if (this.mc.gameSettings.smoothCamera) {
            var17 = this.mouseFilterXAxis.smooth(var17, 0.05F * var4);
            var6 = this.mouseFilterYAxis.smooth(var6, 0.05F * var4);
         }

         this.mc.thePlayer.setAngles(var17, var6 * var7);
      }

      if (!this.mc.skipRenderWorld) {
         anaglyphEnable = this.mc.gameSettings.anaglyph;
         ScaledResolution var15 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
         int var16 = var15.getScaledWidth();
         int var18 = var15.getScaledHeight();
         int var19 = Mouse.getX() * var16 / this.mc.displayWidth;
         int var20 = var18 - Mouse.getY() * var18 / this.mc.displayHeight - 1;
         char var8 = 200;
         if (this.mc.gameSettings.limitFramerate == 1) {
            var8 = 'x';
         }

         if (this.mc.gameSettings.limitFramerate == 2) {
            var8 = '(';
         }

         if (this.mc.theWorld != null) {
            if (this.mc.gameSettings.limitFramerate == 0) {
               this.renderWorld(var1, 0L);
            } else {
               this.renderWorld(var1, this.renderEndNanoTime + (long)(1.0E9 / var8));
            }

            if (this.mc.gameSettings.limitFramerate == 2) {
               long var9 = (this.renderEndNanoTime + (long)(1.0E9 / var8) - System.nanoTime()) / 1000000L;
               if (var9 > 0L && var9 < 500L) {
                  try {
                     Thread.sleep(var9);
                  } catch (InterruptedException var13) {
                     var13.printStackTrace();
                  }
               }
            }

            this.renderEndNanoTime = System.nanoTime();
            if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
               if (this.mc.gameSettings.ofFastDebugInfo) {
                  if (Minecraft.isDebugInfoEnabled()) {
                     this.showDebugInfo = !this.showDebugInfo;
                  }

                  if (this.showDebugInfo) {
                     this.mc.gameSettings.showDebugInfo = true;
                  }
               }

               this.mc.ingameGUI.renderGameOverlay(var1, this.mc.currentScreen != null, var19, var20);
               if (this.mc.gameSettings.ofFastDebugInfo) {
                  this.mc.gameSettings.showDebugInfo = false;
               }
            }
         } else {
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            this.setupOverlayRendering();
            if (this.mc.gameSettings.limitFramerate == 2) {
               long var21 = (this.renderEndNanoTime + (long)(1.0E9 / var8) - System.nanoTime()) / 1000000L;
               if (var21 < 0L) {
                  var21 += 10L;
               }

               if (var21 > 0L && var21 < 500L) {
                  try {
                     Thread.sleep(var21);
                  } catch (InterruptedException var12) {
                     var12.printStackTrace();
                  }
               }
            }

            this.renderEndNanoTime = System.nanoTime();
         }

         if (this.mc.currentScreen != null) {
            GL11.glClear(256);
            this.mc.currentScreen.drawScreen(var19, var20, var1);
            if (this.mc.currentScreen != null && this.mc.currentScreen.guiParticles != null) {
               this.mc.currentScreen.guiParticles.draw(var1);
            }
         }
      }
   }

   public void updateWorldLightLevels() {
      if (this.mc != null) {
         if (this.mc.theWorld != null) {
            if (this.mc.theWorld.worldProvider != null) {
               float var1 = this.mc.gameSettings.ofBrightness;
               float[] var2 = this.mc.theWorld.worldProvider.lightBrightnessTable;
               float var3 = 0.05F;
               if (this.mc.theWorld.worldProvider != null && this.mc.theWorld.worldProvider.isNether) {
                  var3 = 0.1F + var1 * 0.15F;
               }

               float var4 = 3.0F * (1.0F - var1);

               for (int var5 = 0; var5 <= 15; var5++) {
                  float var6 = 1.0F - var5 / 15.0F;
                  var2[var5] = (1.0F - var6) / (var6 * var4 + 1.0F) * (1.0F - var3) + var3;
               }

               Config.setLightLevels(var2);
            }
         }
      }
   }

   public void renderWorld(float var1, long var2) {
      GL11.glEnable(2884);
      GL11.glEnable(2929);
      if (this.mc.renderViewEntity == null) {
         this.mc.renderViewEntity = this.mc.thePlayer;
      }

      this.getMouseOver(var1);
      EntityLiving var4 = this.mc.renderViewEntity;
      RenderGlobal var5 = this.mc.renderGlobal;
      EffectRenderer var6 = this.mc.effectRenderer;
      double var7 = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * var1;
      double var9 = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * var1;
      double var11 = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * var1;
      IChunkProvider var13 = this.mc.theWorld.getIChunkProvider();
      if (var13 instanceof ChunkProviderLoadOrGenerate) {
         ChunkProviderLoadOrGenerate var14 = (ChunkProviderLoadOrGenerate)var13;
         int var15 = MathHelper.floor_float((int)var7) >> 4;
         int var16 = MathHelper.floor_float((int)var11) >> 4;
         var14.setCurrentChunkOver(var15, var16);
      }

      for (int var19 = 0; var19 < 2; var19++) {
         if (this.mc.gameSettings.anaglyph) {
            anaglyphField = var19;
            if (anaglyphField == 0) {
               GL11.glColorMask(false, true, true, false);
            } else {
               GL11.glColorMask(true, false, false, false);
            }
         }

         GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
         this.updateFogColor(var1);
         GL11.glClear(16640);
         GL11.glEnable(2884);
         this.setupCameraTransform(var1, var19);
         ClippingHelperImpl.getInstance();
         if (this.mc.gameSettings.renderDistance < 2 || Config.isFarView()) {
            this.setupFog(-1, var1);
            var5.renderSky(var1);
         }

         GL11.glEnable(2912);
         this.setupFog(1, var1);
         if (this.mc.gameSettings.ambientOcclusion) {
            GL11.glShadeModel(7425);
         }

         Frustrum var20 = new Frustrum();
         var20.setPosition(var7, var9, var11);
         this.mc.renderGlobal.clipRenderersByFrustrum(var20, var1);
         if (var19 == 0) {
            while (!this.mc.renderGlobal.updateRenderers(var4, false) && var2 != 0L) {
               long var21 = var2 - System.nanoTime();
               if (var21 < 0L || !(var21 <= 1.0E9)) {
                  break;
               }
            }
         }

         this.setupFog(0, var1);
         GL11.glEnable(2912);
         GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
         RenderHelper.disableStandardItemLighting();
         if (Config.isUseAlphaFunc()) {
            GL11.glAlphaFunc(516, Config.getAlphaFuncLevel());
         }

         var5.sortAndRender(var4, 0, var1);
         GL11.glShadeModel(7424);
         RenderHelper.enableStandardItemLighting();
         var5.renderEntities(var4.getPosition(var1), var20, var1);
         var6.renderLitParticles(var4, var1);
         RenderHelper.disableStandardItemLighting();
         this.setupFog(0, var1);
         var6.renderParticles(var4, var1);
         if (this.mc.objectMouseOver != null && var4.isInsideOfMaterial(Material.water) && var4 instanceof EntityPlayer) {
            EntityPlayer var18 = (EntityPlayer)var4;
            GL11.glDisable(3008);
            var5.drawBlockBreaking(var18, this.mc.objectMouseOver, 0, var18.inventory.getCurrentItem(), var1);
            var5.drawSelectionBox(var18, this.mc.objectMouseOver, 0, var18.inventory.getCurrentItem(), var1);
            GL11.glEnable(3008);
         }

         GL11.glBlendFunc(770, 771);
         this.setupFog(0, var1);
         GL11.glEnable(3042);
         GL11.glDisable(2884);
         GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
         if (Config.isWaterFancy()) {
            if (this.mc.gameSettings.ambientOcclusion) {
               GL11.glShadeModel(7425);
            }

            GL11.glColorMask(false, false, false, false);
            int var22 = var5.renderAllSortedRenderers(1, var1);
            if (this.mc.gameSettings.anaglyph) {
               if (anaglyphField == 0) {
                  GL11.glColorMask(false, true, true, true);
               } else {
                  GL11.glColorMask(true, false, false, true);
               }
            } else {
               GL11.glColorMask(true, true, true, true);
            }

            if (var22 > 0) {
               var5.renderAllSortedRenderers(1, var1);
            }

            GL11.glShadeModel(7424);
         } else {
            var5.sortAndRender(var4, 1, var1);
         }

         GL11.glDepthMask(true);
         GL11.glEnable(2884);
         GL11.glDisable(3042);
         if (this.cameraZoom == 1.0 && var4 instanceof EntityPlayer && this.mc.objectMouseOver != null && !var4.isInsideOfMaterial(Material.water)) {
            EntityPlayer var23 = (EntityPlayer)var4;
            GL11.glDisable(3008);
            var5.drawBlockBreaking(var23, this.mc.objectMouseOver, 0, var23.inventory.getCurrentItem(), var1);
            var5.drawSelectionBox(var23, this.mc.objectMouseOver, 0, var23.inventory.getCurrentItem(), var1);
            GL11.glEnable(3008);
         }

         this.renderRainSnow(var1);
         GL11.glDisable(2912);
         if (this.pointedEntity == null) {
         }

         this.setupFog(0, var1);
         GL11.glEnable(2912);
         var5.renderClouds(var1);
         GL11.glDisable(2912);
         this.setupFog(1, var1);
         if (this.cameraZoom == 1.0) {
            GL11.glClear(256);
            this.renderHand(var1, var19);
         }

         if (!this.mc.gameSettings.anaglyph) {
            return;
         }
      }

      GL11.glColorMask(true, true, true, false);
   }

   private void addRainParticles() {
      float var1 = this.mc.theWorld.getRainStrength(1.0F);
      if (!Config.isRainFancy()) {
         var1 /= 2.0F;
      }

      if (var1 != 0.0F) {
         this.random.setSeed(this.rendererUpdateCount * 312987231L);
         EntityLiving var2 = this.mc.renderViewEntity;
         World var3 = this.mc.theWorld;
         int var4 = MathHelper.floor_double(var2.posX);
         int var5 = MathHelper.floor_double(var2.posY);
         int var6 = MathHelper.floor_double(var2.posZ);
         byte var7 = 10;
         double var8 = 0.0;
         double var10 = 0.0;
         double var12 = 0.0;
         int var14 = 0;

         for (int var15 = 0; var15 < (int)(100.0F * var1 * var1); var15++) {
            int var16 = var4 + this.random.nextInt(var7) - this.random.nextInt(var7);
            int var17 = var6 + this.random.nextInt(var7) - this.random.nextInt(var7);
            int var18 = var3.findTopSolidBlock(var16, var17);
            int var19 = var3.getBlockId(var16, var18 - 1, var17);
            if (var18 <= var5 + var7 && var18 >= var5 - var7 && var3.getWorldChunkManager().getBiomeGenAt(var16, var17).canSpawnLightningBolt()) {
               float var20 = this.random.nextFloat();
               float var21 = this.random.nextFloat();
               if (var19 > 0) {
                  if (Block.blocksList[var19].blockMaterial == Material.lava) {
                     this.mc.effectRenderer.addEffect(new EntitySmokeFX(var3, var16 + var20, var18 + 0.1F - Block.blocksList[var19].minY, var17 + var21, 0.0, 0.0, 0.0));
                  } else {
                     if (this.random.nextInt(++var14) == 0) {
                        var8 = var16 + var20;
                        var10 = var18 + 0.1F - Block.blocksList[var19].minY;
                        var12 = var17 + var21;
                     }

                     this.mc.effectRenderer.addEffect(new EntityRainFX(var3, var16 + var20, var18 + 0.1F - Block.blocksList[var19].minY, var17 + var21));
                  }
               }
            }
         }

         if (var14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
            this.rainSoundCounter = 0;
            if (var10 > var2.posY + 1.0 && var3.findTopSolidBlock(MathHelper.floor_double(var2.posX), MathHelper.floor_double(var2.posZ)) > MathHelper.floor_double(var2.posY)) {
               this.mc.theWorld.playSoundEffect(var8, var10, var12, "ambient.weather.rain", 0.1F, 0.5F);
            } else {
               this.mc.theWorld.playSoundEffect(var8, var10, var12, "ambient.weather.rain", 0.2F, 1.0F);
            }
         }
      }
   }

   protected void renderRainSnow(float var1) {
      float var2 = this.mc.theWorld.getRainStrength(var1);
      if (!(var2 <= 0.0F)) {
         if (!Config.isRainOff()) {
            EntityLiving var3 = this.mc.renderViewEntity;
            World var4 = this.mc.theWorld;
            int var5 = MathHelper.floor_double(var3.posX);
            int var6 = MathHelper.floor_double(var3.posY);
            int var7 = MathHelper.floor_double(var3.posZ);
            Tessellator var8 = Tessellator.instance;
            GL11.glDisable(2884);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glAlphaFunc(516, 0.01F);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/environment/snow.png"));
            double var9 = var3.lastTickPosX + (var3.posX - var3.lastTickPosX) * var1;
            double var11 = var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * var1;
            double var13 = var3.lastTickPosZ + (var3.posZ - var3.lastTickPosZ) * var1;
            int var15 = MathHelper.floor_double(var11);
            int var16 = 5;
            if (Config.isRainFancy()) {
               var16 = 10;
            }

            BiomeGenBase[] var17 = var4.getWorldChunkManager().getBiomeGenBaseAt(var5 - var16, var7 - var16, var16 * 2 + 1, var16 * 2 + 1);
            int var18 = 0;

            for (int var19 = var5 - var16; var19 <= var5 + var16; var19++) {
               for (int var20 = var7 - var16; var20 <= var7 + var16; var20++) {
                  BiomeGenBase var21 = var17[var18++];
                  if (var21.getEnableSnow()) {
                     int var22 = var4.findTopSolidBlock(var19, var20);
                     if (var22 < 0) {
                        var22 = 0;
                     }

                     int var23 = var22;
                     if (var23 < var15) {
                        var23 = var15;
                     }

                     int var24 = var6 - var16;
                     int var25 = var6 + var16;
                     if (var24 < var22) {
                        var24 = var22;
                     }

                     if (var25 < var22) {
                        var25 = var22;
                     }

                     float var26 = 1.0F;
                     if (var24 != var25) {
                        this.random.setSeed(var19 * var19 * 3121 + var19 * 45238971 + var20 * var20 * 418711 + var20 * 13761);
                        float var27 = this.rendererUpdateCount + var1;
                        float var28 = ((this.rendererUpdateCount & 511) + var1) / 512.0F;
                        float var29 = this.random.nextFloat() + var27 * 0.01F * (float)this.random.nextGaussian();
                        float var30 = this.random.nextFloat() + var27 * (float)this.random.nextGaussian() * 0.001F;
                        double var31 = var19 + 0.5F - var3.posX;
                        double var33 = var20 + 0.5F - var3.posZ;
                        float var35 = MathHelper.sqrt_double(var31 * var31 + var33 * var33) / var16;
                        var8.startDrawingQuads();
                        float var36 = var4.getLightBrightness(var19, var23, var20);
                        GL11.glColor4f(var36, var36, var36, ((1.0F - var35 * var35) * 0.3F + 0.5F) * var2);
                        var8.setTranslationD(-var9 * 1.0, -var11 * 1.0, -var13 * 1.0);
                        var8.addVertexWithUV(var19 + 0, var24, var20 + 0.5, 0.0F * var26 + var29, var24 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 1, var24, var20 + 0.5, 1.0F * var26 + var29, var24 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 1, var25, var20 + 0.5, 1.0F * var26 + var29, var25 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 0, var25, var20 + 0.5, 0.0F * var26 + var29, var25 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 0.5, var24, var20 + 0, 0.0F * var26 + var29, var24 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 0.5, var24, var20 + 1, 1.0F * var26 + var29, var24 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 0.5, var25, var20 + 1, 1.0F * var26 + var29, var25 * var26 / 4.0F + var28 * var26 + var30);
                        var8.addVertexWithUV(var19 + 0.5, var25, var20 + 0, 0.0F * var26 + var29, var25 * var26 / 4.0F + var28 * var26 + var30);
                        var8.setTranslationD(0.0, 0.0, 0.0);
                        var8.draw();
                     }
                  }
               }
            }

            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/environment/rain.png"));
            if (Config.isRainFancy()) {
               var16 = 10;
            }

            var18 = 0;

            for (int var38 = var5 - var16; var38 <= var5 + var16; var38++) {
               for (int var39 = var7 - var16; var39 <= var7 + var16; var39++) {
                  BiomeGenBase var40 = var17[var18++];
                  if (var40.canSpawnLightningBolt()) {
                     int var41 = var4.findTopSolidBlock(var38, var39);
                     int var42 = var6 - var16;
                     int var43 = var6 + var16;
                     if (var42 < var41) {
                        var42 = var41;
                     }

                     if (var43 < var41) {
                        var43 = var41;
                     }

                     float var44 = 1.0F;
                     if (var42 != var43) {
                        this.random.setSeed(var38 * var38 * 3121 + var38 * 45238971 + var39 * var39 * 418711 + var39 * 13761);
                        float var45 = ((this.rendererUpdateCount + var38 * var38 * 3121 + var38 * 45238971 + var39 * var39 * 418711 + var39 * 13761 & 31) + var1) / 32.0F * (3.0F + this.random.nextFloat());
                        double var46 = var38 + 0.5F - var3.posX;
                        double var47 = var39 + 0.5F - var3.posZ;
                        float var48 = MathHelper.sqrt_double(var46 * var46 + var47 * var47) / var16;
                        var8.startDrawingQuads();
                        float var32 = var4.getLightBrightness(var38, 128, var39) * 0.85F + 0.15F;
                        GL11.glColor4f(var32, var32, var32, ((1.0F - var48 * var48) * 0.5F + 0.5F) * var2);
                        var8.setTranslationD(-var9 * 1.0, -var11 * 1.0, -var13 * 1.0);
                        var8.addVertexWithUV(var38 + 0, var42, var39 + 0.5, 0.0F * var44, var42 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 1, var42, var39 + 0.5, 1.0F * var44, var42 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 1, var43, var39 + 0.5, 1.0F * var44, var43 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 0, var43, var39 + 0.5, 0.0F * var44, var43 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 0.5, var42, var39 + 0, 0.0F * var44, var42 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 0.5, var42, var39 + 1, 1.0F * var44, var42 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 0.5, var43, var39 + 1, 1.0F * var44, var43 * var44 / 4.0F + var45 * var44);
                        var8.addVertexWithUV(var38 + 0.5, var43, var39 + 0, 0.0F * var44, var43 * var44 / 4.0F + var45 * var44);
                        var8.setTranslationD(0.0, 0.0, 0.0);
                        var8.draw();
                     }
                  }
               }
            }

            GL11.glEnable(2884);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
         }
      }
   }

   public void setupOverlayRendering() {
      ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0, var1.scaledWidthD, var1.scaledHeightD, 0.0, 1000.0, 3000.0);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
   }

   private void updateFogColor(float var1) {
      World var2 = this.mc.theWorld;
      EntityLiving var3 = this.mc.renderViewEntity;
      float var4 = 1.0F / (4 - this.mc.gameSettings.renderDistance);
      var4 = 1.0F - (float)Math.pow(var4, 0.25);
      Vec3D var5 = var2.getSkyColor(this.mc.renderViewEntity, var1);
      float var6 = (float)var5.xCoord;
      float var7 = (float)var5.yCoord;
      float var8 = (float)var5.zCoord;
      Vec3D var9 = var2.getFogColor(var1);
      this.fogColorRed = (float)var9.xCoord;
      this.fogColorGreen = (float)var9.yCoord;
      this.fogColorBlue = (float)var9.zCoord;
      this.fogColorRed = this.fogColorRed + (var6 - this.fogColorRed) * var4;
      this.fogColorGreen = this.fogColorGreen + (var7 - this.fogColorGreen) * var4;
      this.fogColorBlue = this.fogColorBlue + (var8 - this.fogColorBlue) * var4;
      float var10 = var2.getRainStrength(var1);
      if (var10 > 0.0F) {
         float var11 = 1.0F - var10 * 0.5F;
         float var12 = 1.0F - var10 * 0.4F;
         this.fogColorRed *= var11;
         this.fogColorGreen *= var11;
         this.fogColorBlue *= var12;
      }

      float var17 = var2.getWeightedThunderStrength(var1);
      if (var17 > 0.0F) {
         float var18 = 1.0F - var17 * 0.5F;
         this.fogColorRed *= var18;
         this.fogColorGreen *= var18;
         this.fogColorBlue *= var18;
      }

      if (this.cloudFog) {
         Vec3D var19 = var2.drawClouds(var1);
         this.fogColorRed = (float)var19.xCoord;
         this.fogColorGreen = (float)var19.yCoord;
         this.fogColorBlue = (float)var19.zCoord;
      } else if (var3.isInsideOfMaterial(Material.water)) {
         this.fogColorRed = 0.02F;
         this.fogColorGreen = 0.02F;
         this.fogColorBlue = 0.2F;
      } else if (var3.isInsideOfMaterial(Material.lava)) {
         this.fogColorRed = 0.6F;
         this.fogColorGreen = 0.1F;
         this.fogColorBlue = 0.0F;
      }

      float var20 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * var1;
      this.fogColorRed *= var20;
      this.fogColorGreen *= var20;
      this.fogColorBlue *= var20;
      if (this.mc.gameSettings.anaglyph) {
         float var13 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
         float var14 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
         float var15 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
         this.fogColorRed = var13;
         this.fogColorGreen = var14;
         this.fogColorBlue = var15;
      }

      GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
   }

   private void setupFog(int var1, float var2) {
      EntityLiving var3 = this.mc.renderViewEntity;
      GL11.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.cloudFog) {
         GL11.glFogi(2917, 2048);
         GL11.glFogf(2914, 0.1F);
         float var4 = 1.0F;
         float var5 = 1.0F;
         float var6 = 1.0F;
         if (this.mc.gameSettings.anaglyph) {
            float var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
            float var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
            float var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
         }
      } else if (var3.isInsideOfMaterial(Material.water)) {
         GL11.glFogi(2917, 2048);
         float var11 = 0.1F;
         if (Config.isClearWater()) {
            var11 = 0.02F;
         }

         GL11.glFogf(2914, var11);
         float var14 = 0.4F;
         float var17 = 0.4F;
         float var19 = 0.9F;
         if (this.mc.gameSettings.anaglyph) {
            float var21 = (var14 * 30.0F + var17 * 59.0F + var19 * 11.0F) / 100.0F;
            float var23 = (var14 * 30.0F + var17 * 70.0F) / 100.0F;
            float var10 = (var14 * 30.0F + var19 * 70.0F) / 100.0F;
         }
      } else if (var3.isInsideOfMaterial(Material.lava)) {
         GL11.glFogi(2917, 2048);
         GL11.glFogf(2914, 2.0F);
         float var12 = 0.4F;
         float var15 = 0.3F;
         float var18 = 0.3F;
         if (this.mc.gameSettings.anaglyph) {
            float var20 = (var12 * 30.0F + var15 * 59.0F + var18 * 11.0F) / 100.0F;
            float var22 = (var12 * 30.0F + var15 * 70.0F) / 100.0F;
            float var24 = (var12 * 30.0F + var18 * 70.0F) / 100.0F;
         }
      } else {
         GL11.glFogi(2917, 9729);
         if (GLContext.getCapabilities().GL_NV_fog_distance) {
            if (Config.isFogFancy()) {
               GL11.glFogi(34138, 34139);
            } else {
               GL11.glFogi(34138, 34140);
            }
         }

         float var13 = Config.getFogStart();
         float var16 = 1.0F;
         if (var1 < 0) {
            var13 = 0.0F;
            var16 = 0.8F;
         }

         if (this.mc.theWorld.worldProvider.isNether) {
            var13 = 0.0F;
            var16 = 1.0F;
         }

         GL11.glFogf(2915, this.farPlaneDistance * var13);
         GL11.glFogf(2916, this.farPlaneDistance * var16);
      }

      GL11.glEnable(2903);
      GL11.glColorMaterial(1028, 4608);
   }

   private FloatBuffer setFogColorBuffer(float var1, float var2, float var3, float var4) {
      ((Buffer)this.fogColorBuffer).clear();
      this.fogColorBuffer.put(var1).put(var2).put(var3).put(var4);
      ((Buffer)this.fogColorBuffer).flip();
      return this.fogColorBuffer;
   }
}