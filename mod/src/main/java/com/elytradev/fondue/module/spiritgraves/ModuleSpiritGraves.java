package com.elytradev.fondue.module.spiritgraves;

import java.util.Set;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModuleSpiritGraves extends Module {

	@Override
	public String getName() {
		return "Spirit Graves";
	}

	@Override
	public String getDescription() {
		return "Adds balls of smoke which can be broken to get your items back in the same slots they were in originally, and that pathfind toward the surface slowly.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA, Goal.REDUCE_FRICTION, Goal.BE_UNIQUE);
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		EntityRegistry.registerModEntity(new ResourceLocation("fondue", "spirit_grave"), EntityGrave.class, "spirit_grave", 0, Fondue.inst, 64, 2, true);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDeath(LivingDeathEvent e) {
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)e.getEntity();
			EntityGrave grave = new EntityGrave(e.getEntity().world);
			grave.setPosition(player.posX, player.posY, player.posZ);
			grave.populateFrom(player, true);
			player.world.spawnEntity(grave);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onDrops(PlayerDropsEvent e) {
		EntityGrave eg = (EntityGrave)e.getEntityPlayer().world.findNearestEntityWithinAABB(EntityGrave.class, e.getEntityPlayer().getEntityBoundingBox().expand(0.5, 0.5, 0.5), e.getEntityPlayer());
		if (eg != null) {
			for (EntityItem ei : e.getDrops()) {
				eg.addExtra(ei.getEntityItem());
				ei.setDead();
			}
			e.getDrops().clear();
		}
	}

}
