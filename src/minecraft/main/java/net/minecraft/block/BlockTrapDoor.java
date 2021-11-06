package net.minecraft.block;

import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block
{
    public static final PropertyDirection FACING = BlockHorizontal.HORIZONTAL_FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
    protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockTrapDoor(Material materialIn)
    {
        super(materialIn);
        this.setDefaultState(this.stateContainer.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.valueOf(false)).withProperty(HALF, BlockTrapDoor.DoorHalf.BOTTOM));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb;

        if (((Boolean)state.get(OPEN)).booleanValue())
        {
            switch ((EnumFacing)state.get(FACING))
            {
                case NORTH:
                default:
                    axisalignedbb = NORTH_OPEN_AABB;
                    break;

                case SOUTH:
                    axisalignedbb = SOUTH_OPEN_AABB;
                    break;

                case WEST:
                    axisalignedbb = WEST_OPEN_AABB;
                    break;

                case EAST:
                    axisalignedbb = EAST_OPEN_AABB;
            }
        }
        else if (state.get(HALF) == BlockTrapDoor.DoorHalf.TOP)
        {
            axisalignedbb = TOP_AABB;
        }
        else
        {
            axisalignedbb = BOTTOM_AABB;
        }

        return axisalignedbb;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return !((Boolean)worldIn.getBlockState(pos).get(OPEN)).booleanValue();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (this.material == Material.IRON)
        {
            return false;
        }
        else
        {
            state = state.cycle(OPEN);
            worldIn.setBlockState(pos, state, 2);
            this.playSound(playerIn, worldIn, pos, ((Boolean)state.get(OPEN)).booleanValue());
            return true;
        }
    }

    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_)
    {
        if (p_185731_4_)
        {
            int i = this.material == Material.IRON ? 1037 : 1007;
            worldIn.playEvent(player, i, pos, 0);
        }
        else
        {
            int j = this.material == Material.IRON ? 1036 : 1013;
            worldIn.playEvent(player, j, pos, 0);
        }
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            boolean flag = worldIn.isBlockPowered(pos);

            if (flag || blockIn.getDefaultState().canProvidePower())
            {
                boolean flag1 = ((Boolean)state.get(OPEN)).booleanValue();

                if (flag1 != flag)
                {
                    worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
                    this.playSound((EntityPlayer)null, worldIn, pos, flag);
                }
            }
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (facing.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN, Boolean.valueOf(false));
            iblockstate = iblockstate.withProperty(HALF, hitY > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        }
        else
        {
            iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, Boolean.valueOf(false));
            iblockstate = iblockstate.withProperty(HALF, facing == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
        }

        if (worldIn.isBlockPowered(pos))
        {
            iblockstate = iblockstate.withProperty(OPEN, Boolean.valueOf(true));
        }

        return iblockstate;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    protected static EnumFacing getFacing(int meta)
    {
        switch (meta & 3)
        {
            case 0:
                return EnumFacing.NORTH;

            case 1:
                return EnumFacing.SOUTH;

            case 2:
                return EnumFacing.WEST;

            case 3:
            default:
                return EnumFacing.EAST;
        }
    }

    protected static int getMetaForFacing(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return 0;

            case SOUTH:
                return 1;

            case WEST:
                return 2;

            case EAST:
            default:
                return 3;
        }
    }

    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(OPEN, Boolean.valueOf((meta & 4) != 0)).withProperty(HALF, (meta & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | getMetaForFacing((EnumFacing)state.get(FACING));

        if (((Boolean)state.get(OPEN)).booleanValue())
        {
            i |= 4;
        }

        if (state.get(HALF) == BlockTrapDoor.DoorHalf.TOP)
        {
            i |= 8;
        }

        return i;
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
        return new BlockStateContainer(this, new IProperty[] {FACING, OPEN, HALF});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return (face == EnumFacing.UP && state.get(HALF) == BlockTrapDoor.DoorHalf.TOP || face == EnumFacing.DOWN && state.get(HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !((Boolean)state.get(OPEN)).booleanValue() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public static enum DoorHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private DoorHalf(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
