package com.elytradev.fondue.module.waypoints;

import com.elytradev.concrete.Message;
import com.elytradev.concrete.NetworkContext;
import com.elytradev.concrete.annotation.type.ReceivedOn;
import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.waypoints.client.ModuleWaypointsClient;
import com.google.common.collect.Lists;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

@ReceivedOn(Side.CLIENT)
public class MessageUpdateWaypoint extends Message {

	private WaypointData data;
	
	public MessageUpdateWaypoint(NetworkContext ctx) {
		super(ctx);
	}
	
	public MessageUpdateWaypoint(WaypointData data) {
		super(Fondue.inst.network);
		this.data = data;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void handle(EntityPlayer sender) {
		Fondue.getModule(ModuleWaypointsClient.class).updateWaypoint(data);
	}

}
