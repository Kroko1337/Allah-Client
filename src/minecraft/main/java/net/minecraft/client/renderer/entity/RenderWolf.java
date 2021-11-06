package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;

public class RenderWolf extends RenderLiving<EntityWolf>
{
    private static final ResourceLocation WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf.png");
    private static final ResourceLocation TAMED_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
    private static final ResourceLocation ANGRY_WOLF_TEXTURES = new ResourceLocation("textures/entity/wolf/wolf_angry.png");

    public RenderWolf(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelWolf(), 0.5F);
        this.addLayer(new LayerWolfCollar(this));
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityWolf livingBase, float partialTicks)
    {
        return livingBase.getTailRotation();
    }

    public void doRender(EntityWolf entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.isWolfWet())
        {
            float f = entity.getBrightness() * entity.getShadingWhileWet(partialTicks);
            GlStateManager.color(f, f, f);
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture.
     */
    protected ResourceLocation getEntityTexture(EntityWolf entity)
    {
        if (entity.isTamed())
        {
            return TAMED_WOLF_TEXTURES;
        }
        else
        {
            return entity.isAngry() ? ANGRY_WOLF_TEXTURES : WOLF_TEXTURES;
        }
    }
}
