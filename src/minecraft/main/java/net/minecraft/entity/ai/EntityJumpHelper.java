package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper
{
    private final EntityLiving mob;
    protected boolean isJumping;

    public EntityJumpHelper(EntityLiving mob)
    {
        this.mob = mob;
    }

    public void setJumping()
    {
        this.isJumping = true;
    }

    /**
     * Called to actually make the entity jump if isJumping is true.
     */
    public void tick()
    {
        this.mob.setJumping(this.isJumping);
        this.isJumping = false;
    }
}
