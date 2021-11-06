package net.minecraft.profiler;

public interface ISnooperInfo
{
    void fillSnooper(Snooper snooper);

    void addServerTypeToSnooper(Snooper playerSnooper);

    boolean isSnooperEnabled();
}
