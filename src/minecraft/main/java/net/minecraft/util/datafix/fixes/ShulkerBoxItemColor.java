package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ShulkerBoxItemColor implements IFixableData
{
    public static final String[] NAMES_BY_COLOR = new String[] {"minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box"};

    public int getFixVersion()
    {
        return 813;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        if ("minecraft:shulker_box".equals(compound.getString("id")) && compound.contains("tag", 10))
        {
            NBTTagCompound nbttagcompound = compound.getCompound("tag");

            if (nbttagcompound.contains("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("BlockEntityTag");

                if (nbttagcompound1.getList("Items", 10).isEmpty())
                {
                    nbttagcompound1.remove("Items");
                }

                int i = nbttagcompound1.getInt("Color");
                nbttagcompound1.remove("Color");

                if (nbttagcompound1.isEmpty())
                {
                    nbttagcompound.remove("BlockEntityTag");
                }

                if (nbttagcompound.isEmpty())
                {
                    compound.remove("tag");
                }

                compound.putString("id", NAMES_BY_COLOR[i % 16]);
            }
        }

        return compound;
    }
}
