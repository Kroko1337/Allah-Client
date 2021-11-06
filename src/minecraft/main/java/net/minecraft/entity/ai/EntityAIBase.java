package net.minecraft.entity.ai;

public abstract class EntityAIBase
{
    private int mutexBits;

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public abstract boolean shouldExecute();

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute();
    }

    public boolean isInterruptible()
    {
        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick()
    {
    }

    public void setMutexBits(int mutexBitsIn)
    {
        this.mutexBits = mutexBitsIn;
    }

    public int getMutexBits()
    {
        return this.mutexBits;
    }
}
