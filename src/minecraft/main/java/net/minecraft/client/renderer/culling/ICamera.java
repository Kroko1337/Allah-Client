package net.minecraft.client.renderer.culling;

import net.minecraft.util.math.AxisAlignedBB;

public interface ICamera
{
    boolean isBoundingBoxInFrustum(AxisAlignedBB p_78546_1_);

    void setPosition(double xIn, double yIn, double zIn);
}
