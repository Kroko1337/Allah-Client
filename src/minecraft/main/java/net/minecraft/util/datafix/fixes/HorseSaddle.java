package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSaddle implements IFixableData
{
    public int getFixVersion()
    {
        return 110;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("EntityHorse".equals(compound.getString("id")) && !compound.contains("SaddleItem", 10) && compound.getBoolean("Saddle"))
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.putString("id", "minecraft:saddle");
            nbttagcompound.putByte("Count", (byte)1);
            nbttagcompound.putShort("Damage", (short)0);
            compound.setTag("SaddleItem", nbttagcompound);
            compound.remove("Saddle");
        }

        return compound;
    }
}
