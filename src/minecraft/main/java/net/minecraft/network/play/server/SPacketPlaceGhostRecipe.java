package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlaceGhostRecipe implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private IRecipe recipe;

    public SPacketPlaceGhostRecipe()
    {
    }

    public SPacketPlaceGhostRecipe(int p_i47615_1_, IRecipe p_i47615_2_)
    {
        this.windowId = p_i47615_1_;
        this.recipe = p_i47615_2_;
    }

    public IRecipe func_194311_a()
    {
        return this.recipe;
    }

    public int getWindowId()
    {
        return this.windowId;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.windowId = buf.readByte();
        this.recipe = CraftingManager.getRecipeById(buf.readVarInt());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.windowId);
        buf.writeVarInt(CraftingManager.getIDForRecipe(this.recipe));
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlaceGhostRecipe(this);
    }
}
