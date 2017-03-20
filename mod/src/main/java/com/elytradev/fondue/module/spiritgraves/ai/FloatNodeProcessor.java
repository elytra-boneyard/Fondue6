package com.elytradev.fondue.module.spiritgraves.ai;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;

// Horribly mutated Squid genes
public class FloatNodeProcessor extends NodeProcessor {

	@Override
	public PathPoint getStart() {
		return this.openPoint(
				MathHelper.floor(entity.getEntityBoundingBox().minX),
				MathHelper.floor(entity.getEntityBoundingBox().minY + 0.5),
				MathHelper.floor(entity.getEntityBoundingBox().minZ));
	}

	@Override
	public PathPoint getPathPointToCoords(double x, double y, double z) {
		return this.openPoint(
				MathHelper.floor(x - (entity.width / 2)),
				MathHelper.floor(y + 0.5),
				MathHelper.floor(z - (entity.width / 2)));
	}

	@Override
	public int findPathOptions(PathPoint[] pathOptions, PathPoint currentPoint,
			PathPoint targetPoint, float maxDistance) {
		int i = 0;

		for (EnumFacing enumfacing : EnumFacing.values()) {
			PathPoint point = getAirNode(
					currentPoint.xCoord + enumfacing.getFrontOffsetX(),
					currentPoint.yCoord + enumfacing.getFrontOffsetY(),
					currentPoint.zCoord + enumfacing.getFrontOffsetZ());

			if (point != null && !point.visited
					&& point.distanceTo(targetPoint) < maxDistance) {
				pathOptions[i++] = point;
			}
		}

		return i;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn) {
		return PathNodeType.OPEN;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z) {
		return PathNodeType.OPEN;
	}

	@Nullable
	private PathPoint getAirNode(int x, int y, int z) {
		PathNodeType pathnodetype = this.isFree(x, y, z);
		return pathnodetype == PathNodeType.OPEN ? openPoint(x, y, z) : null;
	}

	private PathNodeType isFree(int oX, int oY, int oZ) {
		MutableBlockPos pos = new MutableBlockPos();

		for (int x = oX; x < oX + entitySizeX; x++) {
			for (int y = oY; y < oY + entitySizeY; y++) {
				for (int z = oZ; z < oZ + entitySizeZ; z++) {
					IBlockState ibs = blockaccess.getBlockState(pos.setPos(x, y, z));

					if (!ibs.getBlock().isAir(ibs, blockaccess, pos)) {
						return PathNodeType.BLOCKED;
					}
				}
			}
		}

		return PathNodeType.OPEN;
	}

}
