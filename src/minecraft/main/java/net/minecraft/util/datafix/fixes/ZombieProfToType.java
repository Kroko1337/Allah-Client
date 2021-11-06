package net.minecraft.util.datafix.fixes;

import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType implements IFixableData
{
    private static final Random RANDOM = new Random();

    public int getFixVersion()
    {
        return 502;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("Zombie".equals(compound.getString("id")) && compound.getBoolean("IsVillager"))
        {
            if (!compound.contains("ZombieType", 99))
            {
                int i = -1;

                if (compound.contains("VillagerProfession", 99))
                {
                    try
                    {
                        i = this.getVillagerProfession(compound.getInt("VillagerProfession"));
                    }
                    catch (RuntimeException var4)
                    {
                        ;
                    }
                }

                if (i == -1)
                {
                    i = this.getVillagerProfession(RANDOM.nextInt(6));
                }

                compound.putInt("ZombieType", i);
            }

            compound.remove("IsVillager");
        }

        return compound;
    }

    private int getVillagerProfession(int p_191277_1_)
    {
        return p_191277_1_ >= 0 && p_191277_1_ < 6 ? p_191277_1_ : -1;
    }
}
