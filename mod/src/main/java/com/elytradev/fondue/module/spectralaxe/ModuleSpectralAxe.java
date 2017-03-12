package com.elytradev.fondue.module.spectralaxe;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModuleSpectralAxe extends Module {

	@Override
	public String getName() {
		return "Spectral Axe";
	}

	@Override
	public String getDescription() {
		return "Gives players who have joined for the first time a Spectral Axe, to make initial wood gathering less annoying.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.REDUCE_FRICTION);
	}
	
	public static ItemSpectralAxe SPECTRAL_AXE;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		ToolMaterial mat = EnumHelper.addToolMaterial("fondue_spectral_axe", 0, 63, 10, 0, 0);
		GameRegistry.register(SPECTRAL_AXE = (ItemSpectralAxe) new ItemSpectralAxe(mat, 0, 0)
				.setUnlocalizedName("fondue.spectral_axe")
				.setRegistryName("spectral_axe")
				.setNoRepair());
				
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onLogIn(PlayerLoggedInEvent e) {
		if (!e.player.getEntityData().getBoolean("fondue:seen")) {
			e.player.inventory.addItemStackToInventory(new ItemStack(SPECTRAL_AXE, 1, 0));
			e.player.getEntityData().setBoolean("fondue:seen", true);
		}
	}

}
