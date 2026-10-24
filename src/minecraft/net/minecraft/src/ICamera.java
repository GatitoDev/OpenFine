package net.minecraft.src;

public interface ICamera {
   boolean isBoundingBoxInFrustum(AxisAlignedBB p_isBoundingBoxInFrustum_1_);

   boolean isBoundingBoxInFrustumFully(AxisAlignedBB p_isBoundingBoxInFrustumFully_1_);

   void setPosition(double p_setPosition_1_, double p_setPosition_3_, double p_setPosition_5_);
}
