package com.elytradev.fondue.module.obelisk;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class GenerateObelisk {

	public static final int REGION_SIZE = 64;
	
	public static void generate(int chunkX, int chunkZ, long worldSeed, ChunkPrimer c) {
		if (isObeliskChunk(worldSeed, chunkX, chunkZ)) {
			int y = c.findGroundBlockIdx(8, 8) - 1;
			IBlockState bs = ModuleObelisk.OBELISK_BLOCK.getDefaultState().withProperty(BlockObelisk.CONTROLLER, false);
			
			generateBase(c, y, bs, true);
			
			int cur = y - 1;
			while (cur > 0) {
				generateBase(c, cur, bs, true);
				cur--;
			}
			
			c.setBlockState(8, y+1, 8, bs);
			c.setBlockState(8, y+2, 8, bs);
		}
	}
	
	private static boolean generateBase(ChunkPrimer c, int y, IBlockState bs, boolean overwrite) {
		boolean result = false;
		
		result |= setBlockState(c, 7, y+0, 6, bs, overwrite);
		result |= setBlockState(c, 8, y+0, 6, bs, overwrite);
		result |= setBlockState(c, 9, y+0, 6, bs, overwrite);
		
		result |= setBlockState(c, 6, y+0, 7, bs, overwrite);
		result |= setBlockState(c, 7, y+0, 7, bs, overwrite);
		result |= setBlockState(c, 8, y+0, 7, bs, overwrite);
		result |= setBlockState(c, 9, y+0, 7, bs, overwrite);
		result |= setBlockState(c, 10, y+0, 7, bs, overwrite);
		
		result |= setBlockState(c, 6, y+0, 8, bs, overwrite);
		result |= setBlockState(c, 7, y+0, 8, bs, overwrite);
		result |= setBlockState(c, 8, y+0, 8, bs, overwrite);
		result |= setBlockState(c, 9, y+0, 8, bs, overwrite);
		result |= setBlockState(c, 10, y+0, 8, bs, overwrite);
		
		result |= setBlockState(c, 6, y+0, 9, bs, overwrite);
		result |= setBlockState(c, 7, y+0, 9, bs, overwrite);
		result |= setBlockState(c, 8, y+0, 9, bs, overwrite);
		result |= setBlockState(c, 9, y+0, 9, bs, overwrite);
		result |= setBlockState(c, 10, y+0, 9, bs, overwrite);
		
		result |= setBlockState(c, 7, y+0, 10, bs, overwrite);
		result |= setBlockState(c, 8, y+0, 10, bs, overwrite);
		result |= setBlockState(c, 9, y+0, 10, bs, overwrite);
		
		return result;
	}

	private static boolean setBlockState(ChunkPrimer c, int x, int y, int z, IBlockState bs, boolean overwrite) {
		if (!overwrite) {
			if (c.getBlockState(x, y, z).getBlock() != Blocks.AIR) return false;
		}
		c.setBlockState(x, y, z, bs);
		return true;
	}

	public static void populate(int chunkX, int chunkZ, long worldSeed, World w) {
		if (isObeliskChunk(worldSeed, chunkX, chunkZ)) {
			int x = ((chunkX*16)+8);
			int z = ((chunkZ*16)+8);
			MutableBlockPos pos = new MutableBlockPos(x, 0, z);
			for (int y = w.getHeight(); y >= 0; y--) {
				pos.setY(y);
				if (w.getBlockState(pos).getBlock() == ModuleObelisk.OBELISK_BLOCK) {
					y++;
					pos.setY(y);
					w.setBlockState(pos, ModuleObelisk.OBELISK_BLOCK.getDefaultState().withProperty(BlockObelisk.CONTROLLER, true));
					break;
				}
			}
		}
	}

	public static boolean isObeliskChunk(long worldSeed, int chunkX, int chunkZ) {
		int regionSizeHalf = REGION_SIZE/2;
		
		if (chunkX < 0) chunkX -= REGION_SIZE;
		if (chunkZ < 0) chunkZ -= REGION_SIZE;
		
		int regionX = chunkX/REGION_SIZE;
		int regionZ = chunkZ/REGION_SIZE;
		
		long hash = hash(regionX, regionZ);
		
		hash += worldSeed*31;
		
		Random regionRandom = new Random(hash);
		
		int fuzz = 8;
		
		int signumX = regionX < 0 ? -1 : 1;
		int signumZ = regionZ < 0 ? -1 : 1;
		
		double xMult = ((regionRandom.nextDouble())-0.5)*2;
		double zMult = ((regionRandom.nextDouble())-0.5)*2;
		
		int xFuzz = (int)(xMult*fuzz);
		int zFuzz = (int)(zMult*fuzz);
		
		int desiredX = ((regionX*REGION_SIZE)+(regionSizeHalf*signumX))+xFuzz;
		int desiredZ = ((regionZ*REGION_SIZE)+(regionSizeHalf*signumZ))+zFuzz;
		
		return chunkX == desiredX && chunkZ == desiredZ;
	}
	
	// thanks Falkreon!
	public static long hash(int x, int z) {
		long xi = swizzle(x) * 452930477L;
		long zi = swizzle(z) * 715225741L;
		long interleave = 2*xi | (2*zi + 1);
		
		return interleave;
	}
	
	public static long swizzle(long i) {
		if (i >= 0) {
			return 2 * i;
		} else {
			return -2 * i - 1;
		}
	}
	
	// Generate a bunch of obelisk maps for a number of world seeds, for debugging.
	public static void main(String[] args) throws Exception {
		for (int seed = 0; seed < 12000; seed += 1000) {
			BufferedImage bi = new BufferedImage(REGION_SIZE*11, REGION_SIZE*11, BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < REGION_SIZE*11; x++) {
				for (int z = 0; z < REGION_SIZE*11; z++) {
					int regionX = x/REGION_SIZE;
					int regionZ = z/REGION_SIZE;
					float r = 1;
					float g = 1;
					float b = 1;
					
					if (regionX == 5 || regionZ == 5) {
						r = 0;
						g = 1;
						b = 1;
					}
					
					if (regionZ % 2 == 0) {
						if (regionX % 2 == 0) {
							r *= 0.2f;
							g *= 0.2f;
							b *= 0.2f;
						} else {
							r *= 0.1f;
							g *= 0.1f;
							b *= 0.1f;
						}
					} else if (regionX % 2 == 1) {
						r *= 0.2f;
						g *= 0.2f;
						b *= 0.2f;
					} else {
						r *= 0.1f;
						g *= 0.1f;
						b *= 0.1f;
					}
					if (isObeliskChunk(seed, x-(REGION_SIZE*5), z-(REGION_SIZE*5))) {
						r = 1;
					}
					int rgb = 0xFF000000;
					rgb |= (int)((r*255))<<16;
					rgb |= (int)((g*255))<<8;
					rgb |= (int)((b*255));
					bi.setRGB(x, z, rgb);
				}
			}
			ImageIO.write(bi, "PNG", new File("test-"+seed+".png"));
		}
	}

}
