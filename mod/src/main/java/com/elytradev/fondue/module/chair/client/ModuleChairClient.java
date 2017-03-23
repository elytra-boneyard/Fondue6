package com.elytradev.fondue.module.chair.client;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.elytradev.fondue.module.chair.EntitySeat;
import com.elytradev.fondue.module.chair.ModuleChair;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class ModuleChairClient extends ModuleClient {

	public static boolean timeskipping;

	@Override
	public String getName() {
		return "Chair (Client)";
	}

	@Override
	public String getDescription() {
		return "Client-side stuff for the Chair module.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		Item chair = Item.getItemFromBlock(ModuleChair.CHAIR);
		ModelLoader.setCustomModelResourceLocation(chair, 0, new ModelResourceLocation(chair.getRegistryName(), "inventory"));
		RenderingRegistry.registerEntityRenderingHandler(EntitySeat.class, RenderNull::new);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if (e.phase == Phase.START) {
			if (timeskipping) {
				World w = Minecraft.getMinecraft().world;
				w.setWorldTime(((w.getWorldTime()/ModuleChair.TIMESKIP_RATE)*ModuleChair.TIMESKIP_RATE)+ModuleChair.TIMESKIP_RATE);
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent e) {
		if (e.phase == Phase.START) {
			if (timeskipping) {
				World w = Minecraft.getMinecraft().world;
				w.setWorldTime((long)(((w.getWorldTime()/ModuleChair.TIMESKIP_RATE)*ModuleChair.TIMESKIP_RATE)+(e.renderTickTime*ModuleChair.TIMESKIP_RATE)));
			}
		}
	}

}
