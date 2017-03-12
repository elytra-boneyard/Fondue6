package com.elytradev.fondue.module.instantpickup;

import java.util.Iterator;
import java.util.Set;

import com.elytradev.concrete.reflect.accessor.Accessor;
import com.elytradev.concrete.reflect.accessor.Accessors;
import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleInstantPickup extends Module {

	@Override
	public String getName() {
		return "Instant Pickup";
	}
	
	@Override
	public String getDescription() {
		return "Makes you instantly pick up the drops of blocks you break and mobs you kill if you have inventory room.";
	}
	
	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA);
	}
	
	private final Accessor<Integer> delayBeforeCanPickup = Accessors.findField(EntityItem.class, "field_145804_b", "delayBeforeCanPickup", "e");
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDrops(LivingDropsEvent e) {
		if (!e.getEntityLiving().world.getGameRules().getBoolean("doMobLoot")) return;
		if (e.getEntityLiving().world.isRemote) return;
		if (e.getSource() instanceof EntityDamageSource) {
			EntityDamageSource eds = (EntityDamageSource)e.getSource();
			if (eds.getEntity() instanceof EntityPlayer) {
				EntityPlayer ep = (EntityPlayer)eds.getEntity();
				Iterator<EntityItem> iter = e.getDrops().iterator();
				while (iter.hasNext()) {
					EntityItem ei = iter.next();
					int oldPickupDelay = delayBeforeCanPickup.get(ei);
					ei.setNoPickupDelay();
					e.getEntityLiving().world.spawnEntity(ei);
					ei.onCollideWithPlayer(ep);
					if (!ei.isDead) {
						ei.setPickupDelay(oldPickupDelay);
					} else {
						iter.remove();
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onBreak(BlockEvent.BreakEvent e) {
		if (e.getWorld().restoringBlockSnapshots) return;
		if (!e.getWorld().getGameRules().getBoolean("doTileDrops")) return;
		if (e.getWorld().isRemote) return;
		if (e.getPlayer() == null) return;
		BlockPos pos = e.getPos();
		int amount = e.getExpToDrop();
		int oldCooldown = e.getPlayer().xpCooldown;
		while (amount > 0) {
			int i = EntityXPOrb.getXPSplit(amount);
			amount -= i;
			EntityXPOrb exp = new EntityXPOrb(e.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, i);
			e.getPlayer().xpCooldown = 0;
			int oldDelay = exp.delayBeforeCanPickup;
			exp.delayBeforeCanPickup = 0;
			e.getWorld().spawnEntity(exp);
			exp.onCollideWithPlayer(e.getPlayer());
			if (!exp.isDead) {
				exp.delayBeforeCanPickup = oldDelay;
			}
		}
		e.getPlayer().xpCooldown = oldCooldown;
		e.setExpToDrop(0);
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDrops(BlockEvent.HarvestDropsEvent e) {
		if (e.getWorld().restoringBlockSnapshots) return;
		if (!e.getWorld().getGameRules().getBoolean("doTileDrops")) return;
		if (e.getWorld().isRemote) return;
		if (e.getHarvester() == null) return;
		BlockPos pos = e.getPos();
		for (ItemStack is : e.getDrops()) {
			if (is.isEmpty()) continue;
			if (e.getWorld().rand.nextFloat() <= e.getDropChance()) {
				double xOfs = e.getWorld().rand.nextFloat() * 0.5 + 0.25;
				double yOfs = e.getWorld().rand.nextFloat() * 0.5 + 0.25;
				double zOfs = e.getWorld().rand.nextFloat() * 0.5 + 0.25;
				EntityItem ei = new EntityItem(e.getWorld(), pos.getX()+xOfs, pos.getY()+yOfs, pos.getZ()+zOfs, is);
				ei.setNoPickupDelay();
				e.getWorld().spawnEntity(ei);
				ei.onCollideWithPlayer(e.getHarvester());
				if (!ei.isDead) {
					ei.setDefaultPickupDelay();
				}
			}
		}
		e.getDrops().clear();
	}
	
}
