package com.elytradev.fondue.module.obelisk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityObelisk extends TileEntity {

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	public void attune(EntityPlayer p) {
		if (p == null) return;
		p.playSound(ModuleObelisk.ATTUNE, 1f, 1.0f);
		
		if (!p.world.isRemote) {
			p.setSpawnPoint(p.getPosition(), true);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expand(3, 0, 3).addCoord(0, -3, 0);
	}
	
}
