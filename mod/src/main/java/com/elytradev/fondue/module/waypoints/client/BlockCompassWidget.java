package com.elytradev.fondue.module.waypoints.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class BlockCompassWidget extends CompassWidget {

	public BlockPos location;
	
	public BlockCompassWidget(BlockPos location) {
		this.location = location;
	}
	
	@Override
	public float getYaw() {
		double blockX = location.getX()+0.5;
		double blockZ = location.getZ()+0.5;
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		double playerX = player.lastTickPosX + (player.posX - player.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks();
		double playerZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks();
		
		double diffX = blockX-playerX;
		double diffZ = blockZ-playerZ;
		
		return (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX))-90);
	}

	@Override
	public double getDistanceSq() {
		return Minecraft.getMinecraft().player.getDistanceSqToCenter(location);
	}

}
