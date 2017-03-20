package com.elytradev.fondue.module.spiritgraves;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

// Horribly mutated Ghast genes
public class FloatMoveHelper extends EntityMoveHelper {
	private final EntityLiving parentEntity;
	private int courseChangeCooldown;

	public FloatMoveHelper(EntityLiving ghast) {
		super(ghast);
		this.parentEntity = ghast;
	}

	@Override
	public void onUpdateMoveHelper() {
		if (this.action == EntityMoveHelper.Action.MOVE_TO) {
			double dX = posX - parentEntity.posX;
			double dY = posY - parentEntity.posY;
			double dZ = posZ - parentEntity.posZ;
			double dist = dX * dX + dY * dY + dZ * dZ;

			if (courseChangeCooldown-- <= 0) {
				courseChangeCooldown += parentEntity.getRNG().nextInt(10) + 20;
				dist = MathHelper.sqrt(dist);

				if (isNotColliding(posX, posY, posZ, dist)) {
					parentEntity.motionX += dX / dist * 0.1;
					parentEntity.motionY += dY / dist * 0.1;
					parentEntity.motionZ += dZ / dist * 0.1;
				} else {
					action = EntityMoveHelper.Action.WAIT;
				}
			}
		}
	}

	private boolean isNotColliding(double x, double y, double z, double dist) {
		double d0 = (x - parentEntity.posX) / dist;
		double d1 = (y - parentEntity.posY) / dist;
		double d2 = (z - parentEntity.posZ) / dist;
		AxisAlignedBB bb = parentEntity.getEntityBoundingBox();

		for (int i = 1; i < dist; ++i) {
			bb = bb.offset(d0, d1, d2);

			if (!parentEntity.world.getCollisionBoxes(parentEntity, bb).isEmpty()) {
				return false;
			}
		}

		return true;
	}
}