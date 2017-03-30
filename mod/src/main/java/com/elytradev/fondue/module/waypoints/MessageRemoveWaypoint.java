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
import net.minecraft.util.math.BlockPos;

@ReceivedOn(Side.CLIENT)
public class MessageRemoveWaypoint extends Message {

	private BlockPos pos;
	
	public MessageRemoveWaypoint(NetworkContext ctx) {
		super(ctx);
	}
	
	public MessageRemoveWaypoint(BlockPos pos) {
		super(Fondue.inst.network);
		this.pos = pos;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void handle(EntityPlayer sender) {
		Fondue.getModule(ModuleWaypointsClient.class).removeWaypoint(pos);
	}

}
