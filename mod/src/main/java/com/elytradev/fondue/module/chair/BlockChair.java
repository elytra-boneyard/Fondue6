package com.elytradev.fondue.module.chair;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BlockChair extends Block {

	public static PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.Plane.HORIZONTAL.facings()));
	
	public BlockChair(Material materialIn) {
		super(materialIn);
		setSoundType(SoundType.CLOTH);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		deleteOldEntities(worldIn, pos, state);
	}
	
	private void deleteOldEntities(World world, BlockPos pos, IBlockState state) {
		// make sure to clean up our armor stand mess from old versions
		List<EntityArmorStand> armorStands = world.getEntitiesWithinAABB(EntityArmorStand.class, state.getBoundingBox(world, pos).offset(pos));
		for (EntityArmorStand eas : armorStands) {
			if (eas.getName().equals("fondue:chair")) {
				eas.setDead();
			}
		}
		
		List<EntitySeat> seats = world.getEntitiesWithinAABB(EntitySeat.class, state.getBoundingBox(world, pos).offset(pos));
		for (EntitySeat es : seats) {
			es.setDead();
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			if (!worldIn.provider.isSurfaceWorld()) {
				playerIn.sendMessage(new TextComponentTranslation("fondue.tooDangerous"));
				return true;
			}
			deleteOldEntities(worldIn, pos, state);
			EntitySeat seat = new EntitySeat(worldIn);
			seat.setPosition(pos.getX()+0.5, pos.getY()-1.15, pos.getZ()+0.5);
			worldIn.spawnEntity(seat);
			playerIn.startRiding(seat, true);
		}
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	// AAAAAAAA
	
	@Override
	public boolean isNormalCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

}
