package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieSplit implements IFixableData
{
    public int getFixVersion()
    {
        return 702;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("Zombie".equals(compound.getString("id")))
        {
            int i = compound.getInt("ZombieType");

            switch (i)
            {
                case 0:
                default:
                    break;

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    compound.putString("id", "ZombieVillager");
                    compound.putInt("Profession", i - 1);
                    break;

                case 6:
                    compound.putString("id", "Husk");
            }

            compound.remove("ZombieType");
        }

        return compound;
    }
}
