package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class RedundantChanceTags implements IFixableData
{
    public int getFixVersion()
    {
        return 113;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if (compound.contains("HandDropChances", 9))
        {
            NBTTagList nbttaglist = compound.getList("HandDropChances", 5);

            if (nbttaglist.tagCount() == 2 && nbttaglist.getFloat(0) == 0.0F && nbttaglist.getFloat(1) == 0.0F)
            {
                compound.remove("HandDropChances");
            }
        }

        if (compound.contains("ArmorDropChances", 9))
        {
            NBTTagList nbttaglist1 = compound.getList("ArmorDropChances", 5);

            if (nbttaglist1.tagCount() == 4 && nbttaglist1.getFloat(0) == 0.0F && nbttaglist1.getFloat(1) == 0.0F && nbttaglist1.getFloat(2) == 0.0F && nbttaglist1.getFloat(3) == 0.0F)
            {
                compound.remove("ArmorDropChances");
            }
        }

        return compound;
    }
}
