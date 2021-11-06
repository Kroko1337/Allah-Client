package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelVex;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.util.ResourceLocation;

public class RenderVex extends RenderBiped<EntityVex>
{
    private static final ResourceLocation VEX_TEXTURE = new ResourceLocation("textures/entity/illager/vex.png");
    private static final ResourceLocation VEX_CHARGING_TEXTURE = new ResourceLocation("textures/entity/illager/vex_charging.png");
    private int modelVersion;

    public RenderVex(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelVex(), 0.3F);
        this.modelVersion = ((ModelVex)this.entityModel).getModelVersion();
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityVex entity)
    {
        return entity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
    }

    public void doRender(EntityVex entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        int i = ((ModelVex)this.entityModel).getModelVersion();

        if (i != this.modelVersion)
        {
            this.entityModel = new ModelVex();
            this.modelVersion = i;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected void preRenderCallback(EntityVex entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }
}
