package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class BannerItemColor implements IFixableData
{
    public int getFixVersion()
    {
        return 804;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("minecraft:banner".equals(compound.getString("id")) && compound.contains("tag", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompound("tag");

            if (nbttagcompound.contains("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");

                if (nbttagcompound1.contains("Base", 99))
                {
                    compound.putShort("Damage", (short)(nbttagcompound1.getShort("Base") & 15));

                    if (nbttagcompound.contains("display", 10))
                    {
                        NBTTagCompound nbttagcompound2 = nbttagcompound.getCompound("display");

                        if (nbttagcompound2.contains("Lore", 9))
                        {
                            NBTTagList nbttaglist = nbttagcompound2.getList("Lore", 8);

                            if (nbttaglist.tagCount() == 1 && "(+NBT)".equals(nbttaglist.getString(0)))
                            {
                                return compound;
                            }
                        }
                    }

                    nbttagcompound1.remove("Base");

                    if (nbttagcompound1.isEmpty())
                    {
                        nbttagcompound.remove("BlockEntityTag");
                    }

                    if (nbttagcompound.isEmpty())
                    {
                        compound.remove("tag");
                    }
                }
            }
        }

        return compound;
    }
}
