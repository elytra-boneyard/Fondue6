package com.elytradev.fondue.module.obelisk;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.obelisk.client.ModuleObeliskClient;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
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
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class BlockObelisk extends Block {

	public static final PropertyBool CONTROLLER = PropertyBool.create("controller");
	
	public BlockObelisk(Material materialIn, MapColor mapColorIn) {
		super(materialIn, mapColorIn);
		setSoundType(new SoundType(1, 1, ModuleObelisk.HOLLOWHIT, ModuleObelisk.HOLLOWHIT, ModuleObelisk.HOLLOWHIT, ModuleObelisk.HOLLOWHIT, ModuleObelisk.HOLLOWHIT));
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
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
		if (worldIn.isRemote && Fondue.isModuleLoaded(ModuleObeliskClient.class)) {
			int x = ((pos.getX()/16)*16);
			int z = ((pos.getZ()/16)*16);
			if (x < 0) x -= 8;
			else x += 8;
			if (z < 0) z -= 8;
			else z += 8;
			MutableBlockPos controller = new MutableBlockPos(x, worldIn.getHeight(x, z), z);
			TileEntity te = worldIn.getTileEntity(controller);
			while (!(te instanceof TileEntityObelisk)) {
				if (controller.getY() <= 0) return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
				controller.setY(controller.getY()-1);
				te = worldIn.getTileEntity(controller);
			}
			Fondue.getModule(ModuleObeliskClient.class).spark((TileEntityObelisk)te);
		}
		return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
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
