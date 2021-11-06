package net.minecraft.world.storage;

import java.io.File;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat
{
    String getName();

    ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata);

    List<WorldSummary> getSaveList() throws AnvilConverterException;

    void flushCache();

    @Nullable

    /**
     * Returns the world's WorldInfo object
     */
    WorldInfo getWorldInfo(String saveName);

    boolean isNewLevelIdAcceptable(String saveName);

    /**
     * Deletes a world directory.
     */
    boolean deleteWorldDirectory(String saveName);

    /**
     * Renames the world by storing the new name in level.dat. It does *not* rename the directory containing the world
     * data.
     */
    void renameWorld(String dirName, String newName);

    boolean isConvertible(String saveName);

    /**
     * gets if the map is old chunk saving (true) or McRegion (false)
     */
    boolean isOldMapFormat(String saveName);

    /**
     * converts the map to mcRegion
     */
    boolean convertMapFormat(String filename, IProgressUpdate progressCallback);

    /**
     * Return whether the given world can be loaded.
     */
    boolean canLoadWorld(String saveName);

    /**
     * Gets a file within the given world.
     */
    File getFile(String saveName, String filePath);
}
