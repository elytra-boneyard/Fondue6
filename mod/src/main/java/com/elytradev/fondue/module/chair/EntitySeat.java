package com.elytradev.fondue.module.chair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class EntitySeat extends Entity {

	public EntitySeat(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {

	}
	
	@Override
	protected void addPassenger(Entity passenger) {
		super.addPassenger(passenger);
		if (world.isRemote) return;
		if (passenger instanceof EntityPlayer) {
			ModuleChair.lounging.add((EntityPlayer)passenger);
			ITextComponent component = passenger.getDisplayName();
			int pct = (int)((ModuleChair.lounging.size()/(double)world.playerEntities.size())*100);
			for (EntityPlayer player : world.playerEntities) {
				player.sendMessage(new TextComponentTranslation("fondue.startLounging", component, pct).setStyle(new Style().setColor(TextFormatting.GOLD)));
			}
		}
	}
	
	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (world.isRemote) return;
		if (passenger instanceof EntityPlayer) {
			ModuleChair.lounging.remove(passenger);
			ITextComponent component = passenger.getDisplayName();
			int pct = (int)((ModuleChair.lounging.size()/(double)world.playerEntities.size())*100);
			for (EntityPlayer player : world.playerEntities) {
				player.sendMessage(new TextComponentTranslation("fondue.stopLounging", component, pct).setStyle(new Style().setColor(TextFormatting.GOLD)));
			}
		}
	}

}
