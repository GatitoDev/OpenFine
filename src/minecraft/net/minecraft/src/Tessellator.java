package net.minecraft.src;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.Util;

public class Tessellator {
   private static boolean convertQuadsToTriangles = true;
   private static boolean tryVBO = false;
   private ByteBuffer byteBuffer;
   private IntBuffer intBuffer;
   private FloatBuffer floatBuffer;
   private int vertexCount;
   private double textureU;
   private double textureV;
   private int color;
   private boolean hasColor;
   private boolean hasTexture;
   private boolean hasNormals;
   private int rawBufferIndex;
   private int addedVertices;
   private boolean isColorDisabled;
   private int drawMode;
   private double xOffset;
   private double yOffset;
   private double zOffset;
   private int normal;
   public static final int BUFFER_SIZE = 262144;
   public static volatile Tessellator instance = new Tessellator(262144);
   private boolean isDrawing;
   private boolean useVBO;
   private IntBuffer vertexBuffers;
   private int vboIndex;
   private int vboCount;
   private int bufferSize;
   private boolean renderingChunk = false;

   public Tessellator(int var1) {
      this.vertexCount = 0;
      this.hasColor = false;
      this.hasTexture = false;
      this.hasNormals = false;
      this.rawBufferIndex = 0;
      this.addedVertices = 0;
      this.isColorDisabled = false;
      this.isDrawing = false;
      this.useVBO = false;
      this.vboIndex = 0;
      this.vboCount = 10;
      this.bufferSize = var1;
      this.byteBuffer = GLAllocation.createDirectByteBuffer(var1 * 4);
      this.intBuffer = this.byteBuffer.asIntBuffer();
      this.floatBuffer = this.byteBuffer.asFloatBuffer();
      this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
      if (this.useVBO) {
         this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
         ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
      }
   }

   public void draw() {
      if (!this.isDrawing) {
         throw new IllegalStateException("Not tesselating!");
      }

      this.isDrawing = false;
      if (!this.renderingChunk) {
         GL11.glEnd();
         this.checkOpenGlError();
      } else if (this.vertexCount > 0) {
         ((Buffer)this.byteBuffer).position(0);
         ((Buffer)this.byteBuffer).limit(this.rawBufferIndex * 4);
         GL11.glEnableClientState(32888);
         GL11.glEnableClientState(32886);
         GL11.glEnableClientState(32884);
         if (this.useVBO) {
            this.vboIndex = (this.vboIndex + 1) % this.vboCount;
            ARBVertexBufferObject.glBindBufferARB(35044, this.vertexBuffers.get(this.vboIndex));
            ARBVertexBufferObject.glBufferDataARB(35044, this.byteBuffer, 35040);
            GL11.glTexCoordPointer(2, 5126, 32, 12L);
            GL11.glColorPointer(4, 5121, 32, 20L);
            GL11.glVertexPointer(3, 5126, 32, 0L);
         } else {
            ((Buffer)this.floatBuffer).position(3);
            GL11.glTexCoordPointer(2, 32, this.floatBuffer);
            ((Buffer)this.byteBuffer).position(20);
            GL11.glColorPointer(4, true, 32, this.byteBuffer);
            ((Buffer)this.floatBuffer).position(0);
            GL11.glVertexPointer(3, 32, this.floatBuffer);
         }

         if (this.drawMode == 7 && convertQuadsToTriangles) {
            GL11.glDrawArrays(4, 0, this.vertexCount);
         } else {
            GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
         }

         GL11.glDisableClientState(32888);
         GL11.glDisableClientState(32886);
         GL11.glDisableClientState(32884);
      }

      this.reset();
   }

   private void reset() {
      this.vertexCount = 0;
      ((Buffer)this.byteBuffer).clear();
      ((Buffer)this.intBuffer).clear();
      this.rawBufferIndex = 0;
      this.addedVertices = 0;
   }

   public void startDrawingQuads() {
      this.startDrawing(7);
   }

   public void startDrawing(int var1) {
      if (this.isDrawing) {
         throw new IllegalStateException("Already tesselating!");
      }

      if (!this.renderingChunk) {
         GL11.glBegin(var1);
      }

      this.isDrawing = true;
      this.reset();
      this.drawMode = var1;
      this.hasNormals = false;
      this.hasColor = false;
      this.hasTexture = false;
      this.isColorDisabled = false;
   }

   public void setTextureUV(double var1, double var3) {
      this.hasTexture = true;
      this.textureU = var1;
      this.textureV = var3;
      if (!this.renderingChunk) {
         GL11.glTexCoord2f((float)var1, (float)var3);
      }
   }

   public void setColorOpaque_F(float var1, float var2, float var3) {
      this.setColorOpaque((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F));
   }

   public void setColorRGBA_F(float var1, float var2, float var3, float var4) {
      this.setColorRGBA((int)(var1 * 255.0F), (int)(var2 * 255.0F), (int)(var3 * 255.0F), (int)(var4 * 255.0F));
   }

   public void setColorOpaque(int var1, int var2, int var3) {
      this.setColorRGBA(var1, var2, var3, 255);
   }

   public void setColorRGBA(int var1, int var2, int var3, int var4) {
      if (!this.isColorDisabled) {
         if (var1 > 255) {
            var1 = 255;
         }

         if (var2 > 255) {
            var2 = 255;
         }

         if (var3 > 255) {
            var3 = 255;
         }

         if (var4 > 255) {
            var4 = 255;
         }

         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         if (var3 < 0) {
            var3 = 0;
         }

         if (var4 < 0) {
            var4 = 0;
         }

         this.hasColor = true;
         if (!this.renderingChunk) {
            GL11.glColor4ub((byte)var1, (byte)var2, (byte)var3, (byte)var4);
         } else if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.color = var4 << 24 | var3 << 16 | var2 << 8 | var1;
         } else {
            this.color = var1 << 24 | var2 << 16 | var3 << 8 | var4;
         }
      }
   }

   public void addVertexWithUV(double var1, double var3, double var5, double var7, double var9) {
      this.setTextureUV(var7, var9);
      this.addVertex(var1, var3, var5);
   }

   public void addVertex(double var1, double var3, double var5) {
      if (!this.renderingChunk) {
         GL11.glVertex3f((float)(var1 + this.xOffset), (float)(var3 + this.yOffset), (float)(var5 + this.zOffset));
      } else {
         this.addedVertices++;
         if (this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
            for (int var7 = 0; var7 < 2; var7++) {
               int var8 = 8 * (3 - var7);
               this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - var8 + 0));
               this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - var8 + 1));
               this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - var8 + 2));
               this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - var8 + 3));
               this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - var8 + 4));
               this.intBuffer.put(this.intBuffer.get(this.rawBufferIndex - var8 + 5));
               this.intBuffer.put(0);
               this.intBuffer.put(0);
               this.vertexCount++;
               this.rawBufferIndex += 8;
            }
         }

         this.intBuffer.put(Float.floatToRawIntBits((float)(var1 + this.xOffset)));
         this.intBuffer.put(Float.floatToRawIntBits((float)(var3 + this.yOffset)));
         this.intBuffer.put(Float.floatToRawIntBits((float)(var5 + this.zOffset)));
         this.intBuffer.put(Float.floatToRawIntBits((float)this.textureU));
         this.intBuffer.put(Float.floatToRawIntBits((float)this.textureV));
         this.intBuffer.put(this.color);
         this.intBuffer.put(0);
         this.intBuffer.put(0);
         this.rawBufferIndex += 8;
         this.vertexCount++;
         if (this.renderingChunk && this.addedVertices % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
            this.draw();
            this.isDrawing = true;
         }
      }
   }

   public void setColorOpaque_I(int var1) {
      int var2 = var1 >> 16 & 0xFF;
      int var3 = var1 >> 8 & 0xFF;
      int var4 = var1 & 0xFF;
      this.setColorOpaque(var2, var3, var4);
   }

   public void setColorRGBA_I(int var1, int var2) {
      int var3 = var1 >> 16 & 0xFF;
      int var4 = var1 >> 8 & 0xFF;
      int var5 = var1 & 0xFF;
      this.setColorRGBA(var3, var4, var5, var2);
   }

   public void disableColor() {
      this.isColorDisabled = true;
   }

   public void setNormal(float var1, float var2, float var3) {
      if (!this.isDrawing) {
         System.out.println("Error: Not drawing !!!");
      }

      this.hasNormals = true;
      byte var4 = (byte)(var1 * 128.0F);
      byte var5 = (byte)(var2 * 127.0F);
      byte var6 = (byte)(var3 * 127.0F);
      if (!this.renderingChunk) {
         GL11.glNormal3b(var4, var5, var6);
      } else {
         System.out.println("ERROR: NORMALS IN CHUNK MODE !!!");
      }
   }

   public void setTranslationD(double var1, double var3, double var5) {
      this.xOffset = var1;
      this.yOffset = var3;
      this.zOffset = var5;
   }

   public void setTranslationF(float var1, float var2, float var3) {
      this.xOffset += var1;
      this.yOffset += var2;
      this.zOffset += var3;
   }

   public void setRenderingChunk(boolean var1) {
      this.renderingChunk = var1;
   }

   private void checkOpenGlError() {
      int var1 = GL11.glGetError();
      if (var1 != 0) {
         String var2 = "OpenGL Error: " + var1 + " " + Util.translateGLErrorString(var1);
         Exception var3 = new Exception(var2);
         var3.printStackTrace();
      }
   }
}
