package com.elytradev.fondue.module.furnacebread;

import java.util.Iterator;

import com.elytradev.fondue.module.Module;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModuleFurnaceBread extends Module {

	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		Iterator<IRecipe> iter = CraftingManager.getInstance().getRecipeList().iterator();
		while (iter.hasNext()) {
			IRecipe ir = iter.next();
			if (ir instanceof ShapedRecipes && ir.getRecipeOutput().getItem() == Items.BREAD) {
				iter.remove();
			}
		}
		
		FurnaceRecipes.instance().addSmelting(Items.WHEAT, new ItemStack(Items.BREAD), 0);
	}
	
}
