package com.elytradev.fondue.module.stoned;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedOreRecipeHighPriority extends ShapedOreRecipe {

	public ShapedOreRecipeHighPriority(Block result, Object... recipe) {
		super(result, recipe);
	}

	public ShapedOreRecipeHighPriority(Item result, Object... recipe) {
		super(result, recipe);
	}

	public ShapedOreRecipeHighPriority(ItemStack result, Object... recipe) {
		super(result, recipe);
	}
	
}
