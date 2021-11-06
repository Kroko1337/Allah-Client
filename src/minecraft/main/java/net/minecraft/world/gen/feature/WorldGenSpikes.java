package net.minecraft.world.gen.feature;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator
{
    private boolean crystalInvulnerable;
    private WorldGenSpikes.EndSpike spike;
    private BlockPos beamTarget;

    public void setSpike(WorldGenSpikes.EndSpike p_186143_1_)
    {
        this.spike = p_186143_1_;
    }

    public void setCrystalInvulnerable(boolean p_186144_1_)
    {
        this.crystalInvulnerable = p_186144_1_;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        if (this.spike == null)
        {
            throw new IllegalStateException("Decoration requires priming with a spike");
        }
        else
        {
            int i = this.spike.getRadius();

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(new BlockPos(position.getX() - i, 0, position.getZ() - i), new BlockPos(position.getX() + i, this.spike.getHeight() + 10, position.getZ() + i)))
            {
                if (blockpos$mutableblockpos.distanceSq((double)position.getX(), (double)blockpos$mutableblockpos.getY(), (double)position.getZ()) <= (double)(i * i + 1) && blockpos$mutableblockpos.getY() < this.spike.getHeight())
                {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.OBSIDIAN.getDefaultState());
                }
                else if (blockpos$mutableblockpos.getY() > 65)
                {
                    this.setBlockAndNotifyAdequately(worldIn, blockpos$mutableblockpos, Blocks.AIR.getDefaultState());
                }
            }

            if (this.spike.isGuarded())
            {
                for (int j = -2; j <= 2; ++j)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        if (MathHelper.abs(j) == 2 || MathHelper.abs(k) == 2)
                        {
                            this.setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, this.spike.getHeight(), position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            this.setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, this.spike.getHeight() + 1, position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                            this.setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, this.spike.getHeight() + 2, position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                        }

                        this.setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX() + j, this.spike.getHeight() + 3, position.getZ() + k), Blocks.IRON_BARS.getDefaultState());
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(worldIn);
            entityendercrystal.setBeamTarget(this.beamTarget);
            entityendercrystal.setInvulnerable(this.crystalInvulnerable);
            entityendercrystal.setLocationAndAngles((double)((float)position.getX() + 0.5F), (double)(this.spike.getHeight() + 1), (double)((float)position.getZ() + 0.5F), rand.nextFloat() * 360.0F, 0.0F);
            worldIn.addEntity0(entityendercrystal);
            this.setBlockAndNotifyAdequately(worldIn, new BlockPos(position.getX(), this.spike.getHeight(), position.getZ()), Blocks.BEDROCK.getDefaultState());
            return true;
        }
    }

    public void setBeamTarget(@Nullable BlockPos pos)
    {
        this.beamTarget = pos;
    }

    public static class EndSpike
    {
        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final int height;
        private final boolean guarded;
        private final AxisAlignedBB topBoundingBox;

        public EndSpike(int centerXIn, int centerZIn, int radiusIn, int heightIn, boolean guardedIn)
        {
            this.centerX = centerXIn;
            this.centerZ = centerZIn;
            this.radius = radiusIn;
            this.height = heightIn;
            this.guarded = guardedIn;
            this.topBoundingBox = new AxisAlignedBB((double)(centerXIn - radiusIn), 0.0D, (double)(centerZIn - radiusIn), (double)(centerXIn + radiusIn), 256.0D, (double)(centerZIn + radiusIn));
        }

        public boolean doesStartInChunk(BlockPos pos)
        {
            int i = this.centerX - this.radius;
            int j = this.centerZ - this.radius;
            return pos.getX() == (i & -16) && pos.getZ() == (j & -16);
        }

        public int getCenterX()
        {
            return this.centerX;
        }

        public int getCenterZ()
        {
            return this.centerZ;
        }

        public int getRadius()
        {
            return this.radius;
        }

        public int getHeight()
        {
            return this.height;
        }

        public boolean isGuarded()
        {
            return this.guarded;
        }

        public AxisAlignedBB getTopBoundingBox()
        {
            return this.topBoundingBox;
        }
    }
}
