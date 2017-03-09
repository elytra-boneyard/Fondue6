package com.elytradev.fondue.module.yawfix;

import com.elytradev.concrete.Message;
import com.elytradev.concrete.NetworkContext;
import com.elytradev.concrete.annotation.field.MarshalledAs;
import com.elytradev.concrete.annotation.type.ReceivedOn;
import com.elytradev.fondue.Fondue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@ReceivedOn(Side.CLIENT)
public class AttackedAtYawMessage extends Message {

	@MarshalledAs("f32")
	private float yaw;
	
	public AttackedAtYawMessage(NetworkContext ctx) {
		super(ctx);
	}
	
	public AttackedAtYawMessage(float yaw) {
		super(Fondue.inst.network);
		this.yaw = yaw;
	}
	
	@Override
	protected void handle(EntityPlayer sender) {
		sender.attackedAtYaw = yaw;
	}

}
