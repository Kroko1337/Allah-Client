package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.IFixableData;

public class PaintingDirection implements IFixableData
{
    public int getFixVersion()
    {
        return 111;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        String s = compound.getString("id");
        boolean flag = "Painting".equals(s);
        boolean flag1 = "ItemFrame".equals(s);

        if ((flag || flag1) && !compound.contains("Facing", 99))
        {
            EnumFacing enumfacing;

            if (compound.contains("Direction", 99))
            {
                enumfacing = EnumFacing.byHorizontalIndex(compound.getByte("Direction"));
                compound.putInt("TileX", compound.getInt("TileX") + enumfacing.getXOffset());
                compound.putInt("TileY", compound.getInt("TileY") + enumfacing.getYOffset());
                compound.putInt("TileZ", compound.getInt("TileZ") + enumfacing.getZOffset());
                compound.remove("Direction");

                if (flag1 && compound.contains("ItemRotation", 99))
                {
                    compound.putByte("ItemRotation", (byte)(compound.getByte("ItemRotation") * 2));
                }
            }
            else
            {
                enumfacing = EnumFacing.byHorizontalIndex(compound.getByte("Dir"));
                compound.remove("Dir");
            }

            compound.putByte("Facing", (byte)enumfacing.getHorizontalIndex());
        }

        return compound;
    }
}
