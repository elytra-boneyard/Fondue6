package com.elytradev.fondue.module.waypoints.client;

import java.util.Random;

import com.elytradev.fondue.module.obelisk.client.RenderObelisk;
import com.elytradev.fondue.module.waypoints.WaypointShape;
import com.elytradev.fondue.module.waypoints.WaypointStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class ObeliskCompassWidget extends BlockCompassWidget {

	private static final ResourceLocation WAYPOINT = new ResourceLocation("fondue", "textures/gui/waypoint.png");
	
	private static Random rand = new Random();
	
	public ObeliskCompassWidget(BlockPos location) {
		super(location);
	}
	

	@Override
	public void render() {
		float t = RenderObelisk.getTime(Minecraft.getMinecraft().world, location, Minecraft.getMinecraft().getRenderPartialTicks());
		
		float sin = (MathHelper.sin(t)+2)/3;
		float cos = (MathHelper.cos(t)+2)/3;
		
		rand.setSeed(RenderObelisk.getSeed(location));
		float r = RenderObelisk.selectColor(rand, sin, cos);
		float g = RenderObelisk.selectColor(rand, sin, cos);
		float b = RenderObelisk.selectColor(rand, sin, cos);
		
		GlStateManager.color(r, g, b);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(WAYPOINT);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, WaypointShape.OBELISK.ordinal()*8, WaypointStyle.SOLID.ordinal()*8, 8, 8, 72, 16);
	}

	@Override
	public int getWidth() {
		return 8;
	}
	
	@Override
	public double getFalloffSize() {
		return 16;
	}
	
	@Override
	public double getFalloffStart() {
		return 64;
	}

}
