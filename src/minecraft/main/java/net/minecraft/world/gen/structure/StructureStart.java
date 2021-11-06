package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public abstract class StructureStart
{
    protected List<StructureComponent> components = Lists.<StructureComponent>newLinkedList();
    protected StructureBoundingBox bounds;
    private int chunkPosX;
    private int chunkPosZ;

    public StructureStart()
    {
    }

    public StructureStart(int chunkX, int chunkZ)
    {
        this.chunkPosX = chunkX;
        this.chunkPosZ = chunkZ;
    }

    public StructureBoundingBox getBoundingBox()
    {
        return this.bounds;
    }

    public List<StructureComponent> getComponents()
    {
        return this.components;
    }

    public void generateStructure(World worldIn, Random rand, StructureBoundingBox structurebb)
    {
        Iterator<StructureComponent> iterator = this.components.iterator();

        while (iterator.hasNext())
        {
            StructureComponent structurecomponent = iterator.next();

            if (structurecomponent.getBoundingBox().intersectsWith(structurebb) && !structurecomponent.addComponentParts(worldIn, rand, structurebb))
            {
                iterator.remove();
            }
        }
    }

    protected void updateBoundingBox()
    {
        this.bounds = StructureBoundingBox.getNewBoundingBox();

        for (StructureComponent structurecomponent : this.components)
        {
            this.bounds.expandTo(structurecomponent.getBoundingBox());
        }
    }

    public NBTTagCompound write(int chunkX, int chunkZ)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putString("id", MapGenStructureIO.getStructureStartName(this));
        nbttagcompound.putInt("ChunkX", chunkX);
        nbttagcompound.putInt("ChunkZ", chunkZ);
        nbttagcompound.setTag("BB", this.bounds.toNBTTagIntArray());
        NBTTagList nbttaglist = new NBTTagList();

        for (StructureComponent structurecomponent : this.components)
        {
            nbttaglist.appendTag(structurecomponent.write());
        }

        nbttagcompound.setTag("Children", nbttaglist);
        this.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
    }

    public void readStructureComponentsFromNBT(World worldIn, NBTTagCompound tagCompound)
    {
        this.chunkPosX = tagCompound.getInt("ChunkX");
        this.chunkPosZ = tagCompound.getInt("ChunkZ");

        if (tagCompound.contains("BB"))
        {
            this.bounds = new StructureBoundingBox(tagCompound.getIntArray("BB"));
        }

        NBTTagList nbttaglist = tagCompound.getList("Children", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            this.components.add(MapGenStructureIO.getStructureComponent(nbttaglist.getCompound(i), worldIn));
        }

        this.readFromNBT(tagCompound);
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
    }

    protected void markAvailableHeight(World worldIn, Random rand, int p_75067_3_)
    {
        int i = worldIn.getSeaLevel() - p_75067_3_;
        int j = this.bounds.getYSize() + 1;

        if (j < i)
        {
            j += rand.nextInt(i - j);
        }

        int k = j - this.bounds.maxY;
        this.bounds.offset(0, k, 0);

        for (StructureComponent structurecomponent : this.components)
        {
            structurecomponent.offset(0, k, 0);
        }
    }

    protected void setRandomHeight(World worldIn, Random rand, int p_75070_3_, int p_75070_4_)
    {
        int i = p_75070_4_ - p_75070_3_ + 1 - this.bounds.getYSize();
        int j;

        if (i > 1)
        {
            j = p_75070_3_ + rand.nextInt(i);
        }
        else
        {
            j = p_75070_3_;
        }

        int k = j - this.bounds.minY;
        this.bounds.offset(0, k, 0);

        for (StructureComponent structurecomponent : this.components)
        {
            structurecomponent.offset(0, k, 0);
        }
    }

    /**
     * currently only defined for Villages, returns true if Village has more than 2 non-road components
     */
    public boolean isValid()
    {
        return true;
    }

    public boolean isValidForPostProcess(ChunkPos pair)
    {
        return true;
    }

    public void notifyPostProcessAt(ChunkPos pair)
    {
    }

    public int getChunkPosX()
    {
        return this.chunkPosX;
    }

    public int getChunkPosZ()
    {
        return this.chunkPosZ;
    }
}
