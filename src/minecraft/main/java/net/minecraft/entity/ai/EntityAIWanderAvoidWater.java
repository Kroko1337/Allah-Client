package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityAIWanderAvoidWater extends EntityAIWander
{
    protected final float probability;

    public EntityAIWanderAvoidWater(EntityCreature creature, double speedIn)
    {
        this(creature, speedIn, 0.001F);
    }

    public EntityAIWanderAvoidWater(EntityCreature creature, double speedIn, float probabilityIn)
    {
        super(creature, speedIn);
        this.probability = probabilityIn;
    }

    @Nullable
    protected Vec3d getPosition()
    {
        if (this.creature.isInWater())
        {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this.creature, 15, 7);
            return vec3d == null ? super.getPosition() : vec3d;
        }
        else
        {
            return this.creature.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.creature, 10, 7) : super.getPosition();
        }
    }
}
