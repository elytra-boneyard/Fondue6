package com.elytradev.fondue.module.rezsickness;

import java.util.Collections;
import java.util.Set;
import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModuleResurrectionSickness extends Module {

	@Override
	public String getName() {
		return "Resurrection Sickness";
	}
	
	@Override
	public String getDescription() {
		return "Gives you Resurrection Sickness for 15 minutes after respawning.";
	}
	
	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.ENCOURAGE_INFRASTRUCTURE);
	}
	
	public static Potion RESURRECTION_SICKNESS;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		GameRegistry.register(RESURRECTION_SICKNESS = new PotionResurrectionSickness()
				.setPotionName("effect.fondue.resurrection_sickness")
				.setRegistryName("resurrection_sickness")
				.registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "CC5AF142-2BD2-4215-B636-2605AED11727", -4, 0)
				.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15, 2)
				.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.2, 2));
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		PotionEffect sickness = new PotionEffect(RESURRECTION_SICKNESS, 18000);
		sickness.setCurativeItems(Collections.emptyList());
		e.player.addPotionEffect(sickness);
		e.player.setHealth(e.player.getMaxHealth()/2);
		// Death is NOT a viable food source.
		e.player.getFoodStats().setFoodLevel(8);
	}
	
}
