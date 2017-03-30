package com.elytradev.fondue.module.obelisk.client;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.elytradev.fondue.module.obelisk.TileEntityObelisk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RenderObelisk extends TileEntitySpecialRenderer<TileEntityObelisk> {

	public static float getTime(World world, BlockPos pos, float partialTicks) {
		long l = world.getTotalWorldTime();
		l += pos.hashCode();
		return ((l%24000)+partialTicks)/20f;
	}
	
	public static long getSeed(BlockPos pos) {
		long l = 31;
		l += (31L * pos.getX());
		l += (31L * pos.getY());
		l += (31L * pos.getZ());
		return l;
	}
	
	public static float selectColor(Random rand, float sin, float cos) {
		switch (rand.nextInt(6)) {
			case 0: return 0;
			case 1: return sin;
			case 2: return cos;
			case 3: return sin/2;
			case 4: return cos/2;
			case 5: return 0.5f;
		}
		return 0;
	}
	
	private Random rand = new Random();
	
	@Override
	public void renderTileEntityAt(TileEntityObelisk te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		
		float t = getTime(te.getWorld(), te.getPos(), partialTicks);
		
		float sin = (MathHelper.sin(t)+2)/3;
		float cos = (MathHelper.cos(t)+2)/3;
		
		double distSq = (x*x)+(y*y)+(z*z);
		
		rand.setSeed(getSeed(te.getPos()));
		float r = selectColor(rand, sin, cos);
		float g = selectColor(rand, sin, cos);
		float b = selectColor(rand, sin, cos);
		
		
		GlStateManager.color(r, g, b);
		
		
		float oldX = OpenGlHelper.lastBrightnessX;
		float oldY = OpenGlHelper.lastBrightnessY;
		
		float xOfs = (sin-0.5f)/3;
		float yOfs = 0.15f;
		float zOfs = (cos-0.5f)/3;
		float a = 0.1f;
		
		int res = 10;
		
		boolean transpose = false;//Minecraft.isFancyGraphicsEnabled() && distSq < 256;
		
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		
		if (transpose) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.disableAlpha();
		}
		GlStateManager.depthMask(false);
		
		TextureAtlasSprite floorGlowmap = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("fondue:blocks/obelisk_floor_glowmap");
		TextureAtlasSprite totemGlowmap = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("fondue:blocks/totem_glowmap");
		TextureAtlasSprite totemTopGlowmap = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("fondue:blocks/totem_top_glowmap");
		
		Tessellator tess = Tessellator.getInstance();
		VertexBuffer vb = tess.getBuffer();
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		if (ModuleObeliskClient.attuning == te && ModuleObeliskClient.attuneTicks < 40) {
			float prog = (ModuleObeliskClient.attuneTicks+partialTicks)/14f;
			
			float minU = totemGlowmap.getInterpolatedU(16/3f);
			float maxU = totemGlowmap.getInterpolatedU((16/3f)*2);
			float minV = totemGlowmap.getInterpolatedV(0);
			float maxV = totemGlowmap.getInterpolatedV(16/3f);
			
			float attuneA = 0.15f;
			
			if (ModuleObeliskClient.attuneTicks > 20) {
				attuneA *= 1-(((ModuleObeliskClient.attuneTicks+partialTicks)-20)/20f);
			}
			
			if (attuneA > 0) {
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.disableAlpha();
				GlStateManager.color(r, g, b, attuneA);
				int attuneRes = ModuleObeliskClient.attuneTicks*6;
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(x, y, z);
				for (int j = 0; j < 4; j++) {
					for (int i = ModuleObeliskClient.attuneTicks; i < attuneRes-ModuleObeliskClient.attuneTicks; i++) {
						float f = i/((float)attuneRes);
						vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						vb.pos(1+((prog/3f)*f), 1+((prog/3f)*f), -0.01-(prog*f)).tex(maxU, minV).endVertex();
						vb.pos(1+((prog/3f)*f), 0-((prog/3f)*f), -0.01-(prog*f)).tex(maxU, maxV).endVertex();
						vb.pos(0-((prog/3f)*f), 0-((prog/3f)*f), -0.01-(prog*f)).tex(minU, maxV).endVertex();
						vb.pos(0-((prog/3f)*f), 1+((prog/3f)*f), -0.01-(prog*f)).tex(minU, minV).endVertex();
						vb.setTranslation(0, 0, 0);
						tess.draw();
					}
					GlStateManager.rotate(90f, 0, 1, 0);
					GlStateManager.translate(-1, 0, 0);
				}
				GlStateManager.popMatrix();
				GlStateManager.color(r, g, b);
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();
			}
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(-2, -1.99, -2).tex(floorGlowmap.getMinU(), floorGlowmap.getMinV()).endVertex();
		vb.pos(-2, -1.99,  3).tex(floorGlowmap.getMinU(), floorGlowmap.getMaxV()).endVertex();
		vb.pos( 3, -1.99,  3).tex(floorGlowmap.getMaxU(), floorGlowmap.getMaxV()).endVertex();
		vb.pos( 3, -1.99, -2).tex(floorGlowmap.getMaxU(), floorGlowmap.getMinV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(-2+xOfs*f, -1.99+yOfs*f, -2+zOfs*f).tex(floorGlowmap.getMinU(), floorGlowmap.getMinV()).endVertex();
				vb.pos(-2+xOfs*f, -1.99+yOfs*f,  3+zOfs*f).tex(floorGlowmap.getMinU(), floorGlowmap.getMaxV()).endVertex();
				vb.pos( 3+xOfs*f, -1.99+yOfs*f,  3+zOfs*f).tex(floorGlowmap.getMaxU(), floorGlowmap.getMaxV()).endVertex();
				vb.pos( 3+xOfs*f, -1.99+yOfs*f, -2+zOfs*f).tex(floorGlowmap.getMaxU(), floorGlowmap.getMinV()).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(-1,  1, 1.01).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
		vb.pos(-1, -2, 1.01).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(2, -2, 1.01).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(2,  1, 1.01).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(-1+xOfs*f,  1+zOfs*f, 1.01+yOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
				vb.pos(-1+xOfs*f, -2+zOfs*f, 1.01+yOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(2+xOfs*f, -2+zOfs*f, 1.01+yOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(2+xOfs*f,  1+zOfs*f, 1.01+yOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(2,  1, -0.01).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
		vb.pos(2, -2, -0.01).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(-1, -2, -0.01).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(-1,  1, -0.01).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(2+xOfs*f,  1+zOfs*f, -0.01-yOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
				vb.pos(2+xOfs*f, -2+zOfs*f, -0.01-yOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(-1+xOfs*f, -2+zOfs*f, -0.01-yOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(-1+xOfs*f,  1+zOfs*f, -0.01-yOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(-0.01,  1, -1).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
		vb.pos(-0.01, -2, -1).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(-0.01, -2, 2).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(-0.01,  1, 2).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(-0.01-yOfs*f,  1+xOfs*f, 0+zOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
				vb.pos(-0.01-yOfs*f, -2+xOfs*f, 0+zOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(-0.01-yOfs*f, -2+xOfs*f, 1+zOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(-0.01-yOfs*f,  1+xOfs*f, 1+zOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(1.01,  1, 2).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
		vb.pos(1.01, -2, 2).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(1.01, -2, -1).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
		vb.pos(1.01,  1, -1).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(1.01+yOfs*f,  1+xOfs*f, 2+zOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMinV()).endVertex();
				vb.pos(1.01+yOfs*f, -2+xOfs*f, 2+zOfs*f).tex(totemGlowmap.getMaxU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(1.01+yOfs*f, -2+xOfs*f, -1+zOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMaxV()).endVertex();
				vb.pos(1.01+yOfs*f,  1+xOfs*f, -1+zOfs*f).tex(totemGlowmap.getMinU(), totemGlowmap.getMinV()).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(0, 1.01, 0).tex(totemTopGlowmap.getMinU(), totemTopGlowmap.getMinV()).endVertex();
		vb.pos(0, 1.01, 1).tex(totemTopGlowmap.getMinU(), totemTopGlowmap.getMaxV()).endVertex();
		vb.pos(1, 1.01, 1).tex(totemTopGlowmap.getMaxU(), totemTopGlowmap.getMaxV()).endVertex();
		vb.pos(1, 1.01, 0).tex(totemTopGlowmap.getMaxU(), totemTopGlowmap.getMinV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(0+xOfs*f, 1.01+yOfs*f, 0+zOfs*f).tex(totemTopGlowmap.getMaxU(), totemTopGlowmap.getMinV()).endVertex();
				vb.pos(0+xOfs*f, 1.01+yOfs*f, 1+zOfs*f).tex(totemTopGlowmap.getMaxU(), totemTopGlowmap.getMaxV()).endVertex();
				vb.pos(1+xOfs*f, 1.01+yOfs*f, 1+zOfs*f).tex(totemTopGlowmap.getMinU(), totemTopGlowmap.getMaxV()).endVertex();
				vb.pos(1+xOfs*f, 1.01+yOfs*f, 0+zOfs*f).tex(totemTopGlowmap.getMinU(), totemTopGlowmap.getMinV()).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, oldX, oldY);
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
	}

}
