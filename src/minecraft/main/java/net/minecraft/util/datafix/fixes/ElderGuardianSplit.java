package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ElderGuardianSplit implements IFixableData
{
    public int getFixVersion()
    {
        return 700;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("Guardian".equals(compound.getString("id")))
        {
            if (compound.getBoolean("Elder"))
            {
                compound.putString("id", "ElderGuardian");
            }

            compound.remove("Elder");
        }

        return compound;
    }
}
