package net.minecraft.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public abstract class Biomes
{
    public static final Biome OCEAN;
    public static final Biome DEFAULT;
    public static final Biome PLAINS;
    public static final Biome DESERT;
    public static final Biome MOUNTAINS;
    public static final Biome FOREST;
    public static final Biome TAIGA;
    public static final Biome SWAMP;
    public static final Biome RIVER;
    public static final Biome NETHER;
    public static final Biome THE_END;
    public static final Biome FROZEN_OCEAN;
    public static final Biome FROZEN_RIVER;
    public static final Biome SNOWY_TUNDRA;
    public static final Biome SNOWY_MOUNTAINS;
    public static final Biome MUSHROOM_FIELDS;
    public static final Biome MUSHROOM_FIELD_SHORE;
    public static final Biome BEACH;
    public static final Biome DESERT_HILLS;
    public static final Biome WOODED_HILLS;
    public static final Biome TAIGA_HILLS;
    public static final Biome MOUNTAIN_EDGE;
    public static final Biome JUNGLE;
    public static final Biome JUNGLE_HILLS;
    public static final Biome JUNGLE_EDGE;
    public static final Biome DEEP_OCEAN;
    public static final Biome STONE_SHORE;
    public static final Biome SNOWY_BEACH;
    public static final Biome BIRCH_FOREST;
    public static final Biome BIRCH_FOREST_HILLS;
    public static final Biome DARK_FOREST;
    public static final Biome SNOWY_TAIGA;
    public static final Biome SNOWY_TAIGA_HILLS;
    public static final Biome GIANT_TREE_TAIGA;
    public static final Biome GIANT_TREE_TAIGA_HILLS;
    public static final Biome WOODED_MOUNTAINS;
    public static final Biome SAVANNA;
    public static final Biome SAVANNA_PLATEAU;
    public static final Biome BADLANDS;
    public static final Biome WOODED_BADLANDS_PLATEAU;
    public static final Biome BADLANDS_PLATEAU;
    public static final Biome THE_VOID;
    public static final Biome SUNFLOWER_PLAINS;
    public static final Biome DESERT_LAKES;
    public static final Biome GRAVELLY_MOUNTAINS;
    public static final Biome FLOWER_FOREST;
    public static final Biome TAIGA_MOUNTAINS;
    public static final Biome SWAMP_HILLS;
    public static final Biome ICE_SPIKES;
    public static final Biome MODIFIED_JUNGLE;
    public static final Biome MODIFIED_JUNGLE_EDGE;
    public static final Biome TALL_BIRCH_FOREST;
    public static final Biome TALL_BIRCH_HILLS;
    public static final Biome DARK_FOREST_HILLS;
    public static final Biome SNOWY_TAIGA_MOUNTAINS;
    public static final Biome GIANT_SPRUCE_TAIGA;
    public static final Biome GIANT_SPRUCE_TAIGA_HILLS;
    public static final Biome MODIFIED_GRAVELLY_MOUNTAINS;
    public static final Biome SHATTERED_SAVANNA;
    public static final Biome SHATTERED_SAVANNA_PLATEAU;
    public static final Biome ERODED_BADLANDS;
    public static final Biome MODIFIED_WOODED_BADLANDS_PLATEAU;
    public static final Biome MODIFIED_BADLANDS_PLATEAU;

    private static Biome getRegisteredBiome(String id)
    {
        Biome biome = Biome.REGISTRY.getOrDefault(new ResourceLocation(id));

        if (biome == null)
        {
            throw new IllegalStateException("Invalid Biome requested: " + id);
        }
        else
        {
            return biome;
        }
    }

    static
    {
        if (!Bootstrap.isRegistered())
        {
            throw new RuntimeException("Accessed Biomes before Bootstrap!");
        }
        else
        {
            OCEAN = getRegisteredBiome("ocean");
            DEFAULT = OCEAN;
            PLAINS = getRegisteredBiome("plains");
            DESERT = getRegisteredBiome("desert");
            MOUNTAINS = getRegisteredBiome("extreme_hills");
            FOREST = getRegisteredBiome("forest");
            TAIGA = getRegisteredBiome("taiga");
            SWAMP = getRegisteredBiome("swampland");
            RIVER = getRegisteredBiome("river");
            NETHER = getRegisteredBiome("hell");
            THE_END = getRegisteredBiome("sky");
            FROZEN_OCEAN = getRegisteredBiome("frozen_ocean");
            FROZEN_RIVER = getRegisteredBiome("frozen_river");
            SNOWY_TUNDRA = getRegisteredBiome("ice_flats");
            SNOWY_MOUNTAINS = getRegisteredBiome("ice_mountains");
            MUSHROOM_FIELDS = getRegisteredBiome("mushroom_island");
            MUSHROOM_FIELD_SHORE = getRegisteredBiome("mushroom_island_shore");
            BEACH = getRegisteredBiome("beaches");
            DESERT_HILLS = getRegisteredBiome("desert_hills");
            WOODED_HILLS = getRegisteredBiome("forest_hills");
            TAIGA_HILLS = getRegisteredBiome("taiga_hills");
            MOUNTAIN_EDGE = getRegisteredBiome("smaller_extreme_hills");
            JUNGLE = getRegisteredBiome("jungle");
            JUNGLE_HILLS = getRegisteredBiome("jungle_hills");
            JUNGLE_EDGE = getRegisteredBiome("jungle_edge");
            DEEP_OCEAN = getRegisteredBiome("deep_ocean");
            STONE_SHORE = getRegisteredBiome("stone_beach");
            SNOWY_BEACH = getRegisteredBiome("cold_beach");
            BIRCH_FOREST = getRegisteredBiome("birch_forest");
            BIRCH_FOREST_HILLS = getRegisteredBiome("birch_forest_hills");
            DARK_FOREST = getRegisteredBiome("roofed_forest");
            SNOWY_TAIGA = getRegisteredBiome("taiga_cold");
            SNOWY_TAIGA_HILLS = getRegisteredBiome("taiga_cold_hills");
            GIANT_TREE_TAIGA = getRegisteredBiome("redwood_taiga");
            GIANT_TREE_TAIGA_HILLS = getRegisteredBiome("redwood_taiga_hills");
            WOODED_MOUNTAINS = getRegisteredBiome("extreme_hills_with_trees");
            SAVANNA = getRegisteredBiome("savanna");
            SAVANNA_PLATEAU = getRegisteredBiome("savanna_rock");
            BADLANDS = getRegisteredBiome("mesa");
            WOODED_BADLANDS_PLATEAU = getRegisteredBiome("mesa_rock");
            BADLANDS_PLATEAU = getRegisteredBiome("mesa_clear_rock");
            THE_VOID = getRegisteredBiome("void");
            SUNFLOWER_PLAINS = getRegisteredBiome("mutated_plains");
            DESERT_LAKES = getRegisteredBiome("mutated_desert");
            GRAVELLY_MOUNTAINS = getRegisteredBiome("mutated_extreme_hills");
            FLOWER_FOREST = getRegisteredBiome("mutated_forest");
            TAIGA_MOUNTAINS = getRegisteredBiome("mutated_taiga");
            SWAMP_HILLS = getRegisteredBiome("mutated_swampland");
            ICE_SPIKES = getRegisteredBiome("mutated_ice_flats");
            MODIFIED_JUNGLE = getRegisteredBiome("mutated_jungle");
            MODIFIED_JUNGLE_EDGE = getRegisteredBiome("mutated_jungle_edge");
            TALL_BIRCH_FOREST = getRegisteredBiome("mutated_birch_forest");
            TALL_BIRCH_HILLS = getRegisteredBiome("mutated_birch_forest_hills");
            DARK_FOREST_HILLS = getRegisteredBiome("mutated_roofed_forest");
            SNOWY_TAIGA_MOUNTAINS = getRegisteredBiome("mutated_taiga_cold");
            GIANT_SPRUCE_TAIGA = getRegisteredBiome("mutated_redwood_taiga");
            GIANT_SPRUCE_TAIGA_HILLS = getRegisteredBiome("mutated_redwood_taiga_hills");
            MODIFIED_GRAVELLY_MOUNTAINS = getRegisteredBiome("mutated_extreme_hills_with_trees");
            SHATTERED_SAVANNA = getRegisteredBiome("mutated_savanna");
            SHATTERED_SAVANNA_PLATEAU = getRegisteredBiome("mutated_savanna_rock");
            ERODED_BADLANDS = getRegisteredBiome("mutated_mesa");
            MODIFIED_WOODED_BADLANDS_PLATEAU = getRegisteredBiome("mutated_mesa_rock");
            MODIFIED_BADLANDS_PLATEAU = getRegisteredBiome("mutated_mesa_clear_rock");
        }
    }
}
