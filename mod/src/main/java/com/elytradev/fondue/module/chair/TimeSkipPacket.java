package com.elytradev.fondue.module.chair;

import com.elytradev.concrete.Message;
import com.elytradev.concrete.NetworkContext;
import com.elytradev.concrete.annotation.type.ReceivedOn;
import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.chair.client.ModuleChairClient;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@ReceivedOn(Side.CLIENT)
public class TimeSkipPacket extends Message {

	private boolean state;
	
	public TimeSkipPacket(NetworkContext ctx) {
		super(ctx);
	}
	
	public TimeSkipPacket(boolean state) {
		super(Fondue.inst.network);
		this.state = state;
	}
	
	@Override
	protected void handle(EntityPlayer sender) {
		ModuleChairClient.timeskipping = state;
	}

}
