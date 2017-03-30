package com.elytradev.fondue.module.waypoints;

import java.util.Map;
import java.util.Random;

import com.elytradev.fondue.Fondue;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import static net.minecraft.item.EnumDyeColor.*;

public class BlockWaypoint extends Block {

	public BlockWaypoint() {
		super(Material.ROCK);
	}

	private static final ImmutableMap<String, EnumDyeColor> ORE_DICTIONARY_TO_ENUM = ImmutableMap.<String, EnumDyeColor>builder()
			.put("dyeBlack", BLACK)
			.put("dyeRed", RED)
			.put("dyeGreen", GREEN)
			.put("dyeBrown", BROWN)
			.put("dyeBlue", BLUE)
			.put("dyePurple", PURPLE)
			.put("dyeCyan", CYAN)
			.put("dyeLightGray", SILVER)
			.put("dyeGray", GRAY)
			.put("dyePink", PINK)
			.put("dyeLime", LIME)
			.put("dyeYellow", YELLOW)
			.put("dyeLightBlue", LIGHT_BLUE)
			.put("dyeMagenta", MAGENTA)
			.put("dyeOrange", ORANGE)
			.put("dyeWhite", WHITE)
			.build();
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack is = playerIn.getHeldItem(hand);
		if (is.isEmpty()) return false;
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TileEntityWaypoint) {
			TileEntityWaypoint tew = (TileEntityWaypoint)te;
			int itemColor = 0;
			for (Map.Entry<String, EnumDyeColor> en : ORE_DICTIONARY_TO_ENUM.entrySet()) {
				for (int id : OreDictionary.getOreIDs(is)) {
					if (en.getKey().equals(OreDictionary.getOreName(id))) {
						itemColor = ModuleWaypoints.BRIGHT_COLORS.get(en.getValue());
						break;
					}
				}
			}
			if (itemColor != 0) {
				int oldColor = tew.color;
				int newColor = ModuleWaypoints.mix(oldColor, itemColor);
				if (oldColor != newColor) {
					if (!worldIn.isRemote) {
						WaypointWorldData worldData = ModuleWaypoints.getDataFor(worldIn);
						WaypointData wd = worldData.get(pos);
						if (wd == null) {
							wd = new WaypointData(pos, newColor, WaypointShape.DIAMOND, WaypointStyle.SOLID);
						} else {
							wd.color = newColor;
						}
						worldData.markDirty();
						tew.color = newColor;
						new MessageUpdateWaypoint(wd).sendToAllWatching(worldIn, pos);
						Fondue.sendUpdatePacket(tew);
						if (!playerIn.isCreative()) {
							is.shrink(1);
							playerIn.setHeldItem(hand, is);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityWaypoint();
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			WaypointWorldData worldData = ModuleWaypoints.getDataFor(worldIn);
			WaypointData wd = new WaypointData(pos, 0xFFFFFF, WaypointShape.DIAMOND, WaypointStyle.SOLID);
			worldData.add(wd);
			worldData.markDirty();
			new MessageUpdateWaypoint(wd).sendToAllWatching(worldIn, pos);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		if (!worldIn.isRemote) {
			WaypointWorldData worldData = ModuleWaypoints.getDataFor(worldIn);
			worldData.remove(pos);
			worldData.markDirty();
		}
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
	}
	
}
