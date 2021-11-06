package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class CPacketPlaceRecipe implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private IRecipe recipeId;
    private boolean placeAll;

    public CPacketPlaceRecipe()
    {
    }

    public CPacketPlaceRecipe(int p_i47614_1_, IRecipe p_i47614_2_, boolean p_i47614_3_)
    {
        this.windowId = p_i47614_1_;
        this.recipeId = p_i47614_2_;
        this.placeAll = p_i47614_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readByte();
        this.recipeId = CraftingManager.getRecipeById(buf.readVarInt());
        this.placeAll = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
        buf.writeVarInt(CraftingManager.getIDForRecipe(this.recipeId));
        buf.writeBoolean(this.placeAll);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlaceRecipe(this);
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    public IRecipe func_194317_b()
    {
        return this.recipeId;
    }

    public boolean shouldPlaceAll()
    {
        return this.placeAll;
    }
}
