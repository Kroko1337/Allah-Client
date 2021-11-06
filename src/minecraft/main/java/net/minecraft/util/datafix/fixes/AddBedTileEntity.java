package net.minecraft.util.datafix.fixes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddBedTileEntity implements IFixableData
{
    private static final Logger LOGGER = LogManager.getLogger();

    public int getFixVersion()
    {
        return 1125;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound compound)
    {
        int i = 416;

        try
        {
            NBTTagCompound nbttagcompound = compound.getCompound("Level");
            int j = nbttagcompound.getInt("xPos");
            int k = nbttagcompound.getInt("zPos");
            NBTTagList nbttaglist = nbttagcompound.getList("TileEntities", 10);
            NBTTagList nbttaglist1 = nbttagcompound.getList("Sections", 10);

            for (int l = 0; l < nbttaglist1.tagCount(); ++l)
            {
                NBTTagCompound nbttagcompound1 = nbttaglist1.getCompound(l);
                int i1 = nbttagcompound1.getByte("Y");
                byte[] abyte = nbttagcompound1.getByteArray("Blocks");

                for (int j1 = 0; j1 < abyte.length; ++j1)
                {
                    if (416 == (abyte[j1] & 255) << 4)
                    {
                        int k1 = j1 & 15;
                        int l1 = j1 >> 8 & 15;
                        int i2 = j1 >> 4 & 15;
                        NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                        nbttagcompound2.putString("id", "bed");
                        nbttagcompound2.putInt("x", k1 + (j << 4));
                        nbttagcompound2.putInt("y", l1 + (i1 << 4));
                        nbttagcompound2.putInt("z", i2 + (k << 4));
                        nbttaglist.appendTag(nbttagcompound2);
                    }
                }
            }
        }
        catch (Exception var17)
        {
            LOGGER.warn("Unable to datafix Bed blocks, level format may be missing tags.");
        }

        return compound;
    }
}
