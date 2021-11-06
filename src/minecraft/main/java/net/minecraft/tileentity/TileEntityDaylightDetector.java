package net.minecraft.tileentity;

import net.minecraft.block.BlockDaylightDetector;
import net.minecraft.util.ITickable;

public class TileEntityDaylightDetector extends TileEntity implements ITickable
{
    public void tick()
    {
        if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType instanceof BlockDaylightDetector)
            {
                ((BlockDaylightDetector)this.blockType).updatePower(this.world, this.pos);
            }
        }
    }
}
