package com.elytradev.fondue;

import com.elytradev.fondue.module.Module;
import com.elytradev.fondue.module.furnacebread.ModuleFurnaceBread;
import com.elytradev.fondue.module.instantpickup.ModuleInstantPickup;
import com.elytradev.fondue.module.pale.ModulePale;
import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="fondue", name="Fondue", version="@VERSION@")
public class Fondue {
	
	private final ImmutableList<Module> modules = ImmutableList.of(
			new ModuleFurnaceBread(),
			new ModuleInstantPickup(),
			new ModulePale()
			);
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		for (Module m : modules) {
			m.onPreInit(e);
		}
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		for (Module m : modules) {
			m.onInit(e);
		}
	}
	
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent e) {
		for (Module m : modules) {
			m.onPostInit(e);
		}
	}
	
}
