package com.elytradev.fondue.module.obelisk;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockObelisk extends Block {

	public static final PropertyBool CONTROLLER = PropertyBool.create("controller");
	
	public BlockObelisk(Material materialIn, MapColor mapColorIn) {
		super(materialIn, mapColorIn);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(CONTROLLER);
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return state.getValue(CONTROLLER) ? new TileEntityObelisk() : null;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CONTROLLER);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityObelisk) {
			((TileEntityObelisk)te).attune(playerIn);
			return true;
		}
		return false;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CONTROLLER, (meta & 1) != 0);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CONTROLLER) ? 1 : 0;
	}
	
}
