package com.elytradev.fondue.module.waypoints.client;

import org.lwjgl.opengl.GL11;

import com.elytradev.fondue.module.waypoints.TileEntityWaypoint;
import com.elytradev.fruitphone.client.render.Rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderWaypoint extends TileEntitySpecialRenderer<TileEntityWaypoint> {

	@Override
	public void renderTileEntityAt(TileEntityWaypoint te, double x, double y, double z, float partialTicks, int destroyStage) {
		
		TextureAtlasSprite sideGlowmap = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("fondue:blocks/waypoint_side_glowmap");
		TextureAtlasSprite poleGlowmap = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("fondue:blocks/waypoint_pole_glowmap");
		
		Tessellator tess = Tessellator.getInstance();
		VertexBuffer vb = tess.getBuffer();
		
		GlStateManager.disableLighting();
		float oldX = OpenGlHelper.lastBrightnessX;
		float oldY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		
		GlStateManager.depthMask(false);
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Rendering.color3(te.color);
		
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.setTranslation(x, y, z);
		vb.pos(0, 1.01, 0).tex(poleGlowmap.getMinU(), poleGlowmap.getMinV()).endVertex();
		vb.pos(0, 1.01, 1).tex(poleGlowmap.getMinU(), poleGlowmap.getMaxV()).endVertex();
		vb.pos(1, 1.01, 1).tex(poleGlowmap.getMaxU(), poleGlowmap.getMaxV()).endVertex();
		vb.pos(1, 1.01, 0).tex(poleGlowmap.getMaxU(), poleGlowmap.getMinV()).endVertex();
		
		vb.pos(1, -0.01, 0).tex(poleGlowmap.getMaxU(), poleGlowmap.getMinV()).endVertex();
		vb.pos(1, -0.01, 1).tex(poleGlowmap.getMaxU(), poleGlowmap.getMaxV()).endVertex();
		vb.pos(0, -0.01, 1).tex(poleGlowmap.getMinU(), poleGlowmap.getMaxV()).endVertex();
		vb.pos(0, -0.01, 0).tex(poleGlowmap.getMinU(), poleGlowmap.getMinV()).endVertex();
		
		vb.pos(1.01, 1, 0).tex(sideGlowmap.getMinU(), sideGlowmap.getMinV()).endVertex();
		vb.pos(1.01, 1, 1).tex(sideGlowmap.getMaxU(), sideGlowmap.getMinV()).endVertex();
		vb.pos(1.01, 0, 1).tex(sideGlowmap.getMaxU(), sideGlowmap.getMaxV()).endVertex();
		vb.pos(1.01, 0, 0).tex(sideGlowmap.getMinU(), sideGlowmap.getMaxV()).endVertex();
		
		vb.pos(-0.01, 0, 0).tex(sideGlowmap.getMinU(), sideGlowmap.getMaxV()).endVertex();
		vb.pos(-0.01, 0, 1).tex(sideGlowmap.getMaxU(), sideGlowmap.getMaxV()).endVertex();
		vb.pos(-0.01, 1, 1).tex(sideGlowmap.getMaxU(), sideGlowmap.getMinV()).endVertex();
		vb.pos(-0.01, 1, 0).tex(sideGlowmap.getMinU(), sideGlowmap.getMinV()).endVertex();
		
		vb.pos(0, 0, 1.01).tex(sideGlowmap.getMinU(), sideGlowmap.getMaxV()).endVertex();
		vb.pos(1, 0, 1.01).tex(sideGlowmap.getMaxU(), sideGlowmap.getMaxV()).endVertex();
		vb.pos(1, 1, 1.01).tex(sideGlowmap.getMaxU(), sideGlowmap.getMinV()).endVertex();
		vb.pos(0, 1, 1.01).tex(sideGlowmap.getMinU(), sideGlowmap.getMinV()).endVertex();
		
		vb.pos(0, 1, -0.01).tex(sideGlowmap.getMinU(), sideGlowmap.getMinV()).endVertex();
		vb.pos(1, 1, -0.01).tex(sideGlowmap.getMaxU(), sideGlowmap.getMinV()).endVertex();
		vb.pos(1, 0, -0.01).tex(sideGlowmap.getMaxU(), sideGlowmap.getMaxV()).endVertex();
		vb.pos(0, 0, -0.01).tex(sideGlowmap.getMinU(), sideGlowmap.getMaxV()).endVertex();
		vb.setTranslation(0, 0, 0);
		tess.draw();
		
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, oldX, oldY);
		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		
		super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
	}
	
}
