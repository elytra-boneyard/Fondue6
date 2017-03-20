package com.elytradev.fondue.module.spiritgraves;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

// Horribly mutated Enderman genes
public class EntityAIMagnetize extends EntityAINearestAttackableTarget<EntityPlayer> {
	private final EntityCreature entity;
	private EntityPlayer player;

	public EntityAIMagnetize(EntityCreature living) {
		 super(living, EntityPlayer.class, false);
		 this.entity = living;
	}

	@Override
	protected double getTargetDistance() {
		return 24;
	}
	
	@Override
	public boolean shouldExecute() {
		double dist = getTargetDistance();
		this.player = this.entity.world.getNearestAttackablePlayer(entity.posX, entity.posY, entity.posZ, dist, dist, null, (e) -> e != null && shouldAttackPlayer(e));
		return player != null;
	}
	
	private boolean shouldAttackPlayer(EntityPlayer player) {
		Vec3d look = player.getLook(1).normalize();
		Vec3d diff = new Vec3d(entity.posX - player.posX, (entity.getEntityBoundingBox().minY + entity.getEyeHeight()) - (player.posY + player.getEyeHeight()), entity.posZ - player.posZ);
		double len = diff.lengthVector();
		diff = diff.normalize();
		double dot = look.dotProduct(diff);
		return dot > 0.975 / len ? player.canEntityBeSeen(entity) : false;
	}

	@Override
	public void startExecuting() {
	}

	@Override
	public void resetTask() {
		player = null;
		super.resetTask();
	}

	@Override
	public boolean continueExecuting() {
		if (player != null) {
			if (!shouldAttackPlayer(player)) {
				return false;
			} else {
				return true;
			}
		} else {
			return targetEntity != null && targetEntity.isEntityAlive() ? true : continueExecuting();
		}
	}

	@Override
	public void updateTask() {
		if (player != null) {
			// SO I HEARD THAT YOU LIKED YOUR AI TO ACTUALLY WORK
			// TOO DAMN BAD
			Vec3d diff = new Vec3d(entity.posX - player.posX, (entity.getEntityBoundingBox().minY + entity.getEyeHeight()) - (player.posY + player.getEyeHeight()), entity.posZ - player.posZ);
			diff = diff.normalize().scale(0.05);
			entity.motionX = -diff.xCoord;
			entity.motionY = -diff.yCoord;
			entity.motionZ = -diff.zCoord;
		}
	}

}
