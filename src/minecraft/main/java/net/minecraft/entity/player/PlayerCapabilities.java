package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities
{
    public boolean disableDamage;
    public boolean isFlying;
    public boolean allowFlying;
    public boolean isCreativeMode;
    public boolean allowEdit = true;
    private float flySpeed = 0.05F;
    private float walkSpeed = 0.1F;

    public void write(NBTTagCompound tagCompound)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putBoolean("invulnerable", this.disableDamage);
        nbttagcompound.putBoolean("flying", this.isFlying);
        nbttagcompound.putBoolean("mayfly", this.allowFlying);
        nbttagcompound.putBoolean("instabuild", this.isCreativeMode);
        nbttagcompound.putBoolean("mayBuild", this.allowEdit);
        nbttagcompound.putFloat("flySpeed", this.flySpeed);
        nbttagcompound.putFloat("walkSpeed", this.walkSpeed);
        tagCompound.setTag("abilities", nbttagcompound);
    }

    public void read(NBTTagCompound tagCompound)
    {
        if (tagCompound.contains("abilities", 10))
        {
            NBTTagCompound nbttagcompound = tagCompound.getCompound("abilities");
            this.disableDamage = nbttagcompound.getBoolean("invulnerable");
            this.isFlying = nbttagcompound.getBoolean("flying");
            this.allowFlying = nbttagcompound.getBoolean("mayfly");
            this.isCreativeMode = nbttagcompound.getBoolean("instabuild");

            if (nbttagcompound.contains("flySpeed", 99))
            {
                this.flySpeed = nbttagcompound.getFloat("flySpeed");
                this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
            }

            if (nbttagcompound.contains("mayBuild", 1))
            {
                this.allowEdit = nbttagcompound.getBoolean("mayBuild");
            }
        }
    }

    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    public void setFlySpeed(float speed)
    {
        this.flySpeed = speed;
    }

    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    public void setWalkSpeed(float speed)
    {
        this.walkSpeed = speed;
    }
}
