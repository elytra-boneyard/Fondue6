package com.elytradev.fondue.module.spiritgraves.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

// Horribly mutated Squid genes
public class PathNavigateFloater extends PathNavigate {

	public PathNavigateFloater(EntityLiving entity) {
		super(entity, entity.world);
	}

	@Override
	protected PathFinder getPathFinder() {
		return new PathFinder(new FloatNodeProcessor());
	}

	@Override
	protected boolean canNavigate() {
		return true;
	}

	@Override
	protected Vec3d getEntityPosition() {
		return new Vec3d(theEntity.posX, theEntity.posY + theEntity.height * 0.5, theEntity.posZ);
	}

	@Override
	protected void pathFollow() {
		Vec3d pos = getEntityPosition();
		float widthSq = theEntity.width * theEntity.width;

		if (pos.squareDistanceTo(currentPath.getVectorFromIndex(theEntity, currentPath.getCurrentPathIndex())) < widthSq) {
			currentPath.incrementPathIndex();
		}

		for (int i = Math.min(currentPath.getCurrentPathIndex() + 6, currentPath.getCurrentPathLength() - 1); i > currentPath.getCurrentPathIndex(); --i) {
			Vec3d pathVec = currentPath.getVectorFromIndex(theEntity, i);

			if (pathVec.squareDistanceTo(pos) <= 36 && isDirectPathBetweenPoints(pos, pathVec, 0, 0, 0)) {
				currentPath.setCurrentPathIndex(i);
				break;
			}
		}

		checkForStuck(pos);
	}

	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d a, Vec3d b, int sizeX, int sizeY, int sizeZ) {
		RayTraceResult rtr = world.rayTraceBlocks(a, b, false, true, false);
		return rtr == null || rtr.typeOfHit == RayTraceResult.Type.MISS;
	}

	@Override
	public boolean canEntityStandOnPos(BlockPos pos) {
		return true;
	}
}
