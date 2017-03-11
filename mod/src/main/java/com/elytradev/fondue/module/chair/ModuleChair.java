package com.elytradev.fondue.module.chair;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModuleChair extends Module {

	@Override
	public String getName() {
		return "Chair";
	}
	
	@Override
	public String getDescription() {
		return "Adds a Comfy Chair, which lets you skip the night if everyone online sits in one. Since you can still do things while sitting in it, it obsoletes mods like Morpheus.";
	}
	
	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA, Goal.ENCOURAGE_SOCIALIZATION, Goal.WORK_ON_SMALL_SERVERS);
	}
	
	public static Block CHAIR;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		GameRegistry.register(CHAIR = new BlockChair(Material.CLOTH)
				.setHardness(0.8f)
				.setRegistryName("chair")
				.setUnlocalizedName("fondue.chair")
				.setCreativeTab(CreativeTabs.DECORATIONS));
		GameRegistry.register(new ItemBlock(CHAIR).setRegistryName("chair"));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(CHAIR,
				"PW ",
				"PWW",
				"/ /",
				'P', "plankWood",
				'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
				'/', "stickWood"));
	}
	
}
