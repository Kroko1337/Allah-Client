package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class SkeletonSplit implements IFixableData
{
    public int getFixVersion()
    {
        return 701;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        String s = compound.getString("id");

        if ("Skeleton".equals(s))
        {
            int i = compound.getInt("SkeletonType");

            if (i == 1)
            {
                compound.putString("id", "WitherSkeleton");
            }
            else if (i == 2)
            {
                compound.putString("id", "Stray");
            }

            compound.remove("SkeletonType");
        }

        return compound;
    }
}
