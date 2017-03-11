package com.elytradev.fondue.module.stoned;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModuleStonedClient extends ModuleClient {

	@Override
	public String getName() {
		return "Stoned (Client)";
	}

	@Override
	public String getDescription() {
		return "Registers item models for the Stoned module.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.COBBLESTONE_SWORD, 0, new ModelResourceLocation("fondue:cobblestone_sword#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.COBBLESTONE_HOE, 0, new ModelResourceLocation("fondue:cobblestone_hoe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.COBBLESTONE_PICKAXE, 0, new ModelResourceLocation("fondue:cobblestone_pickaxe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.COBBLESTONE_AXE, 0, new ModelResourceLocation("fondue:cobblestone_axe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.COBBLESTONE_SHOVEL, 0, new ModelResourceLocation("fondue:cobblestone_shovel#inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.GRANITE_SWORD, 0, new ModelResourceLocation("fondue:granite_sword#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.GRANITE_HOE, 0, new ModelResourceLocation("fondue:granite_hoe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.GRANITE_PICKAXE, 0, new ModelResourceLocation("fondue:granite_pickaxe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.GRANITE_AXE, 0, new ModelResourceLocation("fondue:granite_axe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.GRANITE_SHOVEL, 0, new ModelResourceLocation("fondue:granite_shovel#inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.ANDESITE_SWORD, 0, new ModelResourceLocation("fondue:andesite_sword#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.ANDESITE_HOE, 0, new ModelResourceLocation("fondue:andesite_hoe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.ANDESITE_PICKAXE, 0, new ModelResourceLocation("fondue:andesite_pickaxe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.ANDESITE_AXE, 0, new ModelResourceLocation("fondue:andesite_axe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.ANDESITE_SHOVEL, 0, new ModelResourceLocation("fondue:andesite_shovel#inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.DIORITE_SWORD, 0, new ModelResourceLocation("fondue:diorite_sword#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.DIORITE_HOE, 0, new ModelResourceLocation("fondue:diorite_hoe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.DIORITE_PICKAXE, 0, new ModelResourceLocation("fondue:diorite_pickaxe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.DIORITE_AXE, 0, new ModelResourceLocation("fondue:diorite_axe#inventory"));
		ModelLoader.setCustomModelResourceLocation(ModuleStoned.DIORITE_SHOVEL, 0, new ModelResourceLocation("fondue:diorite_shovel#inventory"));
	}

}
