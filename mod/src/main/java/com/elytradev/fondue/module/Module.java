package com.elytradev.fondue.module;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class Module {

	public void onPreInit(FMLPreInitializationEvent e) {}
	public void onInit(FMLInitializationEvent e) {}
	public void onPostInit(FMLPostInitializationEvent e) {}
	
}
