package com.elytradev.fondue.module.obelisk.client;

import org.lwjgl.opengl.GL11;

import com.elytradev.fondue.module.obelisk.TileEntityObelisk;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderObelisk extends TileEntitySpecialRenderer<TileEntityObelisk> {

	private static final ResourceLocation FLOOR_GLOWMAP = new ResourceLocation("fondue", "textures/blocks/obelisk_floor_glowmap.png");
	private static final ResourceLocation TOTEM_GLOWMAP = new ResourceLocation("fondue", "textures/blocks/totem_glowmap.png");
	private static final ResourceLocation TOTEM_TOP_GLOWMAP = new ResourceLocation("fondue", "textures/blocks/totem_top_glowmap.png");
	
	public static float getTime(TileEntityObelisk te, float partialTicks) {
		long l = te.getWorld().getTotalWorldTime();
		l += te.hashCode();
		return ((l%24000)+partialTicks)/20f;
	}
	
	@Override
	public void renderTileEntityAt(TileEntityObelisk te, double x, double y, double z, float partialTicks, int destroyStage) {
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
		
		float t = getTime(te, partialTicks);
		
		float sin = (MathHelper.sin(t)+2)/3;
		float cos = (MathHelper.cos(t)+2)/3;
		
		boolean attuned = false;
		
		double distSq = (x*x)+(y*y)+(z*z);
		
		float r;
		float g;
		float b;
		
		if (attuned) {
			r = sin;
			g = cos;
			b = 0;
		} else {
			r = sin;
			g = 0;
			b = cos;
		}
		
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
		
		Tessellator tess = Tessellator.getInstance();
		VertexBuffer vb = tess.getBuffer();
		
		bindTexture(FLOOR_GLOWMAP);
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(-2, -1.99, -2).tex(0, 0).endVertex();
		vb.pos(-2, -1.99,  3).tex(0, 1).endVertex();
		vb.pos( 3, -1.99,  3).tex(1, 1).endVertex();
		vb.pos( 3, -1.99, -2).tex(1, 0).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(-2+xOfs*f, -1.99+yOfs*f, -2+zOfs*f).tex(0, 0).endVertex();
				vb.pos(-2+xOfs*f, -1.99+yOfs*f,  3+zOfs*f).tex(0, 1).endVertex();
				vb.pos( 3+xOfs*f, -1.99+yOfs*f,  3+zOfs*f).tex(1, 1).endVertex();
				vb.pos( 3+xOfs*f, -1.99+yOfs*f, -2+zOfs*f).tex(1, 0).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
		}
		
		bindTexture(TOTEM_GLOWMAP);
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(0,  1, 1.01).tex(0, 0).endVertex();
		vb.pos(0, -2, 1.01).tex(0, 1).endVertex();
		vb.pos(1, -2, 1.01).tex(1, 1).endVertex();
		vb.pos(1,  1, 1.01).tex(1, 0).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(0+xOfs*f,  1+zOfs*f, 1.01+yOfs*f).tex(0, 0).endVertex();
				vb.pos(0+xOfs*f, -2+zOfs*f, 1.01+yOfs*f).tex(0, 1).endVertex();
				vb.pos(1+xOfs*f, -2+zOfs*f, 1.01+yOfs*f).tex(1, 1).endVertex();
				vb.pos(1+xOfs*f,  1+zOfs*f, 1.01+yOfs*f).tex(1, 0).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(1,  1, -0.01).tex(1, 0).endVertex();
		vb.pos(1, -2, -0.01).tex(1, 1).endVertex();
		vb.pos(0, -2, -0.01).tex(0, 1).endVertex();
		vb.pos(0,  1, -0.01).tex(0, 0).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(1+xOfs*f,  1+zOfs*f, -0.01-yOfs*f).tex(1, 0).endVertex();
				vb.pos(1+xOfs*f, -2+zOfs*f, -0.01-yOfs*f).tex(1, 1).endVertex();
				vb.pos(0+xOfs*f, -2+zOfs*f, -0.01-yOfs*f).tex(0, 1).endVertex();
				vb.pos(0+xOfs*f,  1+zOfs*f, -0.01-yOfs*f).tex(0, 0).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(-0.01,  1, 0).tex(0, 0).endVertex();
		vb.pos(-0.01, -2, 0).tex(0, 1).endVertex();
		vb.pos(-0.01, -2, 1).tex(1, 1).endVertex();
		vb.pos(-0.01,  1, 1).tex(1, 0).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(-0.01-yOfs*f,  1+xOfs*f, 0+zOfs*f).tex(0, 0).endVertex();
				vb.pos(-0.01-yOfs*f, -2+xOfs*f, 0+zOfs*f).tex(0, 1).endVertex();
				vb.pos(-0.01-yOfs*f, -2+xOfs*f, 1+zOfs*f).tex(1, 1).endVertex();
				vb.pos(-0.01-yOfs*f,  1+xOfs*f, 1+zOfs*f).tex(1, 0).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(1.01,  1, 1).tex(1, 0).endVertex();
		vb.pos(1.01, -2, 1).tex(1, 1).endVertex();
		vb.pos(1.01, -2, 0).tex(0, 1).endVertex();
		vb.pos(1.01,  1, 0).tex(0, 0).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(1.01+yOfs*f,  1+xOfs*f, 1+zOfs*f).tex(1, 0).endVertex();
				vb.pos(1.01+yOfs*f, -2+xOfs*f, 1+zOfs*f).tex(1, 1).endVertex();
				vb.pos(1.01+yOfs*f, -2+xOfs*f, 0+zOfs*f).tex(0, 1).endVertex();
				vb.pos(1.01+yOfs*f,  1+xOfs*f, 0+zOfs*f).tex(0, 0).endVertex();
				vb.setTranslation(0, 0, 0);
				tess.draw();
				GlStateManager.color(r, g, b);
			}
			GlStateManager.depthMask(true);
		}
		
		bindTexture(TOTEM_TOP_GLOWMAP);
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(0, 1.01, 0).tex(0, 0).endVertex();
		vb.pos(0, 1.01, 1).tex(0, 1).endVertex();
		vb.pos(1, 1.01, 1).tex(1, 1).endVertex();
		vb.pos(1, 1.01, 0).tex(1, 0).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		if (transpose) {
			GlStateManager.depthMask(false);
			for (int i = 0; i < res; i++) {
				float f = i/(float)res;
				GlStateManager.color(r, g, b, a);
				vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
				vb.setTranslation(x, y, z);
				vb.pos(0+xOfs*f, 1.01+yOfs*f, 0+zOfs*f).tex(0, 0).endVertex();
				vb.pos(0+xOfs*f, 1.01+yOfs*f, 1+zOfs*f).tex(0, 1).endVertex();
				vb.pos(1+xOfs*f, 1.01+yOfs*f, 1+zOfs*f).tex(1, 1).endVertex();
				vb.pos(1+xOfs*f, 1.01+yOfs*f, 0+zOfs*f).tex(1, 0).endVertex();
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
