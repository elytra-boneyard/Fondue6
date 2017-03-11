package com.elytradev.fondue.module.chair;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockChair extends Block {

	public static PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.Plane.HORIZONTAL.facings()));
	
	private Set<EntityPlayer> lounging = Sets.newSetFromMap(new WeakHashMap<>());
	
	public BlockChair(Material materialIn) {
		super(materialIn);
		setSoundType(SoundType.CLOTH);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		List<EntityArmorStand> armorStands = worldIn.getEntitiesWithinAABB(EntityArmorStand.class, state.getBoundingBox(worldIn, pos).offset(pos));
		for (EntityArmorStand eas : armorStands) {
			if (eas.getName().equals("fondue:chair")) {
				eas.setDead();
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			if (!worldIn.provider.isSurfaceWorld()) {
				playerIn.sendMessage(new TextComponentTranslation("fondue.tooDangerous"));
				return true;
			}
			List<EntityArmorStand> armorStands = worldIn.getEntitiesWithinAABB(EntityArmorStand.class, state.getBoundingBox(worldIn, pos).offset(pos));
			for (EntityArmorStand eas : armorStands) {
				if (eas.getName().equals("fondue:chair")) {
					eas.setDead();
				}
			}
			// Welcome back, SethBling here
			EntityArmorStand eas = new EntityArmorStand(worldIn, pos.getX()+0.5, pos.getY()-1.15, pos.getZ()+0.5);
			switch (state.getValue(FACING)) {
				case EAST:
					eas.rotationYaw = -90;
					break;
				case NORTH:
					eas.rotationYaw = 180;
					break;
				case SOUTH:
					eas.rotationYaw = 0;
					break;
				case WEST:
					eas.rotationYaw = 90;
					break;
				default: break;
			}
			eas.setInvisible(true);
			eas.setNoGravity(true);
			eas.setCustomNameTag("fondue:chair");
			worldIn.spawnEntity(eas);
			playerIn.startRiding(eas, true);
			lounging.add(playerIn);
			Iterator<EntityPlayer> iter = lounging.iterator();
			while (iter.hasNext()) {
				EntityPlayer ep = iter.next();
				if (ep.isDead) {
					iter.remove();
				}
				if (!ep.isRiding()) {
					iter.remove();
				} else {
					Entity riding = ep.getRidingEntity();
					if (!(riding instanceof EntityArmorStand && riding.getName().equals("fondue:chair"))) {
						iter.remove();
					}
				}
			}
			ITextComponent component = playerIn.getDisplayName();
			int pct = (int)((lounging.size()/(double)worldIn.playerEntities.size())*100);
			for (EntityPlayer player : worldIn.playerEntities) {
				player.sendMessage(new TextComponentTranslation("fondue.startLounging", component, pct).setStyle(new Style().setColor(TextFormatting.GOLD)));
			}
			if (pct >= 100) {
				for (EntityPlayer ep : lounging) {
					ep.dismountRidingEntity();
				}
				lounging.clear();
				worldIn.setWorldTime(worldIn.getWorldTime()+12000);
			}
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
