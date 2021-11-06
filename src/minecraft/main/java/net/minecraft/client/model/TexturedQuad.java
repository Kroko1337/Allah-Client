package net.minecraft.client.model;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;

public class TexturedQuad
{
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;

    public TexturedQuad(PositionTextureVertex[] vertices)
    {
        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
    }

    public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight)
    {
        this(vertices);
        float f = 0.0F / textureWidth;
        float f1 = 0.0F / textureHeight;
        vertices[0] = vertices[0].setTextureUV((float)texcoordU2 / textureWidth - f, (float)texcoordV1 / textureHeight + f1);
        vertices[1] = vertices[1].setTextureUV((float)texcoordU1 / textureWidth + f, (float)texcoordV1 / textureHeight + f1);
        vertices[2] = vertices[2].setTextureUV((float)texcoordU1 / textureWidth + f, (float)texcoordV2 / textureHeight - f1);
        vertices[3] = vertices[3].setTextureUV((float)texcoordU2 / textureWidth - f, (float)texcoordV2 / textureHeight - f1);
    }

    public void flipFace()
    {
        PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];

        for (int i = 0; i < this.vertexPositions.length; ++i)
        {
            apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
        }

        this.vertexPositions = apositiontexturevertex;
    }

    public void draw(BufferBuilder renderer, float scale)
    {
        Vec3d vec3d = this.vertexPositions[1].position.subtractReverse(this.vertexPositions[0].position);
        Vec3d vec3d1 = this.vertexPositions[1].position.subtractReverse(this.vertexPositions[2].position);
        Vec3d vec3d2 = vec3d1.crossProduct(vec3d).normalize();
        float f = (float)vec3d2.x;
        float f1 = (float)vec3d2.y;
        float f2 = (float)vec3d2.z;

        if (this.invertNormal)
        {
            f = -f;
            f1 = -f1;
            f2 = -f2;
        }

        renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

        for (int i = 0; i < 4; ++i)
        {
            PositionTextureVertex positiontexturevertex = this.vertexPositions[i];
            renderer.pos(positiontexturevertex.position.x * (double)scale, positiontexturevertex.position.y * (double)scale, positiontexturevertex.position.z * (double)scale).tex((double)positiontexturevertex.textureU, (double)positiontexturevertex.textureV).normal(f, f1, f2).endVertex();
        }

        Tessellator.getInstance().draw();
    }
}
