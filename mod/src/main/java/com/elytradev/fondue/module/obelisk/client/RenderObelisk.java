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
import net.minecraft.util.math.MathHelper;

public class RenderObelisk extends TileEntitySpecialRenderer<TileEntityObelisk> {

	public static float getTime(TileEntityObelisk te, float partialTicks) {
		long l = te.getWorld().getTotalWorldTime();
		l += te.hashCode();
		return ((l%24000)+partialTicks)/20f;
	}
	
	public static long getSeed(TileEntityObelisk te) {
		long l = 31;
		l += (31L * te.getPos().getX());
		l += (31L * te.getPos().getY());
		l += (31L * te.getPos().getZ());
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
		
		float t = getTime(te, partialTicks);
		
		float sin = (MathHelper.sin(t)+2)/3;
		float cos = (MathHelper.cos(t)+2)/3;
		
		double distSq = (x*x)+(y*y)+(z*z);
		
		rand.setSeed(getSeed(te));
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
