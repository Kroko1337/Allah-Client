package net.minecraft.world.storage;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class WorldInfo
{
    private String versionName;
    private int versionId;
    private boolean versionSnapshot;
    public static final EnumDifficulty DEFAULT_DIFFICULTY = EnumDifficulty.NORMAL;
    private long randomSeed;
    private WorldType generator = WorldType.DEFAULT;
    private String generatorOptions = "";
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long gameTime;
    private long dayTime;
    private long lastTimePlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerData;
    private int dimension;
    private String levelName;
    private int saveVersion;
    private int clearWeatherTime;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;
    private GameType gameType;
    private boolean mapFeaturesEnabled;
    private boolean hardcore;
    private boolean allowCommands;
    private boolean initialized;
    private EnumDifficulty difficulty;
    private boolean difficultyLocked;
    private double borderCenterX;
    private double borderCenterZ;
    private double borderSize = 6.0E7D;
    private long borderSizeLerpTime;
    private double borderSizeLerpTarget;
    private double borderSafeZone = 5.0D;
    private double borderDamagePerBlock = 0.2D;
    private int borderWarningBlocks = 5;
    private int borderWarningTime = 15;
    private final Map<DimensionType, NBTTagCompound> dimensionData = Maps.newEnumMap(DimensionType.class);
    private GameRules gameRules = new GameRules();

    protected WorldInfo()
    {
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.LEVEL, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if (compound.contains("Player", 10))
                {
                    compound.setTag("Player", fixer.process(FixTypes.PLAYER, compound.getCompound("Player"), versionIn));
                }

                return compound;
            }
        });
    }

    public WorldInfo(NBTTagCompound nbt)
    {
        if (nbt.contains("Version", 10))
        {
            NBTTagCompound nbttagcompound = nbt.getCompound("Version");
            this.versionName = nbttagcompound.getString("Name");
            this.versionId = nbttagcompound.getInt("Id");
            this.versionSnapshot = nbttagcompound.getBoolean("Snapshot");
        }

        this.randomSeed = nbt.getLong("RandomSeed");

        if (nbt.contains("generatorName", 8))
        {
            String s1 = nbt.getString("generatorName");
            this.generator = WorldType.byName(s1);

            if (this.generator == null)
            {
                this.generator = WorldType.DEFAULT;
            }
            else if (this.generator.isVersioned())
            {
                int i = 0;

                if (nbt.contains("generatorVersion", 99))
                {
                    i = nbt.getInt("generatorVersion");
                }

                this.generator = this.generator.getWorldTypeForGeneratorVersion(i);
            }

            if (nbt.contains("generatorOptions", 8))
            {
                this.generatorOptions = nbt.getString("generatorOptions");
            }
        }

        this.gameType = GameType.getByID(nbt.getInt("GameType"));

        if (nbt.contains("MapFeatures", 99))
        {
            this.mapFeaturesEnabled = nbt.getBoolean("MapFeatures");
        }
        else
        {
            this.mapFeaturesEnabled = true;
        }

        this.spawnX = nbt.getInt("SpawnX");
        this.spawnY = nbt.getInt("SpawnY");
        this.spawnZ = nbt.getInt("SpawnZ");
        this.gameTime = nbt.getLong("Time");

        if (nbt.contains("DayTime", 99))
        {
            this.dayTime = nbt.getLong("DayTime");
        }
        else
        {
            this.dayTime = this.gameTime;
        }

        this.lastTimePlayed = nbt.getLong("LastPlayed");
        this.sizeOnDisk = nbt.getLong("SizeOnDisk");
        this.levelName = nbt.getString("LevelName");
        this.saveVersion = nbt.getInt("version");
        this.clearWeatherTime = nbt.getInt("clearWeatherTime");
        this.rainTime = nbt.getInt("rainTime");
        this.raining = nbt.getBoolean("raining");
        this.thunderTime = nbt.getInt("thunderTime");
        this.thundering = nbt.getBoolean("thundering");
        this.hardcore = nbt.getBoolean("hardcore");

        if (nbt.contains("initialized", 99))
        {
            this.initialized = nbt.getBoolean("initialized");
        }
        else
        {
            this.initialized = true;
        }

        if (nbt.contains("allowCommands", 99))
        {
            this.allowCommands = nbt.getBoolean("allowCommands");
        }
        else
        {
            this.allowCommands = this.gameType == GameType.CREATIVE;
        }

        if (nbt.contains("Player", 10))
        {
            this.playerData = nbt.getCompound("Player");
            this.dimension = this.playerData.getInt("Dimension");
        }

        if (nbt.contains("GameRules", 10))
        {
            this.gameRules.read(nbt.getCompound("GameRules"));
        }

        if (nbt.contains("Difficulty", 99))
        {
            this.difficulty = EnumDifficulty.byId(nbt.getByte("Difficulty"));
        }

        if (nbt.contains("DifficultyLocked", 1))
        {
            this.difficultyLocked = nbt.getBoolean("DifficultyLocked");
        }

        if (nbt.contains("BorderCenterX", 99))
        {
            this.borderCenterX = nbt.getDouble("BorderCenterX");
        }

        if (nbt.contains("BorderCenterZ", 99))
        {
            this.borderCenterZ = nbt.getDouble("BorderCenterZ");
        }

        if (nbt.contains("BorderSize", 99))
        {
            this.borderSize = nbt.getDouble("BorderSize");
        }

        if (nbt.contains("BorderSizeLerpTime", 99))
        {
            this.borderSizeLerpTime = nbt.getLong("BorderSizeLerpTime");
        }

        if (nbt.contains("BorderSizeLerpTarget", 99))
        {
            this.borderSizeLerpTarget = nbt.getDouble("BorderSizeLerpTarget");
        }

        if (nbt.contains("BorderSafeZone", 99))
        {
            this.borderSafeZone = nbt.getDouble("BorderSafeZone");
        }

        if (nbt.contains("BorderDamagePerBlock", 99))
        {
            this.borderDamagePerBlock = nbt.getDouble("BorderDamagePerBlock");
        }

        if (nbt.contains("BorderWarningBlocks", 99))
        {
            this.borderWarningBlocks = nbt.getInt("BorderWarningBlocks");
        }

        if (nbt.contains("BorderWarningTime", 99))
        {
            this.borderWarningTime = nbt.getInt("BorderWarningTime");
        }

        if (nbt.contains("DimensionData", 10))
        {
            NBTTagCompound nbttagcompound1 = nbt.getCompound("DimensionData");

            for (String s : nbttagcompound1.keySet())
            {
                this.dimensionData.put(DimensionType.getById(Integer.parseInt(s)), nbttagcompound1.getCompound(s));
            }
        }
    }

    public WorldInfo(WorldSettings settings, String name)
    {
        this.populateFromWorldSettings(settings);
        this.levelName = name;
        this.difficulty = DEFAULT_DIFFICULTY;
        this.initialized = false;
    }

    public void populateFromWorldSettings(WorldSettings settings)
    {
        this.randomSeed = settings.getSeed();
        this.gameType = settings.getGameType();
        this.mapFeaturesEnabled = settings.isMapFeaturesEnabled();
        this.hardcore = settings.getHardcoreEnabled();
        this.generator = settings.getTerrainType();
        this.generatorOptions = settings.getGeneratorOptions();
        this.allowCommands = settings.areCommandsAllowed();
    }

    public WorldInfo(WorldInfo worldInformation)
    {
        this.randomSeed = worldInformation.randomSeed;
        this.generator = worldInformation.generator;
        this.generatorOptions = worldInformation.generatorOptions;
        this.gameType = worldInformation.gameType;
        this.mapFeaturesEnabled = worldInformation.mapFeaturesEnabled;
        this.spawnX = worldInformation.spawnX;
        this.spawnY = worldInformation.spawnY;
        this.spawnZ = worldInformation.spawnZ;
        this.gameTime = worldInformation.gameTime;
        this.dayTime = worldInformation.dayTime;
        this.lastTimePlayed = worldInformation.lastTimePlayed;
        this.sizeOnDisk = worldInformation.sizeOnDisk;
        this.playerData = worldInformation.playerData;
        this.dimension = worldInformation.dimension;
        this.levelName = worldInformation.levelName;
        this.saveVersion = worldInformation.saveVersion;
        this.rainTime = worldInformation.rainTime;
        this.raining = worldInformation.raining;
        this.thunderTime = worldInformation.thunderTime;
        this.thundering = worldInformation.thundering;
        this.hardcore = worldInformation.hardcore;
        this.allowCommands = worldInformation.allowCommands;
        this.initialized = worldInformation.initialized;
        this.gameRules = worldInformation.gameRules;
        this.difficulty = worldInformation.difficulty;
        this.difficultyLocked = worldInformation.difficultyLocked;
        this.borderCenterX = worldInformation.borderCenterX;
        this.borderCenterZ = worldInformation.borderCenterZ;
        this.borderSize = worldInformation.borderSize;
        this.borderSizeLerpTime = worldInformation.borderSizeLerpTime;
        this.borderSizeLerpTarget = worldInformation.borderSizeLerpTarget;
        this.borderSafeZone = worldInformation.borderSafeZone;
        this.borderDamagePerBlock = worldInformation.borderDamagePerBlock;
        this.borderWarningTime = worldInformation.borderWarningTime;
        this.borderWarningBlocks = worldInformation.borderWarningBlocks;
    }

    /**
     * Creates a new NBTTagCompound for the world, with the given NBTTag as the "Player"
     */
    public NBTTagCompound cloneNBTCompound(@Nullable NBTTagCompound nbt)
    {
        if (nbt == null)
        {
            nbt = this.playerData;
        }

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.updateTagCompound(nbttagcompound, nbt);
        return nbttagcompound;
    }

    private void updateTagCompound(NBTTagCompound nbt, NBTTagCompound playerNbt)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.putString("Name", "1.12.2");
        nbttagcompound.putInt("Id", 1343);
        nbttagcompound.putBoolean("Snapshot", false);
        nbt.setTag("Version", nbttagcompound);
        nbt.putInt("DataVersion", 1343);
        nbt.putLong("RandomSeed", this.randomSeed);
        nbt.putString("generatorName", this.generator.getName());
        nbt.putInt("generatorVersion", this.generator.getVersion());
        nbt.putString("generatorOptions", this.generatorOptions);
        nbt.putInt("GameType", this.gameType.getID());
        nbt.putBoolean("MapFeatures", this.mapFeaturesEnabled);
        nbt.putInt("SpawnX", this.spawnX);
        nbt.putInt("SpawnY", this.spawnY);
        nbt.putInt("SpawnZ", this.spawnZ);
        nbt.putLong("Time", this.gameTime);
        nbt.putLong("DayTime", this.dayTime);
        nbt.putLong("SizeOnDisk", this.sizeOnDisk);
        nbt.putLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());
        nbt.putString("LevelName", this.levelName);
        nbt.putInt("version", this.saveVersion);
        nbt.putInt("clearWeatherTime", this.clearWeatherTime);
        nbt.putInt("rainTime", this.rainTime);
        nbt.putBoolean("raining", this.raining);
        nbt.putInt("thunderTime", this.thunderTime);
        nbt.putBoolean("thundering", this.thundering);
        nbt.putBoolean("hardcore", this.hardcore);
        nbt.putBoolean("allowCommands", this.allowCommands);
        nbt.putBoolean("initialized", this.initialized);
        nbt.putDouble("BorderCenterX", this.borderCenterX);
        nbt.putDouble("BorderCenterZ", this.borderCenterZ);
        nbt.putDouble("BorderSize", this.borderSize);
        nbt.putLong("BorderSizeLerpTime", this.borderSizeLerpTime);
        nbt.putDouble("BorderSafeZone", this.borderSafeZone);
        nbt.putDouble("BorderDamagePerBlock", this.borderDamagePerBlock);
        nbt.putDouble("BorderSizeLerpTarget", this.borderSizeLerpTarget);
        nbt.putDouble("BorderWarningBlocks", (double)this.borderWarningBlocks);
        nbt.putDouble("BorderWarningTime", (double)this.borderWarningTime);

        if (this.difficulty != null)
        {
            nbt.putByte("Difficulty", (byte)this.difficulty.getId());
        }

        nbt.putBoolean("DifficultyLocked", this.difficultyLocked);
        nbt.setTag("GameRules", this.gameRules.write());
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        for (Entry<DimensionType, NBTTagCompound> entry : this.dimensionData.entrySet())
        {
            nbttagcompound1.setTag(String.valueOf(((DimensionType)entry.getKey()).getId()), entry.getValue());
        }

        nbt.setTag("DimensionData", nbttagcompound1);

        if (playerNbt != null)
        {
            nbt.setTag("Player", playerNbt);
        }
    }

    /**
     * Returns the seed of current world.
     */
    public long getSeed()
    {
        return this.randomSeed;
    }

    /**
     * Returns the x spawn position
     */
    public int getSpawnX()
    {
        return this.spawnX;
    }

    /**
     * Return the Y axis spawning point of the player.
     */
    public int getSpawnY()
    {
        return this.spawnY;
    }

    /**
     * Returns the z spawn position
     */
    public int getSpawnZ()
    {
        return this.spawnZ;
    }

    public long getGameTime()
    {
        return this.gameTime;
    }

    /**
     * Get current world time
     */
    public long getDayTime()
    {
        return this.dayTime;
    }

    public long getSizeOnDisk()
    {
        return this.sizeOnDisk;
    }

    /**
     * Returns the player's NBTTagCompound to be loaded
     */
    public NBTTagCompound getPlayerNBTTagCompound()
    {
        return this.playerData;
    }

    /**
     * Set the x spawn position to the passed in value
     */
    public void setSpawnX(int x)
    {
        this.spawnX = x;
    }

    /**
     * Sets the y spawn position
     */
    public void setSpawnY(int y)
    {
        this.spawnY = y;
    }

    /**
     * Set the z spawn position to the passed in value
     */
    public void setSpawnZ(int z)
    {
        this.spawnZ = z;
    }

    public void setGameTime(long time)
    {
        this.gameTime = time;
    }

    /**
     * Set current world time
     */
    public void setDayTime(long time)
    {
        this.dayTime = time;
    }

    public void setSpawn(BlockPos spawnPoint)
    {
        this.spawnX = spawnPoint.getX();
        this.spawnY = spawnPoint.getY();
        this.spawnZ = spawnPoint.getZ();
    }

    /**
     * Get current world name
     */
    public String getWorldName()
    {
        return this.levelName;
    }

    public void setWorldName(String worldName)
    {
        this.levelName = worldName;
    }

    /**
     * Returns the save version of this world
     */
    public int getSaveVersion()
    {
        return this.saveVersion;
    }

    /**
     * Sets the save version of the world
     */
    public void setSaveVersion(int version)
    {
        this.saveVersion = version;
    }

    /**
     * Return the last time the player was in this world.
     */
    public long getLastTimePlayed()
    {
        return this.lastTimePlayed;
    }

    public int getClearWeatherTime()
    {
        return this.clearWeatherTime;
    }

    public void setClearWeatherTime(int cleanWeatherTimeIn)
    {
        this.clearWeatherTime = cleanWeatherTimeIn;
    }

    /**
     * Returns true if it is thundering, false otherwise.
     */
    public boolean isThundering()
    {
        return this.thundering;
    }

    /**
     * Sets whether it is thundering or not.
     */
    public void setThundering(boolean thunderingIn)
    {
        this.thundering = thunderingIn;
    }

    /**
     * Returns the number of ticks until next thunderbolt.
     */
    public int getThunderTime()
    {
        return this.thunderTime;
    }

    /**
     * Defines the number of ticks until next thunderbolt.
     */
    public void setThunderTime(int time)
    {
        this.thunderTime = time;
    }

    /**
     * Returns true if it is raining, false otherwise.
     */
    public boolean isRaining()
    {
        return this.raining;
    }

    /**
     * Sets whether it is raining or not.
     */
    public void setRaining(boolean isRaining)
    {
        this.raining = isRaining;
    }

    /**
     * Return the number of ticks until rain.
     */
    public int getRainTime()
    {
        return this.rainTime;
    }

    /**
     * Sets the number of ticks until rain.
     */
    public void setRainTime(int time)
    {
        this.rainTime = time;
    }

    /**
     * Gets the GameType.
     */
    public GameType getGameType()
    {
        return this.gameType;
    }

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return this.mapFeaturesEnabled;
    }

    public void setMapFeaturesEnabled(boolean enabled)
    {
        this.mapFeaturesEnabled = enabled;
    }

    /**
     * Sets the GameType.
     */
    public void setGameType(GameType type)
    {
        this.gameType = type;
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    public boolean isHardcore()
    {
        return this.hardcore;
    }

    public void setHardcore(boolean hardcoreIn)
    {
        this.hardcore = hardcoreIn;
    }

    public WorldType getGenerator()
    {
        return this.generator;
    }

    public void setGenerator(WorldType type)
    {
        this.generator = type;
    }

    public String getGeneratorOptions()
    {
        return this.generatorOptions == null ? "" : this.generatorOptions;
    }

    /**
     * Returns true if commands are allowed on this World.
     */
    public boolean areCommandsAllowed()
    {
        return this.allowCommands;
    }

    public void setAllowCommands(boolean allow)
    {
        this.allowCommands = allow;
    }

    /**
     * Returns true if the World is initialized.
     */
    public boolean isInitialized()
    {
        return this.initialized;
    }

    /**
     * Sets the initialization status of the World.
     */
    public void setInitialized(boolean initializedIn)
    {
        this.initialized = initializedIn;
    }

    /**
     * Gets the GameRules class Instance.
     */
    public GameRules getGameRulesInstance()
    {
        return this.gameRules;
    }

    /**
     * Returns the border center X position
     */
    public double getBorderCenterX()
    {
        return this.borderCenterX;
    }

    /**
     * Returns the border center Z position
     */
    public double getBorderCenterZ()
    {
        return this.borderCenterZ;
    }

    public double getBorderSize()
    {
        return this.borderSize;
    }

    /**
     * Sets the border size
     */
    public void setBorderSize(double size)
    {
        this.borderSize = size;
    }

    /**
     * Returns the border lerp time
     */
    public long getBorderSizeLerpTime()
    {
        return this.borderSizeLerpTime;
    }

    /**
     * Sets the border lerp time
     */
    public void setBorderSizeLerpTime(long time)
    {
        this.borderSizeLerpTime = time;
    }

    /**
     * Returns the border lerp target
     */
    public double getBorderSizeLerpTarget()
    {
        return this.borderSizeLerpTarget;
    }

    /**
     * Sets the border lerp target
     */
    public void setBorderSizeLerpTarget(double lerpSize)
    {
        this.borderSizeLerpTarget = lerpSize;
    }

    /**
     * Sets the border center Z position
     */
    public void setBorderCenterZ(double posZ)
    {
        this.borderCenterZ = posZ;
    }

    /**
     * Sets the border center X position
     */
    public void setBorderCenterX(double posX)
    {
        this.borderCenterX = posX;
    }

    /**
     * Returns the border safe zone
     */
    public double getBorderSafeZone()
    {
        return this.borderSafeZone;
    }

    /**
     * Sets the border safe zone
     */
    public void setBorderSafeZone(double amount)
    {
        this.borderSafeZone = amount;
    }

    /**
     * Returns the border damage per block
     */
    public double getBorderDamagePerBlock()
    {
        return this.borderDamagePerBlock;
    }

    /**
     * Sets the border damage per block
     */
    public void setBorderDamagePerBlock(double damage)
    {
        this.borderDamagePerBlock = damage;
    }

    /**
     * Returns the border warning distance
     */
    public int getBorderWarningBlocks()
    {
        return this.borderWarningBlocks;
    }

    /**
     * Returns the border warning time
     */
    public int getBorderWarningTime()
    {
        return this.borderWarningTime;
    }

    /**
     * Sets the border warning distance
     */
    public void setBorderWarningBlocks(int amountOfBlocks)
    {
        this.borderWarningBlocks = amountOfBlocks;
    }

    /**
     * Sets the border warning time
     */
    public void setBorderWarningTime(int ticks)
    {
        this.borderWarningTime = ticks;
    }

    public EnumDifficulty getDifficulty()
    {
        return this.difficulty;
    }

    public void setDifficulty(EnumDifficulty newDifficulty)
    {
        this.difficulty = newDifficulty;
    }

    public boolean isDifficultyLocked()
    {
        return this.difficultyLocked;
    }

    public void setDifficultyLocked(boolean locked)
    {
        this.difficultyLocked = locked;
    }

    /**
     * Adds this WorldInfo instance to the crash report.
     */
    public void addToCrashReport(CrashReportCategory category)
    {
        category.addDetail("Level seed", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return String.valueOf(WorldInfo.this.getSeed());
            }
        });
        category.addDetail("Level generator", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return String.format("ID %02d - %s, ver %d. Features enabled: %b", WorldInfo.this.generator.getId(), WorldInfo.this.generator.getName(), WorldInfo.this.generator.getVersion(), WorldInfo.this.mapFeaturesEnabled);
            }
        });
        category.addDetail("Level generator options", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return WorldInfo.this.generatorOptions;
            }
        });
        category.addDetail("Level spawn location", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return CrashReportCategory.getCoordinateInfo(WorldInfo.this.spawnX, WorldInfo.this.spawnY, WorldInfo.this.spawnZ);
            }
        });
        category.addDetail("Level time", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return String.format("%d game time, %d day time", WorldInfo.this.gameTime, WorldInfo.this.dayTime);
            }
        });
        category.addDetail("Level dimension", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return String.valueOf(WorldInfo.this.dimension);
            }
        });
        category.addDetail("Level storage version", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                String s = "Unknown?";

                try
                {
                    switch (WorldInfo.this.saveVersion)
                    {
                        case 19132:
                            s = "McRegion";
                            break;

                        case 19133:
                            s = "Anvil";
                    }
                }
                catch (Throwable var3)
                {
                    ;
                }

                return String.format("0x%05X - %s", WorldInfo.this.saveVersion, s);
            }
        });
        category.addDetail("Level weather", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", WorldInfo.this.rainTime, WorldInfo.this.raining, WorldInfo.this.thunderTime, WorldInfo.this.thundering);
            }
        });
        category.addDetail("Level game mode", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", WorldInfo.this.gameType.getName(), WorldInfo.this.gameType.getID(), WorldInfo.this.hardcore, WorldInfo.this.allowCommands);
            }
        });
    }

    public NBTTagCompound getDimensionData(DimensionType dimensionIn)
    {
        NBTTagCompound nbttagcompound = this.dimensionData.get(dimensionIn);
        return nbttagcompound == null ? new NBTTagCompound() : nbttagcompound;
    }

    public void setDimensionData(DimensionType dimensionIn, NBTTagCompound compound)
    {
        this.dimensionData.put(dimensionIn, compound);
    }

    public int getVersionId()
    {
        return this.versionId;
    }

    public boolean isVersionSnapshot()
    {
        return this.versionSnapshot;
    }

    public String getVersionName()
    {
        return this.versionName;
    }
}
