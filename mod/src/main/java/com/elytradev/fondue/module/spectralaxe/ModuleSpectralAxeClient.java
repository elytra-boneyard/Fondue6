package com.elytradev.fondue.module.spectralaxe;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModuleSpectralAxeClient extends ModuleClient {

	@Override
	public String getName() {
		return "Spectral Axe (Client)";
	}

	@Override
	public String getDescription() {
		return "Registers item models.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		ModelLoader.setCustomModelResourceLocation(ModuleSpectralAxe.SPECTRAL_AXE, 0, new ModelResourceLocation("fondue:spectral_axe#inventory"));
	}

}
