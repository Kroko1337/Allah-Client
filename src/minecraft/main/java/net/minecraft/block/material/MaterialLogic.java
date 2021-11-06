package net.minecraft.block.material;

public class MaterialLogic extends Material
{
    public MaterialLogic(MapColor color)
    {
        super(color);
        this.setAdventureModeExempt();
    }

    /**
     * Returns true if the block is a considered solid. This is true by default.
     */
    public boolean isSolid()
    {
        return false;
    }

    public boolean blocksLight()
    {
        return false;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return false;
    }
}
