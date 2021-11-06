package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<ChunkPos, NBTTagCompound> chunksToSave = Maps.<ChunkPos, NBTTagCompound>newConcurrentMap();
    private final Set<ChunkPos> chunksBeingSaved = Collections.<ChunkPos>newSetFromMap(Maps.newConcurrentMap());
    private final File chunkSaveLocation;
    private final DataFixer fixer;
    private boolean flushing;

    public AnvilChunkLoader(File chunkSaveLocationIn, DataFixer dataFixerIn)
    {
        this.chunkSaveLocation = chunkSaveLocationIn;
        this.fixer = dataFixerIn;
    }

    @Nullable
    public Chunk loadChunk(World worldIn, int x, int z) throws IOException
    {
        ChunkPos chunkpos = new ChunkPos(x, z);
        NBTTagCompound nbttagcompound = this.chunksToSave.get(chunkpos);

        if (nbttagcompound == null)
        {
            DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, x, z);

            if (datainputstream == null)
            {
                return null;
            }

            nbttagcompound = this.fixer.process(FixTypes.CHUNK, CompressedStreamTools.read(datainputstream));
        }

        return this.checkedReadChunkFromNBT(worldIn, x, z, nbttagcompound);
    }

    public boolean isChunkGeneratedAt(int x, int z)
    {
        ChunkPos chunkpos = new ChunkPos(x, z);
        NBTTagCompound nbttagcompound = this.chunksToSave.get(chunkpos);
        return nbttagcompound != null ? true : RegionFileCache.chunkExists(this.chunkSaveLocation, x, z);
    }

    @Nullable
    protected Chunk checkedReadChunkFromNBT(World worldIn, int x, int z, NBTTagCompound compound)
    {
        if (!compound.contains("Level", 10))
        {
            LOGGER.error("Chunk file at {},{} is missing level data, skipping", Integer.valueOf(x), Integer.valueOf(z));
            return null;
        }
        else
        {
            NBTTagCompound nbttagcompound = compound.getCompound("Level");

            if (!nbttagcompound.contains("Sections", 9))
            {
                LOGGER.error("Chunk file at {},{} is missing block data, skipping", Integer.valueOf(x), Integer.valueOf(z));
                return null;
            }
            else
            {
                Chunk chunk = this.readChunkFromNBT(worldIn, nbttagcompound);

                if (!chunk.isAtLocation(x, z))
                {
                    LOGGER.error("Chunk file at {},{} is in the wrong location; relocating. (Expected {}, {}, got {}, {})", Integer.valueOf(x), Integer.valueOf(z), Integer.valueOf(x), Integer.valueOf(z), Integer.valueOf(chunk.x), Integer.valueOf(chunk.z));
                    nbttagcompound.putInt("xPos", x);
                    nbttagcompound.putInt("zPos", z);
                    chunk = this.readChunkFromNBT(worldIn, nbttagcompound);
                }

                return chunk;
            }
        }
    }

    public void saveChunk(World worldIn, Chunk chunkIn) throws MinecraftException, IOException
    {
        worldIn.checkSessionLock();

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound.setTag("Level", nbttagcompound1);
            nbttagcompound.putInt("DataVersion", 1343);
            this.writeChunkToNBT(chunkIn, worldIn, nbttagcompound1);
            this.addChunkToPending(chunkIn.getPos(), nbttagcompound);
        }
        catch (Exception exception)
        {
            LOGGER.error("Failed to save chunk", (Throwable)exception);
        }
    }

    protected void addChunkToPending(ChunkPos pos, NBTTagCompound compound)
    {
        if (!this.chunksBeingSaved.contains(pos))
        {
            this.chunksToSave.put(pos, compound);
        }

        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }

    public boolean writeNextIO()
    {
        if (this.chunksToSave.isEmpty())
        {
            if (this.flushing)
            {
                LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", (Object)this.chunkSaveLocation.getName());
            }

            return false;
        }
        else
        {
            ChunkPos chunkpos = this.chunksToSave.keySet().iterator().next();
            boolean lvt_3_1_;

            try
            {
                this.chunksBeingSaved.add(chunkpos);
                NBTTagCompound nbttagcompound = this.chunksToSave.remove(chunkpos);

                if (nbttagcompound != null)
                {
                    try
                    {
                        this.writeChunkData(chunkpos, nbttagcompound);
                    }
                    catch (Exception exception)
                    {
                        LOGGER.error("Failed to save chunk", (Throwable)exception);
                    }
                }

                lvt_3_1_ = true;
            }
            finally
            {
                this.chunksBeingSaved.remove(chunkpos);
            }

            return lvt_3_1_;
        }
    }

    private void writeChunkData(ChunkPos pos, NBTTagCompound compound) throws IOException
    {
        DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, pos.x, pos.z);
        CompressedStreamTools.write(compound, dataoutputstream);
        dataoutputstream.close();
    }

    public void saveExtraChunkData(World worldIn, Chunk chunkIn) throws IOException
    {
    }

    public void chunkTick()
    {
    }

    public void flush()
    {
        try
        {
            this.flushing = true;

            while (this.writeNextIO());
        }
        finally
        {
            this.flushing = false;
        }
    }

    public static void registerFixes(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.CHUNK, new IDataWalker()
        {
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn)
            {
                if (compound.contains("Level", 10))
                {
                    NBTTagCompound nbttagcompound = compound.getCompound("Level");

                    if (nbttagcompound.contains("Entities", 9))
                    {
                        NBTTagList nbttaglist = nbttagcompound.getList("Entities", 10);

                        for (int i = 0; i < nbttaglist.tagCount(); ++i)
                        {
                            nbttaglist.set(i, fixer.process(FixTypes.ENTITY, (NBTTagCompound)nbttaglist.get(i), versionIn));
                        }
                    }

                    if (nbttagcompound.contains("TileEntities", 9))
                    {
                        NBTTagList nbttaglist1 = nbttagcompound.getList("TileEntities", 10);

                        for (int j = 0; j < nbttaglist1.tagCount(); ++j)
                        {
                            nbttaglist1.set(j, fixer.process(FixTypes.BLOCK_ENTITY, (NBTTagCompound)nbttaglist1.get(j), versionIn));
                        }
                    }
                }

                return compound;
            }
        });
    }

    private void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound compound)
    {
        compound.putInt("xPos", chunkIn.x);
        compound.putInt("zPos", chunkIn.z);
        compound.putLong("LastUpdate", worldIn.getGameTime());
        compound.putIntArray("HeightMap", chunkIn.getHeightMap());
        compound.putBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
        compound.putBoolean("LightPopulated", chunkIn.isLightPopulated());
        compound.putLong("InhabitedTime", chunkIn.getInhabitedTime());
        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getSections();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = worldIn.dimension.hasSkyLight();

        for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage)
        {
            if (extendedblockstorage != Chunk.EMPTY_SECTION)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.putByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 255));
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = extendedblockstorage.getData().getDataForNBT(abyte, nibblearray);
                nbttagcompound.putByteArray("Blocks", abyte);
                nbttagcompound.putByteArray("Data", nibblearray.getData());

                if (nibblearray1 != null)
                {
                    nbttagcompound.putByteArray("Add", nibblearray1.getData());
                }

                nbttagcompound.putByteArray("BlockLight", extendedblockstorage.getBlockLight().getData());

                if (flag)
                {
                    nbttagcompound.putByteArray("SkyLight", extendedblockstorage.getSkyLight().getData());
                }
                else
                {
                    nbttagcompound.putByteArray("SkyLight", new byte[extendedblockstorage.getBlockLight().getData().length]);
                }

                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Sections", nbttaglist);
        compound.putByteArray("Biomes", chunkIn.getBiomeArray());
        chunkIn.setHasEntities(false);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (int i = 0; i < chunkIn.getEntityLists().length; ++i)
        {
            for (Entity entity : chunkIn.getEntityLists()[i])
            {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                if (entity.writeUnlessPassenger(nbttagcompound2))
                {
                    chunkIn.setHasEntities(true);
                    nbttaglist1.appendTag(nbttagcompound2);
                }
            }
        }

        compound.setTag("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        for (TileEntity tileentity : chunkIn.getTileEntityMap().values())
        {
            NBTTagCompound nbttagcompound3 = tileentity.write(new NBTTagCompound());
            nbttaglist2.appendTag(nbttagcompound3);
        }

        compound.setTag("TileEntities", nbttaglist2);
        List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);

        if (list != null)
        {
            long j = worldIn.getGameTime();
            NBTTagList nbttaglist3 = new NBTTagList();

            for (NextTickListEntry nextticklistentry : list)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                ResourceLocation resourcelocation = Block.REGISTRY.getKey(nextticklistentry.getTarget());
                nbttagcompound1.putString("i", resourcelocation == null ? "" : resourcelocation.toString());
                nbttagcompound1.putInt("x", nextticklistentry.position.getX());
                nbttagcompound1.putInt("y", nextticklistentry.position.getY());
                nbttagcompound1.putInt("z", nextticklistentry.position.getZ());
                nbttagcompound1.putInt("t", (int)(nextticklistentry.scheduledTime - j));
                nbttagcompound1.putInt("p", nextticklistentry.priority);
                nbttaglist3.appendTag(nbttagcompound1);
            }

            compound.setTag("TileTicks", nbttaglist3);
        }
    }

    private Chunk readChunkFromNBT(World worldIn, NBTTagCompound compound)
    {
        int i = compound.getInt("xPos");
        int j = compound.getInt("zPos");
        Chunk chunk = new Chunk(worldIn, i, j);
        chunk.setHeightMap(compound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(compound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(compound.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(compound.getLong("InhabitedTime"));
        NBTTagList nbttaglist = compound.getList("Sections", 10);
        int k = 16;
        ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[16];
        boolean flag = worldIn.dimension.hasSkyLight();

        for (int l = 0; l < nbttaglist.tagCount(); ++l)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompound(l);
            int i1 = nbttagcompound.getByte("Y");
            ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i1 << 4, flag);
            byte[] abyte = nbttagcompound.getByteArray("Blocks");
            NibbleArray nibblearray = new NibbleArray(nbttagcompound.getByteArray("Data"));
            NibbleArray nibblearray1 = nbttagcompound.contains("Add", 7) ? new NibbleArray(nbttagcompound.getByteArray("Add")) : null;
            extendedblockstorage.getData().setDataFromNBT(abyte, nibblearray, nibblearray1);
            extendedblockstorage.setBlockLight(new NibbleArray(nbttagcompound.getByteArray("BlockLight")));

            if (flag)
            {
                extendedblockstorage.setSkyLight(new NibbleArray(nbttagcompound.getByteArray("SkyLight")));
            }

            extendedblockstorage.recalculateRefCounts();
            aextendedblockstorage[i1] = extendedblockstorage;
        }

        chunk.setStorageArrays(aextendedblockstorage);

        if (compound.contains("Biomes", 7))
        {
            chunk.setBiomeArray(compound.getByteArray("Biomes"));
        }

        NBTTagList nbttaglist1 = compound.getList("Entities", 10);

        for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompound(j1);
            readChunkEntity(nbttagcompound1, worldIn, chunk);
            chunk.setHasEntities(true);
        }

        NBTTagList nbttaglist2 = compound.getList("TileEntities", 10);

        for (int k1 = 0; k1 < nbttaglist2.tagCount(); ++k1)
        {
            NBTTagCompound nbttagcompound2 = nbttaglist2.getCompound(k1);
            TileEntity tileentity = TileEntity.create(worldIn, nbttagcompound2);

            if (tileentity != null)
            {
                chunk.addTileEntity(tileentity);
            }
        }

        if (compound.contains("TileTicks", 9))
        {
            NBTTagList nbttaglist3 = compound.getList("TileTicks", 10);

            for (int l1 = 0; l1 < nbttaglist3.tagCount(); ++l1)
            {
                NBTTagCompound nbttagcompound3 = nbttaglist3.getCompound(l1);
                Block block;

                if (nbttagcompound3.contains("i", 8))
                {
                    block = Block.getBlockFromName(nbttagcompound3.getString("i"));
                }
                else
                {
                    block = Block.getBlockById(nbttagcompound3.getInt("i"));
                }

                worldIn.scheduleBlockUpdate(new BlockPos(nbttagcompound3.getInt("x"), nbttagcompound3.getInt("y"), nbttagcompound3.getInt("z")), block, nbttagcompound3.getInt("t"), nbttagcompound3.getInt("p"));
            }
        }

        return chunk;
    }

    @Nullable
    public static Entity readChunkEntity(NBTTagCompound compound, World worldIn, Chunk chunkIn)
    {
        Entity entity = createEntityFromNBT(compound, worldIn);

        if (entity == null)
        {
            return null;
        }
        else
        {
            chunkIn.addEntity(entity);

            if (compound.contains("Passengers", 9))
            {
                NBTTagList nbttaglist = compound.getList("Passengers", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    Entity entity1 = readChunkEntity(nbttaglist.getCompound(i), worldIn, chunkIn);

                    if (entity1 != null)
                    {
                        entity1.startRiding(entity, true);
                    }
                }
            }

            return entity;
        }
    }

    @Nullable
    public static Entity readWorldEntityPos(NBTTagCompound compound, World worldIn, double x, double y, double z, boolean attemptSpawn)
    {
        Entity entity = createEntityFromNBT(compound, worldIn);

        if (entity == null)
        {
            return null;
        }
        else
        {
            entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);

            if (attemptSpawn && !worldIn.addEntity0(entity))
            {
                return null;
            }
            else
            {
                if (compound.contains("Passengers", 9))
                {
                    NBTTagList nbttaglist = compound.getList("Passengers", 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        Entity entity1 = readWorldEntityPos(nbttaglist.getCompound(i), worldIn, x, y, z, attemptSpawn);

                        if (entity1 != null)
                        {
                            entity1.startRiding(entity, true);
                        }
                    }
                }

                return entity;
            }
        }
    }

    @Nullable
    protected static Entity createEntityFromNBT(NBTTagCompound compound, World worldIn)
    {
        try
        {
            return EntityList.createEntityFromNBT(compound, worldIn);
        }
        catch (RuntimeException var3)
        {
            return null;
        }
    }

    public static void spawnEntity(Entity entityIn, World worldIn)
    {
        if (worldIn.addEntity0(entityIn) && entityIn.isBeingRidden())
        {
            for (Entity entity : entityIn.getPassengers())
            {
                spawnEntity(entity, worldIn);
            }
        }
    }

    @Nullable
    public static Entity readWorldEntity(NBTTagCompound compound, World worldIn, boolean p_186051_2_)
    {
        Entity entity = createEntityFromNBT(compound, worldIn);

        if (entity == null)
        {
            return null;
        }
        else if (p_186051_2_ && !worldIn.addEntity0(entity))
        {
            return null;
        }
        else
        {
            if (compound.contains("Passengers", 9))
            {
                NBTTagList nbttaglist = compound.getList("Passengers", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    Entity entity1 = readWorldEntity(nbttaglist.getCompound(i), worldIn, p_186051_2_);

                    if (entity1 != null)
                    {
                        entity1.startRiding(entity, true);
                    }
                }
            }

            return entity;
        }
    }
}
