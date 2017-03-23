package com.elytradev.fondue.module.spiritgraves.client;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleSmokeSeekEntity extends ParticleSmokeNormal {

	private Entity seek;
	
	public ParticleSmokeSeekEntity(Entity seek, World worldIn, double xCoordIn,
			double yCoordIn, double zCoordIn, double p_i46348_8_,
			double p_i46348_10_, double p_i46348_12_, float p_i46348_14_) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46348_8_, p_i46348_10_,
				p_i46348_12_, p_i46348_14_);
		this.seek = seek;
		particleMaxAge *= 2;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		Vec3d diff = seek.getPositionVector().addVector(0, seek.height/3, 0).subtract(posX, posY, posZ);
		
		double targetX = diff.xCoord;
		double targetY = diff.yCoord;
		double targetZ = diff.zCoord;
		double diffX = targetX-motionX;
		double diffY = targetY-motionY;
		double diffZ = targetZ-motionZ;
		motionX += (diffX/96);
		motionY += (diffY/96);
		motionZ += (diffZ/96);
	}

	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
	}

}
