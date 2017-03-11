package com.elytradev.fondue.module.odtweaks;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOreDictionaryTweaks extends Module {

	@Override
	public String getName() {
		return "Ore Dictionary Tweaks";
	}

	@Override
	public String getDescription() {
		return "Tweaks some ore dictionary stuff. Mainly adds Granite, Andesite, and Diorite as cobblestone equivalents.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA);
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.STONE, 1, 1));
		OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.STONE, 1, 3));
		OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.STONE, 1, 5));
		
		OreDictionary.registerOre("stone", new ItemStack(Blocks.STONE, 1, 2));
		OreDictionary.registerOre("stone", new ItemStack(Blocks.STONE, 1, 4));
		OreDictionary.registerOre("stone", new ItemStack(Blocks.STONE, 1, 6));
	}

}
