package net.minecraft.entity;

import net.minecraft.util.math.MathHelper;

public class EntityBodyHelper
{
    private final EntityLivingBase mob;
    private int rotationTickCounter;
    private float prevRenderYawHead;

    public EntityBodyHelper(EntityLivingBase livingIn)
    {
        this.mob = livingIn;
    }

    /**
     * Update the Head and Body rendenring angles
     */
    public void updateRenderAngles()
    {
        double d0 = this.mob.posX - this.mob.prevPosX;
        double d1 = this.mob.posZ - this.mob.prevPosZ;

        if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D)
        {
            this.mob.renderYawOffset = this.mob.rotationYaw;
            this.mob.rotationYawHead = this.computeAngleWithBound(this.mob.renderYawOffset, this.mob.rotationYawHead, 75.0F);
            this.prevRenderYawHead = this.mob.rotationYawHead;
            this.rotationTickCounter = 0;
        }
        else
        {
            if (this.mob.getPassengers().isEmpty() || !(this.mob.getPassengers().get(0) instanceof EntityLiving))
            {
                float f = 75.0F;

                if (Math.abs(this.mob.rotationYawHead - this.prevRenderYawHead) > 15.0F)
                {
                    this.rotationTickCounter = 0;
                    this.prevRenderYawHead = this.mob.rotationYawHead;
                }
                else
                {
                    ++this.rotationTickCounter;
                    int i = 10;

                    if (this.rotationTickCounter > 10)
                    {
                        f = Math.max(1.0F - (float)(this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
                    }
                }

                this.mob.renderYawOffset = this.computeAngleWithBound(this.mob.rotationYawHead, this.mob.renderYawOffset, f);
            }
        }
    }

    private float computeAngleWithBound(float p_75665_1_, float p_75665_2_, float p_75665_3_)
    {
        float f = MathHelper.wrapDegrees(p_75665_1_ - p_75665_2_);

        if (f < -p_75665_3_)
        {
            f = -p_75665_3_;
        }

        if (f >= p_75665_3_)
        {
            f = p_75665_3_;
        }

        return p_75665_1_ - f;
    }
}
