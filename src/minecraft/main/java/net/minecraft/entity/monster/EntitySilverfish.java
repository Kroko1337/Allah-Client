package net.minecraft.entity.monster;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySilverfish extends EntityMob
{
    private EntitySilverfish.AISummonSilverfish summonSilverfish;

    public EntitySilverfish(World worldIn)
    {
        super(worldIn);
        this.setSize(0.4F, 0.3F);
    }

    public static void registerFixesSilverfish(DataFixer fixer)
    {
        EntityLiving.registerFixesMob(fixer, EntitySilverfish.class);
    }

    protected void registerGoals()
    {
        this.summonSilverfish = new EntitySilverfish.AISummonSilverfish(this);
        this.goalSelector.addGoal(1, new EntityAISwimming(this));
        this.goalSelector.addGoal(3, this.summonSilverfish);
        this.goalSelector.addGoal(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.goalSelector.addGoal(5, new EntitySilverfish.AIHideInStone(this));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetSelector.addGoal(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return 0.1D;
    }

    public float getEyeHeight()
    {
        return 0.1F;
    }

    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SILVERFISH_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SILVERFISH_DEATH;
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isInvulnerableTo(source))
        {
            return false;
        }
        else
        {
            if ((source instanceof EntityDamageSource || source == DamageSource.MAGIC) && this.summonSilverfish != null)
            {
                this.summonSilverfish.notifyHurt();
            }

            return super.attackEntityFrom(source, amount);
        }
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_SILVERFISH;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick()
    {
        this.renderYawOffset = this.rotationYaw;
        super.tick();
    }

    /**
     * Set the render yaw offset
     */
    public void setRenderYawOffset(float offset)
    {
        this.rotationYaw = offset;
        super.setRenderYawOffset(offset);
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return this.world.getBlockState(pos.down()).getBlock() == Blocks.STONE ? 10.0F : super.getBlockPathWeight(pos);
    }

    protected boolean isValidLightLevel()
    {
        return true;
    }

    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            EntityPlayer entityplayer = this.world.getNearestPlayerNotCreative(this, 5.0D);
            return entityplayer == null;
        }
        else
        {
            return false;
        }
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    static class AIHideInStone extends EntityAIWander
    {
        private EnumFacing facing;
        private boolean doMerge;

        public AIHideInStone(EntitySilverfish silverfishIn)
        {
            super(silverfishIn, 1.0D, 10);
            this.setMutexBits(1);
        }

        public boolean shouldExecute()
        {
            if (this.creature.getAttackTarget() != null)
            {
                return false;
            }
            else if (!this.creature.getNavigator().noPath())
            {
                return false;
            }
            else
            {
                Random random = this.creature.getRNG();

                if (this.creature.world.getGameRules().getBoolean("mobGriefing") && random.nextInt(10) == 0)
                {
                    this.facing = EnumFacing.random(random);
                    BlockPos blockpos = (new BlockPos(this.creature.posX, this.creature.posY + 0.5D, this.creature.posZ)).offset(this.facing);
                    IBlockState iblockstate = this.creature.world.getBlockState(blockpos);

                    if (BlockSilverfish.canContainSilverfish(iblockstate))
                    {
                        this.doMerge = true;
                        return true;
                    }
                }

                this.doMerge = false;
                return super.shouldExecute();
            }
        }

        public boolean shouldContinueExecuting()
        {
            return this.doMerge ? false : super.shouldContinueExecuting();
        }

        public void startExecuting()
        {
            if (!this.doMerge)
            {
                super.startExecuting();
            }
            else
            {
                World world = this.creature.world;
                BlockPos blockpos = (new BlockPos(this.creature.posX, this.creature.posY + 0.5D, this.creature.posZ)).offset(this.facing);
                IBlockState iblockstate = world.getBlockState(blockpos);

                if (BlockSilverfish.canContainSilverfish(iblockstate))
                {
                    world.setBlockState(blockpos, Blocks.MONSTER_EGG.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
                    this.creature.spawnExplosionParticle();
                    this.creature.remove();
                }
            }
        }
    }

    static class AISummonSilverfish extends EntityAIBase
    {
        private final EntitySilverfish silverfish;
        private int lookForFriends;

        public AISummonSilverfish(EntitySilverfish silverfishIn)
        {
            this.silverfish = silverfishIn;
        }

        public void notifyHurt()
        {
            if (this.lookForFriends == 0)
            {
                this.lookForFriends = 20;
            }
        }

        public boolean shouldExecute()
        {
            return this.lookForFriends > 0;
        }

        public void tick()
        {
            --this.lookForFriends;

            if (this.lookForFriends <= 0)
            {
                World world = this.silverfish.world;
                Random random = this.silverfish.getRNG();
                BlockPos blockpos = new BlockPos(this.silverfish);

                for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i)
                {
                    for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j)
                    {
                        for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k)
                        {
                            BlockPos blockpos1 = blockpos.add(j, i, k);
                            IBlockState iblockstate = world.getBlockState(blockpos1);

                            if (iblockstate.getBlock() == Blocks.MONSTER_EGG)
                            {
                                if (world.getGameRules().getBoolean("mobGriefing"))
                                {
                                    world.destroyBlock(blockpos1, true);
                                }
                                else
                                {
                                    world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.get(BlockSilverfish.VARIANT)).getModelBlock(), 3);
                                }

                                if (random.nextBoolean())
                                {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
