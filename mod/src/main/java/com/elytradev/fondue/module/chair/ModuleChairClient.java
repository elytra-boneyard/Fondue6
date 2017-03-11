package com.elytradev.fondue.module.chair;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModuleChairClient extends ModuleClient {

	@Override
	public String getName() {
		return "Chair (Client)";
	}

	@Override
	public String getDescription() {
		return "Registers item models for the Chair module.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		Item chair = Item.getItemFromBlock(ModuleChair.CHAIR);
		ModelLoader.setCustomModelResourceLocation(chair, 0, new ModelResourceLocation(chair.getRegistryName(), "inventory"));
	}

}
