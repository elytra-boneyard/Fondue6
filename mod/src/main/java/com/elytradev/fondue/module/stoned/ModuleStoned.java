package com.elytradev.fondue.module.stoned;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class ModuleStoned extends Module {

	@Override
	public String getName() {
		return "Stoned";
	}

	@Override
	public String getDescription() {
		return "Makes granite, diorite, and andesite actually useful.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA);
	}
	
	public static ItemSword COBBLESTONE_SWORD;
	public static ItemHoe COBBLESTONE_HOE;
	public static ItemPickaxe COBBLESTONE_PICKAXE;
	public static ItemAxe COBBLESTONE_AXE;
	public static ItemSpade COBBLESTONE_SHOVEL;
	
	public static ItemSword GRANITE_SWORD;
	public static ItemHoe GRANITE_HOE;
	public static ItemPickaxe GRANITE_PICKAXE;
	public static ItemAxe GRANITE_AXE;
	public static ItemSpade GRANITE_SHOVEL;
	
	public static ItemSword ANDESITE_SWORD;
	public static ItemHoe ANDESITE_HOE;
	public static ItemPickaxe ANDESITE_PICKAXE;
	public static ItemAxe ANDESITE_AXE;
	public static ItemSpade ANDESITE_SHOVEL;
	
	public static ItemSword DIORITE_SWORD;
	public static ItemHoe DIORITE_HOE;
	public static ItemPickaxe DIORITE_PICKAXE;
	public static ItemAxe DIORITE_AXE;
	public static ItemSpade DIORITE_SHOVEL;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.STONE, 1, 1));
		OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.STONE, 1, 3));
		OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.STONE, 1, 5));
		
		GameRegistry.register(COBBLESTONE_SWORD = ((ItemSword)new ItemSword(ToolMaterial.STONE)
				.setUnlocalizedName("swordStone")
				.setRegistryName("cobblestone_sword")));
		GameRegistry.register(COBBLESTONE_HOE = ((ItemHoe)new ItemHoe(ToolMaterial.STONE)
				.setUnlocalizedName("hoeStone")
				.setRegistryName("cobblestone_hoe")));
		GameRegistry.register(COBBLESTONE_PICKAXE = ((ItemPickaxe)new ItemPickaxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("pickaxeStone")
				.setRegistryName("cobblestone_pickaxe")));
		GameRegistry.register(COBBLESTONE_AXE = ((ItemAxe)new ItemAxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("hatchetStone")
				.setRegistryName("cobblestone_axe")));
		GameRegistry.register(COBBLESTONE_SHOVEL = ((ItemSpade)new ItemSpade(ToolMaterial.STONE)
				.setUnlocalizedName("shovelStone")
				.setRegistryName("cobblestone_shovel")));
		
		GameRegistry.register(GRANITE_SWORD = ((ItemSword)new ItemSword(ToolMaterial.STONE)
				.setUnlocalizedName("swordStone")
				.setRegistryName("granite_sword")));
		GameRegistry.register(GRANITE_HOE = ((ItemHoe)new ItemHoe(ToolMaterial.STONE)
				.setUnlocalizedName("hoeStone")
				.setRegistryName("granite_hoe")));
		GameRegistry.register(GRANITE_PICKAXE = ((ItemPickaxe)new ItemPickaxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("pickaxeStone")
				.setRegistryName("granite_pickaxe")));
		GameRegistry.register(GRANITE_AXE = ((ItemAxe)new ItemAxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("hatchetStone")
				.setRegistryName("granite_axe")));
		GameRegistry.register(GRANITE_SHOVEL = ((ItemSpade)new ItemSpade(ToolMaterial.STONE)
				.setUnlocalizedName("shovelStone")
				.setRegistryName("granite_shovel")));
		
		GameRegistry.register(ANDESITE_SWORD = ((ItemSword)new ItemSword(ToolMaterial.STONE)
				.setUnlocalizedName("swordStone")
				.setRegistryName("andesite_sword")));
		GameRegistry.register(ANDESITE_HOE = ((ItemHoe)new ItemHoe(ToolMaterial.STONE)
				.setUnlocalizedName("hoeStone")
				.setRegistryName("andesite_hoe")));
		GameRegistry.register(ANDESITE_PICKAXE = ((ItemPickaxe)new ItemPickaxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("pickaxeStone")
				.setRegistryName("andesite_pickaxe")));
		GameRegistry.register(ANDESITE_AXE = ((ItemAxe)new ItemAxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("hatchetStone")
				.setRegistryName("andesite_axe")));
		GameRegistry.register(ANDESITE_SHOVEL = ((ItemSpade)new ItemSpade(ToolMaterial.STONE)
				.setUnlocalizedName("shovelStone")
				.setRegistryName("andesite_shovel")));
		
		GameRegistry.register(DIORITE_SWORD = ((ItemSword)new ItemSword(ToolMaterial.STONE)
				.setUnlocalizedName("swordStone")
				.setRegistryName("diorite_sword")));
		GameRegistry.register(DIORITE_HOE = ((ItemHoe)new ItemHoe(ToolMaterial.STONE)
				.setUnlocalizedName("hoeStone")
				.setRegistryName("diorite_hoe")));
		GameRegistry.register(DIORITE_PICKAXE = ((ItemPickaxe)new ItemPickaxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("pickaxeStone")
				.setRegistryName("diorite_pickaxe")));
		GameRegistry.register(DIORITE_AXE = ((ItemAxe)new ItemAxe(ToolMaterial.STONE) {}
				.setUnlocalizedName("hatchetStone")
				.setRegistryName("diorite_axe")));
		GameRegistry.register(DIORITE_SHOVEL = ((ItemSpade)new ItemSpade(ToolMaterial.STONE)
				.setUnlocalizedName("shovelStone")
				.setRegistryName("diorite_shovel")));
		
		RecipeSorter.register("fondue:shaped_ore_high_priority", ShapedOreRecipeHighPriority.class, Category.SHAPED, "before:minecraft:shaped");
		
		ItemStack cobble = new ItemStack(Blocks.COBBLESTONE, 1, 0);
		ItemStack granite = new ItemStack(Blocks.STONE, 1, 1);
		ItemStack diorite = new ItemStack(Blocks.STONE, 1, 3);
		ItemStack andesite = new ItemStack(Blocks.STONE, 1, 5);
		
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_SWORD,
				" # ",
				" # ",
				" / ",
				
				'#', cobble,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_HOE,
				"##",
				" /",
				" /",
				
				'#', cobble,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_HOE,
				"##",
				"/ ",
				"/ ",
				
				'#', cobble,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_PICKAXE,
				"###",
				" / ",
				" / ",
				
				'#', cobble,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_AXE,
				"##",
				"#/",
				" /",
				
				'#', cobble,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_AXE,
				"##",
				"/#",
				"/ ",
				
				'#', cobble,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(COBBLESTONE_SHOVEL,
				"#",
				"/",
				"/",
				
				'#', cobble,
				'/', "stickWood"
				));
		
		
		
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_SWORD,
				" # ",
				" # ",
				" / ",
				
				'#', granite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_HOE,
				"##",
				" /",
				" /",
				
				'#', granite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_HOE,
				"##",
				"/ ",
				"/ ",
				
				'#', granite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_PICKAXE,
				"###",
				" / ",
				" / ",
				
				'#', granite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_AXE,
				"##",
				"#/",
				" /",
				
				'#', granite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_AXE,
				"##",
				"/#",
				"/ ",
				
				'#', granite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(GRANITE_SHOVEL,
				"#",
				"/",
				"/",
				
				'#', granite,
				'/', "stickWood"
				));
		
		
		
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_SWORD,
				" # ",
				" # ",
				" / ",
				
				'#', andesite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_HOE,
				"##",
				" /",
				" /",
				
				'#', andesite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_HOE,
				"##",
				"/ ",
				"/ ",
				
				'#', andesite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_PICKAXE,
				"###",
				" / ",
				" / ",
				
				'#', andesite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_AXE,
				"##",
				"#/",
				" /",
				
				'#', andesite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_AXE,
				"##",
				"/#",
				"/ ",
				
				'#', andesite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(ANDESITE_SHOVEL,
				"#",
				"/",
				"/",
				
				'#', andesite,
				'/', "stickWood"
				));
		
		
		
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_SWORD,
				" # ",
				" # ",
				" / ",
				
				'#', diorite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_HOE,
				"##",
				" /",
				" /",
				
				'#', diorite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_HOE,
				"##",
				"/ ",
				"/ ",
				
				'#', diorite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_PICKAXE,
				"###",
				" / ",
				" / ",
				
				'#', diorite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_AXE,
				"##",
				"#/",
				" /",
				
				'#', diorite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_AXE,
				"##",
				"/#",
				"/ ",
				
				'#', diorite,
				'/', "stickWood"
				));
		GameRegistry.addRecipe(new ShapedOreRecipeHighPriority(DIORITE_SHOVEL,
				"#",
				"/",
				"/",
				
				'#', diorite,
				'/', "stickWood"
				));
	}

}
