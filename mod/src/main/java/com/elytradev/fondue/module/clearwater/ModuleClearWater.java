package com.elytradev.fondue.module.clearwater;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.elytradev.concrete.reflect.accessor.Accessor;
import com.elytradev.concrete.reflect.accessor.Accessors;
import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ModuleClearWater extends Module {

	@Override
	public String getName() {
		return "Clear Water";
	}

	@Override
	public String getDescription() {
		return "Makes water less murky.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA);
	}
	
	private final Accessor<Integer> updateLCG = Accessors.findField(World.class, "updateLCG", "field_73005_l", "l");
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		// This is HILARIOUSLY simple.
		Blocks.WATER.setLightOpacity(1);
		Blocks.FLOWING_WATER.setLightOpacity(1);
		
		// This, however, is not.
		Blocks.GRASS.setTickRandomly(false);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		if (e.phase == Phase.START) {
			if (e.world instanceof WorldServer) {
				tickGrass((WorldServer)e.world);
			}
		}
	}
	
	private void tickGrass(WorldServer w) {
		int randomTickSpeed = w.getGameRules().getInt("randomTickSpeed");
		w.profiler.startSection("fondue:growGrass");

		for (Iterator<Chunk> iterator = w.getPersistentChunkIterable(w.getPlayerChunkMap().getChunkIterator()); iterator.hasNext(); w.profiler.endSection()) {
			w.profiler.startSection("getChunk");
			Chunk chunk = iterator.next();
			int chunkX = chunk.xPosition * 16;
			int chunkZ = chunk.zPosition * 16;
			w.profiler.endStartSection("tickBlocks");

			if (randomTickSpeed > 0) {
				for (ExtendedBlockStorage ebs : chunk.getBlockStorageArray()) {
					// getNeedsRandomTick is not checked because we make grass lie about not needing ticks
					// The performance hit should be negiligible, since grass is basically everywhere
					if (ebs != Chunk.NULL_BLOCK_STORAGE) {
						for (int i = 0; i < randomTickSpeed; ++i) {
							updateLCG.set(w, updateLCG.get(w) * 3 + 1013904223);
							int rand = updateLCG.get(w) >> 2;
							int x = rand & 15;
							int z = rand >> 8 & 15;
							int y = rand >> 16 & 15;
							IBlockState ibs = ebs.get(x, y, z);
							Block block = ibs.getBlock();
							if (block == Blocks.GRASS) {
								w.profiler.startSection("randomTick");
								doTickGrass(w, new BlockPos(x + chunkX, y + ebs.getYLocation(), z + chunkZ), ibs, w.rand);
								w.profiler.endSection();
							}
						}
					}
				}
			}
		}

		w.profiler.endSection();
	}
	
	public void doTickGrass(World w, BlockPos pos, IBlockState state, Random rand) {
		if (w.getBlockState(pos.up()).getBlock() == Blocks.WATER || w.getBlockState(pos.up()).getBlock() == Blocks.FLOWING_WATER ||
				(w.getLightFromNeighbors(pos.up()) < 4 && w.getBlockState(pos.up()).getLightOpacity(w, pos.up()) > 2)) {
			w.setBlockState(pos, Blocks.DIRT.getDefaultState());
		} else {
			if (w.getLightFromNeighbors(pos.up()) >= 9) {
				for (int i = 0; i < 4; ++i) {
					BlockPos targetPos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

					if (targetPos.getY() >= 0 && targetPos.getY() < 256 && !w.isBlockLoaded(targetPos)) {
						return;
					}

					IBlockState above = w.getBlockState(targetPos.up());
					IBlockState target = w.getBlockState(targetPos);

					if (target.getBlock() == Blocks.DIRT && target.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && w.getLightFromNeighbors( targetPos.up()) >= 4 && above.getLightOpacity(w, pos.up()) <= 2
							&& above.getBlock() != Blocks.WATER && above.getBlock() != Blocks.FLOWING_WATER) {
						w.setBlockState(targetPos, Blocks.GRASS.getDefaultState());
					}
				}
			}
		}
	}

}
