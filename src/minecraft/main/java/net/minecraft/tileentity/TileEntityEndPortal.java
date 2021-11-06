package net.minecraft.tileentity;

import net.minecraft.util.EnumFacing;

public class TileEntityEndPortal extends TileEntity
{
    public boolean shouldRenderFace(EnumFacing face)
    {
        return face == EnumFacing.UP;
    }
}
