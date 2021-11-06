package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class HorseSplit implements IFixableData
{
    public int getFixVersion()
    {
        return 703;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("EntityHorse".equals(compound.getString("id")))
        {
            int i = compound.getInt("Type");

            switch (i)
            {
                case 0:
                default:
                    compound.putString("id", "Horse");
                    break;

                case 1:
                    compound.putString("id", "Donkey");
                    break;

                case 2:
                    compound.putString("id", "Mule");
                    break;

                case 3:
                    compound.putString("id", "ZombieHorse");
                    break;

                case 4:
                    compound.putString("id", "SkeletonHorse");
            }

            compound.remove("Type");
        }

        return compound;
    }
}
