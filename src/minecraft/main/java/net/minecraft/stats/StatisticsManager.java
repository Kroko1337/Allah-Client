package net.minecraft.stats;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.TupleIntJsonSerializable;

public class StatisticsManager
{
    protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.<StatBase, TupleIntJsonSerializable>newConcurrentMap();

    public void increment(EntityPlayer player, StatBase stat, int amount)
    {
        this.setValue(player, stat, this.getValue(stat) + amount);
    }

    /**
     * Triggers the logging of an achievement and attempts to announce to server
     */
    public void setValue(EntityPlayer playerIn, StatBase statIn, int p_150873_3_)
    {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(statIn);

        if (tupleintjsonserializable == null)
        {
            tupleintjsonserializable = new TupleIntJsonSerializable();
            this.statsData.put(statIn, tupleintjsonserializable);
        }

        tupleintjsonserializable.setIntegerValue(p_150873_3_);
    }

    /**
     * Reads the given stat and returns its value as an int.
     */
    public int getValue(StatBase stat)
    {
        TupleIntJsonSerializable tupleintjsonserializable = this.statsData.get(stat);
        return tupleintjsonserializable == null ? 0 : tupleintjsonserializable.getIntegerValue();
    }
}
