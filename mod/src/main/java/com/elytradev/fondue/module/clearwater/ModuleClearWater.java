package com.elytradev.fondue.module.clearwater;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		// This is HILARIOUSLY simple.
		Blocks.WATER.setLightOpacity(1);
		Blocks.FLOWING_WATER.setLightOpacity(1);
	}

}
