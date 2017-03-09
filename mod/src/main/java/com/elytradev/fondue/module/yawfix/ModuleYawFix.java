package com.elytradev.fondue.module.yawfix;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.mutable.MutableFloat;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.Module;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModuleYawFix extends Module {

	public static final float EPSILON = 0.05f;
	
	private Map<EntityPlayer, MutableFloat> lastAttackedAtYaw = new WeakHashMap<>();
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		super.onPreInit(e);
		Fondue.inst.network.register(AttackedAtYawMessage.class);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		if (e.phase == Phase.END && !e.player.world.isRemote && e.player instanceof EntityPlayerMP) {
			if (!lastAttackedAtYaw.containsKey(e.player)) {
				lastAttackedAtYaw.put(e.player, new MutableFloat(e.player.attackedAtYaw));
			}
			if (Math.abs(lastAttackedAtYaw.get(e.player).floatValue()-e.player.attackedAtYaw) > EPSILON) {
				new AttackedAtYawMessage(e.player.attackedAtYaw).sendTo(e.player);;
				lastAttackedAtYaw.get(e.player).setValue(e.player.attackedAtYaw);
			}
		}
}
	
}
