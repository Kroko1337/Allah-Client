package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxEntityColor implements IFixableData
{
    public int getFixVersion()
    {
        return 808;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("minecraft:shulker".equals(compound.getString("id")) && !compound.contains("Color", 99))
        {
            compound.putByte("Color", (byte)10);
        }

        return compound;
    }
}
