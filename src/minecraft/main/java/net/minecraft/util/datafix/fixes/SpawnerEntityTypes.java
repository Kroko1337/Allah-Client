package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class SpawnerEntityTypes implements IFixableData
{
    public int getFixVersion()
    {
        return 107;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if (!"MobSpawner".equals(compound.getString("id")))
        {
            return compound;
        }
        else
        {
            if (compound.contains("EntityId", 8))
            {
                String s = compound.getString("EntityId");
                NBTTagCompound nbttagcompound = compound.getCompound("SpawnData");
                nbttagcompound.putString("id", s.isEmpty() ? "Pig" : s);
                compound.setTag("SpawnData", nbttagcompound);
                compound.remove("EntityId");
            }

            if (compound.contains("SpawnPotentials", 9))
            {
                NBTTagList nbttaglist = compound.getList("SpawnPotentials", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);

                    if (nbttagcompound1.contains("Type", 8))
                    {
                        NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompound("Properties");
                        nbttagcompound2.putString("id", nbttagcompound1.getString("Type"));
                        nbttagcompound1.setTag("Entity", nbttagcompound2);
                        nbttagcompound1.remove("Type");
                        nbttagcompound1.remove("Properties");
                    }
                }
            }

            return compound;
        }
    }
}
