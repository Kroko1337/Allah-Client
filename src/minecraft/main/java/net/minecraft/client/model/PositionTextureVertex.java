package net.minecraft.client.model;

import net.minecraft.util.math.Vec3d;

public class PositionTextureVertex
{
    public Vec3d position;
    public float textureU;
    public float textureV;

    public PositionTextureVertex(float x, float y, float z, float texU, float texV)
    {
        this(new Vec3d((double)x, (double)y, (double)z), texU, texV);
    }

    public PositionTextureVertex setTextureUV(float texU, float texV)
    {
        return new PositionTextureVertex(this, texU, texV);
    }

    public PositionTextureVertex(PositionTextureVertex textureVertex, float texturePositionXIn, float texturePositionYIn)
    {
        this.position = textureVertex.position;
        this.textureU = texturePositionXIn;
        this.textureV = texturePositionYIn;
    }

    public PositionTextureVertex(Vec3d p_i47091_1_, float p_i47091_2_, float p_i47091_3_)
    {
        this.position = p_i47091_1_;
        this.textureU = p_i47091_2_;
        this.textureV = p_i47091_3_;
    }
}
