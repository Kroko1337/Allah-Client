package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelSquid extends ModelBase
{
    ModelRenderer body;
    ModelRenderer[] legs = new ModelRenderer[8];

    public ModelSquid()
    {
        int i = -16;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
        this.body.rotationPointY += 8.0F;

        for (int j = 0; j < this.legs.length; ++j)
        {
            this.legs[j] = new ModelRenderer(this, 48, 0);
            double d0 = (double)j * Math.PI * 2.0D / (double)this.legs.length;
            float f = (float)Math.cos(d0) * 5.0F;
            float f1 = (float)Math.sin(d0) * 5.0F;
            this.legs[j].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
            this.legs[j].rotationPointX = f;
            this.legs[j].rotationPointZ = f1;
            this.legs[j].rotationPointY = 15.0F;
            d0 = (double)j * Math.PI * -2.0D / (double)this.legs.length + (Math.PI / 2D);
            this.legs[j].rotateAngleY = (float)d0;
        }
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        for (ModelRenderer modelrenderer : this.legs)
        {
            modelrenderer.rotateAngleX = ageInTicks;
        }
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.body.render(scale);

        for (ModelRenderer modelrenderer : this.legs)
        {
            modelrenderer.render(scale);
        }
    }
}
