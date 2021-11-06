package net.minecraft.client.renderer;

import net.minecraft.util.BlockRenderLayer;

public class RegionRenderCacheBuilder
{
    private final BufferBuilder[] builders = new BufferBuilder[BlockRenderLayer.values().length];

    public RegionRenderCacheBuilder()
    {
        this.builders[BlockRenderLayer.SOLID.ordinal()] = new BufferBuilder(2097152);
        this.builders[BlockRenderLayer.CUTOUT.ordinal()] = new BufferBuilder(131072);
        this.builders[BlockRenderLayer.CUTOUT_MIPPED.ordinal()] = new BufferBuilder(131072);
        this.builders[BlockRenderLayer.TRANSLUCENT.ordinal()] = new BufferBuilder(262144);
    }

    public BufferBuilder getWorldRendererByLayer(BlockRenderLayer layer)
    {
        return this.builders[layer.ordinal()];
    }

    public BufferBuilder getWorldRendererByLayerId(int id)
    {
        return this.builders[id];
    }
}
