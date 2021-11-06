package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;

public class EntityFlyHelper extends EntityMoveHelper
{
    public EntityFlyHelper(EntityLiving p_i47418_1_)
    {
        super(p_i47418_1_);
    }

    public void tick()
    {
        if (this.action == EntityMoveHelper.Action.MOVE_TO)
        {
            this.action = EntityMoveHelper.Action.WAIT;
            this.mob.setNoGravity(true);
            double d0 = this.posX - this.mob.posX;
            double d1 = this.posY - this.mob.posY;
            double d2 = this.posZ - this.mob.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d3 < 2.500000277905201E-7D)
            {
                this.mob.setMoveVertical(0.0F);
                this.mob.setMoveForward(0.0F);
                return;
            }

            float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f, 10.0F);
            float f1;

            if (this.mob.onGround)
            {
                f1 = (float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
            }
            else
            {
                f1 = (float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.FLYING_SPEED).getValue());
            }

            this.mob.setAIMoveSpeed(f1);
            double d4 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f2 = (float)(-(MathHelper.atan2(d1, d4) * (180D / Math.PI)));
            this.mob.rotationPitch = this.limitAngle(this.mob.rotationPitch, f2, 10.0F);
            this.mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
        }
        else
        {
            this.mob.setNoGravity(false);
            this.mob.setMoveVertical(0.0F);
            this.mob.setMoveForward(0.0F);
        }
    }
}
