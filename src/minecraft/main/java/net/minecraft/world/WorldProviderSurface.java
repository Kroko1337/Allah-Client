package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider
{
    public DimensionType getType()
    {
        return DimensionType.OVERWORLD;
    }

    public boolean canDropChunk(int x, int z)
    {
        return !this.world.isSpawnChunk(x, z);
    }
}
