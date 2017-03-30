package com.elytradev.fondue.module.waypoints;

import java.util.List;

import com.elytradev.concrete.Message;
import com.elytradev.concrete.NetworkContext;
import com.elytradev.concrete.annotation.field.MarshalledAs;
import com.elytradev.concrete.annotation.type.ReceivedOn;
import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.waypoints.client.ModuleWaypointsClient;
import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ReceivedOn(Side.CLIENT)
public class MessageSetWaypoints extends Message {

	@MarshalledAs("com.elytradev.fondue.module.waypoints.WaypointDataMarshaller-list")
	private List<WaypointData> waypoints;
	
	public MessageSetWaypoints(NetworkContext ctx) {
		super(ctx);
	}
	
	public MessageSetWaypoints(Iterable<WaypointData> iter) {
		super(Fondue.inst.network);
		waypoints = Lists.newArrayList(iter);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void handle(EntityPlayer sender) {
		Fondue.getModule(ModuleWaypointsClient.class).setWaypoints(waypoints);
	}

}
