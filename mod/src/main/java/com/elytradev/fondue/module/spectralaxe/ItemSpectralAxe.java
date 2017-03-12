package com.elytradev.fondue.module.spectralaxe;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpectralAxe extends ItemAxe {

	protected ItemSpectralAxe(ToolMaterial material, float damage, float speed) {
		super(material, damage, speed);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(I18n.format("item.fondue.spectral_axe.hint"));
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		// override to not take durability damage
		return true;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (getStrVsBlock(stack, state) == efficiencyOnProperMaterial) {
			// only damage the tool if the block was one it's good at breaking
			// mainly intended to prevent damage when breaking leaves
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		} else {
			return true;
		}
	}
	
}
