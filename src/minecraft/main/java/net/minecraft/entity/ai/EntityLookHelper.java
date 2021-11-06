package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class EntityLookHelper
{
    private final EntityLiving mob;
    private float deltaLookYaw;
    private float deltaLookPitch;
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;

    public EntityLookHelper(EntityLiving mob)
    {
        this.mob = mob;
    }

    /**
     * Sets position to look at using entity
     */
    public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch)
    {
        this.posX = entityIn.posX;

        if (entityIn instanceof EntityLivingBase)
        {
            this.posY = entityIn.posY + (double)entityIn.getEyeHeight();
        }
        else
        {
            this.posY = (entityIn.getBoundingBox().minY + entityIn.getBoundingBox().maxY) / 2.0D;
        }

        this.posZ = entityIn.posZ;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }

    /**
     * Sets position to look at
     */
    public void setLookPosition(double x, double y, double z, float deltaYaw, float deltaPitch)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }

    /**
     * Updates look
     */
    public void tick()
    {
        this.mob.rotationPitch = 0.0F;

        if (this.isLooking)
        {
            this.isLooking = false;
            double d0 = this.posX - this.mob.posX;
            double d1 = this.posY - (this.mob.posY + (double)this.mob.getEyeHeight());
            double d2 = this.posZ - this.mob.posZ;
            double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float f1 = (float)(-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
            this.mob.rotationPitch = this.updateRotation(this.mob.rotationPitch, f1, this.deltaLookPitch);
            this.mob.rotationYawHead = this.updateRotation(this.mob.rotationYawHead, f, this.deltaLookYaw);
        }
        else
        {
            this.mob.rotationYawHead = this.updateRotation(this.mob.rotationYawHead, this.mob.renderYawOffset, 10.0F);
        }

        float f2 = MathHelper.wrapDegrees(this.mob.rotationYawHead - this.mob.renderYawOffset);

        if (!this.mob.getNavigator().noPath())
        {
            if (f2 < -75.0F)
            {
                this.mob.rotationYawHead = this.mob.renderYawOffset - 75.0F;
            }

            if (f2 > 75.0F)
            {
                this.mob.rotationYawHead = this.mob.renderYawOffset + 75.0F;
            }
        }
    }

    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_)
    {
        float f = MathHelper.wrapDegrees(p_75652_2_ - p_75652_1_);

        if (f > p_75652_3_)
        {
            f = p_75652_3_;
        }

        if (f < -p_75652_3_)
        {
            f = -p_75652_3_;
        }

        return p_75652_1_ + f;
    }

    public boolean getIsLooking()
    {
        return this.isLooking;
    }

    public double getLookPosX()
    {
        return this.posX;
    }

    public double getLookPosY()
    {
        return this.posY;
    }

    public double getLookPosZ()
    {
        return this.posZ;
    }
}
