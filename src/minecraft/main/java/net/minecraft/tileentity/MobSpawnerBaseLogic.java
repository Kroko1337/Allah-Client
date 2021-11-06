package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public abstract class MobSpawnerBaseLogic
{
    private int spawnDelay = 20;
    private final List<WeightedSpawnerEntity> potentialSpawns = Lists.<WeightedSpawnerEntity>newArrayList();
    private WeightedSpawnerEntity spawnData = new WeightedSpawnerEntity();
    private double mobRotation;
    private double prevMobRotation;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;

    /** Cached instance of the entity to render inside the spawner. */
    private Entity cachedEntity;
    private int maxNearbyEntities = 6;
    private int activatingRangeFromPlayer = 16;
    private int spawnRange = 4;

    @Nullable
    private ResourceLocation getEntityId()
    {
        String s = this.spawnData.getNbt().getString("id");
        return StringUtils.isNullOrEmpty(s) ? null : new ResourceLocation(s);
    }

    public void setEntityId(@Nullable ResourceLocation id)
    {
        if (id != null)
        {
            this.spawnData.getNbt().putString("id", id.toString());
        }
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    private boolean isActivated()
    {
        BlockPos blockpos = this.getSpawnerPosition();
        return this.getWorld().isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, (double)this.activatingRangeFromPlayer);
    }

    public void tick()
    {
        if (!this.isActivated())
        {
            this.prevMobRotation = this.mobRotation;
        }
        else
        {
            BlockPos blockpos = this.getSpawnerPosition();

            if (this.getWorld().isRemote)
            {
                double d3 = (double)((float)blockpos.getX() + this.getWorld().rand.nextFloat());
                double d4 = (double)((float)blockpos.getY() + this.getWorld().rand.nextFloat());
                double d5 = (double)((float)blockpos.getZ() + this.getWorld().rand.nextFloat());
                this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                this.getWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
            }
            else
            {
                if (this.spawnDelay == -1)
                {
                    this.resetTimer();
                }

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i)
                {
                    NBTTagCompound nbttagcompound = this.spawnData.getNbt();
                    NBTTagList nbttaglist = nbttagcompound.getList("Pos", 6);
                    World world = this.getWorld();
                    int j = nbttaglist.tagCount();
                    double d0 = j >= 1 ? nbttaglist.getDouble(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d1 = j >= 2 ? nbttaglist.getDouble(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1);
                    double d2 = j >= 3 ? nbttaglist.getDouble(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);

                    if (entity == null)
                    {
                        return;
                    }

                    int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).grow((double)this.spawnRange)).size();

                    if (k >= this.maxNearbyEntities)
                    {
                        this.resetTimer();
                        return;
                    }

                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || entityliving.getCanSpawnHere() && entityliving.isNotColliding())
                    {
                        if (this.spawnData.getNbt().size() == 1 && this.spawnData.getNbt().contains("id", 8) && entity instanceof EntityLiving)
                        {
                            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                        }

                        AnvilChunkLoader.spawnEntity(entity, world);
                        world.playEvent(2004, blockpos, 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag)
                {
                    this.resetTimer();
                }
            }
        }
    }

    private void resetTimer()
    {
        if (this.maxSpawnDelay <= this.minSpawnDelay)
        {
            this.spawnDelay = this.minSpawnDelay;
        }
        else
        {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getWorld().rand.nextInt(i);
        }

        if (!this.potentialSpawns.isEmpty())
        {
            this.setNextSpawnData((WeightedSpawnerEntity)WeightedRandom.getRandomItem(this.getWorld().rand, this.potentialSpawns));
        }

        this.broadcastEvent(1);
    }

    public void read(NBTTagCompound nbt)
    {
        this.spawnDelay = nbt.getShort("Delay");
        this.potentialSpawns.clear();

        if (nbt.contains("SpawnPotentials", 9))
        {
            NBTTagList nbttaglist = nbt.getList("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.potentialSpawns.add(new WeightedSpawnerEntity(nbttaglist.getCompound(i)));
            }
        }

        if (nbt.contains("SpawnData", 10))
        {
            this.setNextSpawnData(new WeightedSpawnerEntity(1, nbt.getCompound("SpawnData")));
        }
        else if (!this.potentialSpawns.isEmpty())
        {
            this.setNextSpawnData((WeightedSpawnerEntity)WeightedRandom.getRandomItem(this.getWorld().rand, this.potentialSpawns));
        }

        if (nbt.contains("MinSpawnDelay", 99))
        {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }

        if (nbt.contains("MaxNearbyEntities", 99))
        {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }

        if (nbt.contains("SpawnRange", 99))
        {
            this.spawnRange = nbt.getShort("SpawnRange");
        }

        if (this.getWorld() != null)
        {
            this.cachedEntity = null;
        }
    }

    public NBTTagCompound write(NBTTagCompound compound)
    {
        ResourceLocation resourcelocation = this.getEntityId();

        if (resourcelocation == null)
        {
            return compound;
        }
        else
        {
            compound.putShort("Delay", (short)this.spawnDelay);
            compound.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
            compound.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
            compound.putShort("SpawnCount", (short)this.spawnCount);
            compound.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
            compound.putShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
            compound.putShort("SpawnRange", (short)this.spawnRange);
            compound.setTag("SpawnData", this.spawnData.getNbt().copy());
            NBTTagList nbttaglist = new NBTTagList();

            if (this.potentialSpawns.isEmpty())
            {
                nbttaglist.appendTag(this.spawnData.toCompoundTag());
            }
            else
            {
                for (WeightedSpawnerEntity weightedspawnerentity : this.potentialSpawns)
                {
                    nbttaglist.appendTag(weightedspawnerentity.toCompoundTag());
                }
            }

            compound.setTag("SpawnPotentials", nbttaglist);
            return compound;
        }
    }

    public Entity getCachedEntity()
    {
        if (this.cachedEntity == null)
        {
            this.cachedEntity = AnvilChunkLoader.readWorldEntity(this.spawnData.getNbt(), this.getWorld(), false);

            if (this.spawnData.getNbt().size() == 1 && this.spawnData.getNbt().contains("id", 8) && this.cachedEntity instanceof EntityLiving)
            {
                ((EntityLiving)this.cachedEntity).onInitialSpawn(this.getWorld().getDifficultyForLocation(new BlockPos(this.cachedEntity)), (IEntityLivingData)null);
            }
        }

        return this.cachedEntity;
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    public boolean setDelayToMin(int delay)
    {
        if (delay == 1 && this.getWorld().isRemote)
        {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setNextSpawnData(WeightedSpawnerEntity nextSpawnData)
    {
        this.spawnData = nextSpawnData;
    }

    public abstract void broadcastEvent(int id);

    public abstract World getWorld();

    public abstract BlockPos getSpawnerPosition();

    public double getMobRotation()
    {
        return this.mobRotation;
    }

    public double getPrevMobRotation()
    {
        return this.prevMobRotation;
    }
}
