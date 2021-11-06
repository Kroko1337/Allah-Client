package net.minecraft.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;

public class GenLayerBiome extends GenLayer
{
    private Biome[] warmBiomes = new Biome[] {Biomes.DESERT, Biomes.DESERT, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA, Biomes.PLAINS};
    private final Biome[] mediumBiomes = new Biome[] {Biomes.FOREST, Biomes.DARK_FOREST, Biomes.MOUNTAINS, Biomes.PLAINS, Biomes.BIRCH_FOREST, Biomes.SWAMP};
    private final Biome[] coldBiomes = new Biome[] {Biomes.FOREST, Biomes.MOUNTAINS, Biomes.TAIGA, Biomes.PLAINS};
    private final Biome[] iceBiomes = new Biome[] {Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TUNDRA, Biomes.SNOWY_TAIGA};
    private final ChunkGeneratorSettings settings;

    public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_)
    {
        super(p_i45560_1_);
        this.parent = p_i45560_3_;

        if (p_i45560_4_ == WorldType.DEFAULT_1_1)
        {
            this.warmBiomes = new Biome[] {Biomes.DESERT, Biomes.FOREST, Biomes.MOUNTAINS, Biomes.SWAMP, Biomes.PLAINS, Biomes.TAIGA};
            this.settings = null;
        }
        else
        {
            this.settings = p_i45560_5_;
        }
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
                this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
                int k = aint[j + i * areaWidth];
                int l = (k & 3840) >> 8;
                k = k & -3841;

                if (this.settings != null && this.settings.fixedBiome >= 0)
                {
                    aint1[j + i * areaWidth] = this.settings.fixedBiome;
                }
                else if (isBiomeOceanic(k))
                {
                    aint1[j + i * areaWidth] = k;
                }
                else if (k == Biome.getIdForBiome(Biomes.MUSHROOM_FIELDS))
                {
                    aint1[j + i * areaWidth] = k;
                }
                else if (k == 1)
                {
                    if (l > 0)
                    {
                        if (this.nextInt(3) == 0)
                        {
                            aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.BADLANDS_PLATEAU);
                        }
                        else
                        {
                            aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.WOODED_BADLANDS_PLATEAU);
                        }
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(this.warmBiomes[this.nextInt(this.warmBiomes.length)]);
                    }
                }
                else if (k == 2)
                {
                    if (l > 0)
                    {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.JUNGLE);
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(this.mediumBiomes[this.nextInt(this.mediumBiomes.length)]);
                    }
                }
                else if (k == 3)
                {
                    if (l > 0)
                    {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.GIANT_TREE_TAIGA);
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(this.coldBiomes[this.nextInt(this.coldBiomes.length)]);
                    }
                }
                else if (k == 4)
                {
                    aint1[j + i * areaWidth] = Biome.getIdForBiome(this.iceBiomes[this.nextInt(this.iceBiomes.length)]);
                }
                else
                {
                    aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_FIELDS);
                }
            }
        }

        return aint1;
    }
}
