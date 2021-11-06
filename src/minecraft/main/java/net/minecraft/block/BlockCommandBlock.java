package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockCommandBlock extends BlockContainer
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool CONDITIONAL = PropertyBool.create("conditional");

    public BlockCommandBlock(MapColor color)
    {
        super(Material.IRON, color);
        this.setDefaultState(this.stateContainer.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CONDITIONAL, Boolean.valueOf(false)));
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        TileEntityCommandBlock tileentitycommandblock = new TileEntityCommandBlock();
        tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommandblock;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityCommandBlock)
            {
                TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
                boolean flag = worldIn.isBlockPowered(pos);
                boolean flag1 = tileentitycommandblock.isPowered();
                tileentitycommandblock.setPowered(flag);

                if (!flag1 && !tileentitycommandblock.isAuto() && tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE)
                {
                    if (flag)
                    {
                        tileentitycommandblock.setConditionMet();
                        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                    }
                }
            }
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityCommandBlock)
            {
                TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
                CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                boolean flag = !StringUtils.isNullOrEmpty(commandblockbaselogic.getCommand());
                TileEntityCommandBlock.Mode tileentitycommandblock$mode = tileentitycommandblock.getMode();
                boolean flag1 = tileentitycommandblock.isConditionMet();

                if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.AUTO)
                {
                    tileentitycommandblock.setConditionMet();

                    if (flag1)
                    {
                        this.execute(state, worldIn, pos, commandblockbaselogic, flag);
                    }
                    else if (tileentitycommandblock.isConditional())
                    {
                        commandblockbaselogic.setSuccessCount(0);
                    }

                    if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto())
                    {
                        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                    }
                }
                else if (tileentitycommandblock$mode == TileEntityCommandBlock.Mode.REDSTONE)
                {
                    if (flag1)
                    {
                        this.execute(state, worldIn, pos, commandblockbaselogic, flag);
                    }
                    else if (tileentitycommandblock.isConditional())
                    {
                        commandblockbaselogic.setSuccessCount(0);
                    }
                }

                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
    }

    private void execute(IBlockState p_193387_1_, World p_193387_2_, BlockPos p_193387_3_, CommandBlockBaseLogic p_193387_4_, boolean p_193387_5_)
    {
        if (p_193387_5_)
        {
            p_193387_4_.trigger(p_193387_2_);
        }
        else
        {
            p_193387_4_.setSuccessCount(0);
        }

        executeChain(p_193387_2_, p_193387_3_, (EnumFacing)p_193387_1_.get(FACING));
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 1;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityCommandBlock && playerIn.canUseCommandBlock())
        {
            playerIn.openCommandBlock((TileEntityCommandBlock)tileentity);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @deprecated call via {@link IBlockState#hasComparatorInputOverride()} whenever possible. Implementing/overriding
     * is fine.
     */
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    /**
     * @deprecated call via {@link IBlockState#getComparatorInputOverride(World,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileentity).getCommandBlockLogic().getSuccessCount() : 0;
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityCommandBlock)
        {
            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;
            CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();

            if (stack.hasDisplayName())
            {
                commandblockbaselogic.setName(stack.getDisplayName());
            }

            if (!worldIn.isRemote)
            {
                NBTTagCompound nbttagcompound = stack.getTag();

                if (nbttagcompound == null || !nbttagcompound.contains("BlockEntityTag", 10))
                {
                    commandblockbaselogic.setTrackOutput(worldIn.getGameRules().getBoolean("sendCommandFeedback"));
                    tileentitycommandblock.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
                }

                if (tileentitycommandblock.getMode() == TileEntityCommandBlock.Mode.SEQUENCE)
                {
                    boolean flag = worldIn.isBlockPowered(pos);
                    tileentitycommandblock.setPowered(flag);
                }
            }
        }
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7)).withProperty(CONDITIONAL, Boolean.valueOf((meta & 8) != 0));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.get(FACING)).getIndex() | (((Boolean)state.get(CONDITIONAL)).booleanValue() ? 8 : 0);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.get(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing)state.get(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, CONDITIONAL});
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)).withProperty(CONDITIONAL, Boolean.valueOf(false));
    }

    private static void executeChain(World worldIn, BlockPos pos, EnumFacing directionIn)
    {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos);
        GameRules gamerules = worldIn.getGameRules();
        int i;
        IBlockState iblockstate;

        for (i = gamerules.getInt("maxCommandChainLength"); i-- > 0; directionIn = (EnumFacing)iblockstate.get(FACING))
        {
            blockpos$mutableblockpos.move(directionIn);
            iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);
            Block block = iblockstate.getBlock();

            if (block != Blocks.CHAIN_COMMAND_BLOCK)
            {
                break;
            }

            TileEntity tileentity = worldIn.getTileEntity(blockpos$mutableblockpos);

            if (!(tileentity instanceof TileEntityCommandBlock))
            {
                break;
            }

            TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)tileentity;

            if (tileentitycommandblock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE)
            {
                break;
            }

            if (tileentitycommandblock.isPowered() || tileentitycommandblock.isAuto())
            {
                CommandBlockBaseLogic commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();

                if (tileentitycommandblock.setConditionMet())
                {
                    if (!commandblockbaselogic.trigger(worldIn))
                    {
                        break;
                    }

                    worldIn.updateComparatorOutputLevel(blockpos$mutableblockpos, block);
                }
                else if (tileentitycommandblock.isConditional())
                {
                    commandblockbaselogic.setSuccessCount(0);
                }
            }
        }

        if (i <= 0)
        {
            int j = Math.max(gamerules.getInt("maxCommandChainLength"), 0);
            LOGGER.warn("Commandblock chain tried to execure more than " + j + " steps!");
        }
    }
}
