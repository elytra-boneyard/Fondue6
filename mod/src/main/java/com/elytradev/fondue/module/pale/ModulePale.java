package com.elytradev.fondue.module.pale;

import com.elytradev.fondue.module.Module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;

public class ModulePale extends Module {

	public static Potion PALE;
	public static SoundEvent SIZZLE;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		GameRegistry.register(PALE = new PotionPale()
				.setRegistryName("pale")
				.setPotionName("effect.fondue.pale"));
		GameRegistry.register(SIZZLE = new SoundEvent(new ResourceLocation("fondue", "sizzle")).setRegistryName("sizzle"));
		
		try {
			GameRegistry.addSubstitutionAlias("minecraft:rotten_flesh", Type.ITEM,
				new ItemPaleRottenFlesh()
					.setUnlocalizedName("rottenFlesh")
					.setRegistryName("rotten_flesh"));
		} catch (ExistingSubstitutionException e1) {
			e1.printStackTrace();
		}
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onEntityInteract(EntityInteract e) {
		if (e.getTarget() instanceof EntityWolf &&
				e.getItemStack().getItem() instanceof ItemPaleRottenFlesh) {
			// poor doggo :(
			EntityWolf ew = (EntityWolf)e.getTarget();
			int dur = 3600;
			int lvl = 0;
			PotionEffect active = ew.getActivePotionEffect(PALE);
			if (active != null) {
				dur = active.getDuration()+200;
				lvl = active.getAmplifier()+1;
				if (lvl > 3) {
					lvl = 3;
				}
			}
			ew.addPotionEffect(new PotionEffect(PALE, dur, lvl));
			ew.heal(1);
			e.getItemStack().shrink(1);
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent e) {
		PotionEffect pale = e.getEntityLiving().getActivePotionEffect(PALE);
		if (pale != null) {
			if (e.getSource() instanceof EntityDamageSource) {
				Entity attacker = ((EntityDamageSource)e.getSource()).getEntity();
				if (attacker instanceof EntityLivingBase) {
					EntityLivingBase ec = (EntityLivingBase)attacker;
					float mult = 1;
					if (ec.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
						mult += ((pale.getAmplifier()+1)/5f);
					} else if (ec.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
						mult -= ((pale.getAmplifier()+1)/10f);
					}
					e.setAmount(e.getAmount()*mult);
				}
			}
		}
	}
	
}
