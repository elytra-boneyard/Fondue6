package com.elytradev.fondue.module.obelisk;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.obelisk.client.ModuleObeliskClient;
import com.elytradev.fondue.module.waypoints.ModuleWaypoints;
import com.elytradev.fondue.module.waypoints.client.ModuleWaypointsClient;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityObelisk extends TileEntity {

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	@Override
	public void onLoad() {
		if (Fondue.isModuleLoaded(ModuleWaypointsClient.class)) {
			Fondue.getModule(ModuleWaypointsClient.class).onObeliskLoad(getWorld(), getPos());
		}
	}
	
	public void attune(EntityPlayer p) {
		if (p == null) return;
		p.playSound(ModuleObelisk.ATTUNE, 1f, 1.0f);
		
		if (!p.world.isRemote) {
			p.setSpawnPoint(p.getPosition(), true);
		} else if (Fondue.isModuleLoaded(ModuleObeliskClient.class)) {
			Fondue.getModule(ModuleObeliskClient.class).playAttuneEffect(this);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expand(5, 5, 5);
	}
	
}
