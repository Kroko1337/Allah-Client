package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;

public class EntityArmorAndHeld implements IFixableData
{
    public int getFixVersion()
    {
        return 100;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = compound.getList("Equipment", 10);

        if (!nbttaglist.isEmpty() && !compound.contains("HandItems", 10))
        {
            NBTTagList nbttaglist1 = new NBTTagList();
            nbttaglist1.appendTag(nbttaglist.get(0));
            nbttaglist1.appendTag(new NBTTagCompound());
            compound.setTag("HandItems", nbttaglist1);
        }

        if (nbttaglist.tagCount() > 1 && !compound.contains("ArmorItem", 10))
        {
            NBTTagList nbttaglist3 = new NBTTagList();
            nbttaglist3.appendTag(nbttaglist.getCompound(1));
            nbttaglist3.appendTag(nbttaglist.getCompound(2));
            nbttaglist3.appendTag(nbttaglist.getCompound(3));
            nbttaglist3.appendTag(nbttaglist.getCompound(4));
            compound.setTag("ArmorItems", nbttaglist3);
        }

        compound.remove("Equipment");

        if (compound.contains("DropChances", 9))
        {
            NBTTagList nbttaglist4 = compound.getList("DropChances", 5);

            if (!compound.contains("HandDropChances", 10))
            {
                NBTTagList nbttaglist2 = new NBTTagList();
                nbttaglist2.appendTag(new NBTTagFloat(nbttaglist4.getFloat(0)));
                nbttaglist2.appendTag(new NBTTagFloat(0.0F));
                compound.setTag("HandDropChances", nbttaglist2);
            }

            if (!compound.contains("ArmorDropChances", 10))
            {
                NBTTagList nbttaglist5 = new NBTTagList();
                nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloat(1)));
                nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloat(2)));
                nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloat(3)));
                nbttaglist5.appendTag(new NBTTagFloat(nbttaglist4.getFloat(4)));
                compound.setTag("ArmorDropChances", nbttaglist5);
            }

            compound.remove("DropChances");
        }

        return compound;
    }
}
