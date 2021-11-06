package net.minecraft.world.chunk.storage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.NibbleArray;

public class ExtendedBlockStorage
{
    private final int yBase;
    private int blockRefCount;
    private int blockTickRefCount;
    private final BlockStateContainer data;
    private NibbleArray blockLight;
    private NibbleArray skyLight;

    public ExtendedBlockStorage(int y, boolean storeSkylight)
    {
        this.yBase = y;
        this.data = new BlockStateContainer();
        this.blockLight = new NibbleArray();

        if (storeSkylight)
        {
            this.skyLight = new NibbleArray();
        }
    }

    public IBlockState getBlockState(int x, int y, int z)
    {
        return this.data.get(x, y, z);
    }

    public void setBlockState(int x, int y, int z, IBlockState state)
    {
        IBlockState iblockstate = this.getBlockState(x, y, z);
        Block block = iblockstate.getBlock();
        Block block1 = state.getBlock();

        if (block != Blocks.AIR)
        {
            --this.blockRefCount;

            if (block.ticksRandomly())
            {
                --this.blockTickRefCount;
            }
        }

        if (block1 != Blocks.AIR)
        {
            ++this.blockRefCount;

            if (block1.ticksRandomly())
            {
                ++this.blockTickRefCount;
            }
        }

        this.data.set(x, y, z, state);
    }

    /**
     * Returns whether or not this block storage's Chunk is fully empty, based on its internal reference count.
     */
    public boolean isEmpty()
    {
        return this.blockRefCount == 0;
    }

    /**
     * Returns whether or not this block storage's Chunk will require random ticking, used to avoid looping through
     * random block ticks when there are no blocks that would randomly tick.
     */
    public boolean needsRandomTick()
    {
        return this.blockTickRefCount > 0;
    }

    public int getYLocation()
    {
        return this.yBase;
    }

    public void setSkyLight(int x, int y, int z, int value)
    {
        this.skyLight.set(x, y, z, value);
    }

    public int getSkyLight(int x, int y, int z)
    {
        return this.skyLight.get(x, y, z);
    }

    public void setBlockLight(int x, int y, int z, int value)
    {
        this.blockLight.set(x, y, z, value);
    }

    public int getBlockLight(int x, int y, int z)
    {
        return this.blockLight.get(x, y, z);
    }

    public void recalculateRefCounts()
    {
        this.blockRefCount = 0;
        this.blockTickRefCount = 0;

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                for (int k = 0; k < 16; ++k)
                {
                    Block block = this.getBlockState(i, j, k).getBlock();

                    if (block != Blocks.AIR)
                    {
                        ++this.blockRefCount;

                        if (block.ticksRandomly())
                        {
                            ++this.blockTickRefCount;
                        }
                    }
                }
            }
        }
    }

    public BlockStateContainer getData()
    {
        return this.data;
    }

    public NibbleArray getBlockLight()
    {
        return this.blockLight;
    }

    public NibbleArray getSkyLight()
    {
        return this.skyLight;
    }

    public void setBlockLight(NibbleArray newBlocklightArray)
    {
        this.blockLight = newBlocklightArray;
    }

    public void setSkyLight(NibbleArray newSkylightArray)
    {
        this.skyLight = newSkylightArray;
    }
}
